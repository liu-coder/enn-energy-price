package com.enn.energy.price.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.rdfa.framework.cache.api.CacheClient;

@Service
public class CacheService {

    private static Logger logger = LoggerFactory.getLogger(CacheService.class);

    @Autowired
    public CacheClient cacheClient;

    public <T> void setData(String key, String funcPrefix, T value) {
        logger.info("That app setting the cache info includes key is {}, funcPrefix is {}, value is {}.", key, funcPrefix, value.toString());
        this.cacheClient.vSet(key, funcPrefix, value);
    }

    public <T> T getData(String key, String funcPrefix) {
        T value = (T) this.cacheClient.vGet(key, funcPrefix);
        logger.info("The getting cache info includes key is {}, funcPrefix is {}, which result is {}. ", key, funcPrefix, value.toString());
        return value;
    }


}
