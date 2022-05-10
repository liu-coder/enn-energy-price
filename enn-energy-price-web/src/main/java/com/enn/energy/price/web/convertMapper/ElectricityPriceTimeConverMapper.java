package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceStrategyBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityTimeSectionUpdateBO;
import com.enn.energy.price.web.vo.requestvo.ElectricityTimeSectionUpdateReqVO;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceStrategyRespVO;
import com.enn.energy.price.web.vo.responsevo.ElectricityTimeSectionForCreateRespVO;
import com.enn.energy.price.web.vo.responsevo.ElectricityTimeSectionRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ElectricityPriceTimeConverMapper {
    ElectricityPriceTimeConverMapper INSTANCE = Mappers.getMapper(ElectricityPriceTimeConverMapper.class);

    ElectricityPriceStrategyRespVO ElectricityPriceStrategyBOToVO(ElectricityPriceStrategyBO electricityPriceStrategyBO);

    ElectricityTimeSectionRespVO ElectricityTimeSectionUpdateBOToVO(ElectricityTimeSectionUpdateBO timeSectionUpdateBO);

    ElectricityTimeSectionForCreateRespVO ElectricityTimeSectionForCreateBOToVO(ElectricityTimeSectionUpdateBO timeSectionUpdateBO);

}
