package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.CityBO;
import com.enn.energy.price.biz.service.bo.proxyprice.CityListBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ProvinceBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ProvinceListBO;
import com.enn.energy.price.web.vo.requestvo.CityCodeReqVO;
import com.enn.energy.price.web.vo.responsevo.CityListRespVO;
import com.enn.energy.price.web.vo.responsevo.CityRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 城市转换mapper
 */
@Mapper
public interface CityConverMapper {
    CityConverMapper INSTANCE = Mappers.getMapper( CityConverMapper.class );

    /**
     * vo to bo
     * @param cityCodeReqVO
     * @return
     */
    ProvinceBO cityCodeVOTOBO(CityCodeReqVO cityCodeReqVO);


    /**
     * @param cityCode
     * @return
     */
    CityListRespVO CityCodeListBOTOVO(ProvinceListBO cityCode);


    /**
     * @param cityListBO
     * @return
     */
    CityListRespVO CityListBOTOVO(CityListBO cityListBO);

    /**
     * @param cityBO
     * @return
     */
    CityRespVO CityBOTOVO(CityBO cityBO);

    /**
     * @param district
     * @return
     */
    CityRespVO.DistrictVO DistrictBOTOVO(CityBO.District district);
}
