package com.zs.common.security.handler;


import cn.hutool.json.JSONUtil;
import com.zs.common.core.constant.RedisConstants;
import com.zs.common.core.core.Result;
import com.zs.common.core.model.LoginUserInfo;
import com.zs.common.redis.config.RedisUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 登出成功处理器
 *
 * @author zsadmin
 */
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Resource
    private RedisUtil redisUtil;


    @Override
    public void onLogoutSuccess(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Authentication authentication) throws IOException {
        LoginUserInfo loginUserInfo = (LoginUserInfo) authentication.getPrincipal();
        delRedisLoginUserInfo(loginUserInfo);

        response.setContentType("application/json;charset=UTF-8");
        String s = JSONUtil.toJsonStr(new Result<>().ok(200, "注销成功", null));
        response.getWriter().println(s);
    }



    public void delRedisLoginUserInfo(@NotNull LoginUserInfo loginUserInfo) {
        // redis清楚用户登录信息
        redisUtil.del(RedisConstants.ONLINE_USER + loginUserInfo.getSysUser().getSysUserId());
    }
}
