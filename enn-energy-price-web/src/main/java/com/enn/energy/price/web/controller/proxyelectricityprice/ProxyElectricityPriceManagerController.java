package com.enn.energy.price.web.controller.proxyelectricityprice;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceVersionStructuresCreateBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceVersionUpdateBO;
import com.enn.energy.price.biz.service.proxyelectricityprice.ProxyElectricityPriceManagerService;
import com.enn.energy.price.web.convertMapper.ElectricityPriceVersionStructuresCreateReqVOMapper;
import com.enn.energy.price.web.convertMapper.ElectricityPriceVersionUpdateMapper;
import com.enn.energy.price.web.vo.requestvo.ElectricityPriceVersionStructuresCreateReqVO;
import com.enn.energy.price.web.vo.requestvo.ElectricityPriceVersionUpdateReqVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.rdfa.framework.biz.ro.RdfaResult;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 代理购电管理controller
 *
 * @author sunjidong
 * @date 2022/4/30
 **/
@Api(tags = "代理购电管理控制器")
@RestController
@RequestMapping("/ProxyElectricityPrice")
@Slf4j
public class ProxyElectricityPriceManagerController {

    @Resource
    private ProxyElectricityPriceManagerService proxyElectricityPriceManagerService;

    @Resource
    private ElectricityPriceVersionStructuresCreateReqVOMapper versionStructuresCreateReqVOMapper;


    /**
     * @describtion 创建电价版本
     * @author sunjidong
     * @date 2022/5/1 10:02
     * @param
     * @return
     */
    @PostMapping("/createPriceVersion")
    @ApiOperation("创建电价版本")
    public RdfaResult<Boolean> createPriceVersion(ElectricityPriceVersionStructuresCreateReqVO priceVersionStructuresCreateVO){
        ElectricityPriceVersionStructuresCreateBO versionStructuresCreateBO = versionStructuresCreateReqVOMapper.priceVersionStructuresCreateReqVOToBO(priceVersionStructuresCreateVO);
        Boolean ifSuccess = proxyElectricityPriceManagerService.createPriceVersionStructures(versionStructuresCreateBO);
        return RdfaResult.success(ifSuccess);
    }

    @PostMapping("/updatePriceVersion")
    @ApiOperation( "修改电价版本" )
    public RdfaResult updatePriceVersion(@RequestBody @Valid ElectricityPriceVersionUpdateReqVO electricityPriceVersionUpdateReqVO){
        ElectricityPriceVersionUpdateBO electricityPriceVersionUpdateBO = ElectricityPriceVersionUpdateMapper.INSTANCE.electricityPriceVersionUpdateReqVOTOBO( electricityPriceVersionUpdateReqVO );
        RdfaResult result = proxyElectricityPriceManagerService.updatePriceVersion( electricityPriceVersionUpdateBO );
        return result;
    }
}