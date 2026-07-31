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
 * 案件合同
 * </p>
 *
 * @author zs
 * @since 2025-06-08 18:02:46
 */
@Getter
@Setter
@TableName("case_contract")
@Schema(description = "案件合同Entity对象")
public class CaseContractEntity extends BaseEntity {

    /**  案件合同表id */
    @TableId
    private Long caseContractId;

    /**  案件表id */
    private Long caseInfoId;

    /**  开始日期 */
    private Date startDate;

    /**  结束日期 */
    private Date endDate;

    /**  合同金额 */
    private BigDecimal contractAmount;

    /**  付款方式 */
    private String paymentMethod;

    /**  付款方式明细 */
    private String paymentMethodDetails;




}
