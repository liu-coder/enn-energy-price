package com.enn.energy.price.biz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enn.energy.price.client.dto.request.EletricityUnifiedReqDto;
import com.enn.energy.price.client.dto.response.ElectricityPriceUnifiedDetailRespDto;
import com.enn.energy.price.client.dto.response.ElectricityPriceValueDetailRespDTO;
import com.enn.energy.price.client.service.ElectricityPriceSelectService;

import top.rdfa.framework.biz.ro.RdfaResult;

@Service
public class ElectricityPriceUnifiedService {
	
	@Autowired
	ElectricityPriceSelectService electricityPriceSelectService;
	
	public RdfaResult<ElectricityPriceUnifiedDetailRespDto> queryUnifiedPrice(EletricityUnifiedReqDto eletricityUnifiedReqDto){
		
		RdfaResult<ElectricityPriceValueDetailRespDTO> sepecialResult = electricityPriceSelectService.selectElePrice(eletricityUnifiedReqDto);
		
		if(sepecialResult == null) {
			
		}
		return null;
	}
}
