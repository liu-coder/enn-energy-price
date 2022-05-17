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
    RETRY_AFTER("E30006","稍后重试"),
    DOWNLOAD_TEMPLATE_EXCEPTION("E30007","模板下载异常"),
    UPLOAD_TEMPLATE_EXCEPTION("E30008","模板上传异常"),
    VERSION_IS_NOT_ALLOW_DELETE("E30009","只允许删除未来版本"),
    STRUCTURE_IS_NOT_ALLOW_DELETE("E30010","历史体系不允许删除"),
    PROVINCE_CODE_CAN_NOT_NULL("E30011","省编码不能为空"),
    ILLEGAL_INDUSTRY("E30012","用电分类不合法"),
    ILLEGAL_STRATEGY("E30013","定价类型不合法"),
    ILLEGAL_VOLTAGElEVEL("E30014","电压等级不合法"),
    ILLEGAL_NUMBER0("E30015","最大需量数值不合法"),
    ILLEGAL_NUMBER1("E30016","变压器容量基础电价数值不合法"),
    ILLEGAL_NUMBER2("E30017","电度用电价格数值不合法"),
    ILLEGAL_NUMBER3("E30018","电度输配价格数值不合法"),
    ILLEGAL_NUMBER4("E30019","政府附加价格数值不合法"),
    ILLEGAL_NUMBER5("E30020","峰时阶段价格数值不合法"),
    ILLEGAL_NUMBER6("E30021","尖时阶段价格数值不合法"),
    ILLEGAL_NUMBER7("E30022","平时阶段价格数值不合法"),
    ILLEGAL_NUMBER8("E30023","谷时阶段价格数值不合法"),
    TEMPLATE_FORMAT_ILLEGAL("E30024","模板格式不合法"),
    TEMPLATE_NOT_EXISTS("E30025","模板上传异常")
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
