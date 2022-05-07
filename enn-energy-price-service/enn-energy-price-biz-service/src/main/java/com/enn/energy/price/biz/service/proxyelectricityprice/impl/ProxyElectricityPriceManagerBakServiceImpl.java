package com.enn.energy.price.biz.service.proxyelectricityprice.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.enn.energy.price.biz.service.bo.proxyprice.*;
import com.enn.energy.price.biz.service.convertMapper.ElectricityPriceVersionBOConvertMapper;
import com.enn.energy.price.biz.service.convertMapper.ElectricityPriceVersionViewConvertMapper;
import com.enn.energy.price.biz.service.proxyelectricityprice.ProxyElectricityPriceManagerBakService;
import com.enn.energy.price.common.constants.CommonConstant;
import com.enn.energy.price.common.enums.BoolLogic;
import com.enn.energy.price.common.enums.StateEum;
import com.enn.energy.price.common.enums.StrategyEnum;
import com.enn.energy.price.common.error.ErrorCodeEnum;
import com.enn.energy.price.common.error.PriceException;
import com.enn.energy.price.dal.mapper.ext.ExtElectricityPriceDictionaryMapper;
import com.enn.energy.price.dal.mapper.ext.proxyprice.ElectricityPriceRuleCustomMapper;
import com.enn.energy.price.dal.mapper.ext.proxyprice.ElectricityPriceEquipmentCustomMapper;
import com.enn.energy.price.dal.mapper.ext.proxyprice.ElectricityPriceStructureCustomMapper;
import com.enn.energy.price.dal.mapper.ext.proxyprice.ElectricityPriceVersionCustomMapper;
import com.enn.energy.price.dal.mapper.mbg.*;
import com.enn.energy.price.dal.po.mbg.*;
import com.enn.energy.price.dal.po.view.ElectricityPriceEquipmentView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
public class ProxyElectricityPriceManagerBakServiceImpl implements ProxyElectricityPriceManagerBakService {

    @Autowired
    ElectricityPriceVersionCustomMapper electricityPriceVersionCustomMapper;

    @Autowired
    ElectricityPriceStructureCustomMapper electricityPriceStructureCustomMapper;

    @Autowired
    ElectricityPriceEquipmentCustomMapper electricityPriceEquipmentCustomMapper;

    @Resource
    ElectricityPriceRuleCustomMapper priceRuleExtMapper;

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
    private ExtElectricityPriceDictionaryMapper priceDictionaryMapper;


