package com.enn.energy.price.common.error;

/**
 * 错误代码.
 *
 * @author : wuchaon
 * @version : 1.0 2021/10/9 10:20
 * @since : 1.0
 **/
public enum ErrorCodeEnum {

    REQUEST_HANDLER_SUCCESS("200","请求处理成功"),
    REQUEST_HANDLER_FAIL("500","系统繁忙,请稍后再试"),
    METHOD_ARGUMENT_VALID_EXCEPTION("1001","方法参数校验异常"),
    VALIDATION_CODE_EXCEPTION("1002","数据校验异常"),
    CONSTRAINT_VIOLATION_EXCEPTION("1003","违法约束异常"),
    DUPLICATE_KEY_EXCEPTION("1004","数据重复提交异常"),
    HTTP_MESSAGE_NOT_READABLE_EXCEPTION("1004","数据格式异常"),
    NON_EXISTENT_DATA_EXCEPTION("1005","数据不存在异常"),
    INVALID_TOKEN_ERROR("400","用户令牌无效"),

    //查询接口错误提示
    SELECT_PARAM_TIME_ERROR("E20001","时间参数错误"),
    SELECT_VERSION_VALID_ERROR("E20002","未查到有效的版本数据"),
    SELECT_RULE_VALID_ERROR("E20003","未查到有效的电费规则"),
    SELECT_SEASON_VALID_ERROR("E20004","未查到有效季节性数据"),
    SELECT_DETAIL_VALID_ERROR("E20005","未查到有效的电价明细信息，请排查电价详情"),
    SELECT_EQUIPMENT_VALID_ERROR("E20006","未查到有效设备数据"),
    SELECT_CURRENT_VERSION_VALID_ERROR("E20007","未查到正在生效的版本数据"),
    SELECT_SERVICE_UNACCESS_ERROR("E20008","后端电价服务不可达，暂时无法查询"),
    SELECT_VERSION_SIZE_ERROR("E20009","批量查询版本数量过多，请效验查询数量"),
    
    REIDS_LOCK_ERROR("E30001","获取redis red lock 失败"),
    REPEAT_REQUEST("E30002", "用户重复请求"),
    REPEAT_VERSION("E30003","版本已存在"),
    STRUCTURE_NOT_EXISTS("E30004","该省下体系未配置，请确认"),
    VALIDATE_FAIL("E30005","校验不通过，请确认"),
    RETRY_AFTER("E30006","稍后重试")
    ;
    private String errorCode;
    private String errorMsg;

    ErrorCodeEnum(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
