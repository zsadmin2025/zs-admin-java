package com.zs.common.security.provider;


import com.zs.common.core.model.LoginUserInfo;
import com.zs.common.core.tenant.TenantContext;
import com.zs.common.security.service.CustomUserDetailsService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * 用户名密码认证器
 *
 * @author zsadmin
 */
@Slf4j
@Component
public class UserNameAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private CustomUserDetailsService customUserDetailsService;


    @Override
    public boolean supports(@NotNull Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public Authentication authenticate(@NotNull Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        log.debug("开始认证用户: {}", username);



        try {
            // 1、去调用自己实现的UserDetailsService，返回UserDetails
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            if (Objects.isNull(userDetails)) {
                log.warn("用户不存在: {}", username);
                throw new BadCredentialsException("账号或密码错误");
            }

            LoginUserInfo loginUserInfo = (LoginUserInfo) userDetails;

            // 2、密码进行检查
            if (!passwordEncoder().matches(password, userDetails.getPassword())) {
                log.warn("用户密码错误: {}", username);
                throw new BadCredentialsException("账号或密码错误");
            }

            if (!loginUserInfo.isEnabled()) {
                log.warn("用户账号已被禁用: {}", username);
                throw new BadCredentialsException("您的账号已被禁用");
            }


            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            // 将用户名存储在HttpServletRequest的属性中
            request.setAttribute("username", username);

            // 3、返回经过认证的Authentication
            UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            result.setDetails(authentication.getDetails());

            // 将 Authentication 认证信息对象绑定到 SecurityContext即安全上下文中
            SecurityContextHolder.getContext().setAuthentication(result);

            log.info("用户认证成功: {}", username);
            return result;
        } finally {
            // 清理租户上下文，防止内存泄漏
            TenantContext.clear();
        }
    }


}
