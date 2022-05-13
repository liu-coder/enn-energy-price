package com.enn.energy.price.biz.service.proxyelectricityprice.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.enn.energy.price.biz.service.bo.ElectricityPriceDictionaryBO;
import com.enn.energy.price.biz.service.bo.proxyprice.*;
import com.enn.energy.price.biz.service.convertMapper.ElectricityPriceVersionBOConvertMapper;
import com.enn.energy.price.biz.service.convertMapper.ElectricityPriceVersionUpdateBOConverMapper;
import com.enn.energy.price.biz.service.proxyelectricityprice.ProxyElectricityPriceManagerService;
import com.enn.energy.price.common.constants.CommonConstant;
import com.enn.energy.price.common.enums.BoolLogic;
import com.enn.energy.price.common.enums.ChangeTypeEum;
import com.enn.energy.price.common.error.ErrorCodeEnum;
import com.enn.energy.price.common.utils.BeanUtil;
import com.enn.energy.price.dal.mapper.ext.ExtElectricityPriceDictionaryMapper;
import com.enn.energy.price.dal.mapper.ext.proxyprice.*;
import com.enn.energy.price.dal.mapper.mbg.*;
import com.enn.energy.price.dal.po.ext.ElectricityPriceDetailPO;
import com.enn.energy.price.dal.po.mbg.*;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.rdfa.framework.biz.ro.RdfaResult;
import top.rdfa.framework.concurrent.redis.lock.RedissonRedDisLock;
import javax.annotation.Resource;
import java.util.*;
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
    ElectricityPriceVersionCustomMapper electricityPriceVersionCustomMapper;

    @Autowired
    ElectricityPriceStructureRuleCustomMapper electricityPriceStructureRuleCustomMapper;

    @Autowired
    ElectricityPriceStructureCustomMapper electricityPriceStructureCustomMapper;

    @Autowired
    ElectricityPriceEquipmentCustomMapper electricityPriceEquipmentCustomMapper;

    @Autowired
    ElectricityPriceSeasonSectionCustomMapper electricityPriceSeasonSectionCustomMapper;

    @Autowired
    ElectricityTimeSectionCustomMapper electricityTimeSectionCustomMapper;

    @Autowired
    ElectricityPriceRuleCustomMapper electricityPriceRuleCustomMapper;

    @Autowired
    ElectricityPriceCustomMapper electricityPriceCustomMapper;

    @Resource
    ExtElectricityPriceDictionaryMapper electricityPriceDictionaryCustomMapper;

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

    @Resource
    private RedissonRedDisLock redDisLock;


    @Value("${system.tenantId}")
    private String tenantId;

    @Value("${system.tenantName}")
    private String tenantName;

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
        ElectricityPriceVersion priceVersionPO = ElectricityPriceVersionBOConvertMapper.INSTANCE.priceVersionCreateBOToPO(priceVersionCreateBO);
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
            ElectricityPriceStructure electricityPriceStructure = ElectricityPriceVersionBOConvertMapper.INSTANCE.priceStructureCreateBOToPO(priceStructureCreateBO);
            String structureId = IdUtil.simpleUUID();
            electricityPriceStructure.setStructureId(structureId);
            electricityPriceStructure.setState(BoolLogic.NO.getCode());
            electricityPriceStructure.setVersionId(versionId);
            electricityPriceStructure.setCreateTime(createTime);
            electricityPriceStructureMapper.insert(electricityPriceStructure);
            //2.2、创建体系季节对应的三要素、季节以及分时
            ValidationList<ElectricityPriceStructureRuleCreateBO> priceStructureRuleCreateBOList = priceStructureAndRuleAndSeasonCreateBO.getPriceStructureRuleCreateBOList();
            priceStructureRuleCreateBOList.forEach(priceStructureRuleCreateBO -> {
                ElectricityPriceStructureRule priceStructureRule = ElectricityPriceVersionBOConvertMapper.INSTANCE.priceStructureRuleCreateBOToPO(priceStructureRuleCreateBO);
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
                        ElectricitySeasonSection seasonSection = ElectricityPriceVersionBOConvertMapper.INSTANCE.seasonSectionCreateBOToPO(seasonSectionCreateBO);
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
                        ElectricityTimeSection timeSection = ElectricityPriceVersionBOConvertMapper.INSTANCE.timeSectionCreateBOToPO(timeSectionCreateBO);
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
                ElectricityPriceRule electricityPriceRule = ElectricityPriceVersionBOConvertMapper.INSTANCE.priceRuleCreateBOToPO(priceRuleCreateBO);
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
                ElectricityPrice electricityPrice = ElectricityPriceVersionBOConvertMapper.INSTANCE.priceCreateBOToPO(electricityPriceCreateBO);
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
        //先找到默认类型
        ElectricityPriceStructureRule defaultPriceStructureRule = priceStructureRuleList.stream().filter(priceStructureRule -> {
            if (CommonConstant.DEFAULT_TYPE.equals(priceStructureRule.getIndustries())) {
                return true;
            }
            return false;
        }).collect(Collectors.toList()).get(0);

        List<ElectricityPriceRule> toBeUpdatedPriceRuleList = priceRuleList.stream().map(priceRule -> {
            String industry = priceRule.getIndustry();
            String strategy = priceRule.getStrategy();
            String voltageLevel = priceRule.getVoltageLevel();
            boolean matchFail = true;
            for (ElectricityPriceStructureRule priceStructureRule : priceStructureRuleList) {
                String industries = priceStructureRule.getIndustries();
                String strategies = priceStructureRule.getStrategies();
                String voltageLevels = priceStructureRule.getVoltageLevels();
                if (industries.contains(industry) && strategies.contains(strategy) && voltageLevels.contains(voltageLevel)) {
                    priceRule.setStructureRuleId(priceStructureRule.getStructureRuleId());
                    matchFail = false;
                    break;
                }
            }
            if(matchFail){
                priceRule.setStructureRuleId(defaultPriceStructureRule.getStructureRuleId());
            }
            return priceRule;
        }).collect(Collectors.toList());

        //更新电价规则
        toBeUpdatedPriceRuleList.forEach(toBeUpdatedPriceRule -> {
            priceRuleMapper.insert(toBeUpdatedPriceRule);
        });
    }







    @Override
    @Transactional(rollbackFor = Exception.class)
    public RdfaResult<Boolean> updatePriceVersion(ElectricityPriceVersionUpdateBO electricityPriceVersionUpdateBO) {
        //1.根据变更类型判断出体系的变更状态
        //1.1根据是否有id区分是否新增
        List<ElectricityPriceStructureUpdateBO> electricityPriceStructureUpdateBOList = electricityPriceVersionUpdateBO.getElectricityPriceStructureUpdateBOList();
        List<ElectricityPriceStructureUpdateBO> addCollect = electricityPriceStructureUpdateBOList.stream().filter( t -> StringUtils.isEmpty( String.valueOf( t.getId() ) ) ).collect( Collectors.toList() );
        //1.2根据changeType判断是否修改
        List<ElectricityPriceStructureUpdateBO> updateCollect = electricityPriceStructureUpdateBOList.stream().filter( t -> t.getChangeType().equals( ChangeTypeEum.UPDATE.getType() ) ).collect( Collectors.toList() );
        //1.3查询版本下面的体系列表比对哪些缺失
        List<Long> deleteIdList = queryDeleteStructureIds( electricityPriceStructureUpdateBOList, electricityPriceVersionUpdateBO.getId() );
        //2.1 增加体系直接增加
        addCollect.forEach( t->{
            createStructure(t,electricityPriceVersionUpdateBO.getId());
        } );
        //2.2 删除体系直接删除
        deleteIdList.forEach( this::deleteStructure );
        //2.3 修改体系
        updateCollect.forEach( t->{
            /*updateStructure(t,electricityPriceVersionUpdateBO.getId());*/
            updateStructurePlan3( t,electricityPriceVersionUpdateBO.getId() );

        } );
        return RdfaResult.success( true );

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RdfaResult<Boolean> deletePriceVersion(ElectricityPriceVersionDeleteBO electricityPriceVersionDeleteBO) {
        //根据电价版本id查询电价体系id
        Map<String, Object> map=new HashMap<>();
        map.put( "versionId", electricityPriceVersionDeleteBO.getId());
        map.put( "state",BoolLogic.NO.getCode() );
        List<ElectricityPriceStructure> electricityPriceStructures = electricityPriceStructureCustomMapper.queryListByConditions( map );
        //根据电价体系id删除
        electricityPriceStructures.forEach( t->{
            deleteStructure( t.getId() );
        } );
        //根据版本id删除电价体系与设备的关联关系
        electricityPriceEquipmentCustomMapper.deleteEquipmentBindingByVersionId(electricityPriceVersionDeleteBO.getId());
        //查询当前版本的上个版本
        ElectricityPriceVersion electricityPriceVersion=new ElectricityPriceVersion();
        electricityPriceVersion.setProvinceCode( electricityPriceVersionDeleteBO.getProvinceCode() );
        electricityPriceVersion.setStartDate( DateUtil.parse(electricityPriceVersionDeleteBO.getStartDate(), DatePattern.NORM_DATE_PATTERN) );
        electricityPriceVersion.setEndDate(  DateUtil.parse(electricityPriceVersionDeleteBO.getEndDate(), DatePattern.NORM_DATE_PATTERN));
        ElectricityPriceVersion beforePriceVersion = electricityPriceVersionCustomMapper.queryBeforePriceVersion( electricityPriceVersion );
        //更新上一个版本 并删除当前版本
        beforePriceVersion.setEndDate( DateUtil.parse(electricityPriceVersionDeleteBO.getEndDate(), DatePattern.NORM_DATE_PATTERN) );
        beforePriceVersion.setUpdator( tenantId );
        beforePriceVersion.setUpdateTime( DateUtil.date() );
        electricityPriceVersionMapper.updateByPrimaryKey( beforePriceVersion );
        HashMap<String, Object> versionUpdateMap = new HashMap<>();
        versionUpdateMap.put( "id", electricityPriceVersionDeleteBO.getId());
        versionUpdateMap.put( "afterState", BoolLogic.YES.getCode());
        versionUpdateMap.put( "state", BoolLogic.NO.getCode());
        versionUpdateMap.put( "updator",tenantId );
        versionUpdateMap.put( "updateTime",DateUtil.date() );
        electricityPriceVersionCustomMapper.updateElectricityPriceVersionCondition(versionUpdateMap);
        return RdfaResult.success(true);
    }

    @Override
    public List<ElectricityPriceVersionBO> queryPriceVersionList(String provinceCode) {
        HashMap<String, Object> map = new HashMap<>();
        map.put( "provinceCode",provinceCode );
        map.put( "state",BoolLogic.NO.getCode() );
        List<ElectricityPriceVersion> electricityPriceVersions = electricityPriceVersionCustomMapper.queryPriceVersionList( map );
        if(CollectionUtil.isEmpty(electricityPriceVersions  )){
            return null;
        }
        List<ElectricityPriceVersionBO> electricityPriceVersionBOs = ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.electricityPriceVersionListPOToBO( electricityPriceVersions );
        return electricityPriceVersionBOs;
    }

    @Override
    public List<ElectricityPriceStructureBO> queryPriceVersionStructureList(String versionId) {
        Map<String, Object> map = new HashMap<>();
        map.put("versionId",versionId);
        map.put( "state",BoolLogic.NO.getCode() );
        List<ElectricityPriceStructure> electricityPriceStructures = electricityPriceStructureCustomMapper.queryListByConditions( map );
        if(CollectionUtil.isEmpty( electricityPriceStructures )){
            return null;
        }
        List<ElectricityPriceStructureBO> electricityPriceStructureBOS = ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.electricityPriceStructurePOListToBOList( electricityPriceStructures );
        return electricityPriceStructureBOS;
    }

    @Override
    public ElectricityPriceStructureDetailBO getStructureDetail(String structuredId) {
        //根据体系id获取体系信息
        Map<String,Object> map=new HashMap<>();
        map.put( "id", structuredId);
        map.put( "state",BoolLogic.NO.getCode() );
        List<ElectricityPriceStructure> electricityPriceStructures = electricityPriceStructureCustomMapper.queryListByConditions( map );
        if(CollectionUtil.isEmpty( electricityPriceStructures )){
            return null;
        }
        ElectricityPriceStructure electricityPriceStructure = electricityPriceStructures.get(0);
        ElectricityPriceStructureDetailBO electricityPriceStructureDetailBO = ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.ElectricityPriceStructurePOTOElectricityPriceStructureDetailBO( electricityPriceStructure );
        //创建体系规则详情
        List<ElectricityPriceStructureRuleDetailBO> electricityPriceStructureRuleDetailBOS=new ArrayList<>();
        //根据体系id获取体系规则列表
        Map<String,Object> structureRuleQueryMap=new HashMap<>();
        structureRuleQueryMap.put( "structureId", structuredId);
        structureRuleQueryMap.put( "state", BoolLogic.NO.getCode());
        List<ElectricityPriceStructureRule> electricityPriceStructureRules = electricityPriceStructureRuleCustomMapper.queryElectricityPriceRulesByCondition( structureRuleQueryMap );
        //规则列表组装
        electricityPriceStructureRules.forEach( t->{
            //创建体系规则详情与季节列表
            ElectricityPriceStructureRuleDetailBO electricityPriceStructureRuleDetailBO = ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.ElectricityPriceStructureRulePOTOBO( t);
            List<ElectricityPriceSeasonDetailBO> seasonDetailList=new ArrayList<>();
            //根据体系规则id查找季节列表
            HashMap<String, Object> seasonQueryMap = new HashMap<>();
            seasonQueryMap.put( "structureRuleIds", Collections.singletonList( t.getId() ) );
            seasonQueryMap.put( "state", BoolLogic.NO.getCode());
            List<ElectricitySeasonSection> electricitySeasonSections = electricityPriceSeasonSectionCustomMapper.querySeasonSectionIdsByStructureRuleIds( seasonQueryMap );
            //筛选出季节
            Set<String> SeasonSectionIds = electricitySeasonSections.stream().map( ElectricitySeasonSection::getSeasonSectionId ).collect( Collectors.toSet() );
            //对季节区间进行合并(同一个季节)
            SeasonSectionIds.forEach( i->{
                ElectricityPriceSeasonDetailBO electricityPriceSeasonDetailBO = new ElectricityPriceSeasonDetailBO();
                electricityPriceSeasonDetailBO.setSeasonSectionId( Long.valueOf( i ) );
                List<SeasonDateBO> seasonDateBOList =new ArrayList<>();
                electricitySeasonSections.stream().filter( h->h.getSeasonSectionId().equals(i) ).forEach( h->{
                    electricityPriceSeasonDetailBO.setSeasonName(h.getSeasonSectionName());
                    //添加季节区间时间信息
                    SeasonDateBO seasonDateBO = new SeasonDateBO();
                    seasonDateBO.setSeasonId( h.getId() );
                    seasonDateBO.setSeaEndDate( h.getSeaEndDate() );
                    seasonDateBO.setSeaStartDate( h.getSeaStartDate() );
                    seasonDateBOList.add( seasonDateBO );
                } );
                //根据季节id查询分时区间信息
                List<ElectricityTimeSection> timeSectionList = electricityTimeSectionCustomMapper.getTimeSectionListBySeasonSectionId( i );
                Map<String, List<ElectricityTimeSection>> timeMap = timeSectionList.stream().collect(
                        Collectors.groupingBy(
                                //将时区按照策略分组
                                time -> time.getCompare() + "-" + time.getTemperature()
                        ) );
                //组装规则列表
                List<ElectricityPriceStrategyBO> electricityPriceStrategyBOList=new ArrayList<>();
                timeMap.forEach((k,v)->{
                    String[] split = k.split( "-" );
                    ElectricityPriceStrategyBO electricityPriceStrategyBO = new ElectricityPriceStrategyBO();
                    electricityPriceStrategyBO.setCompare( split[0] );
                    electricityPriceStrategyBO.setTemperature( split[1] );
                    List<ElectricityTimeSectionUpdateBO> electricityTimeSectionUpdateBOS = ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.ElectricityTimeSectionPOListToBOList( v );
                    electricityPriceStrategyBO.setElectricityTimeSectionUpdateBOList( electricityTimeSectionUpdateBOS );
                    electricityPriceStrategyBOList.add( electricityPriceStrategyBO );

                });
                //策略列表添加入季节
                electricityPriceSeasonDetailBO.setElectricityPriceStrategyBOList( electricityPriceStrategyBOList );
                //季节列表时间添加入季节
                electricityPriceSeasonDetailBO.setSeasonDateList( seasonDateBOList );
                //季节添加进季节列表
                seasonDetailList.add( electricityPriceSeasonDetailBO);
            } );
            //季节列表添加进体系规则
            electricityPriceStructureRuleDetailBO.setElectricityPriceSeasonDetailBOS( seasonDetailList );
            electricityPriceStructureRuleDetailBO.setStrategies( t.getStrategies() );
            electricityPriceStructureRuleDetailBO.setId( String.valueOf( t.getId() ) );
            electricityPriceStructureRuleDetailBO.setIndustries( t.getIndustries() );
            electricityPriceStructureRuleDetailBO.setVoltageLevels( t.getVoltageLevels() );
            //将体系规则添加进规则列表
            electricityPriceStructureRuleDetailBOS.add(electricityPriceStructureRuleDetailBO);

        } );
        //组装体系价格列表
        List<ElectricityPriceDetailPO> priceDetailPOList = electricityPriceCustomMapper.getPriceDetailListByStructureId( structuredId );
        List<ElectricityPriceDetailBO> electricityPriceDetailBOs = ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.electricityPriceDetailPOListToBOList( priceDetailPOList );

        //将规则列表添加进体系
        electricityPriceStructureDetailBO.setElectricityPriceStructureRuleDetailBOS( electricityPriceStructureRuleDetailBOS );
        electricityPriceStructureDetailBO.setElectricityPriceDetailBOList( electricityPriceDetailBOs );
        return electricityPriceStructureDetailBO;
    }


    @Override
    public Map<Integer, List<ElectricityPriceDictionaryBO>> getPriceElectricityDictionaries(String type, String province) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put( CommonConstant.TYPE, Integer.parseInt(type) );
        hashMap.put( CommonConstant.PROVINCE, province );
        hashMap.put(CommonConstant.STATE, CommonConstant.VALID);
        List<ElectricityPriceDictionary> electricityPriceDictionaries = electricityPriceDictionaryCustomMapper.selectDictionaryByCondition( hashMap );
        List<ElectricityPriceDictionaryBO> dictionaryBOList = BeanUtil.mapList(electricityPriceDictionaries, ElectricityPriceDictionaryBO.class);
        return dictionaryBOList.stream()
                .collect(Collectors.groupingBy(ElectricityPriceDictionaryBO ::getType));

    }

    @Override
    public RdfaResult<Boolean> versionDeleteValidate(ElectricityPriceVersionDeleteBO electricityPriceVersionDeleteBO) {
        if(DateUtil.parse(electricityPriceVersionDeleteBO.getStartDate(), DatePattern.NORM_DATE_PATTERN).isBefore( DateUtil.date() )){
            return RdfaResult.fail( ErrorCodeEnum.VERSION_IS_NOT_ALLOW_DELETE.getErrorCode(), ErrorCodeEnum.VERSION_IS_NOT_ALLOW_DELETE.getErrorMsg());
        }
        //判断版本下体系是否绑定了设备
        HashMap<String, Object> queryStructureMap = new HashMap<>();
        queryStructureMap.put( "versionId",electricityPriceVersionDeleteBO.getId() );
        queryStructureMap.put( "state", BoolLogic.NO.getCode());
        List<ElectricityPriceStructure> electricityPriceStructures = electricityPriceStructureCustomMapper.queryListByConditions( queryStructureMap );
        Set<String> bindingEquipmentStructureNameSet = electricityPriceStructures.stream().filter( t -> judgeStructureEquipmentBinding( t.getId(), t.getProvinceCode(), t.getCityCodes(), t.getDistrictCodes() ) ).collect( Collectors.toList() ).stream().map( t -> t.getStructureName() ).collect( Collectors.toSet() );
        if(CollectionUtils.isNotEmpty( bindingEquipmentStructureNameSet )){
            return RdfaResult.fail( ErrorCodeEnum.VERSION_IS_NOT_ALLOW_DELETE.getErrorCode(),"版本下体系名为"+StringUtils.join( bindingEquipmentStructureNameSet,"," )+"绑定了设备,不能删除" );
        }
        return RdfaResult.success( true );
    }

    private boolean judgeStructureEquipmentBinding(Long id, String provinceCode, String cityCodes, String districtCodes) {
        //根据体系id查询设备表
        List<ElectricityPriceEquipment> electricityPriceEquipments = electricityPriceEquipmentCustomMapper.queryEquipmentBindingByStructureId( String.valueOf( id ) );
        //todo 暂未知设备表怎么存储的
        List<ElectricityPriceEquipment> collect = electricityPriceEquipments.stream().filter( t -> {
            //判断值是否匹配体系的区域,
            return true;
        } ).collect( Collectors.toList() );
        if(CollectionUtils.isNotEmpty( collect )){
            return true;
        }
        return false;
    }

    @Override
    public RdfaResult<Boolean> structureDeleteValidate(ElectricityPriceStructureDeleteValidateBO s) {
        if(DateUtil.parse(s.getEndDate(), DatePattern.NORM_DATE_PATTERN).isBefore( DateUtil.date() )){
            return RdfaResult.fail( ErrorCodeEnum.STRUCTURE_IS_NOT_ALLOW_DELETE.getErrorCode(), ErrorCodeEnum.STRUCTURE_IS_NOT_ALLOW_DELETE.getErrorMsg());
        }
        //根据体系id查询体系区域
        boolean b = judgeStructureEquipmentBinding( Long.valueOf( s.getStructureId() ), s.getProvinceCode(), s.getCityCodes(), s.getDistrictCodes() );
        if(b){
            return RdfaResult.fail( ErrorCodeEnum.STRUCTURE_IS_NOT_ALLOW_DELETE.getErrorCode(), ErrorCodeEnum.STRUCTURE_IS_NOT_ALLOW_DELETE.getErrorMsg());
        }
        return RdfaResult.success( true );
    }

    /**
     * 查找哪些体系被删除了
     * @param electricityPriceStructureUpdateBOList
     * @return
     */
    private List<Long> queryDeleteStructureIds(List<ElectricityPriceStructureUpdateBO> electricityPriceStructureUpdateBOList,String versionId) {
        //根据版本id查询当前版本下的体系id列表
        Map<String,Object> map = new HashMap<>();
        map.put("versionId",versionId);
        map.put( "state",BoolLogic.NO.getCode() );
        List<ElectricityPriceStructure> electricityPriceStructures = electricityPriceStructureCustomMapper.queryListByConditions( map );
        //筛选出现有的id
        List<Long> ids = electricityPriceStructureUpdateBOList.stream().map( ElectricityPriceStructureUpdateBO::getId ).filter(m-> StringUtils.isNotBlank( String.valueOf(m ) )).collect( Collectors.toList() );
        //拿版本下的体系列表与现有的对比，得出哪些删除了
        return electricityPriceStructures.stream().map( ElectricityPriceStructure::getId ).collect( Collectors.toList() ).stream().filter( f -> !ids.contains(String.valueOf( f )  ) ).collect( Collectors.toList() );
    }

    /**
     * 修改体系
     * @param priceStructureUpdateBO
     */
    private void updateStructure(ElectricityPriceStructureUpdateBO priceStructureUpdateBO,String versionId) {
        //查询当前体系绑定的设备信息
        List<ElectricityPriceEquipment> electricityPriceEquipments = electricityPriceEquipmentCustomMapper.queryEquipmentBindingByStructureId( String.valueOf(priceStructureUpdateBO.getId()) );
        //更新操作,先删除当前体系下的数据然后新增
        deleteStructure(Long.valueOf( priceStructureUpdateBO.getId() ));
        String structureId = createStructure( priceStructureUpdateBO, versionId );
        List<ElectricityPriceRule> electricityPriceRules = electricityPriceRuleCustomMapper.queryRuleListByStructureId( structureId );
        //更新设备绑定,根据设备绑定的规则id找到对应的三要素,匹配新体系下的三要素
        electricityPriceEquipments.forEach( t->{
            ElectricityPriceRule electricityPriceRule = priceRuleMapper.selectByPrimaryKey( Long.valueOf(t.getId()) );
            ElectricityPriceRule electricityPriceRule1 = electricityPriceRules.stream().filter( f -> {
                if (f.getIndustry().equals( electricityPriceRule.getIndustry() )
                        && f.getStrategy().equals( electricityPriceRule.getStrategy() )
                        && f.getVoltageLevel().equals( electricityPriceRule.getVoltageLevel() )) {
                    return true;
                }
                return false;
            } ).findFirst().get();
            String structureRuleId = electricityPriceRule1.getStructureRuleId();
            //更新设备表
            t.setStructureId(structureId );
            t.setStructureRuleId( structureRuleId );
            t.setUpdator( tenantId );
            t.setUpdateTime(DateUtil.date());
            electricityPriceEquipmentMapper.updateByPrimaryKey(t);

        } );
    }


    public void updateStructurePlan3(ElectricityPriceStructureUpdateBO priceStructureUpdateBO,String versionId){
        Long structureId = priceStructureUpdateBO.getId();
        //根据体系规则id判断体系规则的变化
        List<ElectricityPriceStructureRuleUpdateBO> electricityPriceStructureRuleUpdateBOList = priceStructureUpdateBO.getElectricityPriceStructureRuleUpdateBOList();
        List<ElectricityPriceStructureRuleUpdateBO> haveIdList = electricityPriceStructureRuleUpdateBOList.stream().filter( t -> Optional.ofNullable( t.getStructureRuleId() ).isPresent() ).collect( Collectors.toList() );
        List<ElectricityPriceStructureRuleUpdateBO> addList = electricityPriceStructureRuleUpdateBOList.stream().filter( t -> !Optional.ofNullable( t.getStructureRuleId() ).isPresent() ).collect( Collectors.toList() );
        List<ElectricityPriceStructureRuleUpdateBO> updateList = electricityPriceStructureRuleUpdateBOList.stream().filter( t -> !Optional.ofNullable( t.getStructureRuleId() ).isPresent() && t.getChangeType().equals( ChangeTypeEum.UPDATE.getType() ) ).collect( Collectors.toList() );

        //根据体系id查询当前体系原有的体系规则列表
        HashMap<String , Object> structureRuleQueryMap = new HashMap<>();
        structureRuleQueryMap.put( "structureId", structureId );
        structureRuleQueryMap.put( "state",BoolLogic.NO.getCode() );
        List<ElectricityPriceStructureRule> electricityPriceStructureRules = electricityPriceStructureRuleCustomMapper.queryElectricityPriceRulesByCondition( structureRuleQueryMap );
        List<Long> haveIds = haveIdList.stream().map( f -> f.getStructureRuleId() ).collect( Collectors.toList() );
        List<Long> deleteIds = new ArrayList<>();
        for (ElectricityPriceStructureRule t : electricityPriceStructureRules) {
            if (!haveIds.contains( t.getId() )) {
                Long id = t.getId();
                deleteIds.add( id );
            }
        }
        //规则的删除
        if(CollectionUtil.isEmpty( deleteIds )){
            deleteStructureRuleByStructureRuleIds(deleteIds);
        }
        //规则新增
        if(CollectionUtil.isEmpty(addList)){
            createStructureRuleAndSeason(addList, structureId );
        }
        //规则更新
        updateList.forEach( t->{
            ElectricityPriceStructureRule structureRule = new ElectricityPriceStructureRule();
            structureRule.setId( t.getStructureRuleId() );
            structureRule.setIndustries( t.getIndustries() );
            structureRule.setStrategies( t.getStrategies() );
            structureRule.setVoltageLevels( t.getVoltageLevels() );
            structureRule.setUpdateTime( DateUtil.date() );
            electricityPriceStructureRuleCustomMapper.updateStructureRule( structureRule );
            //根据体系规则id查询季节列表
            HashMap<String, Object> seasonQueryMap = new HashMap<>();
            seasonQueryMap.put( "structureRuleIds",Collections.singletonList( t.getStructureRuleId() ) );
            seasonQueryMap.put( "state", BoolLogic.NO.getCode());
            List<ElectricitySeasonSection> electricitySeasonSections = electricityPriceSeasonSectionCustomMapper.querySeasonSectionIdsByStructureRuleIds( seasonQueryMap );
            Set<String> collect = electricitySeasonSections.stream().map( m -> m.getSeasonSectionId() ).collect( Collectors.toSet() );
            //判断哪些seasonSectionId删除了
            List<String> havaSeasonIds = t.getElectricityPriceSeasonUpdateReqVOList().stream().filter( l -> StringUtils.isNotEmpty( l.getSeasonSectionId() ) ).map( n -> n.getSeasonSectionId() ).collect( Collectors.toList() );
            Set<String> deleteSeasonIds = collect.stream().filter( n -> !havaSeasonIds.contains( n ) ).collect( Collectors.toSet() );
            List<ElectricityPriceSeasonUpdateBO> updateSeasonList = t.getElectricityPriceSeasonUpdateReqVOList().stream().filter( n -> StringUtils.isNotEmpty( n.getSeasonSectionId() ) && n.getChangeType().equals( ChangeTypeEum.UPDATE.getType() ) ).collect( Collectors.toList() );
            List<ElectricityPriceSeasonUpdateBO> addSeasonList = t.getElectricityPriceSeasonUpdateReqVOList().stream().filter( n -> StringUtils.isEmpty( n.getSeasonSectionId() ) ).collect( Collectors.toList() );
            //删除
            batchDeleteSeasonAndTime( deleteSeasonIds );
            //增加
            addSeasonList.forEach( n->{
                createSeasonsAndTimes(n,String.valueOf( structureId ),t.getStructureRuleId());
            } );
            //修改
            updateSeasonList.forEach( n->{
                String seasonName = n.getSeasonName();
                String seasonSectionId = n.getSeasonSectionId();
                //日期部分
                List<SeasonDateBO> seasonDateList = n.getSeasonDateList();
                List<SeasonDateBO> haveDateIds = seasonDateList.stream().filter( m -> StringUtils.isNotEmpty( String.valueOf( m.getSeasonId() ) ) ).collect( Collectors.toList() );
                List<SeasonDateBO> updateDateList = haveDateIds.stream().filter( m -> m.getChangeType().equals( ChangeTypeEum.UPDATE.getType() ) ).collect( Collectors.toList() );
                List<SeasonDateBO> addDateList = seasonDateList.stream().filter( m -> StringUtils.isEmpty( String.valueOf( m.getSeasonId() ) ) ).collect( Collectors.toList() );
                List<ElectricitySeasonSection> seasonSections = electricitySeasonSections.stream().filter( m -> m.getSeasonSectionId().equals( n.getSeasonSectionId() ) ).collect( Collectors.toList() );
                List<Long> deleteDateIds = seasonSections.stream().filter( m -> !haveDateIds.contains( m.getId() ) ).map( m -> m.getId() ).collect( Collectors.toList() );
                //删除date_time
                if(CollectionUtils.isNotEmpty( deleteDateIds )){
                    HashMap<String , Object> timeDeleteMap = new HashMap<>();
                    timeDeleteMap.put( "ids", StringUtils.join( deleteDateIds,"," ));
                    timeDeleteMap.put( "updateTime",DateUtil.date() );
                    timeDeleteMap.put( "state", BoolLogic.NO.getCode());
                    electricityPriceSeasonSectionCustomMapper.batchDeleteSeasonSectionByIds(timeDeleteMap  );
                }
                //增加date_time
                if(CollectionUtils.isNotEmpty( addDateList )){
                    batchCreateSeasonDate(addDateList,seasonName,seasonSectionId,String.valueOf( structureId ),String.valueOf( t.getStructureRuleId() ));
                }
                //更新时间
                if(CollectionUtils.isNotEmpty(updateDateList)){
                    batchUpdateSeasonDate(updateDateList);
                }
                //策略分时部分
                List<ElectricityPriceStrategyBO> strategyList = n.getElectricityPriceStrategyBOList();
                //根据季节分时查找分时list,然后根据策略分组
                List<ElectricityTimeSection> timeSectionList = electricityTimeSectionCustomMapper.getTimeSectionListBySeasonSectionId( n.getSeasonSectionId() );
                Map<String, List<ElectricityTimeSection>> groupStrategyList = timeSectionList.stream().collect( Collectors.groupingBy( m -> m.getCompare() + "-" + m.getTemperature() ) );
                List<String> strategyNameList = strategyList.stream().map( m -> m.getCompare() + "-" + m.getTemperature() ).collect( Collectors.toList() );
                groupStrategyList.forEach( (k,v)->{
                    if(strategyNameList.contains( k )){
                        strategyList.forEach( m->{
                            String strategyName = m.getCompare() + "-" + m.getTemperature();
                            if(strategyName.equals( k )){
                                //设置标记 匹配上
                                m.setComply( true );
                                List<ElectricityTimeSectionUpdateBO> hasIdList = m.getElectricityTimeSectionUpdateBOList().stream().filter( h -> StringUtils.isNotEmpty( String.valueOf( h.getId() ) ) ).collect( Collectors.toList() );
                                List<ElectricityTimeSectionUpdateBO> updateIdList = hasIdList.stream().filter( h -> h.getChangeType().equals( ChangeTypeEum.UPDATE.getType() ) ).collect( Collectors.toList() );
                                List<ElectricityTimeSectionUpdateBO> addIdList = m.getElectricityTimeSectionUpdateBOList().stream().filter( h -> StringUtils.isEmpty( String.valueOf( h.getId() ) ) ).collect( Collectors.toList() );
                                List<Long> datasourceIdList = v.stream().map( ElectricityTimeSection::getId ).collect( Collectors.toList() );
                                List<Long> hasIds = hasIdList.stream().map( ElectricityTimeSectionUpdateBO::getId ).collect( Collectors.toList() );
                                List<Long> deleteIdList = datasourceIdList.stream().filter( hasIds::contains ).collect( Collectors.toList() );
                                //删除
                                HashMap<String, Object> deleteMap = new HashMap<>();
                                deleteMap.put("ids",StringUtils.join( deleteIdList,"," ));
                                deleteMap.put( "state", BoolLogic.YES.getCode());
                                deleteMap.put( "updateTime",DateUtil.date() );
                                electricityTimeSectionCustomMapper.batchDeleteTimeSectionByIds( deleteMap );
                                //增加
                                List<ElectricityTimeSection> electricityTimeSectionList=new ArrayList<>();
                                batchCreateTime(addIdList,seasonSectionId,m.getCompare(),m.getTemperature());
                                //更新
                                List<ElectricityTimeSection> timeUpdateList=new ArrayList<>();
                                updateIdList.forEach( h->{
                                    ElectricityTimeSection electricityTimeSection=new ElectricityTimeSection();
                                    electricityTimeSection.setTemperature( m.getTemperature() );
                                    electricityTimeSection.setCompare( m.getCompare() );
                                    electricityTimeSection.setStartTime( h.getStartTime() );
                                    electricityTimeSection.setEndTime( h.getEndTime() );
                                    electricityTimeSection.setPeriods( h.getPeriods() );
                                    electricityTimeSection.setId( h.getId() );
                                    electricityTimeSection.setUpdateTime( DateUtil.date() );
                                    timeUpdateList.add( electricityTimeSection );
                                } );
                                electricityTimeSectionCustomMapper.batchUpdateTimeSection(timeUpdateList);
                            }
                        } );
                    }
                    //删除无用的time
                    List<Long> delete = v.stream().map( m -> m.getId() ).collect( Collectors.toList() );
                    HashMap<String, Object> deleteMap = new HashMap<>();
                    deleteMap.put("ids",StringUtils.join( delete,"," ));
                    deleteMap.put( "state", BoolLogic.YES.getCode());
                    deleteMap.put( "updateTime",DateUtil.date() );
                    electricityTimeSectionCustomMapper.batchDeleteTimeSectionByIds( deleteMap );
                } );

                //增加未匹配上的策略
                List<ElectricityPriceStrategyBO> addStrategyList = strategyList.stream().filter( m -> !m.getComply() ).collect( Collectors.toList() );
                strategyList.forEach( m->{
                    batchCreateTime(m.getElectricityTimeSectionUpdateBOList(),seasonSectionId,m.getCompare(),m.getTemperature());
                } );
            } );
        } );
        //价格部分
        List<ElectricityPriceUpdateBO> rulePriceList = priceStructureUpdateBO.getElectricityPriceUpdateBOList();
        List<ElectricityPriceUpdateBO> addRuleList = rulePriceList.stream().filter( t -> StringUtils.isEmpty( String.valueOf( t.getRuleId() ) ) ).collect( Collectors.toList() );
        List<ElectricityPriceUpdateBO> hasRuleIdList = rulePriceList.stream().filter( t -> StringUtils.isNotEmpty( String.valueOf( t.getRuleId() ) ) ).collect( Collectors.toList() );
        List<ElectricityPriceUpdateBO> updateRuleList = hasRuleIdList.stream().filter( t -> t.getRuleChangeType().equals( ChangeTypeEum.UPDATE.getType() ) ).collect( Collectors.toList() );
        List<ElectricityPriceRule> electricityPriceRules = electricityPriceRuleCustomMapper.queryRuleListByStructureId( String.valueOf( structureId ) );
        List<Long> dataSourceRules = electricityPriceRules.stream().map( ElectricityPriceRule::getId ).collect( Collectors.toList() );
        List<Long> hasRuleId = hasRuleIdList.stream().map( ElectricityPriceUpdateBO::getRuleId ).collect( Collectors.toList() );
        List<Long> deleteRuleList = dataSourceRules.stream().filter( t -> !hasRuleId.contains( t ) ).collect( Collectors.toList() );
        //删除
        batchDeleteRulePriceAndEquipment(deleteRuleList );
        //增加
        addRuleList.forEach( t->{
            Long structureRuleId = queryStructureRuleId( t.getIndustry(), t.getStrategy(), t.getVoltageLevel(), String.valueOf( structureId ) );
            createPriceAndRule(t,versionId,String.valueOf( structureId ),structureRuleId);
        } );
        //更新
        updateRuleList.forEach( t->{
            Long structureRuleId = queryStructureRuleId( t.getIndustry(), t.getStrategy(), t.getVoltageLevel(), String.valueOf( structureId ) );
            ElectricityPriceRule electricityPriceRule = new ElectricityPriceRule();
            electricityPriceRule.setVoltageLevel( t.getVoltageLevel() );
            electricityPriceRule.setStructureRuleId( String.valueOf(structureRuleId) );
            electricityPriceRule.setIndustry( t.getIndustry() );
            electricityPriceRule.setStrategy(t.getStrategy());
            electricityPriceRule.setUpdateTime( DateUtil.date() );
            electricityPriceRule.setState( Integer.valueOf( BoolLogic.NO.getCode()  ));
            electricityPriceRuleCustomMapper.updateRuleByConditions(electricityPriceRule);
            //更新价格
            ElectricityPrice electricityPrice = ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.electricityPriceBOToPO( t );
            electricityPrice.setState( Integer.valueOf( BoolLogic.NO.getCode() ) );
            electricityPrice.setUpdateTime( DateUtil.date() );
            electricityPriceCustomMapper.updatePriceByConditions( electricityPrice );
            //更新设备绑定 todo(暂不考虑)
        } );
    }

    /**
     * 创建分时信息
     * @param addIdList
     * @param seasonSectionId
     * @param compare
     * @param Temperature
     */
    public void batchCreateTime(List<ElectricityTimeSectionUpdateBO> addIdList,String seasonSectionId,String compare,String Temperature){
        List<ElectricityTimeSection> electricityTimeSectionList=new ArrayList<>();
        addIdList.forEach( h->{
            ElectricityTimeSection electricityTimeSection=new ElectricityTimeSection();
            String timeSectionId = IdUtil.simpleUUID();
            electricityTimeSection.setSeasonSectionId(seasonSectionId);
            electricityTimeSection.setTimeSectionId( timeSectionId );
            electricityTimeSection.setStartTime( h.getStartTime() );
            electricityTimeSection.setEndTime(h.getEndTime());
            electricityTimeSection.setPeriods( h.getPeriods() );
            electricityTimeSection.setState(BoolLogic.NO.getCode());
            electricityTimeSection.setCreateTime(DateUtil.date());
            electricityTimeSection.setCompare( compare );
            electricityTimeSection.setTemperature(Temperature );
            electricityTimeSectionList.add(electricityTimeSection);
        } );
        electricityTimeSectionCustomMapper.batchInsertTimeSection( electricityTimeSectionList );

    }





    /**
     * 插入seasonList
     * @param addDateList
     * @param seasonName
     * @param seasonSectionId
     * @param structureId
     * @param structureRuleId
     */
    public void batchCreateSeasonDate(List<SeasonDateBO> addDateList, String seasonName, String seasonSectionId, String structureId, String structureRuleId){
        if(CollectionUtils.isNotEmpty( addDateList )){
            addDateList.forEach( t->{
                ElectricitySeasonSection seasonSection = new ElectricitySeasonSection();
                seasonSection.setSeasonSectionName( seasonName );
                seasonSection.setSeasonSectionId( seasonSectionId);
                seasonSection.setCreateTime( DateUtil.date() );
                seasonSection.setStructureId( structureId );
                seasonSection.setStructureRuleId( structureRuleId );
                seasonSection.setState( BoolLogic.NO.getCode() );
                seasonSection.setSeaEndDate( t.getSeaEndDate() );
                seasonSection.setSeaStartDate( t.getSeaStartDate() );
                seasonSectionMapper.insert( seasonSection );
            } );
        }
    }

    /**
     * 批量更新季节日期列表
     * @param updateDateList
     */
    public void batchUpdateSeasonDate(List<SeasonDateBO> updateDateList){
        if(CollectionUtils.isNotEmpty( updateDateList )){
            ArrayList<Object> list = new ArrayList<>();
            updateDateList.forEach( t->{
                HashMap<String, Object> map = new HashMap<>();
                map.put( "id", t.getSeasonId());
                map.put( "seaStartDate",t.getSeaStartDate() );
                map.put( "seaEndDate",t.getSeaEndDate() );
                map.put( "updateTime",DateUtil.date() );
                list.add( map );
            } );
            electricityPriceSeasonSectionCustomMapper.batchUpdateSeason( list );
        }
    }







    /**
     * 更新体系
     * @param priceStructureUpdateBO
     */
    /*public void updateStructurePlan2(ElectricityPriceStructureUpdateBO priceStructureUpdateBO,String versionId){
        //根据体系规则id判断体系规则的变化
        List<ElectricityPriceStructureRuleUpdateBO> electricityPriceStructureRuleUpdateBOList = priceStructureUpdateBO.getElectricityPriceStructureRuleUpdateBOList();
        List<ElectricityPriceStructureRuleUpdateBO> updateList = electricityPriceStructureRuleUpdateBOList.stream().filter( t -> Optional.ofNullable( t.getStructureRuleId() ).isPresent() ).collect( Collectors.toList() );
        List<ElectricityPriceStructureRuleUpdateBO> addList = electricityPriceStructureRuleUpdateBOList.stream().filter( t -> !Optional.ofNullable( t.getStructureRuleId() ).isPresent() ).collect( Collectors.toList() );
        //根据体系id查询当前体系原有的体系规则列表
        HashMap<String , Object> structureRuleQueryMap = new HashMap<>();
        structureRuleQueryMap.put( "structureId", priceStructureUpdateBO.getId());
        structureRuleQueryMap.put( "state",BoolLogic.NO.getCode() );
        List<ElectricityPriceStructureRule> electricityPriceStructureRules = electricityPriceStructureRuleCustomMapper.queryElectricityPriceRulesByCondition( structureRuleQueryMap );
        List<Long> updateIds = updateList.stream().map( f -> f.getStructureRuleId() ).collect( Collectors.toList() );
        List<Long> deleteIds = new ArrayList<>();
        for (ElectricityPriceStructureRule t : electricityPriceStructureRules) {
            if (!updateIds.contains( t.getId() )) {
                Long id = t.getId();
                deleteIds.add( id );
            }
        }
        //规则的删除
        if(CollectionUtil.isEmpty( deleteIds )){
            deleteStructureRuleByStructureRuleIds(deleteIds);
        }
        //规则新增
        if(CollectionUtil.isEmpty(addList)){
            createStructureRuleAndSeason(addList,priceStructureUpdateBO.getId());
        }
        //规则的修改
        updateList.forEach( t->{
            //获取规则id,根据规则id查询季节列表
            Long id = t.getStructureRuleId();
            //比对数据库看是否发生变化
            ElectricityPriceStructureRule structureRule = electricityPriceStructureRuleCustomMapper.queryElectricityPriceRulesByStructureRule( String.valueOf( id ) ).get( 0 );
            if(!t.getIndustries().equals( structureRule.getIndustries() )
                    || !t.getStrategies().equals( structureRule.getStrategies() )
                    || !t.getVoltageLevels().equals(structureRule.getVoltageLevels()  )){
                structureRule.setIndustries( t.getIndustries() );
                structureRule.setStrategies( t.getStrategies() );
                structureRule.setVoltageLevels( t.getVoltageLevels() );
                structureRule.setUpdateTime( DateUtil.date() );
                priceStructureRuleMapper.updateByPrimaryKey( structureRule );
            }

            List<ElectricitySeasonSection> electricitySeasonSections = electricityPriceSeasonSectionCustomMapper.querySeasonSectionIdsByStructureRuleIds( String.valueOf( id ) );
            //匹配季节列表判断季节的变化
            List<ElectricityPriceSeasonUpdateBO> seasonAddList = t.getElectricityPriceSeasonUpdateReqVOList().stream().filter( s -> StringUtils.isEmpty(  s.getSeasonSectionId()  ) ).collect( Collectors.toList() );
            List<ElectricityPriceSeasonUpdateBO> seasonUpdateList = t.getElectricityPriceSeasonUpdateReqVOList().stream().filter( s -> StringUtils.isNotEmpty(s.getSeasonSectionId()  ) ).collect( Collectors.toList() );
            List<String> seasonUpdateIds = seasonUpdateList.stream().map( f -> f.getSeasonSectionId() ).collect( Collectors.toList() );
            Set<String> seasonDeleteIdList = electricitySeasonSections.stream().filter( f -> !seasonUpdateIds.contains( f.getSeasonSectionId() ) ).map( g -> g.getSeasonSectionId() ).collect( Collectors.toSet() );
            //季节删除
            batchDeleteSeasonAndTime(seasonDeleteIdList);
            //季节新增
            seasonAddList.forEach( h->{
                createSeasonsAndTimes(h,priceStructureUpdateBO.getId(),Long.valueOf( t.getStructureRuleId() ));
            } );
            //季节更新 根据season_id查询季节列表
            for (ElectricityPriceSeasonUpdateBO m : seasonUpdateList) {
                List<ElectricitySeasonSection> seasonList = electricityPriceSeasonSectionCustomMapper.querySeasonSectionListBySeasonSectionId( m.getSeasonSectionId() );
                //比对季节时间与季节名称是否改变
                List<SeasonDateBO> seasonDateList = m.getSeasonDateList();
                //筛选出符合条件的以及还未满足的季节列表
                List<ElectricitySeasonSection> complyList = seasonList.stream().filter( l -> {
                    if (l.getSeasonSectionName().equals( m.getSeasonName() )) {
                        List<SeasonDateBO> dateSame = seasonDateList.stream().filter( g -> {
                            if (g.getSeaEndDate().equals( l.getSeaEndDate() ) && g.getSeaStartDate().equals( l.getSeaStartDate() )) {
                                g.setComply( true );
                                return true;
                            }
                            return false;

                        } ).collect( Collectors.toList() );
                        return CollectionUtils.isNotEmpty( dateSame );
                    }
                    return false;
                } ).collect( Collectors.toList() );
                //直接删除不符合的数据库记录并新增不符合的季节列表
                seasonList.removeAll( complyList );
                if (CollectionUtils.isNotEmpty( seasonList )) {
                    List<Long> collect = seasonList.stream().map( h -> h.getId() ).collect( Collectors.toList() );
                    //批量删除不符合的
                    HashMap<String, Object> seasonDeleteMap = new HashMap<>();
                    seasonDeleteMap.put( "ids", CollectionUtil.join( collect, "," ) );
                    seasonDeleteMap.put( "state", BoolLogic.YES.getCode() );
                    seasonDeleteMap.put( "updateTime", DateUtil.date() );
                    int i = electricityPriceSeasonSectionCustomMapper.batchDeleteSeasonSectionByIds( seasonDeleteMap );
                    //插入不符合的季节列表
                    seasonDateList.stream().filter( n -> !n.getComply() ).forEach( k -> {
                        ElectricitySeasonSection electricitySeasonSection = new ElectricitySeasonSection();
                        electricitySeasonSection.setSeasonSectionName( m.getSeasonName() );
                        electricitySeasonSection.setSeasonSectionId( m.getSeasonSectionId() );
                        electricitySeasonSection.setCreateTime( DateUtil.date() );
                        electricitySeasonSection.setStructureId( priceStructureUpdateBO.getId() );
                        electricitySeasonSection.setStructureRuleId( String.valueOf( t.getStructureRuleId() ) );
                        electricitySeasonSection.setState( BoolLogic.NO.getCode() );
                        electricitySeasonSection.setSeaEndDate( k.getSeaEndDate() );
                        electricitySeasonSection.setSeaStartDate( k.getSeaStartDate() );
                        seasonSectionMapper.insert( electricitySeasonSection );
                    } );
                }
                //策略部分
                List<ElectricityPriceStrategyBO> priceStrategyBOList = m.getElectricityPriceStrategyBOList();
                //根据季节区间id查询分时区间信息
                List<ElectricityTimeSection> timeSectionList = electricityTimeSectionCustomMapper.getTimeSectionListBySeasonSectionId( m.getSeasonSectionId() );
                //判断策略的差异
                List<ElectricityTimeSection> timehaveList = timeSectionList.stream().filter( a -> {
                    final Boolean[] have = {false};
                    priceStrategyBOList.forEach( b -> {
                        if (a.getTemperature().equals( b.getTemperature() ) && a.getCompare().equals( b.getCompare() )) {
                            List<ElectricityTimeSectionUpdateBO> sectionUpdateBOList = b.getElectricityTimeSectionUpdateBOList();
                            //判断是否有符合当前所需的数据
                            have[0] = sectionUpdateBOList.stream().anyMatch( e -> {
                                if (e.getPeriods().equals( a.getPeriods() )
                                        && e.getEndTime().equals( a.getEndTime() )
                                        && e.getStartTime().equals( a.getStartTime() )) {
                                    e.setComply( true );
                                    return true;
                                }
                                return false;
                            } );
                        }
                    } );
                    return have[0];
                } ).collect( Collectors.toList() );
                timeSectionList.removeAll( timehaveList );
                //删除不匹配的增加需要的
                List<Long> deleteTimeIds = timeSectionList.stream().map( e -> e.getId() ).collect( Collectors.toList() );
                HashMap<String, Object> deleteMap = new HashMap<>();
                deleteMap.put( "ids",StringUtils.join( deleteTimeIds,"," ) );
                deleteMap.put( "updateTime",DateUtil.date() );
                deleteMap.put( "state", BoolLogic.YES.getCode());
                electricityTimeSectionCustomMapper.batchDeleteTimeSectionByIds( deleteMap );
                //增加
                priceStrategyBOList.forEach( f->{
                    f.getElectricityTimeSectionUpdateBOList().stream().filter( o->!o.getComply() ).collect(Collectors.toList() ).forEach( p->{
                        ElectricityTimeSection timeSection = new ElectricityTimeSection();
                        timeSection.setSeasonSectionId(m.getSeasonSectionId());
                        timeSection.setTimeSectionId(IdUtil.simpleUUID()  );
                        timeSection.setStartTime(p.getStartTime());
                        timeSection.setEndTime( p.getEndTime() );
                        timeSection.setCompare( f.getCompare() );
                        timeSection.setTemperature( f.getTemperature() );
                        timeSection.setPeriods( p.getPeriods() );
                        timeSection.setState( BoolLogic.NO.getCode() );
                        timeSectionMapper.insert( timeSection );
                    } );
                } );
            }
        } );

        //价格部分
        List<ElectricityPriceUpdateBO> priceUpdateBOList = priceStructureUpdateBO.getElectricityPriceUpdateBOList();
        //根据体系id查询当前体系下的规则与价格
        List<ElectricityPriceRule> priceRules = electricityPriceRuleCustomMapper.queryRuleListByStructureId( priceStructureUpdateBO.getId() );
        List<ElectricityPriceRule> ruleHaveList = priceRules.stream().filter( f -> {
            return priceUpdateBOList.stream().anyMatch( e -> {
                if (e.getIndustry().equals( f.getIndustry() )
                        && e.getStrategy().equals( f.getStrategy() ) && e.getVoltageLevel().equals( f.getVoltageLevel() )) {
                    e.setComply( true );
                    return true;
                }
                return false;
            } );
        } ).collect( Collectors.toList() );
        priceRules.removeAll( ruleHaveList );
        List<Long> deleteRuleIds = priceRules.stream().map( q -> q.getId() ).collect( Collectors.toList() );
        //批量删除无用的规则以及对应的价格
        batchDeleteRuleAndPrice( deleteRuleIds );
        //已有规则的
        List<ElectricityPriceUpdateBO> haveRuleList = priceUpdateBOList.stream().filter( m -> m.getComply() ).collect( Collectors.toList() );
        //对已经有的规则进行判断价格是否改变,以及匹配对应的体系规则id
        ruleHaveList.forEach( n->{
            //根据规则查询价格,直接更新价格
            HashMap<String, Object> priceQueryMap = new HashMap<>();
            priceQueryMap.put( "state",BoolLogic.NO.getCode() );
            priceQueryMap.put( "ruleId", n.getId());
            ElectricityPrice electricityPrice = electricityPriceCustomMapper.getPriceByCondition( priceQueryMap ).stream().findFirst().orElse( null );
            haveRuleList.forEach( h->{
                //价格匹配
                if(h.getIndustry().equals( n.getIndustry() )
                        &&h.getVoltageLevel().equals( n.getVoltageLevel() )
                        && h.getStrategy().equals( n.getStrategy() )){
                    if(!h.getTransformerCapacityPrice().equals( electricityPrice.getTransformerCapacityPrice() )
                            || !h.getMaxCapacityPrice().equals( electricityPrice.getMaxCapacityPrice() )
                            || !h.getConsumptionPrice().equals(electricityPrice.getConsumptionPrice()  )
                            || ! h.getDistributionPrice().equals(electricityPrice.getDistributionPrice()  )
                            || ! h.getGovAddPrice().equals(electricityPrice.getGovAddPrice())
                            || ! h.getSharpPrice().equals( electricityPrice.getSharpPrice() )
                            || ! h.getPeakPrice().equals(electricityPrice.getPeakPrice())
                            || ! h.getLevelPrice().equals(electricityPrice.getLevelPrice())
                            || ! h.getValleyPrice().equals(electricityPrice.getValleyPrice())){
                        electricityPrice.setTransformerCapacityPrice( h.getTransformerCapacityPrice() );
                        electricityPrice.setMaxCapacityPrice( h.getMaxCapacityPrice() );
                        electricityPrice.setConsumptionPrice( h.getConsumptionPrice() );
                        electricityPrice.setDistributionPrice( h.getDistributionPrice() );
                        electricityPrice.setGovAddPrice( h.getGovAddPrice() );
                        electricityPrice.setSharpPrice( h.getSharpPrice() );
                        electricityPrice.setPeakPrice( h.getPeakPrice() );
                        electricityPrice.setLevelPrice( h.getLevelPrice() );
                        electricityPrice.setValleyPrice( h.getValleyPrice() );
                        electricityPrice.setUpdateTime( DateUtil.date() );
                        priceMapper.updateByPrimaryKey( electricityPrice );
                        //判断体系规则id是否匹配
                        Long strutureRuleId = queryStructureRuleId( h.getIndustry(), h.getStrategy(), h.getVoltageLevel(), priceStructureUpdateBO.getId() );
                        if(!n.getStructureRuleId().equals( String.valueOf( strutureRuleId ) )){
                            n.setStructureRuleId( String.valueOf(strutureRuleId) );
                            priceRuleMapper.updateByPrimaryKey( n );
                        }

                    }
                }
            } );
        } );
        //新增未匹配的规则以及价格
        priceUpdateBOList.stream().filter( m->!m.getComply() ).forEach( l->{
            //增加规则与价格
            Long strutureRuleId = queryStructureRuleId( l.getIndustry(), l.getStrategy(), l.getVoltageLevel(), priceStructureUpdateBO.getId() );
            createPriceAndRule(l,versionId,priceStructureUpdateBO.getId(),strutureRuleId);
        } );
    }
*/
    /**
     * 根据三要素匹配体系规则
     * @param industry
     * @param strategy
     * @param voltageLevel
     * @param id
     * @return
     */
    private Long queryStructureRuleId(String industry, String strategy, String voltageLevel, String id) {
        Map<String,Object> map=new HashMap<>();
        map.put( "state", BoolLogic.NO.getCode());
        map.put( "structureId",id );
        List<ElectricityPriceStructureRule> priceStructureRuleList = electricityPriceStructureRuleCustomMapper.queryElectricityPriceRulesByCondition( map );
        ElectricityPriceStructureRule electricityPriceStructureRule = priceStructureRuleList.stream().filter( t -> {
            boolean a = Arrays.asList( t.getStrategies().split( "," ) ).contains( strategy );
            boolean b = Arrays.asList( t.getIndustries().split( "," ) ).contains( industry );
            boolean c = Arrays.asList( t.getVoltageLevels().split( "," ) ).contains( voltageLevel );
            if (a && b && c) {
                return true;
            }
            return false;
        } ).collect( Collectors.toList() ).stream().findFirst().orElse( null );
        return electricityPriceStructureRule.getId();
    }


    /**
     * 删除体系
     * @param structureId 体系id
     */
    private void deleteStructure(Long structureId) {
        //根据体系id删除体系规则,季节分时,分时区间,规则,价格
        ElectricityPriceStructure electricityPriceStructure = new ElectricityPriceStructure();
        electricityPriceStructure.setId( structureId );
        electricityPriceStructure.setState( BoolLogic.YES.getCode() );
        electricityPriceStructure.setUpdator( tenantId );
        electricityPriceStructure.setUpdateTime( DateUtil.date() );
        electricityPriceStructureCustomMapper.updateByPrimaryKey( electricityPriceStructure );
        //根据体系id查询体系规则,根据体系规则ids删除体系规则
        HashMap<String, Object> structureRuleQueryMap = new HashMap<>();
        structureRuleQueryMap.put( "structureId", structureId);
        structureRuleQueryMap.put( "state", BoolLogic.NO.getCode());
        List<ElectricityPriceStructureRule> electricityPriceStructureRules = electricityPriceStructureRuleCustomMapper.queryElectricityPriceRulesByCondition( structureRuleQueryMap);
        List<Long> electricityPriceStructureRuleIds = electricityPriceStructureRules.stream().map( ElectricityPriceStructureRule::getId ).collect( Collectors.toList() );
        deleteStructureRuleByStructureRuleIds(electricityPriceStructureRuleIds);
        //批量删除体系规则下对应的规则与价格(根据体系id)
        List<ElectricityPriceRule> electricityPriceRules = electricityPriceRuleCustomMapper.queryRuleListByStructureId( String.valueOf( structureId ) );
        List<Long> electricityPriceRuleIds = electricityPriceRules.stream().map( ElectricityPriceRule::getId ).collect( Collectors.toList() );
        batchDeleteRulePriceAndEquipment(electricityPriceRuleIds);
    }

    /**
     * 根据体系规则id删除体系规则以及对应的季节分时,分时区间
     */
    public void deleteStructureRuleByStructureRuleIds(List<Long> electricityPriceStructureRuleIds){
        //根据体系规则id删除体系规则
        String ids = StringUtils.join( electricityPriceStructureRuleIds, "," );
        HashMap<String, Object> map = new HashMap<>();
        map.put( "ids",ids );
        map.put("state", BoolLogic.YES.getCode() );
        map.put( "updateTime",DateUtil.date() );
        electricityPriceStructureRuleCustomMapper.batchDeleteStructureRuleByIds(map);
        //根据体系规则id查询出体系规则下对应的季节id ,根据季节id删除季节与分时信息
        HashMap<String, Object> seasonQueryMap = new HashMap<>();
        seasonQueryMap.put( "structureRuleIds",ids );
        seasonQueryMap.put( "state", BoolLogic.NO.getCode());
        List<ElectricitySeasonSection> electricitySeasonSections = electricityPriceSeasonSectionCustomMapper.querySeasonSectionIdsByStructureRuleIds( seasonQueryMap );
        //多个季节区间公用一个季节区间id,需要去重,得到体系规则下的多个季节列表
        Set<String> seasonSectionIds = electricitySeasonSections.stream().map( ElectricitySeasonSection::getSeasonSectionId ).collect( Collectors.toSet() );
        batchDeleteSeasonAndTime(seasonSectionIds);

    }

    /**
     * 删除季节分时与分时区间
     */
    public void batchDeleteSeasonAndTime(Set<String> seasonSectionIds){
        String seasonIdsString = StringUtils.join( seasonSectionIds, "," );
        //根据季节sectionId删除季节与分时信息
        Map<String, Object> map = new HashMap<>();
        map.put( "seasonSectionIds",seasonIdsString );
        map.put("state", BoolLogic.YES.getCode() );
        map.put( "updateTime",DateUtil.date() );
        electricityPriceSeasonSectionCustomMapper.batchDeleteSeasonSectionBySectionIds(map );
        electricityTimeSectionCustomMapper.batchDeleteTimeSectionBySeasonSectionIds(map);
    }

    /**
     * 批量删除规则与价格,与设备绑定关系
     */
    public void batchDeleteRulePriceAndEquipment(List<Long> electricityPriceRuleIds){
        String ruleIdsString= StringUtils.join( electricityPriceRuleIds, "," );
        Map<String,Object> map=new HashMap<>();
        map.put( "ruleIds", ruleIdsString);
        map.put( "updateTime",DateUtil.date() );
        map.put( "state",BoolLogic.YES.getCode());
        electricityPriceRuleCustomMapper.bacthDeletePriceRuleByRuleIds( map );
        electricityPriceCustomMapper.batchDeletePriceByRuleIds( map );
        HashMap<String, Object> equipmentMap = new HashMap<>();
        equipmentMap.put( "ruleIds", ruleIdsString);
        equipmentMap.put( "updateTime",DateUtil.date() );
        equipmentMap.put( "state",BoolLogic.YES.getCode());
        electricityPriceEquipmentCustomMapper.batchDeleteEquipmentBindingByConditions( equipmentMap );
    }



    /**
     * 创建体系
     */
    public String createStructure(ElectricityPriceStructureUpdateBO electricityPriceStructureUpdateBO,String versionId){
        ElectricityPriceStructure electricityPriceStructure = ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.electricityPriceStructureBOTOPO( electricityPriceStructureUpdateBO );
        String structureId = IdUtil.simpleUUID();
        electricityPriceStructure.setStructureId(structureId);
        electricityPriceStructure.setState(BoolLogic.NO.getCode());
        electricityPriceStructure.setVersionId(versionId);
        electricityPriceStructure.setCreateTime(DateUtil.date());
        electricityPriceStructureMapper.insert(electricityPriceStructure);
        Long[] defStructureRuleId = {null};
        //创建体系规则 季节分时与分时区间
        electricityPriceStructureUpdateBO.getElectricityPriceStructureRuleUpdateBOList().forEach( t->{
            //创建体系规则
            ElectricityPriceStructureRule electricityPriceStructureRule = ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.electricityPriceStructureRuleBOTOPO( t );
            electricityPriceStructureRule.setStructureId( structureId );
            String structureRuleId = IdUtil.simpleUUID();
            electricityPriceStructureRule.setStructureRuleId( structureRuleId );
            electricityPriceStructureRule.setState(BoolLogic.NO.getCode());
            electricityPriceStructureRule.setCreateTime(DateUtil.date());
            electricityPriceStructureRuleCustomMapper.insertElectricityPriceStructureRule(electricityPriceStructureRule);
            //todo 尚未确定默认编码怎么存储的
            if(electricityPriceStructureRule.getStrategies().equals( "default" )
                    && electricityPriceStructureRule.getVoltageLevels().equals( "default" ) && electricityPriceStructureRule.getIndustries().equals( "default" )){
                defStructureRuleId[0]=electricityPriceStructureRule.getId();
            }

            Long structureRuleAutoId=electricityPriceStructureRule.getId();
            //创建季节分时与分时区间
            t.getElectricityPriceSeasonUpdateReqVOList().forEach( season->{
                createSeasonsAndTimes(season,structureId,structureRuleAutoId);
            } );
            //判断规则属于哪个体系规则下 ->最后会判断出三要素属于哪个体系规则,如果匹配不上的属于默认体系规则
            electricityPriceStructureUpdateBO.getElectricityPriceUpdateBOList().forEach( p->{
                String industry = p.getIndustry();
                String strategy = p.getStrategy();
                String voltageLevel = p.getVoltageLevel();
                List<String> industryList = Arrays.asList( electricityPriceStructureRule.getIndustries().split( "," ) );
                List<String> strategyList = Arrays.asList( electricityPriceStructureRule.getStrategies().split( "," ) );
                List<String> voltageLevelList = Arrays.asList( electricityPriceStructureRule.getVoltageLevels().split( "," ) );
                //为了区分默认体系规则跟非默认的
                if(industryList.stream().anyMatch(i->i.equals( industry ) ) && strategyList.stream().anyMatch(s->s.equals(strategy))
                        && voltageLevelList.stream().anyMatch( v->v.equals( voltageLevel ) )){
                    p.setStructureRuleId( String.valueOf(electricityPriceStructureRule.getId()) );
                }
            } );
        } );

        //创建规则与价格
        electricityPriceStructureUpdateBO.getElectricityPriceUpdateBOList().forEach( p->{
            Long belongStructureRuleId=defStructureRuleId[0];
            if(Optional.ofNullable(p.getStructureRuleId()).isPresent()){
                belongStructureRuleId=Long.valueOf(p.getStructureRuleId());
            }
            createPriceAndRule(p,versionId,structureId,belongStructureRuleId);
        } );

        return String.valueOf( electricityPriceStructure.getId() );
    }

    /**
     * 创建体系规则跟季节
     * @param electricityPriceStructureRuleUpdateBOList
     * @param structureId
     */
    public void createStructureRuleAndSeason(List<ElectricityPriceStructureRuleUpdateBO> electricityPriceStructureRuleUpdateBOList,Long structureId){
        //创建体系规则
        electricityPriceStructureRuleUpdateBOList.forEach( t->{
            ElectricityPriceStructureRule electricityPriceStructureRule = ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.electricityPriceStructureRuleBOTOPO( t );
            electricityPriceStructureRule.setStructureId( String.valueOf(structureId) );
            String structureRuleId = IdUtil.simpleUUID();
            electricityPriceStructureRule.setStructureRuleId( structureRuleId );
            electricityPriceStructureRule.setState(BoolLogic.NO.getCode());
            electricityPriceStructureRule.setCreateTime(DateUtil.date());
            electricityPriceStructureRuleCustomMapper.insertElectricityPriceStructureRule(electricityPriceStructureRule);
            Long structureRuleAutoId=electricityPriceStructureRule.getId();
            //创建季节分时与分时区间
            t.getElectricityPriceSeasonUpdateReqVOList().forEach( season->{
                createSeasonsAndTimes(season,String.valueOf( structureId ),structureRuleAutoId);
            } );
        } );
    }




    /**
     * 创建规则与价格
     * @param p
     * @param versionId
     * @param structureId
     * @param structureRuleId
     */
    private void createPriceAndRule(ElectricityPriceUpdateBO p, String versionId, String structureId, Long structureRuleId) {
        ElectricityPriceRule electricityPriceRule = ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.electricityPriceRuleBOTOPO( p );
        String ruleId = IdUtil.simpleUUID();
        electricityPriceRule.setRuleId( ruleId );
        electricityPriceRule.setVersionId( versionId );
        electricityPriceRule.setStructureId(structureId);
        electricityPriceRule.setStructureRuleId( String.valueOf( structureRuleId ) );
        electricityPriceRule.setState(Integer.valueOf(BoolLogic.NO.getCode()));
        electricityPriceRule.setCreateTime(DateUtil.date());
        electricityPriceRule.setTenantId( tenantId );
        electricityPriceRule.setTenantName( tenantName );
        priceRuleMapper.insert(electricityPriceRule );

        ElectricityPrice electricityPrice = ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.electricityPriceBOTOPO( p );
        String detailId = IdUtil.simpleUUID();
        electricityPrice.setDetailId( detailId);
        electricityPrice.setRuleId( ruleId );
        electricityPrice.setTenantId( tenantId );
        electricityPrice.setTenantName( tenantName );
        electricityPrice.setState(Integer.valueOf(BoolLogic.NO.getCode()));
        electricityPrice.setCreateTime(DateUtil.date());
        priceMapper.insert( electricityPrice);
    }

    /**
     * 创建季节分时与分时区间,以及体系规则
     */
    public void createSeasonsAndTimes(ElectricityPriceSeasonUpdateBO electricityPriceSeasonUpdateBO,String structureId,Long structureRuleAutoId){
        //根据季节区间创建季节区间表,同一个季节公用一个季节区间id
        String seasonSectionId = IdUtil.simpleUUID();
        electricityPriceSeasonUpdateBO.getSeasonDateList().forEach( t->{
            ElectricitySeasonSection electricitySeasonSection=new ElectricitySeasonSection();
            electricitySeasonSection.setStructureId( structureId );
            electricitySeasonSection.setStructureRuleId( String.valueOf( structureRuleAutoId ) );
            electricitySeasonSection.setSeasonSectionId(seasonSectionId );
            electricitySeasonSection.setSeasonSectionName( electricityPriceSeasonUpdateBO.getSeasonName() );
            electricitySeasonSection.setSeaStartDate( t.getSeaStartDate() );
            electricitySeasonSection.setSeaEndDate( t.getSeaEndDate() );
            electricitySeasonSection.setState(BoolLogic.NO.getCode());
            electricitySeasonSection.setCreateTime(DateUtil.date());
            seasonSectionMapper.insert(electricitySeasonSection);
            //以季节为维度插入分时数据
            electricityPriceSeasonUpdateBO.getElectricityPriceStrategyBOList().forEach( f->{
                batchCreateTime(f.getElectricityTimeSectionUpdateBOList(),seasonSectionId, f.getCompare(), f.getTemperature());
            } );
        } );
    }
}