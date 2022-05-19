package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceVersionBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceVersionDeleteBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceVersionStructureBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceVersionUpdateBO;
import com.enn.energy.price.web.vo.requestvo.ElectricityPriceVersionDeleteReqVO;
import com.enn.energy.price.web.vo.requestvo.ElectricityPriceVersionUpdateReqVO;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceVersionRespVO;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceVersionStructureRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 版本转换
 */
@Mapper(uses = {ElectricityPriceStrutureConverMapper.class})
public interface ElectricityPriceVersionConverMapper {
    ElectricityPriceVersionConverMapper INSTANCE = Mappers.getMapper(ElectricityPriceVersionConverMapper.class);

    @Mappings( {
            @Mapping(source = "electricityPriceStructureUpdateReqVOList",target = "electricityPriceStructureUpdateBOList")
    } )
    ElectricityPriceVersionUpdateBO electricityPriceVersionUpdateReqVOToBO(ElectricityPriceVersionUpdateReqVO electricityPriceVersionUpdateReqVO);


    ElectricityPriceVersionDeleteBO electricityPriceVersionDeleteReqVOToBO(ElectricityPriceVersionDeleteReqVO electricityPriceVersionUpdateReqVO);

    ElectricityPriceVersionRespVO electricityPriceVersionRespBOToVO(ElectricityPriceVersionBO electricityPriceVersion);

    List<ElectricityPriceVersionRespVO> electricityPriceVersionRespBOListToVOList(List<ElectricityPriceVersionBO> electricityPriceVersionBOS);

    @Mapping(source = "electricityPriceStructureBOS", target = "electricityPriceStructureRespVOList")
    List<ElectricityPriceVersionStructureRespVO> electricityPriceVersionStructureRespBOListToVOList(List<ElectricityPriceVersionStructureBO> electricityPriceVersionStructureBOS);


}
