package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceStructureAndRuleAndSeasonCreateBO;
import com.enn.energy.price.web.vo.requestvo.ElectricityPriceStructureAndRuleAndSeasonCreateReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * xxxxx此处为类的描述信息
 *
 * @author sunjidong
 * @date 2022/5/1
 **/
@Mapper(uses = {
                ElectricityPriceStructureCreateReqVOMapper.class,
                ElectricityPriceStructureRuleCreateReqVOMapper.class,
                ElectricityPriceRuleCreateReqVOMapper.class
                })
public interface ElectricityPriceStructureAndRuleAndSeasonCreateReqVOMapper {

    ElectricityPriceStructureAndRuleAndSeasonCreateReqVOMapper INSTANCE = Mappers.getMapper(ElectricityPriceStructureAndRuleAndSeasonCreateReqVOMapper.class);

    @Mappings({
            @Mapping(source = "priceStructureAndRuleAndSeasonCreateReqVO.priceStructureCreateReqVO", target = "priceStructureCreateBO"),
            @Mapping(source = "priceStructureAndRuleAndSeasonCreateReqVO.priceStructureRuleCreateReqVOList", target = "priceStructureRuleCreateBOList"),
            @Mapping(source = "priceStructureAndRuleAndSeasonCreateReqVO.priceRuleCreateReqVOList", target = "priceRuleCreateBOList")
    })

    ElectricityPriceStructureAndRuleAndSeasonCreateBO structureAndRuleAndSeasonCreateReqVOToBO(ElectricityPriceStructureAndRuleAndSeasonCreateReqVO priceStructureAndRuleAndSeasonCreateReqVO);

}
