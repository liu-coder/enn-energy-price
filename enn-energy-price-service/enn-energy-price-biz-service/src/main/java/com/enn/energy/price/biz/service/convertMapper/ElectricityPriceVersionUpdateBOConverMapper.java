package com.enn.energy.price.biz.service.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceStructureRuleUpdateBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceSeasonUpdateBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceStructureUpdateBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceUpdateBO;
import com.enn.energy.price.dal.po.mbg.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ElectricityPriceVersionUpdateBOConverMapper {

    ElectricityPriceVersionUpdateBOConverMapper INSTANCE = Mappers.getMapper(ElectricityPriceVersionUpdateBOConverMapper.class);

    /**
     * 版本体系bo转po
     * @param electricityPriceStructureUpdateBO
     * @return
     */
    ElectricityPriceStructure electricityPriceStructureBOTOPO(ElectricityPriceStructureUpdateBO electricityPriceStructureUpdateBO);

    /**
     * 电价体系规则bo转po
     * @param electricityPriceStructureRuleUpdateBo
     * @return
     */
    ElectricityPriceStructureRule electricityPriceStructureRuleBOTOPO(ElectricityPriceStructureRuleUpdateBO electricityPriceStructureRuleUpdateBo);


    /**
     * 电价体系季节区间bo转po
     */

    ElectricitySeasonSection electricitySeasonSectionBOTOPO(ElectricityPriceSeasonUpdateBO electricityPriceSeasonUpdateBO);


    /**
     * 价格规则 bo 转po
     */

    ElectricityPriceRule electricityPriceRuleBOTOPO(ElectricityPriceUpdateBO electricityPriceUpdateBO);

    /**
     * 价格 bo 转po
     */

    ElectricityPrice electricityPriceBOTOPO(ElectricityPriceUpdateBO electricityPriceUpdateBO);

}
