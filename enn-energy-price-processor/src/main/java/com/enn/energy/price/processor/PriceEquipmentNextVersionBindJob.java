package com.enn.energy.price.processor;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.enn.energy.price.biz.service.proxyelectricityprice.ProxyElectricityPriceBindService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.rdfa.timer.client.annotation.RdfaJob;
import top.rdfa.timer.client.handler.RdfaJobHandler;

import java.util.Date;

/**
 * 电价设备下一版本如果是默认继承,处理未生成绑定关系的版本节点
 *
 * @author : quyl
 * @version : 1.0 2022/5/7 9:59
 * @since : 1.0
 **/
@Slf4j
@Component
@RdfaJob("priceEquipmentNextVersionBindJob")
public class PriceEquipmentNextVersionBindJob extends RdfaJobHandler {

    @Autowired
    private ProxyElectricityPriceBindService proxyElectricityPriceBindService;

    /**
     * 电价设备下一版本如果是默认继承，处理未生成绑定关系的版本节点（基于提前建的版本）
     *
     * @param param
     * @return
     */
    @Override
    protected boolean doExecute(String param) {
        Date day = parseParam(param);
        try {
            proxyElectricityPriceBindService.doNextVersionPriceEquipmentBind(day);
        } catch (Exception e) {
            log.error("处理下一版本价格设备绑定关系失败", e);
            return false;
        }
        return true;
    }

    /**
     * 允许传入参数，指定日期
     *
     * @param param
     * @return
     */
    private Date parseParam(String param) {
        Date day = DateUtil.parse(DateUtil.today(), DatePattern.NORM_DATE_PATTERN);
        if (ObjectUtil.isNotEmpty(param)) {
            day = DateUtil.parse(param, DatePattern.NORM_DATE_PATTERN);
        }
        return day;
    }
}
