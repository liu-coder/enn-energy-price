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
import com.enn.energy.price.common.error.ErrorCodeEnum;
import com.enn.energy.price.integration.cimzuul.dto.CimResponse;
import com.enn.energy.price.integration.meteringprice.client.MeteringPriceClient;
import com.enn.energy.price.integration.meteringprice.dto.MeteringPriceReqDto;
import com.enn.energy.price.integration.meteringprice.dto.MeteringPriceRespDto;

import top.rdfa.framework.biz.ro.RdfaResult;

/**
 * 单一电价平台查询自定义电价
 * 
 * @author wenjianpinga
 *
 */
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
		if (cimRespDto == null || !cimRespDto.success()) {
			return new RdfaResult<ElectricityPriceUnifiedDetailRespDto>(false,
					ErrorCodeEnum.SELECT_SERVICE_UNACCESS_ERROR.getErrorCode(),
					ErrorCodeEnum.SELECT_SERVICE_UNACCESS_ERROR.getErrorMsg());
		}
		if (cimRespDto.getCode() != 200 || cimRespDto.getData() == null  ) {
			return new RdfaResult<ElectricityPriceUnifiedDetailRespDto>(false, String.valueOf(cimRespDto.getCode()),
					cimRespDto.getMsg());
		}
		
		List<MeteringPriceRespDto> meteringPriceRespDto = cimRespDto.getData();
		List<PriceDetail> priceDetailList = convertList(meteringPriceRespDto);
		if (priceDetailList.size() == 0) {
			return new RdfaResult<ElectricityPriceUnifiedDetailRespDto>(false,
					ErrorCodeEnum.SELECT_DETAIL_VALID_ERROR.getErrorCode(),
					ErrorCodeEnum.SELECT_DETAIL_VALID_ERROR.getErrorMsg());
		}

		RdfaResult<ElectricityPriceUnifiedDetailRespDto> result = new RdfaResult<ElectricityPriceUnifiedDetailRespDto>();
		ElectricityPriceUnifiedDetailRespDto response = new ElectricityPriceUnifiedDetailRespDto();

		result.setCode(String.valueOf(cimRespDto.getCode()));
		result.setSuccess(true);
		result.setMessage(cimRespDto.getMsg());
		response.setPriceDetails(priceDetailList);
		result.setData(response);
		return result;
	}

	private List<PriceDetail> convertList(List<MeteringPriceRespDto> meteringPriceRespDtoList) {
		
		List<PriceDetail> priceDetailList = new ArrayList<>();
		for (MeteringPriceRespDto meteringPriceRespDto : meteringPriceRespDtoList) {
			PriceDetail priceDetail = new PriceDetail();
			if (meteringPriceRespDto.getPrice() == null) {
				// 价格为空，调过对应数据
				continue;
			}
			priceDetail.setElePrice(new BigDecimal(meteringPriceRespDto.getPrice()));
			String endStr = meteringPriceRespDto.getTimeShareEndDate();
			if (endStr != null && endStr.length() >= 16) {
				endStr = endStr.substring(11, 16);
				if ("00:00".equals(endStr)) {
					endStr = "24:00";
				}
			}
			priceDetail.setEndTime(endStr);
			String startStr = meteringPriceRespDto.getTimeShareStartDate();
			if (startStr != null && startStr.length() >= 16) {
				startStr = startStr.substring(11, 16);
			}
			priceDetail.setStartTime(startStr);
			priceDetail.setPeriods(String.valueOf(Integer.valueOf(meteringPriceRespDto.getTimeShareType()) - 1));
			priceDetailList.add(priceDetail);
		}
		
		return priceDetailList;
	}

}

