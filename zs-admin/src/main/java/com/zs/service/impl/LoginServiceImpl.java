package com.zs.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.util.IdUtil;
import com.zs.common.aop.annotation.LoginLog;
import com.zs.common.core.constant.RedisConstants;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.UserTypeEnum;
import com.zs.common.core.exception.ErrorCodeConstants;
import com.zs.common.core.exception.ZsException;
import com.zs.common.core.model.LoginUserInfo;
import com.zs.common.core.tenant.TenantContext;
import com.zs.common.core.utils.JwtUtil;
import com.zs.common.redis.config.RedisUtil;
import com.zs.common.security.model.TokenVO;
import com.zs.domain.params.LoginParams;
import com.zs.domain.params.RefreshTokenParams;
import com.zs.domain.vo.CodeVO;
import com.zs.service.ILoginService;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * @author zs
 */
@Slf4j
@Service
public class LoginServiceImpl implements ILoginService {

    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private JwtUtil jwtUtil;

    @LoginLog
    @Override
    public Result<TokenVO> login(LoginParams loginParams, HttpServletRequest request, HttpServletResponse response) {
        // 验证码校验
        boolean isCaptcha = checkCaptcha(loginParams.getUuid(), loginParams.getCode());
        if (!isCaptcha) {
            throw new ZsException(ErrorCodeConstants.CAPTCHA_ERROR);
        }
        TenantContext.setTenantId(loginParams.getTenantId());

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginParams.getUsername(), loginParams.getPassword());

        try {
            Authentication authentication = authenticationManager.authenticate(token);
            TokenVO tokenVO = getTokenVO(request, authentication);
            return new Result<TokenVO>().ok(tokenVO);

        } catch (AuthenticationException e) {
            log.warn("用户认证失败: {}, 原因: {}", loginParams.getUsername(), e.getMessage());
            throw new ZsException(ErrorCodeConstants.LOGIN_ERROR.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("登录过程中发生未知错误: {}", loginParams.getUsername(), e);
            throw new ZsException(ErrorCodeConstants.SYSTEM_ERROR.getCode(), e.getMessage());
        }
    }

    private TokenVO getTokenVO(HttpServletRequest request, Authentication authentication) {
        LoginUserInfo loginUserInfo = (LoginUserInfo) authentication.getPrincipal();

        // 保存加密的key
//        saveCryptoKeyToRedis(request, loginUserInfo.getSysUser());

        // 将登录用户信息写入Redis，JwtAuthenticationTokenFilter会从中读取
        String redisKey = jwtUtil.getLoginInfoKey(loginUserInfo.getUserType(), loginUserInfo.getUserId());
        redisUtil.setObject(redisKey, loginUserInfo, jwtUtil.getExpirationTime(), TimeUnit.SECONDS);

        // 生成 refresh token 并存入 Redis（更长的过期时间）
        String refreshToken = jwtUtil.createRefreshToken(loginUserInfo);
        String refreshKey = jwtUtil.getRefreshTokenKey(loginUserInfo.getUserType(), loginUserInfo.getUserId());
        redisUtil.setObject(refreshKey, refreshToken, jwtUtil.getRefreshExpirationTime(), TimeUnit.SECONDS);

        TokenVO tokenVO = new TokenVO();
        tokenVO.setAccessToken(jwtUtil.createToken(loginUserInfo));
        tokenVO.setRefreshToken(refreshToken);
        return tokenVO;
    }

