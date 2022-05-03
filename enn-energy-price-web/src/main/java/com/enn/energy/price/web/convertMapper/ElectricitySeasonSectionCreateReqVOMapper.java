package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricitySeasonSectionCreateBO;
import com.enn.energy.price.web.vo.requestvo.ElectricitySeasonSectionCreateReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * 季节区间请求VO转BO
 *
 * @author sunjidong
 * @date 2022/5/1
 **/
@Mapper
public interface ElectricitySeasonSectionCreateReqVOMapper {

    ElectricitySeasonSectionCreateReqVOMapper INSTANCE = Mappers.getMapper(ElectricitySeasonSectionCreateReqVOMapper.class);

    ElectricitySeasonSectionCreateBO seasonSectionCreateReqVOToBO(ElectricitySeasonSectionCreateReqVO seasonSectionCreateReqVO);

}