package com.enn.energy.price.web.advice;

import com.enn.energy.price.common.error.ErrorCodeEnum;
import com.enn.energy.price.common.error.PriceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import top.rdfa.framework.biz.ro.RdfaResult;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

/**
 * 全局异常处理器.
 *
 * @author : wuchaon
 * @version : 1.0 2021/12/4 0:09
 * @since : 1.0
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 处理自定义异常
     */
    @ExceptionHandler(PriceException.class)
    public RdfaResult handleRRException(PriceException e) {
        log.error(e.getMessage(), e);
        return RdfaResult.fail(e.getCode(), e.getMessage());
    }

    /**
     * 方法参数校验
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RdfaResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        return RdfaResult.fail(ErrorCodeEnum.METHOD_ARGUMENT_VALID_EXCEPTION.getErrorCode(), e.getBindingResult().getFieldError().getDefaultMessage());
    }

    /**
     * ValidationException
     */
    @ExceptionHandler(ValidationException.class)
    public RdfaResult handleValidationException(ValidationException e) {
        log.error(e.getMessage(), e);
        return RdfaResult.fail(ErrorCodeEnum.VALIDATION_CODE_EXCEPTION.getErrorCode(), e.getCause().getMessage());
    }

    /**
     * ConstraintViolationException
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public RdfaResult handleConstraintViolationException(ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        return RdfaResult.fail(ErrorCodeEnum.CONSTRAINT_VIOLATION_EXCEPTION.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public RdfaResult handlerNoFoundException(Exception e) {
        log.error(e.getMessage(), e);
        return RdfaResult.fail("404", "路径不存在，请检查路径是否正确");
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public RdfaResult handleDuplicateKeyException(DuplicateKeyException e) {
        log.error(e.getMessage(), e);
        return RdfaResult.fail(ErrorCodeEnum.DUPLICATE_KEY_EXCEPTION.getErrorCode(), "数据重复，请检查后提交");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public RdfaResult handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error(e.getMessage(), e);
        return RdfaResult.fail(ErrorCodeEnum.HTTP_MESSAGE_NOT_READABLE_EXCEPTION.getErrorCode(), ErrorCodeEnum.HTTP_MESSAGE_NOT_READABLE_EXCEPTION.getErrorMsg());
    }

    @ExceptionHandler(ClientAbortException.class)
    public RdfaResult handleException(ClientAbortException e) {
        log.error(e.getMessage(), e);
        return RdfaResult.fail("500", "系统繁忙,请稍后再试");
    }

    @ExceptionHandler(Exception.class)
    public RdfaResult handleException(Exception e) {
        log.error(e.getMessage(), e);
        return RdfaResult.fail("500", "系统繁忙,请稍后再试");
    }


}
