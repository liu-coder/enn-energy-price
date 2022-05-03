package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityTimeSectionCreateBO;
import com.enn.energy.price.web.vo.requestvo.ElectricityTimeSectionCreateReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 分时区间请求VO转BO
 *
 * @author sunjidong
 * @date 2022/5/1
 **/
@Mapper
public interface ElectricityTimeSectionCreateReqVOMapper {

    ElectricityTimeSectionCreateReqVOMapper INSTANCE = Mappers.getMapper(ElectricityTimeSectionCreateReqVOMapper.class);

    ElectricityTimeSectionCreateBO timeSectionCreateReqVOToBO(ElectricityTimeSectionCreateReqVO timeSectionCreateReqVO);

}
