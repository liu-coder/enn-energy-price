package com.enn.energy.price.biz.service.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.*;
import com.enn.energy.price.dal.po.mbg.*;
import com.enn.energy.price.dal.po.view.ElectricityPriceEquipmentView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * xxxxx此处为类的描述信息
 *
 * @author sunjidong
 * @date 2022/5/13
 **/
@Mapper
public interface CommonBOPOConvertMapper {

    @Mappings({
            @Mapping(source = "startDate", target = "startDate", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "endDate", target = "endDate", dateFormat = "yyyy-MM-dd")
    })
    ElectricityPriceVersion versionUpdateBOToPO(ElectricityPriceVersionUpdateBO versionUpdateBO);

    CommonBOPOConvertMapper INSTANCE = Mappers.getMapper(CommonBOPOConvertMapper.class);

    ElectricityPriceStructure priceStructureBOToPO(ElectricityPriceStructureUpdateBO priceStructureBO);

    ElectricityPriceStructureRule  structureRuleBOToPO(ElectricityPriceStructureRuleUpdateBO structureRuleBO);

    ElectricitySeasonSection seasonDateBOToPO(SeasonDateBO seasonDateBO);

    ElectricityTimeSection seasonStrategyTimeSectionBOToPO(ElectricityTimeSectionUpdateBO seasonStrategyTimeSectionBO);

    ElectricityPrice priceRuleAndDetailBOToPO(ElectricityPriceUpdateBO priceRuleAndDetailBO);

    ElectricityPriceEquipment priceVersionViewToPo(ElectricityPriceEquipmentView priceEquipmentView);

    ElectricityPriceRule priceRuleAndDetailBOToPo(ElectricityPriceUpdateBO priceRuleAndDetailBO);

    ElectricityPriceStructureDetailBO electricityPriceStructurePOTOBO(ElectricityPriceStructure electricityPriceStructurePO);

}
