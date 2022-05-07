package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.*;
import com.enn.energy.price.web.vo.requestvo.*;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceStructureAndRuleValidateRespVO;
import org.apache.ibatis.annotations.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * VO层通用转换器
 *
 * @author sunjidong
 * @date 2022/5/2
 **/
@Mapper
public interface ElectricityPriceVersionCreateBOConvertMapper {

    ElectricityPriceVersionCreateBOConvertMapper INSTANCE = Mappers.getMapper(ElectricityPriceVersionCreateBOConvertMapper.class);

    ElectricityPriceVersionCreateBO priceVersionCreateReqVOToBO(ElectricityPriceVersionCreateReqVO priceVersionStructuresReqVO);

    ElectricityPriceStructureCreateBO priceStructureCreateReqVOToBO(ElectricityPriceStructureCreateReqVO priceStructureCreateReqVO);

    @Mapping(source = "priceStructureRuleCreateReqVO.seasonCreateReqVOList", target = "seasonCreateBOList")
    ElectricityPriceStructureRuleCreateBO priceStructureRuleCreateReqVOToBO(ElectricityPriceStructureRuleCreateReqVO priceStructureRuleCreateReqVO);

    @Mappings({
            @Mapping(source = "seasonCreateReqVO.seasonSectionCreateReqVOList", target = "seasonSectionCreateBOList"),
            @Mapping(source = "seasonCreateReqVO.timeSectionCreateReqVOList", target = "timeSectionCreateBOList"),
    })
    ElectricitySeasonCreateBO seasonCreateReqVOToBO(ElectricitySeasonCreateReqVO seasonCreateReqVO);

    ElectricitySeasonSectionCreateBO seasonSectionCreateReqVOToBO(ElectricitySeasonSectionCreateReqVO seasonSectionCreateReqVO);

    ElectricityTimeSectionCreateBO timeSectionCreateReqVOToBO(ElectricityTimeSectionCreateReqVO timeSectionCreateReqVO);

    @Mapping(source = "priceRuleCreateReqVO.electricityPriceCreateReqVO", target = "electricityPriceCreateBO")
    ElectricityPriceRuleCreateBO priceRuleCreateReqVOToBO(ElectricityPriceRuleCreateReqVO priceRuleCreateReqVO);

    ElectricityPriceCreateBO priceCreateVOToBO(ElectricityPriceCreateReqVO priceReqCreateReqVO);

    @Mappings({
            @Mapping(source = "priceStructureAndRuleAndSeasonCreateReqVO.priceStructureCreateReqVO", target = "priceStructureCreateBO"),
            @Mapping(source = "priceStructureAndRuleAndSeasonCreateReqVO.priceStructureRuleCreateReqVOList", target = "priceStructureRuleCreateBOList"),
            @Mapping(source = "priceStructureAndRuleAndSeasonCreateReqVO.priceRuleCreateReqVOList", target = "priceRuleCreateBOList")
    })
    ElectricityPriceStructureAndRuleAndSeasonCreateBO structureAndRuleAndSeasonCreateReqVOToBO(ElectricityPriceStructureAndRuleAndSeasonCreateReqVO priceStructureAndRuleAndSeasonCreateReqVO);

    @Mappings({
            @Mapping(source = "priceVersionStructuresReqVO.priceVersionCreateReqVO", target = "priceVersionCreateBO"),
            @Mapping(source = "priceVersionStructuresReqVO.priceStructureAndRuleAndSeasonCreateReqVOList", target = "priceStructureAndRuleAndSeasonCreateBOList")
    })
    ElectricityPriceVersionStructuresCreateBO priceVersionStructuresCreateReqVOToBO(ElectricityPriceVersionStructuresCreateReqVO priceVersionStructuresReqVO);


    @Mappings({
            @Mapping(source = "validateReqVO.priceStructureRuleValidateReqVOList", target = "priceStructureRuleValidateReqVOList"),
            @Mapping(source = "validateReqVO.priceRuleValidateReqVOList", target = "priceRuleValidateReqVOList")
    })
    ElectricityPriceStructureAndRuleValidateBO structureAndRuleValidateVOToBO(ElectricityPriceStructureAndRuleValidateReqVO validateReqVO);

     ElectricityPriceStructureAndRuleValidateRespVO priceStructureAndRuleValidateRespBOToVO(ElectricityPriceStructureAndRuleValidateRespBO validateRespBO);
}