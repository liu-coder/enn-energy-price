package com.enn.energy.price.starter.run;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;
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
@MapperScan("com.enn.energy.price.dal.mapper")
@EnableDubbo(scanBasePackages = {"com.enn.energy.price.facade"})
@EnableRdfaSentinel
@EnableKnife4j
@EnableFeignClients("com.enn.energy.price.integration")
@EnableTransactionManagement
public class ApplicationRunner implements CommandLineRunner{
    private static final Logger logger=LoggerFactory.getLogger(ApplicationRunner.class);

    public static void main(String[] args) {
        SpringApplication.run(ApplicationRunner.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("app start");
    }
}
