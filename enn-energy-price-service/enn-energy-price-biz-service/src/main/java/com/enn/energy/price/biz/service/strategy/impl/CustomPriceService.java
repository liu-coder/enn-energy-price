package com.enn.energy.price.biz.service.strategy.impl;

import com.enn.energy.price.biz.service.strategy.PriceStrategyService;
import com.enn.energy.price.client.dto.request.ElectricityPriceValueReqDTO;
import com.enn.energy.price.client.dto.request.EletricityUnifiedReqDto;
import com.enn.energy.price.client.dto.response.ElectricityPriceUnifiedDetailRespDto;
import com.enn.energy.price.client.dto.response.ElectricityPriceValueDetailRespDTO;
import com.enn.energy.price.client.service.ElectricityPriceSelectService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.rdfa.framework.biz.ro.RdfaResult;

import java.lang.reflect.InvocationTargetException;

/**
 * 自定义电价
 * @author xinao
 *
 */
@Slf4j
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
		ElectricityPriceValueDetailRespDTO electricityPriceValueDetailRespDTO = sepecialResult.getData();
		ElectricityPriceUnifiedDetailRespDto electricityPriceUnifiedDetailRespDto = new ElectricityPriceUnifiedDetailRespDto();
		RdfaResult<ElectricityPriceUnifiedDetailRespDto> rdfaResult = new RdfaResult<ElectricityPriceUnifiedDetailRespDto>();

		try {
			BeanUtils.copyProperties(electricityPriceUnifiedDetailRespDto, electricityPriceValueDetailRespDTO);
			rdfaResult.setCode(sepecialResult.getCode());
			rdfaResult.setMessage(sepecialResult.getMessage());
			rdfaResult.setData(electricityPriceUnifiedDetailRespDto);
			rdfaResult.success(sepecialResult.isSuccess());

		} catch (IllegalAccessException | InvocationTargetException e) {
			log.error("copy properties error:", e);
		}
		

		return rdfaResult;
	}

	private ElectricityPriceValueReqDTO converReq(EletricityUnifiedReqDto eletricityUnifiedReqDto) {
		ElectricityPriceValueReqDTO electricityPriceValueReqDTO = new ElectricityPriceValueReqDTO();
		electricityPriceValueReqDTO.setSystemCode(eletricityUnifiedReqDto.getTenantId());
		electricityPriceValueReqDTO.setEquipmentId(eletricityUnifiedReqDto.getDeviceNumber());
		electricityPriceValueReqDTO.setEffectiveTime(eletricityUnifiedReqDto.getEffectiveTime());
		return electricityPriceValueReqDTO;
	}
}
