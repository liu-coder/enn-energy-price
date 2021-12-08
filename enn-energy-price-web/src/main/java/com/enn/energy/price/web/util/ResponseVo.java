package com.enn.energy.price.web.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * 统一的查询结果VO
 *
 * @Author:
 * @Date: 2020/4/9 11:35
 */

@Data
@ApiModel(value = "ResponseVo.统一的返回对象")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class ResponseVo implements Serializable {
    private static final long serialVersionUID = 7748070653645596712L;
    /**
     * 状态码
     */
    @ApiModelProperty(value = "状态码")
    private Integer code;

    /**
     * 状态码对应描述信息
     */
    @ApiModelProperty(value = "状态码对应描述信息")
    private String msg;

    /**
     * 数据
     */
    @ApiModelProperty(value = "数据")
    private Object data;

    private ResponseVo() {

    }

    public ResponseVo(Integer code, String message, Object obj) {
        this.data = obj;
        this.code = code;
        this.msg = message;
    }

    /**
     * 设置正常请求的response（推荐）
     */
    public static final ResponseVo success() {
        return new ResponseVo(0, "",null);
    }

    /**
     * 设置正常请求的response（推荐）
     */
    public static final ResponseVo success(Object dataList) {
        return new ResponseVo(0, "",dataList);
    }


    public static final ResponseVo error() {
        return error("");
    }

    public static final ResponseVo error(String errorMsg) {
        return error(-1, errorMsg);
    }

    public static final ResponseVo error(Integer code, String msg) {
        return new ResponseVo(code, msg, null);
    }



}
