package com.enn.energy.price.facade;


import cn.hutool.core.bean.BeanUtil;
import com.enn.energy.price.client.dto.request.*;
import com.enn.energy.price.client.service.ElectricityPriceDubboService;
import com.enn.energy.price.biz.service.ElectricityPriceService;
import com.enn.energy.price.biz.service.bo.*;
import com.enn.energy.price.common.enums.ResponseCode;
import com.enn.energy.price.common.error.ErrorCodeEnum;
import com.enn.energy.price.common.utils.PriceDateUtils;
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

import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 电价同步.
 *
 * @author : wuchaon
 * @version : 1.0 2021/11/19 17:27
 * @since : 1.0
 **/
@Api(tags = {"电价同步服务"})
@RestController
@Slf4j
@DubboService(version = "1.0.0", protocol = {"dubbo"})
public class ElectricityPriceDubboServiceImpl implements ElectricityPriceDubboService {
    @Autowired
    private ElectricityPriceService electricityPriceService;

    @Override
    @PostMapping(value = "/addElectricityPrice")
    public RdfaResult<String> addElectricityPrice(@RequestBody @NotNull @Validated ElectricityPriceVersionDTO electricityPriceVersionDTO) {

        //校验DTO
        RdfaResult<String> validateResult = validateDTO(electricityPriceVersionDTO);
        if (!validateResult.isSuccess()) {
            return validateResult;
        }

        ElectricityPriceVersionBO electricityPriceVersionBO = BeanUtil.toBean(electricityPriceVersionDTO, ElectricityPriceVersionBO.class);
        convertBO(electricityPriceVersionDTO, electricityPriceVersionBO);
        electricityPriceService.addElectricityPrice(electricityPriceVersionBO);
        return RdfaResult.success(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), null);
    }

    @Override
    @PostMapping(value = "/updateElectricityPrice")
    public RdfaResult<String> updateElectricityPrice(@RequestBody @NotNull ElectricityPriceVersionDTO electricityPriceVersionDTO) {

        //校验DTO
        if (StringUtils.isEmpty(electricityPriceVersionDTO.getVersionId())) {
            return RdfaResult.fail(ResponseCode.FAIL.getCode(), "价格版本id不能为空");
        }
        RdfaResult<String> validateResult = validateDTO(electricityPriceVersionDTO);
        if (!validateResult.isSuccess()) {
            return validateResult;
        }

        ElectricityPriceVersionBO electricityPriceVersionBO = BeanUtil.toBean(electricityPriceVersionDTO, ElectricityPriceVersionBO.class);
        convertBO(electricityPriceVersionDTO, electricityPriceVersionBO);
        electricityPriceService.updateElectricityPrice(electricityPriceVersionBO);
        return RdfaResult.success(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), null);
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
            result = electricityPriceService.delElectricityPrice(delDTO.getVersionId(),delDTO.getEquipmentId(), false);
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
            electricityPriceService.delElectricityPrice(delDTO.getVersionId(),delDTO.getEquipmentId(), true);
        } catch (Exception e) {
            log.error("delete version has error , {} ", e.getMessage());
            return RdfaResult.fail("E30003", "delete version has error");
        }

        return RdfaResult.success(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), null);

    }

    private RdfaResult<String> validateDTO(ElectricityPriceVersionDTO electricityPriceVersionDTO) {

//        if (electricityPriceVersionDTO.getStartDate().compareTo(electricityPriceVersionDTO.getEndDate()) > 0) {
//            return RdfaResult.fail(ResponseCode.FAIL.getCode(), "生效时间不能大于结束时间");
//        }

        List<ElectricityPriceRuleDTO> electricityPriceRuleDTOList = electricityPriceVersionDTO.getElectricityPriceRuleDTOList();

        for (ElectricityPriceRuleDTO electricityPriceRuleDTO : electricityPriceRuleDTOList) {

            //校验季节日期
            List<ElectricityPriceSeasonDTO> electricityPriceSeasonDTOList = electricityPriceRuleDTO.getElectricityPriceSeasonDTOList();
            Collections.sort(electricityPriceSeasonDTOList);

            if (!"01-01".equals(electricityPriceSeasonDTOList.get(0).getSeaStartDate()) || !"12-31".equals(electricityPriceSeasonDTOList.get(electricityPriceSeasonDTOList.size() - 1).getSeaEndDate())) {
                return RdfaResult.fail(ErrorCodeEnum.METHOD_ARGUMENT_VALID_EXCEPTION.getErrorCode(), "日期未覆盖全年");
            }

            SimpleDateFormat format = new SimpleDateFormat("MM-dd");
            format.setLenient(false);
            for (int i = 0; i < electricityPriceSeasonDTOList.size() - 1; i++) {

                try {
                    if ("02-29".equals(electricityPriceSeasonDTOList.get(i).getSeaEndDate())) {
                        return RdfaResult.fail(ErrorCodeEnum.METHOD_ARGUMENT_VALID_EXCEPTION.getErrorCode(), "价格版本季节结束时间不允许维护:2月29日");
                    }

                    if (!PriceDateUtils.addDateByday(format.parse(electricityPriceSeasonDTOList.get(i).getSeaEndDate()), 1).equals(format.parse(electricityPriceSeasonDTOList.get(i + 1).getSeaStartDate()))) {
                        return RdfaResult.fail(ErrorCodeEnum.METHOD_ARGUMENT_VALID_EXCEPTION.getErrorCode(), "价格版本季节覆盖全年的日期格式不对");
                    }


                } catch (ParseException e) {
                    return RdfaResult.fail(ErrorCodeEnum.METHOD_ARGUMENT_VALID_EXCEPTION.getErrorCode(), "价格版本的季节日期格式错误");
                }
            }

            //校验明细时间
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            timeFormat.setLenient(false);

            for (int i = 0; i < electricityPriceSeasonDTOList.size(); i++) {

                //分时校验明细
                if (electricityPriceSeasonDTOList.get(i).getPricingMethod().contains("tp")) {
                    List<ElectricityPriceDetailDTO> electricityPriceDetailDTOList = electricityPriceSeasonDTOList.get(i).getElectricityPriceDetailDTOList();
                    Collections.sort(electricityPriceDetailDTOList);

                    if (!"00:00:00".equals(electricityPriceDetailDTOList.get(0).getStartTime()) || !"23:00:00".equals(electricityPriceDetailDTOList.get(electricityPriceDetailDTOList.size() - 1).getEndTime())) {
                        return RdfaResult.fail(ErrorCodeEnum.METHOD_ARGUMENT_VALID_EXCEPTION.getErrorCode(), "价格明细时间未覆盖全天");
                    }

                    for (int j = 0; j < electricityPriceDetailDTOList.size() - 1; j++) {
                        try {
                            if (!PriceDateUtils.addDateByHour(timeFormat.parse(electricityPriceDetailDTOList.get(j).getEndTime()), 1).equals(timeFormat.parse(electricityPriceDetailDTOList.get(j + 1).getStartTime()))) {
                                return RdfaResult.fail(ErrorCodeEnum.METHOD_ARGUMENT_VALID_EXCEPTION.getErrorCode(), "价格明细覆盖全天的时间格式不对");
                            }
                        } catch (ParseException e) {
                            return RdfaResult.fail(ErrorCodeEnum.METHOD_ARGUMENT_VALID_EXCEPTION.getErrorCode(), "价格明细时间格式错误");
                        }
                    }
                }
            }

        }

        electricityPriceVersionDTO.setEndDate(PriceDateUtils.getDesignatedDayDate("2099-12-31"));
        return RdfaResult.success(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), null);
    }


    private void convertBO(ElectricityPriceVersionDTO electricityPriceVersionDTO, ElectricityPriceVersionBO electricityPriceVersionBO) {

        ElectricityPriceEquipmentDTO electricityPriceEquipmentDTO = electricityPriceVersionDTO.getElectricityPriceEquipmentDTO();
        electricityPriceVersionBO.setElectricityPriceEquipmentBO(BeanUtil.toBean(electricityPriceEquipmentDTO, ElectricityPriceEquipmentBO.class));

        List<ElectricityPriceRuleDTO> electricityPriceRuleDTOList = electricityPriceVersionDTO.getElectricityPriceRuleDTOList();
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

//    public static void main(String[] args) {
//        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
//        try {
//
//            "04:52:00".compareTo("00:00:00");
//            "02-22".compareTo("02-21");
//            format.parse("02-20");
//            PriceDateUtils.addDateByday(format.parse("02-20"), 1).equals(format.parse("02-21"));
//            //format.setLenient(false);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }
}
