package com.zs.sys.tenant.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.exception.ErrorCodeConstants;
import com.zs.common.core.exception.ZsException;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.common.core.tenant.TenantContext;
import com.zs.sys.role.domain.entity.SysRoleEntity;
import com.zs.sys.role.service.ISysRoleMenuService;
import com.zs.sys.role.service.ISysRoleService;
import com.zs.sys.tenant.domain.entity.SysTenantEntity;
import com.zs.sys.tenant.domain.params.SysTenantAddParams;
import com.zs.sys.tenant.domain.params.SysTenantPageQueryParams;
import com.zs.sys.tenant.domain.params.SysTenantSelectQueryParams;
import com.zs.sys.tenant.domain.params.SysTenantUpdateParams;
import com.zs.sys.tenant.domain.vo.SysTenantVO;
import com.zs.sys.tenant.mapper.SysTenantMapper;
import com.zs.sys.tenant.service.SysTenantPackageMenuService;
import com.zs.sys.tenant.service.SysTenantService;
import com.zs.sys.user.domain.entity.SysUserEntity;
import com.zs.sys.user.domain.entity.SysUserRoleEntity;
import com.zs.sys.user.service.ISysUserRoleService;
import com.zs.sys.user.service.ISysUserService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 租户信息表 服务实现类
 * </p>
 *
 * @author zs
 * @since 2025-08-07 23:42:45
 */
