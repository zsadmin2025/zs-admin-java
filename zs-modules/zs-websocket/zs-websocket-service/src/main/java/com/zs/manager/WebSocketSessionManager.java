package com.zs.manager;

import com.alibaba.fastjson.JSON;
import com.zs.common.core.constant.RedisConstants;
import com.zs.common.core.model.BaseUserInfo;
import com.zs.common.core.model.user.SysUser;
import com.zs.common.redis.config.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;


@Component
public class WebSocketSessionManager {

    @Resource
    private RedisUtil redisUtil;


    /**
     * 用户上线
     *
     * @param sessionId sessionId
     * @param baseUserInfo   用户
     */
    public void addUser(String sessionId, BaseUserInfo baseUserInfo){



        Long tenantId = baseUserInfo.getTenantId();
        long loginTime = System.currentTimeMillis();

        // 1. 存储用户详情（Hash）
        String detailKey = RedisConstants.ONLINE_USER_DETAIL_PREFIX + sessionId;
        redisUtil.setHash(detailKey, "user", JSON.toJSONString(baseUserInfo), 30 * 60);

        // 2. 加入租户的在线用户列表（ZSET，按登录时间排序）
        String listKey = RedisConstants.ONLINE_USER_LIST_PREFIX + tenantId;
        redisUtil.addZSet(listKey, sessionId, loginTime, 30 * 60);
    }


    /**
     * 用户下线
     * @param sessionId sessionId
     * @param tenantId  租户Id
     */
    public void removeUser(String sessionId, String tenantId){


        // 1. 删除用户详情
        String detailKey = RedisConstants.ONLINE_USER_DETAIL_PREFIX + sessionId;
        redisUtil.del(detailKey);

        // 2. 从租户列表中移除
        String listKey = RedisConstants.ONLINE_USER_LIST_PREFIX + tenantId;
        redisUtil.deleteZSet(listKey, sessionId);
    }


}
