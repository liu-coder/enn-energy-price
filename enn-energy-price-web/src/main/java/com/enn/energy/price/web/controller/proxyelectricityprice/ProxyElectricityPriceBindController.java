package com.enn.energy.price.web.controller.proxyelectricityprice;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.enn.energy.price.biz.service.bo.proxyprice.*;
import com.enn.energy.price.biz.service.proxyelectricityprice.ProxyElectricityPriceBindService;
import com.enn.energy.price.common.constants.CommonConstant;
import com.enn.energy.price.common.enums.ChangeTypeEum;
import com.enn.energy.price.common.enums.ResponseCode;
import com.enn.energy.price.common.error.ErrorCodeEnum;
import com.enn.energy.price.common.error.PriceException;
import com.enn.energy.price.core.service.impl.DisLockService;
import com.enn.energy.price.web.convertMapper.ElectricityPriceBindConverMapper;
import com.enn.energy.price.web.convertMapper.ElectricityPriceVersionCreateBOConvertMapper;
import com.enn.energy.price.web.convertMapper.ElectricityPriceVersionUpdateConverMapper;
import com.enn.energy.price.web.vo.requestvo.ElectricityPriceBindDetailQueryReqVO;
import com.enn.energy.price.web.vo.requestvo.ElectricityPriceBindRemoveReqVO;
import com.enn.energy.price.web.vo.requestvo.ElectricityPriceBindReqVO;
import com.enn.energy.price.web.vo.requestvo.ElectricityPriceBindVersionsReqVO;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceBindDetailRespVO;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceBindNodeStatusRespVO;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceVersionRespVO;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceVersionsByBindAreaRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.rdfa.framework.biz.ro.RdfaResult;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * @description: 电价绑定相关
 * @author:quyl
 * @createTime:2022/5/11 8:00
 */
@Api(tags = "电价绑定相关控制器")
@RestController
@RequestMapping("/bindProxyElectricityPrice")
@Slf4j
public class ProxyElectricityPriceBindController {


    @Resource
    private ProxyElectricityPriceBindService proxyElectricityPriceBindService;

    @Autowired
    private DisLockService disLockService;

    /**
     * 绑定代理电价
     *
     * @param electricityPriceBindReqVO
     * @return
     */
    @PostMapping("/bindPriceRule")
    @ApiOperation("绑定电价")
    public RdfaResult<Boolean> bindProxyElectricityPrice(@RequestBody @NotNull @Validated ElectricityPriceBindReqVO electricityPriceBindReqVO) {
        log.info("绑定电价:{}", JSON.toJSONString(electricityPriceBindReqVO));
        return doBindPrice(electricityPriceBindReqVO, ChangeTypeEum.ADD.getType());
    }

    /**
     * 修改绑定代理电价
     *
     * @param electricityPriceBindReqVO
     * @return
     */
    @PostMapping(value = "/modifyBindPriceRule")
    @ApiOperation("修改绑定代理电价")
    public RdfaResult<Boolean> modifyBindPriceRule(@RequestBody @NotNull @Validated ElectricityPriceBindReqVO electricityPriceBindReqVO) {
        log.info("修改绑定代理电价:{}", JSON.toJSONString(electricityPriceBindReqVO));
        if (ObjectUtil.isEmpty(electricityPriceBindReqVO.getId())) {
            throw new PriceException(ErrorCodeEnum.METHOD_ARGUMENT_VALID_EXCEPTION.getErrorCode(), "设备绑定id更新时不能为空");
        }
        return doBindPrice(electricityPriceBindReqVO, ChangeTypeEum.UPDATE.getType());
    }

