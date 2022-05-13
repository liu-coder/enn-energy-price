package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceSeasonDetailBO;
import com.enn.energy.price.biz.service.bo.proxyprice.SeasonDateBO;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceSeasonDetailForCreateRespVO;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceSeasonDetailRespVO;
import com.enn.energy.price.web.vo.responsevo.SeansonDateRespVO;
import com.enn.energy.price.web.vo.responsevo.SeasonDateForCreateRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {ElectricityPriceTimeConverMapper.class})
public interface ElectricityPriceSeasonConverMapper {
    ElectricityPriceSeasonConverMapper INSTANCE = Mappers.getMapper(ElectricityPriceSeasonConverMapper.class);

    List<ElectricityPriceSeasonDetailRespVO> electricityPriceSeasonDetailBOListToVOList(List<ElectricityPriceSeasonDetailBO>  electricityPriceSeasonDetailBOs);

    @Mappings( {
            @Mapping( source = "electricityPriceSeasonDetailBO.seasonDateList",target = "seansonDateRespVOS"),
            @Mapping( source = "electricityPriceSeasonDetailBO.electricityPriceStrategyBOList",target = "electricityPriceStrategyVOS")
    } )
    ElectricityPriceSeasonDetailRespVO electricityPriceSeasonDetailBOToVO(ElectricityPriceSeasonDetailBO electricityPriceSeasonDetailBO);

    SeansonDateRespVO seansonDateBOToVO(SeasonDateBO seasonDateBO);


    List<SeansonDateRespVO> seansonDateBOListToVOList(List<SeasonDateBO> seasonDateBOS);


    SeasonDateForCreateRespVO SeansonDateForCreateBOToVO(SeasonDateBO seansonDateBO);

    ElectricityPriceSeasonDetailForCreateRespVO ElectricityPriceSeasonDetailForCreateBOToVO(ElectricityPriceSeasonDetailBO electricityPriceSeasonDetailBO);


}
