package com.zs.common.security.handler;

import cn.hutool.json.JSONUtil;
import com.zs.common.core.core.HttpEnum;
import com.zs.common.core.core.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 自定义权限不足过滤器
 *
 * @author zsadmin
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, @NotNull HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        String s = JSONUtil.toJsonStr(new Result<>().error(HttpEnum.FORBIDDEN));
        response.getWriter().println(s);
    }
}
