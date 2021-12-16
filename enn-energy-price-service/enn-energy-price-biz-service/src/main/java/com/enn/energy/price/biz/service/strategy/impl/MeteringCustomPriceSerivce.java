package com.enn.energy.price.biz.service.strategy.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enn.energy.price.biz.service.strategy.PriceStrategyService;
import com.enn.energy.price.client.dto.request.EletricityUnifiedReqDto;
import com.enn.energy.price.client.dto.response.ElectricityPriceUnifiedDetailRespDto;
import com.enn.energy.price.client.dto.response.ElectricityPriceValueDetailRespDTO.PriceDetail;
import com.enn.energy.price.integration.cimzuul.dto.CimResponse;
import com.enn.energy.price.integration.meteringprice.client.MeteringPriceClient;
import com.enn.energy.price.integration.meteringprice.dto.MeteringPriceReqDto;
import com.enn.energy.price.integration.meteringprice.dto.MeteringPriceRespDto;

import top.rdfa.framework.biz.ro.RdfaResult;

@Service("meteringCustomPriceService")
public class MeteringCustomPriceSerivce implements PriceStrategyService {

	@Autowired
	MeteringPriceClient meteringPriceClient;
	
	@Override
	public RdfaResult<ElectricityPriceUnifiedDetailRespDto> queryPrice(
			EletricityUnifiedReqDto eletricityUnifiedReqDto) {
		
		MeteringPriceReqDto meteringPriceReqDto = new MeteringPriceReqDto();
		
		meteringPriceReqDto.setDate(eletricityUnifiedReqDto.getEffectiveTime() + " 00:00:00");
		meteringPriceReqDto.setDeviceId(eletricityUnifiedReqDto.getDeviceNumber());
		meteringPriceReqDto.setSystemCode(eletricityUnifiedReqDto.getTenantId());
		
		CimResponse<List<MeteringPriceRespDto>> response = meteringPriceClient.queryMeteringPrice(meteringPriceReqDto);
		
		RdfaResult<ElectricityPriceUnifiedDetailRespDto> result = convert(response);
				
		return result;
	}

	private RdfaResult<ElectricityPriceUnifiedDetailRespDto> convert(
			CimResponse<List<MeteringPriceRespDto>> cimRespDto) {
		RdfaResult<ElectricityPriceUnifiedDetailRespDto> result = new RdfaResult<ElectricityPriceUnifiedDetailRespDto>();
		if(cimRespDto == null) {
			result.setCode("404");
			result.setSuccess(false);
			result.setMessage("find metering-prepaid price fail");
			result.setData(null);
			return result;
		}
		if (cimRespDto.getCode() != 200 || cimRespDto.getData() == null) {
			result.setCode(String.valueOf(cimRespDto.getCode()));
			result.setSuccess(false);
			result.setMessage(cimRespDto.getMsg());
			result.setData(null);
			return result;
		}
		
		result.setCode(String.valueOf(cimRespDto.getCode()));
		result.setSuccess(true);
		result.setMessage(cimRespDto.getMsg());
		ElectricityPriceUnifiedDetailRespDto response = new ElectricityPriceUnifiedDetailRespDto();
		List<MeteringPriceRespDto> meteringPriceRespDto = cimRespDto.getData();
		List<PriceDetail> priceDetailList = convertList(meteringPriceRespDto);
		response.setPriceDetails(priceDetailList);
		result.setData(response);
		return result;
	}

	private List<PriceDetail> convertList(List<MeteringPriceRespDto> meteringPriceRespDtoList) {
		
		List<PriceDetail> priceDetailList = new ArrayList<>();
		for (MeteringPriceRespDto meteringPriceRespDto : meteringPriceRespDtoList) {
			PriceDetail priceDetail = new PriceDetail();
			priceDetail.setElePrice(new BigDecimal(meteringPriceRespDto.getPrice()));
			priceDetail.setEndTime(meteringPriceRespDto.getTimeShareEndDate());
			priceDetail.setStartTime(meteringPriceRespDto.getTimeShareStartDate());
			priceDetail.setPeriods(String.valueOf(Integer.valueOf(meteringPriceRespDto.getTimeShareType()) - 1));
			priceDetailList.add(priceDetail);
		}
		
		return priceDetailList;
	}

}

