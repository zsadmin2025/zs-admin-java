package com.zs.gen.util;

import com.zs.gen.domain.model.TreeNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeNodeUtils {


    public static List<TreeNode> buildAntdTree(Map<String, String> fileMap, String businessName) {
        TreeNode root = new TreeNode("root", "", ""); // 根节点无值

        int keyCounter = 0;
        Map<String, TreeNode> pathToNode = new HashMap<>();
        pathToNode.put("", root);

        for (String filename : fileMap.keySet()) {
            String fullPath;
            if (filename.endsWith(".java")) {
                fullPath = getJavaPath(filename, businessName);
            } else if (filename.endsWith(".xml")) {
                fullPath = "java/mapper/xml/" + filename;
            } else if (filename.endsWith(".sql")) {
                fullPath = "java/sql/" + filename;
            } else if (filename.endsWith(".vue")) {
                fullPath = "vue/views/" + businessName +"/" + filename;
            } else if (filename.endsWith(".ts")) {
                fullPath = getTsPath(filename, businessName);
            } else {
                continue;
            }

            String[] parts = fullPath.split("/");
            TreeNode current = root;

            for (int i = 0; i < parts.length; i++) {
                String part = parts[i];
                String parentPathStr = i == 0 ? "" : String.join("/", Arrays.copyOf(parts, i));
                String currentPathStr = String.join("/", Arrays.copyOf(parts, i + 1));

                boolean isLast = i == parts.length - 1; // 是否是文件本身（叶子节点）

                if (!pathToNode.containsKey(currentPathStr)) {
                    String key = parentPathStr.isEmpty()
                            ? String.valueOf(keyCounter++)
                            : pathToNode.get(parentPathStr).getKey() + "-" + pathToNode.get(parentPathStr).getChildren().size();

                    // ⭐ 关键修改：只有最后一层（文件）才赋予 fileMap 中的值
                    String value = isLast ? fileMap.get(filename) : "";

                    TreeNode node = new TreeNode(part, key, value);
                    current.getChildren().add(node);
                    pathToNode.put(currentPathStr, node);
                    current = node;
                } else {
                    current = pathToNode.get(currentPathStr);
                }
            }
        }

        return root.getChildren();
    }

    private static String getJavaPath(String filename, String businessName) {
        if (filename.contains("Controller")) {
            return "java/controller/" + filename;
        } else if (filename.contains("ServiceImpl")) {
            return "java/service/impl/" + filename;
        } else if (filename.contains("Service")) {
            return "java/service/" + filename;
        } else if (filename.contains("Mapper") && !filename.endsWith("Mapper.xml")) {
            return "java/mapper/" + filename;
        } else if (filename.contains("Entity")) {
            return "java/domain/entity/" + filename;
        } else if (filename.contains("VO")) {
            return "java/domain/vo/" + filename;
        } else if (filename.contains("Params")) {
            return "java/domain/params/" + filename;
        } else if (filename.contains("Excel")) {
            return "java/domain/excel/" + filename;
        } else {
            return "java/other/"  + businessName + "/" + filename;
        }
    }

    private static String getTsPath(String filename, String businessName) {
        if (filename.equals("api.ts")) {
            return "vue/api/" + filename;
        } else if (filename.contains("Store")) {
            return "vue/store/"  + businessName + "/" + filename;
        } else if (filename.endsWith("Types.ts")) {
            return "vue/types/" + businessName + "/" + filename;
        } else {
            return "vue/api/"  + businessName + "/" + filename;
        }
    }
}
