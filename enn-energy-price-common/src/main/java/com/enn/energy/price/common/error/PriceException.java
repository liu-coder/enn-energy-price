package com.enn.energy.price.common.error;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义异常.
 *
 * @author : wuchaon
 * @version : 1.0 2021/11/26 16:40
 * @since : 1.0
 **/
@Data
@EqualsAndHashCode(callSuper=false)
public class PriceException extends RuntimeException{

    private String code;

    private String message;

    public PriceException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
