package com.zs.sys.role.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.sys.role.domain.entity.SysRoleMenuEntity;
import com.zs.sys.role.mapper.SysRoleMenuMapper;
import com.zs.sys.role.service.ISysRoleMenuService;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zsadmin
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenuEntity> implements ISysRoleMenuService {


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Long sysRoleId, @NotNull List<Long> menuList) {
        // 先删除角色菜单关系
        this.baseMapper.delete(new LambdaQueryWrapper<SysRoleMenuEntity>().eq(SysRoleMenuEntity::getSysRoleId, sysRoleId));

        // 保存角色菜单关系
        for (Long sysMenuId : menuList) {
            SysRoleMenuEntity sysRoleMenuEntity = new SysRoleMenuEntity();
            sysRoleMenuEntity.setSysRoleId(sysRoleId);
            sysRoleMenuEntity.setSysMenuId(sysMenuId);
            this.baseMapper.insert(sysRoleMenuEntity);
        }


    }

    @NotNull
    @Override
    public List<Long> getMenuList(Long sysRoleId) {
        List<SysRoleMenuEntity> menuEntityList = this.baseMapper.selectList(new LambdaQueryWrapper<SysRoleMenuEntity>().eq(SysRoleMenuEntity::getSysRoleId, sysRoleId));
        return menuEntityList.stream().map(SysRoleMenuEntity::getSysMenuId).collect(Collectors.toList());
    }

    @Override
    public Long getCount(Long sysMenuId) {
        return this.baseMapper.selectCount(new LambdaQueryWrapper<SysRoleMenuEntity>().eq(SysRoleMenuEntity::getSysMenuId, sysMenuId));
    }

    @Override
    public void save(Long sysRoleId, List<Long> menuIdList, Long sysTenantId) {
        // 先删除角色菜单关系
        this.baseMapper.delete(new LambdaQueryWrapper<SysRoleMenuEntity>().eq(SysRoleMenuEntity::getSysRoleId, sysRoleId));

        // 保存角色菜单关系
        for (Long sysMenuId : menuIdList) {
            SysRoleMenuEntity sysRoleMenuEntity = new SysRoleMenuEntity();
            sysRoleMenuEntity.setSysRoleId(sysRoleId);
            sysRoleMenuEntity.setSysMenuId(sysMenuId);
            sysRoleMenuEntity.setTenantId(sysTenantId);
            this.baseMapper.insert(sysRoleMenuEntity);
        }
    }

    @Override
    public void delByMenuId(Long sysMenuId) {
        this.baseMapper.delete(new LambdaQueryWrapper<SysRoleMenuEntity>().eq(SysRoleMenuEntity::getSysMenuId, sysMenuId));
    }
}
