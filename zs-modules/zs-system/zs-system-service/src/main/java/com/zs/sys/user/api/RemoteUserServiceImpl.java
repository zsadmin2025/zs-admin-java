package com.zs.sys.user.api;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.zs.sys.api.role.RemoteUserService;
import com.zs.sys.user.domain.vo.SysUserVO;
import com.zs.sys.user.service.ISysUserService;

import cn.hutool.core.collection.CollUtil;
import jakarta.annotation.Resource;

/**
 * 远程用户服务实现，委托给 ISysUserService 处理
 *
 * @author zs
 */
@Service
public class RemoteUserServiceImpl implements RemoteUserService {

    @Resource
    private ISysUserService sysUserService;

    @Override
    public Map<Long, String> getUserNameMap(List<Long> userIds) {
        if (CollUtil.isEmpty(userIds)) {
            return Collections.emptyMap();
        }
        List<SysUserVO> users = sysUserService.getUserList(userIds.toArray(Long[]::new));
        if (CollUtil.isEmpty(users)) {
            return Collections.emptyMap();
        }
        return users.stream()
                .filter(u -> u.getSysUserId() != null)
                .collect(Collectors.toMap(
                        SysUserVO::getSysUserId,
                        u -> u.getRealName() != null ? u.getRealName() : u.getUsername(),
                        (a, b) -> a
                ));
    }

}
