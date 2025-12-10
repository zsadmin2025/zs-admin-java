package com.zs.sys.menu.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.enums.AdminEnum;
import com.zs.common.core.exception.ZsException;
import com.zs.common.core.model.LoginUserInfo;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.common.core.utils.SecurityUtil;
import com.zs.common.core.utils.TreeUtil;
import com.zs.sys.menu.domain.entity.SysMenuEntity;
import com.zs.sys.menu.domain.params.*;
import com.zs.sys.menu.domain.vo.SysMenuBtnPermissionsVO;
import com.zs.sys.menu.domain.vo.SysMenuListVO;
import com.zs.sys.menu.domain.vo.SysMenuVO;
import com.zs.sys.menu.domain.vo.SysMetaVO;
import com.zs.sys.menu.mapper.SysMenuMapper;
import com.zs.sys.menu.service.ISysMenuService;
import com.zs.sys.role.service.ISysRoleMenuService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @author zsadmin
 */

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenuEntity> implements ISysMenuService {


    @Resource
    private ISysRoleMenuService iSysRoleMenuService;

    @NotNull
    @Override
    public PageResult<SysMenuVO> page(@NotNull SysMenuQueryParams sysMenuQueryParams) {
        Page<SysMenuEntity> page = new PageInfo<>(sysMenuQueryParams);
        QueryWrapper<SysMenuEntity> wrapper = new QueryWrapper<>();

        IPage<SysMenuEntity> iPage = baseMapper.selectPage(page, wrapper);
        List<SysMenuVO> list = BeanUtil.copyToList(iPage.getRecords(), SysMenuVO.class);

        return new PageResult<>(list, page.getTotal(), SysMenuVO.class);


    }

    @NotNull
    @Override
    public List<SysMenuVO> getNavList() {
        LoginUserInfo loginUserInfo = SecurityUtil.getUserInfo();
        List<SysMenuEntity> list;
        if (loginUserInfo.sysUser.getIsAdmin() == AdminEnum.Admin.getValue()) {
            list = baseMapper.selectList(new LambdaQueryWrapper<SysMenuEntity>().eq(SysMenuEntity::getStatus, 1).eq(SysMenuEntity::getVisible, 1).in(SysMenuEntity::getType, 1, 2, 4, 5).orderByAsc(SysMenuEntity::getSort));
        } else {
            list = baseMapper.getMenuList(loginUserInfo.sysUser.getSysUserId());
        }


        List<SysMenuVO> menuVOList = list.stream()
                .map(this::convertToMenuVO)
                .collect(Collectors.toList());


        return TreeUtil.build(menuVOList, 0L);
    }

    private SysMenuVO convertToMenuVO(SysMenuEntity menu) {
        SysMenuVO menuVO = new SysMenuVO();
        menuVO.setMeta(createMetaVO(menu));

        BeanUtil.copyProperties(menu, menuVO);
        return menuVO;
    }

    private SysMetaVO createMetaVO(SysMenuEntity menu) {
        SysMetaVO meta = new SysMetaVO();
        meta.setRequiresAuth(Optional.ofNullable(menu.getRequiresAuth()).orElse(0) == 1);
        meta.setIcon(menu.getIcon());
        meta.setTitle(menu.getTitle());
        meta.setHideInMenu(Optional.ofNullable(menu.getHideInMenu()).orElse(0) == 1);
        meta.setHideChildrenInMenu(Optional.ofNullable(menu.getHideChildrenInMenu()).orElse(0) == 1);
        meta.setActiveMenu(menu.getActiveMenu());
        meta.setNoAffix(Optional.ofNullable(menu.getNoAffix()).orElse(0) == 1);
        meta.setIgnoreCache(Optional.ofNullable(menu.getIgnoreCache()).orElse(0) == 1);
        meta.setSort(menu.getSort());
        return meta;
    }

    @NotNull
    @Override
    public List<SysMenuListVO> getList(@NotNull SysMenuQueryParams sysMenuQueryParams) {
        LambdaQueryWrapper<SysMenuEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Strings.isNotEmpty(sysMenuQueryParams.getTitle()), SysMenuEntity::getTitle, sysMenuQueryParams.getTitle());
        queryWrapper.in(SysMenuEntity::getType, 1, 2, 4, 5);
        queryWrapper.orderByAsc(SysMenuEntity::getSort);
        List<SysMenuEntity> entityList = baseMapper.selectList(queryWrapper);

        List<SysMenuListVO> menuVOList = BeanUtil.copyToList(entityList, SysMenuListVO.class);

        List<SysMenuListVO> tree = TreeUtil.build(BeanUtil.copyToList(menuVOList, SysMenuListVO.class), 0L);

        // 清理空 children
        TreeUtil.cleanEmptyChildren(tree);
        return  tree;
    }

    @Override
    public List<SysMenuListVO> listPermission(SysMenuQueryParams sysMenuQueryParams) {
        LambdaQueryWrapper<SysMenuEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Strings.isNotEmpty(sysMenuQueryParams.getTitle()), SysMenuEntity::getTitle, sysMenuQueryParams.getTitle());
        queryWrapper.in(SysMenuEntity::getType, 1, 2, 3,4, 5);
        queryWrapper.orderByAsc(SysMenuEntity::getSort);
        List<SysMenuEntity> entityList = baseMapper.selectList(queryWrapper);

        List<SysMenuListVO> menuVOList = BeanUtil.copyToList(entityList, SysMenuListVO.class);

        List<SysMenuListVO> tree = TreeUtil.build(BeanUtil.copyToList(menuVOList, SysMenuListVO.class), 0L);

        // 清理空 children
        TreeUtil.cleanEmptyChildren(tree);
        return  tree;
    }

    @NotNull
    public List<SysMenuEntity> getTreeParent(@NotNull SysMenuEntity sysMenuEntity, @NotNull List<SysMenuEntity> deptList) {
        Map<Long, SysMenuEntity> map = deptList.stream().collect(Collectors.toMap(SysMenuEntity::getSysMenuId, Function.identity()));
        List<SysMenuEntity> pidList = new ArrayList<>();
        getTreePid(sysMenuEntity.getPid(), map, pidList);
        pidList.add(sysMenuEntity);
        return pidList;
    }

    public void getTreePid(Long pid, @NotNull Map<Long, SysMenuEntity> map, @NotNull List<SysMenuEntity> pidList) {
        SysMenuEntity parent = map.get(pid);
        if (parent != null) {
            pidList.add(parent);
            getTreePid(parent.getPid(), map, pidList);
        }
    }

    @Override
    public void save(SysMenuAddParams sysMenuAddParams) {
        SysMenuEntity sysMenuEntity = BeanUtil.copyProperties(sysMenuAddParams, SysMenuEntity.class);
        sysMenuEntity.setRequiresAuth(sysMenuAddParams.isRequiresAuth() ? 1 : 0);
        sysMenuEntity.setHideInMenu(sysMenuAddParams.isHideInMenu() ? 1 : 0);
        sysMenuEntity.setHideChildrenInMenu(sysMenuAddParams.isHideChildrenInMenu() ? 1 : 0);
        sysMenuEntity.setNoAffix(sysMenuAddParams.isNoAffix() ? 1 : 0);
        sysMenuEntity.setIgnoreCache(sysMenuAddParams.isIgnoreCache() ? 1 : 0);


        baseMapper.insert(sysMenuEntity);
    }

    @Override
    public void update(SysMenuUpdateParams sysMenuUpdateParams) {
        SysMenuEntity sysMenuEntity = BeanUtil.copyProperties(sysMenuUpdateParams, SysMenuEntity.class);
        sysMenuEntity.setRequiresAuth(sysMenuUpdateParams.isRequiresAuth() ? 1 : 0);
        sysMenuEntity.setHideInMenu(sysMenuUpdateParams.isHideInMenu() ? 1 : 0);
        sysMenuEntity.setHideChildrenInMenu(sysMenuUpdateParams.isHideChildrenInMenu() ? 1 : 0);
        sysMenuEntity.setNoAffix(sysMenuUpdateParams.isNoAffix() ? 1 : 0);
        sysMenuEntity.setIgnoreCache(sysMenuUpdateParams.isIgnoreCache() ? 1 : 0);

        baseMapper.updateById(sysMenuEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        // 查询是否存在子菜单
        Long count = baseMapper.selectCount(new LambdaQueryWrapper<SysMenuEntity>().eq(SysMenuEntity::getPid,  id));
        if (count > 0) {
            throw new ZsException("请先删除子菜单");
        }

        baseMapper.deleteById(id);

        // 删除菜单和角色绑定关系
        iSysRoleMenuService.delByMenuId(id);

    }

    @Override
    public SysMenuListVO getById(Long id) {
        return BeanUtil.copyProperties(baseMapper.selectById(id), SysMenuListVO.class);
    }

    @NotNull
    @Override
    public Set<String> getAllPermissions() {
        List<SysMenuEntity> sysMenuEntityList = baseMapper.selectList(new LambdaQueryWrapper<SysMenuEntity>().eq(SysMenuEntity::getType, 3));
        return sysMenuEntityList.stream().map(SysMenuEntity::getPermissions).filter(Objects::nonNull).flatMap(permissions -> Arrays.stream(permissions.trim().split(","))).collect(Collectors.toSet());
    }

    @NotNull
    @Override
    public Set<String> getPermissions(Long sysUserId) {
        List<SysMenuEntity> sysMenuEntityList = baseMapper.getPermissions(sysUserId);
        return sysMenuEntityList.stream().map(SysMenuEntity::getPermissions).filter(Objects::nonNull).flatMap(permissions -> Arrays.stream(permissions.trim().split(","))).collect(Collectors.toSet());
    }

    @Override
    public List<SysMenuBtnPermissionsVO> getPermissionsBySysMenuId(Long sysMenuId) {
        List<SysMenuEntity> sysMenuEntityList = baseMapper.selectList(new LambdaQueryWrapper<SysMenuEntity>().eq(SysMenuEntity::getType, 3).eq(SysMenuEntity::getPid, sysMenuId).orderByAsc(SysMenuEntity::getSort));
        return  BeanUtil.copyToList(sysMenuEntityList, SysMenuBtnPermissionsVO.class);
    }

    @Override
    public void saveBtnPermissions(SysMenuBtnPermissionsAddParams sysMenuBtnPermissionsAddParams) {
        baseMapper.insert(BeanUtil.copyProperties(sysMenuBtnPermissionsAddParams, SysMenuEntity.class));
    }

    @Override
    public void updateBtnPermissions(SysMenuBtnPermissionsUpdateParams sysMenuBtnPermissionsUpdateParams) {
        baseMapper.updateById(BeanUtil.copyProperties(sysMenuBtnPermissionsUpdateParams, SysMenuEntity.class));
    }


    @Override
    public SysMenuBtnPermissionsVO getBtnPermissionsBySysMenuId(Long sysMenuId) {
        return BeanUtil.copyProperties(baseMapper.selectById(sysMenuId), SysMenuBtnPermissionsVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBtnPermissions(Long sysMenuId) {
        // 查询是否有角色管理该按钮权限
        Long count = iSysRoleMenuService.getCount(sysMenuId);
        if (count > 0) {
            throw new ZsException("该权限按钮有管理角色，请先接触按钮和角色的关联。");
        }
        baseMapper.deleteById(sysMenuId);
    }
}
