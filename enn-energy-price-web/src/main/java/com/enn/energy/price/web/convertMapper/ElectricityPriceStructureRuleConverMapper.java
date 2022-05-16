package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceStructureRuleDetailBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceStructureRuleUpdateBO;
import com.enn.energy.price.web.vo.requestvo.ElectricityPriceSeasonRuleUpdateReqVO;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceStructureRuleDetailForCreateRespVO;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceStructureRuleDetailRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {ElectricityPriceSeasonConverMapper.class,ElectricityPriceConverMapper.class})
public interface ElectricityPriceStructureRuleConverMapper {
    ElectricityPriceStructureRuleConverMapper INSTANCE = Mappers.getMapper(ElectricityPriceStructureRuleConverMapper.class);

    List<ElectricityPriceStructureRuleDetailRespVO> ElectricityPriceStructureRuleDetailBOListToVOList(List<ElectricityPriceStructureRuleDetailBO> electricityPriceStructureRuleDetailBOs);

    @Mapping(source = "electricityPriceSeasonDetailBOS",target = "priceSeasonDetailRespVOS")
    ElectricityPriceStructureRuleDetailRespVO ElectricityPriceStructureRuleDetailBOToVO(ElectricityPriceStructureRuleDetailBO electricityPriceStructureRuleDetailBO);





    ElectricityPriceStructureRuleDetailForCreateRespVO ElectricityPriceStructureRuleDetailForCreateBOToVO(ElectricityPriceStructureRuleDetailBO electricityPriceStructureRuleDetailBO);

    @Mappings( {
            @Mapping(source = "electricityPriceSeasonUpdateReqVOList",target = "electricityPriceSeasonUpdateReqVOList"),
    } )
    ElectricityPriceStructureRuleUpdateBO ElectricityPriceSeasonRuleUpdateReqVOTOBO(ElectricityPriceSeasonRuleUpdateReqVO electricityPriceSeasonRuleUpdateReqVO);


}
