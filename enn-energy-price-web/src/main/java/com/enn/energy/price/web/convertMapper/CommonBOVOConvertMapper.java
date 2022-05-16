package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.*;
import com.enn.energy.price.web.vo.requestvo.*;
import com.enn.energy.price.web.vo.responsevo.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * VO、BO转换器类
 *
 * @author sunjidong
 * @date 2022/5/13
 **/
@Mapper
public interface CommonBOVOConvertMapper {

    CommonBOVOConvertMapper INSTANCE = Mappers.getMapper(CommonBOVOConvertMapper.class);

    //VO -> BO

    @Mapping(source = "versionReqVO.electricityPriceStructureUpdateReqVOList", target = "electricityPriceStructureUpdateBOList")
    ElectricityPriceVersionUpdateBO priceVersionReqVOToBO(ElectricityPriceVersionUpdateReqVO versionReqVO);

    @Mappings({
            @Mapping(source = "versionStructureReqVO.electricityPriceUpdateReqVOList", target = "electricityPriceUpdateBOList"),
            @Mapping(source = "versionStructureReqVO.electricityPriceSeasonRuleUpdateReqVOList", target = "electricityPriceStructureRuleUpdateBOList"),
    })
    ElectricityPriceStructureUpdateBO versionStructureReqVOToBO(ElectricityPriceStructureUpdateReqVO versionStructureReqVO);

    ElectricityPriceUpdateBO priceRuleAndDetailReqVOToBO(ElectricityPriceUpdateReqVO priceRuleAndDetailReqVO);

    @Mapping(source = "structureRuleReqVO.electricityPriceSeasonUpdateReqVOList", target = "electricityPriceSeasonUpdateReqVOList")
    ElectricityPriceStructureRuleUpdateBO structureRuleReqVOToBO(ElectricityPriceSeasonRuleUpdateReqVO structureRuleReqVO);

    @Mappings({
            @Mapping(source = "structureRuleSeasonReqVO.seasonDateVO", target = "seasonDateList"),
            @Mapping(source = "structureRuleSeasonReqVO.electricityPriceStrategyReqVOList", target = "electricityPriceStrategyBOList"),
    })
    ElectricityPriceSeasonUpdateBO structureRuleSeasonReqVOToBO(ElectricityPriceSeasonUpdateReqVO structureRuleSeasonReqVO);

    List<ElectricityPriceSeasonUpdateBO> structureRuleSeasonReqVOToBOList(List<ElectricityPriceSeasonUpdateReqVO> structureRuleSeasonReqVO);


    SeasonDateBO seasonSectionReqVOToBO(SeasonDateVO seasonSectionReqVO);

    @Mapping(source = "structureRuleSeasonStrategyReqVO.electricityTimeSectionUpdateReqVOList", target = "electricityTimeSectionUpdateBOList")
    ElectricityPriceStrategyBO structureRuleSeasonStrategyReqVOToBO(ElectricityPriceStrategyReqVO structureRuleSeasonStrategyReqVO);

    ElectricityTimeSectionUpdateBO seasonStrategyTimeSectionReqVOToBO(ElectricityTimeSectionUpdateReqVO seasonStrategyTimeSectionReqVO);

    @Mappings({
            @Mapping(source = "seasonValidateReqVO.seasonSectionValidateReqVOList", target = "seasonSectionCreateBOList"),
            @Mapping(source = "seasonValidateReqVO.timeSectionValidateReqVOList", target = "timeSectionCreateBOList"),
    })
    ElectricitySeasonCreateBO seasonValidateReqVOToBO(ElectricitySeasonValidateReqVO seasonValidateReqVO);

    List<ElectricitySeasonCreateBO> seasonValidateReqVOListToBOList(List<ElectricitySeasonValidateReqVO> seasonCreateReqVOList);


//BO->VO

    @Mappings({
            @Mapping(source = "versionStructureBO.electricityPriceDetailBOList", target = "priceDetailRespVOList"),
            @Mapping(source = "versionStructureBO.electricityPriceStructureRuleDetailBOS", target = "structureRuleDetailRespVOList"),
    })
    ElectricityPriceStructureDetailRespVO versionStructureRespBOToVO(ElectricityPriceStructureDetailBO versionStructureBO);

    ElectricityPriceDetailRespVO priceRuleAndDetailRespBOToVO(ElectricityPriceDetailBO priceRuleAndDetailBO);

    @Mapping(source = "structureRuleBO.electricityPriceSeasonDetailBOS", target = "priceSeasonDetailRespVOS")
    ElectricityPriceStructureRuleDetailRespVO structureRuleRespBOToVO(ElectricityPriceStructureRuleDetailBO structureRuleBO);

    @Mappings({
            @Mapping(source = "structureRuleSeasonBO.seasonDateList", target = "seansonDateRespVOS"),
            @Mapping(source = "structureRuleSeasonBO.electricityPriceStrategyBOList", target = "electricityPriceStrategyVOS"),
    })
    ElectricityPriceSeasonDetailRespVO structureRuleSeasonRespBOToVO(ElectricityPriceSeasonDetailBO structureRuleSeasonBO);

    SeansonDateRespVO seasonSectionReqVOToBO(SeasonDateBO seasonSectionReqVO);

    @Mapping(source = "structureRuleSeasonStrategyRespBO.electricityTimeSectionUpdateBOList", target = "seasonStrategyTimeSectionRespVOList")
    ElectricityPriceStrategyRespVO structureRuleSeasonStrategyRespBOToVO(ElectricityPriceStrategyBO structureRuleSeasonStrategyRespBO);

    ElectricityTimeSectionRespVO seasonStrategyTimeSectionReqBOToVO(ElectricityTimeSectionUpdateBO seasonStrategyTimeSectionBO);

    List<ElectricityPriceStructureDetailRespVO> versionStructureRespBOToVOList(List<ElectricityPriceStructureDetailBO> versionStructureBO);

    @Mapping(source = "structureListDetailBO.structureDetailBOList", target = "structureDetailForCreateRespVOList")
    ElectricityPriceStructureListRespVO structureListDetailBOToVO(ElectricityPriceStructureListDetailBO structureListDetailBO);

}

