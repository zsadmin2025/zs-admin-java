package com.zs.common.security.service;

import com.zs.common.core.model.LoginUserInfo;

/**
 * 会员端用户详情服务
 */
public interface MemberUserDetailsService {

    /**
     * 根据手机号加载会员用户
     */
    LoginUserInfo loadUserByPhone(String phone);
}
