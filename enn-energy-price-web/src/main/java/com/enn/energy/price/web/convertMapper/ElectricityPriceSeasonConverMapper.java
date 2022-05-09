package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceSeasonDetailBO;
import com.enn.energy.price.biz.service.bo.proxyprice.SeansonDateBO;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceSeasonDetailRespVO;
import com.enn.energy.price.web.vo.responsevo.SeansonDateRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ElectricityPriceSeasonConverMapper {
    ElectricityPriceSeasonConverMapper INSTANCE = Mappers.getMapper(ElectricityPriceSeasonConverMapper.class);

    ElectricityPriceSeasonDetailRespVO ElectricityPriceSeasonDetailBOToVO(ElectricityPriceSeasonDetailBO electricityPriceSeasonDetailBO);

    SeansonDateRespVO SeansonDateBOToVO(SeansonDateBO seansonDateBO);


}
