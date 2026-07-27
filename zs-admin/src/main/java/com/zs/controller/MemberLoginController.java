package com.zs.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.zs.common.aop.annotation.LoginLog;
import com.zs.common.core.constant.RedisConstants;
import com.zs.common.core.core.Result;
import com.zs.common.core.exception.ErrorCodeConstants;
import com.zs.common.core.exception.ZsException;
import com.zs.common.core.model.LoginUserInfo;
import com.zs.common.core.model.user.MemberUser;
import com.zs.common.core.utils.IpUtils;
import com.zs.common.core.utils.JwtUtil;
import com.zs.common.redis.config.RedisUtil;
import com.zs.common.security.model.TokenVO;
import com.zs.common.security.service.MemberUserDetailsService;
import com.zs.domain.vo.CodeVO;
import com.zs.sys.member.domain.params.MemberLoginParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("member/auth")
@Tag(name = "会员登录", description = "会员端登录相关接口")
public class MemberLoginController {

    @Resource
    private MemberUserDetailsService memberUserDetailsService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private RedisUtil redisUtil;

    @LoginLog
    @Operation(summary = "会员手机号密码登录")
    @PostMapping("login")
    public Result<TokenVO> login(@Valid @RequestBody MemberLoginParams params,
                                 HttpServletRequest request, HttpServletResponse response) {
        // 验证码校验
        String captchaKey = RedisConstants.SYS_CAPTCHA + params.getUuid();
        String captcha = (String) redisUtil.get(captchaKey);
        if (captcha == null) {
            throw new ZsException(ErrorCodeConstants.CAPTCHA_ERROR);
        }
        redisUtil.del(captchaKey);
        if (!captcha.equalsIgnoreCase(params.getCode())) {
            throw new ZsException(ErrorCodeConstants.CAPTCHA_ERROR);
        }

        // 加载会员用户
        LoginUserInfo loginUserInfo = memberUserDetailsService.loadUserByPhone(params.getPhone());

        // 校验密码
        if (!passwordEncoder.matches(params.getPassword(), loginUserInfo.getPassword())) {
            throw new ZsException("手机号或密码错误");
        }
        if (!loginUserInfo.isEnabled()) {
            throw new ZsException("账号已被禁用");
        }

        // 设置会员登录信息
        MemberUser memberUser = (MemberUser) loginUserInfo.getUserInfo();
        String ipAddr = IpUtils.getIpAddr(request);
        UserAgent userAgent = UserAgentUtil.parse(request.getHeader(HttpHeaders.USER_AGENT));
        memberUser.setIp(ipAddr);
        memberUser.setIpAddress(IpUtils.getCityInfo(ipAddr));
        memberUser.setLoginTime(new Date());

        // 写入Redis
        String redisKey = jwtUtil.getLoginInfoKey(loginUserInfo.getUserType(), loginUserInfo.getUserId());
        redisUtil.setObject(redisKey, loginUserInfo, jwtUtil.getExpirationTime(), TimeUnit.SECONDS);

        // 生成token
        TokenVO tokenVO = new TokenVO();
        tokenVO.setAccessToken(jwtUtil.createToken(loginUserInfo));
        tokenVO.setRefreshToken("");
        return new Result<TokenVO>().ok(tokenVO);
    }

    @Operation(summary = "获取会员登录验证码")
    @GetMapping("captcha")
    public Result<CodeVO> captcha(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("image/png;charset=UTF-8");
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

            LineCaptcha captcha = CaptchaUtil.createLineCaptcha(100, 45, 1, 1);
            RandomGenerator generator = new RandomGenerator("0123456789", 4);
            captcha.setGenerator(generator);
            captcha.setFont(new Font("微软雅黑", Font.BOLD, 30));
            captcha.createCode();
            String code = captcha.getCode();
            String imageBase64Data = captcha.getImageBase64Data();

            String uuid = IdUtil.fastSimpleUUID();
            redisUtil.setObject(RedisConstants.SYS_CAPTCHA + uuid, code, 60, TimeUnit.SECONDS);

            CodeVO codeVO = new CodeVO();
            codeVO.setImg(imageBase64Data);
            codeVO.setUuid(uuid);
            return new Result<CodeVO>().ok(codeVO);
        } catch (Exception e) {
            throw new ZsException("获取验证码失败");
        }
    }
}
