package com.enn.energy.price.core.service.impl;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import top.rdfa.framework.cache.jedis.impl.CacheClientImpl;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 价格中心redis缓存操作类.
 *
 * @author : wuchaon
 * @version : 1.0 2021/12/3 15:04
 * @since : 1.0
 **/
@Service
public class PriceCacheClientImpl extends CacheClientImpl {

    @Resource(name = "redisTemplate")
    private HashOperations hashOperations;

    @Resource(name = "redisTemplate")
    private ValueOperations valueOperations;

    @Resource(name = "stringRedisTemplate")
    private RedisTemplate<String, String> stringRedisTemplate;

    public Set<Object> hashKeys(String key,String funcPrefix) {
        return hashOperations.keys(getRealKey(key, funcPrefix));

    }

}
