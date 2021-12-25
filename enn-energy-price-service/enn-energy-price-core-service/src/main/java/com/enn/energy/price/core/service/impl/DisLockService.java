package com.enn.energy.price.core.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.rdfa.framework.concurrent.api.exception.LockFailException;
import top.rdfa.framework.concurrent.redis.lock.RedissonRedDisLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Component
@Slf4j
public class DisLockService {
    private static Logger logger = LoggerFactory.getLogger(DisLockService.class);
    @Autowired
    private RedissonRedDisLock redDisLock;

    public boolean biz(String lockKey) {
        Lock lock= null;
        // must use try catch finnaly to lock and unlock!
        try {
            lock = redDisLock.lock(lockKey);
            if (lock != null) {
                logger.info("lock {} success!", lockKey);
                //do the business
                return true;
            } else {
                logger.info("lock {} failed!", lockKey);
                return false;
            }
        } catch (LockFailException e) {
            logger.error(e.getMessage(), e);
            return false;
        } finally {
             redDisLock.unlock(lock);
        }
    }


    /**
     * 获取并发锁
     * @param lockKey
     * @param timeout
     * @param leaseTime
     * @param repeatTimes
     * @return
     */
    public Lock tryLock(String lockKey, int timeout, int leaseTime, int repeatTimes) {
        Lock lock = null;
        int times = 0;
        while (lock == null && times <= repeatTimes) {
            try {
                lock = redDisLock.lock(lockKey, TimeUnit.SECONDS, timeout, leaseTime);
                if (lock == null) {
                    times++;
                    log.error("获取并发锁失败:{},重试次数:{}", lockKey, times);
                    continue;
                }
            } catch (LockFailException e) {
                times++;
                log.error("获取并发锁失败:{},重试次数:{}", lockKey, times);
                continue;
            }
        }
        return lock;
    }

    /**
     * 释放锁
     * @param lock
     */
    public void unlock(Lock lock) {
        redDisLock.unlock(lock);
    }

}
