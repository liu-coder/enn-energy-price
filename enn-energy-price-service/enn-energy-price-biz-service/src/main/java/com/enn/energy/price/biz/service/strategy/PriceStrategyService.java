package com.enn.energy.price.biz.service.strategy;

import com.enn.energy.price.client.dto.request.EletricityUnifiedReqDto;
import com.enn.energy.price.client.dto.response.ElectricityPriceUnifiedDetailRespDto;

import top.rdfa.framework.biz.ro.RdfaResult;

/**
 * 统一的查询电价服务
 * 
 * @author wenjianping
 *
 */
public interface PriceStrategyService {
	/**
	 * 查询电价接口
	 * 
	 * @param eletricityUnifiedReqDto
	 * @return
	 */
	public RdfaResult<ElectricityPriceUnifiedDetailRespDto> queryPrice(EletricityUnifiedReqDto eletricityUnifiedReqDto);

}
