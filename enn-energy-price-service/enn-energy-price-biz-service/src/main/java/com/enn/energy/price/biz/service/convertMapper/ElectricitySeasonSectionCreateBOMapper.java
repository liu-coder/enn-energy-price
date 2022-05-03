package com.enn.energy.price.biz.service.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricitySeasonSectionCreateBO;
import com.enn.energy.price.dal.po.mbg.ElectricitySeasonSection;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 季节区间请求VO转BO
 *
 * @author sunjidong
 * @date 2022/5/1
 **/
@Mapper
public interface ElectricitySeasonSectionCreateBOMapper {

    ElectricitySeasonSectionCreateBOMapper INSTANCE = Mappers.getMapper(ElectricitySeasonSectionCreateBOMapper.class);

    ElectricitySeasonSection seasonSectionCreateBOToPO(ElectricitySeasonSectionCreateBO seasonSectionCreateBO);

}