package com.enn.energy.price.web.controller.proxyelectricityprice;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelFileUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.enn.energy.price.biz.service.bo.ElectricityPriceDictionaryBO;
import com.enn.energy.price.biz.service.bo.proxyprice.*;
import com.enn.energy.price.biz.service.proxyelectricityprice.ProxyElectricityPriceManagerBakService;
import com.enn.energy.price.biz.service.proxyelectricityprice.ProxyElectricityPriceManagerService;
import com.enn.energy.price.common.constants.CommonConstant;
import com.enn.energy.price.common.error.ErrorCodeEnum;
import com.enn.energy.price.common.error.PriceException;
import com.enn.energy.price.web.convertMapper.*;
import com.enn.energy.price.web.vo.requestvo.*;
import com.enn.energy.price.web.vo.responsevo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.rdfa.framework.biz.ro.RdfaResult;
import top.rdfa.framework.concurrent.api.exception.LockFailException;
import top.rdfa.framework.concurrent.api.lock.RdfaDistributeLockFactory;
import top.rdfa.framework.concurrent.redis.lock.RedissonRedDisLock;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

/**
 * 代理购电管理controller
 *
 * @author sunjidong
 * @date 2022/4/30
 **/
@Api(tags = "代理购电管理控制器v2.0")
@RestController
@RequestMapping("/proxyElectricityPrice")
@Slf4j
public class ProxyElectricityPriceManagerController {

    @Value("${system.tenantId}")
    private String tenantId;

    @Value("${system.tenantName}")
    private String tenantName;

    @Resource
    private ProxyElectricityPriceManagerService proxyElectricityPriceManagerService;

    @Resource
    private RdfaDistributeLockFactory rdfaDistributeLockFactory;

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
    public RdfaResult<Boolean> createPriceVersion(@RequestBody @Valid ElectricityPriceVersionUpdateReqVO versionReqVO){
        String lockKey =  String.format(CommonConstant.REDIS_APPEND, CommonConstant.RedisKey.LOCK_PROXY_PRICE_VERSION_CREATE_PREFIX,
                tenantId, versionReqVO.getTimestamp());
        Lock lock = rdfaDistributeLockFactory.getLock(lockKey);
        boolean acquired = lock.tryLock();
        if (!acquired) {
            return RdfaResult.fail(ErrorCodeEnum.REPEAT_REQUEST.getErrorCode(), ErrorCodeEnum.REPEAT_REQUEST.getErrorMsg());
        }
        try {
            ElectricityPriceVersionUpdateBO versionBO = CommonBOVOConvertMapper.INSTANCE.priceVersionReqVOToBO( versionReqVO );
            versionBO.setTenantId(tenantId);
            versionBO.setTenantName(tenantName);
            Boolean ifSuccess = priceManagerBakService.createPriceVersionStructures(versionBO);
            return RdfaResult.success(ifSuccess);
        } finally {
            lock.unlock();
        }

    }

    @PostMapping("/updatePriceVersion")
    @ApiOperation( "修改电价版本" )
    public RdfaResult<Boolean> updatePriceVersion(@RequestBody @Valid ElectricityPriceVersionUpdateReqVO electricityPriceVersionUpdateReqVO){
        ElectricityPriceVersionUpdateBO electricityPriceVersionUpdateBO = ElectricityPriceVersionConverMapper.INSTANCE.electricityPriceVersionUpdateReqVOToBO( electricityPriceVersionUpdateReqVO );
        String lockKey =  String.format("%s:%s:%s", CommonConstant.RedisKey.LOCK_PROXY_PRICE_VERSION_UPDATE_PREFIX,
                tenantId, electricityPriceVersionUpdateBO.getId());
        Lock lock = null;
        try {
            lock = redDisLock.lock(lockKey  );
            if (ObjectUtil.isNull( lock )) {
                return RdfaResult.fail( ErrorCodeEnum.RETRY_AFTER.getErrorCode(), ErrorCodeEnum.RETRY_AFTER.getErrorMsg() );
            }
            return proxyElectricityPriceManagerService.updatePriceVersion(electricityPriceVersionUpdateBO);
        } catch (LockFailException e) {
            return RdfaResult.fail(ErrorCodeEnum.REIDS_LOCK_ERROR.getErrorCode(), ErrorCodeEnum.REIDS_LOCK_ERROR.getErrorMsg());
        }finally {
            redDisLock.unlock(lockKey);
        }
    }

