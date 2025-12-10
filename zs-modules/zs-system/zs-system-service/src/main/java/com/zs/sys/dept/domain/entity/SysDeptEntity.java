package com.zs.sys.dept.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author zsadmin
 */
@Data
@TableName("sys_dept")
@EqualsAndHashCode(callSuper = true)
public class SysDeptEntity extends BaseEntity {

    @TableId
    private Long sysDeptId;
    private Long pid;
    private String pids;
    private String deptName;
    private Long deptHeadId;
    private String deptHeadName;
    private Integer status;
    private String remark;
    private Integer sort;

}
