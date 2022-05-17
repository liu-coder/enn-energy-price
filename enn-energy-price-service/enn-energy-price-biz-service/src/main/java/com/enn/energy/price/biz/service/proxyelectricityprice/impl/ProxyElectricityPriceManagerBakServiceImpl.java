package com.enn.energy.price.biz.service.proxyelectricityprice.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import com.enn.energy.price.biz.service.bo.ElectricityPriceDictionaryBO;
import com.enn.energy.price.biz.service.bo.proxyprice.*;
import com.enn.energy.price.biz.service.convertMapper.CommonBOPOConvertMapper;
import com.enn.energy.price.biz.service.convertMapper.ElectricityPriceVersionUpdateBOConverMapper;
import com.enn.energy.price.biz.service.proxyelectricityprice.ProxyElectricityPriceManagerBakService;
import com.enn.energy.price.biz.service.proxyelectricityprice.ProxyElectricityPriceManagerService;
import com.enn.energy.price.common.constants.CommonConstant;
import com.enn.energy.price.common.enums.BoolLogic;
import com.enn.energy.price.common.enums.StateEum;
import com.enn.energy.price.common.enums.StrategyEnum;
import com.enn.energy.price.common.error.ErrorCodeEnum;
import com.enn.energy.price.common.error.PriceException;
import com.enn.energy.price.common.utils.SnowFlake;
import com.enn.energy.price.dal.mapper.ext.proxyprice.*;
import com.enn.energy.price.dal.mapper.mbg.*;
import com.enn.energy.price.dal.po.ext.ElectricityPriceDetailPO;
import com.enn.energy.price.dal.po.mbg.*;
import com.enn.energy.price.dal.po.view.ElectricityPriceEquipmentView;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.rdfa.framework.auth.client.UnifyAuthContextHolder;
import top.rdfa.framework.auth.facade.entity.UserInfo;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 代购电价service实现
 *
 * @author sunjidong
 * @date 2022/5/1
 **/
@Service
@Slf4j
public class ProxyElectricityPriceManagerBakServiceImpl implements ProxyElectricityPriceManagerBakService {

    @Resource
    ElectricityPriceVersionCustomMapper electricityPriceVersionCustomMapper;

    @Resource
    ElectricityPriceStructureCustomMapper electricityPriceStructureCustomMapper;

    @Resource
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
    private ProxyElectricityPriceManagerService priceManagerService;

    @Resource
    ElectricityPriceStructureRuleCustomMapper electricityPriceStructureRuleCustomMapper;

    @Resource
    ElectricityPriceSeasonSectionCustomMapper electricityPriceSeasonSectionCustomMapper;

    @Resource
    ElectricityTimeSectionCustomMapper electricityTimeSectionCustomMapper;

    @Resource
    ElectricityPriceCustomMapper electricityPriceCustomMapper;

    /**
     * 创建版本
     *
     * @return Boolean
     * @author sunjidong
     * @date 2022/5/2 14:48
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createPriceVersionStructures(ElectricityPriceVersionUpdateBO versionBO) {
        // todo 后期如果加上权限，就放开这边
        UserInfo userInfo = UnifyAuthContextHolder.getUserInfo();
        //最终匹配到的设备与规则的绑定关系
        List<ElectricityPriceEquipmentView> matchedPriceEquipBindRecords = new ArrayList<>();
        //创建时间,后面统一使用这个作为创建时间
        DateTime createTime = DateUtil.date();
        //版本下的所有体系
        List<ElectricityPriceStructureUpdateBO> versionStructureBOList = versionBO.getElectricityPriceStructureUpdateBOList();

        //1、通过生效时间以及省编码等字段校验新建的版本是否重复
        ElectricityPriceVersion priceVersionPO = ifDuplicateVersion(versionBO);

        //2、先找到与老版本绑定的全量数据,后面会通过这个数据筛选出设备与规则的绑定
        List<ElectricityPriceEquipmentView> ruleEquipmentBindRecordList = getAllEquipPriceBindRecords(versionBO);

        //3、创建具体的版本
        createVersionDetail(userInfo, priceVersionPO, createTime);
        //当前版本的uuid
        String versionId = String.valueOf(priceVersionPO.getVersionId());

        //4、创建所有体系以及对应的季节分时区间以及电价规则
        //4.1、遍历所有待新增的体系以及季节分时区间，以及规则、电价
        for (ElectricityPriceStructureUpdateBO versionStructureBO : versionStructureBOList) {
            //存放当前体系下的季节分时的三要素组合
            List<ElectricityPriceStructureRule> priceStructureRuleList = new ArrayList<>();
            List<ElectricityPriceRule> priceRuleList = new ArrayList<>();
            //4.1.1、先创建具体的体系
            ElectricityPriceStructure electricityPriceStructure = CommonBOPOConvertMapper.INSTANCE.priceStructureBOToPO(versionStructureBO);
            electricityPriceStructure.setStructureId(String.valueOf(SnowFlake.getInstance().nextId()));
            electricityPriceStructure.setState(BoolLogic.NO.getCode());
            electricityPriceStructure.setVersionId(versionId);
            electricityPriceStructure.setCreateTime(createTime);
            // todo 后期如果加上权限，就放开这边
//            electricityPriceStructure.setCreator(userInfo.getUserId());
            electricityPriceStructureMapper.insert(electricityPriceStructure);
            //当前体系的uuid
            String structureId = String.valueOf(electricityPriceStructure.getStructureId());
            //获取匹配到当前体系的绑定关系数据
            List<ElectricityPriceEquipmentView> leftRuleEquipmentBindRecordList = getElectricityPriceEquipmentViews(ruleEquipmentBindRecordList, electricityPriceStructure);

            //4.1.2、创建体系季节对应的三要素、季节以及分时
            List<ElectricityPriceStructureRuleUpdateBO> structureRuleBOList = versionStructureBO.getElectricityPriceStructureRuleUpdateBOList();
            createStructureRule(createTime, priceStructureRuleList, structureId, structureRuleBOList);

            //4.1.3、创建电价规则以及电价明细
            List<ElectricityPriceUpdateBO> priceRuleAndDetailBOList = versionStructureBO.getElectricityPriceUpdateBOList();
            createPriceRuleAndPrice(createTime, priceVersionPO, priceStructureRuleList, priceRuleList, structureId, leftRuleEquipmentBindRecordList, priceRuleAndDetailBOList);

            //根据三要素是否能匹配到当前体系下的所有三要素，剔除未能匹配到的绑定关系数据
            deleteBindRecordsNotSuitablePriceRule(matchedPriceEquipBindRecords, priceRuleList, leftRuleEquipmentBindRecordList);
        }
        //插入当前版本下的所有绑定关系
        matchedPriceEquipBindRecords.forEach(matchedPriceEquipBindRecord -> {
            ElectricityPriceEquipment priceEquipment = CommonBOPOConvertMapper.INSTANCE.priceVersionViewToPo(matchedPriceEquipBindRecord);
            electricityPriceEquipmentMapper.insert(priceEquipment);
        });
        return Boolean.TRUE;
    }

    /**
     * 获取匹配到当前体系的绑定关系数据
     *
     * @return ElectricityPriceEquipmentView
     * @author sunjidong
     * @date 2022/5/10 22:10
     */
    private List<ElectricityPriceEquipmentView> getElectricityPriceEquipmentViews(List<ElectricityPriceEquipmentView> ruleEquipmentBindRecordList, ElectricityPriceStructure electricityPriceStructure) {
        String parentId = electricityPriceStructure.getParentid();
//        String provinceCode = electricityPriceStructure.getProvinceCode();
//        String cityCodes = electricityPriceStructure.getCityCodes();
        String districtCodes = electricityPriceStructure.getDistrictCodes();
        //存放经筛选过的匹配当前体系下的设备与规则的绑定数据
        //根据老版本id以及老的体系id，先去除一波数据,保存的是当前体系下能够保留的绑定关系数据
        List<ElectricityPriceEquipmentView> leftRuleEquipmentBindRecordList = ruleEquipmentBindRecordList.stream()
                .filter(ruleEquipmentBindRecord ->
                        parentId.equals(ruleEquipmentBindRecord.getStructureId())
                ).collect(Collectors.toList());

        //剔除绑定关系中不适用于当前体系区域的绑定数据
        leftRuleEquipmentBindRecordList = leftRuleEquipmentBindRecordList.stream().filter(leftRuleEquipmentBindRecord -> {
            //todo 先将updator作为企业所在区，后面需要更改
            String updator = leftRuleEquipmentBindRecord.getUpdator();
            /*String[] area = updator.split(CommonConstant.DATE_SPLIT);
            if (area.length == 3) {
                return provinceCode.contains(area[0]) && cityCodes.contains(area[0]) && districtCodes.contains(area[0]);
            } else if (area.length == 2) {
                return cityCodes.contains(area[0]) && districtCodes.contains(area[0]);
            }*/
            return districtCodes.contains(updator);
        }).collect(Collectors.toList());
        return leftRuleEquipmentBindRecordList;
    }

    /**
     * 根据绑定数据的三要素是否能匹配到当前体系下的所有规则的三要素，剔除未能匹配到的绑定关系数据
     *
     * @author sunjidong
     * @date 2022/5/10 22:03
     */
    private void deleteBindRecordsNotSuitablePriceRule(List<ElectricityPriceEquipmentView> matchedPriceEquipBindRecords, List<ElectricityPriceRule> priceRuleList, List<ElectricityPriceEquipmentView> leftRuleEquipmentBindRecordList) {
        leftRuleEquipmentBindRecordList = leftRuleEquipmentBindRecordList.stream().filter(leftRuleEquipmentBindRecord -> {
            boolean matchResult = false;
            for (ElectricityPriceRule electricityPriceRule : priceRuleList) {
                if (leftRuleEquipmentBindRecord.getIndustry().equals(electricityPriceRule.getIndustry())
                        && leftRuleEquipmentBindRecord.getStrategy().equals(electricityPriceRule.getStrategy())
                        && leftRuleEquipmentBindRecord.getVoltageLevel().equals(electricityPriceRule.getVoltageLevel())) {
                    matchResult = true;
                    break;
                }
            }
            return matchResult;
        }).collect(Collectors.toList());
        matchedPriceEquipBindRecords.addAll(leftRuleEquipmentBindRecordList);
    }

    /**
     * 创建电价规则以及电价明细
     *
     * @author sunjidong
     * @date 2022/5/10 22:06
     */
    private void createPriceRuleAndPrice(DateTime createTime,
                                         ElectricityPriceVersion priceVersionPO,
                                         List<ElectricityPriceStructureRule> priceStructureRuleList,
                                         List<ElectricityPriceRule> priceRuleList,
                                         String structureId,
                                         List<ElectricityPriceEquipmentView> leftRuleEquipmentBindRecordList,
                                         List<ElectricityPriceUpdateBO> priceRuleAndDetailBOList) {
        for (ElectricityPriceUpdateBO priceRuleAndDetailBO : priceRuleAndDetailBOList) {
            //构建电价规则
            ElectricityPriceRule electricityPriceRule = CommonBOPOConvertMapper.INSTANCE.priceRuleAndDetailBOToPo(priceRuleAndDetailBO);
            electricityPriceRule.setRuleId(String.valueOf(SnowFlake.getInstance().nextId()));
            electricityPriceRule.setVersionId(priceVersionPO.getVersionId());
            electricityPriceRule.setStructureId(structureId);
            electricityPriceRule.setTenantId(priceVersionPO.getTenantId());
            electricityPriceRule.setTenantName(priceVersionPO.getTenantName());
            electricityPriceRule.setState(StateEum.NORMAL.getValue());
            electricityPriceRule.setCreateTime(createTime);
            //先找到季节分时那边三要素组合的默认类型
            ElectricityPriceStructureRule defaultPriceStructureRule = priceStructureRuleList.stream().filter(priceStructureRule ->
                    CommonConstant.DEFAULT_TYPE.equals(priceStructureRule.getIndustries())
            ).collect(Collectors.toList()).get(0);
            //生成电价规则并插入数据库
            generatePriceRule(defaultPriceStructureRule, priceStructureRuleList, electricityPriceRule);
            String priceRuleId = electricityPriceRule.getRuleId();
            priceRuleList.add(electricityPriceRule);

            //构建电价明细
//            ElectricityPriceCreateBO electricityPriceCreateBO = priceRuleAndDetailBO.getElectricityPriceCreateBO();
            ElectricityPrice electricityPrice = CommonBOPOConvertMapper.INSTANCE.priceRuleAndDetailBOToPO(priceRuleAndDetailBO);
            electricityPrice.setDetailId(String.valueOf(SnowFlake.getInstance().nextId()));
            electricityPrice.setRuleId(priceRuleId);
            electricityPrice.setTenantId(priceVersionPO.getTenantId());
            electricityPrice.setTenantName(priceVersionPO.getTenantName());
//            electricityPrice.setTransformerCapacityPrice(priceRuleAndDetailBO.getTransformerCapacityPrice());
//            electricityPrice.setMaxCapacityPrice(priceRuleAndDetailBO.getMaxCapacityPrice());
            electricityPrice.setState(StateEum.NORMAL.getValue());
            electricityPrice.setCreateTime(createTime);
            priceMapper.insert(electricityPrice);
            //为当前版本下的设备与电价的绑定关系中的版本id以及体系id等都重新赋值为当前版本下生成的id
            for (ElectricityPriceEquipmentView priceEquipmentView : leftRuleEquipmentBindRecordList) {
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
                priceEquipmentView.setCreator(priceVersionPO.getCreator());
                priceEquipmentView.setUpdateTime(null);
                priceEquipmentView.setUpdator(null);
            }
        }
    }

