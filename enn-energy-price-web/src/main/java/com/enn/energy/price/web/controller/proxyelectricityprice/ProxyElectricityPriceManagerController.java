package com.enn.energy.price.web.controller.proxyelectricityprice;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
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
import com.enn.energy.price.web.convertMapper.ElectricityPriceStrutureConverMapper;
import com.enn.energy.price.web.convertMapper.ElectricityPriceVersionCreateBOConvertMapper;
import com.enn.energy.price.web.convertMapper.ElectricityPriceVersionUpdateConverMapper;
import com.enn.energy.price.web.vo.requestvo.*;
import com.enn.energy.price.web.vo.responsevo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.rdfa.framework.auth.client.annotation.Authorize;
import top.rdfa.framework.biz.ro.RdfaResult;
import top.rdfa.framework.concurrent.api.exception.LockFailException;
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
@Api(tags = "代理购电管理控制器")
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
            ElectricityPriceVersionStructuresCreateBO versionStructuresCreateBO = ElectricityPriceVersionCreateBOConvertMapper.INSTANCE.priceVersionStructuresCreateReqVOToBO(priceVersionStructuresCreateVO);
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
        ElectricityPriceVersionUpdateBO electricityPriceVersionUpdateBO = ElectricityPriceVersionUpdateConverMapper.INSTANCE.electricityPriceVersionUpdateReqVOToBO( electricityPriceVersionUpdateReqVO );
        String lockKey =  String.format("%s:%s:%s", CommonConstant.RedisKey.LOCK_PROXY_PRICE_VERSION_UPDATE_PREFIX,
                tenantId, electricityPriceVersionUpdateBO.getId());
        Lock lock = null;
        try {
            lock = redDisLock.lock(lockKey  );
            if (ObjectUtil.isNull( lock )) {
                return RdfaResult.fail( ErrorCodeEnum.RETRY_AFTER.getErrorCode(), ErrorCodeEnum.RETRY_AFTER.getErrorMsg() );
            }
            return proxyElectricityPriceManagerService.updatePriceVersion( electricityPriceVersionUpdateBO );
        } catch (LockFailException e) {
            return RdfaResult.fail(ErrorCodeEnum.REIDS_LOCK_ERROR.getErrorCode(), ErrorCodeEnum.REIDS_LOCK_ERROR.getErrorMsg());
        }finally {
            redDisLock.unlock(lockKey);
        }
    }

    @PostMapping("/deletePriceVersion")
    @ApiOperation( "删除电价版本" )
    public RdfaResult<Boolean> deletePriceVersion(@RequestBody @Valid ElectricityPriceVersionDeleteReqVO electricityPriceVersionDeleteReqVO){
        if(DateUtil.parse(electricityPriceVersionDeleteReqVO.getStartDate(), DatePattern.NORM_DATE_PATTERN).isBefore( DateUtil.date() )){
            return RdfaResult.fail( ErrorCodeEnum.VERSION_IS_NOT_ALLOW_DELETE.getErrorCode(), ErrorCodeEnum.VERSION_IS_NOT_ALLOW_DELETE.getErrorMsg());
        }
        ElectricityPriceVersionDeleteBO electricityPriceVersionDeleteBO = ElectricityPriceVersionUpdateConverMapper.INSTANCE.electricityPriceVersionDeleteReqVOToBO( electricityPriceVersionDeleteReqVO );
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
     * @param  priceRuleReqVOList  file
     * @return List<ElectricityPriceRuleCreateReqVO>
     */
    @PostMapping("/uploadTemplate")
    public List<ElectricityPriceRuleCreateReqVO> uploadTemplate(List<ElectricityPriceRuleCreateReqVO> priceRuleReqVOList, @RequestParam("fileName") MultipartFile file){
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

    @GetMapping("/getVersionList/{provinceCode}")
    @ApiOperation("获取版本列表")
    public RdfaResult<ElectricityPriceVersionRespVOList> getVersionList(@PathVariable("provinceCode") @ApiParam(required = true, name = "provinceCode", value = "省编码") String provinceCode){
        List<ElectricityPriceVersionBO> electricityPriceVersionBOS = proxyElectricityPriceManagerService.queryPriceVersionList( provinceCode );
        if(CollectionUtils.isEmpty(electricityPriceVersionBOS)){
            return RdfaResult.success( null );
        }
        List<ElectricityPriceVersionRespVO> priceVersionRespVOList = ElectricityPriceVersionUpdateConverMapper.INSTANCE.electricityPriceVersionRespBOListToVOList( electricityPriceVersionBOS );
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
        ElectricityPriceStructureCreateBO noCancelArea = priceManagerBakService.validateDeleteArea(id, structureId, districtCodeList);
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
    public RdfaResult<ElectricityPriceStructureListRespVO> getLastVersionStructures(@RequestParam String provinceCode){
        if(StrUtil.isEmpty(provinceCode)){
            throw new PriceException(ErrorCodeEnum.NON_EXISTENT_DATA_EXCEPTION.getErrorCode(), ErrorCodeEnum.NON_EXISTENT_DATA_EXCEPTION.getErrorMsg());
        }
        List<ElectricityPriceStructureDetailBO> lastVersionStructures = priceManagerBakService.getLastVersionStructures(provinceCode);
        List<ElectricityPriceStructureDetailForCreateRespVO> electricityPriceStructureDetailForCreateRespVOList = ElectricityPriceStrutureConverMapper.INSTANCE.ElectricityPriceStructureDetailForCreaateBOToVOList(lastVersionStructures);
        ElectricityPriceStructureListRespVO priceStructureListRespVO = new ElectricityPriceStructureListRespVO();
        priceStructureListRespVO.setStructureDetailForCreateRespVOList(electricityPriceStructureDetailForCreateRespVOList);
        return RdfaResult.success(priceStructureListRespVO);
    }

    /**
     * 获取默认的体系详细内容
     * @author sunjidong
     * @date 2022/5/11 14:57
     */
    @ApiOperation( "获取默认的体系详细内容")
    @GetMapping("/getDefaultStructureDetail")
    public ElectricityPriceDefaultStructureAndRuleRespVO getDefaultStructureDetail(@RequestParam(value = "provinceCode") String provinceCode){
        if(StrUtil.isEmpty(provinceCode)){
            throw new PriceException(ErrorCodeEnum.NON_EXISTENT_DATA_EXCEPTION.getErrorCode(), ErrorCodeEnum.NON_EXISTENT_DATA_EXCEPTION.getErrorMsg());
        }
        ElectricityPriceDefaultStructureAndRuleBO defaultStructureDetail = priceManagerBakService.getDefaultStructureDetail(CommonConstant.DICTIONARY_VOLTAGELEVEL_TYPE, provinceCode);
        return ElectricityPriceStrutureConverMapper.INSTANCE.electricityPriceStructureAndRuleBOToVO(defaultStructureDetail);
    }

}