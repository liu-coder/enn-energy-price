package com.enn.energy.price.biz.service;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enn.energy.price.biz.service.strategy.PriceStrategyService;
import com.enn.energy.price.client.dto.request.EletricityUnifiedReqDto;
import com.enn.energy.price.client.dto.response.ElectricityPriceUnifiedDetailRespDto;
import com.enn.energy.price.client.service.ElectricityPriceSelectService;
import com.enn.energy.price.common.enums.PriceType;

import top.rdfa.framework.biz.ro.RdfaResult;

@Service
public class ElectricityPriceUnifiedService {
	

	
	@Resource(name="customPriceService")
	PriceStrategyService customPriceService;
	
//	@Resource(name="customPriceService")
//	PriceStrategyService customPriceService;
//	
//	@Resource(name="customPriceService")
//	PriceStrategyService customPriceService;
	
	Map<PriceType, PriceStrategyService> priceStrategyServiceMap;
	
	@PostConstruct
	public void init() {
		priceStrategyServiceMap.put(PriceType.custom, customPriceService);
	}
	
	public RdfaResult<ElectricityPriceUnifiedDetailRespDto> queryUnifiedPrice(EletricityUnifiedReqDto eletricityUnifiedReqDto){
		PriceType priceType = PriceType.valueOf(eletricityUnifiedReqDto.getPriceType());

		PriceStrategyService service = priceStrategyServiceMap.get(priceType);
		
		return service.queryPrice(eletricityUnifiedReqDto);
		
	}
	
}
