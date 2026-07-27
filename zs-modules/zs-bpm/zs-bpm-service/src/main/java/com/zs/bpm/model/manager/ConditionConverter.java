package com.zs.bpm.model.manager;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.zs.bpm.model.domain.dto.ConditionConfigDTO;
import com.zs.bpm.model.domain.dto.ConditionItemDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 对应XML双条件存储：
 * 1. convertToEl → conditionExpression（引擎执行）
 * 2. convertToJson → conditionJson（设计器回显）
 */
@Component
public class ConditionConverter {

    private static final Map<String, String> OPERATOR_MAP = Map.of(
            ">", " > ",
            "<", " < ",
            "==", " == ",
            "!=", " != ",
            "contains", ".contains("
    );

    /**
     * 条件配置 → Flowable EL 执行表达式
     * <p>
     * 支持两种方式：
     * 1. 直接使用原始EL表达式（config.expression 非空时）
     * 2. 通过 conditionList 构建（默认）
     */
    public String convertToEl(ConditionConfigDTO config) {
        if (config == null) {
            return null;
        }

        // 方式1：直接使用原始EL表达式
        if (StrUtil.isNotBlank(config.getExpression())) {
            String expr = config.getExpression().trim();
            return expr.startsWith("${") ? expr : "${" + expr + "}";
        }

        // 方式2：从conditionList构建
        if (config.getConditions() == null || config.getConditions().isEmpty()) {
            return null;
        }
        List<String> expressions = config.getConditions().stream()
                .map(this::buildSingleExpression)
                .toList();
        String logic = "AND".equals(config.getLogic()) ? " && " : " || ";
        return "${" + String.join(logic, expressions) + "}";
    }

    /**
     * 条件配置 → JSON字符串（用于设计器回显）
     */
    public String convertToJson(ConditionConfigDTO config) {

        return JSONUtil.toJsonStr(config);
    }

    private String buildSingleExpression(ConditionItemDTO item) {
        String field = item.getColumnId();
        String opt = item.getOpt1();
        String value = item.getZdy1();
        if ("contains".equals(opt)) {
            return String.format("%s.contains('%s')", field, value);
        }
        // 数字类型不加引号
        if ("Number".equals(item.getColumnType())) {
            return String.format("%s%s%s", field, OPERATOR_MAP.getOrDefault(opt, " == "), value);
        }
        return String.format("%s%s'%s'", field, OPERATOR_MAP.getOrDefault(opt, " == "), value);
    }
}
