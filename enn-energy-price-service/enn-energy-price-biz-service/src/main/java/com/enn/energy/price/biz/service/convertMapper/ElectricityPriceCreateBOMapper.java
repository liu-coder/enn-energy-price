package com.enn.energy.price.biz.service.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceCreateBO;
import com.enn.energy.price.dal.po.mbg.ElectricityPrice;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * xxxxx此处为类的描述信息
 *
 * @author sunjidong
 * @date 2022/5/1
 **/
@Mapper
public interface ElectricityPriceCreateBOMapper {

    ElectricityPriceCreateBOMapper INSTANCE = Mappers.getMapper(ElectricityPriceCreateBOMapper.class);

    ElectricityPrice priceReqCreateBOToPO(ElectricityPriceCreateBO priceReqCreateBO);


}
