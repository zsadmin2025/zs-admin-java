package com.zs.common.core.model.domain;


import lombok.Data;

/**
 * @author zsadmin
 */
@Data
public class SysDeptDTO {

    private Long sysDeptId;
    private Long pid;
    private String pids;
    private String deptName;
    private Long sysUserId;
    private Integer status;
    private String remark;
    private Integer sort;
    private String deptHeadName;
}