    /**
     * 创建体系季节对应的三要素、季节以及分时
     *
     * @author sunjidong
     * @date 2022/5/10 22:00
     */
    private void createStructureRule(DateTime createTime, List<ElectricityPriceStructureRule> priceStructureRuleList, String structureId, List<ElectricityPriceStructureRuleUpdateBO> structureRuleBOList) {
        for (ElectricityPriceStructureRuleUpdateBO structureRuleBO : structureRuleBOList) {
            ElectricityPriceStructureRule priceStructureRule = CommonBOPOConvertMapper.INSTANCE.structureRuleBOToPO(structureRuleBO);
            priceStructureRule.setStructureId(structureId);
            priceStructureRule.setStructureRuleId(String.valueOf(SnowFlake.getInstance().nextId()));
            priceStructureRule.setState(BoolLogic.NO.getCode());
            priceStructureRule.setCreateTime(createTime);
            priceStructureRuleMapper.insert(priceStructureRule);
            //当前体系规则的uuid
            String structureRuleId = String.valueOf(priceStructureRule.getStructureRuleId());
            priceStructureRuleList.add(priceStructureRule);
            //构建季节区间
            List<ElectricityPriceSeasonUpdateBO> seasonCreateBOList = structureRuleBO.getElectricityPriceSeasonUpdateReqVOList();
            seasonCreateBOList.forEach(seasonCreateBO -> {
                String seasonName = seasonCreateBO.getSeasonName();
                //季节id
                String seasonSectionId = String.valueOf(SnowFlake.getInstance().nextId());
                //季节区间
                List<SeasonDateBO> seasonDateBOList = seasonCreateBO.getSeasonDateList();
                for (SeasonDateBO seasonDateBO : seasonDateBOList) {
                    ElectricitySeasonSection seasonSection = CommonBOPOConvertMapper.INSTANCE.seasonDateBOToPO(seasonDateBO);
                    seasonSection.setStructureId(structureId);
                    seasonSection.setStructureRuleId(structureRuleId);
                    seasonSection.setSeasonSectionId(seasonSectionId);
                    seasonSection.setSeasonSectionName(seasonName);
                    seasonSection.setState(BoolLogic.NO.getCode());
                    seasonSection.setCreateTime(createTime);
                    seasonSectionMapper.insert(seasonSection);
                }
                //构建分时区间
                List<ElectricityPriceStrategyBO> structureRuleSeasonStrategyBOList = seasonCreateBO.getElectricityPriceStrategyBOList();
                for (ElectricityPriceStrategyBO structureRuleSeasonStrategyBO : structureRuleSeasonStrategyBOList) {
                    for (ElectricityTimeSectionUpdateBO seasonStrategyTimeSectionBO : structureRuleSeasonStrategyBO.getElectricityTimeSectionUpdateBOList()) {
                        ElectricityTimeSection timeSection = CommonBOPOConvertMapper.INSTANCE.seasonStrategyTimeSectionBOToPO(seasonStrategyTimeSectionBO);
                        String timeSectionId = String.valueOf(SnowFlake.getInstance().nextId());
                        timeSection.setSeasonSectionId(seasonSectionId);
                        timeSection.setTimeSectionId(timeSectionId);
                        timeSection.setCompare(structureRuleSeasonStrategyBO.getCompare());
                        timeSection.setTemperature(structureRuleSeasonStrategyBO.getTemperature());
                        timeSection.setState(BoolLogic.NO.getCode());
                        timeSection.setCreateTime(createTime);
                        timeSectionMapper.insert(timeSection);
                    }
                }
            });
        }
    }

    /**
     * 创建具体的版本
     *
     * @author sunjidong
     * @date 2022/5/10 21:48
     */
    private void createVersionDetail(UserInfo userInfo, ElectricityPriceVersion priceVersionPO, DateTime createTime) {
        String versionId = String.valueOf(SnowFlake.getInstance().nextId());
        priceVersionPO.setVersionId(versionId);
        priceVersionPO.setCreateTime(createTime);
        ElectricityPriceVersion lastPriceVersion = electricityPriceVersionCustomMapper.queryBeforePriceVersion(priceVersionPO);
        ElectricityPriceVersion nextPriceVersion = electricityPriceVersionCustomMapper.queryNextPriceVersion(priceVersionPO);
        if (ObjectUtil.isNotNull(lastPriceVersion)) {
            lastPriceVersion.setEndDate(DateUtil.offsetDay(priceVersionPO.getStartDate(), CommonConstant.NUMBER0));
        }
        if (ObjectUtil.isNull(nextPriceVersion)) {
            priceVersionPO.setEndDate(DateUtil.parse(CommonConstant.DEFAULT_END_DATE, DatePattern.NORM_DATE_PATTERN));
        } else {
            priceVersionPO.setEndDate(DateUtil.offsetDay(nextPriceVersion.getStartDate(), CommonConstant.NUMBER0));
        }
        // todo 后期如果加上权限，就放开这边
//        priceVersionPO.setCreator(userInfo.getUserId());
        //更新上一个版本
        if(ObjectUtil.isNotNull(lastPriceVersion)){
            electricityPriceVersionMapper.updateByPrimaryKey(lastPriceVersion);
        }
        //新增版本
        electricityPriceVersionMapper.insert(priceVersionPO);
    }

    /**
     * 找到与老版本绑定的全量数据,后面会通过这个数据筛选出设备与规则的绑定
     *
     * @param
     * @return
     * @author sunjidong
     * @date 2022/5/10 21:44
     */
    private List<ElectricityPriceEquipmentView> getAllEquipPriceBindRecords(ElectricityPriceVersionUpdateBO versionBO) {
        String lastVersionId = versionBO.getLastVersionId();
        List<ElectricityPriceEquipmentView> ruleEquipmentBindRecordList = new ArrayList<>();
        if (ObjectUtil.isNotNull(lastVersionId)) {
            ElectricityPriceEquipment priceEquipmentExample = new ElectricityPriceEquipment();
            priceEquipmentExample.setTenantId(versionBO.getTenantId());
            priceEquipmentExample.setVersionId(lastVersionId);
            priceEquipmentExample.setState(StateEum.NORMAL.getValue());
            ruleEquipmentBindRecordList = electricityPriceEquipmentCustomMapper.listRuleEquipmentBindRecords(priceEquipmentExample);
        }
        return ruleEquipmentBindRecordList;
    }

    /**
     * 判断待创建的版本是否已经存在
     *
     * @param
     * @return
     * @author sunjidong
     * @date 2022/5/10 20:29
     */
    private ElectricityPriceVersion ifDuplicateVersion(ElectricityPriceVersionUpdateBO versionBO) {
        ElectricityPriceVersion priceVersionExample = CommonBOPOConvertMapper.INSTANCE.versionUpdateBOToPO(versionBO);
//        priceVersionExample.setStartDate(DateUtil.parse(versionBO.getStartDate(), DatePattern.NORM_DATE_PATTERN));
        priceVersionExample.setState(StateEum.NORMAL.getValue());
        long countByPriceVersion = electricityPriceVersionCustomMapper.countByPriceVersionExample(priceVersionExample);
        if (countByPriceVersion > 0) {
            throw new PriceException(ErrorCodeEnum.REPEAT_VERSION.getErrorCode(), ErrorCodeEnum.REPEAT_VERSION.getErrorMsg());
        }
        return priceVersionExample;
    }

    /**
     * 通过匹配季节三要素与规则三要素，生成规则电价明细
     *
     * @param priceStructureRuleList,priceRuleList
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
                priceRule.setStructureRuleId(String.valueOf(priceStructureRule.getId()));
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
     * 校验电价体系以及电价规则
     *
     * @param structureAndRuleValidateBO
     * @return ElectricityPriceStructureAndRuleValidateRespBO
     * @author sunjidong
     * @date 2022/5/6 9:41
     */
    @Override
    public ElectricityPriceStructureAndRuleValidateRespBO validateStructureAndRule(ElectricityPriceVersionUpdateBO structureAndRuleValidateBO) {
        //校验结果BO
        ElectricityPriceStructureAndRuleValidateRespBO validateRespBO = new ElectricityPriceStructureAndRuleValidateRespBO();
        //当前版本id版本id
        String versionId = structureAndRuleValidateBO.getVersionId();
        //该版本下某个体系对应的季节分时内容
        List<ElectricityPriceStructureRuleUpdateBO> priceStructureRuleValidateList = structureAndRuleValidateBO.getElectricityPriceStructureUpdateBOList().get(0).getElectricityPriceStructureRuleUpdateBOList();
        //该版本下某个体系对应的电价规则内容
        List<ElectricityPriceUpdateBO> priceRuleValidateReqVOList = structureAndRuleValidateBO.getElectricityPriceStructureUpdateBOList().get(0).getElectricityPriceUpdateBOList();
        //校验是否成功
        boolean validateResult = false;

        //1、校验季节分时对应的三要素组合是否存在重复
        validateResult = validateIfExistDuplicatedStructureRule(priceStructureRuleValidateList, validateRespBO);
        if (validateResult) {
            return validateRespBO;
        }
        //2、校验电价规则是否存在重复的三要素体系项
        validateResult = validateIfExistDuplicatedRule(priceRuleValidateReqVOList, validateRespBO);
        if (validateResult) {
            return validateRespBO;
        }

        //3、编辑状态下，校验是否修改了已绑定设备的规则
        if (StrUtil.isNotBlank(versionId)) {
            validateResult = validateIfCanEditPriceRules(validateRespBO, priceRuleValidateReqVOList);
            if (validateResult) {
                return validateRespBO;
            }
        }

        //4、校验电价类型、策略、电压等级
        Map<Integer, List<ElectricityPriceDictionaryBO>> structureGroupMap = priceManagerService.getPriceElectricityDictionaries(null, structureAndRuleValidateBO.getProvinceCode());
        //电价类型集合
        List<ElectricityPriceDictionaryBO> industryList = structureGroupMap.get(CommonConstant.INDUSTRY_TYPE);
        //定价类型集合
        List<ElectricityPriceDictionaryBO> strategyList = structureGroupMap.get(CommonConstant.STRATEGY_TYPE);
        //电压等级集合
        List<ElectricityPriceDictionaryBO> voltageLevelList = structureGroupMap.get(CommonConstant.VOLTAGELEVEL_TYPE);
        if (CollUtil.isEmpty(industryList) || CollUtil.isEmpty(strategyList) || CollUtil.isEmpty(voltageLevelList)) {
            throw new PriceException(ErrorCodeEnum.STRUCTURE_NOT_EXISTS.getErrorCode(), ErrorCodeEnum.STRUCTURE_NOT_EXISTS.getErrorMsg());
        }
        //5、校验三要素是否合法
        validateResult = validateIfPriceStructureIllegal(validateRespBO, priceRuleValidateReqVOList, industryList, strategyList, voltageLevelList);
        if (validateResult) {
            return validateRespBO;
        }

        //6、校验电价规则的价格
        ElectricityPriceStructureAndRuleValidateRespBO validatePriceRespBO = doValidatePrice(priceRuleValidateReqVOList);
        if(ObjectUtil.isNotNull(validatePriceRespBO)){
            return validatePriceRespBO;
        }

        //7、校验当为两部制时，是否缺少容需量用电价格
        Map<String, List<ElectricityPriceUpdateBO>> priceRuleValidateReqVOListGroupingByStrategy = priceRuleValidateReqVOList.stream()
                .collect(Collectors.groupingBy(ElectricityPriceUpdateBO::getStrategy));
        validateResult = validateStrategyPrice(validateRespBO, priceRuleValidateReqVOListGroupingByStrategy);
        if (validateResult) {
            return validateRespBO;
        }

        //8、校验是否缺少电度电价
        //先将默认的和非默认的分开
        Map<Boolean, List<ElectricityPriceStructureRuleUpdateBO>> priceStructureRulePartitions
                = priceStructureRuleValidateList.stream()
                .collect(Collectors.partitioningBy(priceStructureRuleValidate -> CommonConstant.DEFAULT_TYPE.equals(priceStructureRuleValidate.getIndustries())));
        //默认的
        List<ElectricityPriceStructureRuleUpdateBO> defaultStructureRuleCreateBOList = priceStructureRulePartitions.get(Boolean.TRUE);
        //自定义的
        List<ElectricityPriceStructureRuleUpdateBO> customStructureRuleCreateBOList = priceStructureRulePartitions.get(Boolean.FALSE);
        List<ElectricityPriceStructureRuleUpdateBO> noExistTimeSectionStructureRuleList = new ArrayList<>();
        if(CollUtil.isNotEmpty(customStructureRuleCreateBOList)) {
            //不存在分时区间的季节体系
            noExistTimeSectionStructureRuleList
                    = customStructureRuleCreateBOList.stream().filter(priceStructureRule -> {
                boolean matchSuccess = true;
                List<ElectricityPriceSeasonUpdateBO> seasonUpdateReqVOList = priceStructureRule.getElectricityPriceSeasonUpdateReqVOList();
                for (ElectricityPriceSeasonUpdateBO seasonUpdateBO : seasonUpdateReqVOList) {
                    if (CollUtil.isNotEmpty(seasonUpdateBO.getElectricityPriceStrategyBOList())) {
                        matchSuccess = false;
                        break;
                    }
                }
                return matchSuccess;
            }).collect(Collectors.toList());
        }
        boolean noTimeSection = true;
        if(CollUtil.isNotEmpty(defaultStructureRuleCreateBOList)){
            //判断默认的是否配置分时
            noTimeSection = defaultStructureRuleCreateBOList.get(CommonConstant.INDUSTRY_TYPE).getElectricityPriceSeasonUpdateReqVOList().stream()
                    .allMatch(seasonCreateBO -> CollUtil.isEmpty(seasonCreateBO.getElectricityPriceStrategyBOList()));
            validateResult = validateIfLackDistributionPriceList(defaultStructureRuleCreateBOList, customStructureRuleCreateBOList, priceRuleValidateReqVOList, noExistTimeSectionStructureRuleList, validateRespBO, noTimeSection);
            if (validateResult) {
                return validateRespBO;
            }
        }

        //9、校验是否缺少分时电价
        //自定义配置，存在分时的集合
        customStructureRuleCreateBOList.removeAll(noExistTimeSectionStructureRuleList);
        validateResult = validateIfLackTimePartPriceList(validateRespBO, priceRuleValidateReqVOList, defaultStructureRuleCreateBOList, customStructureRuleCreateBOList, noTimeSection);
        if (validateResult) {
            return validateRespBO;
        }
        return null;
    }

