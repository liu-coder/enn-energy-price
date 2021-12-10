package com.enn.energy.price.integration.meteringprice.dto;

import lombok.Data;


@Data
public class MeteringPriceRespDto {

	private String timeShareStartDate;
	
	private String timeShareEndDate;
	
	private String timeShareType;
	
	private String price;

}
