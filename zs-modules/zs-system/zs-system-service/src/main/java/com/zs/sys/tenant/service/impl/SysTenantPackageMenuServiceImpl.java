package com.zs.sys.tenant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.sys.tenant.domain.entity.SysTenantPackageMenuEntity;
import com.zs.sys.tenant.mapper.SysTenantPackageMenuMapper;
import com.zs.sys.tenant.service.SysTenantPackageMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 套餐菜单关联表 服务实现类
 * </p>
 *
 * @author zs
 * @since 2025-08-20 17:56:11
 */
@Service
public class SysTenantPackageMenuServiceImpl extends ServiceImpl<SysTenantPackageMenuMapper, SysTenantPackageMenuEntity> implements SysTenantPackageMenuService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(Long sysTenantPackageId, List<Long> menuIdList) {
        List<SysTenantPackageMenuEntity> entities = menuIdList.stream()
                .map(menuId -> buildEntity(sysTenantPackageId, menuId))
                .toList();

        this.saveBatch(entities);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Long sysTenantPackageId, List<Long> menuIdList) {


        List<SysTenantPackageMenuEntity> oldMenuList = this.lambdaQuery().eq(SysTenantPackageMenuEntity::getSysTenantPackageId, sysTenantPackageId).list();

        // 1. 旧的菜单id(数据库已有的)
        List<Long> oldMenuIds = oldMenuList.stream().map(SysTenantPackageMenuEntity::getSysMenuId).toList();

        // 2. 计算需删除和需新增的菜单ID
        // 需删除：旧有但新没有
        List<Long> toDelete = oldMenuIds.stream().filter(menuId -> !menuIdList.contains(menuId)).toList();

        // 需新增：新有但旧没有
        List<Long> toAdd = menuIdList.stream().filter(menuId -> !oldMenuIds.contains(menuId)).toList();

        // 3. 执行删除（如果有需要删除的）
        if (!toDelete.isEmpty()) {
            this.baseMapper.delete(new LambdaQueryWrapper<SysTenantPackageMenuEntity>().eq(SysTenantPackageMenuEntity::getSysTenantPackageId, sysTenantPackageId).in(SysTenantPackageMenuEntity::getSysMenuId, toDelete));
        }

        // 4. 执行新增（如果有需要新增的）
        if (!toAdd.isEmpty()) {
            List<SysTenantPackageMenuEntity> entitiesToAdd = toAdd.stream()
                    .map(menuId -> buildEntity(sysTenantPackageId, menuId))
                    .toList();

            this.saveBatch(entitiesToAdd);
        }


    }

    @Override
    public List<Long> listBySysTenantPackageId(Long sysTenantPackageId) {
        List<SysTenantPackageMenuEntity> entities = this.lambdaQuery().eq(SysTenantPackageMenuEntity::getSysTenantPackageId, sysTenantPackageId).list();
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> menuIds = entities.stream().map(SysTenantPackageMenuEntity::getSysMenuId).toList();
        if (menuIds.isEmpty()) {
            return Collections.emptyList();
        }

        return menuIds;
    }


    // 提取公共方法用于构建实体对象
    private SysTenantPackageMenuEntity buildEntity(Long sysTenantPackageId, Long menuId) {
        SysTenantPackageMenuEntity entity = new SysTenantPackageMenuEntity();
        entity.setSysTenantPackageId(sysTenantPackageId);
        entity.setSysMenuId(menuId);
        return entity;
    }
}