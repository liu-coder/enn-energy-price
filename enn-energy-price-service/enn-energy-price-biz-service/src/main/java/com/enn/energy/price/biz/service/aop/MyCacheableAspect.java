package com.enn.energy.price.biz.service.aop;

import java.io.Serializable;
import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.Ordered;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import com.enn.energy.price.common.constants.CommonConstant;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import top.rdfa.framework.biz.ro.RdfaResult;
import top.rdfa.framework.cache.api.CacheClient;

/**
 * 一次性请求id检测
 * 
 * @author wenjianpinga
 */
@Aspect
@Component
@Slf4j
public class MyCacheableAspect implements Ordered {

	@Resource
	private CacheClient cacheClient;

	@SuppressWarnings("unchecked")
	@Around("@annotation(com.enn.energy.price.biz.service.aop.MyCacheable)")
	public Object cache(ProceedingJoinPoint pjp) throws Throwable {
		Object[] args = pjp.getArgs();
		if (ObjectUtil.isEmpty(args)) {
			return pjp.proceed();
		}

		Method method = getMethod(pjp);
		Signature signature = pjp.getSignature();
		String signName = signature.getName();

		MyCacheable cacheable = method.getAnnotation(MyCacheable.class);
		
		if (cacheable == null) {
			return pjp.proceed();
		}
		
		Class<?> classType = method.getReturnType();
		boolean useWrapper = false;
		if (classType.equals(RdfaResult.class)) {
			useWrapper = true;
		}
		String key = getSpelKey(pjp, cacheable.key());
//		System.out.println("cache key is" + key);
		String prefix = CommonConstant.CACHE_PREFIX;

		Object result = cacheClient.vGet(key, prefix);
		
		if (result != null) {
			log.debug("get value form cache {}", key);
			if(useWrapper) {
				@SuppressWarnings("rawtypes")
				RdfaResult wapperResult = new RdfaResult(true, "0", "");
				wapperResult.setData((Serializable) result);
				return wapperResult;
			}
			return result;
		}

		result = pjp.proceed();

		if (needCache(result)) {
			log.debug("method process success , cache for key {}", key);
			if (useWrapper) {
				cacheClient.vSetIfAbsentWithTimeout(key, prefix, ((RdfaResult) result).getData(),
						cacheable.timeout() * 1000);
			} else {
				cacheClient.vSetIfAbsentWithTimeout(key, prefix, result, cacheable.timeout() * 1000);
			}
		}

		return result;

	}


	private boolean needCache(Object result) {
		if (result == null) {
			return false;
		}
		
		if(result instanceof RdfaResult) {
			if (!((RdfaResult) result).isSuccess()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 取得field中spel表达是的值
	 * 
	 * @param pjp
	 * @param field
	 * @return
	 */
	private String getSpelKey(ProceedingJoinPoint pjp, String field) {
		Method method = getMethod(pjp);
		Object[] args = pjp.getArgs();

		LocalVariableTableParameterNameDiscoverer u =

				new LocalVariableTableParameterNameDiscoverer();

		String[] paraNameArr = u.getParameterNames(method);

		// 应用SPEL进行key的解析
		ExpressionParser parser = new SpelExpressionParser();

		// SPEL高低文
		StandardEvaluationContext context = new StandardEvaluationContext();

		// 把办法参数放入SPEL高低文中
		for (int i = 0; i < paraNameArr.length; i++) {
			context.setVariable(paraNameArr[i], args[i]);
		}
		if (field.contains(",")) {
			return getKeyForStr(parser, context, field);
		}
		String value = parser.parseExpression(field).getValue(context, String.class);
		return value;
	}

	private String getKeyForStr(ExpressionParser parser, StandardEvaluationContext context, String field) {
		StringBuffer value = new StringBuffer();
		String[] valus = field.split(",");
		for (String lv : valus) {

			if (lv.contains("#")) {
				String rv = parser.parseExpression(lv).getValue(context, String.class);
				value.append(rv);
			} else {
				value.append(lv);
			}
			value.append("-");
		}
		String val = value.toString();
		val = val.substring(0, val.length() - 1);
		return val;
	}

	/**
	 * 获取被拦截方法对象
	 * 
	 * MethodSignature.getMethod() 获取的是顶层接口或者父类的方法对象 而我们的注解在实现类的方法上
	 * 所以应该使用反射获取当前对象的方法对象
	 */
	public Method getMethod(ProceedingJoinPoint pjp) {
		// 获取参数的类型
		Object[] args = pjp.getArgs();
		@SuppressWarnings("rawtypes")
		Class[] argTypes = new Class[pjp.getArgs().length];
		for (int i = 0; i < args.length; i++) {
			if (args[i] != null) {
				argTypes[i] = args[i].getClass();
			}
		}
		Method method = null;
		try {
			method = pjp.getTarget().getClass().getMethod(pjp.getSignature().getName(), argTypes);
		} catch (NoSuchMethodException e) {
			log.error("", e);
		} catch (SecurityException e) {
			log.error("", e);
		}
		return method;
	}

	@Override
	public int getOrder() {
		return 100;
	}
}
