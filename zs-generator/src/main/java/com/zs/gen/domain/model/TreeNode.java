package com.zs.gen.domain.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// 定义树节点，用于构建目录结构
@Data
public class TreeNode {

    String title;
    String key;
    String value;
    List<TreeNode> children;

    public TreeNode(String title, String key, String value) {
        this.title = title;
        this.key = key;
        this.value = value;
        this.children = new ArrayList<>();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("title", title);
        map.put("key", key);
        if (value != null && !value.isEmpty()) {
            map.put("value", value); // 只在有值时添加
        }
        if (!children.isEmpty()) {
            List<Map<String, Object>> childMaps = new ArrayList<>();
            for (TreeNode child : children) {
                childMaps.add(child.toMap());
            }
            map.put("children", childMaps);
        }
        return map;
    }
}
