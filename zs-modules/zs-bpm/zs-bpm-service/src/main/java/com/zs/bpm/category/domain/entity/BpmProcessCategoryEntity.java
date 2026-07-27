package com.zs.bpm.category.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 流程分类实体
 *
 * @author zsadmin
 */
@Data
@TableName("bpm_process_category")
@EqualsAndHashCode(callSuper = false)
public class BpmProcessCategoryEntity extends BaseEntity {

    @TableId
    private Long id;
    private String name;
    private String code;
    private String icon;
    private Integer sort;
    private Integer status;
}
