package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceRuleCreateBO;
import com.enn.energy.price.web.vo.requestvo.ElectricityPriceRuleCreateReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * xxxxx此处为类的描述信息
 *
 * @author sunjidong
 * @date 2022/5/1
 **/
@Mapper(uses = {ElectricityPriceCreateReqVOMapper.class})
public interface ElectricityPriceRuleCreateReqVOMapper {

    ElectricityPriceRuleCreateReqVOMapper INSTANCE = Mappers.getMapper(ElectricityPriceRuleCreateReqVOMapper.class);

    @Mapping(source = "priceRuleCreateVO.electricityPriceReqVO", target = "electricityPriceReqVO")
    ElectricityPriceRuleCreateBO priceRuleCreateVOToBO(ElectricityPriceRuleCreateReqVO priceRuleCreateReqVO);


}
