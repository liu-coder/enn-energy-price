package com.enn.energy.price.dal.mapper.ext.proxyprice;

import com.enn.energy.price.dal.po.mbg.ElectricitySeasonSection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ElectricityPriceSeasonSectionCustomMapper {
    /**
     * 根据季节区间id查询对应的季节分时信息列表
     * @return
     */
    List<ElectricitySeasonSection> querySeasonSectionListBySeasonSectionId(@Param( "seasonSectionId" ) String seasonSectionId);

    /**
     * 根据体系规则ids 查询季节分时区间
     * @param structureRuleIds
     * @return
     */
    List<ElectricitySeasonSection> querySeasonSectionIdsByStructureRuleIds(@Param( "structureRuleIds" ) String structureRuleIds);


    /**
     * 批量删除季节区间根据季节区间id
     * @param seansonIds
     */
    void batchDeleteSeasonSectionByIds(@Param( "seansonIds" ) String seansonIds);
}
