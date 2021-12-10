package com.enn.energy.price.biz.service.strategy.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enn.energy.price.biz.service.strategy.PriceStrategyService;
import com.enn.energy.price.client.dto.request.ElectricityPriceValueReqDTO;
import com.enn.energy.price.client.dto.request.EletricityUnifiedReqDto;
import com.enn.energy.price.client.dto.response.ElectricityPriceUnifiedDetailRespDto;
import com.enn.energy.price.client.dto.response.ElectricityPriceValueDetailRespDTO;
import com.enn.energy.price.client.service.ElectricityPriceSelectService;

import top.rdfa.framework.biz.ro.RdfaResult;

/**
 * 自定义电价
 * @author xinao
 *
 */
@Service("customPriceService")
public class CustomPriceService implements PriceStrategyService {

	@Autowired
	ElectricityPriceSelectService electricityPriceSelectService;

	public RdfaResult<ElectricityPriceUnifiedDetailRespDto> queryPrice(
			EletricityUnifiedReqDto eletricityUnifiedReqDto) {

		ElectricityPriceValueReqDTO electricityPriceValueReqDTO = converReq(eletricityUnifiedReqDto);
		RdfaResult<ElectricityPriceValueDetailRespDTO> sepecialResult = electricityPriceSelectService
				.selectElePrice(electricityPriceValueReqDTO);
		RdfaResult<ElectricityPriceUnifiedDetailRespDto> respDto = converCustom(sepecialResult);
		return respDto;
	}

	private RdfaResult<ElectricityPriceUnifiedDetailRespDto> converCustom(
			RdfaResult<ElectricityPriceValueDetailRespDTO> sepecialResult) {
		// TODO Auto-generated method stub
		return null;
	}

	private ElectricityPriceValueReqDTO converReq(EletricityUnifiedReqDto eletricityUnifiedReqDto) {
		ElectricityPriceValueReqDTO electricityPriceValueReqDTO = new ElectricityPriceValueReqDTO();
		electricityPriceValueReqDTO.setSystemCode(eletricityUnifiedReqDto.getTenantId());
		electricityPriceValueReqDTO.setEquipmentId(eletricityUnifiedReqDto.getDeviceNumber());
		electricityPriceValueReqDTO.setEffectiveTime(eletricityUnifiedReqDto.getEffectiveTime());
		return electricityPriceValueReqDTO;
	}
}
