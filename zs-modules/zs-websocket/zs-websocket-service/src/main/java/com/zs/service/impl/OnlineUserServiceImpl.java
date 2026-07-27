package com.zs.service.impl;


import cn.hutool.json.JSONUtil;
import com.zs.common.core.constant.RedisConstants;
import com.zs.common.core.model.user.SysUser;
import com.zs.common.core.page.PageResult;
import com.zs.common.core.tenant.TenantContext;
import com.zs.common.redis.config.RedisUtil;
import com.zs.model.params.OnlineUserQueryParams;
import com.zs.service.OnlineUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class OnlineUserServiceImpl implements OnlineUserService {

    @Resource
    private RedisUtil redisUtil;


    @Override
    public PageResult<SysUser> getOnlineUsersPage(OnlineUserQueryParams onlineUserQueryParams) {
        String tenantId = TenantContext.getTenantId();
        String listKey = RedisConstants.ONLINE_USER_LIST_PREFIX + tenantId;

        // 分页：按登录时间倒序（最新登录在前）
        long start = (long) (onlineUserQueryParams.getCurrent() - 1) * onlineUserQueryParams.getPageSize();
        long end = start + onlineUserQueryParams.getPageSize() - 1;

        // 批量获取用户详情
        List<SysUser> users = new ArrayList<>();

        // ZREVRANGE 获取 sessionId 列表（按 score 倒序）
        Set<Object> sessionIds = redisUtil.zRevRange(listKey, start, end);
        if (sessionIds == null || sessionIds.isEmpty()) {
            users =  Collections.emptyList();
        }


        if (sessionIds != null) {
            for (Object sessionId : sessionIds) {
                String detailKey = RedisConstants.ONLINE_USER_DETAIL_PREFIX + sessionId;
                String userJson = (String) redisUtil.getHash(detailKey, "user");
                if (userJson != null) {
                    users.add(JSONUtil.toBean(userJson, SysUser.class));
                }
            }
        }

        return new PageResult<>(users,users.size() );
    }
}
