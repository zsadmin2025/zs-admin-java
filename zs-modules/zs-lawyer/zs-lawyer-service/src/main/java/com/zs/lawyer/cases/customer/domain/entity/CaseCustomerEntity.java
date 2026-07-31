package com.zs.lawyer.cases.customer.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 案件客户表
 * </p>
 *
 * @author zs
 * @since 2025-06-08 17:55:28
 */
@Getter
@Setter
@TableName("case_customer")
@Schema(description = "案件客户信息Entity对象")
public class CaseCustomerEntity extends BaseEntity {

    /**  表id */
    @TableId
    private Long caseCustomerId;

    /**  案件id */
    private Long caseInfoId;

    /**  客户id */
    private Long customerId;

    /**  客户名称 */
    private String customerName;

    /**  客户编号 */
    private String customerCode;

    /**  客户状态 */
    private String customerType;




}
