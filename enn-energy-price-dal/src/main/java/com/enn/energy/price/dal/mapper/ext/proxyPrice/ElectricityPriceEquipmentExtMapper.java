package com.enn.energy.price.dal.mapper.ext.proxyPrice;

import com.enn.energy.price.dal.po.mbg.ElectricityPriceEquipment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/2 16:05
 */
@Mapper
public interface ElectricityPriceEquipmentExtMapper {
    /**
     * 查询版本下的设备绑定关系
     * @param versionId
     * @return
     */
    List<ElectricityPriceEquipment> queryEquipmentBinding(@Param( "versionId" ) String versionId);
}
