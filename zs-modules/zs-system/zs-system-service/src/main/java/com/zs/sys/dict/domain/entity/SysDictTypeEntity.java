package com.zs.sys.dict.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author zsadmin
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "sys_dict_type")
public class SysDictTypeEntity extends BaseEntity {


    @TableId
    private Long sysDictTypeId;
    private String dictType;
    private String dictName;
    private Integer sort;
    private Integer status;


}