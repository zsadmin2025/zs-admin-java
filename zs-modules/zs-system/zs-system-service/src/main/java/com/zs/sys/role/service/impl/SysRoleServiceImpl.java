package com.zs.sys.role.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.exception.ZsException;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.sys.role.domain.entity.SysRoleEntity;
import com.zs.sys.role.domain.entity.SysRoleMenuEntity;
import com.zs.sys.role.domain.params.SysRoleAddParams;
import com.zs.sys.role.domain.params.SysRoleQueryParams;
import com.zs.sys.role.domain.vo.SysRoleVO;
import com.zs.sys.role.mapper.SysRoleMapper;
import com.zs.sys.role.service.ISysRoleDeptService;
import com.zs.sys.role.service.ISysRoleMenuService;
import com.zs.sys.role.service.ISysRoleService;
import com.zs.sys.user.service.ISysUserRoleService;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zsadmin
 */

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRoleEntity> implements ISysRoleService{

    @Resource
    private ISysRoleMenuService iSysRoleMenuService;
    @Resource
    private ISysUserRoleService iSysUserRoleService;
    @Resource
    private ISysRoleDeptService iSysRoleDeptService;

    @NotNull
    @Override
    public PageResult<SysRoleVO> page(@NotNull SysRoleQueryParams sysRoleQueryParams) {

        Page<SysRoleEntity> page = new PageInfo<>(sysRoleQueryParams);
        LambdaQueryWrapper<SysRoleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Strings.isNotEmpty(sysRoleQueryParams.getRoleName()), SysRoleEntity::getRoleName, sysRoleQueryParams.getRoleName());

        IPage<SysRoleEntity> iPage = baseMapper.selectPage(page, wrapper);
        List<SysRoleVO> list = BeanUtil.copyToList(iPage.getRecords(), SysRoleVO.class);

        return new PageResult<>(list, page.getTotal(), SysRoleVO.class);
    }


    @Nullable
    @Override
    public List<SysRoleVO> getList(@NotNull SysRoleQueryParams sysRoleQueryParams) {
        LambdaQueryWrapper<SysRoleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Strings.isNotEmpty(sysRoleQueryParams.getRoleName()), SysRoleEntity::getRoleName, sysRoleQueryParams.getRoleName());
        return BeanUtil.copyToList(baseMapper.selectList(wrapper), SysRoleVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@NotNull SysRoleAddParams sysRoleAddParams) {
        SysRoleEntity sysRoleEntity = BeanUtil.copyProperties(sysRoleAddParams, SysRoleEntity.class);
        baseMapper.insert(sysRoleEntity);
        if (!sysRoleAddParams.getMenuList().isEmpty()) {
            iSysRoleMenuService.save(sysRoleEntity.getSysRoleId(), sysRoleAddParams.getMenuList());
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NotNull SysRoleAddParams sysRoleAddParams) {
        SysRoleEntity sysRoleEntity = BeanUtil.copyProperties(sysRoleAddParams, SysRoleEntity.class);
        baseMapper.updateById(sysRoleEntity);
        if (!sysRoleAddParams.getMenuList().isEmpty()) {
            iSysRoleMenuService.save(sysRoleEntity.getSysRoleId(), sysRoleAddParams.getMenuList());
        }

        if (!sysRoleAddParams.getDeptList().isEmpty()) {
            iSysRoleDeptService.save(sysRoleEntity.getSysRoleId(), sysRoleAddParams.getDeptList());
        }
    }

    @NotNull
    @Override
    public SysRoleVO getById(Long id) {
        SysRoleVO sysRoleVO = BeanUtil.copyProperties(baseMapper.selectById(id), SysRoleVO.class);
        // 角色对应的菜单权限
        List<Long> menuList = iSysRoleMenuService.getMenuList(id);
        sysRoleVO.setMenuList(menuList);

        // 角色对应的部门权限
        List<Long> deptList = iSysRoleDeptService.getDeptIds(id);
        sysRoleVO.setDeptList(deptList);
        return sysRoleVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        List<Long> userRoleIds = iSysUserRoleService.queryByRoleId(id);
        if (!userRoleIds.isEmpty()) {
            throw new ZsException("角色正在使用中，不能删除");
        }

        baseMapper.deleteById(id);
        // 删除角色对应的菜单权限
        iSysRoleMenuService.remove(new QueryWrapper<SysRoleMenuEntity>().eq("sys_role_id", id));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchDelById(@NotNull List<Long> sysRoleIds) {

        for (Long sysRoleId : sysRoleIds) {

            List<Long> userRoleIds = iSysUserRoleService.queryByRoleId(sysRoleId);
            if (!userRoleIds.isEmpty()) {
                throw new ZsException("角色正在使用中，不能删除");
            }
        }

        baseMapper.deleteByIds(sysRoleIds);
        // 删除角色对应的菜单权限
        iSysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenuEntity>().in(SysRoleMenuEntity::getSysRoleId, sysRoleIds));
    }

    @Override
    public List<SysRoleEntity> findByUserId(Long sysUserId) {
        return this.baseMapper.getList(sysUserId);
    }

    @Override
    public List<SysRoleVO> getList(List<Long> sysRoleIds) {

        List<SysRoleEntity> sysRoleEntityList = this.baseMapper.selectList(new LambdaQueryWrapper<SysRoleEntity>().in(SysRoleEntity::getSysRoleId, sysRoleIds));

        return BeanUtil.copyToList(sysRoleEntityList, SysRoleVO.class);
    }


    @NotNull
    @Override
    public Set<Integer> getDataScope(Long userId) {
        List<SysRoleEntity> roleEntityList = this.baseMapper.getList(userId);
        return roleEntityList.stream().map(SysRoleEntity::getDataScope).collect(Collectors.toSet());
    }

    @Override
    public Set<Long> getRoleDeptIds(Long userId) {
        return this.baseMapper.getDataScopeDeptIds(userId);
    }
}
