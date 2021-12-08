package com.enn.energy.price.starter.run;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import top.rdfa.framework.sentinel.spring.EnableRdfaSentinel;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import top.rdfa.framework.auth.client.annotation.EnableUnifiedAuthenticationClient;
import top.rdfa.framework.auth.client.constant.RunTypeEnum;
/**
 *  starter
 *
 * @author anyone
 * @since Wed Dec 08 15:51:28 CST 2021
 */
@SpringBootApplication(scanBasePackages = { "top.rdfa", "com.enn.energy.price" })
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableDiscoveryClient
@EnableDubbo(scanBasePackages = {"com.enn.energy.price.facade"})
@EnableRdfaSentinel
public class TestApplicationRunner {
    private static final Logger logger= LoggerFactory.getLogger(TestApplicationRunner.class);

    public static void main(String[] args) {
        SpringApplication.run(TestApplicationRunner.class, args);
        logger.info("app start");
    }

}
