package com.enn.energy.price.common.constants;

/**
 * @author Zhiqiang Li
 * @date 2021/11/29
 * @description:
 **/
public class CommonConstant {
    /**
     * 状态 已删除
     */
    public static final  int  DELETED = 1;
    /**
     * 状态 正常
     */
    public static final int  VALID = 0;
    /**
     *来自于UAC
     */
    public static final String SOURCE_UAC = "uacSync";

    /**
     * 缓存key 参与者中心id映射cimCode
     */

    public static final String REDIS_TENANT_ID_TO_CIM_CODE_KEY = "tenantId-to-cimCode";

    /**
     * 电价redisKey 前缀
     */
    public static final String ELECTRICITY_PRICE = "ep";

    /**
     * 缓存KEY分隔符
     */
    public static final String KEY_SPERATOR = ":";

    /**
     * 并发锁前缀
     */
    public static final String LOCK_KEY = "price";

    /**
     * 缓存值分隔符
     */
    public static final String VALUE_SPERATOR = "#";

	/**
	 * 缓存前缀
	 */
	public static final String CACHE_PREFIX = "price_electricity_";
    /**
     * 获取并发锁超时时间
     */
    public static final int LOCK_TIME_OUT = 15;

    /**
     * 并发锁释放时间
     */
    public static final int LOCK_LEASE_TIME = 1800;

    /**
     * 获取并发锁重试次数
     */
    public static final int LOCK_REPEAT_TIMES = 3;


    public static Integer VERSION_HISTORY = 1;
    public static Integer VERSION_CURRENT = 2;
    public static Integer VERSION_FUTURE = 3;
    public static Integer TIME_MONTH_DAY_SPILT = 5;
    public static String SEPARATOR_SPILT = "/";
    public static String LEAP_YEAR_FEB_END = "02-29";
    public static String NOLEAP_YEAR_FEB_END = "02-28";

    public final static String PRICE_METHOD_P = "p";

    public final static String DEFAULT_TYPE = "默认";

    /**
     * redis key
     */
    public static class RedisKey {
        public final static String LOCK_PROXY_PRICE_VERSION_CREATE_PREFIX = "proxyprice:lock:version:create";
        public final static String LOCK_PROXY_PRICE_VERSION_UPDATE_PREFIX = "proxyprice:lock:version:delete";
    }
}
