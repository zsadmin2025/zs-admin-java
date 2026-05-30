package com.zs.sys.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.enums.AdminEnum;
import com.zs.common.core.enums.DataScopeEnum;
import com.zs.common.core.enums.StatusEnum;
import com.zs.common.core.exception.ZsException;
import com.zs.common.core.model.DataPermission;
import com.zs.common.core.model.LoginUserInfo;
import com.zs.common.core.model.user.SysUser;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.common.security.service.PlatformUserDetailsService;
import com.zs.sys.dept.service.ISysDeptService;
import com.zs.sys.menu.service.ISysMenuService;
import com.zs.sys.role.domain.entity.SysRoleEntity;
import com.zs.sys.role.service.ISysRoleService;
import com.zs.sys.user.domain.dto.SysUserDeptPostDTO;
import com.zs.sys.user.domain.entity.SysUserEntity;
import com.zs.sys.user.domain.params.SysUserAddParams;
import com.zs.sys.user.domain.params.SysUserPasswordParams;
import com.zs.sys.user.domain.params.SysUserQueryParams;
import com.zs.sys.user.domain.params.SysUserUpdateParams;
import com.zs.sys.user.domain.vo.SysUserInfoVO;
import com.zs.sys.user.domain.vo.SysUserVO;
import com.zs.sys.user.mapper.SysUserMapper;
import com.zs.sys.user.service.ISysUserDeptPostService;
import com.zs.sys.user.service.ISysUserRoleService;
import com.zs.sys.user.service.ISysUserService;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zsadmin
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserEntity> implements ISysUserService, PlatformUserDetailsService {


    @Resource
    private ISysMenuService iSysMenuService;
    @Resource
    private ISysDeptService iSysDeptService;
    @Resource
    private ISysRoleService iSysRoleService;
    @Resource
    private ISysUserRoleService iSysUserRoleService;
    @Resource
    private ISysUserDeptPostService iSysUserDeptPostService;


    @NotNull
    @Override
    public PageResult<SysUserVO> page(@NotNull SysUserQueryParams sysUserQueryParams) {
        Page<SysUserEntity> page = new PageInfo<>(sysUserQueryParams);
        Map<String, Object> params = BeanUtil.beanToMap(sysUserQueryParams);
        List<Long> deptList = iSysDeptService.getSubDeptIdList(sysUserQueryParams.getSysDeptId());

        if (CollectionUtil.isNotEmpty(deptList)) {
            params.put("deptList", deptList);
        }
        IPage<SysUserEntity> iPage = baseMapper.page(page, params);
        page.getRecords().forEach(item -> {
            item.setDeptName(iSysDeptService.getBySysDeptId(item.getSysDeptId()));
        });
        List<SysUserVO> list = BeanUtil.copyToList(iPage.getRecords(), SysUserVO.class);

        return new PageResult<>(list, page.getTotal(), SysUserVO.class);
    }

    @Nullable
    @Override
    public List<SysUserVO> list(@NotNull SysUserQueryParams sysUserQueryParams) {
        List<Long> deptList = iSysDeptService.getSubDeptIdList(sysUserQueryParams.getSysDeptId());

        Map<String, Object> params = BeanUtil.beanToMap(sysUserQueryParams);
        params.put("status", StatusEnum.NORMAL.getValue());
        params.put("deptList", deptList);
        List<SysUserEntity> list = this.baseMapper.getList(params);

        return BeanUtil.copyToList(list, SysUserVO.class);
    }

    @Override
    public List<SysUserVO> getUserList(Long[] sysUserIds) {

        sysUserIds = Arrays.stream(sysUserIds).filter(Objects::nonNull).toArray(Long[]::new);
        if (sysUserIds.length == 0) {
            return Collections.emptyList();
        }
        List<Long> idList = List.of(sysUserIds);
        List<SysUserEntity> list = this.baseMapper.getUserList(idList);
        return BeanUtil.copyToList(list, SysUserVO.class);
    }

    @Override
    public List<SysUserVO> getUserListByDeptId(List<Long> sysDeptIds) {
        List<SysUserEntity> sysUserList = this.baseMapper.selectList(new LambdaQueryWrapper<SysUserEntity>().in(SysUserEntity::getSysDeptId, sysDeptIds));
        return BeanUtil.copyToList(sysUserList, SysUserVO.class);
    }

    @Override
    public List<SysUserVO> getUserListByPostId(List<Long> sysPostIds) {
        List<SysUserEntity> sysUserList = this.baseMapper.selectList(new LambdaQueryWrapper<SysUserEntity>().in(SysUserEntity::getSysPostId, sysPostIds));
        return BeanUtil.copyToList(sysUserList, SysUserVO.class);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@NotNull SysUserAddParams sysUserAddParams) {
        SysUserEntity sysUserEntity = BeanUtil.copyProperties(sysUserAddParams, SysUserEntity.class);
        // 判断登录账号不能重复
        if (exists(sysUserEntity)) {
            throw new RuntimeException("登录账号已存在");
        }

        sysUserEntity.setPassword(new BCryptPasswordEncoder().encode(sysUserEntity.getPassword()));
        this.baseMapper.insert(sysUserEntity);
        // 保存用户与部门关系
        iSysUserDeptPostService.saveOrUpdate(sysUserEntity.getSysUserId(), sysUserAddParams.getDeptPostList());
        // 保存用户与角色关系
        iSysUserRoleService.saveOrUpdate(sysUserEntity.getSysUserId(), sysUserAddParams.getRoleIdList());
    }

    private boolean exists(SysUserEntity sysUserEntity) {
        // 判断登录账号不能重复,  排除自身
        return this.baseMapper.exists(Wrappers.<SysUserEntity>lambdaQuery().eq(SysUserEntity::getUsername, sysUserEntity.getUsername()).ne(SysUserEntity::getSysUserId, sysUserEntity.getSysUserId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NotNull SysUserUpdateParams sysUserUpdateParams) {
        SysUserEntity sysUserEntity = BeanUtil.copyProperties(sysUserUpdateParams, SysUserEntity.class);
        // 判断登录账号不能重复
        if (exists(sysUserEntity)) {
            throw new RuntimeException("登录账号已存在");
        }
        this.baseMapper.updateById(sysUserEntity);

        if (sysUserUpdateParams.getDeptPostList() != null && !sysUserUpdateParams.getDeptPostList().isEmpty()) {
            // 保存用户与部门关系
            iSysUserDeptPostService.saveOrUpdate(sysUserEntity.getSysUserId(), sysUserUpdateParams.getDeptPostList());
        }
        if(  sysUserUpdateParams.getRoleIdList() != null && !sysUserUpdateParams.getRoleIdList().isEmpty()){
            // 先删除用户与角色关系
            iSysUserRoleService.saveOrUpdate(sysUserEntity.getSysUserId(), sysUserUpdateParams.getRoleIdList());
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelById(@NotNull Long[] ids) {
        for (Long id : ids) {
            // 删除用户与部门职位关系
            iSysUserDeptPostService.delByUserId(id);
            // 删除用户与角色关系
            iSysUserRoleService.delByUserId(id);
            this.baseMapper.updateDeleted(id);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delById(Long id) {
        // 删除用户与部门职位关系
        iSysUserDeptPostService.delByUserId(id);
        // 删除用户与角色关系
        iSysUserRoleService.delByUserId(id);
        this.baseMapper.updateDeleted(id);
    }


    @Override
    public void resetPassword(@NotNull SysUserPasswordParams sysUserPasswordParams) {
        // 校验两次密码是否一致
        if (!sysUserPasswordParams.getNewPassword().equals(sysUserPasswordParams.getConfirmPassword())) {
            throw new ZsException("两次输入密码不一致");
        }

        SysUserEntity sysUserEntity = new SysUserEntity();
        sysUserEntity.setSysUserId(sysUserPasswordParams.getSysUserId());
        sysUserEntity.setPassword(new BCryptPasswordEncoder().encode(sysUserPasswordParams.getNewPassword()));

        this.baseMapper.updateById(sysUserEntity);
    }

    @NotNull
    @Override
    public SysUserInfoVO getById(Long id) {
        SysUserEntity sysUserEntity = baseMapper.selectById(id);
        SysUserInfoVO sysUserInfoVO = BeanUtil.copyProperties(sysUserEntity, SysUserInfoVO.class);
        // 查询用户的角色
        List<Long> roleIdList = iSysUserRoleService.queryRoleIdList(sysUserEntity.getSysUserId());
        // 查询用户所属的任职部门
        List<SysUserDeptPostDTO> deptPostList = iSysUserDeptPostService.getByUserId(sysUserEntity.getSysUserId());
        sysUserInfoVO.setRoleIdList(roleIdList);
        sysUserInfoVO.setDeptPostList(deptPostList);

        return sysUserInfoVO;
    }


    public Set<String> getPermissions(@NotNull SysUserEntity sysUserEntity) {
        // 超级管理员获取全部,其他根据角色获取
        if (sysUserEntity.getIsAdmin() == AdminEnum.Admin.getValue()) {
            return iSysMenuService.getAllPermissions();
        } else {
            return iSysMenuService.getPermissions(sysUserEntity.getSysUserId());
        }
    }


    @NotNull
    @Override
    public UserDetails loadUserByUsername(String username) {
        SysUserEntity sysUserEntity = baseMapper.selectByUserName(username);
        // 查询数据库用户是否存在
        if (Objects.isNull(sysUserEntity)) {
            throw new UsernameNotFoundException("用户不存在");
        }
        SysUser sysUser = toSysUser(sysUserEntity);

        // 获取用户的角色
        List<SysRoleEntity> roles = iSysRoleService.findByUserId(sysUserEntity.getSysUserId());
        // 构建数据权限
        DataPermission dataPermission = buildDataPermission(sysUser, roles);

        return new LoginUserInfo(sysUser, getPermissions(sysUserEntity), dataPermission);
    }




    private DataPermission buildDataPermission(SysUser sysUser, List<SysRoleEntity> roles) {
        DataPermission dataPermission = new DataPermission();
        dataPermission.setUserId(sysUser.getSysUserId());
        dataPermission.setDeptId(sysUser.getSysDeptId());

        Set<Long> roleIds = roles.stream()
                .map(SysRoleEntity::getSysRoleId)
                .collect(Collectors.toSet());
        dataPermission.setRoleIds(roleIds);

        Set<Integer> dataScopeTypes = roles.stream()
                .map(SysRoleEntity::getDataScope)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<DataScopeEnum> dataScopeEnumValues = dataScopeTypes.stream()
                .map(DataScopeEnum::value)
                .collect(Collectors.toSet());
        dataPermission.setDataScopeTypes(dataScopeEnumValues);

        Set<Long> deptIds = new HashSet<>();

        // 自定义权限
        if (dataScopeTypes.contains(DataScopeEnum.CUSTOM.getValue())) {
            // 自定义权限，一个用户对应多个角色，不同的角色自定义权限也不同，这里直接使用用户的ID去查询自定义权限的部门ID
            Set<Long> customDeptIds = iSysRoleService.getRoleDeptIds(sysUser.getSysUserId());
            if (CollectionUtil.isNotEmpty(customDeptIds)) {
                deptIds.addAll(customDeptIds);
            }
        }

        // 本部门及子部门权限
        if (dataScopeTypes.contains(DataScopeEnum.DEPT_AND_CHILD.getValue())) {
            Set<Long> childDeptIds = iSysDeptService.getDeptAndChildrenDeptIds(sysUser.getSysDeptId());
            if (CollectionUtil.isNotEmpty(childDeptIds)) {
                deptIds.addAll(childDeptIds);
            }
        }

        dataPermission.setDeptIds(deptIds);

        return dataPermission;
    }

    @Override
    public LoginUserInfo loadUserByUsernameAndTenant(String username, String tenantId) {
        SysUserEntity sysUserEntity = baseMapper.selectByUserNameAndTenant(username, tenantId);
        if (Objects.isNull(sysUserEntity)) {
            throw new UsernameNotFoundException("用户不存在");
        }
        SysUser sysUser = toSysUser(sysUserEntity);
        List<SysRoleEntity> roles = iSysRoleService.findByUserId(sysUserEntity.getSysUserId());
        DataPermission dataPermission = buildDataPermission(sysUser, roles);
        return new LoginUserInfo(sysUser, getPermissions(sysUserEntity), dataPermission);
    }

    private SysUser toSysUser(SysUserEntity entity) {
        SysUser sysUser = BeanUtil.toBean(entity, SysUser.class);
        sysUser.setUserId(entity.getSysUserId());
        return sysUser;
    }

}
