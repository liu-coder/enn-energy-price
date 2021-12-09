package com.enn.energy.price.web.controller;

import com.enn.energy.price.common.utils.PriceDateUtils;
import com.enn.energy.price.core.service.impl.CacheService;
import com.enn.energy.price.web.dto.CacheDemoDelKeyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import top.rdfa.framework.biz.ro.RdfaResult;

import java.util.Date;


@RestController
@RequestMapping("/redisCache")
public class CacheDemoController {


    @Autowired
    public CacheService cacheService;
    @Autowired
    public StringRedisTemplate redisTemplate;
    @PostMapping("/saveData/{funcPrefix}/{key}")
    public <T> T saveDataInCache(@PathVariable String key, @PathVariable String funcPrefix, @RequestBody T value) {
        this.cacheService.setData(key, funcPrefix, value);
        return (T) "success";
    }

    @PostMapping("/saveData/{key}")
    public String saveDataInCache(@PathVariable String key) {
        redisTemplate.opsForValue().set(key,"谢谢配合");
        return "success";
    }

    @GetMapping("/getData/{funcPrefix}/{key}")
    public <T> T getDataFromCache(@PathVariable  String key, @PathVariable String funcPrefix) {
        return this.cacheService.getData(key, funcPrefix);
    }

    @PostMapping("/redis/del/key")
    public RdfaResult delDataFromCache(@RequestBody CacheDemoDelKeyDto dto) {
        Boolean delete = redisTemplate.delete(dto.getKey());
        return delete.booleanValue()? RdfaResult.success(""):RdfaResult.fail("1111","失败");
    }

    @PostMapping("/demo/thread")
    public RdfaResult thread() throws InterruptedException {
        for (int i = 0; i<1000000;i++){
            new Thread(i+""){
                @Override
                public void run() {
                    System.out.println("线程名称 ： " +Thread.currentThread().getName());
                    String dateToStr = PriceDateUtils.dayDateToStr(new Date());
                    System.out.println("dateToStr ： " + dateToStr);
                }
            }.start();
        }
        while (Thread.activeCount()<3){
            break;
        }
        return RdfaResult.success("");
    }

}
