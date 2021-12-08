package com.enn.energy.price.starter.run;

import com.enn.energy.price.starter.run.TestApplicationRunner;
import com.enn.energy.price.web.controller.*;
import org.apache.http.util.Asserts;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import top.rdfa.framework.auth.facade.entity.UserInfo;
import top.rdfa.framework.biz.ro.RdfaResult;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationRunner.class)
public class IntegrationTestWebController {

    @Autowired
    CacheDemoController cacheDemoController;

    @Autowired
    ConfigserviceDemoController configserviceDemoController;

    @Autowired
    DisLockDemoController disLockDemoController;

    @Autowired
    RdfaOssDemoController rdfaOssDemoController;

    @Autowired
    RdfaProducerDemoController rdfaProducerDemoController;

    @Autowired
    SsoDemoController ssoDemoController;

    @Test
    public void testCache(){
        long nowT = System.currentTimeMillis();
        String standard = "Test_" + nowT;
        String funcPrefix = "integration";
        String cacheRes = cacheDemoController.saveDataInCache(standard, funcPrefix, standard);
        Assert.assertEquals("success", cacheRes);

        Object dataFromCache = cacheDemoController.getDataFromCache(standard, funcPrefix);
        Assert.assertEquals(standard, dataFromCache.toString());

    }


    @Test
    public void testConfig(){
        String loggerLevel = configserviceDemoController.getLoggerLevel();
        Asserts.notEmpty(loggerLevel, "Test Config");
    }

    @Test
    public void testDisLock(){
        long nowT = System.currentTimeMillis();
        String lockKey = "Test_" + nowT;
        RdfaResult<String> lockRes = disLockDemoController.lock(lockKey);
        Assert.assertEquals("200", lockRes.getCode());
    }

    @Test
    public void testOss() {
        HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        try {
            String download = rdfaOssDemoController.download("https://lfrz1.stor.enncloud.cn/ennew-dev/public/qT8I30Ep8LClHsA9/rdfa-demo/easterEgg.png", httpServletResponse);
            Assert.assertEquals("success", download);
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public void testProducer(){
        // check "test rdfa mq convertAndSend 1" from console after this;
        String producerRes = rdfaProducerDemoController.sendMessage();
        Assert.assertEquals("send success", producerRes);
    }

    @Test
    public void testSso(){
        RdfaResult<String> unAuthRes = ssoDemoController.unAuthorize();
        Assert.assertEquals("200", unAuthRes.getCode());
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        try {
            RdfaResult<UserInfo> authRes = ssoDemoController.authorizeUserInfo(mockHttpServletRequest);
        }catch (NullPointerException e){
            Assert.assertTrue(true);
            return;
        }
        Assert.fail();
    }

}