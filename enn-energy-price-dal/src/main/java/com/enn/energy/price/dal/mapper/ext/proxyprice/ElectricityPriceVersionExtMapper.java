package com.enn.energy.price.dal.mapper.ext.proxyprice;

import com.enn.energy.price.dal.po.mbg.ElectricityPriceVersion;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ElectricityPriceVersionExtMapper {
    /**
     * 更新电价版本信息根据id
     * @param electricityPriceVersion
     * @return
     */
    int updateElectricityPriceVersionById(ElectricityPriceVersion electricityPriceVersion);


    /**
     * 查询当前版本的前一个版本
     * @param electricityPriceVersion
     * @return
     */
    ElectricityPriceVersion queryBeforePriceVersion(ElectricityPriceVersion electricityPriceVersion);
}
