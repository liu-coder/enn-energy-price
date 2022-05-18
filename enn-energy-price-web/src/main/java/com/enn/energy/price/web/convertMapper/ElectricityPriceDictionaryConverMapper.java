package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.ElectricityPriceDictionaryBO;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceDictionaryRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ElectricityPriceDictionaryConverMapper {
    ElectricityPriceDictionaryConverMapper INSTANCE = Mappers.getMapper(ElectricityPriceDictionaryConverMapper.class);

    ElectricityPriceDictionaryRespVO ElectricityPriceDictionaryBOToVO(ElectricityPriceDictionaryBO electricityPriceDictionaryBO);
    List<ElectricityPriceDictionaryRespVO> ElectricityPriceDictionaryBOListToVOList(List<ElectricityPriceDictionaryBO> electricityPriceDictionaryBOs);

}
