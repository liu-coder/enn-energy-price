package com.enn.energy.price.dal.mapper.ext.proxyprice;

import com.enn.energy.price.dal.po.mbg.ElectricityTimeSection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Xinao
 */
@Mapper
public interface ElectricityTimeSectionCustomMapper {
    /**
     * 批量删除季节分时信息根据季节区间id(uuid)
     */
    void batchDeleteTimeSectionBySeasonSectionIds(@Param( "seasonSectionIds" ) String seasonSectionIds);


    /**
     * 根据seasonid查询分时区间信息
     * @param seasonSectionId
     * @return
     */
    List<ElectricityTimeSection> getTimeSectionListBySeasonSectionId(@Param( "seasonSectionId" ) String seasonSectionId);
}
