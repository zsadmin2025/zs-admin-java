package com.zs.sys.tenant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.sys.tenant.domain.entity.SysTenantPackageEntity;
import com.zs.sys.tenant.domain.params.SysTenantPackageAddParams;
import com.zs.sys.tenant.domain.params.SysTenantPackagePageQueryParams;
import com.zs.sys.tenant.domain.params.SysTenantPackageSelectQueryParams;
import com.zs.sys.tenant.domain.params.SysTenantPackageUpdateParams;
import com.zs.sys.tenant.domain.vo.SysTenantPackageVO;

import java.util.List;

/**
 * <p>
 * 租户套餐表 服务类
 * </p>
 *
 * @author zs
 * @since 2025-08-07 23:42:40
 */
public interface SysTenantPackageService extends IService<SysTenantPackageEntity> {

    /** 分页 **/
    PageResult<SysTenantPackageVO> page(SysTenantPackagePageQueryParams sysTenantPackagePageQueryParams);

    /** 列表 **/
    List<SysTenantPackageVO> getList(SysTenantPackageSelectQueryParams sysTenantPackageSelectQueryParams);

    /** 新增 **/
    void save(SysTenantPackageAddParams sysTenantPackageAddParams);

    /** 更新 **/
    void update(SysTenantPackageUpdateParams sysTenantPackageUpdateParams);

    /** 根据id查询 **/
    SysTenantPackageVO getById(Long id);

    /** 单个删除 **/
    void deleteById(Long sysTenantPackageId);

    /** 批量删除 **/
    void batchDelById(Long[] sysTenantPackageIds);
}