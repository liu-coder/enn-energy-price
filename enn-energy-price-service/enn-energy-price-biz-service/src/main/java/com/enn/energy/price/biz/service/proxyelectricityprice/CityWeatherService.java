package com.enn.energy.price.biz.service.proxyelectricityprice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.enn.energy.price.common.constants.CommonConstant;
import com.enn.energy.price.common.utils.PriceDateUtils;
import com.enn.energy.price.core.service.impl.PriceCacheClientImpl;
import com.enn.energy.price.dal.mapper.ext.proxyprice.CityWeatherExtMapper;
import com.enn.energy.price.dal.po.mbg.CityWeather;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * 城市天气.
 *
 * @author : wuchaon
 * @version : 1.0 2022/5/7 11:06
 * @since : 1.0
 **/
@Service
@Slf4j
public class CityWeatherService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PriceCacheClientImpl priceCacheClientImpl;

    @Value("${bigData.url}")
    private String url;

    @Value("${rdfa.redis.cityTemp.timeOut}")
    private String timeOut;

    @Autowired
    private CityWeatherExtMapper cityWeatherExtMapper;

    /**
     * 根据指定日期从指标平台获取对应城市的天气
     *
     * @param cityCode
     * @param date
     * @return
     * @throws InterruptedException
     */
    public String getCityWeather(String cityCode, Date date) throws InterruptedException {

        JSONObject requestParam = new JSONObject();
        JSONArray params = new JSONArray();
        JSONObject body = new JSONObject();
        body.put("cityId", cityCode);
        String dateTime = PriceDateUtils.dayTimeDateToStr(date);
        body.put("predictDate", dateTime);
        body.put("startTime", dateTime);
        body.put("endTime", dateTime);
        params.add(body);
        requestParam.put("params", params);

        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.add(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        HttpEntity entity = new HttpEntity(JSON.toJSONString(requestParam), headers);
        String weatherResult = null;
        int repeatTimes = 0;
        while (repeatTimes < 3) {
            try {
                weatherResult = restTemplate.postForObject(url, entity, String.class);
                log.info("城市天气返回:{}", weatherResult);
                break;
            } catch (Exception e) {
                log.error("获取城市天气失败:{}", e.getMessage());
                Thread.sleep(1000);
                repeatTimes++;
            }
        }
        return weatherResult;
    }


    /**
     * 生成指定日期城市的温度
     * 缓存默认60天
     *
     * @param weatherResult
     * @param cityCode
     * @param date
     */
    @Transactional(rollbackFor = Exception.class)
    public void setCityTemperature(String weatherResult, String cityCode, Date date) {

        JSONObject resultObj;
        try {
            resultObj = JSONObject.parseObject(weatherResult);
        } catch (Exception e) {
            log.error("获取城市天气返回的格式异常");
            return;
        }

        if (null == resultObj || !"0".equals(resultObj.getString("retCode"))) {
            log.error("获取城市天气失败:{}", weatherResult);
            return;
        }

        JSONArray citys = resultObj.getJSONArray("citys");
        JSONObject city = citys.getJSONObject(0);
        JSONArray times = city.getJSONArray("times");
        JSONObject time = times.getJSONObject(0);
        JSONArray results = time.getJSONArray("result");
        int num = 0;
        Map<String, String> cache = new HashMap<>();
        List<CityWeather> dbList = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            JSONObject result = results.getJSONObject(i);
            CityWeather cityWeather = new CityWeather();
            cityWeather.setAreaCode(cityCode);
            cityWeather.setDate(result.getDate("date"));
            cityWeather.setTempDay(result.getString("temp_day"));
            cityWeather.setTempNight(result.getString("temp_night"));
            String dateStr = PriceDateUtils.dayDateToStr(result.getDate("date"));

            if (PriceDateUtils.dayDateToStr(date).equals(dateStr) || PriceDateUtils.dayDateToStr(PriceDateUtils.getDeltaDay(date, 1)).equals(dateStr)) {
                dbList.add(cityWeather);
                cache.put(cityCode + CommonConstant.KEY_SPERATOR + dateStr, cityWeather.getTempDay());
                num++;
            }
            if (num == 2) {
                break;
            }
        }
        cityWeatherExtMapper.batchInsertOrUpdate(dbList);
        //添加缓存
        cache.forEach((key, value) -> priceCacheClientImpl.vSetWithTimeOut(key, CommonConstant.ELECTRICITY_PRICE, value, Long.parseLong(timeOut)));
    }

}
