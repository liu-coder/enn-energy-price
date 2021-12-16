package com.enn.energy.price.dal.mapper.ext;

import com.enn.energy.price.dal.po.mbg.ElectricityPriceEquipment;
import com.enn.energy.price.dal.po.view.ElectricityPriceEquVersionView;
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

    List<ElectricityPriceEquipment> selectElectricityPriceEquipment(ElectricityPriceEquipment electricityPriceEquipment);

    void batchAddElectricityPriceEquipment(List<ElectricityPriceEquipment> electricityPriceEquipmentList);

    void addElectricityPriceEquipment(ElectricityPriceEquipment electricityPriceEquipment);


    List<ElectricityPriceEquipment> selectEquByCondition(Map<String,Object> map);

    void updatePriceEquipmentState(String versionId);

    void updatePriceEquipment(ElectricityPriceEquipment electricityPriceEquipment);

    List<ElectricityPriceEquipment> selectEquByConditionOrderByUpdateTime(Map<String, Object> map);

    ElectricityPriceEquVersionView selectEquVersionRecentOneValidByCondition(Map<String,Object> map);

    ElectricityPriceEquVersionView selectEquVersionLastOneValidByTime(Map<String, Object> map);
}