    /**
     * @param priceVersionStructuresCreateBO
     * @return Boolean
     * @describtion 创建版本
     * @author sunjidong
     * @date 2022/5/2 14:48
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createPriceVersionStructures(ElectricityPriceVersionStructuresCreateBO priceVersionStructuresCreateBO) {
        //1、通过生效时间以及省编码等字段校验新建的版本是否重复
        ElectricityPriceVersionCreateBO priceVersionCreateBO = priceVersionStructuresCreateBO.getPriceVersionCreateBO();
        ElectricityPriceVersion priceVersionPO = ElectricityPriceVersionBOConvertMapper.INSTANCE.priceVersionCreateBOToPO(priceVersionCreateBO);
        priceVersionPO.setState(StateEum.NORMAL.getValue());
        long countByPriceVersion = electricityPriceVersionCustomMapper.countByPriceVersionExample(priceVersionPO);
        if (countByPriceVersion > 0) {
            throw new PriceException(ErrorCodeEnum.REPEAT_VERSION.getErrorCode(), ErrorCodeEnum.REPEAT_VERSION.getErrorMsg());
        }

        //2、先找到与老版本绑定的全量数据,后面会通过这个数据筛选出设备与规则的绑定
        String lastVersionId = priceVersionStructuresCreateBO.getPriceVersionCreateBO().getLastVersionId();
        List<ElectricityPriceEquipmentView> ruleEquipmentBindRecordList = new ArrayList<>();
        if (ObjectUtil.isNotNull(lastVersionId)) {
            ElectricityPriceEquipment priceEquipmentExample = new ElectricityPriceEquipment();
            priceEquipmentExample.setTenantId(priceVersionStructuresCreateBO.getPriceVersionCreateBO().getTenantId());
            priceEquipmentExample.setVersionId(lastVersionId);
            priceEquipmentExample.setEquipmentId(priceVersionStructuresCreateBO.getPriceVersionCreateBO().getEquipmentId());
            priceEquipmentExample.setSystemCode(priceVersionStructuresCreateBO.getPriceVersionCreateBO().getSystemCode());
            priceEquipmentExample.setState(StateEum.NORMAL.getValue());
            ruleEquipmentBindRecordList = electricityPriceEquipmentCustomMapper.listRuleEquipmentBindRecords(priceEquipmentExample);
        }
        //创建时间,后面统一使用这个作为创建时间
        DateTime createTime = DateUtil.date();
        //3、创建版本
        String versionId = IdUtil.simpleUUID();
        priceVersionPO.setVersionId(versionId);
        priceVersionPO.setCreateTime(createTime);
        electricityPriceVersionMapper.insert(priceVersionPO);
        //最终匹配到的设备与规则的绑定关系
        List<ElectricityPriceEquipmentView> matchedPriceEquipBindRecords = new ArrayList<>();
        //4、创建所有体系以及对应的季节分时区间以及电价规则
        List<ElectricityPriceStructureAndRuleAndSeasonCreateBO> priceStructureAndRuleAndSeasonCreateBOList = priceVersionStructuresCreateBO.getPriceStructureAndRuleAndSeasonCreateBOList().list;
        //4.1、遍历所有待新增的体系以及季节分时区间，以及规则、电价
        for (ElectricityPriceStructureAndRuleAndSeasonCreateBO priceStructureAndRuleAndSeasonCreateBO : priceStructureAndRuleAndSeasonCreateBOList) {
            //存放经筛选过的匹配当前体系下的设备与规则的绑定数据
            List<ElectricityPriceEquipmentView> leftRuleEquipmentBindRecordList = new ArrayList<>();
            //存放当前体系下的季节分时的三要素组合
            List<ElectricityPriceStructureRule> priceStructureRuleList = new ArrayList<>();
            List<ElectricityPriceRule> priceRuleList = new ArrayList<>();
            //4.1.1、先创建具体的体系
            ElectricityPriceStructureCreateBO priceStructureCreateBO = priceStructureAndRuleAndSeasonCreateBO.getPriceStructureCreateBO();
            ElectricityPriceStructure electricityPriceStructure = ElectricityPriceVersionBOConvertMapper.INSTANCE.priceStructureCreateBOToPO(priceStructureCreateBO);
            String structureId = IdUtil.simpleUUID();
            electricityPriceStructure.setStructureId(structureId);
            electricityPriceStructure.setState(BoolLogic.NO.getCode());
            electricityPriceStructure.setVersionId(versionId);
            electricityPriceStructure.setCreateTime(createTime);
            electricityPriceStructureMapper.insert(electricityPriceStructure);
            //获取当前体系的父体系id、省市区
            String parentId = electricityPriceStructure.getParentid();
            String provinceCode = electricityPriceStructure.getProvinceCode();
            String cityCodes = electricityPriceStructure.getCityCodes();
            String districtCodes = electricityPriceStructure.getDistrictCodes();
            //根据老版本id以及老的体系id，先去除一波数据
            leftRuleEquipmentBindRecordList = ruleEquipmentBindRecordList.stream().filter(ruleEquipmentBindRecord -> {
                return lastVersionId.equals(ruleEquipmentBindRecord.getVersionId()) && parentId.equals(ruleEquipmentBindRecord.getStructureId());
            }).collect(Collectors.toList());

            //剔除绑定关系中不适用于当前体系区域的绑定数据
            leftRuleEquipmentBindRecordList = leftRuleEquipmentBindRecordList.stream().filter(leftRuleEquipmentBindRecord -> {
                //todo 先将updator作为企业所在区，后面需要更改
                String updator = leftRuleEquipmentBindRecord.getUpdator();
                String[] area = updator.split("-");
                if (area.length == 3) {
                    return provinceCode.contains(area[0]) && cityCodes.contains(area[0]) && districtCodes.contains(area[0]);
                } else if (area.length == 2) {
                    return cityCodes.contains(area[0]) && districtCodes.contains(area[0]);
                }
                return false;
            }).collect(Collectors.toList());

            //2.2、创建体系季节对应的三要素、季节以及分时
            List<ElectricityPriceStructureRuleCreateBO> priceStructureRuleCreateBOList = priceStructureAndRuleAndSeasonCreateBO.getPriceStructureRuleCreateBOList().list;
            for (ElectricityPriceStructureRuleCreateBO priceStructureRuleCreateBO : priceStructureRuleCreateBOList) {
                ElectricityPriceStructureRule priceStructureRule = ElectricityPriceVersionBOConvertMapper.INSTANCE.priceStructureRuleCreateBOToPO(priceStructureRuleCreateBO);
                String structureRuleId = IdUtil.simpleUUID();
                priceStructureRule.setStructureId(structureId);
                priceStructureRule.setStructureRuleId(structureRuleId);
                priceStructureRule.setState(BoolLogic.NO.getCode());
                priceStructureRule.setCreateTime(createTime);
                priceStructureRuleMapper.insert(priceStructureRule);
                priceStructureRuleList.add(priceStructureRule);
                //构建季节区间
                List<ElectricitySeasonCreateBO> seasonCreateBOList = priceStructureRuleCreateBO.getSeasonCreateBOList().list;
                seasonCreateBOList.forEach(seasonCreateBO -> {
                    String seasonName = seasonCreateBO.getSeasonSectionName();
                    String seasonSectionId = IdUtil.simpleUUID();
                    //季节区间
                    List<ElectricitySeasonSectionCreateBO> seasonSectionCreateBOList = seasonCreateBO.getSeasonSectionCreateBOList().list;
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
                    List<ElectricityTimeSectionCreateBO> timeSectionCreateBOList = seasonCreateBO.getTimeSectionCreateBOList().list;
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
            }

            //2.3、创建电价规则以及电价明细
            List<ElectricityPriceRuleCreateBO> priceRuleCreateBOList = priceStructureAndRuleAndSeasonCreateBO.getPriceRuleCreateBOList().list;
            for (ElectricityPriceRuleCreateBO priceRuleCreateBO : priceRuleCreateBOList) {
                List<ElectricityPriceEquipmentView> existsRuleEquipmentBindRecordList = new ArrayList(leftRuleEquipmentBindRecordList);
                //构建电价规则
                ElectricityPriceRule electricityPriceRule = ElectricityPriceVersionBOConvertMapper.INSTANCE.priceRuleCreateBOToPO(priceRuleCreateBO);
                String priceRuleId = IdUtil.simpleUUID();
                electricityPriceRule.setRuleId(priceRuleId);
                electricityPriceRule.setVersionId(versionId);
                electricityPriceRule.setStructureId(structureId);
                electricityPriceRule.setTenantId(priceVersionStructuresCreateBO.getPriceVersionCreateBO().getTenantId());
                electricityPriceRule.setTenantName(priceVersionStructuresCreateBO.getPriceVersionCreateBO().getTenantName());
                electricityPriceRule.setState(StateEum.NORMAL.getValue());
                electricityPriceRule.setCreateTime(createTime);
                //先找到季节分时那边三要素组合的默认类型
                ElectricityPriceStructureRule defaultPriceStructureRule = priceStructureRuleList.stream().filter(priceStructureRule -> {
                    return CommonConstant.DEFAULT_TYPE.equals(priceStructureRule.getIndustries());
                }).collect(Collectors.toList()).get(0);
                //生成电价明细并插入数据库
                generatePriceRule(defaultPriceStructureRule, priceStructureRuleList, electricityPriceRule);
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
                electricityPrice.setState(StateEum.NORMAL.getValue());
                electricityPrice.setCreateTime(createTime);
                priceMapper.insert(electricityPrice);
                //根据三要素是否能匹配到当前体系下的所有三要素，剔除未能匹配到的绑定关系数据
                existsRuleEquipmentBindRecordList = existsRuleEquipmentBindRecordList.stream().filter(existRuleEquipmentBindRecord -> {
                    return existRuleEquipmentBindRecord.getIndustry().equals(electricityPriceRule.getIndustry())
                            && existRuleEquipmentBindRecord.getStrategy().equals(electricityPriceRule.getStrategy())
                            && existRuleEquipmentBindRecord.getVoltageLevel().equals(electricityPriceRule.getVoltageLevel());
                }).collect(Collectors.toList());
                for (ElectricityPriceEquipmentView priceEquipmentView : existsRuleEquipmentBindRecordList) {
                    priceEquipmentView.setEquipmentId(priceVersionPO.getEquipmentId());
                    priceEquipmentView.setEquipmentName(priceVersionPO.getEquipmentName());
                    priceEquipmentView.setVersionId(priceVersionPO.getVersionId());
                    priceEquipmentView.setTenantId(priceVersionPO.getTenantId());
                    priceEquipmentView.setTenantName(priceVersionPO.getTenantName());
                    priceEquipmentView.setSystemCode(priceVersionPO.getSystemCode());
                    priceEquipmentView.setStructureId(structureId);
                    priceEquipmentView.setStructureRuleId(electricityPriceRule.getStructureRuleId());
                    priceEquipmentView.setRuleId(priceRuleId);
                    priceEquipmentView.setId(null);
                    priceEquipmentView.setCreateTime(createTime);
                    priceEquipmentView.setCreator("随便先写一个");
                    priceEquipmentView.setUpdateTime(null);
                    priceEquipmentView.setUpdator(null);
                }
                matchedPriceEquipBindRecords.addAll(existsRuleEquipmentBindRecordList);
            }
            //插入当前版本下的所有绑定关系
            matchedPriceEquipBindRecords.forEach(matchedPriceEquipBindRecord -> {
                ElectricityPriceEquipment priceEquipment = ElectricityPriceVersionViewConvertMapper.INSTANCE.priceVersionViewToPo(matchedPriceEquipBindRecord);
                electricityPriceEquipmentMapper.insert(priceEquipment);
            });
        }
        return Boolean.TRUE;
    }

    /**
     * @param priceStructureRuleList,priceRuleList
     * @describtion 通过匹配季节三要素与规则三要素，生成规则电价明细
     * @author sunjidong
     * @date 2022/5/3 9:07
     */
    private void generatePriceRule(ElectricityPriceStructureRule defaultPriceStructureRule,
                                   List<ElectricityPriceStructureRule> priceStructureRuleList,
                                   ElectricityPriceRule priceRule) {
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
        if (matchFail) {
            priceRule.setStructureRuleId(defaultPriceStructureRule.getStructureRuleId());
        }
        //更新电价规则
        priceRuleMapper.insert(priceRule);
    }





