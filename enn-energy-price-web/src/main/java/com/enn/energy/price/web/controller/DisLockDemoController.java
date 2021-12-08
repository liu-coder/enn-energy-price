package com.enn.energy.price.web.controller;

import com.enn.energy.price.core.service.impl.DisLockService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.rdfa.framework.biz.ro.RdfaResult;

import javax.annotation.Resource;

@RestController
@RequestMapping("/disLock")
public class DisLockDemoController {

    @Resource
    private DisLockService lockService;

    @GetMapping("/lock/{lockKey}")
    public RdfaResult<String> lock(@PathVariable String lockKey){
        boolean result=lockService.biz(lockKey);
        return RdfaResult.success(result==true?"200":"500",result==true?"加锁成功":"加锁失败",lockKey);

    }

}
