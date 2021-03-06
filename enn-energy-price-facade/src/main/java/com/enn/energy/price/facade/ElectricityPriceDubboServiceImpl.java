package com.enn.energy.price.facade;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.enn.energy.price.biz.service.ElectricityPriceService;
import com.enn.energy.price.biz.service.bo.*;
import com.enn.energy.price.client.dto.request.*;
import com.enn.energy.price.client.service.ElectricityPriceDubboService;
import com.enn.energy.price.common.constants.CommonConstant;
import com.enn.energy.price.common.enums.ResponseCode;
import com.enn.energy.price.common.error.ErrorCodeEnum;
import com.enn.energy.price.common.error.PriceException;
import com.enn.energy.price.common.utils.PriceDateUtils;
import com.enn.energy.price.core.service.impl.DisLockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.rdfa.framework.biz.ro.RdfaResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.Lock;

/**
 * 电价同步.
 *
 * @author : wuchaon
 * @version : 1.0 2021/11/19 17:27
 * @since : 1.0
 **/
@Api(tags = {"电价同步服务"})
@RestController
@Validated
@Slf4j
@DubboService(version = "1.0.0", protocol = {"dubbo"})
public class ElectricityPriceDubboServiceImpl implements ElectricityPriceDubboService {

    @Autowired
    private ElectricityPriceService electricityPriceService;

    @Autowired
    private DisLockService disLockService;


