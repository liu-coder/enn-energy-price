package com.enn.energy.price.biz.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.enn.energy.price.biz.service.aop.MyCacheable;
import com.enn.energy.price.biz.service.strategy.PriceStrategyService;
import com.enn.energy.price.client.dto.request.EletricityUnifiedReqDto;
import com.enn.energy.price.client.dto.response.ElectricityPriceUnifiedDetailRespDto;
import com.enn.energy.price.common.enums.PriceType;

import top.rdfa.framework.biz.ro.RdfaResult;
import top.rdfa.framework.cache.api.CacheClient;

@Service
//@Slf4j
public class ElectricityPriceUnifiedService {
	

	
	@Resource(name="customPriceService")
	PriceStrategyService customPriceService;
	
	
	@Resource(name="cimPriceService")
	PriceStrategyService cimPriceService;
	
	@Resource(name="meteringCustomPriceService")
	PriceStrategyService meteringCustomPriceService;

	@Resource
	CacheClient cacheClient;

	
	Map<PriceType, PriceStrategyService> priceStrategyServiceMap = new HashMap<>();
	
	@PostConstruct
	public void init() {
		priceStrategyServiceMap.put(PriceType.custom, customPriceService);
		priceStrategyServiceMap.put(PriceType.catalogue, cimPriceService);
		priceStrategyServiceMap.put(PriceType.meteringCustom, meteringCustomPriceService);
	}
	
	@MyCacheable(key = "#eletricityUnifiedReqDto.priceType,#eletricityUnifiedReqDto.tenantId,#eletricityUnifiedReqDto.deviceNumber,#eletricityUnifiedReqDto.effectiveTime", timeout = 24
			* 60 * 60)
	public RdfaResult<ElectricityPriceUnifiedDetailRespDto> queryUnifiedPrice(EletricityUnifiedReqDto eletricityUnifiedReqDto){
		PriceType priceType = PriceType.valueOf(eletricityUnifiedReqDto.getPriceType());

//		ElectricityPriceUnifiedDetailRespDto resultDto = cacheClient.vGet(getKey(eletricityUnifiedReqDto),
//				CommonConstant.CACHE_PREFIX);
//		if (resultDto != null) {
//			log.info("get data from redis for queryUnifiedPrice, {}", eletricityUnifiedReqDto);
//			return newResult(resultDto);
//		}
		PriceStrategyService service = priceStrategyServiceMap.get(priceType);
		if(service != null) {
			RdfaResult<ElectricityPriceUnifiedDetailRespDto> result = service.queryPrice(eletricityUnifiedReqDto);
//			if (result != null && result.getData() != null) {
//				cacheClient.vSetWithTimeOut(getKey(eletricityUnifiedReqDto), CommonConstant.CACHE_PREFIX,
//						result.getData(), 60 * 1000);
//			}

			return result;
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
	
	private RdfaResult<ElectricityPriceUnifiedDetailRespDto> newResult(
			ElectricityPriceUnifiedDetailRespDto electricityPriceUnifiedDetailRespDto) {

		return new RdfaResult<ElectricityPriceUnifiedDetailRespDto>(true, "0", "",
				electricityPriceUnifiedDetailRespDto);
	}

	private String getKey(EletricityUnifiedReqDto eletricityUnifiedReqDto) {
		return eletricityUnifiedReqDto.getPriceType() + "-" + eletricityUnifiedReqDto.getTenantId() + "-"
				+ eletricityUnifiedReqDto.getDeviceNumber() + "-" + eletricityUnifiedReqDto.getEffectiveTime();
	}

}
