package com.zs.bpm.form.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 动态表单定义实体
 *
 * @author zsadmin
 */
@Data
@TableName("bpm_form_definition")
@EqualsAndHashCode(callSuper = false)
public class BpmFormDefinitionEntity extends BaseEntity {

    @TableId
    private Long id;
    private String formName;
    private String description;
    private Integer status;

    private String formKey;
    // 表单字段配置
    private String formRule;
    //表单全局配置
    private String formOption;
}
