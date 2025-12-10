package com.zs.sys.tenant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.sys.tenant.domain.entity.SysTenantUserEntity;
import com.zs.sys.tenant.domain.params.SysTenantUserAddParams;
import com.zs.sys.tenant.domain.params.SysTenantUserPageQueryParams;
import com.zs.sys.tenant.domain.params.SysTenantUserSelectQueryParams;
import com.zs.sys.tenant.domain.params.SysTenantUserUpdateParams;
import com.zs.sys.tenant.domain.vo.SysTenantUserVO;

import java.util.List;

/**
 * <p>
 * 租户用户关联表 服务类
 * </p>
 *
 * @author zs
 * @since 2025-08-07 23:42:38
 */
public interface SysTenantUserService extends IService<SysTenantUserEntity> {

    /** 分页 **/
    PageResult<SysTenantUserVO> page(SysTenantUserPageQueryParams sysTenantUserPageQueryParams);

    /** 列表 **/
    List<SysTenantUserVO> getList(SysTenantUserSelectQueryParams sysTenantUserSelectQueryParams);

    /** 新增 **/
    void save(SysTenantUserAddParams sysTenantUserAddParams);

    /** 更新 **/
    void update(SysTenantUserUpdateParams sysTenantUserUpdateParams);

    /** 根据id查询 **/
    SysTenantUserVO getById(Long id);

    /** 单个删除 **/
    void deleteById(Long sysTenantUserId);

    /** 批量删除 **/
    void batchDelById(Long[] sysTenantUserIds);
}