    /**
     * @describtion 校验电价体系以及电价规则
     * @author sunjidong
     * @date 2022/5/6 9:41
     * @param
     * @return ElectricityPriceStructureAndRuleValidateRespBO
     */
    @Override
    public ElectricityPriceStructureAndRuleValidateRespBO validateStructureAndRule(ElectricityPriceStructureAndRuleValidateBO structureAndRuleValidateBO) {
        //校验结果BO
        ElectricityPriceStructureAndRuleValidateRespBO validateRespBO = new ElectricityPriceStructureAndRuleValidateRespBO();
        String versionId = structureAndRuleValidateBO.getVersionId();
        //1、校验季节分时对应的三要素组合是否存在重复
        List<ElectricityPriceStructureRuleCreateBO> priceStructureRuleValidateList = structureAndRuleValidateBO.getPriceStructureRuleValidateReqVOList().list;
        int beforeDistinctSize = priceStructureRuleValidateList.size();
        priceStructureRuleValidateList = priceStructureRuleValidateList.stream().distinct().collect(Collectors.toList());
        int afterDistinctsize = priceStructureRuleValidateList.size();
        if (beforeDistinctSize != afterDistinctsize) {
            validateRespBO.setIfExistDuplicatedStructureRule(Boolean.TRUE);
            return validateRespBO;
        }

        //2、校验是否存在季节、分时的区间
        Date startDate = structureAndRuleValidateBO.getStartDate();
        String startDateStr = DateUtil.formatDate(startDate);
        String year = startDateStr.split(CommonConstant.DATE_SPLIT)[0];
        boolean validateResult = false;
        DateTime startTimeOfWholeDay = DateUtil.parse(CommonConstant.DAY_EXAMPLE_ZERO, DatePattern.NORM_DATETIME_PATTERN);
        DateTime endTimeOfWholeDay = DateUtil.parse(CommonConstant.DAY_EXAMPLE_NINE, DatePattern.NORM_DATETIME_PATTERN);
        //一天的经过的ms
        long msOfWholeDay = DateUtil.between(startTimeOfWholeDay, endTimeOfWholeDay, DateUnit.MS);
        DateTime firstDateOfYear = DateUtil.parse(year + CommonConstant.DATE_SPLIT + CommonConstant.FIRST_DAY_OF_YEAR, DatePattern.NORM_DATE_PATTERN);
        DateTime endDateOfYear = DateUtil.parse(year + CommonConstant.DATE_SPLIT + CommonConstant.LAST_DAY_OF_YEAR, DatePattern.NORM_DATE_PATTERN);
        //一年经过的天数
        long allYearDays = DateUtil.between(firstDateOfYear, endDateOfYear, DateUnit.DAY);
        for (ElectricityPriceStructureRuleCreateBO structureRuleCreateBO : priceStructureRuleValidateList) {
            //获取季节区间
            List<ElectricitySeasonSectionCreateBO> allSeasonSectionCreateBOList = new ArrayList<>();
            List<ElectricitySeasonCreateBO> seasonCreateBOList = structureRuleCreateBO.getSeasonCreateBOList().list;
            for (ElectricitySeasonCreateBO seasonCreateBO : seasonCreateBOList) {
                //获取某一个季节下的所有季节区间
                List<ElectricitySeasonSectionCreateBO> SeasonSectionCreateBOList = seasonCreateBO.getSeasonSectionCreateBOList().list;
                //获取某个季节下的所有分时区间
                ValidationList<ElectricityTimeSectionCreateBO> timeSectionCreateBOList = seasonCreateBO.getTimeSectionCreateBOList();
                if (CollUtil.isNotEmpty(timeSectionCreateBOList)) {
                    //根据温度来分区成默认的以及温度修正策略的分时区间
                    Map<Boolean, List<ElectricityTimeSectionCreateBO>> booleanTimeSectionListMap = timeSectionCreateBOList.stream()
                            .collect(Collectors.partitioningBy(timeSectionCreateBO -> {
                                return StrUtil.isBlank(timeSectionCreateBO.getTemperature());
                            }));
                    //默认分时区间集合
                    List<ElectricityTimeSectionCreateBO> defaultTimeSectionCreateBOList = booleanTimeSectionListMap.get(Boolean.TRUE);
                    validateResult = intersectionOfTime(defaultTimeSectionCreateBOList);
                    if (validateResult) {
                        validateRespBO.setIfExistIntersectionTimeSection(Boolean.TRUE);
                        return validateRespBO;
                    }
                    //校验默认分时区间是否满一天
                    validateResult = ifReachWholeDayTime(msOfWholeDay, defaultTimeSectionCreateBOList);
                    if (!validateResult) {
                        validateRespBO.setIfNotReachWholeDayTimeSection(Boolean.TRUE);
                        return validateRespBO;
                    }

                    //修正策略分时区间集合
                    List<ElectricityTimeSectionCreateBO> amendTimeSectionCreateBOList = booleanTimeSectionListMap.get(Boolean.FALSE);
                    validateResult = intersectionOfTime(amendTimeSectionCreateBOList);
                    if (validateResult) {
                        validateRespBO.setIfExistIntersectionTimeSection(Boolean.TRUE);
                        return validateRespBO;
                    }
                    //校验修正策略分时区间是否满一天
                    validateResult = ifReachWholeDayTime(msOfWholeDay, amendTimeSectionCreateBOList);
                    if (!validateResult) {
                        validateRespBO.setIfNotReachWholeDayTimeSection(Boolean.TRUE);
                        return validateRespBO;
                    }
                }
                allSeasonSectionCreateBOList.addAll(SeasonSectionCreateBOList);
            }
            //2.1、校验季节区间是否存在交集
            validateResult = intersectionOfDate(year, allSeasonSectionCreateBOList);
            if (validateResult) {
                validateRespBO.setIfExistIntersectionSeasonSectionRule(Boolean.TRUE);
                return validateRespBO;
            }

            //2.2、校验季节区间是否满一年
            validateResult = ifReachWholeYear(year, allYearDays, allSeasonSectionCreateBOList);
            if (!validateResult) {
                validateRespBO.setIfExistIntersectionSeasonSectionRule(Boolean.TRUE);
                return validateRespBO;
            }
        }

        //3、校验电价规则
        List<ElectricityPriceRuleCreateBO> priceRuleValidateReqVOList = structureAndRuleValidateBO.getPriceRuleValidateReqVOList().list;
        //3.1、首先校验是否存在重复的三要素体系项
        int beforeDistinctRuleList = priceRuleValidateReqVOList.size();
        priceRuleValidateReqVOList = priceRuleValidateReqVOList.stream().distinct().collect(Collectors.toList());
        int afterDistinctRuleList = priceRuleValidateReqVOList.size();
        if(beforeDistinctRuleList != afterDistinctRuleList){
            validateRespBO.setIfExistDuplicatedRule(Boolean.TRUE);
            return validateRespBO;
        }
        //3.2、编辑状态下，校验是否修改了已绑定设备的规则
        if (StrUtil.isNotBlank(versionId)) {
            List<ElectricityPriceRuleCreateBO> editPriceRuleValidateReqVOList = priceRuleValidateReqVOList.stream()
                    .filter(priceRuleValidateReqVO -> {
                        return StrUtil.isNotBlank(priceRuleValidateReqVO.getRuleId());
                    }).collect(Collectors.toList());
            List<String> ruleIdList = editPriceRuleValidateReqVOList.stream().map(editPriceRuleValidateReqVO -> {
                return editPriceRuleValidateReqVO.getRuleId();
            }).collect(Collectors.toList());
            //先找到修改过的
            if(CollUtil.isNotEmpty(ruleIdList)){
                List<String> editPriceRuleSerialNoList = getCantEditPriceRuleSerialNos(editPriceRuleValidateReqVOList, ruleIdList);
                if(CollUtil.isNotEmpty(editPriceRuleSerialNoList) && DateUtil.compare(structureAndRuleValidateBO.getStartDate(), DateUtil.date()) <= 0){
                    validateRespBO.setCantEditPriceRules(editPriceRuleSerialNoList);
                    return validateRespBO;
                }
            }
        }

        //3.3、校验电价类型、策略、电压等级
        String provinceCode = structureAndRuleValidateBO.getProvinceCode();
        Map<String,Object> paramMap = new HashMap<>(4);
        paramMap.put("province", structureAndRuleValidateBO.getProvinceCode());
        paramMap.put("state", 0);
        //查询到该省下的三要素字典数据
        List<ElectricityPriceDictionary> electricityPriceDictionaries = priceDictionaryMapper.selectDictionaryByCondition(paramMap);
        Map<Integer, List<ElectricityPriceDictionary>> structureGroupMap = electricityPriceDictionaries.stream().collect(Collectors.groupingBy(priceDictionarie -> {
            return priceDictionarie.getType();
        }));
        //电价类型集合
        List<ElectricityPriceDictionary> industryList = structureGroupMap.get(0);
        //定价类型集合
        List<ElectricityPriceDictionary> strategyList = structureGroupMap.get(1);
        //电压等级集合
        List<ElectricityPriceDictionary> voltageLevelList = structureGroupMap.get(2);
        if(CollUtil.isEmpty(industryList) || CollUtil.isEmpty(strategyList) || CollUtil.isEmpty(voltageLevelList)){
            throw new PriceException(ErrorCodeEnum.STRUCTURE_NOT_EXISTS.getErrorCode(),ErrorCodeEnum.STRUCTURE_NOT_EXISTS.getErrorMsg());
        }
        List<String> notExistRuleSerialNoList = validateStructure(CommonConstant.INDUSTRY, industryList, priceRuleValidateReqVOList);
        if(CollUtil.isNotEmpty(notExistRuleSerialNoList)){
            validateRespBO.setIllegalIndustry(notExistRuleSerialNoList);
            return validateRespBO;
        }
        notExistRuleSerialNoList = validateStructure(CommonConstant.STRATEGY, strategyList, priceRuleValidateReqVOList);
        if(CollUtil.isNotEmpty(notExistRuleSerialNoList)){
            validateRespBO.setIllegalIndustry(notExistRuleSerialNoList);
            return validateRespBO;
        }
        notExistRuleSerialNoList = validateStructure(CommonConstant.VOLTAGELEVEL, voltageLevelList, priceRuleValidateReqVOList);
        if(CollUtil.isNotEmpty(notExistRuleSerialNoList)){
            validateRespBO.setIllegalIndustry(notExistRuleSerialNoList);
            return validateRespBO;
        }

        //3.4、校验 当为两部制时，是否缺少容需量用电价格
        Map<String, List<ElectricityPriceRuleCreateBO>> priceRuleValidateReqVOListGroupingByStrategy = priceRuleValidateReqVOList.stream().collect(Collectors.groupingBy(priceRuleValidateReqVO -> {
            return priceRuleValidateReqVO.getStrategy();
        }));
        //两部制
        List<ElectricityPriceRuleCreateBO> doubleStrategyPriceRuleCreateBOList = priceRuleValidateReqVOListGroupingByStrategy.get(String.valueOf(StrategyEnum.DOUBLESTRAGEY.getCode()));
        List<String> doubleStrategyPriceRuleList = getLackCapacityAndDemandCapacityPriceList(StrategyEnum.DOUBLESTRAGEY.getCode(), doubleStrategyPriceRuleCreateBOList);
        if(CollUtil.isNotEmpty(doubleStrategyPriceRuleList)){
            validateRespBO.setLackCapacityAndDemandCapacityPriceList(doubleStrategyPriceRuleList);
            return validateRespBO;
        }
        //单一制
        List<ElectricityPriceRuleCreateBO> singleStrategyPriceRuleCreateBOList = priceRuleValidateReqVOListGroupingByStrategy.get(String.valueOf(StrategyEnum.SINGLESTRAGEY.getCode()));
        List<String> singleStrategyPriceRuleList = getLackCapacityAndDemandCapacityPriceList(StrategyEnum.SINGLESTRAGEY.getCode(), singleStrategyPriceRuleCreateBOList);
        if(CollUtil.isNotEmpty(singleStrategyPriceRuleList)){
            validateRespBO.setNeedLessCapacityAndDemandCapacityPriceList(singleStrategyPriceRuleList);
            return validateRespBO;
        }

        //-----校验是否缺少电度电价
        //先将默认的和非默认的分开
        Map<Boolean, List<ElectricityPriceStructureRuleCreateBO>> priceStructureRulePartitions
                = priceStructureRuleValidateList.stream()
                .collect(Collectors.partitioningBy(priceStructureRuleValidate -> {
            return CommonConstant.DEFAULT_TYPE.equals(priceStructureRuleValidate.getIndustries());
        }));
        //默认的
        List<ElectricityPriceStructureRuleCreateBO> defaultStructureRuleCreateBOList = priceStructureRulePartitions.get(Boolean.TRUE);
        //自定义的
        List<ElectricityPriceStructureRuleCreateBO> customStructureRuleCreateBOList = priceStructureRulePartitions.get(Boolean.FALSE);
        //自定义里没有配置分时的体系集合
        List<ElectricityPriceStructureRuleCreateBO> noExistTimeSectionStructureRuleList
                = customStructureRuleCreateBOList.stream().filter(priceStructureRule -> {
            boolean matchSuccess = true;
            for (ElectricitySeasonCreateBO seasonCreateBO : priceStructureRule.getSeasonCreateBOList()) {
                if (CollUtil.isNotEmpty(seasonCreateBO.getTimeSectionCreateBOList())) {
                    matchSuccess = false;
                    break;
                }
            }
            return matchSuccess;
        }).collect(Collectors.toList());
        //判断默认的是否配置分时
        boolean noTimeSection = defaultStructureRuleCreateBOList.get(0).getSeasonCreateBOList().stream()
                .allMatch(seasonCreateBO -> {
                    if (CollUtil.isEmpty(seasonCreateBO.getTimeSectionCreateBOList())) {
                        return true;
                    }
                    return false;
                });
        //找到三要素匹配，没有配置分时的规则
        List<String> lackDistributionPriceList = priceRuleValidateReqVOList.stream().filter(priceRuleValidateReqVO -> {
            boolean matchSuccess = false;
            for (ElectricityPriceStructureRuleCreateBO ruleCreateBO : noExistTimeSectionStructureRuleList) {
                if (ruleCreateBO.getIndustries().contains(priceRuleValidateReqVO.getIndustry())
                        && ruleCreateBO.getStrategies().contains(priceRuleValidateReqVO.getStrategy())
                        && ruleCreateBO.getVoltageLevels().contains(priceRuleValidateReqVO.getVoltageLevel())
                        && StrUtil.isBlank(priceRuleValidateReqVO.getElectricityPriceCreateBO().getConsumptionPrice())) {
                    matchSuccess = true;
                }
            }
            if (!matchSuccess && noTimeSection && StrUtil.isBlank(priceRuleValidateReqVO.getElectricityPriceCreateBO().getConsumptionPrice())) {
                matchSuccess = true;
            }
            return matchSuccess;
        }).map(priceRuleValidateReqVO -> {
            return priceRuleValidateReqVO.getSerialNo();
        }).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(lackDistributionPriceList)){
            validateRespBO.setLackDistributionPriceList(lackDistributionPriceList);
            return validateRespBO;
        }

