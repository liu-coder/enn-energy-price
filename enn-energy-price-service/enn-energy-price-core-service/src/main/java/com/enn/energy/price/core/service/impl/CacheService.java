package com.enn.energy.price.core.service.impl;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import top.rdfa.framework.cache.api.CacheClient;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class CacheService {

    private static Logger logger = LoggerFactory.getLogger(CacheService.class);

    @Autowired
    public CacheClient cacheClient;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${rdfa.redis.app-prefix}")
    private String appPrefix;

    public <T> void setData(String key, String funcPrefix, T value) {
        logger.info("That app setting the cache info includes key is {}, funcPrefix is {}, value is {}.", key, funcPrefix, value.toString());
        this.cacheClient.vSet(key, funcPrefix, value);
    }

    public <T> T getData(String key, String funcPrefix) {
        T value = (T) this.cacheClient.vGet(key, funcPrefix);
        logger.info("The getting cache info includes key is {}, funcPrefix is {}, which result is {}. ", key, funcPrefix, value.toString());
        return value;
    }

    public Set<String> getHKeys(String key, String funcPrefix){
        BoundHashOperations<String, String, Object> operations = redisTemplate.boundHashOps(appPrefix + ":" + funcPrefix + ":" + key);
        Set<String> keys = operations.keys();
        return keys;
    }

    public Set<String> getHKeysWithPattern(String key, String funcPrefix,String pattern){
        BoundHashOperations<String, String, Object> operations = redisTemplate.boundHashOps(appPrefix + ":" + funcPrefix + ":" + key);
        Set<String> keys = operations.keys();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()){
            if (!iterator.next().contains(pattern)){
                iterator.remove();
            }
        }
        return keys;
    }

    public <T> T getHashData(String key, String funcPrefix,String hKey) {
        T value = (T) this.cacheClient.hGet(key, funcPrefix,hKey);
//        logger.info("The getting cache info includes key is {}, funcPrefix is {}, which result is {}. ", key, funcPrefix, value.toString());
        return value;
    }

    public <T> List<T> getListHashData(String key, String funcPrefix, String hKey, Class<T> clazz) {
        String value = this.cacheClient.hGet(key, funcPrefix,hKey);
        return JSON.parseArray(value,clazz);
    }

    public <HV> void hPut(String key, String funcPrefix, String hashKey, HV value) {
        cacheClient.hPut(key,funcPrefix,hashKey,value);
    }

    public <HV> void hPutWithTimeOut(String key, String funcPrefix, String hashKey, HV value,Long expireTime) {
        cacheClient.hPut(key,funcPrefix,hashKey,value);
        redisTemplate.expire(appPrefix + ":" + funcPrefix + ":" + key,expireTime, TimeUnit.SECONDS);
    }

    public <T> void hdelHashKey(String key, String funcPrefix,Object... hashKeys) {
        logger.info("That app setting the cache info includes key is {}, funcPrefix is {}, hashKey is {}.", key, funcPrefix, hashKeys);
        this.cacheClient.hDelete(key,funcPrefix,hashKeys);
    }

    //倒叙排列 hkeys
    public Set<String> getSortHKeys(String key, String funcPrefix){
        BoundHashOperations<String, String, Object> operations = redisTemplate.boundHashOps(appPrefix + ":" + funcPrefix + ":" + key);
        Set<String> keys = operations.keys();
        Set<String> sortKeys = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });
        sortKeys.addAll(keys);
        return sortKeys;
    }
    public static void main(String[] args) {
        Set<String> s1 = new HashSet<>();
        s1.add("2021-01-01");
        s1.add("2022-01-01");
        s1.add("2023-01-01");
        s1.add("2024-01-01");
        s1.add("2025-05-01");
        s1.add("2026-07-01");
        s1.forEach(s -> {
            System.out.println(s);
        });
        Set<String> sortKeys = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });
        sortKeys.addAll(s1);
        System.out.println("==================");
        sortKeys.forEach(s -> {
            System.out.println(s);
        });
    }
}