    @PostMapping("/deletePriceVersion")
    @ApiOperation( "删除电价版本" )
    public RdfaResult<Boolean> deletePriceVersion(@RequestBody @Valid ElectricityPriceVersionDeleteReqVO electricityPriceVersionDeleteReqVO){
        ElectricityPriceVersionDeleteBO electricityPriceVersionDeleteBO = ElectricityPriceVersionConverMapper.INSTANCE.electricityPriceVersionDeleteReqVOToBO( electricityPriceVersionDeleteReqVO );
        String lockKey =  String.format("%s:%s:%s", CommonConstant.RedisKey.LOCK_PROXY_PRICE_VERSION_UPDATE_PREFIX,
                tenantId, electricityPriceVersionDeleteBO.getId());
        Lock lock = null;
        try {
            lock = redDisLock.lock( lockKey );
            if (ObjectUtil.isNull( lock )) {
                return RdfaResult.fail( ErrorCodeEnum.RETRY_AFTER.getErrorCode(), ErrorCodeEnum.RETRY_AFTER.getErrorMsg() );
            }
            return proxyElectricityPriceManagerService.deletePriceVersion(electricityPriceVersionDeleteBO);
        } catch (LockFailException e) {
            return RdfaResult.fail(ErrorCodeEnum.REIDS_LOCK_ERROR.getErrorCode(), ErrorCodeEnum.REIDS_LOCK_ERROR.getErrorMsg());
        }finally {
            redDisLock.unlock(lockKey);
        }
    }

    /**
     * 校验电价体系以及电价规则
     * @author sunjidong
     * @date 2022/5/6 9:04
     * @param validateReqVO
     * @return ElectricityPriceStructureAndRuleValidateRespVO
     */
    @PostMapping("/validateStructureAndRule")
    @ApiOperation( "校验电价体系以及电价规则" )
    public RdfaResult<ElectricityPriceStructureAndRuleValidateRespVO> validateStructureAndRule(@RequestBody @Valid ElectricityPriceVersionUpdateReqVO validateReqVO){
        ElectricityPriceVersionUpdateBO structureAndRuleValidateBO = CommonBOVOConvertMapper.INSTANCE.priceVersionReqVOToBO(validateReqVO);
        ElectricityPriceStructureAndRuleValidateRespBO validateRespBO = priceManagerBakService.validateStructureAndRule(structureAndRuleValidateBO);
        if(ObjectUtil.isNull(validateRespBO)){
            return RdfaResult.success(null);
        }
        ElectricityPriceStructureAndRuleValidateRespVO structureAndRuleValidateRespVO
                = CommonBOVOConvertMapper.INSTANCE.priceStructureAndRuleValidateRespBOToVO(validateRespBO);
        return new RdfaResult<>(Boolean.FALSE, ErrorCodeEnum.VALIDATE_FAIL.getErrorCode(), ErrorCodeEnum.VALIDATE_FAIL.getErrorMsg(), structureAndRuleValidateRespVO);
    }

