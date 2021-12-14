package com.enn.energy.price.biz.service.strategy.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enn.energy.price.biz.service.strategy.PriceStrategyService;
import com.enn.energy.price.client.dto.request.EletricityUnifiedReqDto;
import com.enn.energy.price.client.dto.response.ElectricityPriceUnifiedDetailRespDto;
import com.enn.energy.price.integration.cim.client.CimApiClient;
import com.enn.energy.price.integration.cim.dto.CimPriceReq;
import com.enn.energy.price.integration.cim.dto.CimPriceResp;
import com.enn.energy.price.integration.cimzuul.dto.CimResponse;

import top.rdfa.framework.biz.ro.RdfaResult;

@Service("cimPriceService")
public class CimPriceService implements  PriceStrategyService{

	@Autowired
	CimApiClient CimApiClient;
	
	@Override
	public RdfaResult<ElectricityPriceUnifiedDetailRespDto> queryPrice(
			EletricityUnifiedReqDto eletricityUnifiedReqDto) {
		RdfaResult<ElectricityPriceUnifiedDetailRespDto> respDto = null;
		
		CimPriceReq cimPriceReq = new CimPriceReq();

		cimPriceReq.setIdType("03");
		cimPriceReq.setBusinessId(eletricityUnifiedReqDto.getDeviceNumber());
		cimPriceReq.setDate(eletricityUnifiedReqDto.getEffectiveTime());
		cimPriceReq.setSystemCode(eletricityUnifiedReqDto.getTenantId());


		
		CimResponse<CimPriceResp>  response= CimApiClient.getDayEnergyPrice(cimPriceReq);
		if(response.getData() != null) {

			respDto = converResp(response);
		}else {
			cimPriceReq.setIdType("01");
			cimPriceReq.setBusinessId(eletricityUnifiedReqDto.getTenantId());
			cimPriceReq.setDate(eletricityUnifiedReqDto.getEffectiveTime());
			response= CimApiClient.getDayEnergyPrice(cimPriceReq);
			if(response.getData() != null) {
				respDto = converResp(response);
			}
		}
		
		return respDto;
	}

	private RdfaResult<ElectricityPriceUnifiedDetailRespDto> converResp(CimResponse<CimPriceResp> cimResponse) {
		RdfaResult<ElectricityPriceUnifiedDetailRespDto> result = new RdfaResult<>();
		ElectricityPriceUnifiedDetailRespDto response = new ElectricityPriceUnifiedDetailRespDto();
		CimPriceResp cimPriceResp = cimResponse.getData();
//		response.setBaseCapacityPrice(cimPriceResp.getDemandPrice());
		return null;
	}

	private CimPriceReq converReq(EletricityUnifiedReqDto eletricityUnifiedReqDto) {
		CimPriceReq cimPriceReq = new CimPriceReq();
		cimPriceReq.setBusinessId(eletricityUnifiedReqDto.getTenantId());
		cimPriceReq.setDate(eletricityUnifiedReqDto.getEffectiveTime());
		return cimPriceReq;
	}

}
