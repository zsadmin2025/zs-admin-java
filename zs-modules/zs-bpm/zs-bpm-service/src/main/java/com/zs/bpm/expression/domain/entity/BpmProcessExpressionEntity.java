package com.zs.bpm.expression.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 流程表达式实体
 *
 * @author zsadmin
 */
@Data
@TableName("bpm_process_expression")
@EqualsAndHashCode(callSuper = false)
public class BpmProcessExpressionEntity extends BaseEntity {

    @TableId
    private Long id;
    private String name;
    private String code;
    private String expression;
    private String returnType;
    private String description;
}
