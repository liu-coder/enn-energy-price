package com.enn.energy.price.common.error;

/**
 * 错误代码.
 *
 * @author : wuchaon
 * @version : 1.0 2021/10/9 10:20
 * @since : 1.0
 **/
public enum ErrorCodeEnum {

    METHOD_ARGUMENT_VALID_EXCEPTION("1001","方法参数校验异常"),
    VALIDATION_CODE_EXCEPTION("1002","数据校验异常"),
    CONSTRAINT_VIOLATION_EXCEPTION("1003","违法约束异常"),
    DUPLICATE_KEY_EXCEPTION("1004","数据重复提交异常"),
    HTTP_MESSAGE_NOT_READABLE_EXCEPTION("1004","数据格式异常"),
    NON_EXISTENT_DATA_EXCEPTION("1005","数据不存在异常"),
    INVALID_TOKEN_ERROR("400","用户令牌无效");

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
