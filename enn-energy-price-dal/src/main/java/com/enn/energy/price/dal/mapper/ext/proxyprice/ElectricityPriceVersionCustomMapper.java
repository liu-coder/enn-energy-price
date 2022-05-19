package com.enn.energy.price.dal.mapper.ext.proxyprice;

import com.enn.energy.price.dal.po.ext.ElectricityPriceVersionDto;
import com.enn.energy.price.dal.po.mbg.ElectricityPriceStructure;
import com.enn.energy.price.dal.po.mbg.ElectricityPriceVersion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface ElectricityPriceVersionCustomMapper {
    /**
     * 更新电价版本信息根据map
     *
     * @param map
     * @return
     */
    int updateElectricityPriceVersionCondition(Map<String, Object> map);

    /**
     * @param
     * @return
     * @describtion
     * @author sunjidong
     * @date 2022/5/3 16:13
     */
    long countByPriceVersionExample(ElectricityPriceVersion electricityPriceVersion);


    /**
     * 查询当前版本的前一个版本
     *
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
     *
     * @param map
     * @return
     */
    List<ElectricityPriceVersion> queryPriceVersionList(Map<String, Object> map);

    /**
     * 根据条件获取版本列表
     *
     * @param map
     * @return
     */
    List<ElectricityPriceVersion> queryPriceVersionByCondition(Map<String, Object> map);

    /**
     * 根据条件查询价格体系列表
     *
     * @param priceVersion
     * @return
     */
    List<ElectricityPriceStructure> queryStructuresByCondition(ElectricityPriceVersion priceVersion);

    /**
     * 查询当前版本的前一个版本
     *
     * @param electricityPriceVersion
     * @return
     */
    ElectricityPriceVersion queryNextPriceVersion(ElectricityPriceVersion electricityPriceVersion);

    /**
     * 批量获取下个版本
     *
     * @param provinceCodes
     * @return
     */
    List<ElectricityPriceVersionDto> queryNextPriceVersions(@Param( "provinceCodes" ) List<String> provinceCodes, @Param( "nextDay" ) Date nextDay);

    /**
     * 根据版本id查版本信息
     *
     * @param versionId
     * @return
     */
    ElectricityPriceVersion selectElectricityPriceVersionByVersionId(String versionId);

    /**
     * 根据当前版本信息获取下一版本的信息
     *
     * @param electricityPriceVersion
     * @return
     */
    ElectricityPriceVersion selectNextElectricityPriceVersion(ElectricityPriceVersion electricityPriceVersion);

}
