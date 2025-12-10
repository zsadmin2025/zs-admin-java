package com.zs.common.security.handler;

import cn.hutool.json.JSONUtil;
import com.zs.common.core.core.HttpEnum;
import com.zs.common.core.core.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 自定义未登录处理
 *
 * @author zsadmin
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, @NotNull HttpServletResponse response, AuthenticationException authException) throws IOException {


        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        String s = JSONUtil.toJsonStr(new Result<>().error(HttpEnum.UNAUTHORIZED));
        response.getWriter().println(s);
    }
}