@Service
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenantEntity> implements SysTenantService {

    @Resource
    private ISysRoleService sysRoleService;
    @Resource
    private ISysRoleMenuService iSysRoleMenuService;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysUserRoleService iSysUserRoleService;
    @Resource
    private SysTenantPackageMenuService sysTenantPackageMenuService;
    @Resource
    private ISysRoleService iSysRoleService;

    @Override
    public PageResult<SysTenantVO> page(@NotNull SysTenantPageQueryParams sysTenantPageQueryParams) {

        Page<SysTenantEntity> page = new PageInfo<>(sysTenantPageQueryParams);
        LambdaQueryWrapper<SysTenantEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Strings.isNotEmpty(sysTenantPageQueryParams.getTenantName()), SysTenantEntity::getTenantName, sysTenantPageQueryParams.getTenantName())
                .like(Strings.isNotEmpty(sysTenantPageQueryParams.getContactPerson()),SysTenantEntity::getContactPerson, sysTenantPageQueryParams.getContactPerson())
                .like(Strings.isNotEmpty(sysTenantPageQueryParams.getContactPhone()),SysTenantEntity::getContactPhone, sysTenantPageQueryParams.getContactPhone())
                .like(Strings.isNotEmpty(sysTenantPageQueryParams.getContactEmail()),SysTenantEntity::getContactEmail, sysTenantPageQueryParams.getContactEmail())
                .eq(Objects.nonNull(sysTenantPageQueryParams.getStatus()), SysTenantEntity::getStatus, sysTenantPageQueryParams.getStatus());

        IPage<SysTenantEntity> iPage = baseMapper.selectPage(page, wrapper);
        List<SysTenantVO> list = BeanUtil.copyToList(iPage.getRecords(), SysTenantVO.class);

        return new PageResult<>(list, page.getTotal(), SysTenantVO.class);
    }

    @Override
    public List<SysTenantVO> getList(@NotNull SysTenantSelectQueryParams sysTenantSelectQueryParams) {
        QueryWrapper<SysTenantEntity> wrapper = new QueryWrapper<>();
        return BeanUtil.copyToList(baseMapper.selectList(wrapper), SysTenantVO.class);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(@NotNull SysTenantAddParams sysTenantAddParams) {
        // 校验租户名称是否重复
        validateTenantName(sysTenantAddParams.getTenantName());

        SysTenantEntity sysTenantEntity = BeanUtil.copyProperties(sysTenantAddParams, SysTenantEntity.class);
        baseMapper.insert(sysTenantEntity);

        try {
            //  切换租户上下文为新租户 A
            TenantContext.setTenantId(String.valueOf(sysTenantEntity.getSysTenantId()));
            // 注意：tenant_id 会由 MyBatis-Plus 自动填充（因为 now tenantContext = newTenantId）
            // 创建角色
            SysRoleEntity sysRoleEntity = createRole();

            // 创建角色权限对应关系
            createRoleMenu(sysTenantAddParams.getSysTenantPackageId(), sysRoleEntity.getSysRoleId());

            // 创建用户
            SysUserEntity sysUserEntity = createUser(sysTenantAddParams, sysRoleEntity.getSysRoleId());

            sysTenantEntity.setSysUserId(sysUserEntity.getSysUserId());
            // 更新租户中的userId,用于关联用户
            this.baseMapper.updateById(sysTenantEntity);

        } finally {
            TenantContext.clear();
        }


    }

    private SysRoleEntity createRole() {
        SysRoleEntity sysRoleEntity = new SysRoleEntity();
        sysRoleEntity.setRoleName("租户管理员");
        sysRoleEntity.setRoleCode("tenant_admin");
        sysRoleEntity.setDataScope(1);
        sysRoleEntity.setType(1);
        sysRoleEntity.setSort(0);
        sysRoleEntity.setStatus(1);
        sysRoleEntity.setRemark("系统自动生成");

        try {
            sysRoleService.save(sysRoleEntity);
        } catch (Exception e) {
            throw new RuntimeException("创建租户管理员角色失败", e);
        }

        return sysRoleEntity;
    }


    private void createRoleMenu(Long sysTenantPackageId, Long roleId) {
        // 根据租户套餐ID查询租户套餐关联的菜单列表
        List<Long> menuIdList = sysTenantPackageMenuService.listBySysTenantPackageId(sysTenantPackageId);

        if (!menuIdList.isEmpty()) {
            iSysRoleMenuService.save(roleId, menuIdList);
        }

    }

    private SysUserEntity createUser(SysTenantAddParams sysTenantAddParams, Long roleId) {
        SysUserEntity sysUserEntity = new SysUserEntity();
        sysUserEntity.setUsername(sysTenantAddParams.getUsername());
        sysUserEntity.setPassword(new BCryptPasswordEncoder().encode(sysTenantAddParams.getPassword()));
        sysUserEntity.setRealName(sysTenantAddParams.getContactPerson());
        sysUserEntity.setPhone(sysTenantAddParams.getContactPhone());
        sysUserEntity.setEmail(sysTenantAddParams.getContactEmail());

        sysUserService.save(sysUserEntity);

        // 创建用户角色对应关系
        SysUserRoleEntity sysUserRoleEntity = new SysUserRoleEntity();
        sysUserRoleEntity.setSysUserId(sysUserEntity.getSysUserId());
        sysUserRoleEntity.setSysRoleId(roleId);
        iSysUserRoleService.saveUserRole(sysUserRoleEntity);

        return sysUserEntity;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(@NotNull SysTenantUpdateParams sysTenantUpdateParams) {
        // 校验租户是否存在
        SysTenantEntity tenant = validateTenant(sysTenantUpdateParams.getSysTenantId());
        // 校验租户名称是否重复
        validateTenantName(sysTenantUpdateParams.getTenantName());


        SysTenantEntity sysTenantEntity = BeanUtil.copyProperties(sysTenantUpdateParams, SysTenantEntity.class);
        baseMapper.updateById(sysTenantEntity);

        try {
            //  切换租户上下文为新租户 A
            TenantContext.setTenantId(String.valueOf(sysTenantEntity.getSysTenantId()));

            // 判断套餐是否改变
            if (!tenant.getSysTenantPackageId().equals(sysTenantUpdateParams.getSysTenantPackageId())) {
                // 创建角色权限对应关系
                updateRoleMenu(tenant, sysTenantUpdateParams.getSysTenantPackageId());
            }


        } finally {
            TenantContext.clear();
        }
    }

    private void updateRoleMenu(SysTenantEntity sysTenantEntity, Long sysTenantPackageId) {
        TenantContext.setTenantId(String.valueOf(sysTenantEntity.getSysTenantId()));

        // 根据租户套餐对应的用户ID查询对应角色
        List<SysRoleEntity> sysRoleEntityList =  iSysRoleService.findByUserId(sysTenantEntity.getSysUserId());

        // 查找出租户管理员角色
        SysRoleEntity tenantAdminRole = sysRoleEntityList.stream().filter(role -> role.getRoleCode().equals("tenant_admin")).findFirst().orElse(null);

        if(tenantAdminRole == null) {
            throw new ZsException(ErrorCodeConstants.TENANT_ADMIN_ROLE_NOT_EXIST);
        }


        // 根据租户套餐ID查询租户套餐关联的菜单列表
        List<Long> menuIdList = sysTenantPackageMenuService.listBySysTenantPackageId(sysTenantPackageId);

        if (!menuIdList.isEmpty()) {
            iSysRoleMenuService.save(tenantAdminRole.getSysRoleId(), menuIdList);
        }

    }

    private void validateTenantName(String tenantName) {
        SysTenantEntity sysTenantEntity = this.baseMapper.selectOne(new LambdaQueryWrapper<SysTenantEntity>().eq(SysTenantEntity::getTenantName, tenantName));

        if (sysTenantEntity != null && !sysTenantEntity.getTenantName().equals(tenantName)) {
            throw new ZsException(ErrorCodeConstants.TENANT_NAME_EXIST);
        }

    }

    private SysTenantEntity validateTenant(Long sysTenantId) {
        SysTenantEntity tenant = this.baseMapper.selectById(sysTenantId);
        if (tenant == null) {
            throw new ZsException(ErrorCodeConstants.TENANT_NOT_EXIST);
        }
        // 内置租户，不允许删除
        if (isSystemTenant(tenant)) {
            throw new ZsException(ErrorCodeConstants.TENANT_SYSTEM_NOT_DELETE);
        }
        return tenant;
    }

    private static boolean isSystemTenant(SysTenantEntity tenant) {
        return Objects.equals(tenant.getType(), 0);
    }

    @Override
    public SysTenantVO getById(Long id) {
        SysTenantVO sysTenantVO = BeanUtil.copyProperties(baseMapper.selectById(id), SysTenantVO.class);
        return sysTenantVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Long id) {
        SysTenantEntity tenant = validateTenant(id);
        try {

            // 设置租户上下文
            TenantContext.setTenantId(String.valueOf(tenant.getSysTenantId()));

            // 查询用户对应的角色
            List<Long> roleIdList = iSysUserRoleService.queryRoleIdList(tenant.getSysUserId());


            // 删除用户和用户角色关系
            if (tenant.getSysUserId() != null) {
                sysUserService.delById(tenant.getSysUserId());
            }



            if (roleIdList != null && !roleIdList.isEmpty()) {
                // 删除角色和角色权限关系
                iSysRoleService.batchDelById(roleIdList);
            }



            // 删除租户
            baseMapper.deleteById(id);
        }finally {
            TenantContext.clear();
        }

    }

    @Override
    public void batchDelById(@NotNull Long[] ids) {
        baseMapper.deleteByIds(Arrays.asList(ids));
    }
}