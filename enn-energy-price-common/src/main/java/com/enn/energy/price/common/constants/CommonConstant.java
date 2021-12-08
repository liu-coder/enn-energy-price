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
     * 分隔符
     */
    public static final String KEY_SPERATOR = "_";

}
