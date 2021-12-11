package com.enn.energy.price.biz.service;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.enn.energy.price.core.service.impl.CacheService;
import com.enn.energy.price.core.service.impl.PriceCacheClientImpl;
import com.enn.energy.price.biz.service.bo.*;
import com.enn.energy.price.common.constants.CommonConstant;
import com.enn.energy.price.common.error.ErrorCodeEnum;
import com.enn.energy.price.common.error.PriceException;
import com.enn.energy.price.common.utils.PriceCollectionUtils;
import com.enn.energy.price.common.utils.PriceDateUtils;
import com.enn.energy.price.common.utils.SnowFlake;
import com.enn.energy.price.dal.po.mbg.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import top.rdfa.framework.biz.ro.RdfaResult;

import java.util.*;

/**
 * 电价service.
 *
 * @author : wuchaon
 * @version : 1.0 2021/11/25 17:35
 * @since : 1.0
 **/
@Service
@Slf4j
public class ElectricityPriceService {

    @Autowired
    private ElectricityPriceEquipmentService electricityPriceEquipmentService;

    @Autowired
    private ElectricityPriceVersionService electricityPriceVersionService;

    @Autowired
    private ElectricityPriceRuleService electricityPriceRuleService;

    @Autowired
    private ElectricityPriceDetailService electricityPriceDetailService;

    @Autowired
    private ElectricityPriceSeasonService electricityPriceSeasonService;

    @Autowired
    private PriceCacheClientImpl priceCacheClientImpl;

