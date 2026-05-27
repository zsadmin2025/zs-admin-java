package com.zs.common.security.handler;


import cn.hutool.json.JSONUtil;
import com.zs.common.core.constant.RedisConstants;
import com.zs.common.core.core.Result;
import com.zs.common.core.model.LoginUserInfo;
import com.zs.common.core.utils.JwtUtil;
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
    @Resource
    private JwtUtil jwtUtil;


    @Override
    public void onLogoutSuccess(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Authentication authentication) throws IOException {
        LoginUserInfo loginUserInfo = (LoginUserInfo) authentication.getPrincipal();
        delRedisLoginUserInfo(loginUserInfo);

        response.setContentType("application/json;charset=UTF-8");
        Result<Void> result = new Result<>();
        String s = JSONUtil.toJsonStr(result.ok(200, "注销成功", null));
        response.getWriter().println(s);
    }



    public void delRedisLoginUserInfo(@NotNull LoginUserInfo loginUserInfo) {
        // 根据用户类型删除对应的登录信息 Redis key
        String loginInfoKey = jwtUtil.getLoginInfoKey(loginUserInfo.getUserType(), loginUserInfo.getUserId());
        redisUtil.del(loginInfoKey);
        // 清除在线用户记录
        redisUtil.del(RedisConstants.ONLINE_USER + loginUserInfo.getUserId());
    }
}
