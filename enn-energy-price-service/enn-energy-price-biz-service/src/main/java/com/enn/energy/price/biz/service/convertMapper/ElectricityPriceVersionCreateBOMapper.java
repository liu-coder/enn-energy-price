package com.enn.energy.price.biz.service.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceVersionCreateBO;
import com.enn.energy.price.dal.po.mbg.ElectricityPriceVersion;
import org.apache.ibatis.annotations.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 版本的创建(仅版本部分)请求VO转换器
 *
 * @author sunjidong
 * @date 2022/5/1
 **/
@Mapper
public interface ElectricityPriceVersionCreateBOMapper {

    ElectricityPriceVersionCreateBOMapper INSTANCE = Mappers.getMapper(ElectricityPriceVersionCreateBOMapper.class);

    ElectricityPriceVersion priceVersionCreateBOToPO(ElectricityPriceVersionCreateBO priceVersionStructuresBO);

}
