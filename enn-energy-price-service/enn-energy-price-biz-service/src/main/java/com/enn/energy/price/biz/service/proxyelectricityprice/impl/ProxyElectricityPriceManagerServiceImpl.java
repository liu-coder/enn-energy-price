package com.enn.energy.price.biz.service.proxyelectricityprice.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.enn.energy.price.biz.service.bo.proxyprice.*;
import com.enn.energy.price.biz.service.convertMapper.ElectricityPriceVersionBOConvertMapper;
import com.enn.energy.price.biz.service.convertMapper.ElectricityPriceVersionUpdateBOConverMapper;
import com.enn.energy.price.biz.service.proxyelectricityprice.ProxyElectricityPriceManagerService;
import com.enn.energy.price.common.constants.CommonConstant;
import com.enn.energy.price.common.enums.BoolLogic;
import com.enn.energy.price.common.enums.changeTypeEum;
import com.enn.energy.price.common.error.ErrorCodeEnum;
import com.enn.energy.price.dal.mapper.ext.proxyprice.*;
import com.enn.energy.price.dal.mapper.mbg.*;
import com.enn.energy.price.dal.po.mbg.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.rdfa.framework.biz.ro.RdfaResult;
import top.rdfa.framework.concurrent.api.exception.LockFailException;
import top.rdfa.framework.concurrent.redis.lock.RedissonRedDisLock;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
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
    ElectricityPriceVersionCustomMapper electricityPriceVersionExtMapper;

    @Autowired
    ElectricityPriceStructureCustomMapper electricityPriceStructureExtMapper;

    @Autowired
    ElectricityPriceEquipmentCustomMapper electricityPriceEquipmentExtMapper;

    @Autowired
    ElectricityPriceStructureRuleCustomMapper electricityPriceStructureRuleExtMapper;

    @Autowired
    ElectricityPriceSeasonSectionCustomMapper electricityPriceSeasonSectionExtMapper;

    @Autowired
    ElectricityTimeSectionCustomMapper electricityTimeSectionExtMapper;

    @Autowired
    ElectricityPriceRuleCustomMapper electricityPriceRuleExtMapper;

    @Autowired
    ElectricityPriceCustomMapper electricityPriceExtMapper;

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
        List<ElectricityPriceStructureUpdateBO> addCollect = electricityPriceStructureUpdateBOList.stream().filter( t -> StringUtils.isEmpty( t.getId() ) ).collect( Collectors.toList() );
        //1.2根据changeType判断是否修改
        List<ElectricityPriceStructureUpdateBO> updateCollect = electricityPriceStructureUpdateBOList.stream().filter( t -> t.getChangeType().equals( changeTypeEum.UPDATE.getType() ) ).collect( Collectors.toList() );
        String lockKey =  String.format("%s:%s:%s", CommonConstant.RedisKey.LOCK_PROXY_PRICE_VERSION_UPDATE_PREFIX,
                tenantId, electricityPriceVersionUpdateBO.getId());
        Lock lock = null;
        try {
            lock = redDisLock.lock(lockKey  );
            if (ObjectUtil.isNull( lock )) {
                return RdfaResult.fail( ErrorCodeEnum.RETRY_AFTER.getErrorCode(), ErrorCodeEnum.RETRY_AFTER.getErrorMsg() );
            }
            //1.3查询版本下面的体系列表比对哪些缺失
            List<Long> deleteIdList = queryDeleteStructureIds( electricityPriceStructureUpdateBOList, electricityPriceVersionUpdateBO.getId() );
            //2.1 增加体系直接增加
            addCollect.forEach( t->{
                createStructure(t,electricityPriceVersionUpdateBO.getId());
            } );
            //2.2 删除体系直接删除
            deleteIdList.forEach( this::deleteStructure );
            //2.3 修改体系(暂行覆盖)
            updateCollect.forEach( t->{
                updateStructure(t,electricityPriceVersionUpdateBO.getId());
            } );
        } catch (LockFailException e) {
            return RdfaResult.fail(ErrorCodeEnum.REIDS_LOCK_ERROR.getErrorCode(), ErrorCodeEnum.REIDS_LOCK_ERROR.getErrorMsg());
        }finally {
            redDisLock.unlock(lockKey);
        }
        return RdfaResult.success( true );

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RdfaResult<Boolean> deletePriceVersion(ElectricityPriceVersionDeleteBO electricityPriceVersionDeleteBO) {
        String lockKey =  String.format("%s:%s:%s", CommonConstant.RedisKey.LOCK_PROXY_PRICE_VERSION_UPDATE_PREFIX,
                tenantId, electricityPriceVersionDeleteBO.getId());
        Lock lock = null;
        try {
            lock = redDisLock.lock( lockKey );
            if (ObjectUtil.isNull( lock )) {
                return RdfaResult.fail( ErrorCodeEnum.RETRY_AFTER.getErrorCode(), ErrorCodeEnum.RETRY_AFTER.getErrorMsg() );
            }
            //根据电价版本id查询电价体系id
            List<ElectricityPriceStructure> electricityPriceStructures = electricityPriceStructureExtMapper.queryListByVersionId( electricityPriceVersionDeleteBO.getId() );
            //根据电价体系id删除
            electricityPriceStructures.forEach( t->{
                deleteStructure( t.getId() );
            } );
            //根据版本id删除电价体系与设备的关联关系
            electricityPriceEquipmentExtMapper.deleteEquipmentBindingByVersionId(electricityPriceVersionDeleteBO.getId());
            //查询当前版本的上个版本
            ElectricityPriceVersion electricityPriceVersion=new ElectricityPriceVersion();
            electricityPriceVersion.setProvince( electricityPriceVersionDeleteBO.getProvinceCode() );
            electricityPriceVersion.setStartDate( DateUtil.parse(electricityPriceVersionDeleteBO.getStartDate(), DatePattern.NORM_DATE_PATTERN) );
            electricityPriceVersion.setEndDate(  DateUtil.parse(electricityPriceVersionDeleteBO.getEndDate(), DatePattern.NORM_DATE_PATTERN));
            ElectricityPriceVersion beforePriceVersion = electricityPriceVersionExtMapper.queryBeforePriceVersion( electricityPriceVersion );
            //更新上一个版本 并删除当前版本
            beforePriceVersion.setEndDate( DateUtil.parse(electricityPriceVersionDeleteBO.getEndDate(), DatePattern.NORM_DATE_PATTERN) );
            beforePriceVersion.setUpdator( tenantId );
            beforePriceVersion.setUpdateTime( DateUtil.date() );
            electricityPriceVersionMapper.updateByPrimaryKey( beforePriceVersion );
            electricityPriceVersionMapper.deleteByPrimaryKey( Long.valueOf( electricityPriceVersionDeleteBO.getId() ) );
        } catch (LockFailException e) {
            return RdfaResult.fail(ErrorCodeEnum.REIDS_LOCK_ERROR.getErrorCode(), ErrorCodeEnum.REIDS_LOCK_ERROR.getErrorMsg());
        }finally {
            redDisLock.unlock(lockKey);
        }
        return RdfaResult.success(true);
    }

    /**
     * 查找哪些体系被删除了
     * @param electricityPriceStructureUpdateBOList
     * @return
     */
    private List<Long> queryDeleteStructureIds(List<ElectricityPriceStructureUpdateBO> electricityPriceStructureUpdateBOList,String versionId) {
        //根据版本id查询当前版本下的体系id列表
        List<ElectricityPriceStructure> electricityPriceStructures = electricityPriceStructureExtMapper.queryListByVersionId( versionId );
        //筛选出现有的id
        List<String> ids = electricityPriceStructureUpdateBOList.stream().map( ElectricityPriceStructureUpdateBO::getId ).filter( StringUtils::isNotEmpty ).collect( Collectors.toList() );
        //拿版本下的体系列表与现有的对比，得出哪些删除了
        return electricityPriceStructures.stream().map( ElectricityPriceStructure::getId ).collect( Collectors.toList() ).stream().filter( f -> !ids.contains(String.valueOf( f )  ) ).collect( Collectors.toList() );
    }

    /**
     * 修改体系
     * @param priceStructureUpdateBO
     */
    private void updateStructure(ElectricityPriceStructureUpdateBO priceStructureUpdateBO,String versionId) {
        //查询当前体系绑定的设备信息
        List<ElectricityPriceEquipment> electricityPriceEquipments = electricityPriceEquipmentExtMapper.queryEquipmentBindingByStructureId( priceStructureUpdateBO.getId() );
        //更新操作,先删除当前体系下的数据然后新增
        deleteStructure(Long.valueOf( priceStructureUpdateBO.getId() ));
        String structureId = createStructure( priceStructureUpdateBO, versionId );
        List<ElectricityPriceRule> electricityPriceRules = electricityPriceRuleExtMapper.queryRuleListByStructureId( structureId );
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




        /*//1.修改体系名称,体系区域
        ElectricityPriceStructure electricityPriceStructure = ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.electricityPriceStructureBOTOPO( priceStructureUpdateBO );
        electricityPriceStructure.setUpdator( tenantId );
        electricityPriceStructure.setUpdateTime( DateUtil.date() );
        electricityPriceStructure.setState(BoolLogic.NO.getCode() );
        electricityPriceStructureExtMapper.updateByPrimaryKey(electricityPriceStructure );
        //2.修改体系规则
        //2.1查询之前体系下的体系规则
        List<ElectricityPriceStructureRule> electricityPriceStructureRules = electricityPriceStructureRuleExtMapper.queryElectricityPriceRulesByStructureId( priceStructureUpdateBO.getId() );
        //2.1.1查找出当前体系下体系规则被删除的规则
        List<String> afterStructureRuleIds = priceStructureUpdateBO.getElectricityPriceStructureRuleUpdateBOList().stream().filter( t -> StringUtils.isNotEmpty( String.valueOf( t.getId() ) ) ).map( ElectricityPriceStructureRuleUpdateBO::getId ).collect( Collectors.toList() );
        List<Long> deleteStructureRuleIds = electricityPriceStructureRules.stream().filter( t -> !afterStructureRuleIds.contains( t ) ).collect( Collectors.toList() ).stream().map( ElectricityPriceStructureRule::getId ).collect( Collectors.toList() );
        //2.1.1.1 删除体系规则以及对应的季节分时与价格
        if(!CollectionUtils.isEmpty( deleteStructureRuleIds )){
            //2.1.1.1.1 删除季节分时
            deleteStructureRuleByStructureRuleIds(deleteStructureRuleIds);
            //2.1.1.1.2 删除规则与价格 根据体系规则找到对应的规则id
            List<ElectricityPriceStructureRule> priceRules = electricityPriceStructureRuleExtMapper.queryElectricityPriceRulesByStructureRule( StringUtils.join( deleteStructureRuleIds ) );
            batchDeleteRuleAndPrice(priceRules.stream().map( ElectricityPriceStructureRule::getId ).collect( Collectors.toList()) );
        }
        //2.1.2 筛选出新增的体系规则
        List<ElectricityPriceStructureRuleUpdateBO> addStructureRuleList = priceStructureUpdateBO.getElectricityPriceStructureRuleUpdateBOList().stream().filter( t -> StringUtils.isEmpty( t.getId() ) ).collect( Collectors.toList() );
        if(!CollectionUtils.isEmpty( addStructureRuleList )){

        }*/

    }



    /**
     * 删除体系
     * @param structureId 体系id
     */
    private void deleteStructure(Long structureId) {
        //根据体系id删除体系规则,季节分时,分时区间,规则,价格
        electricityPriceStructureMapper.deleteByPrimaryKey( structureId );
        //根据体系id查询体系规则,根据体系规则ids删除体系规则
        List<ElectricityPriceStructureRule> electricityPriceStructureRules = electricityPriceStructureRuleExtMapper.queryElectricityPriceRulesByStructureId( String.valueOf(structureId) );
        List<Long> electricityPriceStructureRuleIds = electricityPriceStructureRules.stream().map( ElectricityPriceStructureRule::getId ).collect( Collectors.toList() );
        deleteStructureRuleByStructureRuleIds(electricityPriceStructureRuleIds);
        //批量删除体系规则下对应的规则与价格(根据体系id)
        List<ElectricityPriceRule> electricityPriceRules = electricityPriceRuleExtMapper.queryRuleListByStructureId( String.valueOf( structureId ) );
        List<Long> electricityPriceRuleIds = electricityPriceRules.stream().map( ElectricityPriceRule::getId ).collect( Collectors.toList() );
        batchDeleteRuleAndPrice(electricityPriceRuleIds);
    }

    /**
     * 根据体系规则id删除体系规则以及对应的季节分时,分时区间
     */
    public void deleteStructureRuleByStructureRuleIds(List<Long> electricityPriceRuleIds){
        //根据体系规则id删除体系规则
        String ids = StringUtils.join( electricityPriceRuleIds, "," );
        electricityPriceStructureRuleExtMapper.batchDeleteStructureRuleByIds(ids);
        //根据体系规则id查询出体系规则下对应的季节id ,根据季节id删除季节与分时信息
        List<ElectricitySeasonSection> electricitySeasonSections = electricityPriceSeasonSectionExtMapper.querySeasonSectionIdsByStructureRuleIds( ids );
        List<Long> seasonIds = electricitySeasonSections.stream().map( ElectricitySeasonSection::getId ).collect( Collectors.toList() );
        batchDeleteSeasonAndTime(seasonIds);

    }

    /**
     * 删除季节分时与分时区间
     */
    public void batchDeleteSeasonAndTime(List<Long> seasonIds){
        String seasonIdsString = StringUtils.join( seasonIds, "," );
        //根据季节id删除季节与分时信息
        electricityPriceSeasonSectionExtMapper.batchDeleteSeasonSectionByIds(seasonIdsString );
        electricityTimeSectionExtMapper.batchDeleteTimeSectionBySeasonSectionIds(seasonIdsString);
    }

    /**
     * 批量删除规则与价格
     */
    public void batchDeleteRuleAndPrice(List<Long> electricityPriceRuleIds){
        String ruleIdsString= StringUtils.join( electricityPriceRuleIds, "," );
        electricityPriceRuleExtMapper.bacthDeletePriceRuleByRuleIds( ruleIdsString );
        electricityPriceExtMapper.batchDeletePriceByRuleIds( ruleIdsString );
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
        String[] defStructureRuleId = {null};
        //创建体系规则 季节分时与分时区间
        electricityPriceStructureUpdateBO.getElectricityPriceStructureRuleUpdateBOList().forEach( t->{
            //创建体系规则
            ElectricityPriceStructureRule electricityPriceStructureRule = ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.electricityPriceStructureRuleBOTOPO( t );
            electricityPriceStructureRule.setStructureId( structureId );
            String structureRuleId = IdUtil.simpleUUID();
            electricityPriceStructureRule.setStructureRuleId( structureRuleId );
            electricityPriceStructureRule.setState(BoolLogic.NO.getCode());
            electricityPriceStructureRule.setCreateTime(DateUtil.date());
            electricityPriceStructureRuleExtMapper.insertElectricityPriceStructureRule(electricityPriceStructureRule);
            Long structureRuleAutoId=electricityPriceStructure.getId();
            //创建季节分时与分时区间
            t.getElectricityPriceSeasonUpdateReqVOList().forEach( season->{
                createSeasonsAndTimes(season,structureId,structureRuleAutoId);
            } );
            //判断规则属于哪个体系规则下 ->最后会判断出三要素属于哪个体系规则,如果匹配不上的属于默认体系规则
            electricityPriceStructureUpdateBO.getElectricityPriceUpdateBOList().stream().filter( f->Optional.ofNullable(f.getStructureRuleId()).isPresent()).forEach( p->{
                String industry = p.getIndustry();
                String strategy = p.getStrategy();
                String voltageLevel = p.getVoltageLevel();
                List<String> industryList = Arrays.asList( electricityPriceStructureRule.getIndustries().split( "," ) );
                List<String> strategyList = Arrays.asList( electricityPriceStructureRule.getStrategies().split( "," ) );
                List<String> voltageLevelList = Arrays.asList( electricityPriceStructureRule.getVoltageLevels().split( "," ) );
                //为了区分默认体系规则跟非默认的
                if(industryList.size()>1 || strategyList.size()>1 || voltageLevelList.size()>1){
                    if(industryList.stream().anyMatch(i->i.equals( industry ) ) && strategyList.stream().anyMatch(s->s.equals(strategy))
                            && voltageLevelList.stream().anyMatch( v->v.equals( voltageLevel ) )){
                        p.setStructureRuleId( structureRuleId );
                    }
                }else{
                    //默认的体系规则,三个值长度都为1
                    defStructureRuleId[0] =structureRuleId;
                }
            } );
        } );

        //创建规则与价格
        electricityPriceStructureUpdateBO.getElectricityPriceUpdateBOList().forEach( p->{
            String belongStructureRuleId=defStructureRuleId[0];
            if(Optional.ofNullable(p.getStructureRuleId()).isPresent()){
                belongStructureRuleId=p.getStructureRuleId();
            }
            createPriceAndRule(p,versionId,structureId,belongStructureRuleId);
        } );

        return structureId;
    }

    /**
     * 创建规则与价格
     * @param p
     * @param versionId
     * @param structureId
     * @param structureRuleId
     */
    private void createPriceAndRule(ElectricityPriceUpdateBO p, String versionId, String structureId, String structureRuleId) {
        ElectricityPriceRule electricityPriceRule = ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.electricityPriceRuleBOTOPO( p );
        String ruleId = IdUtil.simpleUUID();
        electricityPriceRule.setRuleId( ruleId );
        electricityPriceRule.setVersionId( versionId );
        electricityPriceRule.setStructureId(structureId);
        electricityPriceRule.setStructureRuleId( structureRuleId );
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
                f.getElectricityTimeSectionUpdateBOList().forEach( g->{
                    ElectricityTimeSection electricityTimeSection=new ElectricityTimeSection();
                    String timeSectionId = IdUtil.simpleUUID();
                    electricityTimeSection.setSeasonSectionId(seasonSectionId);
                    electricityTimeSection.setTimeSectionId( timeSectionId );
                    electricityTimeSection.setStartTime( g.getStartTime() );
                    electricityTimeSection.setEndTime(g.getEndTime());
                    electricityTimeSection.setPeriods( g.getPeriods() );
                    electricityTimeSection.setState(BoolLogic.NO.getCode());
                    electricityTimeSection.setCreateTime(DateUtil.date());
                    electricityTimeSection.setCompare( f.getCompare() );
                    electricityTimeSection.setTemperature(f.getTemperature() );
                    timeSectionMapper.insertSelective( electricityTimeSection );
                } );
            } );
        } );
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


    /*LinkedMultiValueMap<String,Object> judgeStructureChange(ElectricityPriceVersionUpdateBO electricityPriceVersionUpdateBO){
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
    }*/
}