    /**
     * @describtion 下载模板
     * @author sunjidong
     * @date 2022/5/7 20:38
     * @param response
     */
    @GetMapping("/downLoadTemplate")
    public void downLoadTemplate(@ApiParam(value = "省编码", name = "provinceCode", required = true) String provinceCode, HttpServletResponse response){
        if(StrUtil.isBlank(provinceCode)){
            throw new PriceException(ErrorCodeEnum.PROVINCE_CODE_CAN_NOT_NULL.getErrorCode(), ErrorCodeEnum.PROVINCE_CODE_CAN_NOT_NULL.getErrorMsg());
        }
        ExcelWriter excelWriter = priceManagerBakService.downLoadTemplate(provinceCode);
        try {
            response.setHeader("Content-Disposition", excelWriter.getDisposition("template.xls", CharsetUtil.CHARSET_UTF_8));
            excelWriter.flush(response.getOutputStream());
        } catch (IOException e) {
            throw new PriceException(ErrorCodeEnum.DOWNLOAD_TEMPLATE_EXCEPTION.getErrorCode(), ErrorCodeEnum.DOWNLOAD_TEMPLATE_EXCEPTION.getErrorMsg());
        }
    }

    /**
     * @describtion 导入模板
     * @author sunjidong
     * @date 2022/5/7 20:38
     * @param  importDataReqVO  file
     * @return List<ElectricityPriceRuleCreateReqVO>
     */
    @PostMapping("/uploadTemplate")
    public RdfaResult<UploadTemplateRespVO> uploadTemplate(ElectricityPriceImportDataReqVO importDataReqVO, @RequestParam("fileName") MultipartFile file){
        if(StrUtil.isBlank(importDataReqVO.getProvinceCode())){
            throw new PriceException(ErrorCodeEnum.PROVINCE_CODE_CAN_NOT_NULL.getErrorCode(), ErrorCodeEnum.PROVINCE_CODE_CAN_NOT_NULL.getErrorMsg());
        }
        ElectricityPriceImportDataBO importDataBO = CommonBOVOConvertMapper.INSTANCE.importDataReqVOToBO(importDataReqVO);
        ExcelReader reader;
        try {
            reader = ExcelUtil.getReader(file.getInputStream());
        } catch (IOException e) {
            throw new PriceException(ErrorCodeEnum.UPLOAD_TEMPLATE_EXCEPTION.getErrorCode(), ErrorCodeEnum.UPLOAD_TEMPLATE_EXCEPTION.getErrorMsg());
        }
        List<ElectricityPriceUpdateBO> priceRuleCreateRespBOList = priceManagerBakService.uploadTemplate(reader, importDataBO);
        UploadTemplateRespVO uploadTemplateRespVO = new UploadTemplateRespVO();
        uploadTemplateRespVO.setPriceUpdateBOList(priceRuleCreateRespBOList);
        return RdfaResult.success(uploadTemplateRespVO);
    }

    /**
     * @describtion 导入模板
     * @author sunjidong
     * @date 2022/5/7 20:38
     * @param file
     * @return List<ElectricityPriceRuleCreateReqVO>
     */
    @PostMapping("/validateTemplate")
    public RdfaResult<ElectricityPriceStructureAndRuleValidateRespVO> validateTemplate(String provinceCode, @RequestParam("fileName") MultipartFile file){
        if(StrUtil.isBlank(provinceCode)){
            throw new PriceException(ErrorCodeEnum.PROVINCE_CODE_CAN_NOT_NULL.getErrorCode(), ErrorCodeEnum.PROVINCE_CODE_CAN_NOT_NULL.getErrorMsg());
        }
        ExcelReader reader;
        try {
            boolean xls = ExcelFileUtil.isXls(file.getInputStream());
            if(!xls){
                throw new PriceException(ErrorCodeEnum.TEMPLATE_FORMAT_ILLEGAL.getErrorCode(), ErrorCodeEnum.TEMPLATE_FORMAT_ILLEGAL.getErrorMsg());
            }
            reader = ExcelUtil.getReader(file.getInputStream());
        } catch (IOException e) {
            throw new PriceException(ErrorCodeEnum.TEMPLATE_NOT_EXISTS.getErrorCode(), ErrorCodeEnum.TEMPLATE_NOT_EXISTS.getErrorMsg());
        }
        ElectricityPriceStructureAndRuleValidateRespBO validateRespBO = priceManagerBakService.validateTemplate(provinceCode, reader);
        return RdfaResult.success(CommonBOVOConvertMapper.INSTANCE.priceStructureAndRuleValidateRespBOToVO(validateRespBO));
    }

