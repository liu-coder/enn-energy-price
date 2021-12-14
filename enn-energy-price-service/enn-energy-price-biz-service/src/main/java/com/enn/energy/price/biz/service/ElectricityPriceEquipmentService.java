package com.enn.energy.price.biz.service;

import cn.hutool.core.collection.CollectionUtil;
import com.enn.energy.price.biz.service.bo.ElectricityPriceEquipmentBO;
import com.enn.energy.price.common.utils.BeanUtil;
import com.enn.energy.price.dal.mapper.ext.ElectricityPriceEquipmentExtMapper;
import com.enn.energy.price.dal.po.mbg.ElectricityPriceEquipment;
import com.enn.energy.price.dal.po.view.ElectricityPriceEquVersionView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.rdfa.framework.exception.RdfaException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 设备电价service.
 *
 * @author : wuchaon
 * @version : 1.0 2021/11/27 10:56
 * @since : 1.0
 **/
@Service
@Slf4j
public class ElectricityPriceEquipmentService {

    @Autowired
    private ElectricityPriceEquipmentExtMapper electricityPriceEquipmentExtMapper;

    /**
     * 根据设备id和系统code查询设备绑定的所有价格版本
     * @param electricityPriceEquipment
     * @return
     */
    public List<ElectricityPriceEquipment> selectElectricityPriceEquipment(ElectricityPriceEquipment electricityPriceEquipment) {

        return electricityPriceEquipmentExtMapper.selectElectricityPriceEquipment(electricityPriceEquipment);
    }


    /**
     * 批量添加设备价格
     * @param electricityPriceEquipmentList
     */
    public void batchAddElectricityPriceEquipment(List<ElectricityPriceEquipment> electricityPriceEquipmentList){
        if(CollectionUtil.isEmpty(electricityPriceEquipmentList)){
            return;
        }
        electricityPriceEquipmentExtMapper.batchAddElectricityPriceEquipment(electricityPriceEquipmentList);
    }

    /**
     * 添加设备价格
     * @param electricityPriceEquipment
     */
    public void addElectricityPriceEquipment(ElectricityPriceEquipment electricityPriceEquipment){

        electricityPriceEquipmentExtMapper.addElectricityPriceEquipment(electricityPriceEquipment);
    }

    /**
     * 根据价格版本id查询价格设备
     * @param versionId
     * @return
     */
    public ElectricityPriceEquipment selectPriceEquipmentByVersionId(String versionId){

      return electricityPriceEquipmentExtMapper.selectPriceEquipmentByVersionId(versionId);
    }


    public List<ElectricityPriceEquipmentBO> selectEquByCondition(String versionId) {
        Map<String,Object> map = new HashMap<>();
        map.put("versionId",versionId);
        map.put("state",0);
        List<ElectricityPriceEquipment> electricityPriceEquipments = electricityPriceEquipmentExtMapper.selectEquByCondition(map);
        List<ElectricityPriceEquipmentBO> equipmentBos = BeanUtil.mapList(electricityPriceEquipments, ElectricityPriceEquipmentBO.class);
        return equipmentBos;
    }

    public List<ElectricityPriceEquipmentBO> selectEquByConditionOrderByUpdateTime(String equipmentId, String cimCode) {
        Map<String,Object> map = new HashMap<>();
        map.put("equipmentId",equipmentId);
        map.put("state",0);
        map.put("systemCode",cimCode);
        List<ElectricityPriceEquipment> electricityPriceEquipments = electricityPriceEquipmentExtMapper.selectEquByConditionOrderByUpdateTime(map);
        List<ElectricityPriceEquipmentBO> equipmentBos = BeanUtil.mapList(electricityPriceEquipments, ElectricityPriceEquipmentBO.class);
        return equipmentBos;
    }
    public ElectricityPriceEquipmentBO selectEquByCondition(String equipmentId, String versionId, String cimCode) throws RdfaException {
        Map<String,Object> map = new HashMap<>();
        map.put("equipmentId",equipmentId);
        map.put("state",0);
        map.put("systemCode",cimCode);
        map.put("versionId",versionId);
        List<ElectricityPriceEquipment> electricityPriceEquipments = electricityPriceEquipmentExtMapper.selectEquByCondition(map);
        if (electricityPriceEquipments.size() > 1){
            throw new RdfaException("设备数据绑定的同一版本查到多条规则数据，请排查设备数据");
        }
        if (electricityPriceEquipments.size() == 0){
            throw new RdfaException("根据设备ID、版本ID、cim编码未查到设备数据");
        }
        List<ElectricityPriceEquipmentBO> equipmentBos = BeanUtil.mapList(electricityPriceEquipments, ElectricityPriceEquipmentBO.class);
        return equipmentBos.get(0);
    }


    /**
     * 更新设备状态
     *
     * @param versionId
     */
    public void updatePriceEquipmentState(String versionId) {
        electricityPriceEquipmentExtMapper.updatePriceEquipmentState(versionId);
    }

    /**
     * 更新设备
     *
     * @param electricityPriceEquipment
     */
    public void updatePriceEquipment(ElectricityPriceEquipment electricityPriceEquipment) {
        electricityPriceEquipmentExtMapper.updatePriceEquipment(electricityPriceEquipment);
    }

    public ElectricityPriceEquVersionView selectEquVersionRecentOneValidByCondition(String equipmentId, String systemCode, Date activeTime) {
        Map<String,Object> map = new HashMap<>();
        map.put("equipmentId",equipmentId);
        map.put("state",0);
        map.put("systemCode",systemCode);
        map.put("activeTime",activeTime);
        ElectricityPriceEquVersionView equVersionView = electricityPriceEquipmentExtMapper.selectEquVersionRecentOneValidByCondition(map);
        return equVersionView;
    }


    public ElectricityPriceEquVersionView selectEquVersionLastOneValidByTime(String equipmentId, String systemCode, Date activeTime) {
        Map<String,Object> map = new HashMap<>();
        map.put("equipmentId",equipmentId);
        map.put("state",0);
        map.put("systemCode",systemCode);
        map.put("activeTime",activeTime);
        ElectricityPriceEquVersionView equVersionView = electricityPriceEquipmentExtMapper.selectEquVersionLastOneValidByTime(map);
        return equVersionView;
    }

}
