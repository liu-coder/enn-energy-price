package com.enn.energy.price.biz.service.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.ProvinceBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ProvinceListBO;
import com.enn.energy.price.dal.po.ext.CityCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CityConverMapper {
    CityConverMapper INSTANCE = Mappers.getMapper(CityConverMapper.class);

    /**
     * @param cityCode
     * @return
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "areaCode", source = "areaCode")
    @Mapping(target = "name", source = "name")
    ProvinceBO cityPOToBO(CityCode cityCode);

    /**
     * @param cityCode
     * @return
     */
    List<ProvinceBO> CityCodePOListToBOList(List<CityCode> cityCode);

}
