package com.enn.energy.price.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.rdfa.framework.auth.client.annotation.Authorize;
import top.rdfa.framework.auth.facade.authrization.AuthContext;
import top.rdfa.framework.auth.facade.constant.AuthConstant;
import top.rdfa.framework.auth.facade.entity.UserInfo;
import top.rdfa.framework.biz.ro.RdfaResult;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/sso")
public class SsoDemoController {

    @GetMapping("/authorize")
    @Authorize()
    public RdfaResult<String> authorize(HttpServletRequest request) {
        AuthContext authContext = (AuthContext) request.getServletContext().getAttribute(AuthConstant.AUTH_CONTEXT_KEY);
        return RdfaResult.success("200","process success",authContext.getUserInfo().getUsername());
    }

    @GetMapping("/unauthorize")
    public RdfaResult<String> unAuthorize( ) {
        return RdfaResult.success("200","process success","No verification is required !");
    }

    @GetMapping("/authorize/userInfo")
    @Authorize()
    public RdfaResult<UserInfo> authorizeUserInfo(HttpServletRequest request) {
        AuthContext authContext = (AuthContext) request.getServletContext().getAttribute(AuthConstant.AUTH_CONTEXT_KEY);
        return RdfaResult.success("200","process success",authContext.getUserInfo());
    }

}
