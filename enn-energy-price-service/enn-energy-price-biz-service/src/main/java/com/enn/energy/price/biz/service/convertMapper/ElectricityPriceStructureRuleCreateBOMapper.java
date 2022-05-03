package com.enn.energy.price.biz.service.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceStructureRuleCreateBO;
import com.enn.energy.price.dal.po.mbg.ElectricityPriceStructureRule;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * VO转BO转换器类
 *
 * @author sunjidong
 * @date 2022/5/1
 **/
@Mapper
public interface ElectricityPriceStructureRuleCreateBOMapper {

    ElectricityPriceStructureRuleCreateBOMapper INSTANCE = Mappers.getMapper(ElectricityPriceStructureRuleCreateBOMapper.class);

    ElectricityPriceStructureRule priceStructureRuleCreateBOToPO(ElectricityPriceStructureRuleCreateBO priceStructureRuleCreateBO);

}
