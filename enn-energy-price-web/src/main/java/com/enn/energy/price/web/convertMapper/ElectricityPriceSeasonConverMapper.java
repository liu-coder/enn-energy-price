package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceSeasonDetailBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceSeasonUpdateBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceStrategyBO;
import com.enn.energy.price.biz.service.bo.proxyprice.SeasonDateBO;
import com.enn.energy.price.web.vo.requestvo.ElectricityPriceSeasonUpdateReqVO;
import com.enn.energy.price.web.vo.requestvo.ElectricityPriceStrategyReqVO;
import com.enn.energy.price.web.vo.requestvo.SeasonDateVO;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceSeasonDetailRespVO;
import com.enn.energy.price.web.vo.responsevo.SeansonDateRespVO;
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

    @Mappings( {
            @Mapping(source = "electricityPriceStrategyReqVOList",target = "electricityPriceStrategyBOList"),
            @Mapping( source = "seasonDateVO",target = "seasonDateList")
    } )
    ElectricityPriceSeasonUpdateBO ElectricityPriceSeasonUpdateReqVOTOBO(ElectricityPriceSeasonUpdateReqVO electricityPriceSeasonUpdateReqVO);

    List<ElectricityPriceSeasonUpdateBO> electricityPriceSeasonVOListToBOList(List<ElectricityPriceSeasonUpdateReqVO> electricityPriceSeasonUpdateReqVO);

    SeasonDateBO seasonDateVOTOBO(SeasonDateVO seasonDateVO);

    @Mapping( source = "electricityTimeSectionUpdateReqVOList",target = "electricityTimeSectionUpdateBOList")
    ElectricityPriceStrategyBO ElectricityPriceStrategyReqVOTOBO(ElectricityPriceStrategyReqVO electricityPriceStrategyReqVOTOVO);

}
