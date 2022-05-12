package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.*;
import com.enn.energy.price.web.vo.requestvo.*;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceStructureAndRuleValidateRespVO;
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
public interface ElectricityPriceVersionValidateConvertMapper {

    ElectricityPriceVersionValidateConvertMapper INSTANCE = Mappers.getMapper(ElectricityPriceVersionValidateConvertMapper.class);

    //VO -> BO

    @Mappings({
            @Mapping(source = "validateReqVO.priceStructureRuleValidateReqVOList", target = "priceStructureRuleValidateReqVOList"),
            @Mapping(source = "validateReqVO.priceRuleValidateReqVOList", target = "priceRuleValidateReqVOList")
    })
    ElectricityPriceStructureAndRuleValidateBO structureAndRuleValidateVOToBO(ElectricityPriceStructureAndRuleValidateReqVO validateReqVO);

    @Mapping(source = "validateVO.seasonValidateReqVOList", target = "seasonCreateBOList")
    ElectricityPriceStructureRuleCreateBO priceStructureRuleValidateReqVOToBO(ElectricityPriceStructureRuleValidateReqVO validateVO);

    @Mappings({
            @Mapping(source = "seasonValidateReqVO.seasonSectionValidateReqVOList", target = "seasonSectionCreateBOList"),
            @Mapping(source = "seasonValidateReqVO.timeSectionValidateReqVOList", target = "timeSectionCreateBOList"),
    })
    ElectricitySeasonCreateBO seasonValidateReqVOToBO(ElectricitySeasonValidateReqVO seasonValidateReqVO);

    List<ElectricitySeasonCreateBO> seasonValidateReqVOListToBOList(List<ElectricitySeasonValidateReqVO> seasonCreateReqVOList);

    ElectricitySeasonSectionCreateBO seasonSectionValidateReqVOToBO(ElectricitySeasonSectionValidateReqVO validateVO);

    ElectricityTimeSectionCreateBO timeSectionValidateReqVOToBO(ElectricityTimeSectionValidateReqVO validateVO);

//BO -> VO

    ElectricityPriceStructureAndRuleValidateRespVO priceStructureAndRuleValidateRespBOToVO(ElectricityPriceStructureAndRuleValidateRespBO validateRespBO);

}
