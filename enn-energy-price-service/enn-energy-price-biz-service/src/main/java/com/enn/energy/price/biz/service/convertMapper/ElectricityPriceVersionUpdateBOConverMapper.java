package com.enn.energy.price.biz.service.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.*;
import com.enn.energy.price.dal.po.ext.ElectricityPriceDetailPO;
import com.enn.energy.price.dal.po.mbg.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ElectricityPriceVersionUpdateBOConverMapper {

    ElectricityPriceVersionUpdateBOConverMapper INSTANCE = Mappers.getMapper(ElectricityPriceVersionUpdateBOConverMapper.class);

    /**
     * 版本体系bo转po
     * @param electricityPriceStructureUpdateBO
     * @return
     */
    @Mappings( {
            @Mapping(source = "id",target = "structureId")
    } )
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


    /**
     * @param electricityPriceVersion
     * @return
     */
    ElectricityPriceVersionBO electricityPriceVersionPOTOBO(ElectricityPriceVersion electricityPriceVersion);

    /**
     * @param electricityPriceVersions
     * @return
     */
    List<ElectricityPriceVersionBO> electricityPriceVersionListPOToBO(List<ElectricityPriceVersion> electricityPriceVersions);


    /**
     * @param ectricityPriceStructure
     * @return
     */
    ElectricityPriceStructureBO electricityPriceStructurePOToBO(ElectricityPriceStructure ectricityPriceStructure);

    /**
     * @param electricityPriceStructures
     * @return
     */
    List<ElectricityPriceStructureBO> electricityPriceStructurePOListToBOList(List<ElectricityPriceStructure> electricityPriceStructures);


    /**
     * @param ectricityPriceStructure
     * @return
     */
    ElectricityPriceStructureDetailBO ElectricityPriceStructurePOTOElectricityPriceStructureDetailBO(ElectricityPriceStructure ectricityPriceStructure);


    /**
     *
     * @param ectricityPriceStructure
     * @return
     */
    ElectricityPriceStructureRuleDetailBO ElectricityPriceStructureRulePOTOBO(ElectricityPriceStructureRule ectricityPriceStructure);


    /**
     * @param electricityTimeSection
     * @return
     */
    ElectricityTimeSectionUpdateBO ElectricityTimeSectionPOToBO(ElectricityTimeSection electricityTimeSection);

    /**
     * @param electricityTimeSectionList
     * @return
     */
    List<ElectricityTimeSectionUpdateBO> ElectricityTimeSectionPOListToBOList(List<ElectricityTimeSection> electricityTimeSectionList);

    /**
     * @param electricityPriceDetailPOS
     * @return
     */
    List<ElectricityPriceDetailBO>  electricityPriceDetailPOListToBOList(List<ElectricityPriceDetailPO> electricityPriceDetailPOS);

    /**
     * @param electricityPriceDetailPO
     * @return
     */
    ElectricityPriceDetailBO  electricityPriceDetailPOToBO(ElectricityPriceDetailPO electricityPriceDetailPO);

    /**
     * @param electricityPrice
     * @return
     */
    ElectricityPrice electricityPriceBOToPO(ElectricityPriceUpdateBO electricityPrice);
}
