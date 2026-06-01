package com.zs.sys.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.events.DataPermissionChangedEvent;
import com.zs.sys.user.domain.entity.SysUserRoleEntity;
import com.zs.sys.user.mapper.SysUserRoleMapper;
import com.zs.sys.user.service.ISysUserRoleService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zsadmin
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRoleEntity> implements ISysUserRoleService {

    @Resource
    private ApplicationEventPublisher eventPublisher;

    @Override
    public void saveOrUpdate(Long sysUserId, @NotNull List<Long> sysRoleIdList) {
        // 先删除用户与角色关系
        baseMapper.delete(new LambdaQueryWrapper<SysUserRoleEntity>().eq(SysUserRoleEntity::getSysUserId, sysUserId));
        // 在添加用户与角色关系
        if (!sysRoleIdList.isEmpty()) {
            for (Long sysRoleId : sysRoleIdList) {
                SysUserRoleEntity sysUserRoleEntity = new SysUserRoleEntity();
                sysUserRoleEntity.setSysUserId(sysUserId);
                sysUserRoleEntity.setSysRoleId(sysRoleId);
                baseMapper.insert(sysUserRoleEntity);
            }
        }

        // 发布用户角色关联变更事件
        eventPublisher.publishEvent(new DataPermissionChangedEvent(
                this, DataPermissionChangedEvent.ChangeType.USER_ROLE_CHANGED,
                Set.of(sysUserId)));
    }

    @NotNull
    @Override
    public List<Long> queryRoleIdList(Long sysUserId) {
        List<SysUserRoleEntity> sysUserRoleEntityList = baseMapper.selectList(new LambdaQueryWrapper<SysUserRoleEntity>().eq(SysUserRoleEntity::getSysUserId, sysUserId));
        return sysUserRoleEntityList.stream().map(SysUserRoleEntity::getSysRoleId).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public void delByUserId(Long sysUserId) {
        this.baseMapper.delete(new LambdaQueryWrapper<SysUserRoleEntity>().eq(SysUserRoleEntity::getSysUserId, sysUserId));
    }

    @NotNull
    @Override
    public List<Long> queryByRoleId(Long sysRoleId) {
        List<SysUserRoleEntity> sysUserRoleEntityList = baseMapper.selectList(new LambdaQueryWrapper<SysUserRoleEntity>().eq(SysUserRoleEntity::getSysRoleId, sysRoleId));
        return sysUserRoleEntityList.stream().map(SysUserRoleEntity::getSysUserId).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveUserRole(SysUserRoleEntity sysUserRoleEntity) {
        // 保存用户角色关联
        // 先删除，后新增
        baseMapper.delete(new LambdaQueryWrapper<SysUserRoleEntity>().eq(SysUserRoleEntity::getSysUserId, sysUserRoleEntity.getSysUserId())
                .eq(SysUserRoleEntity::getSysRoleId, sysUserRoleEntity.getSysRoleId()));
        baseMapper.insert(sysUserRoleEntity);

    }

    @Override
    public List<Long> queryUserIdList(List<Long> sysRoleIdList) {
        List<SysUserRoleEntity> sysUserRoleEntityList = baseMapper.selectList(new LambdaQueryWrapper<SysUserRoleEntity>().in(SysUserRoleEntity::getSysRoleId, sysRoleIdList));
        if (!sysUserRoleEntityList.isEmpty()) {
            return sysUserRoleEntityList.stream().map(SysUserRoleEntity::getSysUserId).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return List.of();
    }


}
