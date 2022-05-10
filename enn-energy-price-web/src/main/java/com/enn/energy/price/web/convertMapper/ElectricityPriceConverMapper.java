package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceDetailBO;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceDetailForCreateRespVO;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceDetailRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ElectricityPriceConverMapper {
    ElectricityPriceConverMapper INSTANCE = Mappers.getMapper(ElectricityPriceConverMapper.class);

    ElectricityPriceDetailRespVO ElectricityPriceDetailBOToVO(ElectricityPriceDetailBO electricityPriceDetailBO);

    ElectricityPriceDetailForCreateRespVO ElectricityPriceDetailForUpdateBOToVO(ElectricityPriceDetailBO electricityPriceDetailBO);

}