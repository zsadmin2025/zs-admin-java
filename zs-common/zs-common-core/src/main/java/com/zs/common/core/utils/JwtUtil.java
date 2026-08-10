package com.zs.common.core.utils;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.zs.common.core.constant.Constants;
import com.zs.common.core.enums.UserTypeEnum;
import com.zs.common.core.model.BaseUserInfo;
import com.zs.common.core.model.LoginUserInfo;
import com.zs.common.core.tenant.TenantContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    @Value("${jwt.expiration}") // 从配置文件中读取过期时间（单位：秒）
    private Long expirationTime;
    @Value("${jwt.refresh-expiration}") // refresh_token 过期时间（单位：秒），默认7天
    private Long refreshExpirationTime;
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

        extracted(user);

        String tenantId = TenantContext.getTenantId();
        Map<String, Object> claims = new HashMap<>();
        claims.put("userType", user.getUserType().getCode());
        claims.put("userId", user.getUserId());
        claims.put("tenantId", tenantId);
        claims.put(Constants.TENANT_HEADER, tenantId);

        return Jwts.builder()
                .claims(claims)
                .subject(getLoginInfoKey(user.getUserType(), user.getUserId()))
                .issuer(ISSUER)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime * 1000L))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    private static void extracted(BaseUserInfo user) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String userAgentString = request.getHeader(HttpHeaders.USER_AGENT);
        UserAgent userAgent = UserAgentUtil.parse(userAgentString);
        String ipAddr = IpUtils.getIpAddr(request);
        user.setIp(ipAddr);
        user.setIpAddress(IpUtils.getCityInfo(ipAddr));
        user.setLoginTime(new Date());
        user.setBrowser(userAgent.getBrowser().toString());
        user.setOs(userAgent.getOs().toString());
    }

    /**
     * 解析token
     *
     * @param token token
     * @return Claims
     */
    @Nullable
    public Claims parseToken(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
        } catch (Exception e) {
            return null;
        }
    }

    public Long getExpirationTime() {
        return expirationTime;
    }

    public Long getRefreshExpirationTime() {
        return refreshExpirationTime;
    }

    /**
     * 签发 refresh token，有效期比 access token 更长
     */
    public String createRefreshToken(LoginUserInfo loginUserInfo) {
        BaseUserInfo user = loginUserInfo.getUserInfo();
        String tenantId = TenantContext.getTenantId();
        Map<String, Object> claims = new HashMap<>();
        claims.put("userType", user.getUserType().getCode());
        claims.put("userId", user.getUserId());
        claims.put("tenantId", tenantId);
        claims.put("tokenType", "refresh");

        return Jwts.builder()
                .claims(claims)
                .subject(getRefreshTokenKey(user.getUserType(), user.getUserId()))
                .issuer(ISSUER)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpirationTime * 1000L))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    /**
     * 获取 refresh token 在 Redis 中的存储 key
     */
    public String getRefreshTokenKey(UserTypeEnum userType, Long userId) {
        return Constants.REFRESH_TOKEN + userType.getCode() + ":" + userId;
    }

    /**
     * 判断 token 是否为 refresh token 类型
     */
    public boolean isRefreshToken(Claims claims) {
        return "refresh".equals(claims.get("tokenType", String.class));
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