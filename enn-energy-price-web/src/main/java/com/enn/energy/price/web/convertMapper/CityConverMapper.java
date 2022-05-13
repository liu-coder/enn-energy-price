package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.CityBO;
import com.enn.energy.price.biz.service.bo.proxyprice.CityListBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ProvinceBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ProvinceListBO;
import com.enn.energy.price.web.vo.requestvo.CityCodeReqVO;
import com.enn.energy.price.web.vo.responsevo.CityListRespVO;
import com.enn.energy.price.web.vo.responsevo.CityRespVO;
import com.enn.energy.price.web.vo.responsevo.ProvinceListVO;
import com.enn.energy.price.web.vo.responsevo.ProvinceVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 城市转换mapper
 */
@Mapper
public interface CityConverMapper {
    CityConverMapper INSTANCE = Mappers.getMapper( CityConverMapper.class );

    /**
     * vo to bo
     * @param provinceVO
     * @return
     */

    ProvinceBO provinceVOToBO(ProvinceVO provinceVO);


    /**
     * @param cityCode
     * @return
     */
    CityListRespVO CityCodeListBOTOVO(ProvinceListBO cityCode);


    /**
     * @param cityListBO
     * @return
     */
    @Mapping(target = "cityItemList", source = "cityItemList")
    CityListRespVO CityListBOTOVO(CityListBO cityListBO);

    /**
     * @param cityBO
     * @return
     */
    @Mapping(target = "districtList",source = "districtList")
    CityRespVO CityBOTOVO(CityBO cityBO);

    /**
     * @param district
     * @return
     */
    CityRespVO.DistrictVO DistrictBOTOVO(CityBO.District district);

    /**
     * @param provinceBO
     * @return
     */
    ProvinceVO ProvinceBOToVO(ProvinceBO provinceBO);


    @Mapping(target = "provinceVOList", source = "provinceBOList")
    ProvinceListVO ProvinceBOListToVOList(ProvinceListBO provinceBO);
}
