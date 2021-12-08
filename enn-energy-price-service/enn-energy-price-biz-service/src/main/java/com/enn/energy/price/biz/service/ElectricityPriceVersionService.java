package com.enn.energy.price.biz.service;

import cn.hutool.core.collection.CollectionUtil;
import com.enn.energy.price.biz.service.bo.ElectricityPriceVersionBO;
import com.enn.energy.price.common.utils.BeanUtil;
import com.enn.energy.price.common.utils.PriceDateUtils;
import com.enn.energy.price.dal.mapper.ext.ElectricityPriceVersionExtMapper;
import com.enn.energy.price.dal.po.mbg.ElectricityPriceVersion;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.rdfa.framework.exception.RdfaException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 电价版本service.
 *
 * @author : wuchaon
 * @version : 1.0 2021/11/29 10:11
 * @since : 1.0
 **/
@Service
public class ElectricityPriceVersionService {

    @Autowired
    private ElectricityPriceVersionExtMapper electricityPriceVersionExtMapper;

    /**
     * 根据版本id查询价格版本
     *
     * @param versionId
     * @return
     */
    public ElectricityPriceVersion selectByVersionId(String versionId) {
        if (StringUtils.isEmpty(versionId)) {
            return null;
        }
        return electricityPriceVersionExtMapper.selectByVersionId(versionId);
    }

    /**
     * 添加价格版本
     *
     * @param electricityPriceVersion
     */
    public void addElectricityPriceVersion(ElectricityPriceVersion electricityPriceVersion) {

        electricityPriceVersionExtMapper.addElectricityPriceVersion(electricityPriceVersion);
    }

    /**
     * 批量添加价格版本
     *
     * @param electricityPriceVersionList
     */
    public void batchAddElectricityPriceVersion(List<ElectricityPriceVersion> electricityPriceVersionList) {

        electricityPriceVersionExtMapper.batchAddElectricityPriceVersion(electricityPriceVersionList);
    }

    /**
     * 更新价格版本
     *
     * @param electricityPriceVersion
     */
    public void updatePriceVersion(ElectricityPriceVersion electricityPriceVersion) {
        electricityPriceVersionExtMapper.updatePriceVersion(electricityPriceVersion);
    }

    /**
     * 批量更新价格版本
     *
     * @param electricityPriceVersionList
     */
    public void batchUpdatePriceVersion(List<ElectricityPriceVersion> electricityPriceVersionList) {
        electricityPriceVersionExtMapper.batchUpdatePriceVersion(electricityPriceVersionList);
    }

    /**
     * 更新价格版本状态
     *
     * @param versionId
     */
    public void updatePriceVersionState(String versionId) {
        electricityPriceVersionExtMapper.updatePriceVersionState(versionId);
    }

    /**
     * 批量根据版本id查询价格版本
     *
     * @param versionIdList
     * @return
     */
    public List<ElectricityPriceVersion> selectByVersionIds(List<String> versionIdList) {
        if (CollectionUtil.isEmpty(versionIdList)) {
            return null;
        }
        return electricityPriceVersionExtMapper.selectByVersionIds(versionIdList);
    }

    /**
     * 查询满足条件的版本信息
     *
     * @param versionIds
     * @param activeTime
     * @return
     * @throws RdfaException
     */
    public ElectricityPriceVersionBO selectVersionByCondition(List<String> versionIds, Date activeTime) throws RdfaException {
        Map<String, Object> map = new HashMap<>();
        map.put("versionIds", versionIds);
        map.put("state", 0);
        List<ElectricityPriceVersion> electricityPriceVersions = electricityPriceVersionExtMapper.selectVersionByCondition(map);
        List<ElectricityPriceVersionBO> electricityPriceVersionBos = BeanUtil.mapList(electricityPriceVersions, ElectricityPriceVersionBO.class);
        List<ElectricityPriceVersionBO> versionBos = electricityPriceVersionBos.stream().
                filter(item -> PriceDateUtils.beforeOrEqual(item.getStartDate(), activeTime) && PriceDateUtils.afterOrEqual(item.getEndDate(), activeTime)).
                collect(Collectors.toList());
        if (versionBos.size() > 1 || versionBos.size() == 0) {
            throw new RdfaException("版本数据存在异常，请排查版本的有效时间");
        }
        return versionBos.get(0);
    }

    public List<ElectricityPriceVersionBO> selectVersionByVersionIdList(List<String> versionIds) {
        Map<String, Object> map = new HashMap<>();
        map.put("versionIds", versionIds);
        map.put("state", 0);
        List<ElectricityPriceVersion> electricityPriceVersions = electricityPriceVersionExtMapper.selectVersionByCondition(map);
        List<ElectricityPriceVersionBO> versionBos = BeanUtil.mapList(electricityPriceVersions, ElectricityPriceVersionBO.class);
        return versionBos;
    }

    public ElectricityPriceVersionBO selectVersionByVersionId(String versionId) throws RdfaException {
        Map<String, Object> map = new HashMap<>();
        map.put("versionId", versionId);
        map.put("state", 0);
        List<ElectricityPriceVersion> priceVersions = electricityPriceVersionExtMapper.selectVersionByCondition(map);
        if (priceVersions.size() == 0) {
            return null;
        }
        if (priceVersions.size() > 1) {
            throw new RdfaException("版本数据存在异常，请排查版本数据");
        }
        List<ElectricityPriceVersionBO> versionBos = BeanUtil.mapList(priceVersions, ElectricityPriceVersionBO.class);
        return versionBos.get(0);
    }

    public List<ElectricityPriceVersionBO> selectEquVersionsByCondition(String equipmentId,String cimCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("equipmentId", equipmentId);
        map.put("systemCode", cimCode);
        map.put("state",0);
        List<ElectricityPriceVersion> electricityPriceVersions = electricityPriceVersionExtMapper.selectEquVersionsByCondition(map);
        List<ElectricityPriceVersionBO> versionBos = BeanUtil.mapList(electricityPriceVersions, ElectricityPriceVersionBO.class);
        return versionBos;
    }

}