    @Autowired
    private CacheService cacheService;

//    //需要新增的版本
//    private List<ElectricityPriceVersion> additionalPriceVersionList = new ArrayList<>();
//
//    private Map<String, String> additionalPriceVersionCache = new HashMap<>();
//
//    private List<ElectricityPriceRule> additionalPriceRuleList = new ArrayList<>();
//
//    private List<ElectricityPriceSeason> additionalPriceSeasonList = new ArrayList<>();
//
//    private List<ElectricityPriceDetail> additionalPriceDetailList = new ArrayList<>();
//
//    private List<ElectricityPriceEquipment> additionalPriceEquipmentList = new ArrayList<>();
//
//    //有时间交集的版本
//    private List<ElectricityPriceVersion> electricityPriceVersionList = new ArrayList<>();
//
//    //有交集的版本的之前的field
//    private List<String> allFullOldFieldList = new ArrayList<>();
//
//    //需要新增缓存的交集版本的hkey
//    private Map<ElectricityPriceVersion, List<String>> versionOldField = new HashMap<>();
//
//    //没有被完全覆盖的，需要新增之前的缓存
//    private List<ElectricityPriceVersion> needAddOldCacheList = new ArrayList<>();
//
//    //保存当前设备下所有的价格缓存数据
//    private Map<String, String> fieldValue = new HashMap<>();
//
//    @Transactional(rollbackFor = Exception.class)
//    public void addElectricityPrice(ElectricityPriceVersionBO electricityPriceVersionBO) {
//
//        //绑定类型:设备
//        if ("1".equals(electricityPriceVersionBO.getBindType())) {
//
//            String cacheKey = electricityPriceVersionBO.getElectricityPriceEquipmentBO().getEquipmentId();
//
//            List<ElectricityPriceEquipment> electricityPriceEquipmentList = electricityPriceEquipmentService.selectByEquipmentId(electricityPriceVersionBO.getElectricityPriceEquipmentBO().getEquipmentId());
//
//            if (CollectionUtil.isEmpty(electricityPriceEquipmentList)) {
//
//                directlyAddElectricityPrice(electricityPriceVersionBO);
//
//            } else {
//                List<String> versionIdList = new ArrayList<>();
//
//                for (ElectricityPriceEquipment oldElectricityPriceEquipment : electricityPriceEquipmentList) {
//                    versionIdList.add(oldElectricityPriceEquipment.getVersionId());
//                }
//
//                this.electricityPriceVersionList = findDateIntersection(electricityPriceVersionBO, electricityPriceVersionService.selectByVersionIds(versionIdList));
//
//                //版本号小于任意一个版本,或者时间没有交集
//                if (CollectionUtil.isEmpty(this.electricityPriceVersionList)) {
//                    return;
//                }
//                directlyAddElectricityPrice(electricityPriceVersionBO);
//                addDateIntersectionVersion(electricityPriceVersionBO, this.electricityPriceVersionList);
//            }
//
//
//            if (CollectionUtil.isNotEmpty(this.electricityPriceVersionList)) {
//                //删除原来版本的缓存
//                for (String fullOldField : this.allFullOldFieldList) {
//                    priceCacheClientImpl.hDelete(cacheKey, CommonConstant.ELECTRICITY_PRICE, fullOldField);
//                }
//                //批量更新之前的版本
//                electricityPriceVersionService.batchUpdatePriceVersion(this.electricityPriceVersionList);
//            }
//
//            //批量添加额外的版本
//            if (CollectionUtil.isNotEmpty(this.additionalPriceVersionList)) {
//                electricityPriceVersionService.batchAddElectricityPriceVersion(this.additionalPriceVersionList);
//                electricityPriceRuleService.batchAddElectricityPriceRule(this.additionalPriceRuleList);
//                electricityPriceSeasonService.batchAddElectricityPriceSeason(this.additionalPriceSeasonList);
//                electricityPriceDetailService.batchAddElectricityPriceDetail(this.additionalPriceDetailList);
//                electricityPriceEquipmentService.batchAddElectricityPriceEquipment(this.additionalPriceEquipmentList);
//                //添加额外版本的缓存
//                this.additionalPriceVersionCache.forEach((field, value) -> priceCacheClientImpl.hPut(cacheKey, CommonConstant.ELECTRICITY_PRICE, field, value));
//            }
//
//            //更新老版本的缓存
//            for (ElectricityPriceVersion electricityPriceVersion : this.needAddOldCacheList) {
//                if (electricityPriceVersion.getState() != null && electricityPriceVersion.getState() == 0) {
//                    for (String fullOldField : this.versionOldField.get(electricityPriceVersion)) {
//                        String[] fieldsArray = fullOldField.split(CommonConstant.VALUE_SPERATOR);
//                        if (null != fieldsArray && fieldsArray.length == 6) {
//                            String newField = electricityPriceVersion.getVersionId() + CommonConstant.VALUE_SPERATOR + PriceDateUtils.dayDateToStr(electricityPriceVersion.getStartDate()) + CommonConstant.VALUE_SPERATOR +
//                                    PriceDateUtils.dayDateToStr(electricityPriceVersion.getEndDate()) + CommonConstant.VALUE_SPERATOR + fieldsArray[3] + CommonConstant.VALUE_SPERATOR + fieldsArray[4] + CommonConstant.VALUE_SPERATOR + fieldsArray[5];
//                            priceCacheClientImpl.hPut(cacheKey, CommonConstant.ELECTRICITY_PRICE, newField, this.fieldValue.get(fullOldField));
//                        }
//                        this.fieldValue.remove(fullOldField);
//                    }
//                }
//            }
//        }
//    }
//
//
//    /**
//     * 比较时间交集
//     *
//     * @param electricityPriceVersionBO
//     * @param electricityPriceVersionList
//     * @return
//     */
//    private List<ElectricityPriceVersion> findDateIntersection(ElectricityPriceVersionBO electricityPriceVersionBO, List<ElectricityPriceVersion> electricityPriceVersionList) {
//
//        List<ElectricityPriceVersion> electricityPriceVersionResultList = new ArrayList<>();
//        if (CollectionUtil.isNotEmpty(electricityPriceVersionList)) {
//            for (ElectricityPriceVersion electricityPriceVersion : electricityPriceVersionList) {
//                if (electricityPriceVersionBO.getStartDate().compareTo(electricityPriceVersion.getEndDate()) < 1
//                        && electricityPriceVersionBO.getEndDate().compareTo(electricityPriceVersion.getStartDate()) > -1) {
//                    if (electricityPriceVersionBO.getVersionName().compareTo(electricityPriceVersion.getVersionName()) < 0) {
//                        return null;
//                    }
//                    electricityPriceVersionResultList.add(electricityPriceVersion);
//                }
//
//            }
//        }
//        return electricityPriceVersionResultList;
//    }
//
//
//    /**
//     * 直接添加价格版本，无需时间交集、版本
//     *
//     * @param electricityPriceVersionBO
//     */
//    protected void directlyAddElectricityPrice(ElectricityPriceVersionBO electricityPriceVersionBO) {
//
//        ElectricityPriceVersion electricityPriceVersion = BeanUtil.toBean(electricityPriceVersionBO, ElectricityPriceVersion.class);
//        electricityPriceVersion.setVersionId(String.valueOf(SnowFlake.getInstance().nextId()));
//        electricityPriceVersion.setCreateTime(new Date());
//        electricityPriceVersion.setUpdateTime(new Date());
//        electricityPriceVersion.setState(0);
//
//        ElectricityPriceEquipment electricityPriceEquipment = BeanUtil.toBean(electricityPriceVersionBO.getElectricityPriceEquipmentBO(), ElectricityPriceEquipment.class);
//        electricityPriceEquipment.setVersionId(electricityPriceVersion.getVersionId());
//        electricityPriceEquipment.setSystemCode(electricityPriceVersionBO.getSystemCode());
//        electricityPriceEquipment.setCreateTime(new Date());
//        electricityPriceEquipment.setUpdateTime(new Date());
//        electricityPriceEquipment.setState(0);
//        electricityPriceVersion.setEquipmentId(electricityPriceEquipment.getEquipmentId());
//        electricityPriceVersion.setEquipmentName(electricityPriceEquipment.getEquipmentName());
//
//        List<ElectricityPriceRule> allElectricityPriceRuleList = new ArrayList<>();
//        List<ElectricityPriceSeason> allElectricityPriceSeasonList = new ArrayList<>();
//        List<ElectricityPriceDetail> allElectricityPriceDetailList = new ArrayList<>();
//
//        for (ElectricityPriceRuleBO electricityPriceRuleBO : electricityPriceVersionBO.getElectricityPriceRuleBOList()) {
//
//            ElectricityPriceRule electricityPriceRule = BeanUtil.toBean(electricityPriceRuleBO, ElectricityPriceRule.class);
//            electricityPriceRule.setRuleId(String.valueOf(SnowFlake.getInstance().nextId()));
//            electricityPriceRule.setVersionId(electricityPriceVersion.getVersionId());
//            electricityPriceRule.setCreateTime(new Date());
//            electricityPriceRule.setUpdateTime(new Date());
//            electricityPriceRule.setState(0);
//            allElectricityPriceRuleList.add(electricityPriceRule);
//            electricityPriceEquipment.setRuleId(electricityPriceRule.getRuleId());
//
//            for (ElectricityPriceSeasonBO electricityPriceSeasonBO : electricityPriceRuleBO.getElectricityPriceSeasonBOList()) {
//
//                ElectricityPriceSeason electricityPriceSeason = BeanUtil.toBean(electricityPriceSeasonBO, ElectricityPriceSeason.class);
//                electricityPriceSeason.setVersionId(electricityPriceVersion.getVersionId());
//                electricityPriceSeason.setRuleId(electricityPriceRule.getRuleId());
//                electricityPriceSeason.setSeasonId(String.valueOf(SnowFlake.getInstance().nextId()));
//                electricityPriceSeason.setCreateTime(new Date());
//                electricityPriceSeason.setUpdateTime(new Date());
//                electricityPriceSeason.setState(0);
//                allElectricityPriceSeasonList.add(electricityPriceSeason);
//
//                String versionSeasonField = electricityPriceVersion.getVersionId() + CommonConstant.VALUE_SPERATOR + PriceDateUtils.dayDateToStr(electricityPriceVersion.getStartDate()) + CommonConstant.VALUE_SPERATOR + PriceDateUtils.dayDateToStr(electricityPriceVersion.getEndDate())
//                        + CommonConstant.VALUE_SPERATOR + electricityPriceSeason.getSeaStartDate() + CommonConstant.VALUE_SPERATOR + electricityPriceSeason.getSeaEndDate() + CommonConstant.VALUE_SPERATOR + electricityPriceSeason.getPricingMethod();
//
//                List<ElectricityPriceDetailCache> electricityPriceDetailCacheList = new ArrayList<>();
//                List<ElectricityPriceDetail> electricityPriceDetailList = PriceCollectionUtils.convertEntity(electricityPriceSeasonBO.getElectricityPriceDetailBOList(), ElectricityPriceDetail.class);
//
//                for (ElectricityPriceDetail electricityPriceDetail : electricityPriceDetailList) {
//                    electricityPriceDetail.setVersionId(electricityPriceVersion.getVersionId());
//                    electricityPriceDetail.setRuleId(electricityPriceRule.getRuleId());
//                    electricityPriceDetail.setDetailId(String.valueOf(SnowFlake.getInstance().nextId()));
//                    electricityPriceDetail.setSeasonId(electricityPriceSeason.getSeasonId());
//                    electricityPriceDetail.setCreateTime(new Date());
//                    electricityPriceDetail.setUpdateTime(new Date());
//                    electricityPriceDetail.setState(0);
//                    electricityPriceDetailCacheList.add(BeanUtil.copyProperties(electricityPriceDetail, ElectricityPriceDetailCache.class));
//
//                }
//                this.additionalPriceVersionCache.put(versionSeasonField, JSON.toJSONString(electricityPriceDetailCacheList));
//                allElectricityPriceDetailList.addAll(electricityPriceDetailList);
//            }
//
//        }
//
//        //添加版本
//        //electricityPriceVersionService.addElectricityPriceVersion(electricityPriceVersion);
//        this.additionalPriceVersionList.add(electricityPriceVersion);
//        //添加设备
//        //electricityPriceEquipmentService.addElectricityPriceEquipment(electricityPriceEquipment);
//        this.additionalPriceEquipmentList.add(electricityPriceEquipment);
//        // electricityPriceRuleService.batchAddElectricityPriceRule(allElectricityPriceRuleList);
//        this.additionalPriceRuleList.addAll(allElectricityPriceRuleList);
//        //electricityPriceSeasonService.batchAddElectricityPriceSeason(allElectricityPriceSeasonList);
//        this.additionalPriceSeasonList.addAll(allElectricityPriceSeasonList);
//        //electricityPriceDetailService.batchAddElectricityPriceDetail(allElectricityPriceDetailList);
//        this.additionalPriceDetailList.addAll(allElectricityPriceDetailList);
//        //添加电价缓存
////        //fieldValueCache.forEach((key, fieldValue) -> priceCacheClientImpl.hPut(cacheKey, CommonConstant.ELECTRICITY_PRICE, key, JSON.toJSONString(fieldValue)));
////        this.additionalPriceVersionCache.putAll(fieldValueCache);
//    }
//
//
//    /**
//     * 添加有交集的价格版本
//     *
//     * @param electricityPriceVersionBO
//     * @param electricityPriceVersionList
//     */
//    private void addDateIntersectionVersion(ElectricityPriceVersionBO electricityPriceVersionBO, List<ElectricityPriceVersion> electricityPriceVersionList) {
//
//        String cacheKey = electricityPriceVersionBO.getElectricityPriceEquipmentBO().getEquipmentId();
//        Set<Object> hKeys = priceCacheClientImpl.hashKeys(cacheKey, CommonConstant.ELECTRICITY_PRICE);
//
//        for (Object hKey : hKeys) {
//            fieldValue.put((String) hKey, priceCacheClientImpl.hGet(cacheKey, CommonConstant.ELECTRICITY_PRICE, hKey));
//        }
//
//        for (ElectricityPriceVersion electricityPriceVersion : electricityPriceVersionList) {
//
//            String partOldField = electricityPriceVersion.getVersionId() + CommonConstant.VALUE_SPERATOR + PriceDateUtils.dayDateToStr(electricityPriceVersion.getStartDate()) + CommonConstant.VALUE_SPERATOR +
//                    PriceDateUtils.dayDateToStr(electricityPriceVersion.getEndDate());
//
//            List<String> fullOldFieldList = new ArrayList<>();
//            for (String field : fieldValue.keySet()) {
//                if (StringUtils.startsWith(field, partOldField)) {
//                    fullOldFieldList.add(field);
//                }
//            }
//            this.allFullOldFieldList.addAll(fullOldFieldList);
//            this.versionOldField.put(electricityPriceVersion, fullOldFieldList);
//
//            //历史版本的开始日期小于当前添加的价格版本的开始日期
//            if (electricityPriceVersion.getStartDate().compareTo(electricityPriceVersionBO.getStartDate()) < 0) {
//
//                //额外的时间段版本的数据
//                if (electricityPriceVersion.getEndDate().compareTo(electricityPriceVersionBO.getEndDate()) > 0) {
//
//                    ElectricityPriceVersion additionalElectricityPriceVersion = BeanUtil.toBean(electricityPriceVersion, ElectricityPriceVersion.class);
//                    additionalElectricityPriceVersion.setStartDate(PriceDateUtils.addDateByday(electricityPriceVersionBO.getEndDate(), 1));
//                    additionalElectricityPriceVersion.setLastVersionId(electricityPriceVersion.getVersionId());
//                    additionalElectricityPriceVersion.setVersionId(String.valueOf(SnowFlake.getInstance().nextId()));
//                    this.additionalPriceVersionList.add(additionalElectricityPriceVersion);
//                    this.additionalPriceRuleList.addAll(getAdditionalPriceRule(additionalElectricityPriceVersion.getVersionId(), electricityPriceVersion.getVersionId()));
//                    this.additionalPriceSeasonList.addAll(getAdditionalPriceSeason(additionalElectricityPriceVersion.getVersionId(), electricityPriceVersion.getVersionId()));
//                    this.additionalPriceDetailList.addAll(getAdditionalPriceDetail(additionalElectricityPriceVersion.getVersionId(), electricityPriceVersion.getVersionId()));
//                    this.additionalPriceEquipmentList.addAll(getAdditionalPriceEquipment(additionalElectricityPriceVersion.getVersionId(), electricityPriceVersion.getVersionId()));
//
//                    for (String fullOldField : fullOldFieldList) {
//                        String[] fieldsArray = fullOldField.split(CommonConstant.VALUE_SPERATOR);
//                        if (null != fieldsArray && fieldsArray.length == 6) {
//                            String newField = additionalElectricityPriceVersion.getVersionId() + CommonConstant.VALUE_SPERATOR + PriceDateUtils.dayDateToStr(additionalElectricityPriceVersion.getStartDate()) + CommonConstant.VALUE_SPERATOR +
//                                    PriceDateUtils.dayDateToStr(additionalElectricityPriceVersion.getEndDate()) + CommonConstant.VALUE_SPERATOR + fieldsArray[3] + CommonConstant.VALUE_SPERATOR + fieldsArray[4] + CommonConstant.VALUE_SPERATOR + fieldsArray[5];
//                            this.additionalPriceVersionCache.put(newField, this.fieldValue.get(fullOldField));
//                        }
//                    }
//                }
//
//                electricityPriceVersion.setEndDate(PriceDateUtils.addDateByday(electricityPriceVersionBO.getStartDate(), -1));
//                this.needAddOldCacheList.add(electricityPriceVersion);
//
//            } else {
//                electricityPriceVersion.setStartDate(PriceDateUtils.addDateByday(electricityPriceVersionBO.getEndDate(), 1));
//                //完全覆盖老版本的情况
//                if (electricityPriceVersion.getEndDate().compareTo(electricityPriceVersion.getStartDate()) < 0) {
//                    electricityPriceVersion.setState(1);
//                    electricityPriceVersion.setStartDate(null);
//                } else {
//                    this.needAddOldCacheList.add(electricityPriceVersion);
//                }
//            }
//            electricityPriceVersion.setUpdateTime(new Date());
//        }
//
//    }
//
//
//    private List<ElectricityPriceRule> getAdditionalPriceRule(String newVersionId, String oldVersionId) {
//
//        List<ElectricityPriceRule> priceRuleList = electricityPriceRuleService.selectPriceRulesByVersionId(oldVersionId);
//        for (ElectricityPriceRule rule : priceRuleList) {
//            rule.setVersionId(newVersionId);
//        }
//        return priceRuleList;
//    }
//
//    private List<ElectricityPriceSeason> getAdditionalPriceSeason(String newVersionId, String oldVersionId) {
//
//        List<ElectricityPriceSeason> priceSeasonList = electricityPriceSeasonService.selectPriceSeasonsByVersionId(oldVersionId);
//        for (ElectricityPriceSeason season : priceSeasonList) {
//            season.setVersionId(newVersionId);
//        }
//        return priceSeasonList;
//    }
//
//    private List<ElectricityPriceDetail> getAdditionalPriceDetail(String newVersionId, String oldVersionId) {
//
//        List<ElectricityPriceDetail> priceDetailList = electricityPriceDetailService.selectPriceDetailsByVersionId(oldVersionId);
//        for (ElectricityPriceDetail detail : priceDetailList) {
//            detail.setVersionId(newVersionId);
//        }
//        return priceDetailList;
//    }
//
//    private List<ElectricityPriceEquipment> getAdditionalPriceEquipment(String newVersionId, String oldVersionId) {
//
//        List<ElectricityPriceEquipment> priceEquipmentList = electricityPriceEquipmentService.selectPriceEquipmentsByVersionId(oldVersionId);
//        for (ElectricityPriceEquipment equipment : priceEquipmentList) {
//            equipment.setVersionId(newVersionId);
//        }
//        return priceEquipmentList;
//    }