    @PostMapping(value = "/unBoundPriceRule")
    @ApiOperation("解绑")
    public RdfaResult<String> unBoundPriceRule(@RequestBody @NotNull @Validated ElectricityPriceBindRemoveReqVO electricityPriceBindRemoveReqVO) {
        log.info("解绑代理电价:{}", JSON.toJSONString(electricityPriceBindRemoveReqVO));
        ElectricityPriceBindRemoveBO electricityPriceBindRemoveBO = ElectricityPriceVersionCreateBOConvertMapper.INSTANCE.priceBindRemoveReqVOToBO(electricityPriceBindRemoveReqVO);
        String lockKey = CommonConstant.LOCK_KEY + CommonConstant.KEY_SPERATOR + CommonConstant.RedisKey.LOCK_PROXY_PRICE_UNBOUND_PREFIX + CommonConstant.KEY_SPERATOR + electricityPriceBindRemoveReqVO.getId();
        Lock lock = null;
        try {
            lock = disLockService.tryLock(lockKey, CommonConstant.LOCK_TIME_OUT, CommonConstant.LOCK_LEASE_TIME, CommonConstant.LOCK_REPEAT_TIMES);
            if (lock != null) {
                proxyElectricityPriceBindService.unBoundPriceRule(electricityPriceBindRemoveBO);
                return RdfaResult.success(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), null);
            }
        } catch (PriceException e) {
            log.error(e.getMessage(), e);
            return RdfaResult.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return RdfaResult.fail(ResponseCode.FAIL.getCode(), ResponseCode.FAIL.getMessage());
        } finally {
            disLockService.unlock(lock);
        }
        return RdfaResult.fail(ErrorCodeEnum.REIDS_LOCK_ERROR.getErrorCode(), ErrorCodeEnum.REIDS_LOCK_ERROR.getErrorMsg());
    }

    /**
     * 根据节点id 查看绑定电价详情 选择节点+当前有效版本的绑定关系（已绑定和已失效的查询）
     *
     * @param nodeId
     * @return
     */
    @PostMapping(value = "/getPriceBindDetail")
    @ApiOperation("查看绑定电价详情（已绑定和已失效）")
    public RdfaResult<ElectricityPriceBindDetailRespVO> getPriceBindDetail(@RequestBody @NotNull @Validated ElectricityPriceBindDetailQueryReqVO electricityPriceBindDetailQueryReqVO) {
        //根据节点id查询绑定的规则id，对应的 省市区，版本，体系，以及具体的规则详情
        ElectricityPriceBindDetailBO electricityPriceBindDetailBO = proxyElectricityPriceBindService.getPriceBindDetail(electricityPriceBindDetailQueryReqVO.getNodeId());
        ElectricityPriceBindDetailRespVO electricityPriceBindDetailRespVO = ElectricityPriceBindConverMapper.INSTANCE.ElectricityPriceBindDetailBOTOVO(electricityPriceBindDetailBO);
        return RdfaResult.success(electricityPriceBindDetailRespVO);
    }

    //业务树查询，根据企业id查询根节点列表（包含根节点所属的省市区）

    /**
     * 根据企业id获取节点绑定价格的状态
     *
     * @param entId
     * @return
     */
    @GetMapping("/getNodeBindPriceStatusByEntId/{entId}")
    @ApiOperation("获取版本列表")
    public RdfaResult<ElectricityPriceBindNodeStatusRespVO> getNodeBindPriceStatusByEntId(@PathVariable("entId") @ApiParam(required = true, name = "entId", value = "企业id") String entId) {
        if (StrUtil.isEmpty(entId)) {
            throw new PriceException(ErrorCodeEnum.NON_EXISTENT_DATA_EXCEPTION.getErrorCode(), ErrorCodeEnum.NON_EXISTENT_DATA_EXCEPTION.getErrorMsg());
        }
        ElectricityPriceBindNodeStatusBO electricityPriceBindNodeStatusBO = proxyElectricityPriceBindService.getNodeBindPriceStatusByEntId(entId);
        ElectricityPriceBindNodeStatusRespVO electricityPriceBindNodeStatusRespVO = ElectricityPriceBindConverMapper.INSTANCE.ElectricityPriceBindNodeStatusBOTOVO(electricityPriceBindNodeStatusBO);
        return RdfaResult.success(electricityPriceBindNodeStatusRespVO);
    }

    /**
     * 根据绑定区域获取有效电价版本列表
     * 护了A省B市C区，如果A省B市C区下存在当前时间点生效的电价体系规则，则此处区域展示A省B市C区；如果C区下不存在代购电价规则，
     * 则向上看B市是否存在，如果存在，则展示A省B市；同理，如果B市下不存在，A省存在，则展示A省；如果A省都不存在，则展示“暂无数据”
     *
     * @param electricityPriceBindVersionsReqVO
     * @return
     */
    @PostMapping("/getElectricityPriceVersionByBindArea")
    @ApiOperation("获取版本列表")
    public RdfaResult<ElectricityPriceVersionsByBindAreaRespVO> getElectricityPriceVersionByBindArea(@RequestBody ElectricityPriceBindVersionsReqVO electricityPriceBindVersionsReqVO) {
        ElectricityPriceBindVersionsBO electricityPriceBindVersionsBO = ElectricityPriceVersionCreateBOConvertMapper.INSTANCE.electricityPriceBindVersionsReqVOToBO(electricityPriceBindVersionsReqVO);
        ElectricityPriceVersionsByBindAreaBO electricityPriceVersionsByBindAreaBO = proxyElectricityPriceBindService.queryElectricityPriceVersionByBindArea(electricityPriceBindVersionsBO);
        List<ElectricityPriceVersionRespVO> priceVersionRespVOList = ElectricityPriceVersionUpdateConverMapper.INSTANCE.electricityPriceVersionRespBOListToVOList(electricityPriceVersionsByBindAreaBO.getElectricityPriceVersionBOS());
        ElectricityPriceVersionsByBindAreaRespVO electricityPriceVersionsByBindAreaRespVO = ElectricityPriceVersionsByBindAreaRespVO.builder().electricityPriceVersionRespVOList(priceVersionRespVOList).provinceCode(electricityPriceVersionsByBindAreaBO.getProvinceCode()).cityCode(electricityPriceVersionsByBindAreaBO.getCityCode()).districtCode(electricityPriceVersionsByBindAreaBO.getDistrictCode()).build();
        return RdfaResult.success(electricityPriceVersionsByBindAreaRespVO);
    }


    private RdfaResult<Boolean> doBindPrice(ElectricityPriceBindReqVO electricityPriceBindReqVO, Integer changeType) {
//        ElectricityPriceBindBO electricityPriceBindBO = BeanUtil.toBean(electricityPriceBindReqVO, ElectricityPriceBindBO.class);
//        electricityPriceBindBO.setNextVersionStructurePriceBO(BeanUtil.copyProperties(electricityPriceBindReqVO.getNextVersionStructurePrice(), ElectricityPriceBindBO.NextVersionStructurePriceBO.class));
        ElectricityPriceBindBO electricityPriceBindBO = ElectricityPriceVersionCreateBOConvertMapper.INSTANCE.priceBindReqVOToBO(electricityPriceBindReqVO);
        electricityPriceBindBO.setAdjust(electricityPriceBindReqVO.getAdjust().byteValue());
        electricityPriceBindBO.setNextChangeFlag(electricityPriceBindReqVO.getNextChangeFlag().byteValue());
        electricityPriceBindBO.setChangeType(changeType);
        String lockKey = CommonConstant.LOCK_KEY + CommonConstant.KEY_SPERATOR + ChangeTypeEum.findMsgByType(changeType).getMsg() + CommonConstant.KEY_SPERATOR + CommonConstant.RedisKey.LOCK_PROXY_PRICE_BIND_PREFIX + CommonConstant.KEY_SPERATOR + electricityPriceBindBO.getNodeId();
        Lock lock = null;
        try {
            lock = disLockService.tryLock(lockKey, CommonConstant.LOCK_TIME_OUT, CommonConstant.LOCK_LEASE_TIME, CommonConstant.LOCK_REPEAT_TIMES);
            if (lock != null) {
                proxyElectricityPriceBindService.bindProxyElectricityPrice(electricityPriceBindBO);
                return RdfaResult.success(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), null);
            }
        } catch (PriceException e) {
            log.error(e.getMessage(), e);
            return RdfaResult.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return RdfaResult.fail(ResponseCode.FAIL.getCode(), ResponseCode.FAIL.getMessage());
        } finally {
            disLockService.unlock(lock);
        }
        return RdfaResult.fail(ErrorCodeEnum.REIDS_LOCK_ERROR.getErrorCode(), ErrorCodeEnum.REIDS_LOCK_ERROR.getErrorMsg());
    }

}
