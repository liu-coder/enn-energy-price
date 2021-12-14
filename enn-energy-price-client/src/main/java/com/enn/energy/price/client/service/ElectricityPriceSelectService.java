package com.enn.energy.price.client.service;

import com.enn.energy.price.client.dto.request.ElectricityPriceValueReqDTO;
import com.enn.energy.price.client.dto.request.ElectricityPriceVersionDetailReqDTO;
import com.enn.energy.price.client.dto.request.ElectricityPriceVersionsReqDTO;
import com.enn.energy.price.client.dto.response.ElectricityPriceValueDetailRespDTO;
import com.enn.energy.price.client.dto.response.ElectricityPriceVersionDetailRespDTO;
import com.enn.energy.price.client.dto.response.ElectricityPriceVersionsRespDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import top.rdfa.framework.biz.ro.PagedRdfaResult;
import top.rdfa.framework.biz.ro.RdfaResult;

import java.text.ParseException;

/**
 * 价格、版本等相关查询
 */
@FeignClient(name = "ENN-ENERGY-PRICE")
public interface ElectricityPriceSelectService {
    /**
     * 查询电价
     * @param electricityPriceValueReqDTO
     * @return
     */
    @PostMapping(value = "/price/electricityPrice")
    @ApiOperation("自定义电价查询")
    RdfaResult<ElectricityPriceValueDetailRespDTO> selectElePrice(ElectricityPriceValueReqDTO electricityPriceValueReqDTO);

    /**
     * 查询版本列表
     * @param versionsReqDTO
     * @return
     */
    @PostMapping(value = "/price/selectVersions")
    @ApiOperation("自定价版本列表查询")
    PagedRdfaResult<ElectricityPriceVersionsRespDTO> selectVersions(ElectricityPriceVersionsReqDTO versionsReqDTO) throws ParseException;

    /**
     * 查询版本详情
     * @param detailReqDTO
     * @return
     * @throws ParseException
     */
    @PostMapping(value = "/price/versionDetail")
    @ApiOperation("自定义版本详情信息查询")
    RdfaResult<ElectricityPriceVersionDetailRespDTO> versionDetail(ElectricityPriceVersionDetailReqDTO detailReqDTO) throws Exception;

}
