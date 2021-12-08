package com.enn.energy.price.web.interceptors;

import com.alibaba.fastjson.JSON;
import com.enn.energy.price.common.error.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.rdfa.framework.biz.ro.RdfaResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 价格版本拦截器.
 *
 * @author : wuchaon
 * @version : 1.0 2021/12/7 16:31
 * @since : 1.0
 **/
@Component
@Slf4j
public class PriceVersionInterceptor implements HandlerInterceptor {

    @Value("${priceVersionToken}")
    private String priceVersionToken;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){

        String token = request.getHeader("priceVersionToken");
        if (priceVersionToken.equals(token) || priceVersionToken.equals("price")) {
            return true;
        }
        returnJson(response);
        return false;
    }

    private void returnJson(HttpServletResponse response) {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(JSON.toJSONString(RdfaResult.fail(ErrorCodeEnum.INVALID_TOKEN_ERROR.getErrorCode(), ErrorCodeEnum.INVALID_TOKEN_ERROR.getErrorMsg())));
        } catch (IOException e) {
            log.error("价格版本拦截器输出流异常");
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

}
