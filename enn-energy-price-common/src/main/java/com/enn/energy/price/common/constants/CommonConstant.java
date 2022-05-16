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

    /**
     * 尖
     */
    public static final String SHARP = "0";

    /**
     * 峰
     */
    public static final String PEEK = "1";

    /**
     * 平
     */
    public static final String LEVEL = "2";

    /**
     * 谷
     */
    public static final String VALLEY = "3";


    public static Integer VERSION_HISTORY = 1;
    public static Integer VERSION_CURRENT = 2;
    public static Integer VERSION_FUTURE = 3;
    public static Integer TIME_MONTH_DAY_SPILT = 5;
    public static String SEPARATOR_SPILT = "/";
    public static String LEAP_YEAR_FEB_END = "02-29";
    public static String NOLEAP_YEAR_FEB_END = "02-28";

    public final static String PRICE_METHOD_P = "p";

    public final static String DEFAULT_TYPE = "default";

    /**
     * 日期间隔符
     */
    public static final String DATE_SPLIT = "-";
    /**
     * 一年的第一天
     */
    public static final String FIRST_DAY_OF_YEAR = "01-01";
    /**
     * 一年的最后一天
     */
    public static final String LAST_DAY_OF_YEAR = "12-31";

    /**
     * 2022-12-31
     */
    public static final String DAY_EXAMPLE = "2022-12-31 ";

    /**
     * 2022-12-31
     */
    public static final String TIME_ZERO_SPLIT = ":00";

    /**
     * :59
     */
    public static final String TIME_NINE = ":59";

    /**
     * 时间分隔符
     */
    public static final String TIME_SPLIT = ":";

    /**
     * 00
     */
    public static final String TIME_ZERO = "00";

    /**
     * 2022-12-31 00:00:00
     */
    public static final String DAY_EXAMPLE_ZERO = "2022-12-31 00:00:00";

    /**
     * 2022-12-31 11:59:59
     */
    public static final String DAY_EXAMPLE_NINE = "2022-12-31 11:59:59";

    /**
     * 2022-12-31 11:59:59
     */
    public static final String INDUSTRY = "industry";

    /**
     * 2022-12-31 11:59:59
     */
    public static final String STRATEGY = "strategy";

    /**
     * 2022-12-31 11:59:59
     */
    public static final String VOLTAGELEVEL = "voltageLevel";

    /**
     * 2022-12-31 11:59:59
     */
    public static final String INDUSTRY_CHINA = "用电分类";

    /**
     * 2022-12-31 11:59:59
     */
    public static final String STRATEGY_CHINA = "定价类型";

    /**
     * 2022-12-31 11:59:59
     */
    public static final String VOLTAGELEVEL_CHINA = "电压等级";

    /**
     * 2022-12-31 11:59:59
     */
    public static final String CONSUMPTION_CHINA = "电度用电价格(元/千瓦时)";

    /**
     * 2022-12-31 11:59:59
     */
    public static final String OTHER_CHINA = "其它(元/千瓦时)";

    /**
     * 2022-12-31 11:59:59
     */
    public static final String DISTRIBUTION_CHINA = "电度输配";

    /**
     * 2022-12-31 11:59:59
     */
    public static final String GOV_ADD_CHINA = "政府性附加";

    /**
     * 2022-12-31 11:59:59
     */
    public static final String TIME_SECTION_CHINA = "分时电度用电价格(元/千瓦时)";

    /**
     * 2022-12-31 11:59:59
     */
    public static final String SHARP_CHINA = "尖";

    /**
     * 2022-12-31 11:59:59
     */
    public static final String PEEK_CHINA = "峰";

    /**
     * 2022-12-31 11:59:59
     */
    public static final String LEVEL_CHINA = "平";

    /**
     * 2022-12-31 11:59:59
     */
    public static final String VALLY_CHINA = "谷";

    /**
     * 2022-12-31 11:59:59
     */
    public static final String CAPACITY_CHINA = "按容(需)电价格(元/千瓦·月)";

    /**
     * 2022-12-31 11:59:59
     */
    public static final String MAX_CAPACITY_CHINA = "最大需量";

    /**
     * 2022-12-31 11:59:59
     */
    public static final String TRANSFORMER_CAPACITY_CHINA = "变压器容量";

    /**
     * 区域分隔符
     */
    public static final String AREA_SPLIT = ",";

    /**
     * 默认失效日期
     */
    public static final String DEFAULT_END_DATE = "2099-12-31";

    /**
     * 数字 -1
     */
    public static final Integer NUMBER0 = -1;

    /**
     * 2022-12-31 11:59:59
     */
    public static final Integer INDUSTRY_TYPE = 0;

    /**
     * 2022-12-31 11:59:59
     */
    public static final Integer STRATEGY_TYPE = 1;

    /**
     * 2022-12-31 11:59:59
     */
    public static final Integer VOLTAGELEVEL_TYPE = 2;

    /**
     * 区域分隔符
     */
    public static final String TYPE = "type";

    /**
     * 区域分隔符
     */
    public static final String PROVINCE = "province";

    /**
     * 区域分隔符
     */
    public static final String STATE = "state";

    /**
     * 字典类型
     */
    public static final String DICTIONARY_VOLTAGELEVEL_TYPE = "1";

    /**
     * 字典类型
     */
    public static final String DICTIONARY_INDUSTRY_TYPE = "0";

    /**
     * 结束时间
     */
    public static final String END_TIME = "24:00";

    /**
     * 结束时间
     */
    public static final String START_TIME = "00:00";

    /**
     * 体系id
     */
    public static final String STRUCTURE_ID = "structureId";

    /**
     * 拼接符
     */
    public static final String REDIS_APPEND = "%s:%s:%s";


    /**
     * redis key
     */
    public static class RedisKey {
        public final static String LOCK_PROXY_PRICE_VERSION_CREATE_PREFIX = "proxyprice:lock:version:create";
        public final static String LOCK_PROXY_PRICE_VERSION_UPDATE_PREFIX = "proxyprice:lock:version:delete";
    }
}
