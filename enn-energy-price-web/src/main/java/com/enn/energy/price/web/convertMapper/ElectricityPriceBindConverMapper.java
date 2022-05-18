package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.*;
import com.enn.energy.price.web.vo.requestvo.ElectricityPriceBindEditReqVO;
import com.enn.energy.price.web.vo.requestvo.ElectricityPriceBindRemoveReqVO;
import com.enn.energy.price.web.vo.requestvo.ElectricityPriceBindReqVO;
import com.enn.energy.price.web.vo.requestvo.ElectricityPriceBindVersionsReqVO;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceBindEditDetailRespVO;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceBindDetailRespVO;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceBindNodeStatusRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ElectricityPriceBindConverMapper {

    ElectricityPriceBindConverMapper INSTANCE = Mappers.getMapper( ElectricityPriceBindConverMapper.class );

    @Mapping(target = "nodeBindStatusList", source = "nodeBindStatusList")
    ElectricityPriceBindNodeStatusRespVO ElectricityPriceBindNodeStatusBOTOVO(ElectricityPriceBindNodeStatusBO electricityPriceBindNodeStatusBO);

    @Mappings({
            @Mapping(source = "electricityPriceDetailBO", target = "electricityPriceDetailRespVO"),
            @Mapping(source = "nextVersionPriceBindBO", target = "nextVersionPriceBindVO"),
    })
    ElectricityPriceBindDetailRespVO ElectricityPriceBindDetailBOTOVO(ElectricityPriceBindDetailBO electricityPriceBindDetailBO);

    @Mappings({
            @Mapping(source = "electricityPriceBindEditDetailItemBO", target = "electricityPriceBindEditDetailItemRespVO"),
            @Mapping(source = "nextVersionPriceBindDetailItemBO", target = "nextVersionPriceBindVO"),
    })
    ElectricityPriceBindEditDetailRespVO ElectricityPriceBindDetailByEditBOTOVO(ElectricityPriceBindEditDetailBO electricityPriceBindEditDetailBO);

    ElectricityPriceBindEditBO ElectricityPriceBindEditReqVOTOBO(ElectricityPriceBindEditReqVO electricityPriceBindEditReqVO);

    ElectricityPriceBindRemoveBO priceBindRemoveReqVOToBO(ElectricityPriceBindRemoveReqVO electricityPriceBindRemoveReqVO);

    ElectricityPriceBindVersionsBO electricityPriceBindVersionsReqVOToBO(ElectricityPriceBindVersionsReqVO electricityPriceBindVersionsReqVO);

    @Mapping(source = "electricityPriceBindReqVO.nextVersionStructurePrice", target = "nextVersionStructurePriceBO")
    ElectricityPriceBindBO priceBindReqVOToBO(ElectricityPriceBindReqVO electricityPriceBindReqVO);
}
