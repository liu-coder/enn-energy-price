package com.enn.energy.price.client.dto.request;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 统一电价查询dto接口
 * @author wenjianping
 *
 */
@Data
public class EletricityUnifiedReqDto {

	/**
	 * 电价类型
	 */
	private String type;
	
	/**
	 * 企业编码
	 */
	private String systemCode;
	
	/**
	 * 设备id,设备协同中的cimId
	 */
    @NotBlank(message = "设备ID不能为空")
    private String equipmentId;
	
	/**
	 * 查询日期,格式：
	 */
    @ApiModelProperty(value = "查询日期", example = "2021-11-19", required = true)
    @NotNull(message = "查询日期必填")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Future(message = "查询日期必须大于等于今天")
	private String date;
}
