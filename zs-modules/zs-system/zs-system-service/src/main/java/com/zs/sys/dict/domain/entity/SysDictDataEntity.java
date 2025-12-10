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
@TableName(value = "sys_dict_data")
public class SysDictDataEntity extends BaseEntity {

    @TableId
    private Long sysDictDataId;
    private Long sysDictTypeId;
    private Long pid;
    private String dictType;
    private String dictLabel;
    private String dictValue;
    private Integer sort;
    private Integer status;

}