    /**
     * 生效电价版本删除
     *
     * @param versionId
     */
    @Transactional(rollbackFor = Exception.class)
    public RdfaResult delElectricityPrice(String versionId, boolean isCommon) {
        //获取已有版本电价，不存在，则抛出不存在异常，校验当前的版本是否生效，生效，则报不可用删除
        ElectricityPriceVersion electricityPriceVersion = electricityPriceVersionService.selectByVersionId(versionId);
        if (ObjectUtils.isEmpty(electricityPriceVersion)) { //ObjectUtils
            return RdfaResult.success("");
        }
        //校验版本是否生效，
        if (!isCommon && isVaLid(electricityPriceVersion)) {
            return RdfaResult.fail("E30002", "this version is in effect and cannot be deleted ");
        }
        //通过versionId 查询ruleid
//        List<ElectricityPriceRuleBO> ruleBos = electricityPriceRuleService.selectRuleListByVersionId(versionId);
        //关联的规则  版本设备的绑定关系 版本删除
        electricityPriceVersionService.updatePriceVersionState(versionId);//删除版本
        electricityPriceRuleService.updatePriceRuleState(versionId);//删除规则
        electricityPriceEquipmentService.updatePriceEquipmentState(versionId);//删除设备
        electricityPriceDetailService.deleteDetailsByVersionId(versionId);//价格明细 TODO 根据 versionId 修改详情
        electricityPriceSeasonService.updateSeasonStateByVersionId(versionId);//删除季节
        //更新删除版本的上一个版本的结束时间

        removeRedisPriceVersionData(versionId);//清除缓存
        return RdfaResult.success("");
    }

