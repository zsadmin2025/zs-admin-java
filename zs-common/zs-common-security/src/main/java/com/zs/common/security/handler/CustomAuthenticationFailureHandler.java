package com.zs.common.security.handler;


import cn.hutool.json.JSONUtil;
import com.zs.common.aop.annotation.LoginLog;
import com.zs.common.core.core.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 自定义登录失败处理
 *
 * @author zsadmin
 */

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {


    @LoginLog(value = 2)
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull AuthenticationException exception) throws IOException {


        response.setContentType("application/json;charset=UTF-8");
        String s = JSONUtil.toJsonStr(new Result<>().error(500,  exception.getMessage()));
        response.getWriter().println(s);


    }
}
