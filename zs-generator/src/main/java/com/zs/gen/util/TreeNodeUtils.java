package com.zs.gen.util;

import com.zs.gen.domain.model.TreeNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeNodeUtils {


    public static List<TreeNode> buildAntdTree(Map<String, String> fileMap, String businessName, String packageName, String moduleName) {
        TreeNode root = new TreeNode("root", "", ""); // 根节点无值

        int keyCounter = 0;
        Map<String, TreeNode> pathToNode = new HashMap<>();
        pathToNode.put("", root);

        // 将包名转换为路径格式，并设置默认值
        String packagePath = (packageName != null && !packageName.isEmpty()) ? packageName.replace(".", "/") : "com/zs";
        // 设置默认模块名
        String modulePath = (moduleName != null && !moduleName.isEmpty()) ? moduleName : "sys";

        // 遍历文件映射，获取实际文件名和内容
        for (Map.Entry<String, String> entry : fileMap.entrySet()) {
            String actualFilename = entry.getKey();
            String fileContent = entry.getValue();
            
            String fullPath;
            if (actualFilename.endsWith(".java")) {
                // 后端：java/com/zs/模块名/业务名/具体目录/文件名
                // 例如：java/com/zs/sys/demo/controller/DemoController.java
                String subPath = getJavaSubPath(actualFilename);
                // subPath 是 controller/DemoController.java，不需要再拆分
                fullPath = "java/" + packagePath + "/" + modulePath + "/" + businessName + "/" + subPath;
            } else if (actualFilename.endsWith(".xml")) {
                // 后端：java/com/zs/模块名/业务名/mapper/xml/文件名
                fullPath = "java/" + packagePath + "/" + modulePath + "/" + businessName + "/mapper/xml/" + actualFilename;
            } else if (actualFilename.endsWith(".sql")) {
                // 后端：java/com/zs/模块名/业务名/sql/文件名
                fullPath = "java/" + packagePath + "/" + modulePath + "/" + businessName + "/sql/" + actualFilename;
            } else if (actualFilename.endsWith(".vue")) {
                // 前端：vue/业务名/views/文件名
                fullPath = "vue/" + businessName + "/views/" + actualFilename;
            } else if (actualFilename.endsWith(".ts")) {
                // 前端：vue/业务名/具体目录/文件名
                fullPath = "vue/" + businessName + "/" + getTsSubPath(actualFilename, businessName);
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

                    // 只有最后一层（文件）才赋予 fileMap 中的值
                    String value = isLast ? fileContent : "";

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

    // 获取Java文件的子路径，例如controller/FileNameController.java
    private static String getJavaSubPath(String filename) {
        if (filename.contains("Controller")) {
            return "controller/" + filename;
        } else if (filename.contains("ServiceImpl")) {
            return "service/impl/" + filename;
        } else if (filename.contains("Service")) {
            return "service/" + filename;
        } else if (filename.contains("Mapper") && !filename.endsWith("Mapper.xml")) {
            return "mapper/" + filename;
        } else if (filename.contains("Entity")) {
            return "domain/entity/" + filename;
        } else if (filename.contains("VO")) {
            return "domain/vo/" + filename;
        } else if (filename.contains("Params")) {
            return "domain/params/" + filename;
        } else if (filename.contains("Excel")) {
            return "domain/excel/" + filename;
        } else {
            return "other/" + filename;
        }
    }
    
    // 获取TS文件的子路径，例如api/filename.ts或store/demo/filename.ts
    private static String getTsSubPath(String filename, String businessName) {
        if (filename.contains("Store")) {
            return "store/" + businessName + "/" + filename;
        } else if (filename.endsWith("Types.ts")) {
            return "types/" + businessName + "/" + filename;
        } else {
            return "api/" + filename;
        }
    }

    // 保留原有方法，避免编译错误
    private static String getJavaPath(String filename, String packagePath, String moduleName, String businessName) {
        // 使用默认模块名
        String modulePath = (moduleName != null && !moduleName.isEmpty()) ? moduleName : "sys";
        String basePath = "java/" + packagePath + "/" + modulePath + "/" + businessName + "/";
        
        return basePath + getJavaSubPath(filename);
    }

    // 保留原有方法，避免编译错误
    private static String getTsPath(String filename, String businessName) {
        return "vue/" + getTsSubPath(filename, businessName);
    }
}