    /**
     * @describtion 季节、分时相关校验
     * @author sunjidong
     * @date 2022/5/6 9:04
     * @param validateReqVOList
     * @return ElectricityPriceStructureAndRuleValidateRespVO
     */
    @PostMapping("/validateSeasonTime")
    @ApiOperation( "季节、分时相关校验" )
    public RdfaResult<ElectricityPriceStructureAndRuleValidateRespVO> validateSeasonTime(@RequestBody @Valid List<ElectricitySeasonValidateReqVO> validateReqVOList){
        if(CollUtil.isEmpty(validateReqVOList)){
            throw new PriceException(ErrorCodeEnum.NON_EXISTENT_DATA_EXCEPTION.getErrorCode(), ErrorCodeEnum.NON_EXISTENT_DATA_EXCEPTION.getErrorMsg());
        }
        List<ElectricitySeasonCreateBO> seasonCreateBOList = CommonBOVOConvertMapper.INSTANCE.seasonValidateReqVOListToBOList(validateReqVOList);
        ElectricityPriceStructureAndRuleValidateRespBO validateRespBO = priceManagerBakService.validateSeasonTime(seasonCreateBOList);
        if(ObjectUtil.isNull(validateRespBO)){
            return RdfaResult.success(null);
        }
        ElectricityPriceStructureAndRuleValidateRespVO structureAndRuleValidateRespVO
                = CommonBOVOConvertMapper.INSTANCE
                .priceStructureAndRuleValidateRespBOToVO(validateRespBO);
        return new RdfaResult<>(Boolean.FALSE, ErrorCodeEnum.VALIDATE_FAIL.getErrorCode(), ErrorCodeEnum.VALIDATE_FAIL.getErrorMsg(), structureAndRuleValidateRespVO);
    }

    @GetMapping("/getVersionList/{provinceCode}")
    @ApiOperation("获取版本列表")
    public RdfaResult<ElectricityPriceVersionRespVOList> getVersionList(@PathVariable("provinceCode") @ApiParam(required = true, name = "provinceCode", value = "省编码") String provinceCode){
        List<ElectricityPriceVersionBO> electricityPriceVersionBOS = proxyElectricityPriceManagerService.queryPriceVersionList( provinceCode );
        if(CollectionUtils.isEmpty(electricityPriceVersionBOS)){
            return RdfaResult.success( null );
        }
        List<ElectricityPriceVersionRespVO> priceVersionRespVOList = ElectricityPriceVersionConverMapper.INSTANCE.electricityPriceVersionRespBOListToVOList( electricityPriceVersionBOS );
        ElectricityPriceVersionRespVOList respVOList = ElectricityPriceVersionRespVOList.builder().electricityPriceVersionRespVOList( priceVersionRespVOList ).build();
        return RdfaResult.success(respVOList) ;
    }


    @GetMapping("/getVersionStructureList/{versionId}")
    @ApiOperation( "获取版本体系列表" )
    public RdfaResult<ElectricityPriceStructureRespVOList> getVersionStructureList(@PathVariable("versionId") @ApiParam(required = true, name = "versionId", value = "版本id") String versionId){
        List<ElectricityPriceStructureBO> electricityPriceStructureBOS = proxyElectricityPriceManagerService.queryPriceVersionStructureList( versionId );
        if(CollectionUtils.isEmpty(electricityPriceStructureBOS)){
            return RdfaResult.success( null );
        }
        List<ElectricityPriceStructureRespVO> electricityPriceStructureRespVOS = ElectricityPriceStrutureConverMapper.INSTANCE.ElectricityPriceStructureRespBOListToVOList( electricityPriceStructureBOS );
        ElectricityPriceStructureRespVOList respVOList = ElectricityPriceStructureRespVOList.builder().electricityPriceStructureRespVOList( electricityPriceStructureRespVOS ).build();
        return RdfaResult.success( respVOList );
    }

