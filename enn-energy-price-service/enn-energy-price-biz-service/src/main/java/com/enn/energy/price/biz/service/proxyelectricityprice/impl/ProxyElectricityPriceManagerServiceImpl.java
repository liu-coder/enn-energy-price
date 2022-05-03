package com.enn.energy.price.biz.service.proxyelectricityprice.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.enn.energy.price.biz.service.bo.proxyprice.*;
import com.enn.energy.price.biz.service.convertMapper.*;
import com.enn.energy.price.biz.service.proxyelectricityprice.ProxyElectricityPriceManagerService;
import com.enn.energy.price.common.enums.BoolLogic;
import com.enn.energy.price.common.enums.ResponseEum;
import com.enn.energy.price.common.enums.StateEum;
import com.enn.energy.price.dal.mapper.ext.proxyPrice.ElectricityPriceEquipmentExtMapper;
import com.enn.energy.price.dal.mapper.ext.proxyPrice.ElectricityPriceStructureExtMapper;
import com.enn.energy.price.dal.mapper.ext.proxyPrice.ElectricityPriceVersionExtMapper;
import com.enn.energy.price.dal.mapper.mbg.*;
import com.enn.energy.price.dal.po.mbg.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import top.rdfa.framework.biz.ro.RdfaResult;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 代购电价service实现
 *
 * @author sunjidong
 * @date 2022/5/1
 **/
@Service
@Slf4j
public class ProxyElectricityPriceManagerServiceImpl implements ProxyElectricityPriceManagerService {

    @Autowired
    ElectricityPriceVersionExtMapper electricityPriceVersionExtMapper;

    @Autowired
    ElectricityPriceStructureExtMapper electricityPriceStructureExtMapper;

    @Autowired
    ElectricityPriceEquipmentExtMapper electricityPriceEquipmentExtMapper;

    @Resource
    ElectricityPriceVersionMapper electricityPriceVersionMapper;

    @Resource
    ElectricityPriceStructureMapper electricityPriceStructureMapper;

    @Resource
    ElectricityPriceEquipmentMapper electricityPriceEquipmentMapper;

    @Resource
    private ElectricityPriceStructureRuleMapper priceStructureRuleMapper;

    @Resource
    private ElectricitySeasonSectionMapper seasonSectionMapper;

    @Resource
    private ElectricityTimeSectionMapper timeSectionMapper;

    @Resource
    private ElectricityPriceRuleMapper priceRuleMapper;

    @Resource
    private ElectricityPriceMapper priceMapper;

