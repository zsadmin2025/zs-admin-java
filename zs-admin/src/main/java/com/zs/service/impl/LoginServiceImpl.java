package com.zs.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.zs.common.aop.annotation.LoginLog;
import com.zs.common.core.constant.RedisConstants;
import com.zs.common.core.core.HttpEnum;
import com.zs.common.core.core.Result;
import com.zs.common.core.exception.ErrorCodeConstants;
import com.zs.common.core.exception.ZsException;
import com.zs.common.core.model.LoginUserInfo;
import com.zs.common.core.model.user.SysUser;
import com.zs.common.core.tenant.TenantContext;
import com.zs.common.core.utils.CryptoUtil;
import com.zs.common.core.utils.IpUtils;
import com.zs.common.core.utils.JwtUtil;
import com.zs.common.redis.config.RedisUtil;
import com.zs.common.security.model.TokenVO;
import com.zs.domain.params.LoginParams;
import com.zs.domain.vo.CodeVO;
import com.zs.service.ILoginService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Date;
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
            // 认证失败处理
            return new Result<TokenVO>().error(HttpEnum.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private TokenVO getTokenVO(HttpServletRequest request, Authentication authentication) {
        LoginUserInfo loginUserInfo = (LoginUserInfo) authentication.getPrincipal();

        String userAgentString = request.getHeader(HttpHeaders.USER_AGENT);
        UserAgent userAgent = UserAgentUtil.parse(userAgentString);
        String ipAddr = IpUtils.getIpAddr(request);

        SysUser sysUser = loginUserInfo.getSysUser();
        sysUser.setIp(ipAddr);
        sysUser.setIpAddress(IpUtils.getCityInfo(ipAddr));
        sysUser.setLoginTime(new Date());
        sysUser.setBrowser(userAgent.getBrowser().toString());
        sysUser.setOs(userAgent.getOs().toString());

        // 保存加密的key
        saveCryptoKeyToRedis(request, loginUserInfo.getSysUser());

        TokenVO tokenVO = new TokenVO();
        tokenVO.setAccessToken(jwtUtil.createToken(loginUserInfo));
        tokenVO.setRefreshToken("");
        return tokenVO;
    }

    @Override
    public Result<CodeVO> captcha(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 设置响应内容类型
            response.setContentType("image/png;charset=UTF-8");
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

            // 如果验证码不区分大小写，使用equalsIgnoreCase；否则使用equals
            return captcha.equalsIgnoreCase(code); // 验证码不区分大小写

        } catch (Exception e) {
            log.error("Error checking captcha for UUID: {}", uuid, e);
            return false;
        }
    }

    private void saveCryptoKeyToRedis(HttpServletRequest request, SysUser sysUser) {
        String cryptoKey = request.getHeader("cryptoKey");
        if (cryptoKey == null) {
            throw new ZsException("请求头cryptoKey 不能为空");
        }
        String decryptedKey = CryptoUtil.sm2Decrypt(cryptoKey).replace("\"", "");
        redisUtil.setObject(RedisConstants.SM4_KEY + sysUser.getSysUserId(), decryptedKey);
    }
}