    private void removeRedisPriceVersionData(String versionId) {
        //通过versionId获取所有已绑定的设备
        List<ElectricityPriceEquipmentBO> equipmentBOS = electricityPriceEquipmentService.selectEquByCondition(versionId);
        for (ElectricityPriceEquipmentBO item : equipmentBOS) {
            String key = item.getEquipmentId();
            Set<String> hKeys = cacheService.getHKeysWithPattern(key, CommonConstant.ELECTRICITY_PRICE, versionId + "#");
            hKeys.forEach(hkey -> cacheService.hdelHashKey(key, CommonConstant.ELECTRICITY_PRICE, hkey));//删除包含 versionId 的 hashKey
        }
    }

    private boolean isVaLid(ElectricityPriceVersion electricityPriceVersion) {
        //版本生效判定 ： 当前时间 >= 版本生效时间
        Date currentDate = new Date();
        return currentDate.compareTo(electricityPriceVersion.getStartDate()) >= 0;
    }


    /**
     * 更新价格版本
     *
     * @param electricityPriceVersionBO
     */
//    public void updateElectricityPrice(ElectricityPriceVersionBO electricityPriceVersionBO) {
//
//        ElectricityPriceVersion electricityPriceVersion = electricityPriceVersionService.selectByVersionId(electricityPriceVersionBO.getVersionId());
//
//        if (null == electricityPriceVersion) {
//            throw new PriceException(ErrorCodeEnum.NON_EXISTENT_DATA_EXCEPTION.getErrorCode(), "当前电价版本不存在");
//        }
//
//        //当前修改的版本是未来的版本
//        if (electricityPriceVersion.getStartDate().compareTo(new Date()) > 0) {
//            //删除老的
//            delElectricityPrice(electricityPriceVersionBO.getVersionId(), true);
//
//        } else {
//            ElectricityPriceVersion newElectricityPriceVersion = new ElectricityPriceVersion();
//            newElectricityPriceVersion.setVersionId(electricityPriceVersionBO.getVersionId());
//            newElectricityPriceVersion.setEndDate(new Date());
//            newElectricityPriceVersion.setUpdateTime(new Date());
//
//
//            String cacheKey = electricityPriceVersionBO.getElectricityPriceEquipmentBO().getEquipmentId();
//            Set<Object> hKeys = priceCacheClientImpl.hashKeys(cacheKey, CommonConstant.ELECTRICITY_PRICE);
//
//            //保存当前设备下当前版本的所有的价格缓存数据
//            Map<String, String> fieldValue = new HashMap<>();
//            for (Object hKey : hKeys) {
//                if (StringUtils.startsWith((String) hKey, electricityPriceVersionBO.getVersionId())) {
//                    fieldValue.put((String) hKey, priceCacheClientImpl.hGet(cacheKey, CommonConstant.ELECTRICITY_PRICE, hKey));
//                }
//            }
//
//            //删除老缓存
//            fieldValue.forEach((field, value) -> priceCacheClientImpl.hDelete(cacheKey, CommonConstant.ELECTRICITY_PRICE, field));
//
//            //更新老版本的结束时间
//            electricityPriceVersionService.updatePriceVersion(newElectricityPriceVersion);
//            //新增缓存
//            fieldValue.forEach((field, value) -> {
//                String[] fields = field.split(CommonConstant.VALUE_SPERATOR);
//                String newField = fields[0] + CommonConstant.VALUE_SPERATOR + fields[1] + CommonConstant.VALUE_SPERATOR +
//                        PriceDateUtils.dayDateToStr(new Date()) + CommonConstant.VALUE_SPERATOR + fields[3] + CommonConstant.VALUE_SPERATOR + fields[4] + CommonConstant.VALUE_SPERATOR + fields[5];
//
//                priceCacheClientImpl.hPut(cacheKey, CommonConstant.ELECTRICITY_PRICE, newField, value);
//            });
//
//            electricityPriceVersionBO.setStartDate(electricityPriceVersionBO.getStartDate().compareTo(new Date()) > 0 ? electricityPriceVersionBO.getStartDate() : PriceDateUtils.addDateByday(new Date(), 1));
//        }
//
//        electricityPriceVersionBO.setLastVersionId(electricityPriceVersionBO.getVersionId());
//        addElectricityPrice(electricityPriceVersionBO);
//    }
//

}
