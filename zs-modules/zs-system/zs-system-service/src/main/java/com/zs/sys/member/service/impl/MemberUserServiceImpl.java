package com.zs.sys.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.model.LoginUserInfo;
import com.zs.common.core.model.user.MemberUser;
import com.zs.common.core.tenant.TenantContext;
import com.zs.common.security.service.MemberUserDetailsService;
import com.zs.sys.member.domain.entity.MemberUserEntity;
import com.zs.sys.member.mapper.MemberUserMapper;
import com.zs.sys.member.service.IMemberUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;

@Service
public class MemberUserServiceImpl extends ServiceImpl<MemberUserMapper, MemberUserEntity>
        implements IMemberUserService, MemberUserDetailsService {

    @Override
    public LoginUserInfo loadUserByPhone(String phone) {
        String tenantId = TenantContext.getTenantId();
        MemberUserEntity entity;
        if (StringUtils.isNotBlank(tenantId)) {
            entity = baseMapper.selectByPhoneAndTenant(phone, tenantId);
        } else {
            entity = baseMapper.selectByPhone(phone);
        }
        if (Objects.isNull(entity)) {
            throw new UsernameNotFoundException("会员不存在");
        }
        MemberUser memberUser = BeanUtil.toBean(entity, MemberUser.class);
        memberUser.setUserId(entity.getMemberUserId());
        // 会员端暂无细粒度权限，给空集合
        return new LoginUserInfo(memberUser, Collections.emptySet(), null);
    }
}
