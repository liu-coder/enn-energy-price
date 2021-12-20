package com.enn.energy.price.biz.service.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 一次性请求注解
 * 
 * @author wenjianpinga
 *
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyCacheable {
	/**
	 * 超时时间，秒
	 * 
	 * @return
	 */
	int timeout() default 600;

	/**
	 * 缓存的key，不包括应用的前缀，支持spel表达式
	 * 
	 * @return
	 */
	String key();

//	Class<T> classType();

//	String value();

//	String condition();
}
