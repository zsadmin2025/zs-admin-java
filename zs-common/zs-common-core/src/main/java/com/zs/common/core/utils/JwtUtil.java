package com.zs.common.core.utils;


import com.zs.common.core.constant.Constants;
import com.zs.common.core.enums.UserTypeEnum;
import com.zs.common.core.model.BaseUserInfo;
import com.zs.common.core.model.LoginUserInfo;
import com.zs.common.redis.config.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zsadmin
 **/
@Component
public class JwtUtil {


    // 签发者
    private static final String ISSUER = "zsAdmin.top";
    // 从配置文件中读取密钥
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}") // 从配置文件中读取过期时间（单位：分钟）
    private Long expirationTime;
    @Resource
    private RedisUtil redisUtil;
    private SecretKey secretKey;


    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成token签名
     *
     * @return String
     */
    public String createToken(LoginUserInfo loginUserInfo) {
        BaseUserInfo user = loginUserInfo.getUserInfo();
        //header参数
        final Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("alg", "HS256");
        headerMap.put("typ", "JWT");


          Map<String, Object> claims = new HashMap<>();
            claims.put("userType", user.getUserType().getCode());
            claims.put("userId", user.getUserId());
            claims.put("tenantId", String.valueOf(com.zs.common.core.tenant.TenantContext.getTenantId()));
            claims.put(Constants.TENANT_HEADER, String.valueOf(com.zs.common.core.tenant.TenantContext.getTenantId()));
            // sessionId, clientType, deviceId 字段预留，后续可按需启用

        String token = Jwts.builder()
                .header().add(headerMap)
                .and()
                .claims(claims)
                .subject(getLoginInfoKey(user.getUserType(), user.getUserId()))
                .issuer(ISSUER)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime * 1000L))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
        return token;
    }



    /**
     * 解析token
     *
     * @param token token
     * @return Claims
     */
    @Nullable
    public Claims parseToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
            return claims;
        } catch (Exception e) {
            return null;
        }
    }



    public Long getExpirationTime() {
        return expirationTime;
    }

    public String getLoginInfoKey(UserTypeEnum userType, Long userId) {
        return switch (userType) {
            case PLATFORM -> Constants.LOGIN_INFO + userId;
            case MEMBER -> Constants.MEMBER_LOGIN_INFO + userId;
            case COMPANION -> Constants.COMPANION_LOGIN_INFO + userId;
        };
    }


     public String getRedisKey(Claims claims) {
        String userType = claims.get("userType", String.class);
        Long userId = claims.get("userId", Long.class);
        return switch (UserTypeEnum.fromCode(userType)) {
            case PLATFORM -> Constants.LOGIN_INFO + userId;
            case MEMBER -> Constants.MEMBER_LOGIN_INFO + userId;
            case COMPANION -> Constants.COMPANION_LOGIN_INFO + userId;
        };
    }
}