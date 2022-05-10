package com.enn.energy.price.processor;

import com.enn.energy.price.biz.service.proxyelectricityprice.CityWeatherService;
import com.enn.energy.price.common.utils.PriceDateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.rdfa.timer.client.annotation.RdfaJob;
import top.rdfa.timer.client.handler.RdfaJobHandler;

import java.util.Date;

/**
 * 获取天气温度.
 *
 * @author : wuchaon
 * @version : 1.0 2022/5/7 9:59
 * @since : 1.0
 **/
@Slf4j
@Component
@RdfaJob("cityWeatherJob")
public class CityWeatherJob extends RdfaJobHandler {

    @Autowired
    private CityWeatherService cityWeatherService;

    /**
     * 生成指定城市的温度
     * 从指定日期开始，一直到当天，每次获取当天和下一天的天气
     *
     * @param s
     * @return
     */
    @Override
    protected boolean doExecute(String s) {

        log.info("执行获取城市温度JOB");
        Date date = new Date();
        if (StringUtils.isNotEmpty(s)) {
            date = PriceDateUtils.strToDayTimeDate(s);
        }

        String weatherResult = null;
        String cityCode = "440100";

        while (date.compareTo(new Date()) < 1) {
            try {
                weatherResult = cityWeatherService.getCityWeather(cityCode, date);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
            cityWeatherService.setCityTemperature(weatherResult, cityCode, date);
            date = PriceDateUtils.getDeltaDay(date, 1);
        }
        log.info("获取城市温度JOB完成");
        return true;
    }
}
