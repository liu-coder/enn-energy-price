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
	
	/**
	 * 电价设备可能都是批量查询，增加一个随机延时，避免批量失效。随机值为0-random中间的随机值，单位秒
	 */
	int random() default 0;

//	Class<T> classType();

//	String value();

//	String condition();
}
