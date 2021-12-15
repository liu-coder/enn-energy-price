package com.enn.energy.price.web.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 价格版本配置类.
 *
 * @author : wuchaon
 * @version : 1.0 2021/12/7 16:37
 * @since : 1.0
 **/
@Configuration
public class PriceVersionConfig implements WebMvcConfigurer {

    @Autowired
    PriceVersionInterceptor priceVersionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 添加拦截器
//        InterceptorRegistration interceptor = registry.addInterceptor(priceVersionInterceptor);

        // 设置拦截的路径
//        interceptor.addPathPatterns("/addElectricityPrice","/updateElectricityPrice",
//                "/delElectricityPrice","/price/electricityPrice","/price/selectVersions",
//                "/price/versionDetail","/price/findDictionary");
    }

}
