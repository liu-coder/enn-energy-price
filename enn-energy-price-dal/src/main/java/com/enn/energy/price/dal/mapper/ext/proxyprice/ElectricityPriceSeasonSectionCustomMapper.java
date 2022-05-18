package com.enn.energy.price.dal.mapper.ext.proxyprice;

import com.enn.energy.price.dal.po.mbg.ElectricitySeasonSection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ElectricityPriceSeasonSectionCustomMapper {
    /**
     * 根据季节区间id查询对应的季节分时信息列表
     * @return
     */
    List<ElectricitySeasonSection> querySeasonSectionListBySeasonSectionId(@Param( "seasonSectionId" ) String seasonSectionId);

    /**
     * 根据体系规则ids 查询季节分时区间
     * @param map
     * @return
     */
    List<ElectricitySeasonSection> querySeasonSectionIdsByStructureRuleIds(Map<String,Object> map);


    /**
     * 批量删除季节区间根据季节区间id
     * @param map
     */
    int batchDeleteSeasonSectionBySectionIds(Map<String,Object> map);


    /**
     * 批量删除季节根据季节id
     * @param map
     * @return
     */
    int batchDeleteSeasonSectionByIds(Map<String,Object> map);


    /**
     * 批量更新season
     * @param updateDateList
     * @return
     */
    int batchUpdateSeason(List updateDateList);

    /**
     * 根据体系规则ids 查询季节分时区间
     * @param structureRuleId
     * @return
     */
    List<ElectricitySeasonSection> querySeasonSectionListByStructureRuleId(@Param( "structureRuleId" ) String structureRuleId);



}
