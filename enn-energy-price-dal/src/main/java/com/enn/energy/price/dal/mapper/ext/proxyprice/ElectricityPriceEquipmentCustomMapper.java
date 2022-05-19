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
     * 批量更新
     * @param map
     * @return
     */
    int batchDeleteEquipmentBindingByConditions(Map<String,Object> map);

    /**
     * 查询当前设备绑定关系是否已经存在
     *
     * @param electricityPriceEquipment
     * @return
     */
    ElectricityPriceEquipment queryIsExistEquipmentBind(ElectricityPriceEquipment electricityPriceEquipment);

    /**
     * 根据ruieid查询设备绑定关系
     * @param ruleId
     * @return
     */
    long countRuleEquipmentBindRecordsById(@Param( "ruleId" ) String ruleId);

    /**
     * 根据nodeIds获取设备绑定列表
     *
     * @param nodeIds
     * @return
     */
    List<ElectricityPriceEquipment> getElectricityPriceEquipmentByNodeIds(List<Long> nodeIds);

    /**
     * 根据规则ids查找当前规则不存在的绑定关系
     *
     * @param ruleIds
     * @return
     */
    List<ElectricityPriceEquipment> getElectricityPriceEquipmentNotExistRule(List<String> ruleIds);

    /**
     * 根据Map查询价格设备绑定关系
     *
     * @param map
     * @return
     */
    List<ElectricityPriceEquipment> queryElectricityPriceEquipmentByMap(Map<String,Object> map);

    /**
     * 查询到day时间过期的并且有设置默认继承的版本
     *
     * @param day
     * @return
     */
    List<ElectricityPriceEquipmentVersionDto> queryExpireEquipmentVersion(Date day);

    /**
     * 获取下一个版本和设备的绑定关系
     *
     * @param list
     * @return
     */
    List<ElectricityPriceNextVersionDto> queryBindNextVersionPriceEquipment(List<ElectricityPriceNextVersionDto> list);

    /**
     * 获取版本对应的体系价格
     * @param versionIds
     * @return
     */
    List<ElectricityPriceVersionRuleDto> queryVersionStructAndRule(List<String> versionIds);

    void batchAddElectricityPriceEquipment(List<ElectricityPriceEquipment> list);
}
