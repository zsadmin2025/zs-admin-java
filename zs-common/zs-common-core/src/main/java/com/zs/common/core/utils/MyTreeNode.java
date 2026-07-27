package com.zs.common.core.utils;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MyTreeNode {

    private Long id;
    private Long pid;
    private String name;
    /**
     * 节点类型：dept-部门，post-岗位
     */
    private String type;
    private List<MyTreeNode> children = new ArrayList<>();

    public MyTreeNode(Long sysDeptId, Long pid, String deptName) {
        this.id = sysDeptId;
        this.pid = pid;
        this.name = deptName;
        this.type = "dept";
    }

    public MyTreeNode(Long id, Long pid, String name, String type) {
        this.id = id;
        this.pid = pid;
        this.name = name;
        this.type = type;
    }
}
