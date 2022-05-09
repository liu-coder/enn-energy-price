package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceStructureRuleDetailBO;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceStructureRuleDetailRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ElectricityPriceStructureRuleConverMapper {
    ElectricityPriceStructureRuleConverMapper INSTANCE = Mappers.getMapper(ElectricityPriceStructureRuleConverMapper.class);

    ElectricityPriceStructureRuleDetailRespVO ElectricityPriceStructureRuleDetailBOToVO(ElectricityPriceStructureRuleDetailBO electricityPriceStructureRuleDetailBO);
}
