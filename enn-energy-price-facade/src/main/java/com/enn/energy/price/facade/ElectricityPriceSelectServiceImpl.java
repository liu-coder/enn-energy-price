package com.enn.energy.price.facade;

import com.enn.energy.price.client.dto.response.*;
import com.enn.energy.price.client.dto.request.*;
import com.enn.energy.price.client.service.ElectricityPriceSelectService;
import com.enn.energy.price.facade.handler.ElectricityPriceSelectHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.rdfa.framework.biz.ro.PagedRdfaResult;
import top.rdfa.framework.biz.ro.RdfaResult;
import java.text.ParseException;

/**
 * @author ：chenchangtong
 * @date ：Created 2021/11/30 10:19
 * @description：快乐工作每一天
 */
@Api(tags = {"自定义电价查询服务"})
@RestController
@RequestMapping("/price")
@Slf4j
@DubboService(version = "1.0.0", protocol = {"dubbo"})
public class ElectricityPriceSelectServiceImpl implements ElectricityPriceSelectService {

    @Autowired
    private ElectricityPriceSelectHandler electricityPriceSelectHandler;

    @PostMapping("/electricityPrice")
    @Override
    public RdfaResult<ElectricityPriceValueDetailRespDTO> selectElePrice(@Validated @RequestBody ElectricityPriceValueReqDTO requestDto) {
        return electricityPriceSelectHandler.selectElePrice(requestDto);
    }

    @PostMapping(value = "/selectVersions")
    @Override
    public PagedRdfaResult<ElectricityPriceVersionsRespDTO> selectVersions(@Validated @RequestBody ElectricityPriceVersionsReqDTO requestDto) throws ParseException {
        return electricityPriceSelectHandler.selectVersions(requestDto);
    }

    /**
     * 查询版本详情
     * @param detailReqDTO
     * @return
     * @throws ParseException
     */
    @PostMapping(value = "/versionDetail")
    @Override
    public RdfaResult<ElectricityPriceVersionDetailRespDTO> versionDetail(@Validated @RequestBody ElectricityPriceVersionDetailReqDTO detailReqDTO){
        return electricityPriceSelectHandler.versionDetail(detailReqDTO.getEquipmentId(),detailReqDTO.getSystemCode(),detailReqDTO.getVersionId());
    }

    @PostMapping(value = "/currentVersionDetail")
    @ApiOperation("自定义版本当前生效版本详情")
    @Override
    public RdfaResult<ElectricityPriceVersionDetailRespDTO> currentVersionDetail(@Validated @RequestBody ElectricityPriceCurrentVersionDetailReqDTO reqDTO) {
        RdfaResult<ElectricityPriceVersionDetailRespDTO> rdfaResult = electricityPriceSelectHandler.currentVersionDetail(reqDTO);
        return rdfaResult;
    }


}
