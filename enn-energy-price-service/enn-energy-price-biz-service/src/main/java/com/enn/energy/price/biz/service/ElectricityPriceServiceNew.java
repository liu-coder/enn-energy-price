package com.enn.energy.price.biz.service;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.enn.energy.price.biz.service.bo.*;
import com.enn.energy.price.common.constants.CommonConstant;
import com.enn.energy.price.common.error.ErrorCodeEnum;
import com.enn.energy.price.common.error.PriceException;
import com.enn.energy.price.common.utils.PriceCollectionUtils;
import com.enn.energy.price.common.utils.PriceDateUtils;
import com.enn.energy.price.common.utils.SnowFlake;
import com.enn.energy.price.core.service.impl.PriceCacheClientImpl;
import com.enn.energy.price.dal.po.mbg.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
public class ElectricityPriceServiceNew {

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


    @Transactional(rollbackFor = Exception.class)
    public void addElectricityPrice(ElectricityPriceVersionBO electricityPriceVersionBO, ElectricityPriceVersion updateElectricityPriceVersion) {

        //绑定类型:设备
        if ("1".equals(electricityPriceVersionBO.getBindType())) {

            List<ElectricityPriceVersionBO> updateElectricityPriceVersionList = findPriceVersion(electricityPriceVersionBO);

            //需要新增的版本
            Map<String, String> addPriceVersionCache = new HashMap<>();
            List<ElectricityPriceRule> addPriceRuleList = new ArrayList<>();
            List<ElectricityPriceSeason> addPriceSeasonList = new ArrayList<>();
            List<ElectricityPriceDetail> addPriceDetailList = new ArrayList<>();
            ElectricityPriceEquipment electricityPriceEquipment = new ElectricityPriceEquipment();
            ElectricityPriceVersion electricityPriceVersion = directlyAddElectricityPrice(electricityPriceVersionBO, addPriceVersionCache, addPriceRuleList, addPriceSeasonList, addPriceDetailList, electricityPriceEquipment);

            String cacheKey = electricityPriceVersionBO.getSystemCode() + CommonConstant.KEY_SPERATOR + electricityPriceVersionBO.getElectricityPriceEquipmentBO().getEquipmentId();
            Set<Object> hKeys = priceCacheClientImpl.hashKeys(cacheKey, CommonConstant.ELECTRICITY_PRICE);

            //调整需要更新版本的缓存field
            List<String> fullOldFieldList = new ArrayList<>();

            for (ElectricityPriceVersionBO updatePriceVersionBO : updateElectricityPriceVersionList) {
                for (Object hKey : hKeys) {
                    String[] fileds = ((String) hKey).split(CommonConstant.VALUE_SPERATOR);
                    if (fileds.length == 6 && fileds[2].equals(updatePriceVersionBO.getVersionId())) {
                        fullOldFieldList.add((String) hKey);
                        addPriceVersionCache.put(fileds[0] + CommonConstant.VALUE_SPERATOR + PriceDateUtils.dayDateToStr(updatePriceVersionBO.getEndDate()) + CommonConstant.VALUE_SPERATOR + fileds[2] + CommonConstant.VALUE_SPERATOR + fileds[3] + CommonConstant.VALUE_SPERATOR + fileds[4] + CommonConstant.VALUE_SPERATOR + fileds[5], priceCacheClientImpl.hGet(cacheKey, CommonConstant.ELECTRICITY_PRICE, hKey));
                    }
                }
            }

            //更新的时候，需要删除老版本缓存
            if (updateElectricityPriceVersion != null) {
                for (Object hKey : hKeys) {
                    String[] fileds = ((String) hKey).split(CommonConstant.VALUE_SPERATOR);
                    if (fileds.length == 6 && fileds[2].equals(updateElectricityPriceVersion.getVersionId())) {
                        fullOldFieldList.add((String) hKey);
                    }
                }
            }

            //删除需要更新版本的缓存
            for (String fullOldField : fullOldFieldList) {
                priceCacheClientImpl.hDelete(cacheKey, CommonConstant.ELECTRICITY_PRICE, fullOldField);
            }

            //批量更新之前的版本
            if (CollectionUtil.isNotEmpty(updateElectricityPriceVersionList)) {
                electricityPriceVersionService.batchUpdatePriceVersion(PriceCollectionUtils.convertEntity(updateElectricityPriceVersionList, ElectricityPriceVersion.class));
            }

            //更新的时候，删除老版本
            if (updateElectricityPriceVersion != null) {
                String updateVersionId = updateElectricityPriceVersion.getVersionId();
                electricityPriceVersionService.updatePriceVersionState(updateVersionId);
                electricityPriceRuleService.updatePriceRuleState(updateVersionId);
                electricityPriceSeasonService.updateSeasonStateByVersionId(updateVersionId);
                electricityPriceDetailService.deleteDetailsByVersionId(updateVersionId);
                electricityPriceEquipmentService.updatePriceEquipmentState(updateVersionId);
            }

            //添加版本
            electricityPriceVersionService.addElectricityPriceVersion(electricityPriceVersion);
            electricityPriceRuleService.batchAddElectricityPriceRule(addPriceRuleList);
            electricityPriceSeasonService.batchAddElectricityPriceSeason(addPriceSeasonList);
            electricityPriceDetailService.batchAddElectricityPriceDetail(addPriceDetailList);
            electricityPriceEquipmentService.addElectricityPriceEquipment(electricityPriceEquipment);
            //添加缓存
            addPriceVersionCache.forEach((field, value) -> priceCacheClientImpl.hPut(cacheKey, CommonConstant.ELECTRICITY_PRICE, field, value));
        }

    }


