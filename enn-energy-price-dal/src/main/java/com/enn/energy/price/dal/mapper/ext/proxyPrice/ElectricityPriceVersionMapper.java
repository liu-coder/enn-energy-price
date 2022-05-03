package com.enn.energy.price.dal.mapper.ext.proxyPrice;

import com.enn.energy.price.dal.po.mbg.ElectricityPriceVersion;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ElectricityPriceVersionMapper {
    /**
     * 更新电价版本信息根据id
     * @param electricityPriceVersion
     * @return
     */
    int updateElectricityPriceVersionById(ElectricityPriceVersion electricityPriceVersion);

}
