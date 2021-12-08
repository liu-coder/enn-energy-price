package com.enn.energy.price.web.controller;

import com.enn.energy.price.core.service.impl.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/redisCache")
public class CacheDemoController {

    @Autowired
    public CacheService cacheService;

    @PostMapping("/saveData/{funcPrefix}/{key}")
    public <T> T saveDataInCache(@PathVariable String key, @PathVariable String funcPrefix, @RequestBody T value) {
        this.cacheService.setData(key, funcPrefix, value);
        return (T) "success";
    }

    @GetMapping("/getData/{funcPrefix}/{key}")
    public <T> T getDataFromCache(@PathVariable  String key, @PathVariable String funcPrefix) {
        return this.cacheService.getData(key, funcPrefix);
    }
}
