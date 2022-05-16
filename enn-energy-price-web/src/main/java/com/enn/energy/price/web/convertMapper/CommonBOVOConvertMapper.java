package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.ElectricityPriceDictionaryBO;
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

    List<ElectricityPriceUpdateBO> priceRuleAndDetailListReqVOToBO(List<ElectricityPriceUpdateReqVO> priceRuleAndDetailListReqVO);

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
            @Mapping(source = "seasonValidateReqVO.timeSectionValidateReqVOList", target = "timeSectionCreateBOList")
    })
    ElectricitySeasonCreateBO seasonValidateReqVOToBO(ElectricitySeasonValidateReqVO seasonValidateReqVO);

    List<ElectricitySeasonCreateBO> seasonValidateReqVOListToBOList(List<ElectricitySeasonValidateReqVO> seasonCreateReqVOList);

    @Mapping(source = "importDataReqVO.updateReqVOList", target = "updateReqVOList")
    ElectricityPriceImportDataBO importDataReqVOToBO(ElectricityPriceImportDataReqVO importDataReqVO);

    /**
     * @param electricityPriceVersionUpdateReqVO
     * @return bo
     */
    ElectricityPriceVersionDeleteBO  electricityPriceVersionDeleteReqVOToBO(ElectricityPriceVersionDeleteReqVO electricityPriceVersionDeleteReqVO);

    /**
     * 电价体系删除转换
     * @param vo
     * @return
     */
    ElectricityPriceStructureDeleteValidateBO ElectricityPriceStructureDeleteValidateVOToBO(ElectricityPriceStructureDeleteValidateReqVO vo);

    /**
     * vo to bo
     * @param provinceVO
     * @return
     */

    ProvinceBO provinceVOToBO(ProvinceVO provinceVO);

//BO->VO

    @Mappings({
            @Mapping(source = "versionStructureBO.electricityPriceDetailBOList", target = "priceDetailRespVOList"),
            @Mapping(source = "versionStructureBO.electricityPriceStructureRuleDetailBOS", target = "structureRuleDetailRespVOList"),
    })
    ElectricityPriceStructureDetailRespVO versionStructureRespBOToVO(ElectricityPriceStructureDetailBO versionStructureBO);

    ElectricityPriceDetailRespVO priceRuleAndDetailRespBOToVO(ElectricityPriceDetailBO priceRuleAndDetailBO);

    List<ElectricityPriceDetailRespVO> priceRuleAndDetailListBOToVO(List<ElectricityPriceDetailBO> priceRuleAndDetailListBO);


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

    ElectricityPriceStructureAndRuleValidateRespVO priceStructureAndRuleValidateRespBOToVO(ElectricityPriceStructureAndRuleValidateRespBO validateRespBO);

    /**
     * @param electricityPriceDictionaryBO
     * @return
     */
    ElectricityPriceDictionaryRespVO ElectricityPriceDictionaryBOToVO(ElectricityPriceDictionaryBO electricityPriceDictionaryBO);


    /**
     * @param electricityPriceDictionaryBOs
     * @return
     */
    List<ElectricityPriceDictionaryRespVO> ElectricityPriceDictionaryBOListToVOList(List<ElectricityPriceDictionaryBO> electricityPriceDictionaryBOs);


    /**
     * @param electricityPriceVersionBOS
     * @return
     */
    List<ElectricityPriceVersionRespVO> electricityPriceVersionRespBOListToVOList(List<ElectricityPriceVersionBO> electricityPriceVersionBOS);

    /**
     * @param electricityPriceVersion
     * @return
     */
    ElectricityPriceVersionRespVO electricityPriceVersionRespBOToVO(ElectricityPriceVersionBO electricityPriceVersion);

    /**
     * @param priceStructureBO
     * @return
     */
    ElectricityPriceStructureRespVO ElectricityPriceStructureRespBOToVO(ElectricityPriceStructureBO priceStructureBO);

    /**
     * @param priceStructureBOList
     * @return
     */
    List<ElectricityPriceStructureRespVO> ElectricityPriceStructureRespBOListToVOList(List<ElectricityPriceStructureBO> priceStructureBOList);

    /**
     * @param cityCode
     * @return
     */
    CityListRespVO CityCodeListBOTOVO(ProvinceListBO cityCode);


    /**
     * @param cityListBO
     * @return
     */
    @Mapping(target = "cityItemList", source = "cityItemList")
    CityListRespVO CityListBOTOVO(CityListBO cityListBO);

    /**
     * @param cityBO
     * @return
     */
    @Mapping(target = "districtList",source = "districtList")
    CityRespVO CityBOTOVO(CityBO cityBO);

    /**
     * @param district
     * @return
     */
    CityRespVO.DistrictVO DistrictBOTOVO(CityBO.District district);

    /**
     * @param provinceBO
     * @return
     */
    ProvinceVO ProvinceBOToVO(ProvinceBO provinceBO);


    @Mapping(target = "provinceVOList", source = "provinceBOList")
    ProvinceListVO ProvinceBOListToVOList(ProvinceListBO provinceBO);
}

