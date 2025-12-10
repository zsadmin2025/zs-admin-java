package com.zs.sys.tenant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.sys.tenant.domain.entity.SysTenantEntity;
import com.zs.sys.tenant.domain.params.SysTenantAddParams;
import com.zs.sys.tenant.domain.params.SysTenantPageQueryParams;
import com.zs.sys.tenant.domain.params.SysTenantSelectQueryParams;
import com.zs.sys.tenant.domain.params.SysTenantUpdateParams;
import com.zs.sys.tenant.domain.vo.SysTenantVO;

import java.util.List;

/**
 * <p>
 * 租户信息表 服务类
 * </p>
 *
 * @author zs
 * @since 2025-08-07 23:42:45
 */
public interface SysTenantService extends IService<SysTenantEntity> {

    /** 分页 **/
    PageResult<SysTenantVO> page(SysTenantPageQueryParams sysTenantPageQueryParams);

    /** 列表 **/
    List<SysTenantVO> getList(SysTenantSelectQueryParams sysTenantSelectQueryParams);

    /** 新增 **/
    void save(SysTenantAddParams sysTenantAddParams);

    /** 更新 **/
    void update(SysTenantUpdateParams sysTenantUpdateParams);

    /** 根据id查询 **/
    SysTenantVO getById(Long id);

    /** 单个删除 **/
    void deleteById(Long sysTenantId);

    /** 批量删除 **/
    void batchDelById(Long[] sysTenantIds);
}