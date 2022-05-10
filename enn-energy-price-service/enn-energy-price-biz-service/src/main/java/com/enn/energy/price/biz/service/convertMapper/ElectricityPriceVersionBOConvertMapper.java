package com.enn.energy.price.biz.service.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.*;
import com.enn.energy.price.dal.po.mbg.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * BO层通用转换器
 *
 * @author sunjidong
 * @date 2022/5/3
 **/
@Mapper
public interface ElectricityPriceVersionBOConvertMapper {
    ElectricityPriceVersionBOConvertMapper INSTANCE = Mappers.getMapper(ElectricityPriceVersionBOConvertMapper.class);

    ElectricityPrice priceCreateBOToPO(ElectricityPriceCreateBO priceReqCreateBO);

    ElectricityPriceRule priceRuleCreateBOToPO(ElectricityPriceRuleCreateBO priceRuleCreateBO);

    ElectricityPriceStructure priceStructureCreateBOToPO(ElectricityPriceStructureCreateBO priceStructureCreateBO);

    ElectricityPriceStructureRule priceStructureRuleCreateBOToPO(ElectricityPriceStructureRuleCreateBO priceStructureRuleCreateBO);

    ElectricityPriceVersion priceVersionCreateBOToPO(ElectricityPriceVersionCreateBO priceVersionStructuresBO);

    ElectricitySeasonSection seasonSectionCreateBOToPO(ElectricitySeasonSectionCreateBO seasonSectionCreateBO);

    ElectricityTimeSection timeSectionCreateBOToPO(ElectricityTimeSectionCreateBO timeSectionCreateBO);




}