    /**
     * 校验是否缺少分时电价
     *
     * @author sunjidong
     * @date 2022/5/11 14:11
     */
    private boolean validateIfLackTimePartPriceList(ElectricityPriceStructureAndRuleValidateRespBO validateRespBO,
                                                    List<ElectricityPriceUpdateBO> priceRuleValidateReqVOList,
                                                    List<ElectricityPriceStructureRuleUpdateBO> defaultStructureRuleCreateBOList,
                                                    List<ElectricityPriceStructureRuleUpdateBO> customStructureRuleCreateBOList,
                                                    boolean noTimeSection) {
        List<StructureRuleAndTimeTypeBO> customStructureRuleAndTimeTypeBOList = customStructureRuleCreateBOList.stream().map(customStructureRuleCreateBO -> {
            List<ElectricityTimeSectionUpdateBO> customTimeSectionCreateBOList = new ArrayList<>();
            for (ElectricityPriceSeasonUpdateBO createBO : customStructureRuleCreateBO.getElectricityPriceSeasonUpdateReqVOList()) {
                if (CollUtil.isNotEmpty(createBO.getElectricityPriceStrategyBOList())) {
                    for (ElectricityPriceStrategyBO priceStrategyBO : createBO.getElectricityPriceStrategyBOList()) {
                        customTimeSectionCreateBOList.addAll(priceStrategyBO.getElectricityTimeSectionUpdateBOList());
                    }
                }
            }
            Map<String, List<ElectricityTimeSectionUpdateBO>> customPeriodsTimeSectionMap = customTimeSectionCreateBOList.stream()
                    .collect(Collectors.groupingBy(ElectricityTimeSectionUpdateBO::getPeriods));
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
        if (!noTimeSection) {
            List<ElectricityTimeSectionUpdateBO> defaultTimeSectionCreateBOList = new ArrayList<>();
            List<ElectricityPriceSeasonUpdateBO> seasonUpdateReqVOList = defaultStructureRuleCreateBOList.get(0).getElectricityPriceSeasonUpdateReqVOList();
            for (ElectricityPriceSeasonUpdateBO createBO : seasonUpdateReqVOList) {
                if (CollUtil.isNotEmpty(createBO.getElectricityPriceStrategyBOList())) {
                    for (ElectricityPriceStrategyBO priceStrategyBO : createBO.getElectricityPriceStrategyBOList()) {
                        defaultTimeSectionCreateBOList.addAll(priceStrategyBO.getElectricityTimeSectionUpdateBOList());
                    }
                }
            }
            //默认的体系对应的分时类型
            Map<String, List<ElectricityTimeSectionUpdateBO>> defaultStructureRuleAndTimeTypeBOMap
                    = defaultTimeSectionCreateBOList.stream()
                    .collect(Collectors.groupingBy(ElectricityTimeSectionUpdateBO::getPeriods));

            Set<String> defaultPeriods = defaultStructureRuleAndTimeTypeBOMap.keySet();
            defaultStructureRuleAndTimeTypeBO.setIndustries(CommonConstant.DEFAULT_TYPE);
            defaultStructureRuleAndTimeTypeBO.setStrategies(CommonConstant.DEFAULT_TYPE);
            defaultStructureRuleAndTimeTypeBO.setVoltageLevels(CommonConstant.DEFAULT_TYPE);
            defaultStructureRuleAndTimeTypeBO.setTimeTypeList(defaultPeriods);
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
                        switch (timeType) {
                            case CommonConstant.SHARP:
                                return StrUtil.isEmpty(priceRuleValidateReqVO.getSharpPrice());
                            case CommonConstant.PEEK:
                                return StrUtil.isEmpty(priceRuleValidateReqVO.getPeakPrice());
                            case CommonConstant.LEVEL:
                                return StrUtil.isEmpty(priceRuleValidateReqVO.getLevelPrice());
                            case CommonConstant.VALLEY:
                                return StrUtil.isEmpty(priceRuleValidateReqVO.getValleyPrice());
                            default:
                                return false;
                        }
                    }
                }
            }
            //获取到默认的体系对应的分时类型
            if (!matchSuccess && !noTimeSection) {
                for (String timeType : defaultStructureRuleAndTimeTypeBO.getTimeTypeList()) {
                    switch (timeType) {
                        case CommonConstant.SHARP:
                            return StrUtil.isEmpty(priceRuleValidateReqVO.getSharpPrice());
                        case CommonConstant.PEEK:
                            return StrUtil.isEmpty(priceRuleValidateReqVO.getPeakPrice());
                        case CommonConstant.LEVEL:
                            return StrUtil.isEmpty(priceRuleValidateReqVO.getLevelPrice());
                        case CommonConstant.VALLEY:
                            return StrUtil.isEmpty(priceRuleValidateReqVO.getValleyPrice());
                        default:
                            return false;
                    }
                }
            }
            return matchSuccess;
        }).map(ElectricityPriceUpdateBO::getSerialNo).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(lackTimePartPriceList)) {
            validateRespBO.setLackTimePartPriceList(lackTimePartPriceList);
            return true;
        }
        return false;
    }

    /**
     * 分别校验两部制以及一部制时的相应电价是否填写
     *
     * @author sunjidong
     * @date 2022/5/11 11:16
     */
    private boolean validateStrategyPrice(ElectricityPriceStructureAndRuleValidateRespBO validateRespBO,
                                          Map<String, List<ElectricityPriceUpdateBO>> priceRuleValidateReqVOListGroupingByStrategy) {
        List<ElectricityPriceUpdateBO> doubleStrategyPriceRuleCreateBOList = priceRuleValidateReqVOListGroupingByStrategy.get(String.valueOf(StrategyEnum.DOUBLESTRATEGY.getCode()));
        if(CollUtil.isNotEmpty(doubleStrategyPriceRuleCreateBOList)){
            List<String> doubleStrategyPriceRuleList = getLackCapacityAndDemandCapacityPriceList(StrategyEnum.DOUBLESTRATEGY.getCode(), doubleStrategyPriceRuleCreateBOList);
            if (CollUtil.isNotEmpty(doubleStrategyPriceRuleList)) {
                validateRespBO.setLackCapacityAndDemandCapacityPriceList(doubleStrategyPriceRuleList);
                return true;
            }
        }
        //单一制
        List<ElectricityPriceUpdateBO> singleStrategyPriceRuleCreateBOList = priceRuleValidateReqVOListGroupingByStrategy.get(String.valueOf(StrategyEnum.SINGLESTRATEGY.getCode()));
        if(CollUtil.isNotEmpty(singleStrategyPriceRuleCreateBOList)){
            List<String> singleStrategyPriceRuleList = getLackCapacityAndDemandCapacityPriceList(StrategyEnum.SINGLESTRATEGY.getCode(), singleStrategyPriceRuleCreateBOList);
            if (CollUtil.isNotEmpty(singleStrategyPriceRuleList)) {
                validateRespBO.setNeedLessCapacityAndDemandCapacityPriceList(singleStrategyPriceRuleList);
                return true;
            }
        }
        return false;
    }

    /**
     * 校验三要素是否合法
     *
     * @author sunjidong
     * @date 2022/5/11 11:11
     */
    private boolean validateIfPriceStructureIllegal(ElectricityPriceStructureAndRuleValidateRespBO validateRespBO,
                                                    List<ElectricityPriceUpdateBO> priceRuleValidateReqVOList,
                                                    List<ElectricityPriceDictionaryBO> industryList,
                                                    List<ElectricityPriceDictionaryBO> strategyList,
                                                    List<ElectricityPriceDictionaryBO> voltageLevelList) {
        List<String> notExistRuleSerialNoList = validateStructure(CommonConstant.INDUSTRY, industryList, priceRuleValidateReqVOList);
        if (CollUtil.isNotEmpty(notExistRuleSerialNoList)) {
            validateRespBO.setIllegalIndustry(notExistRuleSerialNoList);
            return true;
        }
        notExistRuleSerialNoList = validateStructure(CommonConstant.STRATEGY, strategyList, priceRuleValidateReqVOList);
        if (CollUtil.isNotEmpty(notExistRuleSerialNoList)) {
            validateRespBO.setIllegalIndustry(notExistRuleSerialNoList);
            return true;
        }
        notExistRuleSerialNoList = validateStructure(CommonConstant.VOLTAGELEVEL, voltageLevelList, priceRuleValidateReqVOList);
        if (CollUtil.isNotEmpty(notExistRuleSerialNoList)) {
            validateRespBO.setIllegalIndustry(notExistRuleSerialNoList);
            return true;
        }
        return false;
    }

    /**
     * 1、获取当为两部制时，未填写容需量价格时的店家规则 2、获取当为一部制时，填写了容需量用电价格的规则
     *
     * @param type
     * @return String
     * @author sunjidong
     * @date 2022/5/6 21:28
     */
    private List<String> getLackCapacityAndDemandCapacityPriceList(Byte type, List<ElectricityPriceUpdateBO> strategyPriceRuleCreateBOList) {
        return strategyPriceRuleCreateBOList.stream()
                .filter(priceRuleValidateReqVO -> {
                    if (type.equals(StrategyEnum.DOUBLESTRATEGY.getCode())) {
                        boolean doubleStrategyBoolean = priceRuleValidateReqVO.getStrategy()
                                .equals(String.valueOf(StrategyEnum.DOUBLESTRATEGY.getCode()));
                        boolean ifBlankOfPrice = StrUtil.isBlank(priceRuleValidateReqVO.getTransformerCapacityPrice())
                                || StrUtil.isBlank(priceRuleValidateReqVO.getMaxCapacityPrice());
                        if (doubleStrategyBoolean && ifBlankOfPrice) {
                            return true;
                        }
                    }
                    if (type.equals(StrategyEnum.SINGLESTRATEGY.getCode())) {
                        boolean singleStrategyBoolean = priceRuleValidateReqVO.getStrategy()
                                .equals(String.valueOf(StrategyEnum.SINGLESTRATEGY.getCode()));
                        boolean ifNotBlankOfPrice = StrUtil.isNotBlank(priceRuleValidateReqVO.getTransformerCapacityPrice())
                                || StrUtil.isNotBlank(priceRuleValidateReqVO.getMaxCapacityPrice());
                        return singleStrategyBoolean && ifNotBlankOfPrice;
                    }
                    return false;
                }).map(ElectricityPriceUpdateBO::getSerialNo).collect(Collectors.toList());
    }

    /**
     * 获取被修改的电价规则里是否存有被绑定的，返回行号
     *
     * @author sunjidong
     * @date 2022/5/11 10:58
     */
    private List<String> getCantEditPriceRuleSerialNos(List<ElectricityPriceUpdateBO> editPriceRuleValidateReqVOList) {
        List<String> ruleIdList = editPriceRuleValidateReqVOList.stream().map(electricityPriceUpdateBO -> {
            return String.valueOf(electricityPriceUpdateBO.getRuleId());
        }).collect(Collectors.toList());
        if (CollUtil.isEmpty(ruleIdList)) {
            return null;
        }
        List<ElectricityPriceRule> electricityPriceRules = priceRuleExtMapper.selectRulesByRuleIdList(ruleIdList);
        //确认被修改过的电价规则Id集合
        List<String> beModifiedRuleList = editPriceRuleValidateReqVOList.stream().filter(editPriceRuleValidateReqVO -> {
            String industry = editPriceRuleValidateReqVO.getIndustry();
            String strategy = editPriceRuleValidateReqVO.getStrategy();
            String voltageLevel = editPriceRuleValidateReqVO.getVoltageLevel();
            boolean matchFail = true;
            for (ElectricityPriceRule priceRule : electricityPriceRules) {
                String industryDatabase = priceRule.getIndustry();
                String strategyDatabase = priceRule.getStrategy();
                String voltageLevelDatabase = priceRule.getVoltageLevel();
                if (industryDatabase.equals(industry)
                        && strategyDatabase.equals(strategy)
                        && voltageLevelDatabase.equals(voltageLevel)) {
                    matchFail = false;
                    break;
                }
            }
            return matchFail;
        }).map(electricityPriceUpdateBO -> {
            return String.valueOf(electricityPriceUpdateBO.getRuleId());
        }).collect(Collectors.toList());

        //根据ruleId集合判断是否已绑定设备
        List<ElectricityPriceEquipment> electricityPriceEquipments = electricityPriceEquipmentCustomMapper.listRuleEquipmentBindRecordsByRuleIdList(beModifiedRuleList);
        return editPriceRuleValidateReqVOList.stream().filter(editPriceRuleValidateReqVO -> {
            boolean matchSuccess = false;
            for (ElectricityPriceEquipment priceEquipment : electricityPriceEquipments) {
                if (String.valueOf(editPriceRuleValidateReqVO.getRuleId()).equals(priceEquipment.getRuleId())) {
                    matchSuccess = true;
                    break;
                }
            }
            return matchSuccess;
        }).map(ElectricityPriceUpdateBO::getSerialNo).collect(Collectors.toList());
    }

    /**
     * 判断日期是否存在交集
     *
     * @param allSeasonSectionCreateBOList
     * @return boolean
     */
    private boolean intersectionOfDate(String year, List<SeasonDateBO> allSeasonSectionCreateBOList) {
        for (int i = 0; i < allSeasonSectionCreateBOList.size(); i++) {
            //a
            String startDateStr = year + CommonConstant.DATE_SPLIT + allSeasonSectionCreateBOList.get(i).getSeaStartDate();
            DateTime startDate = DateUtil.parse(startDateStr, DatePattern.NORM_DATE_PATTERN);
            //b
            String endDateStr = year + CommonConstant.DATE_SPLIT + allSeasonSectionCreateBOList.get(i).getSeaEndDate();
            DateTime endDate = DateUtil.parse(endDateStr, DatePattern.NORM_DATE_PATTERN);
            for (int j = i + 1; j < allSeasonSectionCreateBOList.size(); j++) {
                //c
                String toBeComparedStartDateStr = year + CommonConstant.DATE_SPLIT + allSeasonSectionCreateBOList.get(j).getSeaStartDate();
                DateTime toBeComparedStartDate = DateUtil.parse(toBeComparedStartDateStr, DatePattern.NORM_DATE_PATTERN);
                //d
                String toBeComparedEndDateStr = year + CommonConstant.DATE_SPLIT + allSeasonSectionCreateBOList.get(j).getSeaEndDate();
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
     * @param priceStrategyList
     * @return boolean
     */
    private boolean intersectionOfTime(List<ElectricityPriceStrategyBO> priceStrategyList) {
        for (int i = 0; i < priceStrategyList.size(); i++) {
            List<ElectricityTimeSectionUpdateBO> allTimeSectionCreateBOList = priceStrategyList.get(i).getElectricityTimeSectionUpdateBOList();
            for (int j = 0; j < allTimeSectionCreateBOList.size(); j++) {
                //a
                String startTimeStr = CommonConstant.DAY_EXAMPLE + allTimeSectionCreateBOList.get(i).getStartTime() + CommonConstant.TIME_ZERO_SPLIT;
                DateTime startTime = DateUtil.parse(startTimeStr, DatePattern.NORM_DATETIME_PATTERN);
                //b
                String endTimeStr = CommonConstant.DAY_EXAMPLE + allTimeSectionCreateBOList.get(i).getEndTime() + CommonConstant.TIME_ZERO_SPLIT;
                DateTime endTime = DateUtil.parse(endTimeStr, DatePattern.NORM_DATETIME_PATTERN);
//                endTime = DateUtil.offsetSecond(endTime, -1);
//                if(CommonConstant.END_TIME.equals(allTimeSectionCreateBOList.get(i).getEndTime())){
//                    endTimeStr = CommonConstant.DAY_EXAMPLE_NINE;
//                    endTime = DateUtil.parse(endTimeStr, DatePattern.NORM_DATETIME_PATTERN);
//                }
                for (int k = j + 1; k < allTimeSectionCreateBOList.size(); k++) {
                    //c
                    String toBeComparedStartTimeStr = CommonConstant.DAY_EXAMPLE
                            + allTimeSectionCreateBOList.get(k).getStartTime()
                            + CommonConstant.TIME_ZERO_SPLIT;
                    DateTime toBeComparedStartTime = DateUtil.parse(toBeComparedStartTimeStr, DatePattern.NORM_DATETIME_PATTERN);
                    //d
                    String toBeComparedEndTimeStr = CommonConstant.DAY_EXAMPLE
                            + allTimeSectionCreateBOList.get(k).getEndTime()
                            + CommonConstant.TIME_ZERO_SPLIT;
                    DateTime toBeComparedEndTime = DateUtil.parse(toBeComparedEndTimeStr, DatePattern.NORM_DATETIME_PATTERN);
                    /*toBeComparedEndTime = DateUtil.offsetSecond(toBeComparedEndTime, -1);
                    if(CommonConstant.END_TIME.equals(allTimeSectionCreateBOList.get(k).getEndTime())){
                        toBeComparedEndTimeStr = CommonConstant.DAY_EXAMPLE_NINE;
                        toBeComparedEndTime = DateUtil.parse(toBeComparedEndTimeStr, DatePattern.NORM_DATETIME_PATTERN);
                    }*/
                    boolean compare1 = DateUtil.compare(startTime, toBeComparedEndTime) < 0;
                    boolean compare2 = DateUtil.compare(endTime, toBeComparedStartTime) > 0;
                    // 如果 a<d && b>c ，则必有交集
                    if (compare1 && compare2) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 校验分时区间是否满一天
     *
     * @param
     * @return boolean
     * @author sunjidong
     * @date 2022/5/6 14:52
     */
    private boolean ifReachWholeDayTime(long msOfWholeDay, List<ElectricityPriceStrategyBO> priceStrategyList) {
        for (ElectricityPriceStrategyBO priceStrategyBO : priceStrategyList) {
            List<ElectricityTimeSectionUpdateBO> timeSectionCreateBOList = priceStrategyBO.getElectricityTimeSectionUpdateBOList();
            timeSectionCreateBOList = timeSectionCreateBOList.stream().map(timeSectionCreateBO -> {
                String startTimeStr = CommonConstant.DAY_EXAMPLE + timeSectionCreateBO.getStartTime() + CommonConstant.TIME_ZERO_SPLIT;
                timeSectionCreateBO.setStartTime(startTimeStr);
                String endTimeStr = CommonConstant.DAY_EXAMPLE + timeSectionCreateBO.getEndTime() + CommonConstant.TIME_ZERO_SPLIT;
                timeSectionCreateBO.setEndTime(endTimeStr);
                return timeSectionCreateBO;
            }).collect(Collectors.toList());
            long dayMs = 0L;
            for (ElectricityTimeSectionUpdateBO timeSectionCreateBO : timeSectionCreateBOList) {
                DateTime endTime = DateUtil.parse(timeSectionCreateBO.getEndTime(), DatePattern.NORM_DATETIME_PATTERN);
//                endTime = DateUtil.offsetSecond(endTime, CommonConstant.NUMBER0);
//                if(CommonConstant.DAY_EXAMPLE_FOUR.equals(timeSectionCreateBO.getEndTime())){
//                    endTime = DateUtil.parse(CommonConstant.DAY_EXAMPLE_NINE, DatePattern.NORM_DATETIME_PATTERN);
//                }
                DateTime startTime = DateUtil.parse(timeSectionCreateBO.getStartTime(), DatePattern.NORM_DATETIME_PATTERN);
                dayMs += DateUtil.between(startTime, endTime, DateUnit.SECOND);
            }
            if(dayMs == msOfWholeDay){
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 校验季节区间是否满一年
     *
     * @param year
     * @return boolean
     * @author sunjidong
     * @date 2022/5/6 14:52
     */
    private boolean ifReachWholeYear(String year, long allYearDays, List<SeasonDateBO> allSeasonSectionCreateBOList) {
        allSeasonSectionCreateBOList = allSeasonSectionCreateBOList.stream().map(seasonSectionCreateBO -> {
            seasonSectionCreateBO.setSeaStartDate(year + CommonConstant.DATE_SPLIT + seasonSectionCreateBO.getSeaStartDate());
            seasonSectionCreateBO.setSeaEndDate(year + CommonConstant.DATE_SPLIT + seasonSectionCreateBO.getSeaEndDate());
            return seasonSectionCreateBO;
        }).collect(Collectors.toList());
        long days = 0L;
        for (SeasonDateBO seasonSectionCreateBO : allSeasonSectionCreateBOList) {
            DateTime endDateTime = DateUtil.parse(seasonSectionCreateBO.getSeaEndDate(), DatePattern.NORM_DATE_PATTERN);
            DateTime startDateTime = DateUtil.parse(seasonSectionCreateBO.getSeaStartDate(), DatePattern.NORM_DATE_PATTERN);
            days += DateUtil.betweenDay(startDateTime, endDateTime, true);
        }
        days += allSeasonSectionCreateBOList.size() - 1;
        if(days == allYearDays){
            return false;
        }
        return true;
    }

    /**
     * 遍历校验规则的三要素是否合法
     *
     * @param type
     * @return List<String>
     * @author sunjidong
     * @date 2022/5/6 20:52
     */
    private List<String> validateStructure(String type, List<ElectricityPriceDictionaryBO> structureList, List<ElectricityPriceUpdateBO> priceRuleValidateReqVOList) {
        return priceRuleValidateReqVOList.stream().filter(priceRuleValidateReqVO -> {
            boolean matchFlag = true;
            String industry = priceRuleValidateReqVO.getIndustry();
            String strategy = priceRuleValidateReqVO.getStrategy();
            String voltageLevel = priceRuleValidateReqVO.getVoltageLevel();
            for (ElectricityPriceDictionaryBO industryDictionary : structureList) {
                boolean industryBoolean = type.equals(CommonConstant.INDUSTRY) && industryDictionary.getCode().equals(industry);
                boolean strategyBoolean = type.equals(CommonConstant.STRATEGY) && industryDictionary.getCode().equals(strategy);
                boolean voltageLevelBoolean = type.equals(CommonConstant.VOLTAGELEVEL) && industryDictionary.getCode().equals(voltageLevel);
                if (industryBoolean || strategyBoolean || voltageLevelBoolean) {
                    matchFlag = false;
                    break;
                }
            }
            return matchFlag;
        }).map(ElectricityPriceUpdateBO::getSerialNo).collect(Collectors.toList());
    }

    /**
     * 下载模板
     *
     * @author sunjidong
     * @date 2022/5/7 20:45
     */
    @Override
    public ExcelWriter downLoadTemplate(String provinceCode) {

        Map<Integer, List<ElectricityPriceDictionaryBO>> priceElectricityDictionaries = priceManagerService.getPriceElectricityDictionaries(null, provinceCode);
        //用电行业
        List<ElectricityPriceDictionaryBO> industryList = priceElectricityDictionaries.get(CommonConstant.INDUSTRY_TYPE);
        //电价类型
        List<ElectricityPriceDictionaryBO> voltageLevelList = priceElectricityDictionaries.get(CommonConstant.VOLTAGELEVEL_TYPE);
        //两部制、一部制
        List<ElectricityPriceDictionaryBO> strategyList = priceElectricityDictionaries.get(CommonConstant.STRATEGY_TYPE);

        ExcelWriter excelWriter = new ExcelWriter();
        excelWriter.merge(0, 1, 0, 0, CommonConstant.INDUSTRY_CHINA, true);
        excelWriter.merge(0, 1, 1, 1, CommonConstant.STRATEGY_CHINA, true);
        excelWriter.merge(0, 1, 2, 2, CommonConstant.VOLTAGELEVEL_CHINA, true);
        excelWriter.merge(0, 1, 3, 3, CommonConstant.CONSUMPTION_CHINA, true);

        excelWriter.merge(0, 0, 4, 5, CommonConstant.OTHER_CHINA, true);
        excelWriter.writeCellValue(4, 1, CommonConstant.DISTRIBUTION_CHINA);
        excelWriter.writeCellValue(5, 1, CommonConstant.GOV_ADD_CHINA);

        excelWriter.merge(0, 0, 6, 9, CommonConstant.TIME_SECTION_CHINA, true);
        excelWriter.writeCellValue(6, 1, CommonConstant.SHARP_CHINA);
        excelWriter.writeCellValue(7, 1, CommonConstant.PEEK_CHINA);
        excelWriter.writeCellValue(8, 1, CommonConstant.LEVEL_CHINA);
        excelWriter.writeCellValue(9, 1, CommonConstant.VALLY_CHINA);

        excelWriter.merge(0, 0, 10, 11, CommonConstant.CAPACITY_CHINA, true);
        excelWriter.writeCellValue(10, 1, CommonConstant.MAX_CAPACITY_CHINA);
        excelWriter.writeCellValue(11, 1, CommonConstant.TRANSFORMER_CAPACITY_CHINA);

        //初始化下拉列表
        if(CollUtil.isNotEmpty(industryList)){
            List<String> industries = industryList.stream().map(ElectricityPriceDictionaryBO::getName).collect(Collectors.toList());
            String[] industryArray = industries.toArray(new String[industries.size()]);
            excelWriter.addSelect(new CellRangeAddressList(2, 500, 0, 0),  industryArray);
        }
        if(CollUtil.isNotEmpty(strategyList)){
            List<String> strategies = strategyList.stream().map(ElectricityPriceDictionaryBO::getName).collect(Collectors.toList());
            String[] strategyArray = strategies.toArray( new String [strategies.size()] );
            excelWriter.addSelect(new CellRangeAddressList(2, 500, 1, 1), strategyArray);
        }
        if(CollUtil.isNotEmpty(voltageLevelList)){
            List<String> voltageLevels = voltageLevelList.stream().map(ElectricityPriceDictionaryBO::getName).collect(Collectors.toList());
            String[] voltageLevelArray = voltageLevels.toArray( new String [voltageLevels.size()] );
            excelWriter.addSelect(new CellRangeAddressList(2, 500, 2, 2), voltageLevelArray);
        }

        excelWriter.autoSizeColumnAll();
        excelWriter.autoSizeColumn(4, true);
        excelWriter.autoSizeColumn(5, true);
        excelWriter.autoSizeColumn(6, true);
        excelWriter.autoSizeColumn(7, true);
        excelWriter.autoSizeColumn(8, true);
        excelWriter.autoSizeColumn(9, true);
        excelWriter.autoSizeColumn(10, true);
        excelWriter.autoSizeColumn(11, true);
        return excelWriter;
    }

    /**
     * 上传模板
     *
     * @param reader
     * @return ElectricityPriceRuleCreateBO
     * @author sunjidong
     * @date 2022/5/8 8:11
     */
    @Override
    public List<ElectricityPriceUpdateBO> uploadTemplate(ExcelReader reader, ElectricityPriceImportDataBO importDataBO) {
        String provinceCode = importDataBO.getProvinceCode();
        List<ElectricityPriceUpdateBO> priceRuleCreateBOList = importDataBO.getUpdateReqVOList();
        List<List<Object>> records = reader.read(2);
        //该省的三要素字典
        Map<Integer, List<ElectricityPriceDictionaryBO>> priceElectricityDictionaries = priceManagerService.getPriceElectricityDictionaries(null, provinceCode);
        //用电行业
        List<ElectricityPriceDictionaryBO> industryList = priceElectricityDictionaries.get(CommonConstant.INDUSTRY_TYPE);
        //电价类型
        List<ElectricityPriceDictionaryBO> voltageLevelList = priceElectricityDictionaries.get(CommonConstant.VOLTAGELEVEL_TYPE);
        //两部制、一部制
        List<ElectricityPriceDictionaryBO> strategyList = priceElectricityDictionaries.get(CommonConstant.STRATEGY_TYPE);
        //先把excel数据转换为ElectricityPriceUpdateBO，并把三要素名称转为code
        List<ElectricityPriceUpdateBO> priceRuleRecords = records.stream().map(rowRecord -> {
            ElectricityPriceUpdateBO priceRuleCreateBO = new ElectricityPriceUpdateBO();
            String industryName = StrUtil.isBlank((String) rowRecord.get(0)) ? CommonConstant.STR_BLANK : (String) rowRecord.get(0);
            String strategyName = StrUtil.isBlank((String) rowRecord.get(1)) ? CommonConstant.STR_BLANK : (String) rowRecord.get(1);
            String voltageLevelName = StrUtil.isBlank((String) rowRecord.get(2)) ? CommonConstant.STR_BLANK : (String) rowRecord.get(2);
            String industryCode = CommonConstant.STR_BLANK;
            if(CollUtil.isNotEmpty(industryList)){
                industryCode = industryList.stream().filter(priceDictionaryBO -> priceDictionaryBO.getName().equals(industryName.trim())).findFirst().orElse(new ElectricityPriceDictionaryBO()).getCode();
            }
            String voltageLevelCode = CommonConstant.STR_BLANK;
            if(CollUtil.isNotEmpty(voltageLevelList)){
                voltageLevelCode = voltageLevelList.stream()
                        .filter(priceDictionaryBO -> priceDictionaryBO.getName().equals(voltageLevelName.trim())).findFirst().orElse(new ElectricityPriceDictionaryBO()).getCode();
            }
            String strategyCode = CommonConstant.STR_BLANK;
            if(CollUtil.isNotEmpty(strategyList)){
                strategyCode = strategyList.stream()
                        .filter(priceDictionaryBO -> priceDictionaryBO.getName().equals(strategyName.trim())).findFirst().orElse(new ElectricityPriceDictionaryBO()).getCode();
            }
            priceRuleCreateBO.setIndustry(industryCode);
            priceRuleCreateBO.setStrategy(strategyCode);
            priceRuleCreateBO.setVoltageLevel(voltageLevelCode);
            priceRuleCreateBO.setMaxCapacityPrice(StrUtil.isBlank((String) rowRecord.get(10)) ? CommonConstant.STR_BLANK : (String)rowRecord.get(10));
            priceRuleCreateBO.setTransformerCapacityPrice(StrUtil.isBlank((String) rowRecord.get(11)) ? CommonConstant.STR_BLANK : (String)rowRecord.get(11));
            priceRuleCreateBO.setConsumptionPrice(StrUtil.isBlank((String) rowRecord.get(3)) ? CommonConstant.STR_BLANK : (String)rowRecord.get(3));
            priceRuleCreateBO.setDistributionPrice(StrUtil.isBlank((String) rowRecord.get(4)) ? CommonConstant.STR_BLANK : (String)rowRecord.get(4));
            priceRuleCreateBO.setGovAddPrice(StrUtil.isBlank((String) rowRecord.get(5)) ? CommonConstant.STR_BLANK : (String)rowRecord.get(5));
            priceRuleCreateBO.setSharpPrice(StrUtil.isBlank((String) rowRecord.get(6)) ? CommonConstant.STR_BLANK : (String)rowRecord.get(6));
            priceRuleCreateBO.setPeakPrice(StrUtil.isBlank((String) rowRecord.get(7)) ? CommonConstant.STR_BLANK : (String)rowRecord.get(7));
            priceRuleCreateBO.setLevelPrice(StrUtil.isBlank((String) rowRecord.get(8)) ? CommonConstant.STR_BLANK : (String)rowRecord.get(8));
            priceRuleCreateBO.setValleyPrice(StrUtil.isBlank((String) rowRecord.get(9)) ? CommonConstant.STR_BLANK : (String)rowRecord.get(9));
            return priceRuleCreateBO;
        }).collect(Collectors.toList());

        //判断原有的和导入的是否有相同的三要素，有的话，就覆盖价格
        List<ElectricityPriceUpdateBO> finalPriceRuleRecords = priceRuleRecords;
        priceRuleCreateBOList = priceRuleCreateBOList.stream().map(priceRuleCreateBO -> {
            for (ElectricityPriceUpdateBO rowRecord : finalPriceRuleRecords) {
                if (priceRuleCreateBO.getIndustry().equals(rowRecord.getIndustry())
                        && priceRuleCreateBO.getStrategy().equals(rowRecord.getStrategy())
                        && priceRuleCreateBO.getVoltageLevel().equals(rowRecord.getVoltageLevel())) {
                    priceRuleCreateBO.setMaxCapacityPrice(rowRecord.getMaxCapacityPrice());
                    priceRuleCreateBO.setTransformerCapacityPrice(rowRecord.getTransformerCapacityPrice());
                    priceRuleCreateBO.setConsumptionPrice(rowRecord.getConsumptionPrice());
                    priceRuleCreateBO.setDistributionPrice(rowRecord.getDistributionPrice());
                    priceRuleCreateBO.setGovAddPrice(rowRecord.getGovAddPrice());
                    priceRuleCreateBO.setSharpPrice(rowRecord.getSharpPrice());
                    priceRuleCreateBO.setPeakPrice(rowRecord.getSharpPrice());
                    priceRuleCreateBO.setLevelPrice(rowRecord.getLevelPrice());
                    priceRuleCreateBO.setValleyPrice(rowRecord.getValleyPrice());
                }
            }
            return priceRuleCreateBO;
        }).collect(Collectors.toList());

        List<ElectricityPriceUpdateBO> priceRuleCreateRespBOList = new ArrayList<>(priceRuleCreateBOList);
        List<ElectricityPriceUpdateBO> finalPriceRuleCreateBOList = priceRuleCreateBOList;
        priceRuleRecords = priceRuleRecords.stream().filter(rowRecord -> {
            boolean matchResult = true;
            for (ElectricityPriceUpdateBO priceRuleCreateBO : finalPriceRuleCreateBOList) {
                if (priceRuleCreateBO.getIndustry().equals(rowRecord.getIndustry())
                        && priceRuleCreateBO.getStrategy().equals(rowRecord.getStrategy())
                        && priceRuleCreateBO.getVoltageLevel().equals(rowRecord.getVoltageLevel())) {
                    matchResult = false;
                    break;
                }
            }
            return matchResult;
        }).collect(Collectors.toList());
        priceRuleCreateRespBOList.addAll(priceRuleRecords);
        return priceRuleCreateRespBOList;
    }

    /**
     * 校验数据的合法性
     * @author sunjidong
     * @date 2022/5/16 22:19
     */
    @Override
    public ElectricityPriceStructureAndRuleValidateRespBO validateTemplate(String provinceCode, ExcelReader reader) {
        List<List<Object>> records = reader.read(2);
        if(CollUtil.isEmpty(records)){
            throw new PriceException(ErrorCodeEnum.NON_EXISTENT_DATA_EXCEPTION.getErrorCode(), ErrorCodeEnum.NON_EXISTENT_DATA_EXCEPTION.getErrorMsg());
        }

        //该省的三要素字典
        Map<Integer, List<ElectricityPriceDictionaryBO>> priceElectricityDictionaries = priceManagerService.getPriceElectricityDictionaries(null, provinceCode);
        //用电行业
        List<ElectricityPriceDictionaryBO> industryList = priceElectricityDictionaries.get(CommonConstant.INDUSTRY_TYPE);
        List<String> industryNameList = new ArrayList<>();
        if(CollUtil.isNotEmpty(industryList)){
            industryNameList = industryList.stream().map(ElectricityPriceDictionaryBO::getName).collect(Collectors.toList());
        }
        //电价类型
        List<ElectricityPriceDictionaryBO> voltageLevelList = priceElectricityDictionaries.get(CommonConstant.VOLTAGELEVEL_TYPE);
        List<String> voltageLevelNameList = new ArrayList<>();
        if(CollUtil.isNotEmpty(voltageLevelList)){
            voltageLevelNameList = voltageLevelList.stream().map(ElectricityPriceDictionaryBO::getName).collect(Collectors.toList());
        }
        //两部制、一部制
        List<ElectricityPriceDictionaryBO> strategyList = priceElectricityDictionaries.get(CommonConstant.STRATEGY_TYPE);
        List<String> strategyNameList = new ArrayList<>();
        if(CollUtil.isNotEmpty(strategyList)){
            strategyNameList = strategyList.stream().map(ElectricityPriceDictionaryBO::getName).collect(Collectors.toList());
        }

        List<ElectricityPriceUpdateBO> priceRuleRecords = records.stream().map(rowRecord -> {
            ElectricityPriceUpdateBO priceRuleCreateBO = new ElectricityPriceUpdateBO();
            String industryName = StrUtil.isBlank((String) rowRecord.get(0)) ? CommonConstant.STR_BLANK : (String) rowRecord.get(0);
            String strategyName = StrUtil.isBlank((String) rowRecord.get(1)) ? CommonConstant.STR_BLANK : (String) rowRecord.get(1);
            String voltageLevelName = StrUtil.isBlank((String) rowRecord.get(2)) ? CommonConstant.STR_BLANK : (String) rowRecord.get(2);
            /*String industryCode = CommonConstant.STR_BLANK;
            if(CollUtil.isNotEmpty(industryList)){
                industryCode = industryList.stream().filter(priceDictionaryBO -> priceDictionaryBO.getName().equals(industryName.trim())).findFirst().orElse(new ElectricityPriceDictionaryBO()).getCode();
            }
            String voltageLevelCode = CommonConstant.STR_BLANK;
            if(CollUtil.isNotEmpty(voltageLevelList)){
                voltageLevelCode = voltageLevelList.stream()
                        .filter(priceDictionaryBO -> priceDictionaryBO.getName().equals(voltageLevelName.trim())).findFirst().orElse(new ElectricityPriceDictionaryBO()).getCode();
            }
            String strategyCode = CommonConstant.STR_BLANK;
            if(CollUtil.isNotEmpty(strategyList)){
                strategyCode = strategyList.stream()
                        .filter(priceDictionaryBO -> priceDictionaryBO.getName().equals(strategyName.trim())).findFirst().orElse(new ElectricityPriceDictionaryBO()).getCode();
            }*/
            priceRuleCreateBO.setIndustry(industryName);
            priceRuleCreateBO.setStrategy(strategyName);
            priceRuleCreateBO.setVoltageLevel(voltageLevelName);
            priceRuleCreateBO.setMaxCapacityPrice(StrUtil.isBlank((String) rowRecord.get(10)) ? CommonConstant.STR_BLANK : (String)rowRecord.get(10));
            priceRuleCreateBO.setTransformerCapacityPrice(StrUtil.isBlank((String) rowRecord.get(11)) ? CommonConstant.STR_BLANK : (String)rowRecord.get(11));
            priceRuleCreateBO.setConsumptionPrice(StrUtil.isBlank((String) rowRecord.get(3)) ? CommonConstant.STR_BLANK : (String)rowRecord.get(3));
            priceRuleCreateBO.setDistributionPrice(StrUtil.isBlank((String) rowRecord.get(4)) ? CommonConstant.STR_BLANK : (String)rowRecord.get(4));
            priceRuleCreateBO.setGovAddPrice(StrUtil.isBlank((String) rowRecord.get(5)) ? CommonConstant.STR_BLANK : (String)rowRecord.get(5));
            priceRuleCreateBO.setSharpPrice(StrUtil.isBlank((String) rowRecord.get(6)) ? CommonConstant.STR_BLANK : (String)rowRecord.get(6));
            priceRuleCreateBO.setPeakPrice(StrUtil.isBlank((String) rowRecord.get(7)) ? CommonConstant.STR_BLANK : (String)rowRecord.get(7));
            priceRuleCreateBO.setLevelPrice(StrUtil.isBlank((String) rowRecord.get(8)) ? CommonConstant.STR_BLANK : (String)rowRecord.get(8));
            priceRuleCreateBO.setValleyPrice(StrUtil.isBlank((String) rowRecord.get(9)) ? CommonConstant.STR_BLANK : (String)rowRecord.get(9));
            return priceRuleCreateBO;
        }).collect(Collectors.toList());

        //保存校验结果
        ElectricityPriceStructureAndRuleValidateRespBO validateRespBO;

        validateRespBO = doValidateCombination(priceRuleRecords, industryNameList, strategyNameList, voltageLevelNameList);
        if(ObjectUtil.isNotNull(validateRespBO)){
            return validateRespBO;
        }

        validateRespBO = doValidatePrice(priceRuleRecords);
        if(ObjectUtil.isNotNull(validateRespBO)){
            return validateRespBO;
        }
        return null;
    }

    /**
     * 校验体系的组合是否合法
     * @author sunjidong
     * @date 2022/5/17 14:33
     */
    private ElectricityPriceStructureAndRuleValidateRespBO doValidateCombination(List<ElectricityPriceUpdateBO> priceRuleRecords,
                                                                                 List<String> industryNameList,
                                                                                 List<String> strategyNameList,
                                                                                 List<String> voltageLevelNameList
                                                                                 ){
        ElectricityPriceStructureAndRuleValidateRespBO validateRespBO;
        List<String> illegalIndustryList = new ArrayList<>();
        List<String> illegalStrategyList = new ArrayList<>();
        List<String> illegalVoltageLevelList = new ArrayList<>();
        //校验用电类型的合法性
        for (int i = 0; i < priceRuleRecords.size(); i++) {
            ElectricityPriceUpdateBO rowRecord = priceRuleRecords.get(i);
            String industryName = rowRecord.getIndustry().trim();
            if (!industryNameList.contains(industryName)) {
                illegalIndustryList.add(String.valueOf(i + 3));
            }
        }
        if(CollUtil.isNotEmpty(illegalIndustryList)){
            validateRespBO = new ElectricityPriceStructureAndRuleValidateRespBO();
            validateRespBO.setIllegalIndustry(illegalIndustryList);
            return validateRespBO;
        }

        //校验定价类型的合法性
        for (int i = 0; i < priceRuleRecords.size(); i++) {
            ElectricityPriceUpdateBO rowRecord = priceRuleRecords.get(i);
            String strategyName = rowRecord.getStrategy().trim();
            if (!strategyNameList.contains(strategyName)) {
                illegalStrategyList.add(String.valueOf(i + 3));
            }
        }
        if(CollUtil.isNotEmpty(illegalStrategyList)){
            validateRespBO = new ElectricityPriceStructureAndRuleValidateRespBO();
            validateRespBO.setIllegalStrategy(illegalStrategyList);
            return validateRespBO;
        }

        //校验电压等级的合法性
        for (int i = 0; i < priceRuleRecords.size(); i++) {
            ElectricityPriceUpdateBO rowRecord = priceRuleRecords.get(i);
            String voltageLevelName = rowRecord.getVoltageLevel().trim();
            if (!voltageLevelNameList.contains(voltageLevelName)) {
                illegalVoltageLevelList.add(String.valueOf(i + 3));
            }
        }
        if(CollUtil.isNotEmpty(illegalVoltageLevelList)){
            validateRespBO = new ElectricityPriceStructureAndRuleValidateRespBO();
            validateRespBO.setIllegalVoltageLevel(illegalVoltageLevelList);
            return validateRespBO;
        }
        return null;
    }

    /**
     * 校验电价规则的电价是否合法
     * @author sunjidong
     * @date 2022/5/17 14:34
     */
    private ElectricityPriceStructureAndRuleValidateRespBO doValidatePrice(List<ElectricityPriceUpdateBO> priceRuleRecords){
        List<String> illegalPeekList = new ArrayList<>();

        List<String> illegalSharpList = new ArrayList<>();

        List<String> illegalLevelList = new ArrayList<>();

        List<String> illegalValleyList = new ArrayList<>();

        List<String> illegalGovAddPriceList = new ArrayList<>();

        List<String> illegalDistributionPriceList = new ArrayList<>();

        List<String> illegalConsumptionPriceList = new ArrayList<>();

        List<String> illegalMaxCapacityPriceList = new ArrayList<>();

        List<String> illegalTransformerCapacityPriceList = new ArrayList<>();

        ElectricityPriceStructureAndRuleValidateRespBO validateRespBO;

        //校验电镀用电的合法性
        for (int i = 0; i < priceRuleRecords.size(); i++) {
            ElectricityPriceUpdateBO rowRecord = priceRuleRecords.get(i);
            String consumptionPrice = rowRecord.getConsumptionPrice().trim();
            if(StrUtil.isNotBlank(consumptionPrice)
                    && !ReUtil.isMatch(CommonConstant.DECIMAL_PATTERN, consumptionPrice)){
                illegalConsumptionPriceList.add(String.valueOf(i + 3));
            }
        }
        if(CollUtil.isNotEmpty(illegalConsumptionPriceList)){
            validateRespBO = new ElectricityPriceStructureAndRuleValidateRespBO();
            validateRespBO.setIllegalConsumptionPriceList(illegalConsumptionPriceList);
            return validateRespBO;
        }

        //校验电镀输配的合法性
        for (int i = 0; i < priceRuleRecords.size(); i++) {
            ElectricityPriceUpdateBO rowRecord = priceRuleRecords.get(i);
            String distributionPrice = rowRecord.getDistributionPrice().trim();
            if(StrUtil.isNotBlank(distributionPrice)
                    && !ReUtil.isMatch(CommonConstant.DECIMAL_PATTERN, distributionPrice)){
                illegalDistributionPriceList.add(String.valueOf(i + 3));
            }
        }
        if(CollUtil.isNotEmpty(illegalDistributionPriceList)){
            validateRespBO = new ElectricityPriceStructureAndRuleValidateRespBO();
            validateRespBO.setIllegalDistributionPriceList(illegalDistributionPriceList);
            return validateRespBO;
        }

        //校验政府性附加的合法性
        for (int i = 0; i < priceRuleRecords.size(); i++) {
            ElectricityPriceUpdateBO rowRecord = priceRuleRecords.get(i);
            String govAddPrice = rowRecord.getGovAddPrice().trim();
            if(StrUtil.isNotBlank(govAddPrice)
                    && !ReUtil.isMatch(CommonConstant.DECIMAL_PATTERN, govAddPrice)){
                illegalGovAddPriceList.add(String.valueOf(i + 3));
            }
        }
        if(CollUtil.isNotEmpty(illegalGovAddPriceList)){
            validateRespBO = new ElectricityPriceStructureAndRuleValidateRespBO();
            validateRespBO.setIllegalGovAddPriceList(illegalGovAddPriceList);
            return validateRespBO;
        }

        //校验尖的合法性
        for (int i = 0; i < priceRuleRecords.size(); i++) {
            ElectricityPriceUpdateBO rowRecord = priceRuleRecords.get(i);
            String sharp = rowRecord.getSharpPrice().trim();
            if(StrUtil.isNotBlank(sharp)
                    && !ReUtil.isMatch(CommonConstant.DECIMAL_PATTERN, sharp)){
                illegalSharpList.add(String.valueOf(i + 3));
            }
        }
        if(CollUtil.isNotEmpty(illegalSharpList)){
            validateRespBO = new ElectricityPriceStructureAndRuleValidateRespBO();
            validateRespBO.setIllegalSharpList(illegalSharpList);
            return validateRespBO;
        }

        //校验峰的合法性
        for (int i = 0; i < priceRuleRecords.size(); i++) {
            ElectricityPriceUpdateBO rowRecord = priceRuleRecords.get(i);
            String peek = rowRecord.getPeakPrice().trim();
            if(StrUtil.isNotBlank(peek)
                    && !ReUtil.isMatch(CommonConstant.DECIMAL_PATTERN, peek)){
                illegalPeekList.add(String.valueOf(i + 3));
            }
        }
        if(CollUtil.isNotEmpty(illegalPeekList)){
            validateRespBO = new ElectricityPriceStructureAndRuleValidateRespBO();
            validateRespBO.setIllegalPeekList(illegalPeekList);
            return validateRespBO;
        }

        //校验平的合法性
        for (int i = 0; i < priceRuleRecords.size(); i++) {
            ElectricityPriceUpdateBO rowRecord = priceRuleRecords.get(i);
            String level = rowRecord.getLevelPrice().trim();
            if(StrUtil.isNotBlank(level)
                    && !ReUtil.isMatch(CommonConstant.DECIMAL_PATTERN, level)){
                illegalLevelList.add(String.valueOf(i + 3));
            }
        }
        if(CollUtil.isNotEmpty(illegalLevelList)){
            validateRespBO = new ElectricityPriceStructureAndRuleValidateRespBO();
            validateRespBO.setIllegalLevelList(illegalLevelList);
            return validateRespBO;
        }

        //校验谷的合法性
        for (int i = 0; i < priceRuleRecords.size(); i++) {
            ElectricityPriceUpdateBO rowRecord = priceRuleRecords.get(i);
            String valley = rowRecord.getValleyPrice().trim();
            if(StrUtil.isNotBlank(valley)
                    && !ReUtil.isMatch(CommonConstant.DECIMAL_PATTERN, valley)){
                illegalValleyList.add(String.valueOf(i + 3));
            }
        }
        if(CollUtil.isNotEmpty(illegalValleyList)){
            validateRespBO = new ElectricityPriceStructureAndRuleValidateRespBO();
            validateRespBO.setIllegalValleyList(illegalValleyList);
            return validateRespBO;
        }

        //校验最大需量的合法性
        for (int i = 0; i < priceRuleRecords.size(); i++) {
            ElectricityPriceUpdateBO rowRecord = priceRuleRecords.get(i);
            String maxCapacityPrice = rowRecord.getMaxCapacityPrice().trim();
            if(StrUtil.isNotBlank(maxCapacityPrice)
                    && !ReUtil.isMatch(CommonConstant.DECIMAL_PATTERN, maxCapacityPrice)){
                illegalMaxCapacityPriceList.add(String.valueOf(i + 3));
            }
        }
        if(CollUtil.isNotEmpty(illegalMaxCapacityPriceList)){
            validateRespBO = new ElectricityPriceStructureAndRuleValidateRespBO();
            validateRespBO.setIllegalMaxCapacityPriceList(illegalMaxCapacityPriceList);
            return validateRespBO;
        }

        //校验容需量的合法性
        for (int i = 0; i < priceRuleRecords.size(); i++) {
            ElectricityPriceUpdateBO rowRecord = priceRuleRecords.get(i);
            String transformerCapacityPrice = rowRecord.getTransformerCapacityPrice().trim();
            if(StrUtil.isNotBlank(transformerCapacityPrice)
                    && !ReUtil.isMatch(CommonConstant.DECIMAL_PATTERN, transformerCapacityPrice)){
                illegalTransformerCapacityPriceList.add(String.valueOf(i + 3));
            }
        }
        if(CollUtil.isNotEmpty(illegalTransformerCapacityPriceList)){
            validateRespBO = new ElectricityPriceStructureAndRuleValidateRespBO();
            validateRespBO.setIllegalTransformerCapacityPriceList(illegalTransformerCapacityPriceList);
            return validateRespBO;
        }
        return null;
    }

    /**
     * 校验季节区间
     *
     * @param seasonCreateBOList
     * @return ElectricityPriceStructureAndRuleValidateRespVO
     * @author sunjidong
     * @date 2022/5/6 9:04
     */
    @Override
    public ElectricityPriceStructureAndRuleValidateRespBO validateSeasonTime(List<ElectricitySeasonCreateBO> seasonCreateBOList) {
        ElectricityPriceStructureAndRuleValidateRespBO validateRespBO = new ElectricityPriceStructureAndRuleValidateRespBO();
        //校验是否存在季节、分时的区间
        boolean validateResult = false;
        //生效日期
        Date startDate = seasonCreateBOList.get(0).getStartDate();
        String startDateStr = DateUtil.formatDate(startDate);
        String year = startDateStr.split(CommonConstant.DATE_SPLIT)[0];
        DateTime startTimeOfWholeDay = DateUtil.parse(CommonConstant.DAY_EXAMPLE_ZERO, DatePattern.NORM_DATETIME_PATTERN);
        DateTime endTimeOfWholeDay = DateUtil.parse(CommonConstant.DAY_EXAMPLE_FOUR, DatePattern.NORM_DATETIME_PATTERN);
        //一天的经过的ms
        long msOfWholeDay = DateUtil.between(startTimeOfWholeDay, endTimeOfWholeDay, DateUnit.SECOND);
        DateTime firstDateOfYear = DateUtil.parse(year + CommonConstant.DATE_SPLIT + CommonConstant.FIRST_DAY_OF_YEAR, DatePattern.NORM_DATE_PATTERN);
        DateTime endDateOfYear = DateUtil.parse(year + CommonConstant.DATE_SPLIT + CommonConstant.LAST_DAY_OF_YEAR, DatePattern.NORM_DATE_PATTERN);
        //一年经过的天数
        long allYearDays = DateUtil.between(firstDateOfYear, endDateOfYear, DateUnit.DAY);

        List<SeasonDateBO> allSeasonSectionCreateBOList = new ArrayList<>();
        for (ElectricitySeasonCreateBO seasonCreateBO : seasonCreateBOList) {
            //获取某一个季节下的所有季节区间
            List<SeasonDateBO> seasonSectionCreateBOList = seasonCreateBO.getSeasonSectionCreateBOList();
            //获取某个季节下的所有分时区间
            List<ElectricityPriceStrategyBO> timeSectionCreateBOList = seasonCreateBO.getTimeSectionCreateBOList();
            //1、校验分时区间是否有交集以及是否满一天
            validateResult = validateSeasonTimeDetail(validateRespBO, msOfWholeDay, timeSectionCreateBOList);
            if (validateResult) {
                return validateRespBO;
            }
            allSeasonSectionCreateBOList.addAll(seasonSectionCreateBOList);
            //2、校验同一季节下的默认以及修正是否完全一样
            validateResult = validateIfSameOfTimeSection(validateRespBO, timeSectionCreateBOList);
            if (validateResult) {
                return validateRespBO;
            }
        }

        //2.1、校验季节区间是否存在交集以及是否满一年
        validateResult = validateSeasonSectionDetail(validateRespBO, year, allYearDays, allSeasonSectionCreateBOList);
        return validateResult ? validateRespBO : null;
    }

    /**
     * 校验季节区间是否存在交集以及是否满一年
     *
     * @author sunjidong
     * @date 2022/5/11 10:19
     */
    private boolean validateSeasonSectionDetail(ElectricityPriceStructureAndRuleValidateRespBO validateRespBO, String year, long allYearDays, List<SeasonDateBO> allSeasonSectionCreateBOList) {
        boolean validateResult = false;
        if (CollUtil.isNotEmpty(allSeasonSectionCreateBOList)) {
            validateResult = intersectionOfDate(year, allSeasonSectionCreateBOList);
            if (validateResult) {
                validateRespBO.setIfExistIntersectionSeasonSectionRule(Boolean.TRUE);
                return validateResult;
            }

            //2.2、校验季节区间是否满一年
            validateResult = ifReachWholeYear(year, allYearDays, allSeasonSectionCreateBOList);
            if (validateResult) {
                validateRespBO.setIfNotReachWholeYearSeasonSection(Boolean.TRUE);
                return validateResult;
            }
        }
        return validateResult;
    }

    /**
     * 校验同一季节下的默认以及修正是否完全一样
     *
     * @author sunjidong
     * @date 2022/5/11 10:11
     */
    private boolean validateIfSameOfTimeSection(ElectricityPriceStructureAndRuleValidateRespBO validateRespBO, List<ElectricityPriceStrategyBO> timeSectionCreateBOList) {
        Map<Boolean, List<ElectricityPriceStrategyBO>> booleanListMap = timeSectionCreateBOList.stream()
                .collect(Collectors.partitioningBy(timeSection -> StrUtil.isNotBlank(timeSection.getTemperature())
                ));
        List<ElectricityPriceStrategyBO> temperatureTimeSectionCreateBOList = booleanListMap.get(Boolean.TRUE);
        List<ElectricityPriceStrategyBO> defaultTimeSectionCreateBOList = booleanListMap.get(Boolean.FALSE);
        if (CollUtil.isNotEmpty(temperatureTimeSectionCreateBOList)
                && CollUtil.isNotEmpty(defaultTimeSectionCreateBOList)) {
            String temperatureTimeStr = CommonConstant.STR_BLANK;
            String defaultTimeStr = CommonConstant.STR_BLANK;
            for (ElectricityPriceStrategyBO strategyBO : temperatureTimeSectionCreateBOList) {
                List<ElectricityTimeSectionUpdateBO> electricityTimeSectionUpdateBOList = strategyBO.getElectricityTimeSectionUpdateBOList();
                temperatureTimeStr = electricityTimeSectionUpdateBOList.stream()
                        .sorted(Comparator.comparing(ElectricityTimeSectionUpdateBO::getStartTime)).map(temperatureTimeSection -> temperatureTimeSection.getPeriods() + temperatureTimeSection.getStartTime() + temperatureTimeSection.getEndTime()).collect(Collectors.joining(CommonConstant.DATE_SPLIT));
            }

            for (ElectricityPriceStrategyBO electricityPriceStrategyBO : defaultTimeSectionCreateBOList) {
                List<ElectricityTimeSectionUpdateBO> electricityTimeSectionUpdateBOList = electricityPriceStrategyBO.getElectricityTimeSectionUpdateBOList();
                defaultTimeStr = electricityTimeSectionUpdateBOList.stream().sorted(Comparator.comparing(ElectricityTimeSectionUpdateBO::getStartTime)).map(defaultTimeSection -> defaultTimeSection.getPeriods() + defaultTimeSection.getStartTime() + defaultTimeSection.getEndTime()).collect(Collectors.joining(CommonConstant.DATE_SPLIT));
            }
            if (temperatureTimeStr.equals(defaultTimeStr)) {
                validateRespBO.setIfExistSameTimeSection(Boolean.TRUE);
                return true;
            }
        }
        return false;
    }

    /**
     * 校验季节以及分时的区间
     * @author sunjidong
     * @date 2022/5/17 13:59
     */
    private boolean validateSeasonTimeDetail(ElectricityPriceStructureAndRuleValidateRespBO validateRespBO, long msOfWholeDay, List<ElectricityPriceStrategyBO> priceStrategyBOList) {
        boolean validateResult = false;
        if (CollUtil.isNotEmpty(priceStrategyBOList)) {
            //根据温度来分区成默认的以及温度修正策略的分时区间
            Map<Boolean, List<ElectricityPriceStrategyBO>> booleanTimeSectionListMap = priceStrategyBOList.stream()
                    .collect(Collectors.partitioningBy(timeSectionCreateBO -> StrUtil.isBlank(timeSectionCreateBO.getTemperature())));
            //默认分时区间集合
            List<ElectricityPriceStrategyBO> defaultTimeSectionCreateBOList = booleanTimeSectionListMap.get(Boolean.TRUE);
            if(CollUtil.isNotEmpty(defaultTimeSectionCreateBOList)){
                validateResult = intersectionOfTime(defaultTimeSectionCreateBOList);
                if (validateResult) {
                    validateRespBO.setIfExistIntersectionTimeSection(Boolean.TRUE);
                    return true;
                }
                //校验默认分时区间是否满一天
                validateResult = ifReachWholeDayTime(msOfWholeDay, defaultTimeSectionCreateBOList);
                if (validateResult) {
                    validateRespBO.setIfNotReachWholeDayTimeSection(Boolean.TRUE);
                    return true;
                }
            }

            //修正策略分时区间集合
            List<ElectricityPriceStrategyBO> amendTimeSectionCreateBOList = booleanTimeSectionListMap.get(Boolean.FALSE);
            if(CollUtil.isNotEmpty(amendTimeSectionCreateBOList)){
                validateResult = intersectionOfTime(amendTimeSectionCreateBOList);
                if (validateResult) {
                    validateRespBO.setIfExistIntersectionTimeSection(Boolean.TRUE);
                    return true;
                }
                //校验修正策略分时区间是否满一天
                validateResult = ifReachWholeDayTime(msOfWholeDay, amendTimeSectionCreateBOList);
                if (validateResult) {
                    validateRespBO.setIfNotReachWholeDayTimeSection(Boolean.TRUE);
                    return true;
                }
            }
        }
        return validateResult;
    }

    /**
     * 校验待删除的电价是否已绑定设备
     *
     * @param id
     * @return Boolean
     * @author sunjidong
     * @date 2022/5/9 10:18
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean validateDeletePriceRule(String id) {
        long countRuleEquipmentBindRecords = electricityPriceEquipmentCustomMapper.countRuleEquipmentBindRecordsById(Long.parseLong(id));
        //未查到绑定关系，直接删除
        if (countRuleEquipmentBindRecords <= 0L) {
            priceRuleMapper.deleteByPrimaryKey(Long.valueOf(id));
            return Boolean.TRUE;
        }
        //查到绑定关系
        return Boolean.FALSE;
    }

    /**
     * 取消区域时，校验是否绑定了设备
     *
     * @param structureId
     * @return ElectricityPriceStructureCreateBO
     * @author sunjidong
     * @date 2022/5/9 10:18
     */
    @Override
    public ElectricityPriceStructureCreateBO validateDeleteArea(String structureId, List<String> districtCodeList) {
        //通过体系id查找此体系绑定的设备所在的区域
        List<ElectricityPriceEquipment> electricityPriceEquipmentList = electricityPriceEquipmentCustomMapper.queryEquipmentBindingByStructureId(structureId);
        if (CollUtil.isEmpty(electricityPriceEquipmentList)) {
            return null;
        }
        //首先找到此体系下原适用的区域
        ElectricityPriceStructure electricityPriceStructure = electricityPriceStructureCustomMapper.selectByPrimaryKey(structureId);
        String districtCodes = electricityPriceStructure.getDistrictCodes();
        String[] districtCodeArray = districtCodes.split(CommonConstant.AREA_SPLIT);
        Stream<String> districtCodeStream = Arrays.stream(districtCodeArray);

        //查询绑定关系中涉及到的区域
        // todo 先用getUpdator代替设备所在得区域
        List<String> bindAreaList = electricityPriceEquipmentList.stream()
                .map(ElectricityPriceEquipment::getDistrictCodes)
                .collect(Collectors.toList());

        //匹配找到被取消的区域
        String noCancelArea = districtCodeStream.filter(districtCode -> {
            boolean matchResult = true;
            for (String nowDistrictCode : districtCodeList) {
                if (districtCode.equals(nowDistrictCode)) {
                    matchResult = false;
                    break;
                }
            }
            return matchResult;
        }).filter(districtCode -> {
            for (String bindArea : bindAreaList) {
                if (districtCode.equals(bindArea)) {
                    return true;
                }
            }
            return false;
        }).collect(Collectors.joining(CommonConstant.AREA_SPLIT));
        if (StrUtil.isEmpty(noCancelArea)) {
            return null;
        }
        ElectricityPriceStructureCreateBO structureCreateBO = new ElectricityPriceStructureCreateBO();
        structureCreateBO.setDistrictCodes(noCancelArea);
        return structureCreateBO;
    }

    /**
     * 根据省编码查找版本以及版本下的所有体系详细内容
     *
     * @param provinceCode
     * @return ElectricityPriceStructureDetailBO
     * @author sunjidong
     * @date 2022/5/9 15:53
     */
    @Override
    public ElectricityPriceStructureListDetailBO getLastVersionStructures(String provinceCode) {
        ElectricityPriceStructureListDetailBO structureListDetailBO = new ElectricityPriceStructureListDetailBO();
        ElectricityPriceVersion electricityPriceVersion = new ElectricityPriceVersion();
        electricityPriceVersion.setProvinceCode(provinceCode);
        electricityPriceVersion.setEndDate(DateUtil.parse(CommonConstant.DEFAULT_END_DATE, DatePattern.NORM_DATE_PATTERN));
        List<ElectricityPriceStructureDetailBO> structureDetailBOList = new ArrayList<>();
        //查询到该省下的所有体系
        List<ElectricityPriceStructure> priceStructureList = electricityPriceVersionCustomMapper.queryStructuresByCondition(electricityPriceVersion);
        for (ElectricityPriceStructure priceStructure : priceStructureList) {
            structureListDetailBO.setLastVersionId(priceStructure.getVersionId());
            ElectricityPriceStructureDetailBO structureDetail = getStructureAndRuleDetail(priceStructure);
            structureDetailBOList.add(structureDetail);
        }
        structureListDetailBO.setStructureDetailBOList(structureDetailBOList);
        return structureListDetailBO;
    }

    /**
     * 校验是否存在重复的季节对应的体系三要素组合
     *
     * @author sunjidong
     * @date 2022/5/11 10:36
     */
    private boolean validateIfExistDuplicatedStructureRule(List<ElectricityPriceStructureRuleUpdateBO> priceStructureRuleValidateList,
                                                           ElectricityPriceStructureAndRuleValidateRespBO validateRespBO) {
        int beforeDistinctSize = priceStructureRuleValidateList.size();
        priceStructureRuleValidateList = priceStructureRuleValidateList.stream().distinct().collect(Collectors.toList());
        int afterDistinctSize = priceStructureRuleValidateList.size();
        if (beforeDistinctSize != afterDistinctSize) {
            validateRespBO.setIfExistDuplicatedStructureRule(Boolean.TRUE);
            return true;
        }
        return false;
    }

    /**
     * 校验是否存在重复的电价规则的体系三要素组合
     *
     * @author sunjidong
     * @date 2022/5/11 10:36
     */
    private boolean validateIfExistDuplicatedRule(List<ElectricityPriceUpdateBO> priceRuleValidateReqVOList,
                                                  ElectricityPriceStructureAndRuleValidateRespBO validateRespBO) {
        int beforeDistinctRuleList = priceRuleValidateReqVOList.size();
        int afterDistinctRuleList = priceRuleValidateReqVOList.stream()
                .distinct()
                .collect(Collectors.toList())
                .size();
        if (beforeDistinctRuleList != afterDistinctRuleList) {
            validateRespBO.setIfExistDuplicatedRule(Boolean.TRUE);
            return true;
        }
        return false;
    }

    /**
     * 判断是否存在不可以编辑的电价规则
     *
     * @author sunjidong
     * @date 2022/5/11 11:05
     */
    private boolean validateIfCanEditPriceRules(ElectricityPriceStructureAndRuleValidateRespBO validateRespBO,
                                                List<ElectricityPriceUpdateBO> priceRuleValidateReqVOList) {
        List<ElectricityPriceUpdateBO> editPriceRuleValidateReqVOList = priceRuleValidateReqVOList.stream()
                .filter(priceRuleValidateReqVO -> ObjectUtil.isNotNull(priceRuleValidateReqVO.getRuleId())).collect(Collectors.toList());
        //获取被修改的电价规则里是否存有被绑定的，返回行号
        List<String> editPriceRuleSerialNoList = getCantEditPriceRuleSerialNos(editPriceRuleValidateReqVOList);
        if (CollUtil.isNotEmpty(editPriceRuleSerialNoList)) {
            validateRespBO.setCantEditPriceRules(editPriceRuleSerialNoList);
            return true;
        }
        return false;
    }

    /**
     * 校验是否缺少电度电价
     *
     * @author sunjidong
     * @date 2022/5/11 11:20
     */
    private boolean validateIfLackDistributionPriceList(List<ElectricityPriceStructureRuleUpdateBO> defaultStructureRuleCreateBOList,
                                                        List<ElectricityPriceStructureRuleUpdateBO> customStructureRuleCreateBOList,
                                                        List<ElectricityPriceUpdateBO> priceRuleValidateReqVOList,
                                                        List<ElectricityPriceStructureRuleUpdateBO> noExistTimeSectionStructureRuleList,
                                                        ElectricityPriceStructureAndRuleValidateRespBO validateRespBO,
                                                        boolean noTimeSection) {

        //找到三要素匹配，没有配置分时的规则
        List<String> lackDistributionPriceList = priceRuleValidateReqVOList.stream().filter(priceRuleValidateReqVO -> {
            boolean matchSuccess = false;
            for (ElectricityPriceStructureRuleUpdateBO ruleCreateBO : noExistTimeSectionStructureRuleList) {
                if (ruleCreateBO.getIndustries().contains(priceRuleValidateReqVO.getIndustry())
                        && ruleCreateBO.getStrategies().contains(priceRuleValidateReqVO.getStrategy())
                        && ruleCreateBO.getVoltageLevels().contains(priceRuleValidateReqVO.getVoltageLevel())
                        && StrUtil.isBlank(priceRuleValidateReqVO.getConsumptionPrice())) {
                    matchSuccess = true;
                }
            }
            if (!matchSuccess && noTimeSection && StrUtil.isBlank(priceRuleValidateReqVO.getConsumptionPrice())) {
                matchSuccess = true;
            }
            return matchSuccess;
        }).map(ElectricityPriceUpdateBO::getSerialNo).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(lackDistributionPriceList)) {
            validateRespBO.setLackDistributionPriceList(lackDistributionPriceList);
            return true;
        }
        return false;
    }

    /**
     * 获取默认的体系详细内容
     *
     * @author sunjidong
     * @date 2022/5/11 14:57
     */
    @Override
    public ElectricityPriceStructureDetailBO getDefaultStructureDetail(String type, String provinceCode) {
        ElectricityPriceStructureDetailBO priceStructureDetailBO = new ElectricityPriceStructureDetailBO();
        //先找到当前省份的字典
        Map<Integer, List<ElectricityPriceDictionaryBO>> priceElectricityDictionaries = priceManagerService.getPriceElectricityDictionaries(type, provinceCode);
        List<ElectricityPriceDictionaryBO> electricityPriceDictionaryBOS = priceElectricityDictionaries.get(Integer.parseInt(type));
        List<ElectricityPriceDetailBO> priceDetailBOList = new ArrayList<>();
        //构建默认的电价规则
        for (ElectricityPriceDictionaryBO dictionaryBO : electricityPriceDictionaryBOS) {
            ElectricityPriceDetailBO priceDetailBO = new ElectricityPriceDetailBO();
            priceDetailBO.setVoltageLevel(dictionaryBO.getCode());
            priceDetailBOList.add(priceDetailBO);
        }
        List<ElectricityPriceStructureRuleDetailBO> structureRuleDetailBOList = new ArrayList<>();
        ElectricityPriceStructureRuleDetailBO structureRuleDetailBO = new ElectricityPriceStructureRuleDetailBO();
        //构建默认的季节分时
        List<ElectricityTimeSectionUpdateBO> timeSectionUpdateBOList = new ArrayList<>();
        ElectricityTimeSectionUpdateBO timeSectionUpdateBO = new ElectricityTimeSectionUpdateBO();
        timeSectionUpdateBO.setEndTime(CommonConstant.END_TIME);
        timeSectionUpdateBO.setStartTime(CommonConstant.START_TIME);
        timeSectionUpdateBO.setPeriods(CommonConstant.LEVEL_CHINA);
        timeSectionUpdateBOList.add(timeSectionUpdateBO);

        List<ElectricityPriceStrategyBO> strategyBOList = new ArrayList<>();
        ElectricityPriceStrategyBO strategyBO = new ElectricityPriceStrategyBO();
        strategyBO.setElectricityTimeSectionUpdateBOList(timeSectionUpdateBOList);
        strategyBOList.add(strategyBO);

        List<SeasonDateBO> seasonDateList = new ArrayList<>();
        SeasonDateBO seasonDateBO = new SeasonDateBO();
        seasonDateBO.setSeaStartDate(CommonConstant.FIRST_DAY_OF_YEAR);
        seasonDateBO.setSeaEndDate(CommonConstant.LAST_DAY_OF_YEAR);
        seasonDateList.add(seasonDateBO);

        List<ElectricityPriceSeasonDetailBO> electricityPriceSeasonDetailBOS = new ArrayList<>();
        ElectricityPriceSeasonDetailBO seasonDetailBO = new ElectricityPriceSeasonDetailBO();
        seasonDetailBO.setSeasonDateList(seasonDateList);
        seasonDetailBO.setElectricityPriceStrategyBOList(strategyBOList);
        electricityPriceSeasonDetailBOS.add(seasonDetailBO);
        structureRuleDetailBO.setIndustries(CommonConstant.DEFAULT_TYPE);
        structureRuleDetailBO.setStrategies(CommonConstant.DEFAULT_TYPE);
        structureRuleDetailBO.setVoltageLevels(CommonConstant.DEFAULT_TYPE);
        structureRuleDetailBO.setElectricityPriceSeasonDetailBOS(electricityPriceSeasonDetailBOS);
        structureRuleDetailBOList.add(structureRuleDetailBO);

        priceStructureDetailBO.setElectricityPriceStructureRuleDetailBOS(structureRuleDetailBOList);
        priceStructureDetailBO.setElectricityPriceDetailBOList(priceDetailBOList);
        return priceStructureDetailBO;
    }

    /**
     * 根据体系id获取到体系的详情信息
     *
     * @author sunjidong
     * @date 2022/5/13 8:55
     */
    private ElectricityPriceStructureDetailBO getStructureAndRuleDetail(ElectricityPriceStructure priceStructure) {
        String structuredId = String.valueOf(priceStructure.getId());
        ElectricityPriceStructureDetailBO electricityPriceStructureDetailBO = CommonBOPOConvertMapper.INSTANCE.electricityPriceStructurePOTOBO(priceStructure);
        electricityPriceStructureDetailBO.setId(null);
        electricityPriceStructureDetailBO.setParentid(structuredId);
        //创建体系规则详情
        List<ElectricityPriceStructureRuleDetailBO> electricityPriceStructureRuleDetailBOS = new ArrayList<>();
        //根据体系id获取体系规则列表
        Map<String, Object> structureRuleQueryMap = new HashMap<>();
        structureRuleQueryMap.put(CommonConstant.STRUCTURE_ID, structuredId);
        structureRuleQueryMap.put(CommonConstant.STATE, BoolLogic.NO.getCode());
        List<ElectricityPriceStructureRule> electricityPriceStructureRules = electricityPriceStructureRuleCustomMapper.queryElectricityPriceRulesByCondition(structureRuleQueryMap);
        //规则列表组装
        electricityPriceStructureRules.forEach(structureRule -> {
            //创建体系规则详情与季节列表
            ElectricityPriceStructureRuleDetailBO electricityPriceStructureRuleDetailBO = ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.ElectricityPriceStructureRulePOTOBO(structureRule);
            electricityPriceStructureRuleDetailBO.setId(null);
            List<ElectricityPriceSeasonDetailBO> seasonDetailList = new ArrayList<>();
            //根据体系规则id查找季节列表
            List<ElectricitySeasonSection> electricitySeasonSectionList = electricityPriceSeasonSectionCustomMapper.querySeasonSectionListByStructureRuleId(String.valueOf(structureRule.getId()));
            //筛选出季节
            Set<String> seasonSectionIdList = electricitySeasonSectionList.stream().map(ElectricitySeasonSection::getSeasonSectionId).collect(Collectors.toSet());
            //对季节区间进行合并(同一个季节)
            seasonSectionIdList.forEach(seasonId -> {
                ElectricityPriceSeasonDetailBO electricityPriceSeasonDetailBO = new ElectricityPriceSeasonDetailBO();
                List<SeasonDateBO> seasonDateBOList = new ArrayList<>();
                electricitySeasonSectionList.stream().filter(seasonSection -> seasonSection.getSeasonSectionId().equals(seasonId))
                        .forEach(seasonSection -> {
                            electricityPriceSeasonDetailBO.setSeasonName(seasonSection.getSeasonSectionName());
                            //添加季节区间时间信息
                            SeasonDateBO seasonDateBO = new SeasonDateBO();
                            seasonDateBO.setSeaEndDate(seasonSection.getSeaEndDate());
                            seasonDateBO.setSeaStartDate(seasonSection.getSeaStartDate());
                            seasonDateBOList.add(seasonDateBO);
                        });
                //根据季节id查询分时区间信息
                List<ElectricityTimeSection> timeSectionList = electricityTimeSectionCustomMapper.getTimeSectionListBySeasonSectionId(seasonId);
                timeSectionList = timeSectionList.stream().peek(timeSection -> {
                    timeSection.setId(null);
                    timeSection.setTimeSectionId(null);
                }).collect(Collectors.toList());
                Map<String, List<ElectricityTimeSection>> timeMap = timeSectionList.stream().collect(
                        Collectors.groupingBy(
                                //将时区按照策略分组
                                time -> time.getCompare() + CommonConstant.DATE_SPLIT + time.getTemperature()
                        ));
                //组装规则列表
                List<ElectricityPriceStrategyBO> electricityPriceStrategyBOList = new ArrayList<>();
                timeMap.forEach((strategy, timeSectionDetailList) -> {
                    String[] split = strategy.split(CommonConstant.DATE_SPLIT);
                    ElectricityPriceStrategyBO electricityPriceStrategyBO = new ElectricityPriceStrategyBO();
                    if (split.length > 0) {
                        electricityPriceStrategyBO.setCompare(split[0]);
                        electricityPriceStrategyBO.setTemperature(split[1]);
                    }
                    List<ElectricityTimeSectionUpdateBO> electricityTimeSectionUpdateBOS = ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.ElectricityTimeSectionPOListToBOList(timeSectionDetailList);
                    List<ElectricityTimeSectionUpdateBO> timeSectionUpdateBOList = new ArrayList<>();
                    timeSectionUpdateBOList.addAll(electricityTimeSectionUpdateBOS);
                    electricityPriceStrategyBO.setElectricityTimeSectionUpdateBOList(timeSectionUpdateBOList);
                    electricityPriceStrategyBOList.add(electricityPriceStrategyBO);

                });
                //策略列表添加入季节
                electricityPriceSeasonDetailBO.setElectricityPriceStrategyBOList(electricityPriceStrategyBOList);
                //季节列表时间添加入季节
                electricityPriceSeasonDetailBO.setSeasonDateList(seasonDateBOList);
                //季节添加进季节列表
                seasonDetailList.add(electricityPriceSeasonDetailBO);
            });
            //季节列表添加进体系规则
            electricityPriceStructureRuleDetailBO.setElectricityPriceSeasonDetailBOS(seasonDetailList);
            electricityPriceStructureRuleDetailBO.setStrategies(structureRule.getStrategies());
            electricityPriceStructureRuleDetailBO.setIndustries(structureRule.getIndustries());
            electricityPriceStructureRuleDetailBO.setVoltageLevels(structureRule.getVoltageLevels());
            //将体系规则添加进规则列表
            electricityPriceStructureRuleDetailBOS.add(electricityPriceStructureRuleDetailBO);

        });
        //组装体系价格列表
        List<ElectricityPriceDetailPO> priceDetailPOList = electricityPriceCustomMapper.getPriceDetailListByStructureId(structuredId);
        List<ElectricityPriceDetailBO> electricityPriceDetailBOs = ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.electricityPriceDetailPOListToBOList(priceDetailPOList);
        electricityPriceDetailBOs = electricityPriceDetailBOs.stream().peek( priceDetail -> {
            priceDetail.setPriceId(null);
            priceDetail.setRuleId(null);
        }).collect(Collectors.toList());
        //将规则列表添加进体系
        electricityPriceStructureDetailBO.setElectricityPriceStructureRuleDetailBOS(electricityPriceStructureRuleDetailBOS);
        electricityPriceStructureDetailBO.setElectricityPriceDetailBOList(electricityPriceDetailBOs);
        return electricityPriceStructureDetailBO;
    }

    public static void main(String[] args) {
//        boolean compare1 = DateUtil.compare(DateUtil.parse("2022-12-12 12:00:00", DatePattern.NORM_DATETIME_PATTERN), DateUtil.parse("2022-12-12 12:00:00", DatePattern.NORM_DATETIME_PATTERN)) <= 0;
//        System.out.println(compare1);


        DateTime firstDateOfYear = DateUtil.parse("2022-01-01", DatePattern.NORM_DATE_PATTERN);
        DateTime endDateOfYear = DateUtil.parse("2022-10-01", DatePattern.NORM_DATE_PATTERN);
        //一年经过的天数
        long allYearDays = DateUtil.between(firstDateOfYear, endDateOfYear, DateUnit.DAY);

        DateTime firstDateOfYear2 = DateUtil.parse("2022-10-02", DatePattern.NORM_DATE_PATTERN);
        DateTime endDateOfYear2 = DateUtil.parse("2022-12-21", DatePattern.NORM_DATE_PATTERN);
        //一年经过的天数
        long allYearDays2 = DateUtil.between(firstDateOfYear2, endDateOfYear2, DateUnit.DAY);

        DateTime firstDateOfYear3 = DateUtil.parse("2022-12-22", DatePattern.NORM_DATE_PATTERN);
        DateTime endDateOfYear3 = DateUtil.parse("2022-12-31", DatePattern.NORM_DATE_PATTERN);
        //一年经过的天数
        long allYearDays3 = DateUtil.between(firstDateOfYear3, endDateOfYear3, DateUnit.DAY);
        long l = allYearDays + allYearDays2 +allYearDays3;
        System.out.println(l);
    }
}