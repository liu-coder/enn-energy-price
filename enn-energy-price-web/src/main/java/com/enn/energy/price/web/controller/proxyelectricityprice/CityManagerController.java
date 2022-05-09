package com.enn.energy.price.web.controller.proxyelectricityprice;

import com.enn.energy.price.biz.service.bo.proxyprice.CityListBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ProvinceBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ProvinceListBO;
import com.enn.energy.price.biz.service.proxyelectricityprice.CityManagerService;
import com.enn.energy.price.web.convertMapper.CityConverMapper;
import com.enn.energy.price.web.vo.requestvo.CityCodeReqVO;
import com.enn.energy.price.web.vo.responsevo.CityListRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.rdfa.framework.biz.ro.RdfaResult;

import java.util.Optional;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/7 9:40
 */
@RequestMapping("/city")
@Api("城市查询控制器")
@RestController
@Slf4j
public class CityManagerController {
    @Autowired
    private CityManagerService ctyManagerService;

    @PostMapping("/queryProvinces")
    @ApiOperation( "查询省列表" )
    public RdfaResult<CityListRespVO> queryProvinces(){
        ProvinceListBO provinceListBO = ctyManagerService.queryProvinces();
        CityListRespVO cityListRespVO =null;
        if(Optional.ofNullable( provinceListBO ).isPresent()){
            cityListRespVO = CityConverMapper.INSTANCE.CityCodeListBOTOVO( provinceListBO );
        }
        return RdfaResult.success( cityListRespVO );
    }


    @PostMapping("/queryCity")
    @ApiOperation( "查询省下面城市列表" )
    public RdfaResult<CityListRespVO> queryCityCodes(@RequestBody CityCodeReqVO cityCodeReqVO){
        ProvinceBO provinceBO = CityConverMapper.INSTANCE.cityCodeVOTOBO( cityCodeReqVO );
        CityListBO cityListBO = ctyManagerService.queryCitys( provinceBO );
        CityListRespVO cityListRespVO = CityConverMapper.INSTANCE.CityListBOTOVO( cityListBO );
        return RdfaResult.success( cityListRespVO );
    }
}