    /**
     * @describtion 创建版本
     * @author sunjidong
     * @date 2022/5/2 14:48
     * @param priceVersionStructuresCreateBO
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createPriceVersionStructures(ElectricityPriceVersionStructuresCreateBO priceVersionStructuresCreateBO) {
        //创建时间
        DateTime createTime = DateUtil.date();
        //1、创建版本
        ElectricityPriceVersionCreateBO priceVersionCreateBO = priceVersionStructuresCreateBO.getPriceVersionCreateBO();
        String versionId = IdUtil.simpleUUID();
        ElectricityPriceVersion priceVersionPO = ElectricityPriceVersionCreateBOMapper.INSTANCE.priceVersionCreateBOToPO(priceVersionCreateBO);
        priceVersionPO.setVersionId(versionId);
        priceVersionPO.setState(0);
        electricityPriceVersionMapper.insert(priceVersionPO);
        //2、创建所有体系以及对应的季节分时区间以及电价规则
        ValidationList<ElectricityPriceStructureAndRuleAndSeasonCreateBO> priceStructureAndRuleAndSeasonCreateBOList = priceVersionStructuresCreateBO.getPriceStructureAndRuleAndSeasonCreateBOList();
        priceStructureAndRuleAndSeasonCreateBOList.forEach(priceStructureAndRuleAndSeasonCreateBO -> {
            List<ElectricityPriceStructureRule> priceStructureRuleList = new ArrayList<>();
            List<ElectricityPriceRule> priceRuleList = new ArrayList<>();
            //2.1、先创建具体的体系
            ElectricityPriceStructureCreateBO priceStructureCreateBO = priceStructureAndRuleAndSeasonCreateBO.getPriceStructureCreateBO();
            ElectricityPriceStructure electricityPriceStructure = ElectricityPriceStructureCreateBOMapper.INSTANCE.priceStructureCreateBOToPO(priceStructureCreateBO);
            String structureId = IdUtil.simpleUUID();
            electricityPriceStructure.setStructureId(structureId);
            electricityPriceStructure.setState(BoolLogic.NO.getCode());
            electricityPriceStructure.setVersionId(versionId);
            electricityPriceStructure.setCreateTime(createTime);
            electricityPriceStructureMapper.insert(electricityPriceStructure);
            //2.2、创建体系季节对应的三要素、季节以及分时
            ValidationList<ElectricityPriceStructureRuleCreateBO> priceStructureRuleCreateBOList = priceStructureAndRuleAndSeasonCreateBO.getPriceStructureRuleCreateBOList();
            priceStructureRuleCreateBOList.forEach(priceStructureRuleCreateBO -> {
                ElectricityPriceStructureRule priceStructureRule = ElectricityPriceStructureRuleCreateBOMapper.INSTANCE.priceStructureRuleCreateBOToPO(priceStructureRuleCreateBO);
                String structureRuleId = IdUtil.simpleUUID();
                priceStructureRule.setStructureId(structureId);
                priceStructureRule.setStructureRuleId(structureRuleId);
                priceStructureRule.setState(BoolLogic.NO.getCode());
                priceStructureRule.setCreateTime(createTime);
                priceStructureRuleMapper.insert(priceStructureRule);
                priceStructureRuleList.add(priceStructureRule);
                //构建季节区间
                ValidationList<ElectricitySeasonCreateBO> seasonCreateBOList = priceStructureRuleCreateBO.getSeasonCreateBOList();
                seasonCreateBOList.forEach(seasonCreateBO -> {
                    String seasonName = seasonCreateBO.getSeasonSectionName();
                    String seasonSectionId = IdUtil.simpleUUID();
                    //季节区间
                    ValidationList<ElectricitySeasonSectionCreateBO> seasonSectionCreateBOList = seasonCreateBO.getSeasonSectionCreateBOList();
                    seasonSectionCreateBOList.forEach(seasonSectionCreateBO -> {
                        ElectricitySeasonSection seasonSection = ElectricitySeasonSectionCreateBOMapper.INSTANCE.seasonSectionCreateBOToPO(seasonSectionCreateBO);
                        seasonSection.setSeasonSectionId(seasonSectionId);
                        seasonSection.setStructureId(structureId);
                        seasonSection.setStructureRuleId(structureRuleId);
                        seasonSection.setSeasonSectionName(seasonName);
                        seasonSection.setState(BoolLogic.NO.getCode());
                        seasonSection.setCreateTime(createTime);
                        seasonSectionMapper.insert(seasonSection);
                    });
                    //构建分时区间
                    ValidationList<ElectricityTimeSectionCreateBO> timeSectionCreateBOList = seasonCreateBO.getTimeSectionCreateBOList();
                    timeSectionCreateBOList.forEach(timeSectionCreateBO -> {
                        ElectricityTimeSection timeSection = ElectricityTimeSectionCreateBOMapper.INSTANCE.timeSectionCreateBOToPO(timeSectionCreateBO);
                        String timeSectionId = IdUtil.simpleUUID();
                        timeSection.setSeasonSectionId(seasonSectionId);
                        timeSection.setTimeSectionId(timeSectionId);
                        timeSection.setState(BoolLogic.NO.getCode());
                        timeSection.setCreateTime(createTime);
                        timeSectionMapper.insert(timeSection);
                    });
                });
            });

            //2.3、创建电价规则以及电价明细
            ValidationList<ElectricityPriceRuleCreateBO> priceRuleCreateBOList = priceStructureAndRuleAndSeasonCreateBO.getPriceRuleCreateBOList();
            priceRuleCreateBOList.forEach(priceRuleCreateBO -> {
                //构建电价规则
                ElectricityPriceRule electricityPriceRule = ElectricityPriceRuleCreateBOMapper.INSTANCE.priceRuleCreateBOToPO(priceRuleCreateBO);
                String priceRuleId = IdUtil.simpleUUID();
                electricityPriceRule.setRuleId(priceRuleId);
                electricityPriceRule.setVersionId(versionId);
                electricityPriceRule.setStructureId(structureId);
                electricityPriceRule.setTenantId(priceVersionStructuresCreateBO.getPriceVersionCreateBO().getTenantId());
                electricityPriceRule.setTenantName(priceVersionStructuresCreateBO.getPriceVersionCreateBO().getTenantName());
                electricityPriceRule.setState(0);
                electricityPriceRule.setCreateTime(createTime);
                priceRuleMapper.insert(electricityPriceRule);
                priceRuleList.add(electricityPriceRule);
                //构建电价明细
                ElectricityPriceCreateBO electricityPriceCreateBO = priceRuleCreateBO.getElectricityPriceCreateBO();
                ElectricityPrice electricityPrice = ElectricityPriceCreateBOMapper.INSTANCE.priceReqCreateBOToPO(electricityPriceCreateBO);
                String detailId = IdUtil.simpleUUID();
                electricityPrice.setDetailId(detailId);
                electricityPrice.setRuleId(priceRuleId);
                electricityPrice.setTenantId(priceVersionStructuresCreateBO.getPriceVersionCreateBO().getTenantId());
                electricityPrice.setTenantName(priceVersionStructuresCreateBO.getPriceVersionCreateBO().getTenantName());
                electricityPrice.setTransformerCapacityPrice(priceRuleCreateBO.getTransformerCapacityPrice());
                electricityPrice.setMaxCapacityPrice(priceRuleCreateBO.getMaxCapacityPrice());
                electricityPrice.setState(0);
                electricityPrice.setCreateTime(createTime);
                priceMapper.insert(electricityPrice);
            });
            generatePriceRuleDetail(priceStructureRuleList, priceRuleList);
        });
        return Boolean.TRUE;
    }

    /**
     * @describtion 通过匹配季节三要素与规则三要素，生成规则电价明细
     * @author sunjidong
     * @date 2022/5/3 9:07
     * @param priceStructureRuleList,priceRuleList
     */
    private void generatePriceRuleDetail(List<ElectricityPriceStructureRule> priceStructureRuleList,
                                         List<ElectricityPriceRule> priceRuleList){
        List<ElectricityPriceRule> toBeUpdatedPriceRuleList = priceRuleList.stream().filter(priceRule -> {
            String industry = priceRule.getIndustry();
            String strategy = priceRule.getStrategy();
            String voltageLevel = priceRule.getVoltageLevel();
            for (ElectricityPriceStructureRule priceStructureRule : priceStructureRuleList) {
                String industries = priceStructureRule.getIndustries();
                String strategies = priceStructureRule.getStrategies();
                String voltageLevels = priceStructureRule.getVoltageLevels();
                if (industries.contains(industry) && strategies.contains(strategy) && voltageLevels.contains(voltageLevel)) {
                    priceRule.setStructureRuleId(priceStructureRule.getStructureRuleId());
                    return true;
                }
            }
            return false;
        }).collect(Collectors.toList());
        //更新电价规则
        toBeUpdatedPriceRuleList.forEach(toBeUpdatedPriceRule -> {
            priceRuleMapper.insert(toBeUpdatedPriceRule);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RdfaResult updatePriceVersion(ElectricityPriceVersionUpdateBO electricityPriceVersionUpdateBO) {
        //2.1查询当前版本下的体系列表,根据传入值id判断是否是历史数据，如果是，则进行修改,缺少的部分进行校验是否绑定设备,多的部分则进行绑定添加
        //2.1.1 查询当前版本判断当前版本所处时间段,历史与当前版本有设备绑定关系需要校验区域,未来版本可以更改区域
        //校验体系名称是否重复
        if(judgeStructureNameDuplicate(electricityPriceVersionUpdateBO)){
            return RdfaResult.fail( ResponseEum.VERSION_STRUCTURE_NAME_DUPLICATE.getCode(),ResponseEum.VERSION_STRUCTURE_NAME_DUPLICATE.getMsg() );
        }
        //区分体系变化
        LinkedMultiValueMap<String, Object> stringObjectLinkedMultiValueMap = judgeStructureChange( electricityPriceVersionUpdateBO );

        //判断版本时间段,过去以及正在使用的版本
        if(DateUtil.compare(DateUtil.parse(electricityPriceVersionUpdateBO.getStartDate(),
                DatePattern.NORM_DATE_PATTERN ),DateUtil.parse(DateUtil.today(), DatePattern.NORM_DATE_PATTERN))<0){
            //获取到删除与修改的体系的id,进行判断是否绑定了设备
            List<ElectricityPriceEquipment> electricityPriceEquipments = electricityPriceEquipmentExtMapper.queryEquipmentBinding( String.valueOf( electricityPriceVersionUpdateBO.getId() ) );
            //判断删除的版本是否绑定了设备
            List<Object> delete = stringObjectLinkedMultiValueMap.get( "delete" );
            List<String> existEquipmentIds= electricityPriceEquipments.stream().map( ElectricityPriceEquipment::getStructureId ).collect( Collectors.toList() );
            if(!CollectionUtils.isEmpty( delete )){
                List<String> deleteIdList = delete.stream().map( t -> ((ElectricityPriceStructureUpdateBO) t).getId() ).collect( Collectors.toList() );
                if(deleteIdList.stream().anyMatch( existEquipmentIds::contains )){
                    return RdfaResult.fail( ResponseEum.EXIST_STRUCTURE_EQUIPMENT.getCode(),ResponseEum.EXIST_STRUCTURE_EQUIPMENT.getMsg() );
                }
            }

            //判断修改的体系的区域是否发生变化 1.区域发生变化新增了区域 2.删除了区域
            List<Object> update = stringObjectLinkedMultiValueMap.get( "update" );
            if(!CollectionUtils.isEmpty( update )){
                List<String> updateIdList = update.stream().map( t -> ((ElectricityPriceStructureUpdateBO) t).getId() ).collect( Collectors.toList() );
                //对修改过的并且绑定设备的体系的区域进行比较，没绑定设备的体系直接进行修改
                List<String> updateAndBindingList = updateIdList.stream().filter( existEquipmentIds::contains ).collect( Collectors.toList() );
                //获取传入的需要修改体系且已经绑定区域的体系信息
                List<ElectricityPriceStructureUpdateBO> nowList = electricityPriceVersionUpdateBO.getElectricityPriceStructureUpdateBOList().stream().filter( t -> updateAndBindingList.contains( t.getId() ) ).collect( Collectors.toList() );
                //查询需要比对区域信息的体系信息，进行区域比对
                List<ElectricityPriceStructure> electricityPriceStructures = electricityPriceStructureExtMapper.batchQueryStructureListByIds( updateAndBindingList );
                List<String> noCoverStructureIds=new ArrayList<>();
                electricityPriceStructures.forEach( t->{
                    nowList.forEach( f->{
                        if(f.getId().equals(String.valueOf(t.getId()))){
                            //原来的体系市区域列表
                            String cityCodesBefore = t.getCityCodes();
                            //现在传入的市区域列表
                            String cityCodesNow = f.getCityCodes();
                            Boolean isCover = judgeCityCover( cityCodesBefore, cityCodesNow );
                            if(!isCover){
                                noCoverStructureIds.add( t.getStructureId() );
                            }
                        }
                    } );

                } );

                if(noCoverStructureIds.size()>0){
                    return RdfaResult.fail( ResponseEum.UPDATE_STRUCTURE_AREA_NO_COVER.getCode(),ResponseEum.UPDATE_STRUCTURE_AREA_NO_COVER.getMsg() );
                }
            }
        }
        //统一操作 2.1更新电价版本名称 2.2更新体系信息,包含删除体系添加体系信息
        //2.1
        ElectricityPriceVersion electricityPriceVersion = new ElectricityPriceVersion();
        electricityPriceVersion.setId(electricityPriceVersionUpdateBO.getId());
        electricityPriceVersion.setVersionName(electricityPriceVersion.getVersionName());
        electricityPriceVersion.setUpdator(electricityPriceVersionUpdateBO.getTenantId()  );
        electricityPriceVersion.setUpdateTime( DateUtil.date());
        electricityPriceVersion.setState( StateEum.NORMAL.getValue());
        int i = electricityPriceVersionExtMapper.updateElectricityPriceVersionById( electricityPriceVersion );
        if(i==0){
            return RdfaResult.fail( ResponseEum.VERSION_UPDATE_FAIL.getCode(),ResponseEum.VERSION_UPDATE_FAIL.getMsg() );
        }
        return null;
        //2.2
    }

    /**
     * 判断现传入的体系区域是否覆盖之前的体系区域
     * @param cityCodesBefore
     * @param cityCodesNow
     * @return
     */
    private Boolean judgeCityCover(String cityCodesBefore, String cityCodesNow) {

        return false;
    }


    /**
     * 校验版本下是否存在重复体系名称
     * @param electricityPriceVersionUpdateBO
     * @return true 存在重复 false 不存在
     */
    Boolean judgeStructureNameDuplicate(ElectricityPriceVersionUpdateBO electricityPriceVersionUpdateBO){
        int size = electricityPriceVersionUpdateBO.getElectricityPriceStructureUpdateBOList().stream().map( ElectricityPriceStructureUpdateBO::getStructureName ).collect( Collectors.toSet() ).size();
        if(electricityPriceVersionUpdateBO.getElectricityPriceStructureUpdateBOList().size()>size){
            return true;
        }
        return false;
    }


    LinkedMultiValueMap<String,Object> judgeStructureChange(ElectricityPriceVersionUpdateBO electricityPriceVersionUpdateBO){
        //根据版本id查找当前版本下的所有体系id,与现有进行比较,判断出哪些删除了,哪些修改了,哪些新增了
        List<ElectricityPriceStructure> electricityPriceStructures = electricityPriceStructureExtMapper.queryListByVersionId( String.valueOf( electricityPriceVersionUpdateBO.getId() ) );
        LinkedMultiValueMap<String,Object> map= new LinkedMultiValueMap<>();
        List<ElectricityPriceStructureUpdateBO> electricityPriceStructureUpdateBOList = electricityPriceVersionUpdateBO.getElectricityPriceStructureUpdateBOList();

        //新增的体系
        electricityPriceStructureUpdateBOList.forEach( t->{
            if(!Optional.ofNullable(t.getId()).isPresent()){
                map.add( "add",t );
            }
        } );
        //删除与修改的体系
        List<String> nowIds = electricityPriceStructureUpdateBOList.stream().map( ElectricityPriceStructureUpdateBO::getId ).collect( Collectors.toList() );
        electricityPriceStructures.forEach( t->{
            if(nowIds.contains( String.valueOf(t.getId()) )){
                ElectricityPriceStructureUpdateBO electricityPriceStructureUpdateBO = electricityPriceStructureUpdateBOList.stream().filter( f -> f.getId().equals( t.getId() ) ).findFirst().get();
                //todo 体系修改,  历史与当前版本 1.判断体系是否绑定了设备 2.体系区域是否发生修改


                map.add("update",electricityPriceStructureUpdateBO );
            }else{
                map.add( "delete",t.getId() );
            }

        } );
        return map;
    }
}