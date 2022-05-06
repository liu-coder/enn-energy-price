package com.enn.energy.price.dal.mapper.ext.proxyprice;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Xinao
 */
@Mapper
public interface ElectricityTimeSectionExtMapper {
    /**
     * 批量删除季节分时信息根据季节区间id(uuid)
     */
    void batchDeleteTimeSectionBySeasonSectionIds(@Param( "seasonSectionIds" ) String seasonSectionIds);
}
