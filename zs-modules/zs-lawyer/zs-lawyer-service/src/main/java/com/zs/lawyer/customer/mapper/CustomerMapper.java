package com.zs.lawyer.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zs.lawyer.customer.domain.entity.CustomerEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 客户表 Mapper 接口
 * </p>
 *
 * @author zs
 * @since 2025-05-29 21:55:59
 */
@Mapper
public interface CustomerMapper extends BaseMapper<CustomerEntity> {

    String selectMaxCustomerCode(String dateStr);
}
