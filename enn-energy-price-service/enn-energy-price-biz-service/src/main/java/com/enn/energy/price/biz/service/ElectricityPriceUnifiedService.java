<<<<<<< HEAD
package com.enn.energy.price.biz.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.enn.energy.price.biz.service.strategy.PriceStrategyService;
import com.enn.energy.price.client.dto.request.EletricityUnifiedReqDto;
import com.enn.energy.price.client.dto.response.ElectricityPriceUnifiedDetailRespDto;
import com.enn.energy.price.common.enums.PriceType;

import top.rdfa.framework.biz.ro.RdfaResult;

@Service
public class ElectricityPriceUnifiedService {
	

	
	@Resource(name="customPriceService")
	PriceStrategyService customPriceService;
	
	
	@Resource(name="cimPriceService")
	PriceStrategyService cimPriceService;
	
	@Resource(name="meteringCustomPriceService")
	PriceStrategyService meteringCustomPriceService;

	
	Map<PriceType, PriceStrategyService> priceStrategyServiceMap = new HashMap<>();
	
	@PostConstruct
	public void init() {
		priceStrategyServiceMap.put(PriceType.custom, customPriceService);
		priceStrategyServiceMap.put(PriceType.catalogue, cimPriceService);
		priceStrategyServiceMap.put(PriceType.meteringCustom, meteringCustomPriceService);
	}
	
	public RdfaResult<ElectricityPriceUnifiedDetailRespDto> queryUnifiedPrice(EletricityUnifiedReqDto eletricityUnifiedReqDto){
		PriceType priceType = PriceType.valueOf(eletricityUnifiedReqDto.getPriceType());

		PriceStrategyService service = priceStrategyServiceMap.get(priceType);
		if(service != null) {
			return service.queryPrice(eletricityUnifiedReqDto);
		}

		return nullResult();
	}
	
	private RdfaResult<ElectricityPriceUnifiedDetailRespDto> nullResult(){
		RdfaResult<ElectricityPriceUnifiedDetailRespDto> rdfaResult = new RdfaResult<>();
		rdfaResult.setCode("404");
		rdfaResult.setSuccess(false);
		rdfaResult.setMessage("");
		return rdfaResult;
	}
	
}
=======
//package com.enn.energy.price.biz.service;
//
//import java.util.Map;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.Resource;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.enn.energy.price.biz.service.strategy.PriceStrategyService;
//import com.enn.energy.price.client.dto.request.EletricityUnifiedReqDto;
//import com.enn.energy.price.client.dto.response.ElectricityPriceUnifiedDetailRespDto;
//import com.enn.energy.price.client.service.ElectricityPriceSelectService;
//import com.enn.energy.price.common.enums.PriceType;
//
//import top.rdfa.framework.biz.ro.RdfaResult;
//
//@Service
//public class ElectricityPriceUnifiedService {
//
//
//
//	@Resource(name="customPriceService")
//	PriceStrategyService customPriceService;
//
//
//	@Resource(name="cimPriceService")
//	PriceStrategyService cimPriceService;
//
//	@Resource(name="meteringCustomPriceSerivce")
//	PriceStrategyService meteringCustomPriceSerivce;
//
//
//	Map<PriceType, PriceStrategyService> priceStrategyServiceMap;
//
//	@PostConstruct
//	public void init() {
//		priceStrategyServiceMap.put(PriceType.custom, customPriceService);
//		priceStrategyServiceMap.put(PriceType.catalogue, cimPriceService);
//		priceStrategyServiceMap.put(PriceType.meteringCustom, meteringCustomPriceSerivce);
//	}
//
//	public RdfaResult<ElectricityPriceUnifiedDetailRespDto> queryUnifiedPrice(EletricityUnifiedReqDto eletricityUnifiedReqDto){
//		PriceType priceType = PriceType.valueOf(eletricityUnifiedReqDto.getPriceType());
//
//		PriceStrategyService service = priceStrategyServiceMap.get(priceType);
//		if(service != null) {
//			return service.queryPrice(eletricityUnifiedReqDto);
//		}
//
//		return nullResult();
//	}
//
//	private RdfaResult<ElectricityPriceUnifiedDetailRespDto> nullResult(){
//		RdfaResult<ElectricityPriceUnifiedDetailRespDto> rdfaResult = new RdfaResult<>();
//		rdfaResult.setCode("404");
//		rdfaResult.setSuccess(false);
//		rdfaResult.setMessage("");
//		return rdfaResult;
//	}
//
//}
>>>>>>> lawson_20211221V2.0
