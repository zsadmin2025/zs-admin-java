package com.zs.bpm.org.impl;

import com.zs.bpm.org.BusinessOrgService;
import com.zs.sys.dept.domain.vo.SysDeptVO;
import com.zs.sys.dept.service.ISysDeptService;
import com.zs.sys.user.domain.vo.SysUserVO;
import com.zs.sys.user.service.ISysUserRoleService;
import com.zs.sys.user.service.ISysUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 业务组织服务实现类
 * <p>
 * 实现BusinessOrgService接口，提供用户、部门、角色、岗位等组织数据的查询能力。
 * 使用系统现有的服务来查询业务数据。
 * </p>
 *
 * @author zsadmin
 * @since 1.0.0
 */
@Slf4j
@Service
public class BusinessOrgServiceImpl implements BusinessOrgService {

    @Lazy
    @Resource
    private ISysUserService sysUserService;

    @Lazy
    @Resource
    private ISysUserRoleService sysUserRoleService;

    @Lazy
    @Resource
    private ISysDeptService sysDeptService;

    @Override
    public List<String> getUserIdsByRoleId(String roleId) {
        try {
            Long roleIdLong = Long.parseLong(roleId);
            List<Long> userIds = sysUserRoleService.queryByRoleId(roleIdLong);
            if (userIds == null || userIds.isEmpty()) {
                return Collections.emptyList();
            }
            return userIds.stream()
                    .map(String::valueOf)
                    .toList();
        } catch (Exception e) {
            log.error("根据角色ID查询用户失败：roleId={}", roleId, e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<String> getUserIdsByPostId(String postId) {
        try {
            Long postIdLong = Long.parseLong(postId);
            List<Long> postIdList = List.of(postIdLong);
            List<SysUserVO> users = sysUserService.getUserListByPostId(postIdList);
            if (users == null || users.isEmpty()) {
                return Collections.emptyList();
            }
            return users.stream()
                    .map(user -> String.valueOf(user.getSysUserId()))
                    .toList();
        } catch (Exception e) {
            log.error("根据岗位ID查询用户失败：postId={}", postId, e);
            return Collections.emptyList();
        }
    }

    @Override
    public String getDeptHeadUserId(String deptId) {
        try {
            Long deptIdLong = Long.parseLong(deptId);
            SysDeptVO dept = sysDeptService.getById(deptIdLong);
            if (dept != null && dept.getDeptHeadId() != null) {
                return String.valueOf(dept.getDeptHeadId());
            }
            return null;
        } catch (Exception e) {
            log.error("查询部门负责人失败：deptId={}", deptId, e);
            return null;
        }
    }

    @Override
    public String getLeaderUserId(String userId) {
        try {
            // 查询用户的直属上级
            // 这里需要根据实际业务逻辑实现
            // 暂时返回null，需要根据实际业务实现
            log.warn("查询直属上级功能需要根据业务实现：userId={}", userId);
            return null;
        } catch (Exception e) {
            log.error("查询直属上级失败：userId={}", userId, e);
            return null;
        }
    }

    @Override
    public List<String> getRoleIdsByUserId(String userId) {
        try {
            Long userIdLong = Long.parseLong(userId);
            List<Long> roleIds = sysUserRoleService.queryRoleIdList(userIdLong);
            if (roleIds == null || roleIds.isEmpty()) {
                return Collections.emptyList();
            }
            return roleIds.stream()
                    .map(String::valueOf)
                    .toList();
        } catch (Exception e) {
            log.error("查询用户角色失败：userId={}", userId, e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<String> getSystemAdminUserIds() {
        // 返回系统管理员用户ID列表
        // 这里可以根据实际业务需求查询管理员用户
        // 暂时返回空列表，需要根据实际业务实现
        log.warn("获取系统管理员用户ID列表功能需要根据业务实现");
        return new ArrayList<>();
    }
}
