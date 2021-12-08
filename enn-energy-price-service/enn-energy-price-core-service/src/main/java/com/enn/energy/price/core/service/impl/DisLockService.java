package com.enn.energy.price.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.rdfa.framework.concurrent.api.exception.LockFailException;
import top.rdfa.framework.concurrent.redis.lock.RedissonRedDisLock;
import java.util.concurrent.locks.Lock;

@Component
public class DisLockService {
    private static Logger logger = LoggerFactory.getLogger(DisLockService.class);
    @Autowired
    private RedissonRedDisLock redDisLock;

    public boolean biz(String lockKey){
        Lock lock= null;
        // must use try catch finnaly to lock and unlock!
        try{
            lock = redDisLock.lock(lockKey);
            if(lock!=null){
                logger.info("lock {} success!", lockKey);
                //do the business
                return true;
            }else{
                logger.info("lock {} failed!", lockKey);
                return false;
            }
        } catch(LockFailException e){
            logger.error(e.getMessage(), e);
            return false;
        } finally{
            redDisLock.unlock(lock);
        }
    }


}
