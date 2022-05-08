package com.enn.energy.price.biz.service.proxyelectricityprice.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.enn.energy.price.biz.service.bo.proxyprice.CityBO;
import com.enn.energy.price.biz.service.bo.proxyprice.CityListBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ProvinceBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ProvinceListBO;
import com.enn.energy.price.biz.service.convertMapper.CityConverMapper;
import com.enn.energy.price.biz.service.proxyelectricityprice.CityManagerService;
import com.enn.energy.price.common.enums.BoolLogic;
import com.enn.energy.price.dal.mapper.ext.proxyprice.CityExtMapper;
import com.enn.energy.price.dal.po.ext.CityCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/7 9:51
 */
@Service
public class CityMangerServiceImp implements CityManagerService {
    @Autowired
    CityExtMapper cityExtMapper;



    @Override
    public CityListBO queryCitys(ProvinceBO provinceBO) {
        CityCode cityCode = new CityCode();
        cityCode.setParentId( provinceBO.getProvinceCode() );
        cityCode.setState( BoolLogic.NO.getCode());
        List<CityCode> cityCodes = cityExtMapper.queryCityList( cityCode );
        List<CityBO> cityBOList = cityCodes.stream().map( t -> {
            CityCode cityCode1 = new CityCode();
            cityCode1.setState( BoolLogic.NO.getCode() );
            cityCode1.setParentId( t.getId() );
            List<CityCode> cityCodesList = cityExtMapper.queryCityList( cityCode1 );
            CityBO cityBO = new CityBO();
            cityBO.setCity( t.getName() );
            cityBO.setCityCode( t.getAreaCode() );
            List<CityBO.District> collect = cityCodesList.stream().map( f -> {
                CityBO.District district = new CityBO.District();
                district.setDistrictCode( f.getAreaCode() );
                district.setDistrict( f.getName() );
                return district;
            } ).collect( Collectors.toList() );
            cityBO.setDistrictList( collect );
            return cityBO;
        } ).collect( Collectors.toList() );
        CityListBO cityListBO = new CityListBO();
        cityListBO.setCityItemList( cityBOList );
        return cityListBO;
    }

    @Override
    public ProvinceListBO queryProvinces() {
        CityCode cityCode = new CityCode();
        cityCode.setLevel( "1" );
        cityCode.setState( BoolLogic.NO.getCode());
        List<CityCode> cityCodes = cityExtMapper.queryCityList( cityCode );
        if(CollectionUtil.isEmpty( cityCodes )){
            return null;
        }
        ProvinceListBO provinceListBO = CityConverMapper.INSTANCE.CityCodePOListTOBOList( cityCodes );
        return provinceListBO;
    }
}
