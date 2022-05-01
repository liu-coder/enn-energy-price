package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceStructureRuleCreateBO;
import com.enn.energy.price.web.vo.requestvo.ElectricityPriceStructureRuleCreateReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * VO转BO转换器类
 *
 * @author sunjidong
 * @date 2022/5/1
 **/
@Mapper(uses = {ElectricitySeasonCreateReqVOMapper.class})
public interface ElectricityPriceStructureRuleCreateReqVOMapper {

    ElectricityPriceStructureRuleCreateReqVOMapper INSTANCE = Mappers.getMapper(ElectricityPriceStructureRuleCreateReqVOMapper.class);

    @Mapping(source = "priceStructureRuleCreateReqVO.seasonCreateReqVOList", target = "seasonCreateBOList")
    ElectricityPriceStructureRuleCreateBO priceStructureRuleCreateReqVOToBO(ElectricityPriceStructureRuleCreateReqVO priceStructureRuleCreateReqVO);

}