    @Override
    public Result<CodeVO> captcha(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 设置响应内容类型
//            response.setContentType("image/png;charset=UTF-8");
            // 禁止缓存
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

            LineCaptcha captcha = CaptchaUtil.createLineCaptcha(100, 45, 1, 1);
            RandomGenerator generator = new RandomGenerator("0123456789", 4);
            // 自定义验证码内容为四则运算方式
            captcha.setGenerator(generator);
            captcha.setFont(new Font("微软雅黑", Font.BOLD, 30));
            // 重新生成code
            captcha.createCode();
            String code = captcha.getCode();

            String imageBase64Data = captcha.getImageBase64Data();

            String uuid = IdUtil.fastSimpleUUID();
            // 保存到redis
            redisUtil.setObject(RedisConstants.SYS_CAPTCHA + uuid, code, 60, TimeUnit.SECONDS);

            CodeVO codeVO = new CodeVO();
            codeVO.setImg(imageBase64Data);
            codeVO.setUuid(uuid);
            return new Result<CodeVO>().ok(codeVO);

        } catch (Exception e) {
            throw new ZsException("获取验证码失败");
        }
    }

    @Override
    public boolean checkCaptcha(String uuid, String code) {
        try {
            // 使用更明确的命名方式，避免命名空间冲突
            String captchaKey = RedisConstants.SYS_CAPTCHA + uuid;
            String captcha = (String) redisUtil.get(captchaKey);

            // 判断captcha为null
            if (captcha == null) {
                log.warn("Captcha not found for UUID: {}", uuid);
                return false;
            }

            // 验证后立即删除，防止重放
            redisUtil.del(captchaKey);
            // 如果验证码不区分大小写，使用equalsIgnoreCase；否则使用equals
            return captcha.equalsIgnoreCase(code); // 验证码不区分大小写

        } catch (Exception e) {
            log.error("Error checking captcha for UUID: {}", uuid, e);
            return false;
        }
    }

//    private void saveCryptoKeyToRedis(HttpServletRequest request, SysUser sysUser) {
//        String cryptoKey = request.getHeader("cryptoKey");
//        if (cryptoKey == null) {
//            throw new ZsException("请求头cryptoKey 不能为空");
//        }
//        String decryptedKey = CryptoUtil.sm2Decrypt(cryptoKey).replace("\"", "");
//        redisUtil.setObject(RedisConstants.SM4_KEY + sysUser.getSysUserId(), decryptedKey);
//    }

    @Override
    public Result<TokenVO> refresh(RefreshTokenParams params) {
        String refreshToken = params.getRefreshToken();

        // 1. 解析 refresh token
        Claims claims = jwtUtil.parseToken(refreshToken);
        if (claims == null) {
            throw new ZsException("refresh token 无效或已过期");
        }

        // 2. 校验是否为 refresh token 类型
        if (!jwtUtil.isRefreshToken(claims)) {
            throw new ZsException("非法的 token 类型");
        }

        // 3. 从 Redis 校验 refresh token 是否仍然有效
        String refreshKey = claims.getSubject();
        String storedToken = (String) redisUtil.get(refreshKey);
        if (storedToken == null || !storedToken.equals(refreshToken)) {
            throw new ZsException("refresh token 已失效，请重新登录");
        }

        // 4. 获取已登录的用户信息
        String userType = claims.get("userType", String.class);
        Long userId = claims.get("userId", Long.class);
        String loginKey = jwtUtil.getLoginInfoKey(UserTypeEnum.fromCode(userType), userId);
        LoginUserInfo loginUserInfo = (LoginUserInfo) redisUtil.get(loginKey);
        if (loginUserInfo == null) {
            throw new ZsException("登录已过期，请重新登录");
        }

        // 5. 旋转 refresh token（删除旧的，生成新的，防止重放攻击）
        redisUtil.del(refreshKey);
        String newRefreshToken = jwtUtil.createRefreshToken(loginUserInfo);
        String newRefreshKey = jwtUtil.getRefreshTokenKey(loginUserInfo.getUserType(), loginUserInfo.getUserId());
        redisUtil.setObject(newRefreshKey, newRefreshToken, jwtUtil.getRefreshExpirationTime(), TimeUnit.SECONDS);

        // 6. 签发新的 access token
        TokenVO tokenVO = new TokenVO();
        tokenVO.setAccessToken(jwtUtil.createToken(loginUserInfo));
        tokenVO.setRefreshToken(newRefreshToken);
        return new Result<TokenVO>().ok(tokenVO);
    }
}
