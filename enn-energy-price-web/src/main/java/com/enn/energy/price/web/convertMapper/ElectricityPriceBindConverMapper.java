package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceBindDetailBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceBindNodeStatusBO;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceBindDetailRespVO;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceBindNodeStatusRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

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


}
