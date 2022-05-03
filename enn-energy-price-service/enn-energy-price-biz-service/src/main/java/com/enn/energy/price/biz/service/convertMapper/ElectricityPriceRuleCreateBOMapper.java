package com.enn.energy.price.biz.service.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceRuleCreateBO;
import com.enn.energy.price.dal.po.mbg.ElectricityPriceRule;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * xxxxx此处为类的描述信息
 *
 * @author sunjidong
 * @date 2022/5/1
 **/
@Mapper
public interface ElectricityPriceRuleCreateBOMapper {

    ElectricityPriceRuleCreateBOMapper INSTANCE = Mappers.getMapper(ElectricityPriceRuleCreateBOMapper.class);

    ElectricityPriceRule priceRuleCreateBOToPO(ElectricityPriceRuleCreateBO priceRuleCreateBO);


}