    /**
     * 查找需要更新的版本
     *
     * @param electricityPriceVersionBO
     * @return
     */
    private List<ElectricityPriceVersionBO> findPriceVersion(ElectricityPriceVersionBO electricityPriceVersionBO) {

        List<ElectricityPriceVersionBO> updateElectricityPriceVersionList = new ArrayList<>();
        ElectricityPriceEquipment electricityPriceEquipmentParameter = new ElectricityPriceEquipment();
        electricityPriceEquipmentParameter.setSystemCode(electricityPriceVersionBO.getSystemCode());
        electricityPriceEquipmentParameter.setEquipmentId(electricityPriceVersionBO.getElectricityPriceEquipmentBO().getEquipmentId());
        List<ElectricityPriceEquipment> electricityPriceEquipmentList = electricityPriceEquipmentService.selectElectricityPriceEquipment(electricityPriceEquipmentParameter);

        List<ElectricityPriceVersionBO> electricityPriceVersionList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(electricityPriceEquipmentList)) {
            List<String> versionIdList = new ArrayList<>();
            for (ElectricityPriceEquipment oldElectricityPriceEquipment : electricityPriceEquipmentList) {
                if (!oldElectricityPriceEquipment.getVersionId().equals(electricityPriceVersionBO.getVersionId()))
                    versionIdList.add(oldElectricityPriceEquipment.getVersionId());
            }

            electricityPriceVersionList.addAll(PriceCollectionUtils.convertEntity(electricityPriceVersionService.selectByVersionIds(versionIdList), ElectricityPriceVersionBO.class));
        }

        electricityPriceVersionList.add(electricityPriceVersionBO);
        Collections.sort(electricityPriceVersionList);

