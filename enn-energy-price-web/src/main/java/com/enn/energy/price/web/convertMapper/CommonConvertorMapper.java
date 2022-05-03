package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceCreateBO;
import com.enn.energy.price.web.vo.requestvo.ElectricityPriceCreateReqVO;
import org.apache.ibatis.annotations.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * xxxxx此处为类的描述信息
 *
 * @author sunjidong
 * @date 2022/5/2
 **/
@Mapper
public interface CommonConvertorMapper {

    CommonConvertorMapper INSTANCE = Mappers.getMapper(CommonConvertorMapper.class);

    ElectricityPriceCreateBO priceReqCreateVOToBO(ElectricityPriceCreateReqVO priceReqCreateReqVO);

}