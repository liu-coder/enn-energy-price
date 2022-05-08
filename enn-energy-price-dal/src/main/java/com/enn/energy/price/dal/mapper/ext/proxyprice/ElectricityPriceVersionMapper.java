package com.enn.energy.price.dal.mapper.ext.proxyprice;

import com.enn.energy.price.dal.po.mbg.ElectricityPriceVersion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ElectricityPriceVersionMapper {
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


    /**
     * 根据省份查询价格版本列表
     * @param provinceCode
     * @return
     */
    List<ElectricityPriceVersion> queryPriceVersionList(@Param( "provinceCode" ) String provinceCode);
}
