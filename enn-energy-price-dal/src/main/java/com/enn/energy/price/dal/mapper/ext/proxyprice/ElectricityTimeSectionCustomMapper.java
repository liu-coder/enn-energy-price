package com.enn.energy.price.dal.mapper.ext.proxyprice;

import com.enn.energy.price.dal.po.mbg.ElectricityTimeSection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author Xinao
 */
@Mapper
public interface ElectricityTimeSectionCustomMapper {
    /**
     * 批量删除分时信息根据季节区间id(uuid)
     */
    void batchDeleteTimeSectionBySeasonSectionIds(Map<String,Object> map);


    /**
     * 根据seasonid查询分时区间信息
     * @param seasonSectionId
     * @return
     */
    List<ElectricityTimeSection> getTimeSectionListBySeasonSectionId(@Param( "seasonSectionId" ) String seasonSectionId);


    /**
     * 根据timeId删除分时数据
     * @param map
     * @return
     */
    int batchDeleteTimeSectionByIds(Map<String,Object> map);


    /**
     * 批量新增
     * @param electricityTimeSectionList
     * @return
     */
    int batchInsertTimeSection(List<ElectricityTimeSection> electricityTimeSectionList);


    /**
     * 批量更新
     * @param electricityTimeSectionList
     * @return
     */
    int batchUpdateTimeSection(List<ElectricityTimeSection> electricityTimeSectionList);
}
