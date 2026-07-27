package com.zs.sys.events;

import cn.hutool.core.bean.BeanUtil;
import com.zs.common.core.enums.AdminEnum;
import com.zs.common.core.enums.DataScopeEnum;
import com.zs.common.core.events.DataPermissionChangedEvent;
import com.zs.common.core.model.DataPermission;
import com.zs.common.core.model.LoginUserInfo;
import com.zs.common.core.model.user.SysUser;
import com.zs.common.core.utils.JwtUtil;
import com.zs.common.redis.config.RedisUtil;
import com.zs.sys.dept.service.ISysDeptService;
import com.zs.sys.menu.service.ISysMenuService;
import com.zs.sys.role.domain.entity.SysRoleEntity;
import com.zs.sys.role.service.ISysRoleService;
import com.zs.sys.user.domain.entity.SysUserEntity;
import com.zs.sys.user.mapper.SysUserMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 数据权限变更监听器 —— 主动重建 LoginUserInfo 并推送至 Redis
 *
 * @author zsadmin
 */
@Slf4j
@Component
public class DataPermissionSyncListener {

    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private ISysRoleService roleService;
    @Resource
    private ISysDeptService deptService;
    @Resource
    private ISysMenuService menuService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private JwtUtil jwtUtil;

    @Async
    @EventListener
    public void onDataPermissionChanged(DataPermissionChangedEvent event) {
        log.info("收到数据权限变更事件: type={}, affectedCount={}",
                event.getChangeType(), event.getAffectedUserIds().size());

        for (Long userId : event.getAffectedUserIds()) {
            try {
                syncUserPermission(userId);
            } catch (Exception e) {
                log.error("同步用户[{}]权限缓存失败", userId, e);
            }
        }
    }

    /**
     * 重建单个用户的权限并主动推送至 Redis
     */
    private void syncUserPermission(Long userId) {
        SysUserEntity entity = sysUserMapper.selectById(userId);
        if (entity == null) {
            log.warn("用户[{}]不存在，跳过权限同步", userId);
            return;
        }

        // 1. 重建基础信息
        SysUser sysUser = toSysUser(entity);

        // 2. 重建 DataPermission
        List<SysRoleEntity> roles = roleService.findByUserId(userId);
        DataPermission dp = buildDataPermission(sysUser, roles);

        // 3. 重建权限集合
        Set<String> permissions = buildPermissions(entity);

        // 4. 组装 LoginUserInfo
        LoginUserInfo updated = new LoginUserInfo(sysUser, permissions, dp);

        // 5. 主动推送至 Redis
        String redisKey = "sys_login_info:" + userId;
        redisUtil.setObject(redisKey, updated, jwtUtil.getExpirationTime(), TimeUnit.SECONDS);

        log.debug("已同步用户[{}]权限缓存到Redis, roles={}", userId, roles.size());
    }

    private SysUser toSysUser(SysUserEntity entity) {
        SysUser u = BeanUtil.toBean(entity, SysUser.class);
        u.setUserId(entity.getSysUserId());
        return u;
    }

    private DataPermission buildDataPermission(SysUser sysUser, List<SysRoleEntity> roles) {
        DataPermission dp = new DataPermission();
        dp.setUserId(sysUser.getSysUserId());
        dp.setDeptId(sysUser.getSysDeptId());
        dp.setRoleIds(roles.stream()
                .map(SysRoleEntity::getSysRoleId)
                .collect(Collectors.toSet()));

        Set<Integer> scopeValues = roles.stream()
                .map(SysRoleEntity::getDataScope)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        dp.setDataScopeTypes(scopeValues.stream()
                .map(DataScopeEnum::value)
                .collect(Collectors.toSet()));

        Set<Long> deptIds = new HashSet<>();
        if (scopeValues.contains(DataScopeEnum.CUSTOM.getValue())) {
            Set<Long> customDeptIds = roleService.getRoleDeptIds(sysUser.getSysUserId());
            if (customDeptIds != null) {
                deptIds.addAll(customDeptIds);
            }
        }
        if (scopeValues.contains(DataScopeEnum.DEPT_AND_CHILD.getValue())) {
            Set<Long> childDeptIds = deptService.getDeptAndChildrenDeptIds(sysUser.getSysDeptId());
            if (childDeptIds != null) {
                deptIds.addAll(childDeptIds);
            }
        }
        dp.setDeptIds(deptIds);

        return dp;
    }

    private Set<String> buildPermissions(SysUserEntity entity) {
        if (Objects.equals(entity.getIsAdmin(), AdminEnum.Admin.getValue())) {
            return menuService.getAllPermissions();
        }
        return menuService.getPermissions(entity.getSysUserId());
    }
}