    @Override
    @PostMapping(value = "/addElectricityPrice")
    public RdfaResult<String> addElectricityPrice(ElectricityPriceVersionDTO electricityPriceVersionDTO) {

        log.info("新增电价:{}", JSON.toJSONString(electricityPriceVersionDTO));
        //校验DTO
        RdfaResult<String> validateResult = validateDTO(electricityPriceVersionDTO, null);
        if (!validateResult.isSuccess()) {
            return validateResult;
        }

        ElectricityPriceVersionBO electricityPriceVersionBO = BeanUtil.toBean(electricityPriceVersionDTO, ElectricityPriceVersionBO.class);
        convertBO(electricityPriceVersionDTO, null, electricityPriceVersionBO);

        String lockKey = CommonConstant.LOCK_KEY + CommonConstant.KEY_SPERATOR + electricityPriceVersionBO.getSystemCode() + CommonConstant.KEY_SPERATOR + electricityPriceVersionBO.getElectricityPriceEquipmentBO().getEquipmentId();
        Lock lock = null;
        try {
            lock = disLockService.tryLock(lockKey, CommonConstant.LOCK_TIME_OUT, CommonConstant.LOCK_LEASE_TIME, CommonConstant.LOCK_REPEAT_TIMES);
            if (lock != null) {
                electricityPriceService.addElectricityPrice(electricityPriceVersionBO, false);
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

    @Override
    @PostMapping(value = "/updateElectricityPrice")
    public RdfaResult<String> updateElectricityPrice(ElectricityPriceVersionUpdateDTO electricityPriceVersionUpdateDTO) {

        log.info("修改电价:{}", JSON.toJSONString(electricityPriceVersionUpdateDTO));
        RdfaResult<String> validateResult = validateDTO(null, electricityPriceVersionUpdateDTO);
        if (!validateResult.isSuccess()) {
            return validateResult;
        }

        ElectricityPriceVersionBO electricityPriceVersionBO = BeanUtil.toBean(electricityPriceVersionUpdateDTO, ElectricityPriceVersionBO.class);
        convertBO(null, electricityPriceVersionUpdateDTO, electricityPriceVersionBO);
        electricityPriceService.updateElectricityPrice(electricityPriceVersionBO);

        Lock lock = null;
        String lockKey = CommonConstant.LOCK_KEY + CommonConstant.KEY_SPERATOR + electricityPriceVersionBO.getSystemCode() + CommonConstant.KEY_SPERATOR + electricityPriceVersionBO.getElectricityPriceEquipmentBO().getEquipmentId();
        try {
            lock = disLockService.tryLock(lockKey, CommonConstant.LOCK_TIME_OUT, CommonConstant.LOCK_LEASE_TIME, CommonConstant.LOCK_REPEAT_TIMES);
            if (lock != null) {
                electricityPriceService.addElectricityPrice(electricityPriceVersionBO, true);
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
     * 罗森 - 删除未生效的版本
     *
     * @param delDTO
     * @return
     */
    @Override
    @PostMapping(value = "/delElectricityPrice")
    @ApiOperation("删除电价版本")
    public RdfaResult<String> delElectricityPrice(@Validated @RequestBody ElectricityPriceVersionDelDTO delDTO) {
        RdfaResult result = null;
        try {
            result = electricityPriceService.delElectricityPrice(delDTO.getVersionId(), delDTO.getEquipmentId(), delDTO.getSystemCode(), false);
        } catch (Exception e) {
            log.error("delete version has error , {} ", e.getMessage());
            return RdfaResult.fail("E30003", "delete version has error");
        }

        return result;
    }

    /**
     * 通用版本-删除
     *
     * @param delDTO
     * @return
     */
    @PostMapping(value = "/delElectricityPriceForCommon")
    @ApiOperation("删除电价版本同步通用")
    @Override
    public RdfaResult<String> delElectricityPriceForCommon(@RequestBody ElectricityPriceVersionDelDTO delDTO) {
        try {
            electricityPriceService.delElectricityPrice(delDTO.getVersionId(), delDTO.getEquipmentId(), delDTO.getSystemCode(), true);
        } catch (Exception e) {
            log.error("delete version has error , {} ", e.getMessage());
            return RdfaResult.fail("E30003", "delete version has error");
        }

        return RdfaResult.success(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), null);

    }

    @Override
    @PostMapping(value = "/batchAddElectricityPrice")
    public RdfaResult<String> batchAddElectricityPrice(List<ElectricityPriceVersionDTO> electricityPriceVersionDTOList) {

        if (electricityPriceVersionDTOList.size() > 2000) {
            return RdfaResult.fail(ErrorCodeEnum.VALIDATION_CODE_EXCEPTION.getErrorCode(), "条数不能超过2000");
        }

        Set<String> checkRepeat = new HashSet<>();
        List<ElectricityPriceVersionBO> electricityPriceVersionBOList = new ArrayList<>();

        for (ElectricityPriceVersionDTO electricityPriceVersionDTO : electricityPriceVersionDTOList) {
            if (checkRepeat.contains(electricityPriceVersionDTO.getSystemCode() + electricityPriceVersionDTO.getElectricityPriceEquipmentDTO().getEquipmentId())) {
                return RdfaResult.fail(ErrorCodeEnum.CONSTRAINT_VIOLATION_EXCEPTION.getErrorCode(), "存在相同的系统编码和设备id,systemCode:" + electricityPriceVersionDTO.getSystemCode() + ",equipmentId:=" + electricityPriceVersionDTO.getElectricityPriceEquipmentDTO().getEquipmentId());
            }
            checkRepeat.add(electricityPriceVersionDTO.getSystemCode() + electricityPriceVersionDTO.getElectricityPriceEquipmentDTO().getEquipmentId());

            //校验DTO
            RdfaResult<String> validateResult = validateDTO(electricityPriceVersionDTO, null);
            if (!validateResult.isSuccess()) {
                validateResult.setMessage("systemCode:" + electricityPriceVersionDTO.getSystemCode() + ",equipmentId:=" + electricityPriceVersionDTO.getElectricityPriceEquipmentDTO().getEquipmentId() + validateResult.getMessage());
                return validateResult;
            }
            ElectricityPriceVersionBO electricityPriceVersionBO = BeanUtil.toBean(electricityPriceVersionDTO, ElectricityPriceVersionBO.class);
            convertBO(electricityPriceVersionDTO, null, electricityPriceVersionBO);
            electricityPriceVersionBOList.add(electricityPriceVersionBO);
        }

        List<Lock> lockList = new ArrayList<>();
        try {
            for (int i = 0; i < electricityPriceVersionBOList.size(); i++) {
                String lockKey = CommonConstant.LOCK_KEY + CommonConstant.KEY_SPERATOR + electricityPriceVersionBOList.get(i).getSystemCode() + CommonConstant.KEY_SPERATOR + electricityPriceVersionBOList.get(i).getElectricityPriceEquipmentBO().getEquipmentId();
                Lock lock = disLockService.tryLock(lockKey, CommonConstant.LOCK_TIME_OUT, CommonConstant.LOCK_LEASE_TIME, CommonConstant.LOCK_REPEAT_TIMES);

                if (lock == null) {
                    return RdfaResult.fail(ErrorCodeEnum.REIDS_LOCK_ERROR.getErrorCode(), ErrorCodeEnum.REIDS_LOCK_ERROR.getErrorMsg());
                }
                lockList.add(lock);
                if (i == electricityPriceVersionBOList.size() - 1) {
                    electricityPriceService.batchAddElectricityPrice(electricityPriceVersionBOList);
                    return RdfaResult.success(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), null);
                }
            }
        } catch (PriceException e) {
            log.error(e.getMessage(), e);
            return RdfaResult.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            for (Lock unLock : lockList) {
                disLockService.unlock(unLock);
            }
        }

        return RdfaResult.fail(ResponseCode.FAIL.getCode(), ResponseCode.FAIL.getMessage());
    }

    private RdfaResult<String> validateDTO(ElectricityPriceVersionDTO electricityPriceVersionDTO, ElectricityPriceVersionUpdateDTO electricityPriceVersionUpdateDTO) {

        List<ElectricityPriceRuleDTO> electricityPriceRuleDTOList = electricityPriceVersionDTO != null ? electricityPriceVersionDTO.getElectricityPriceRuleDTOList() : electricityPriceVersionUpdateDTO.getElectricityPriceRuleDTOList();
        String year = String.format("%tY", electricityPriceVersionDTO != null ? electricityPriceVersionDTO.getStartDate() : electricityPriceVersionUpdateDTO.getStartDate());


        if (null != electricityPriceVersionDTO) {
            String regexp = "^[A-Za-z0-9]+$";
            String provinceCode = electricityPriceVersionDTO.getProvinceCode();
            if (StringUtils.isNotEmpty(provinceCode) && !provinceCode.matches(regexp)) {
                return RdfaResult.fail(ErrorCodeEnum.METHOD_ARGUMENT_VALID_EXCEPTION.getErrorCode(), "省、市编码必须为英文和数字");
            }

            String cityCode = electricityPriceVersionDTO.getCityCode();
            if (StringUtils.isNotEmpty(cityCode) && !cityCode.matches(regexp)) {
                return RdfaResult.fail(ErrorCodeEnum.METHOD_ARGUMENT_VALID_EXCEPTION.getErrorCode(), "市、区编码必须为英文和数字");
            }

            String districtCode = electricityPriceVersionDTO.getDistrictCode();
            if (StringUtils.isNotEmpty(districtCode) && !districtCode.matches(regexp)) {
                return RdfaResult.fail(ErrorCodeEnum.METHOD_ARGUMENT_VALID_EXCEPTION.getErrorCode(), "区、县编码必须为英文和数字");
            }
        }


        for (ElectricityPriceRuleDTO electricityPriceRuleDTO : electricityPriceRuleDTOList) {

            //校验季节日期
            List<ElectricityPriceSeasonDTO> electricityPriceSeasonDTOList = electricityPriceRuleDTO.getElectricityPriceSeasonDTOList();
            Collections.sort(electricityPriceSeasonDTOList);

            if (!"01-01".equals(electricityPriceSeasonDTOList.get(0).getSeaStartDate()) || !"12-31".equals(electricityPriceSeasonDTOList.get(electricityPriceSeasonDTOList.size() - 1).getSeaEndDate())) {
                return RdfaResult.fail(ErrorCodeEnum.METHOD_ARGUMENT_VALID_EXCEPTION.getErrorCode(), "日期未覆盖全年");
            }

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            format.setLenient(false);
            for (int i = 0; i < electricityPriceSeasonDTOList.size() - 1; i++) {

                try {
                    if (!PriceDateUtils.addDateByday(format.parse(year + "-" + electricityPriceSeasonDTOList.get(i).getSeaEndDate()), 1).equals(format.parse(year + "-" + electricityPriceSeasonDTOList.get(i + 1).getSeaStartDate()))) {
                        return RdfaResult.fail(ErrorCodeEnum.METHOD_ARGUMENT_VALID_EXCEPTION.getErrorCode(), "价格版本季节覆盖全年的日期格式不对");
                    }

                } catch (ParseException e) {
                    return RdfaResult.fail(ErrorCodeEnum.METHOD_ARGUMENT_VALID_EXCEPTION.getErrorCode(), "价格版本的季节日期格式错误");
                }
            }

            //校验明细时间
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            timeFormat.setLenient(false);

            for (int i = 0; i < electricityPriceSeasonDTOList.size(); i++) {

                //分时校验明细
                if ("tp".equals(electricityPriceSeasonDTOList.get(i).getPricingMethod())) {
                    List<ElectricityPriceDetailDTO> electricityPriceDetailDTOList = electricityPriceSeasonDTOList.get(i).getElectricityPriceDetailDTOList();
                    Collections.sort(electricityPriceDetailDTOList);

                    if (!"00:00".equals(electricityPriceDetailDTOList.get(0).getStartTime()) || !"24:00".equals(electricityPriceDetailDTOList.get(electricityPriceDetailDTOList.size() - 1).getEndTime())) {
                        return RdfaResult.fail(ErrorCodeEnum.METHOD_ARGUMENT_VALID_EXCEPTION.getErrorCode(), "价格明细时间未覆盖全天");
                    }

                    for (int j = 0; j < electricityPriceDetailDTOList.size() - 1; j++) {
                        if (!electricityPriceDetailDTOList.get(j).getEndTime().equals(electricityPriceDetailDTOList.get(j + 1).getStartTime())) {
                            return RdfaResult.fail(ErrorCodeEnum.METHOD_ARGUMENT_VALID_EXCEPTION.getErrorCode(), "价格明细覆盖全天的时间格式不对");
                        }
                    }
                }

                //单一电价
                if ("p".equals(electricityPriceSeasonDTOList.get(i).getPricingMethod())) {
                    List<ElectricityPriceDetailDTO> electricityPriceDetailDTOList = electricityPriceSeasonDTOList.get(i).getElectricityPriceDetailDTOList();
                    for (int j = 0; j < electricityPriceDetailDTOList.size(); j++) {
                        electricityPriceDetailDTOList.get(j).setStartTime(null);
                        electricityPriceDetailDTOList.get(j).setEndTime(null);
                        electricityPriceDetailDTOList.get(j).setStartStep(null);
                        electricityPriceDetailDTOList.get(j).setEndStep(null);
                        electricityPriceDetailDTOList.get(j).setStep(null);
                    }
                }


            }
        }
        return RdfaResult.success(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), null);
    }


    private void convertBO(ElectricityPriceVersionDTO electricityPriceVersionDTO, ElectricityPriceVersionUpdateDTO electricityPriceVersionUpdateDTO, ElectricityPriceVersionBO electricityPriceVersionBO) {

        if (null != electricityPriceVersionDTO) {
            ElectricityPriceEquipmentDTO electricityPriceEquipmentDTO = electricityPriceVersionDTO.getElectricityPriceEquipmentDTO();
            electricityPriceVersionBO.setElectricityPriceEquipmentBO(BeanUtil.toBean(electricityPriceEquipmentDTO, ElectricityPriceEquipmentBO.class));
        }

        List<ElectricityPriceRuleDTO> electricityPriceRuleDTOList = electricityPriceVersionDTO != null ? electricityPriceVersionDTO.getElectricityPriceRuleDTOList() : electricityPriceVersionUpdateDTO.getElectricityPriceRuleDTOList();
        List<ElectricityPriceRuleBO> electricityPriceRuleBOList = new ArrayList<>();
        electricityPriceVersionBO.setElectricityPriceRuleBOList(electricityPriceRuleBOList);

        for (ElectricityPriceRuleDTO electricityPriceRuleDTO : electricityPriceRuleDTOList) {

            ElectricityPriceRuleBO electricityPriceRuleBO = BeanUtil.toBean(electricityPriceRuleDTO, ElectricityPriceRuleBO.class);
            List<ElectricityPriceSeasonDTO> electricityPriceSeasonDTOList = electricityPriceRuleDTO.getElectricityPriceSeasonDTOList();
            List<ElectricityPriceSeasonBO> electricityPriceSeasonBOList = new ArrayList<>();

            for (ElectricityPriceSeasonDTO electricityPriceSeasonDTO : electricityPriceSeasonDTOList) {

                ElectricityPriceSeasonBO electricityPriceSeasonBO = BeanUtil.toBean(electricityPriceSeasonDTO, ElectricityPriceSeasonBO.class);
                List<ElectricityPriceDetailDTO> electricityPriceDetailDTOList = electricityPriceSeasonDTO.getElectricityPriceDetailDTOList();
                List<ElectricityPriceDetailBO> electricityPriceDetailBOList = new ArrayList<>();
                for (ElectricityPriceDetailDTO electricityPriceDetailDTO : electricityPriceDetailDTOList) {
                    electricityPriceDetailBOList.add(BeanUtil.toBean(electricityPriceDetailDTO, ElectricityPriceDetailBO.class));
                }
                electricityPriceSeasonBO.setElectricityPriceDetailBOList(electricityPriceDetailBOList);
                electricityPriceSeasonBOList.add(electricityPriceSeasonBO);

            }
            electricityPriceRuleBO.setElectricityPriceSeasonBOList(electricityPriceSeasonBOList);
            electricityPriceRuleBOList.add(electricityPriceRuleBO);
        }
    }

}
