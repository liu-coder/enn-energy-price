package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricitySeasonCreateBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ElectricitySeasonSectionCreateBO;
import com.enn.energy.price.web.vo.requestvo.ElectricitySeasonCreateReqVO;
import com.enn.energy.price.web.vo.requestvo.ElectricitySeasonSectionCreateReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * VO转BO转换器类
 *
 * @author sunjidong
 * @date 2022/5/1
 **/
@Mapper(uses = {ElectricitySeasonSectionCreateReqVOMapper.class})
public interface ElectricitySeasonCreateReqVOMapper {

    ElectricitySeasonSectionCreateReqVOMapper INSTANCE = Mappers.getMapper(ElectricitySeasonSectionCreateReqVOMapper.class);

    @Mapping(source = "seasonCreateReqVO.seasonSectionCreateReqVOList", target = "seasonSectionCreateBOList")
    ElectricitySeasonCreateBO seasonCreateReqVOToBO(ElectricitySeasonCreateReqVO seasonCreateReqVO);

}