        //----校验是否缺少分时电价
        //自定义配置，存在分时的集合
        customStructureRuleCreateBOList.removeAll(noExistTimeSectionStructureRuleList);
        //获取到自定义中每个体系对应的分时类型
        List<StructureRuleAndTimeTypeBO> customStructureRuleAndTimeTypeBOList = customStructureRuleCreateBOList.stream().map(customStructureRuleCreateBO -> {
            List<ElectricityTimeSectionCreateBO> customTimeSectionCreateBOList = new ArrayList<>();
            for (ElectricitySeasonCreateBO createBO : customStructureRuleCreateBO.getSeasonCreateBOList().list) {
                customTimeSectionCreateBOList.addAll(createBO.getTimeSectionCreateBOList());
            }
            Map<String, List<ElectricityTimeSectionCreateBO>> customPeriodsTimeSectionMap = customTimeSectionCreateBOList.stream().collect(Collectors.groupingBy(timeSectionCreateBO -> {
                return timeSectionCreateBO.getPeriods();
            }));
            Set<String> periods = customPeriodsTimeSectionMap.keySet();
            StructureRuleAndTimeTypeBO structureRuleAndTimeTypeBO = new StructureRuleAndTimeTypeBO();
            structureRuleAndTimeTypeBO.setIndustries(customStructureRuleCreateBO.getIndustries());
            structureRuleAndTimeTypeBO.setStrategies(customStructureRuleCreateBO.getStrategies());
            structureRuleAndTimeTypeBO.setVoltageLevels(customStructureRuleCreateBO.getVoltageLevels());
            structureRuleAndTimeTypeBO.setTimeTypeList(periods);
            return structureRuleAndTimeTypeBO;
        }).collect(Collectors.toList());
        //获取默认的体系对应的分时
        StructureRuleAndTimeTypeBO defaultStructureRuleAndTimeTypeBO = new StructureRuleAndTimeTypeBO();
        if(!noTimeSection){
            List<ElectricityTimeSectionCreateBO> defaultTimeSectionCreateBOList = new ArrayList<>();
            for (ElectricitySeasonCreateBO createBO : defaultStructureRuleCreateBOList.get(0).getSeasonCreateBOList().list) {
                defaultTimeSectionCreateBOList.addAll(createBO.getTimeSectionCreateBOList());
            }
            //默认的体系对应的分时类型
            Map<String, List<ElectricityTimeSectionCreateBO>> defaultStructureRuleAndTimeTypeBOMap
                    = defaultTimeSectionCreateBOList.stream()
                    .collect(Collectors.groupingBy(timeSectionCreateBO -> {
                        return timeSectionCreateBO.getPeriods();
                    }));

            Set<String> periods = defaultStructureRuleAndTimeTypeBOMap.keySet();
            defaultStructureRuleAndTimeTypeBO.setIndustries(CommonConstant.DEFAULT_TYPE);
            defaultStructureRuleAndTimeTypeBO.setStrategies(CommonConstant.DEFAULT_TYPE);
            defaultStructureRuleAndTimeTypeBO.setVoltageLevels(CommonConstant.DEFAULT_TYPE);
            defaultStructureRuleAndTimeTypeBO.setTimeTypeList(periods);
        }

