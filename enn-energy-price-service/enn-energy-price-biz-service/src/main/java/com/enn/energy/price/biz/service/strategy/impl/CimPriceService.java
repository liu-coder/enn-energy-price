package com.enn.energy.price.biz.service.strategy.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enn.energy.price.biz.service.strategy.PriceStrategyService;
import com.enn.energy.price.client.dto.request.EletricityUnifiedReqDto;
import com.enn.energy.price.client.dto.response.ElectricityPriceUnifiedDetailRespDto;
import com.enn.energy.price.client.dto.response.ElectricityPriceValueDetailRespDTO.PriceDetail;
import com.enn.energy.price.common.error.ErrorCodeEnum;
import com.enn.energy.price.integration.cim.client.CimApiClient;
import com.enn.energy.price.integration.cim.dto.CimPriceReq;
import com.enn.energy.price.integration.cim.dto.CimPriceResp;
import com.enn.energy.price.integration.cim.dto.CimPriceResp.TimeSharing;
import com.enn.energy.price.integration.cim.dto.IdTypeConstant;
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

		cimPriceReq.setIdType(IdTypeConstant.DEVICE_IDTYPE);
		cimPriceReq.setBusinessId(eletricityUnifiedReqDto.getDeviceNumber());
		cimPriceReq.setDate(eletricityUnifiedReqDto.getEffectiveTime());
		cimPriceReq.setSystemCode(eletricityUnifiedReqDto.getTenantId());

		CimResponse<CimPriceResp>  response= CimApiClient.getDayEnergyPrice(cimPriceReq);
		if (response == null || !response.success()) {
			return new RdfaResult<ElectricityPriceUnifiedDetailRespDto>(false,
					ErrorCodeEnum.SELECT_SERVICE_UNACCESS_ERROR.getErrorCode(),
					ErrorCodeEnum.SELECT_SERVICE_UNACCESS_ERROR.getErrorMsg());
		}

		if (isSuccess(response)) {
			respDto = converResp(response);
		}else {
			cimPriceReq.setIdType(IdTypeConstant.COMPANY_IDTYPE);
			cimPriceReq.setBusinessId(eletricityUnifiedReqDto.getTenantId());
			cimPriceReq.setDate(eletricityUnifiedReqDto.getEffectiveTime());
			response= CimApiClient.getDayEnergyPrice(cimPriceReq);
			if (isSuccess(response)) {
				respDto = converResp(response);
			} else {
				respDto = new RdfaResult<ElectricityPriceUnifiedDetailRespDto>(false,
						ErrorCodeEnum.SELECT_DETAIL_VALID_ERROR.getErrorCode(),
						ErrorCodeEnum.SELECT_DETAIL_VALID_ERROR.getErrorMsg());
			}
		}
		
		return respDto;
	}

	private boolean isSuccess(CimResponse<CimPriceResp>  response) {
		return response.success() && response.getData() != null && response.getData().getPriceDataList() != null;
	}

	private RdfaResult<ElectricityPriceUnifiedDetailRespDto> converResp(CimResponse<CimPriceResp> cimResponse) {
		RdfaResult<ElectricityPriceUnifiedDetailRespDto> result = new RdfaResult<>();
		result.setCode(String.valueOf(cimResponse.getCode()));
		result.setMessage(cimResponse.getMsg());
		result.setSuccess(cimResponse.success());
		if (cimResponse.success() && cimResponse.getData() != null) {
			ElectricityPriceUnifiedDetailRespDto response = new ElectricityPriceUnifiedDetailRespDto();
			CimPriceResp cimPriceResp = cimResponse.getData();
			response.setBaseCapacityPrice(cimPriceResp.getDemandPrice());
			response.setMaxCapacityPrice(cimPriceResp.getDemandPrice());
			response.setPriceRate(cimPriceResp.getPriceRate());
			List<PriceDetail> priceDetailList = convertPriceDetail(cimPriceResp.getPriceDataList());
			if (priceDetailList.size() == 0) {
				//没有数据
				return new RdfaResult<ElectricityPriceUnifiedDetailRespDto>(false,
						ErrorCodeEnum.SELECT_DETAIL_VALID_ERROR.getErrorCode(),
						ErrorCodeEnum.SELECT_DETAIL_VALID_ERROR.getErrorMsg());
			}
			response.setPriceDetails(priceDetailList);
			result.setData(response);
		}

		return result;
	}

	private List<PriceDetail> convertPriceDetail(List<TimeSharing> timeSharingList) {
		List<PriceDetail> priceDetailList = new ArrayList<>();
		if (timeSharingList != null) {
			for (TimeSharing timeSharing : timeSharingList) {
				PriceDetail priceDetail = new PriceDetail();
				if (timeSharing.getPrice() == null) {
					continue;
				}
				priceDetail.setElePrice(timeSharing.getPrice());
				if (timeSharing.getTimeShareType() != null) {
					priceDetail.setPeriods(String.valueOf(Integer.valueOf(timeSharing.getTimeShareType()) - 1));
				} else {
					// 设置成无差别
					priceDetail.setPeriods("4");
				}
				priceDetail.setStartTime(timeSharing.getTimeShareStartDate());
				priceDetail.setEndTime(timeSharing.getTimeShareEndDate());
				if (timeSharing.getLadderStartValue() == null) {
					priceDetail.setStartStep("");
				} else {
					priceDetail.setStartStep(timeSharing.getLadderStartValue().toString());
				}

				if (timeSharing.getLadderEndValue() == null) {
					priceDetail.setEndStep("");
				} else {
					priceDetail.setEndStep(timeSharing.getLadderEndValue().toString());
				}

				priceDetail.setStep(timeSharing.getLadderName());
				priceDetailList.add(priceDetail);
			}
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
