package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.*;
import com.enn.energy.price.web.vo.requestvo.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * xxxxx此处为类的描述信息
 *
 * @author sunjidong
 * @date 2022/5/12
 **/
@Mapper
public interface ElectricityPriceVersionCreateConvertMapper {

    ElectricityPriceVersionCreateConvertMapper INSTANCE = Mappers.getMapper(ElectricityPriceVersionCreateConvertMapper.class);

    //VO -> BO
    @Mappings({
            @Mapping(source = "priceVersionStructuresReqVO.priceVersionCreateReqVO", target = "priceVersionCreateBO"),
            @Mapping(source = "priceVersionStructuresReqVO.priceStructureAndRuleAndSeasonCreateReqVOList", target = "priceStructureAndRuleAndSeasonCreateBOList")
    })
    ElectricityPriceVersionStructuresCreateBO priceVersionStructuresCreateReqVOToBO(ElectricityPriceVersionStructuresCreateReqVO priceVersionStructuresReqVO);

    @Mappings({
            @Mapping(source = "priceStructureAndRuleAndSeasonCreateReqVO.priceStructureCreateReqVO", target = "priceStructureCreateBO"),
            @Mapping(source = "priceStructureAndRuleAndSeasonCreateReqVO.priceStructureRuleCreateReqVOList", target = "priceStructureRuleCreateBOList"),
            @Mapping(source = "priceStructureAndRuleAndSeasonCreateReqVO.priceRuleCreateReqVOList", target = "priceRuleCreateBOList")
    })
    ElectricityPriceStructureAndRuleAndSeasonCreateBO structureAndRuleAndSeasonCreateReqVOToBO(ElectricityPriceStructureAndRuleAndSeasonCreateReqVO priceStructureAndRuleAndSeasonCreateReqVO);

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

    List<ElectricityPriceRuleCreateBO> priceRuleCreateReqVOListToBOList(List<ElectricityPriceRuleCreateReqVO> priceRuleCreateReqVOList);


//BO->VO

    @Mapping(source = "priceRuleCreateBO.electricityPriceCreateBO", target = "electricityPriceCreateReqVO")
    ElectricityPriceRuleCreateReqVO priceRuleCreateReqBOToVO(ElectricityPriceRuleCreateBO priceRuleCreateBO);

    List<ElectricityPriceRuleCreateReqVO> priceRuleCreateReqBOListToVOList(List<ElectricityPriceRuleCreateBO> priceRuleCreateBOList);


}
