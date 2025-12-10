package com.zs.common.core.utils;


import com.zs.common.core.constant.Constants;
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
    public String createToken(@NotNull LoginUserInfo loginUserInfo) {
        //header参数
        final Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("alg", "HS256");
        headerMap.put("typ", "JWT");

        String token = Jwts.builder()
                .header().add(headerMap)
                .and()
                .subject(Constants.LOGIN_INFO + loginUserInfo.sysUser.getSysUserId())
                .issuer(ISSUER)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime * 1000L))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
        redisUtil.setObject(Constants.LOGIN_INFO + loginUserInfo.getSysUser().getSysUserId(), loginUserInfo, expirationTime, TimeUnit.SECONDS);
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
}