package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceVersionStructuresCreateBO;
import com.enn.energy.price.web.vo.requestvo.ElectricityPriceVersionStructuresCreateReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 新建版本总请求VO与BO转换器
 *
 * @author sunjidong
 * @date 2022/5/1
 **/
@Mapper(uses = {
                ElectricityPriceVersionCreateReqVOMapper.class,
                ElectricityPriceStructureAndRuleAndSeasonCreateReqVOMapper.class
                }
       )
public interface ElectricityPriceVersionStructuresCreateReqVOMapper {

    ElectricityPriceVersionStructuresCreateReqVOMapper INSTANCE = Mappers.getMapper(ElectricityPriceVersionStructuresCreateReqVOMapper.class);

    @Mappings({
            @Mapping(source = "priceVersionStructuresReqVO.priceVersionCreateReqVO", target = "priceVersionCreateBO"),
            @Mapping(source = "priceVersionStructuresReqVO.priceStructureAndRuleAndSeasonCreateReqVOList", target = "priceStructureAndRuleAndSeasonCreateBOList")
    })
    ElectricityPriceVersionStructuresCreateBO priceVersionStructuresCreateReqVOToBO(ElectricityPriceVersionStructuresCreateReqVO priceVersionStructuresReqVO);
}