    @GetMapping("/getStructureDetail/{structureId}")
    @ApiOperation("获取体系详情")
    public RdfaResult<ElectricityPriceStructureDetailRespVO> getStructureDetail (@PathVariable("structureId") @ApiParam(required = true, name = "structureId", value = "体系id") String structureId){
        ElectricityPriceStructureDetailBO structureDetail = proxyElectricityPriceManagerService.getStructureDetail( structureId );
        ElectricityPriceStructureDetailRespVO electricityPriceStructureDetailRespVO = ElectricityPriceStrutureConverMapper.INSTANCE.ElectricityPriceStructureDetailBOToVO( structureDetail );
        return RdfaResult.success( electricityPriceStructureDetailRespVO );
    }


    @ApiOperation( "查询电价字典" )
    @GetMapping("/getDictionaries")
    public RdfaResult<ElectricityPriceDictionaryMapRespVO> getElectricityPriceDictionaries(@ApiParam(required = true, name = "type", value = "类型 0:用电行业 1:电压等级") String type,
                                                                                           @ApiParam(required = true, name = "province", value = "省编码") String province){
        if(StrUtil.isBlank(province)){
            throw new PriceException(ErrorCodeEnum.METHOD_ARGUMENT_VALID_EXCEPTION.getErrorCode(),
                                    ErrorCodeEnum.METHOD_ARGUMENT_VALID_EXCEPTION.getErrorMsg());
        }
        Map<Integer, List<ElectricityPriceDictionaryBO>> typeDictionary = proxyElectricityPriceManagerService.getPriceElectricityDictionaries(type, province);
        ElectricityPriceDictionaryMapRespVO dictionaryMapRespVO = new ElectricityPriceDictionaryMapRespVO();
        dictionaryMapRespVO.setTypeDictionary( typeDictionary );
        return RdfaResult.success( dictionaryMapRespVO );
    }

    @ApiOperation( "删除电价规则时，校验是否绑定了设备" )
    @GetMapping("/validateDeletePriceRule/{id}")
    public RdfaResult<Boolean> validateDeletePriceRule(@PathVariable("id") @ApiParam(required = true, name = "id", value = "电价规则Id") String id){
        if(StrUtil.isBlank(id)){
            throw new PriceException(ErrorCodeEnum.NON_EXISTENT_DATA_EXCEPTION.getErrorCode(), ErrorCodeEnum.NON_EXISTENT_DATA_EXCEPTION.getErrorMsg());
        }
        return RdfaResult.success(priceManagerBakService.validateDeletePriceRule(id));
    }

    @ApiOperation( "取消区域时，校验是否绑定了设备" )
    @GetMapping("/validateDeleteArea")
    public RdfaResult<ElectricityPriceStructureCreateBO> validateDeleteArea(@ApiParam(required = true, name = "id", value = "主键Id") String id,
                                                  @ApiParam(required = true, name = "structureId", value = "体系Id") String structureId,
                                                  @ApiParam(required = true, name = "districtCodeList", value = "体系区域") List<String> districtCodeList){
        if(CollUtil.isEmpty(districtCodeList) || StrUtil.isEmpty(id) || StrUtil.isEmpty(structureId)){
            throw new PriceException(ErrorCodeEnum.NON_EXISTENT_DATA_EXCEPTION.getErrorCode(), ErrorCodeEnum.NON_EXISTENT_DATA_EXCEPTION.getErrorMsg());
        }
        ElectricityPriceStructureCreateBO noCancelArea = priceManagerBakService.validateDeleteArea(id, districtCodeList);
        if(ObjectUtil.isNull(noCancelArea)){
            return RdfaResult.success(null);
        }
        return new RdfaResult<>(Boolean.FALSE, ErrorCodeEnum.VALIDATE_FAIL.getErrorCode(), ErrorCodeEnum.VALIDATE_FAIL.getErrorMsg(), noCancelArea);
    }

