package com.enn.energy.price.web.controller.proxyelectricityprice;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.enn.energy.price.biz.service.bo.proxyprice.*;
import com.enn.energy.price.biz.service.proxyelectricityprice.ProxyElectricityPriceManagerBakService;
import com.enn.energy.price.biz.service.proxyelectricityprice.ProxyElectricityPriceManagerService;
import com.enn.energy.price.common.constants.CommonConstant;
import com.enn.energy.price.common.error.ErrorCodeEnum;
import com.enn.energy.price.common.error.PriceException;
import com.enn.energy.price.web.convertMapper.ElectricityPriceVersionCreateBOConvertMapper;
import com.enn.energy.price.web.convertMapper.ElectricityPriceVersionUpdateMapper;
import com.enn.energy.price.web.vo.requestvo.*;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceStructureAndRuleValidateRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.rdfa.framework.biz.ro.RdfaResult;
import top.rdfa.framework.concurrent.api.exception.LockFailException;
import top.rdfa.framework.concurrent.redis.lock.RedissonRedDisLock;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.Lock;

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

    @Value("${system.tenantId}")
    private String tenantId;

    @Value("${system.tenantName}")
    private String tenantName;

    @Resource
    private ProxyElectricityPriceManagerService proxyElectricityPriceManagerService;

    @Resource
    private ElectricityPriceVersionCreateBOConvertMapper priceVersionCreateBOConvertMapper;

    @Resource
    private RedissonRedDisLock redDisLock;

    @Resource
    private ProxyElectricityPriceManagerBakService priceManagerBakService;

    /**
     * @describtion 创建电价版本
     * @author sunjidong
     * @date 2022/5/1 10:02
     * @param
     * @return
     */
    @PostMapping("/createPriceVersion")
    @ApiOperation("创建电价版本")
    public RdfaResult<Boolean> createPriceVersion(@RequestBody @Valid ElectricityPriceVersionStructuresCreateReqVO priceVersionStructuresCreateVO){
        String lockKey =  String.format("%s:%s:%s", CommonConstant.RedisKey.LOCK_PROXY_PRICE_VERSION_CREATE_PREFIX,
                tenantId, priceVersionStructuresCreateVO.getTimestamp());
        Lock lock = null;
        try {
            lock = redDisLock.lock(lockKey);
            if (ObjectUtil.isNull(lock)) {
                return RdfaResult.fail(ErrorCodeEnum.REPEAT_REQUEST.getErrorCode(), ErrorCodeEnum.REPEAT_REQUEST.getErrorMsg());
            }
            ElectricityPriceVersionStructuresCreateBO versionStructuresCreateBO = priceVersionCreateBOConvertMapper.priceVersionStructuresCreateReqVOToBO(priceVersionStructuresCreateVO);
            versionStructuresCreateBO.getPriceVersionCreateBO().setTenantId(tenantId);
            versionStructuresCreateBO.getPriceVersionCreateBO().setTenantName(tenantName);
            Boolean ifSuccess = priceManagerBakService.createPriceVersionStructures(versionStructuresCreateBO);
            return RdfaResult.success(ifSuccess);
        } catch (LockFailException e) {
            return RdfaResult.fail(ErrorCodeEnum.REPEAT_REQUEST.getErrorCode(), ErrorCodeEnum.REPEAT_REQUEST.getErrorMsg());
        } finally {
            redDisLock.unlock(lock);
        }

    }

    @PostMapping("/updatePriceVersion")
    @ApiOperation( "修改电价版本" )
    public RdfaResult<Boolean> updatePriceVersion(@RequestBody @Valid ElectricityPriceVersionUpdateReqVO electricityPriceVersionUpdateReqVO){
        ElectricityPriceVersionUpdateBO electricityPriceVersionUpdateBO = ElectricityPriceVersionUpdateMapper.INSTANCE.electricityPriceVersionUpdateReqVOTOBO( electricityPriceVersionUpdateReqVO );
        return proxyElectricityPriceManagerService.updatePriceVersion( electricityPriceVersionUpdateBO );
    }

    @PostMapping("/deletePriceVersion")
    @ApiOperation( "删除电价版本" )
    public RdfaResult<Boolean> deletePriceVersion(@RequestBody @Valid ElectricityPriceVersionDeleteReqVO electricityPriceVersionDeleteReqVO){
        ElectricityPriceVersionDeleteBO electricityPriceVersionDeleteBO = ElectricityPriceVersionUpdateMapper.INSTANCE.electricityPriceVersionDeleteReqVOTOBO( electricityPriceVersionDeleteReqVO );
        return proxyElectricityPriceManagerService.deletePriceVersion(electricityPriceVersionDeleteBO);
    }

    /**
     * @describtion 校验电价体系以及电价规则
     * @author sunjidong
     * @date 2022/5/6 9:04
     * @param validateReqVO
     * @return ElectricityPriceStructureAndRuleValidateRespVO
     */
    @PostMapping("/validateStructureAndRule")
    @ApiOperation( "校验电价体系以及电价规则" )
    public RdfaResult<ElectricityPriceStructureAndRuleValidateRespVO> validateStructureAndRule(@RequestBody @Valid ElectricityPriceStructureAndRuleValidateReqVO validateReqVO){
        ElectricityPriceStructureAndRuleValidateBO structureAndRuleValidateBO = ElectricityPriceVersionCreateBOConvertMapper.INSTANCE.structureAndRuleValidateVOToBO(validateReqVO);
        ElectricityPriceStructureAndRuleValidateRespBO validateRespBO = priceManagerBakService.validateStructureAndRule(structureAndRuleValidateBO);
        if(ObjectUtil.isNull(structureAndRuleValidateBO)){
            return RdfaResult.success(null);
        }
        ElectricityPriceStructureAndRuleValidateRespVO structureAndRuleValidateRespVO
                = ElectricityPriceVersionCreateBOConvertMapper.INSTANCE.priceStructureAndRuleValidateRespBOToVO(validateRespBO);
        return new RdfaResult<>(Boolean.FALSE, ErrorCodeEnum.VALIDATE_FAIL.getErrorCode(), ErrorCodeEnum.VALIDATE_FAIL.getErrorMsg(), structureAndRuleValidateRespVO);
    }

    /**
     * @describtion 下载模板
     * @author sunjidong
     * @date 2022/5/7 20:38
     * @param response
     */
    @GetMapping("/downLoadTemplate")
    public void downLoadTemplate(HttpServletResponse response){
        ExcelWriter excelWriter = priceManagerBakService.downLoadTemplate();
        try {
            excelWriter.flush(response.getOutputStream());
        } catch (IOException e) {
            throw new PriceException(ErrorCodeEnum.DOWNLOAD_TEMPLATE_EXCEPTION.getErrorCode(), ErrorCodeEnum.DOWNLOAD_TEMPLATE_EXCEPTION.getErrorMsg());
        }
    }

    /**
     * @describtion 导入模板
     * @author sunjidong
     * @date 2022/5/7 20:38
     * @param  priceRuleReqVOList  file
     * @return List<ElectricityPriceRuleCreateReqVO>
     */
    @PostMapping("/uploadTemplate")
    public List<ElectricityPriceRuleCreateReqVO> uploadTemplate(List<ElectricityPriceRuleCreateReqVO> priceRuleReqVOList, @RequestParam("fileName") MultipartFile file){
        if(CollUtil.isEmpty(priceRuleReqVOList)){
            throw new PriceException(ErrorCodeEnum.NON_EXISTENT_DATA_EXCEPTION.getErrorCode(), ErrorCodeEnum.NON_EXISTENT_DATA_EXCEPTION.getErrorMsg());
        }
        List<ElectricityPriceRuleCreateBO> priceRuleCreateBOList = ElectricityPriceVersionCreateBOConvertMapper.INSTANCE.priceRuleCreateReqVOListToBOList(priceRuleReqVOList);
        ExcelReader reader;
        try {
            reader = ExcelUtil.getReader(file.getInputStream());
        } catch (IOException e) {
            throw new PriceException(ErrorCodeEnum.UPLOAD_TEMPLATE_EXCEPTION.getErrorCode(), ErrorCodeEnum.UPLOAD_TEMPLATE_EXCEPTION.getErrorMsg());
        }
        List<ElectricityPriceRuleCreateBO> priceRuleCreateRespBOList = priceManagerBakService.uploadTemplate(reader, priceRuleCreateBOList);
        return ElectricityPriceVersionCreateBOConvertMapper.INSTANCE.priceRuleCreateReqBOListToVOList(priceRuleCreateRespBOList);
    }

    /**
     * @describtion 季节、分时相关校验
     * @author sunjidong
     * @date 2022/5/6 9:04
     * @param validateReqVO
     * @return ElectricityPriceStructureAndRuleValidateRespVO
     */
    @PostMapping("/validateSeasonTime")
    @ApiOperation( "季节、分时相关校验" )
    public RdfaResult<ElectricityPriceStructureAndRuleValidateRespVO> validateSeasonTime(@RequestBody @Valid List<ElectricitySeasonValidateReqVO> validateReqVO){
        List<ElectricitySeasonCreateBO> seasonCreateBOList = ElectricityPriceVersionCreateBOConvertMapper.INSTANCE.seasonValidateReqVOListToBOList(validateReqVO);
        ElectricityPriceStructureAndRuleValidateRespBO validateRespBO = priceManagerBakService.validateSeasonTime(seasonCreateBOList);
        if(ObjectUtil.isNull(validateRespBO)){
            return RdfaResult.success(null);
        }
        ElectricityPriceStructureAndRuleValidateRespVO structureAndRuleValidateRespVO
                = ElectricityPriceVersionCreateBOConvertMapper.INSTANCE
                .priceStructureAndRuleValidateRespBOToVO(validateRespBO);
        return new RdfaResult<>(Boolean.FALSE, ErrorCodeEnum.VALIDATE_FAIL.getErrorCode(), ErrorCodeEnum.VALIDATE_FAIL.getErrorMsg(), structureAndRuleValidateRespVO);
    }

}