package com.enn.energy.price.dal.mapper.ext;

import com.enn.energy.price.dal.po.mbg.ElectricityPriceEquipment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 设备电价Mapper.
 *
 * @author : wuchaon
 * @version : 1.0 2021/11/27 10:58
 * @since : 1.0
 **/
@Mapper
public interface ElectricityPriceEquipmentExtMapper {

    List<ElectricityPriceEquipment> selectByEquipmentId(String equipmentId);

    void batchAddElectricityPriceEquipment(List<ElectricityPriceEquipment> electricityPriceEquipmentList);

    void addElectricityPriceEquipment(ElectricityPriceEquipment electricityPriceEquipment);

    List<ElectricityPriceEquipment> selectPriceEquipmentsByVersionId(String versionId);

    List<ElectricityPriceEquipment> selectEquByCondition(Map<String,Object> map);

    void updatePriceEquipmentState(String versionId);

    List<ElectricityPriceEquipment> selectEquByConditionOrderByUpdateTime(Map<String, Object> map);
}
