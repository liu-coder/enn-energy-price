package com.enn.energy.price.integration.cim.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class CimPriceResp {

	private String businessId;
	
	private String date;
	
	private BigDecimal demandPrice;
	
	private String idType;

	private String priceType;

	private BigDecimal priceRate;
	
	private String respDataCode;
	
	private List<TimeSharing> priceDataList;
	
	@Data
	public static class TimeSharing {
		private BigDecimal ladderEndValue;
		
		private String ladderName;
		
		private BigDecimal ladderStartValue;
		
		private BigDecimal price;
		
		private String timeShareEndDate;
		
		private String timeShareLevel;
		
		private String timeShareStartDate;
		
		private String timeShareType;
		
	}
}
