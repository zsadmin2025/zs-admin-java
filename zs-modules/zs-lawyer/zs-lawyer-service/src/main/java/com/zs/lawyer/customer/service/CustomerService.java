package com.zs.lawyer.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.customer.domain.entity.CustomerEntity;
import com.zs.lawyer.customer.domain.params.CustomerAddParams;
import com.zs.lawyer.customer.domain.params.CustomerPageQueryParams;
import com.zs.lawyer.customer.domain.params.CustomerSelectQueryParams;
import com.zs.lawyer.customer.domain.params.CustomerUpdateParams;
import com.zs.lawyer.customer.domain.vo.CustomerVO;

import java.util.List;

/**
 * <p>
 * 客户表 服务类
 * </p>
 *
 * @author zs
 * @since 2025-05-29 21:55:59
 */
public interface CustomerService extends IService<CustomerEntity> {

    /** 分页 **/
    PageResult<CustomerVO> page(CustomerPageQueryParams customerPageQueryParams);

    /** 列表 **/
    List<CustomerVO> getList(CustomerSelectQueryParams customerSelectQueryParams);

    /** 新增 **/
    void save(CustomerAddParams customerAddParams);

    /** 更新 **/
    void update(CustomerUpdateParams customerUpdateParams);

    /** 根据id查询 **/
    CustomerVO getById(Long id);

    /** 单个删除 **/
    void deleteById(Long customerId);

    /** 批量删除 **/
    void batchDelById(Long[] customerIds);
}