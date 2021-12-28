package com.enn.energy.price.integration.cim.dto;

import lombok.Data;

@Data
/**
 * cim电价查询请求对象，详见：CimApiClient
 * 
 * @author xinao
 *
 */
public class CimPriceReq {
	/**
	 * 业务ID.（01:企业ID、02:户号ID、03:设备id）
	 */
	private String businessId;
	/**
	 * 查询时间 yyyy-MM-dd HH:mm:ss || yyyy-MM-dd
	 */
	private String date;
	
	/**
	 * 查询的业务ID类型，类型：01企业、02户号、03设备id
	 */
	private String idType;
	
	private String systemCode;



}
