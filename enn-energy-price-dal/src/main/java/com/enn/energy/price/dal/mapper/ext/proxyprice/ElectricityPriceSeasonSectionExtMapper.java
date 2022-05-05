package com.enn.energy.price.dal.mapper.ext.proxyprice;

import com.enn.energy.price.dal.po.mbg.ElectricitySeasonSection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ElectricityPriceSeasonSectionExtMapper {
    /**
     * 根据季节区间id查询对应的季节分时信息列表
     * @return
     */
    List<ElectricitySeasonSection> selectSeasonSectionListBySeasonSectionId(@Param( "seasonSectionId" ) String seasonSectionId);
}
