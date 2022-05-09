package com.enn.energy.price.dal.mapper.ext.proxyprice;

import com.enn.energy.price.dal.po.mbg.ElectricityPriceVersion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

@Mapper
public interface ElectricityPriceVersionCustomMapper {
    /**
     * 更新电价版本信息根据map
     * @param map
     * @return
     */
    int updateElectricityPriceVersionCondition(Map<String,Object> map);

    /**
     * @describtion
     * @author sunjidong
     * @date 2022/5/3 16:13
     * @param
     * @return
     */
    long countByPriceVersionExample(ElectricityPriceVersion electricityPriceVersion);


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
    //List<ElectricityPriceVersion> queryPriceVersionList(@Param( "provinceCode" ) String provinceCode);

    /**
     * 根据条件查询价格版本列表
     * @param map
     * @return
     */
    List<ElectricityPriceVersion> queryPriceVersionList(Map<String,Object> map);

}
