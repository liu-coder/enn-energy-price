package com.enn.energy.price.biz.service.strategy.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enn.energy.price.biz.service.strategy.PriceStrategyService;
import com.enn.energy.price.client.dto.request.EletricityUnifiedReqDto;
import com.enn.energy.price.client.dto.response.ElectricityPriceUnifiedDetailRespDto;
import com.enn.energy.price.client.dto.response.ElectricityPriceValueDetailRespDTO.PriceDetail;
import com.enn.energy.price.integration.cim.client.CimApiClient;
import com.enn.energy.price.integration.cim.dto.CimPriceReq;
import com.enn.energy.price.integration.cim.dto.CimPriceResp;
import com.enn.energy.price.integration.cim.dto.CimPriceResp.TimeSharing;
import com.enn.energy.price.integration.cimzuul.dto.CimResponse;

import top.rdfa.framework.biz.ro.RdfaResult;

/**
 * 去cim查询电价
 * 
 * @author wenjianping
 *
 */
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
		result.setCode(String.valueOf(cimResponse.getCode()));
		result.setMessage(cimResponse.getMsg());
		result.setSuccess(cimResponse.success());
		if (cimResponse.success()) {
			ElectricityPriceUnifiedDetailRespDto response = new ElectricityPriceUnifiedDetailRespDto();
			CimPriceResp cimPriceResp = cimResponse.getData();
			response.setBaseCapacityPrice(cimPriceResp.getDemandPrice());
			response.setMaxCapacityPrice(cimPriceResp.getDemandPrice());
			response.setPriceRate(cimPriceResp.getPriceRate());
			List<PriceDetail> priceDetailList = convertPriceDetail(cimPriceResp.getPriceDataList());
			response.setPriceDetails(priceDetailList);
			result.setData(response);
		}

		return result;
	}

	private List<PriceDetail> convertPriceDetail(List<TimeSharing> timeSharingList) {
		List<PriceDetail> priceDetailList = new ArrayList<>();
		for (TimeSharing timeSharing : timeSharingList) {
			PriceDetail priceDetail = new PriceDetail();
			priceDetail.setElePrice(timeSharing.getPrice());
			priceDetail.setPeriods(String.valueOf(Integer.valueOf(timeSharing.getTimeShareType()) - 1));
			priceDetail.setStartTime(timeSharing.getTimeShareStartDate());
			priceDetail.setEndTime(timeSharing.getTimeShareEndDate());
			priceDetail.setStartStep(String.valueOf(timeSharing.getLadderStartValue()));
			priceDetail.setEndStep(String.valueOf(timeSharing.getLadderEndValue()));
			priceDetail.setStep(timeSharing.getLadderName());
			priceDetailList.add(priceDetail);
		}
		return priceDetailList;
	}

	private CimPriceReq converReq(EletricityUnifiedReqDto eletricityUnifiedReqDto) {
		CimPriceReq cimPriceReq = new CimPriceReq();
		cimPriceReq.setBusinessId(eletricityUnifiedReqDto.getTenantId());
		cimPriceReq.setDate(eletricityUnifiedReqDto.getEffectiveTime());
		return cimPriceReq;
	}

}
