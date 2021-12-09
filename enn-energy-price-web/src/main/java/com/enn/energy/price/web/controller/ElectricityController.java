package com.enn.energy.price.web.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enn.energy.price.client.dto.request.EletricityUnifiedReqDto;
import com.enn.energy.price.client.dto.response.ElectricityPriceUnifiedDetailRespDto;

import io.swagger.annotations.ApiOperation;
import top.rdfa.framework.biz.ro.RdfaResult;



@RestController
@RequestMapping()
public class ElectricityController {


    /**
     * 统一电价接口，统一自定义电价，目录电价
     * @param <ElectricityPriceUnifiedDetailRespDto>
     * @return
     */
    @PostMapping("/price/queryElectricity")
    @ApiOperation("统一电价接口，统一自定义电价，目录电价查询")
    public  RdfaResult<ElectricityPriceUnifiedDetailRespDto> queryElectricity(@RequestBody EletricityUnifiedReqDto eletricityUnifiedReqDto){
        
    	
    	return null;
    }

}
