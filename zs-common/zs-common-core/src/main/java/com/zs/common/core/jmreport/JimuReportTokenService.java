package com.zs.common.core.jmreport;


import cn.hutool.json.JSONUtil;
import com.zs.common.core.constant.Constants;
import com.zs.common.core.exception.ZsException;
import com.zs.common.core.model.LoginUserInfo;
import com.zs.common.core.utils.JwtUtil;
import com.zs.common.redis.config.RedisUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Setter;
import org.jeecg.modules.jmreport.api.JmReportTokenServiceI;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
public class JimuReportTokenService implements JmReportTokenServiceI {

    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private RedisUtil redisUtil;

    @Setter
    private  String tenantId = "";
    @Override
    public String getToken(HttpServletRequest request) {
        String token = request.getParameter(Constants.ACCESS_TOKEN);
        if (token != null) {
            LoginUserInfo loginUserInfo = parseToken(token);
            tenantId = String.valueOf(loginUserInfo.getSysUser().getTenantId());
        }
        return token;
    }

    private LoginUserInfo parseToken(String token) {
        token = token.substring(Constants.TOKEN_PREFIX.length()); // 去除 "Bearer " 前缀
        Claims claims = jwtUtil.parseToken(token);
        if (Objects.isNull(claims)) {
            throw new ZsException("Invalid token");
        }
        String loginInfo = claims.getSubject();
        Object jsonLoginUserInfo = redisUtil.get(loginInfo);
        return JSONUtil.toBean(JSONUtil.parseObj(jsonLoginUserInfo), LoginUserInfo.class);
    }

    @Override
    public String getToken() {
        return JmReportTokenServiceI.super.getToken();
    }

    @Override
    public String getUsername(String token) {
        LoginUserInfo loginUserInfo = parseToken(token);
        return loginUserInfo.getUsername();
    }


    @Override
    public Boolean verifyToken(String token) {
        return true;
    }

    @Override
    public Map<String, Object> getUserInfo(String token) {
        return JmReportTokenServiceI.super.getUserInfo(token);
    }

    @Override
    public HttpHeaders customApiHeader() {
        return JmReportTokenServiceI.super.customApiHeader();
    }

    @Override
    public String getTenantId() {
        return tenantId;
    }

}