        for (int i = 0; i < electricityPriceVersionList.size(); i++) {

            ElectricityPriceVersionBO priceVersionBO = electricityPriceVersionList.get(i);
            if (i == electricityPriceVersionList.size() - 1) {
                if (priceVersionBO.getEndDate() == null) {
                    electricityPriceVersionBO.setEndDate(PriceDateUtils.strToDayDate("2099-12-31"));
                } else {
                    if (priceVersionBO.getEndDate().compareTo(PriceDateUtils.strToDayDate("2099-12-31")) != 0) {
                        priceVersionBO.setEndDate(PriceDateUtils.strToDayDate("2099-12-31"));
                        priceVersionBO.setUpdateTime(new Date());
                        updateElectricityPriceVersionList.add(priceVersionBO);
                    }
                }
                break;
            }
            ElectricityPriceVersionBO nextPriceVersionBO = electricityPriceVersionList.get(i + 1);
            if (priceVersionBO.getEndDate() == null) {
                electricityPriceVersionBO.setEndDate(PriceDateUtils.addDateByday(nextPriceVersionBO.getStartDate(), -1));
            } else if (priceVersionBO.getEndDate().compareTo(PriceDateUtils.addDateByday(nextPriceVersionBO.getStartDate(), -1)) != 0) {
                priceVersionBO.setEndDate(PriceDateUtils.addDateByday(nextPriceVersionBO.getStartDate(), -1));
                priceVersionBO.setUpdateTime(new Date());
                updateElectricityPriceVersionList.add(priceVersionBO);
            }
        }
        return updateElectricityPriceVersionList;
    }


    /**
     * 准备添加的价格版本
     *
     * @param electricityPriceVersionBO
     */
    public ElectricityPriceVersion directlyAddElectricityPrice(ElectricityPriceVersionBO electricityPriceVersionBO, Map<String, String> addPriceVersionCache, List<ElectricityPriceRule> addPriceRuleList, List<ElectricityPriceSeason> addPriceSeasonList, List<ElectricityPriceDetail> addPriceDetailList, ElectricityPriceEquipment electricityPriceEquipment) {

        ElectricityPriceVersion electricityPriceVersion = BeanUtil.toBean(electricityPriceVersionBO, ElectricityPriceVersion.class);
        electricityPriceVersion.setVersionId(String.valueOf(SnowFlake.getInstance().nextId()));
        electricityPriceVersion.setCreateTime(new Date());
        electricityPriceVersion.setUpdateTime(new Date());
        electricityPriceVersion.setState(0);

        BeanUtil.copyProperties(electricityPriceVersionBO.getElectricityPriceEquipmentBO(), electricityPriceEquipment);
        electricityPriceEquipment.setVersionId(electricityPriceVersion.getVersionId());
        electricityPriceEquipment.setSystemCode(electricityPriceVersionBO.getSystemCode());
        electricityPriceEquipment.setCreateTime(new Date());
        electricityPriceEquipment.setUpdateTime(new Date());
        electricityPriceEquipment.setState(0);
        electricityPriceVersion.setEquipmentId(electricityPriceEquipment.getEquipmentId());
        electricityPriceVersion.setEquipmentName(electricityPriceEquipment.getEquipmentName());


        for (ElectricityPriceRuleBO electricityPriceRuleBO : electricityPriceVersionBO.getElectricityPriceRuleBOList()) {

            ElectricityPriceRule electricityPriceRule = BeanUtil.toBean(electricityPriceRuleBO, ElectricityPriceRule.class);
            electricityPriceRule.setRuleId(String.valueOf(SnowFlake.getInstance().nextId()));
            electricityPriceRule.setVersionId(electricityPriceVersion.getVersionId());
            electricityPriceRule.setCreateTime(new Date());
            electricityPriceRule.setUpdateTime(new Date());
            electricityPriceRule.setState(0);
            addPriceRuleList.add(electricityPriceRule);
            electricityPriceEquipment.setRuleId(electricityPriceRule.getRuleId());

            for (ElectricityPriceSeasonBO electricityPriceSeasonBO : electricityPriceRuleBO.getElectricityPriceSeasonBOList()) {

                ElectricityPriceSeason electricityPriceSeason = BeanUtil.toBean(electricityPriceSeasonBO, ElectricityPriceSeason.class);
                electricityPriceSeason.setVersionId(electricityPriceVersion.getVersionId());
                electricityPriceSeason.setRuleId(electricityPriceRule.getRuleId());
                electricityPriceSeason.setSeasonId(String.valueOf(SnowFlake.getInstance().nextId()));
                electricityPriceSeason.setCreateTime(new Date());
                electricityPriceSeason.setUpdateTime(new Date());
                electricityPriceSeason.setState(0);
                addPriceSeasonList.add(electricityPriceSeason);

                String versionSeasonField = PriceDateUtils.dayDateToStr(electricityPriceVersion.getStartDate()) + CommonConstant.VALUE_SPERATOR + PriceDateUtils.dayDateToStr(electricityPriceVersion.getEndDate())
                        + CommonConstant.VALUE_SPERATOR + electricityPriceVersion.getVersionId() + CommonConstant.VALUE_SPERATOR + electricityPriceSeason.getSeaStartDate() + CommonConstant.VALUE_SPERATOR + electricityPriceSeason.getSeaEndDate() + CommonConstant.VALUE_SPERATOR + electricityPriceSeason.getPricingMethod();

                List<ElectricityPriceDetailCache> electricityPriceDetailCacheList = new ArrayList<>();
                List<ElectricityPriceDetail> electricityPriceDetailList = PriceCollectionUtils.convertEntity(electricityPriceSeasonBO.getElectricityPriceDetailBOList(), ElectricityPriceDetail.class);

                for (ElectricityPriceDetail electricityPriceDetail : electricityPriceDetailList) {
                    electricityPriceDetail.setVersionId(electricityPriceVersion.getVersionId());
                    electricityPriceDetail.setRuleId(electricityPriceRule.getRuleId());
                    electricityPriceDetail.setDetailId(String.valueOf(SnowFlake.getInstance().nextId()));
                    electricityPriceDetail.setSeasonId(electricityPriceSeason.getSeasonId());
                    electricityPriceDetail.setCreateTime(new Date());
                    electricityPriceDetail.setUpdateTime(new Date());
                    electricityPriceDetail.setState(0);
                    electricityPriceDetailCacheList.add(BeanUtil.copyProperties(electricityPriceDetail, ElectricityPriceDetailCache.class));

                }
                addPriceVersionCache.put(versionSeasonField, JSON.toJSONString(electricityPriceDetailCacheList));
                addPriceDetailList.addAll(electricityPriceDetailList);
            }

        }
        return electricityPriceVersion;
    }


    /**
     * 更新价格版本
     *
     * @param electricityPriceVersionBO
     */
    public void updateElectricityPrice(ElectricityPriceVersionBO electricityPriceVersionBO) {

        ElectricityPriceVersion electricityPriceVersion = electricityPriceVersionService.selectByVersionId(electricityPriceVersionBO.getVersionId());

        if (null == electricityPriceVersion) {
            log.error("修改的价格版本不存在,版本id:{}", electricityPriceVersionBO.getVersionId());
            throw new PriceException(ErrorCodeEnum.NON_EXISTENT_DATA_EXCEPTION.getErrorCode(), "当前电价版本不存在");
        }


        addElectricityPrice(electricityPriceVersionBO, electricityPriceVersion);
    }


}
