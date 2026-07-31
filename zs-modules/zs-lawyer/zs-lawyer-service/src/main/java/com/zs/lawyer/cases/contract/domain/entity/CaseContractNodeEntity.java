package com.zs.lawyer.cases.contract.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 案件合同节点
 * </p>
 *
 * @author zs
 * @since 2025-06-08 18:03:56
 */
@Getter
@Setter
@TableName("case_contract_node")
@Schema(description = "案件合同节点Entity对象")
public class CaseContractNodeEntity extends BaseEntity {

    /**  表id */
    @TableId
    private Long caseContractNodeId;

    /**  案件id */
    private Long caseInfoId;

    /**  关联合同表id */
    private Long caseContractId;

    /**  款项类别(字典paymentCategory) */
    private String paymentCategory;

    /**  款项名称 */
    private String paymentName;

    /**  应收金额 */
    private BigDecimal receivableAmount;

    /**  预计收款时间 */
    private Date expectedCollectionDate;

    /**  收款条件 */
    private String paymentTerms;




}
