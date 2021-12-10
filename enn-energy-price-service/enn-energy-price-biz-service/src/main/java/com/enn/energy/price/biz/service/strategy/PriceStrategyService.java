package com.enn.energy.price.biz.service.strategy;

import com.enn.energy.price.client.dto.request.EletricityUnifiedReqDto;
import com.enn.energy.price.client.dto.response.ElectricityPriceUnifiedDetailRespDto;

import top.rdfa.framework.biz.ro.RdfaResult;

public interface PriceStrategyService {
	public RdfaResult<ElectricityPriceUnifiedDetailRespDto> queryPrice(EletricityUnifiedReqDto eletricityUnifiedReqDto);

}
