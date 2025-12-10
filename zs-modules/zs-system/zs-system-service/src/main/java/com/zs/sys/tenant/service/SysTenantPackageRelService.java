package com.zs.sys.tenant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.sys.tenant.domain.entity.SysTenantPackageRelEntity;
import com.zs.sys.tenant.domain.params.SysTenantPackageRelAddParams;
import com.zs.sys.tenant.domain.params.SysTenantPackageRelPageQueryParams;
import com.zs.sys.tenant.domain.params.SysTenantPackageRelSelectQueryParams;
import com.zs.sys.tenant.domain.params.SysTenantPackageRelUpdateParams;
import com.zs.sys.tenant.domain.vo.SysTenantPackageRelVO;

import java.util.List;

/**
 * <p>
 * 租户套餐关联表 服务类
 * </p>
 *
 * @author zs
 * @since 2025-08-07 23:42:43
 */
public interface SysTenantPackageRelService extends IService<SysTenantPackageRelEntity> {

    /** 分页 **/
    PageResult<SysTenantPackageRelVO> page(SysTenantPackageRelPageQueryParams sysTenantPackageRelPageQueryParams);

    /** 列表 **/
    List<SysTenantPackageRelVO> getList(SysTenantPackageRelSelectQueryParams sysTenantPackageRelSelectQueryParams);

    /** 新增 **/
    void save(SysTenantPackageRelAddParams sysTenantPackageRelAddParams);

    /** 更新 **/
    void update(SysTenantPackageRelUpdateParams sysTenantPackageRelUpdateParams);

    /** 根据id查询 **/
    SysTenantPackageRelVO getById(Long id);

    /** 单个删除 **/
    void deleteById(Long sysTenantPackageRelId);

    /** 批量删除 **/
    void batchDelById(Long[] sysTenantPackageRelIds);
}