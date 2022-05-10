package com.enn.energy.price.dal.mapper.ext.proxyprice;

import com.enn.energy.price.dal.po.mbg.ElectricityPriceEquipment;
import com.enn.energy.price.dal.po.view.ElectricityPriceEquVersionView;
import com.enn.energy.price.dal.po.view.ElectricityPriceEquipmentView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/2 16:05
 */
@Mapper
public interface ElectricityPriceEquipmentCustomMapper {
    /**
     * 查询版本下的设备绑定关系
     * @param versionId
     * @return
     */
    List<ElectricityPriceEquipment> queryEquipmentBinding(@Param( "versionId" ) String versionId);

    /**
     * 查询版本下的设备绑定关系
     * @param record
     * @return List<ElectricityPriceEquipment>
     */
    List<ElectricityPriceEquipmentView> listRuleEquipmentBindRecords(ElectricityPriceEquipment record);

    /**
     * 根据规则id集合查询设备绑定关系
     * @param ruleIdList
     * @return List<ElectricityPriceEquipment>
     */
    List<ElectricityPriceEquipment> listRuleEquipmentBindRecordsByRuleIdList(List<String> ruleIdList);

    /**
     * 查询体系下的设备绑定关系
     * @param structureId
     * @return
     */
    List<ElectricityPriceEquipment> queryEquipmentBindingByStructureId(@Param( "structureId" ) String structureId);


    /**
     * 删除设备绑定关系
     * @param versionId
     * @return
     */
    int deleteEquipmentBindingByVersionId(@Param( "versionId" ) String versionId);

    /**
     * 根据ruieid查询设备绑定关系
     * @param id
     * @return
     */
    long countRuleEquipmentBindRecordsById(@Param( "id" ) Long id);
}
