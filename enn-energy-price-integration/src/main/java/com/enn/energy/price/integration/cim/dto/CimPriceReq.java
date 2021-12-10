package com.enn.energy.price.integration.cim.dto;

import lombok.Data;

@Data
public class CimPriceReq {

	private String businessId;
	
	private String date;
	
	private String idType;
	
	private String systemCode;
}
