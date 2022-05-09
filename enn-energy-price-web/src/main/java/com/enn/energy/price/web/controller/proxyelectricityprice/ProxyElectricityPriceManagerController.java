package com.enn.energy.price.web.controller.proxyelectricityprice;

import cn.hutool.core.util.ObjectUtil;
import com.enn.energy.price.biz.service.bo.proxyprice.*;
import com.enn.energy.price.biz.service.proxyelectricityprice.ProxyElectricityPriceManagerBakService;
import com.enn.energy.price.biz.service.proxyelectricityprice.ProxyElectricityPriceManagerService;
import com.enn.energy.price.common.constants.CommonConstant;
import com.enn.energy.price.common.error.ErrorCodeEnum;
import com.enn.energy.price.web.convertMapper.ElectricityPriceVersionCreateBOConvertMapper;
import com.enn.energy.price.web.convertMapper.ElectricityPriceVersionUpdateMapper;
import com.enn.energy.price.web.vo.requestvo.ElectricityPriceStructureAndRuleValidateReqVO;
import com.enn.energy.price.web.vo.requestvo.ElectricityPriceVersionDeleteReqVO;
import com.enn.energy.price.web.vo.requestvo.ElectricityPriceVersionStructuresCreateReqVO;
import com.enn.energy.price.web.vo.requestvo.ElectricityPriceVersionUpdateReqVO;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceStructureAndRuleValidateRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import top.rdfa.framework.biz.ro.RdfaResult;
import top.rdfa.framework.concurrent.api.exception.LockFailException;
import top.rdfa.framework.concurrent.redis.lock.RedissonRedDisLock;
import javax.annotation.Resource;
import javax.validation.Valid;
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
        ElectricityPriceVersionUpdateBO electricityPriceVersionUpdateBO = ElectricityPriceVersionUpdateConverMapper.INSTANCE.electricityPriceVersionUpdateReqVOTOBO( electricityPriceVersionUpdateReqVO );
        return proxyElectricityPriceManagerService.updatePriceVersion( electricityPriceVersionUpdateBO );
    }

    @PostMapping("/deletePriceVersion")
    @ApiOperation( "删除电价版本" )
    public RdfaResult<Boolean> deletePriceVersion(@RequestBody @Valid ElectricityPriceVersionDeleteReqVO electricityPriceVersionDeleteReqVO){
        ElectricityPriceVersionDeleteBO electricityPriceVersionDeleteBO = ElectricityPriceVersionUpdateConverMapper.INSTANCE.electricityPriceVersionDeleteReqVOTOBO( electricityPriceVersionDeleteReqVO );
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
                = ElectricityPriceVersionCreateBOConvertMapper.INSTANCE
                .priceStructureAndRuleValidateRespBOToVO(validateRespBO);
        return new RdfaResult<>(Boolean.FALSE, ErrorCodeEnum.VALIDATE_FAIL.getErrorCode(), ErrorCodeEnum.VALIDATE_FAIL.getErrorMsg(), structureAndRuleValidateRespVO);
    }

    /**
     * @describtion 下载模板
     * @author sunjidong
     * @date 2022/5/7 20:38
     * @param response
     * @return
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
     * @param
     * @return
     */
    @PostMapping("/uploadTemplate")
    public void uploadTemplate(List<ElectricityPriceRuleCreateReqVO> priceRuleReqVOList, @RequestParam("fileName") MultipartFile file){
        ExcelReader reader;
        try {
            reader = ExcelUtil.getReader(file.getInputStream());
        } catch (IOException e) {
            throw new PriceException(ErrorCodeEnum.UPLOAD_TEMPLATE_EXCEPTION.getErrorCode(), ErrorCodeEnum.UPLOAD_TEMPLATE_EXCEPTION.getErrorMsg());
        }
        List<List<Object>> read = reader.read(2);
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
    @GetMapping("/getDictionarys/{type}")
    public RdfaResult<ElectricityPriceDictionaryRespVOList> getElectricityPriceDictionarys(@PathVariable("type") @ApiParam(required = true, name = "type", value = "类型 0:用电行业 1:电压等级") String type){
        List<ElectricityPriceDictionaryBO> priceElectricityDictionarys = proxyElectricityPriceManagerService.getPriceElectricityDictionarys( type );
        List<ElectricityPriceDictionaryRespVO> electricityPriceDictionaryRespVOS = ElectricityPriceVersionUpdateConverMapper.INSTANCE.ElectricityPriceDictionaryBOListToVOList( priceElectricityDictionarys );
        ElectricityPriceDictionaryRespVOList electricityPriceDictionaryRespVOList = new ElectricityPriceDictionaryRespVOList();
        electricityPriceDictionaryRespVOList.setElectricityPriceDictionaryRespVOList( electricityPriceDictionaryRespVOS );
        return RdfaResult.success( electricityPriceDictionaryRespVOList );
    }

}