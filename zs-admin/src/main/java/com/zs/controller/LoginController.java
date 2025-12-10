package com.zs.controller;

import com.zs.common.core.core.Result;
import com.zs.common.core.crypto.annotation.Decryption;
import com.zs.common.core.enums.CryptoTypeEnum;
import com.zs.common.security.model.TokenVO;
import com.zs.domain.params.LoginParams;
import com.zs.domain.vo.CodeVO;
import com.zs.service.ILoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author zs
 */
@RestController
@RequestMapping("auth")
@Tag(name = "登录认证", description = "登录相关接口")
public class LoginController {

    @Resource
    private ILoginService loginService;


    @Operation(summary = "账号密码登录")
    @Decryption(value = CryptoTypeEnum.SM2)
    @PostMapping("login")
    public  Result<TokenVO> login(@Valid @RequestBody LoginParams loginParams, HttpServletRequest request, HttpServletResponse response) {
      return  loginService.login(loginParams, request, response);
    }

    @Operation(summary = "获取登录验证码")
    @GetMapping("captcha")
    public Result<CodeVO> captcha(HttpServletRequest request, HttpServletResponse response) {
        return loginService.captcha(request, response);
    }




}