    /**
     * @describtion 根据省编码查找版本以及版本下的所有体系详细内容
     * @author sunjidong
     * @date 2022/5/9 15:53
     * @param
     * @return
     */
    @ApiOperation( "根据省编码查找最迟的版本以及版本下的所有体系详细内容")
    @GetMapping("/getLastVersionStructures")
    public RdfaResult<ElectricityPriceStructureListRespVO> getLastVersionStructures(@RequestParam @ApiParam(required = true, name = "provinceCode", value = "省编码") String provinceCode){
        if(StrUtil.isEmpty(provinceCode)){
            throw new PriceException(ErrorCodeEnum.NON_EXISTENT_DATA_EXCEPTION.getErrorCode(), ErrorCodeEnum.NON_EXISTENT_DATA_EXCEPTION.getErrorMsg());
        }
        ElectricityPriceStructureListDetailBO lastVersionStructure = priceManagerBakService.getLastVersionStructures(provinceCode);
        ElectricityPriceStructureListRespVO priceStructureListRespVO = CommonBOVOConvertMapper.INSTANCE.structureListDetailBOToVO(lastVersionStructure);
        return RdfaResult.success(priceStructureListRespVO);
    }

    /**
     * 获取默认的体系详细内容
     * @author sunjidong
     * @date 2022/5/11 14:57
     */
    @ApiOperation( "获取默认的体系详细内容")
    @GetMapping("/getDefaultStructureDetail")
    public ElectricityPriceStructureDetailRespVO getDefaultStructureDetail(@RequestParam(value = "provinceCode") @ApiParam(required = true, name = "provinceCode", value = "省编码") String provinceCode){
        if(StrUtil.isEmpty(provinceCode)){
            throw new PriceException(ErrorCodeEnum.NON_EXISTENT_DATA_EXCEPTION.getErrorCode(), ErrorCodeEnum.NON_EXISTENT_DATA_EXCEPTION.getErrorMsg());
        }
        ElectricityPriceStructureDetailBO defaultStructureDetail = priceManagerBakService.getDefaultStructureDetail(CommonConstant.DICTIONARY_VOLTAGELEVEL_TYPE, provinceCode);
        return CommonBOVOConvertMapper.INSTANCE.versionStructureRespBOToVO(defaultStructureDetail);
    }

    @ApiOperation(value = "版本删除校验")
    @PostMapping("/versionDeleteValidate")
    public RdfaResult<Boolean> versionDeleteValidate(@Valid @RequestBody ElectricityPriceVersionDeleteReqVO priceVersionDeleteReqVO){
        ElectricityPriceVersionDeleteBO electricityPriceVersionDeleteBO = ElectricityPriceVersionConverMapper.INSTANCE.electricityPriceVersionDeleteReqVOToBO( priceVersionDeleteReqVO );
        RdfaResult<Boolean> result = proxyElectricityPriceManagerService.versionDeleteValidate( electricityPriceVersionDeleteBO );
        return result;
    }


    @ApiOperation(value = "体系删除校验")
    @PostMapping("/structureDeleteValidate")
    public RdfaResult<Boolean> structureDeleteValidate(@RequestBody @Valid ElectricityPriceStructureDeleteValidateReqVO structureDeleteValidateReqVO){
        ElectricityPriceStructureDeleteValidateBO structureDeleteValidateBO = ElectricityPriceStrutureConverMapper.INSTANCE.ElectricityPriceStructureDeleteValidateVOToBO( structureDeleteValidateReqVO );
        RdfaResult<Boolean> result=proxyElectricityPriceManagerService.structureDeleteValidate(structureDeleteValidateBO);
        return result;
    }

}