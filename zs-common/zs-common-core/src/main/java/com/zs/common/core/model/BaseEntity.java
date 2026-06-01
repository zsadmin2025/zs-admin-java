package com.zs.common.core.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zsadmin
 */
@Data
public class BaseEntity implements Serializable {


    @TableLogic(value = "0", delval = "1")
    private int deleted;

    @TableField(fill = FieldFill.INSERT)
    private Long creator;

    @TableField(exist = false)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private String createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updater;

    @TableField(exist = false)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateTime;

    @TableField(exist = false)
    private Long creatorDept;

    @TableField(exist = false)
    private Long tenantId;




}