        List<String> lackTimePartPriceList = priceRuleValidateReqVOList.stream().filter(priceRuleValidateReqVO -> {
            boolean matchSuccess = false;
            for (StructureRuleAndTimeTypeBO structureRuleAndTimeTypeBO : customStructureRuleAndTimeTypeBOList) {
                if (structureRuleAndTimeTypeBO.getIndustries().contains(priceRuleValidateReqVO.getIndustry())
                        && structureRuleAndTimeTypeBO.getStrategies().contains(priceRuleValidateReqVO.getStrategy())
                        && structureRuleAndTimeTypeBO.getVoltageLevels().contains(priceRuleValidateReqVO.getVoltageLevel())) {
                    matchSuccess = true;
                    //再判断分时价格有没有填写
                    for (String timeType : structureRuleAndTimeTypeBO.getTimeTypeList()) {
                        switch (timeType){
                            case CommonConstant.SHARP:
                                return StrUtil.isEmpty(priceRuleValidateReqVO.getElectricityPriceCreateBO().getSharpPrice());
                            case CommonConstant.PEEK:
                                return StrUtil.isEmpty(priceRuleValidateReqVO.getElectricityPriceCreateBO().getPeakPrice());
                            case CommonConstant.LEVEL:
                                return StrUtil.isEmpty(priceRuleValidateReqVO.getElectricityPriceCreateBO().getLevelPrice());
                            case CommonConstant.VALLEY:
                                return StrUtil.isEmpty(priceRuleValidateReqVO.getElectricityPriceCreateBO().getValleyPrice());
                        }
                    }
                }
            }
            //获取到默认的体系对应的分时类型
            if(!matchSuccess && !noTimeSection){
                for (String timeType : defaultStructureRuleAndTimeTypeBO.getTimeTypeList()) {
                    switch (timeType){
                        case CommonConstant.SHARP:
                            if(StrUtil.isEmpty(priceRuleValidateReqVO.getElectricityPriceCreateBO().getSharpPrice())){
                                matchSuccess = true;
                                break;
                            }
                        case CommonConstant.PEEK:
                            if(StrUtil.isEmpty(priceRuleValidateReqVO.getElectricityPriceCreateBO().getPeakPrice())){
                                matchSuccess = true;
                                break;
                            }
                        case CommonConstant.LEVEL:
                            if(StrUtil.isEmpty(priceRuleValidateReqVO.getElectricityPriceCreateBO().getLevelPrice())){
                                matchSuccess = true;
                                break;
                            }
                        case CommonConstant.VALLEY:
                            if(StrUtil.isEmpty(priceRuleValidateReqVO.getElectricityPriceCreateBO().getValleyPrice())){
                                matchSuccess = true;
                                break;
                            }
                    }
                }
               return matchSuccess;
            }
            return matchSuccess;
        }).map(priceRuleValidateReqVO -> {
            return  priceRuleValidateReqVO.getSerialNo();
        }).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(lackTimePartPriceList)){
            validateRespBO.setLackTimePartPriceList(lackTimePartPriceList);
            return validateRespBO;
        }

        //校验策略修正前后分时内容是否完全相同
        for (ElectricityPriceStructureRuleCreateBO structureRuleCreateBO : priceStructureRuleValidateList) {
            for (ElectricitySeasonCreateBO seasonCreateBO : structureRuleCreateBO.getSeasonCreateBOList()) {
                ValidationList<ElectricityTimeSectionCreateBO> timeSectionCreateBOList = seasonCreateBO.getTimeSectionCreateBOList();
                if(CollUtil.isNotEmpty(timeSectionCreateBOList)){
                    Map<Boolean, List<ElectricityTimeSectionCreateBO>> booleanListMap = timeSectionCreateBOList.stream()
                            .collect(Collectors.partitioningBy(timeSection -> {
                                return StrUtil.isNotBlank(timeSection.getTemperature());
                            }));
                    List<ElectricityTimeSectionCreateBO> temperatureTimeSectionCreateBOList = booleanListMap.get(Boolean.TRUE);
                    List<ElectricityTimeSectionCreateBO> defaultTimeSectionCreateBOList = booleanListMap.get(Boolean.FALSE);
                    if (CollUtil.isNotEmpty(temperatureTimeSectionCreateBOList)
                        && CollUtil.isNotEmpty(defaultTimeSectionCreateBOList)) {
                        String temperatureTimeStr = temperatureTimeSectionCreateBOList.stream().sorted(Comparator.comparing(temperatureTimeSection -> {
                            return temperatureTimeSection.getStartTime();
                        })).map(temperatureTimeSection -> {
                            return temperatureTimeSection.getPeriods() + temperatureTimeSection.getStartTime() + temperatureTimeSection.getEndTime();
                        }).collect(Collectors.joining(CommonConstant.DATE_SPLIT));

                        String defaultTimeStr = defaultTimeSectionCreateBOList.stream().sorted(Comparator.comparing(defaultTimeSection -> {
                            return defaultTimeSection.getStartTime();
                        })).map(defaultTimeSection -> {
                            return defaultTimeSection.getPeriods() + defaultTimeSection.getStartTime() + defaultTimeSection.getEndTime();
                        }).collect(Collectors.joining(CommonConstant.DATE_SPLIT));
                        if(temperatureTimeStr.equals(defaultTimeStr)){
                            validateRespBO.setIfExistSameTimeSection(Boolean.TRUE);
                            return validateRespBO;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * @describtion 1、获取当为两部制时，未填写容需量价格时的店家规则 2、获取当为一部制时，填写了容需量用电价格的规则
     * @author sunjidong
     * @date 2022/5/6 21:28
     * @param
     * @return
     */
    private List<String> getLackCapacityAndDemandCapacityPriceList(Byte type, List<ElectricityPriceRuleCreateBO> strategyPriceRuleCreateBOList) {
        return strategyPriceRuleCreateBOList.stream()
                .filter(priceRuleValidateReqVO -> {
                    if(type.equals(StrategyEnum.DOUBLESTRAGEY.getCode())){
                        if(priceRuleValidateReqVO.getStrategy()
                                .equals(String.valueOf(StrategyEnum.DOUBLESTRAGEY.getCode()))){
                            if(StrUtil.isBlank(priceRuleValidateReqVO.getTransformerCapacityPrice())
                                    || StrUtil.isBlank(priceRuleValidateReqVO.getMaxCapacityPrice())){
                                return true;
                            }
                        }
                    }
                    if(type.equals(StrategyEnum.SINGLESTRAGEY.getCode())){
                        if(priceRuleValidateReqVO.getStrategy()
                                .equals(String.valueOf(StrategyEnum.SINGLESTRAGEY.getCode()))){
                            if(StrUtil.isNotBlank(priceRuleValidateReqVO.getTransformerCapacityPrice())
                                    || StrUtil.isNotBlank(priceRuleValidateReqVO.getMaxCapacityPrice())){
                                return true;
                            }
                        }
                    }
                    return false;
                }).map(priceRuleValidateReqVO -> {
                    return priceRuleValidateReqVO.getSerialNo();
                }).collect(Collectors.toList());
    }

    private List<String> getCantEditPriceRuleSerialNos(List<ElectricityPriceRuleCreateBO> editPriceRuleValidateReqVOList, List<String> ruleIdList) {
        List<ElectricityPriceRule> electricityPriceRules = priceRuleExtMapper.selectRulesByRuleIdList(ruleIdList);
        //确认被修改过的电价规则Id集合
        List<String> beModifiedRuleList = editPriceRuleValidateReqVOList.stream().filter(editPriceRuleValidateReqVO -> {
            String industry = editPriceRuleValidateReqVO.getIndustry();
            String strategy = editPriceRuleValidateReqVO.getStrategy();
            String voltageLevel = editPriceRuleValidateReqVO.getVoltageLevel();
            boolean matchFail = true;
            for (ElectricityPriceRule priceRule : electricityPriceRules) {
                String industryDadabase = priceRule.getIndustry();
                String strategyDatabase = priceRule.getStrategy();
                String voltageLevelDatabase = priceRule.getVoltageLevel();
                if (industryDadabase.equals(industry)
                        && strategyDatabase.equals(strategy)
                        && voltageLevelDatabase.equals(voltageLevel)) {
                    matchFail = false;
                    break;
                }
            }
            return matchFail;
        }).map(beEditedPriceRule -> {
            return beEditedPriceRule.getRuleId();
        }).collect(Collectors.toList());

        //根据ruleId集合判断是否已绑定设备
        List<ElectricityPriceEquipment> electricityPriceEquipments = electricityPriceEquipmentCustomMapper.listRuleEquipmentBindRecordsByRuleIdList(beModifiedRuleList);
        List<String> cantEditPriceRules = editPriceRuleValidateReqVOList.stream().filter(editPriceRuleValidateReqVO -> {
            boolean matchSuccess = false;
            for (ElectricityPriceEquipment priceEquipment : electricityPriceEquipments) {
                if(editPriceRuleValidateReqVO.getRuleId().equals(priceEquipment.getRuleId())){
                    matchSuccess = true;
                    break;
                }
            }
            return matchSuccess;
        }).map(editPriceRuleValidateReqVO -> {
            return editPriceRuleValidateReqVO.getSerialNo();
        }).collect(Collectors.toList());
        return cantEditPriceRules;
    }

    /**
     * 判断日期是否存在交集
     *
     * @param
     * @return boolean
     */
    private boolean intersectionOfDate(String year, List<ElectricitySeasonSectionCreateBO> allSeasonSectionCreateBOList) {
        for (int i = 0; i < allSeasonSectionCreateBOList.size(); i++) {
            //a
            String startDateStr = year + allSeasonSectionCreateBOList.get(i).getSeaStartDate();
            DateTime startDate = DateUtil.parse(startDateStr, DatePattern.NORM_DATE_PATTERN);
            //b
            String endDateStr = year + allSeasonSectionCreateBOList.get(i).getSeaEndDate();
            DateTime endDate = DateUtil.parse(endDateStr, DatePattern.NORM_DATE_PATTERN);
            for (int j = i + 1; j < allSeasonSectionCreateBOList.size(); j++) {
                //c
                String toBeComparedStartDateStr = year + allSeasonSectionCreateBOList.get(j).getSeaStartDate();
                DateTime toBeComparedStartDate = DateUtil.parse(toBeComparedStartDateStr, DatePattern.NORM_DATE_PATTERN);
                //d
                String toBeComparedEndDateStr = year + allSeasonSectionCreateBOList.get(j).getSeaEndDate();
                DateTime toBeComparedEndDate = DateUtil.parse(toBeComparedEndDateStr, DatePattern.NORM_DATE_PATTERN);
                boolean compare1 = DateUtil.compare(startDate, toBeComparedEndDate) <= 0;
                boolean compare2 = DateUtil.compare(endDate, toBeComparedStartDate) >= 0;
                // 如果 a<d && b>c ，则必有交集
                if (compare1 && compare2) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断时间是否存在交集
     *
     * @param
     * @return boolean
     */
    private boolean intersectionOfTime(List<ElectricityTimeSectionCreateBO> allTimeSectionCreateBOList) {
        for (int i = 0; i < allTimeSectionCreateBOList.size(); i++) {
            //a
            String startTimeStr = CommonConstant.DAY_EXAMPLE + allTimeSectionCreateBOList.get(i).getStartTime() + CommonConstant.TIME_ZERO_SPLIT;
            DateTime startTime = DateUtil.parse(startTimeStr, DatePattern.NORM_DATETIME_PATTERN);
            //b
            String endTimeStr = CommonConstant.DAY_EXAMPLE + allTimeSectionCreateBOList.get(i).getEndTime() + CommonConstant.TIME_ZERO_SPLIT;
            DateTime endTime = DateUtil.parse(endTimeStr, DatePattern.NORM_DATETIME_PATTERN);
            endTime = DateUtil.offsetSecond(endTime, -1);

            for (int j = i + 1; j < allTimeSectionCreateBOList.size(); j++) {
                //c
                String toBeComparedStartTimeStr = CommonConstant.DAY_EXAMPLE
                        + allTimeSectionCreateBOList.get(j).getStartTime()
                        + CommonConstant.TIME_ZERO_SPLIT;
                DateTime toBeComparedStartTime = DateUtil.parse(toBeComparedStartTimeStr, DatePattern.NORM_DATETIME_PATTERN);
                //d
                String toBeComparedEndTimeStr = CommonConstant.DAY_EXAMPLE
                        + allTimeSectionCreateBOList.get(j).getEndTime()
                        + CommonConstant.TIME_ZERO_SPLIT;
                DateTime toBeComparedEndTime = DateUtil.parse(toBeComparedEndTimeStr, DatePattern.NORM_DATETIME_PATTERN);
                toBeComparedEndTime = DateUtil.offsetSecond(toBeComparedEndTime, -1);
                boolean compare1 = DateUtil.compare(startTime, toBeComparedEndTime) < 0;
                boolean compare2 = DateUtil.compare(endTime, toBeComparedStartTime) > 0;
                // 如果 a<d && b>c ，则必有交集
                if (compare1 && compare2) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @describtion 校验分时区间是否满一天
     * @author sunjidong
     * @date 2022/5/6 14:52
     * @param
     * @return
     */
    private boolean ifReachWholeDayTime(long msOfWholeDay, List<ElectricityTimeSectionCreateBO> timeSectionCreateBOList){
        timeSectionCreateBOList = timeSectionCreateBOList.stream().peek(timeSectionCreateBO -> {
            String startTimeStr = CommonConstant.DAY_EXAMPLE + timeSectionCreateBO.getStartTime() + CommonConstant.TIME_ZERO_SPLIT;
            timeSectionCreateBO.setStartTime(startTimeStr);
            String endTimeStr = CommonConstant.DAY_EXAMPLE + timeSectionCreateBO.getEndTime() + CommonConstant.TIME_ZERO_SPLIT;
            timeSectionCreateBO.setEndTime(endTimeStr);
        }).collect(Collectors.toList());
        long dayMs = 0L;
        for (ElectricityTimeSectionCreateBO timeSectionCreateBO : timeSectionCreateBOList) {
            DateTime endTime = DateUtil.parse(timeSectionCreateBO.getEndTime(), DatePattern.NORM_DATETIME_PATTERN);
            endTime = DateUtil.offsetSecond(endTime, -1);
            DateTime startTime = DateUtil.parse(timeSectionCreateBO.getStartTime(), DatePattern.NORM_DATETIME_PATTERN);
            dayMs += DateUtil.between(startTime, endTime, DateUnit.MS);
        }
        if (dayMs != msOfWholeDay) {
            return false;
        }
        return true;
    }

    /**
     * @describtion 校验季节区间是否满一年
     * @author sunjidong
     * @date 2022/5/6 14:52
     * @param
     * @return
     */
    private boolean ifReachWholeYear(String year, long allYearDays, List<ElectricitySeasonSectionCreateBO> allSeasonSectionCreateBOList){
        allSeasonSectionCreateBOList = allSeasonSectionCreateBOList.stream().peek(seasonSectionCreateBO -> {
            seasonSectionCreateBO.setSeaStartDate(year + seasonSectionCreateBO.getSeaStartDate());
            seasonSectionCreateBO.setSeaEndDate(year + seasonSectionCreateBO.getSeaEndDate());
        }).collect(Collectors.toList());
        long days = 0L;
        for (ElectricitySeasonSectionCreateBO seasonSectionCreateBO : allSeasonSectionCreateBOList) {
            DateTime endDateTime = DateUtil.parse(seasonSectionCreateBO.getSeaEndDate(), DatePattern.NORM_DATE_PATTERN);
            DateTime startDateTime = DateUtil.parse(seasonSectionCreateBO.getSeaStartDate(), DatePattern.NORM_DATE_PATTERN);
            days += DateUtil.between(startDateTime, endDateTime, DateUnit.DAY);
        }
        if (days != allYearDays) {
            return false;
        }
        return true;
    }

/**
 * @describtion 遍历校验规则的三要素是否合法
 * @author sunjidong
 * @date 2022/5/6 20:52
 * @param
 * @return  List<String>
 */
    private List<String> validateStructure(String type, List<ElectricityPriceDictionary> structureList, List<ElectricityPriceRuleCreateBO> priceRuleValidateReqVOList){
        return priceRuleValidateReqVOList.stream().filter(priceRuleValidateReqVO -> {
            boolean matchFlag = true;
            String industry = priceRuleValidateReqVO.getIndustry();
            String strategy = priceRuleValidateReqVO.getStrategy();
            String voltageLevel = priceRuleValidateReqVO.getVoltageLevel();
            for (ElectricityPriceDictionary industryDictionary : structureList) {
                if(type.equals(CommonConstant.INDUSTRY)){
                    if (industryDictionary.equals(industry)) {
                        matchFlag = false;
                        break;
                    }
                }
                if(type.equals(CommonConstant.STRATEGY)){
                    if (industryDictionary.equals(strategy)) {
                        matchFlag = false;
                        break;
                    }
                }
                if(type.equals(CommonConstant.VOLTAGELEVEL)){
                    if (industryDictionary.equals(voltageLevel)) {
                        matchFlag = false;
                        break;
                    }
                }
            }
            return matchFlag;
        }).map(notExistRule -> {
            return notExistRule.getSerialNo();
        }).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        List<String> a = new ArrayList<>();
        a.add("a");
        a.add("b");
        a.add("c");
        List<String> d = a.stream().filter(x -> {
            return x.equals("d");
        }).collect(Collectors.toList());
        System.out.println(d.size());
    }
}