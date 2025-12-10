package com.zs.common.security.service;


import org.springframework.security.core.userdetails.UserDetails;


/**
 * 自定义账号密码验证逻辑
 *
 * @author zsadmin
 */
public interface CustomUserDetailsService {

    UserDetails loadUserByUsername(String username);
}
