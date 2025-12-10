package com.zs.common.core.utils;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MyTreeNode {

    private Long id;
    private Long pid;
    private String name;
    private List<MyTreeNode> children = new ArrayList<>();

    public MyTreeNode(Long sysDeptId, Long pid, String deptName) {
        this.id = sysDeptId;
        this.pid = pid;
        this.name = deptName;
    }
}
