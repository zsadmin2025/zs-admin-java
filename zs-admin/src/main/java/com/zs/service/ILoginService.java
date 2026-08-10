package com.zs.service;

import com.zs.common.core.core.Result;
import com.zs.common.security.model.TokenVO;
import com.zs.domain.params.LoginParams;
import com.zs.domain.params.RefreshTokenParams;
import com.zs.domain.vo.CodeVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author zs
 */
public interface ILoginService {

    /**
     * 登录
     * @param loginParams 登录参数
     * @param request 请求
     * @param response 响应
     * @return 登录结果
     */
    Result<TokenVO> login(LoginParams loginParams, HttpServletRequest request, HttpServletResponse response);

    /**
     * 获取验证码
     * @param request 请求
     * @param response 响应
     * @return 验证码结果
     */
    Result<CodeVO> captcha(HttpServletRequest request, HttpServletResponse response);

    /**
     * 校验验证码
     * @param uuid 验证码 uuid
     * @param code 验证码
     * @return 校验结果
     */
    boolean checkCaptcha(String uuid, String code);

    /**
     * 刷新 token
     * @param params 刷新参数
     * @return 新的 token 信息
     */
    Result<TokenVO> refresh(RefreshTokenParams params);
}
