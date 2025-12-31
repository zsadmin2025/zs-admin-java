package com.zs.gen.util;

import cn.hutool.core.util.StrUtil;
import com.zs.gen.config.GenConfigProperties;
import com.zs.gen.constants.GenConstants;
import com.zs.gen.domain.entity.GenTable;
import com.zs.gen.domain.entity.GenTableColumn;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * 代码生成器工具类
 * 提供代码生成过程中的各种辅助方法，包括表信息初始化、列属性处理、类型转换等
 *
 * @author zs
 */
public final class GenUtils {


    /**
     * 初始化表信息
     * @param genTable 表信息实体
     * @param genConfigProperties 生成配置属性
     */
    public static void initTable(GenTable genTable, GenConfigProperties genConfigProperties) {
        // 设置类名：首字母大写的驼峰命名
        genTable.setClassName(StrUtil.upperFirst(StrUtil.toCamelCase(genTable.getClassName())));
        // 设置包名
        genTable.setPackageName(genConfigProperties.getPackageName());
        // 设置模块名
        genTable.setModuleName(genConfigProperties.getModuleName());
        // 设置业务名
        genTable.setBusinessName(StrUtil.subAfter(genTable.getTableName(), "_", true));
        // 设置功能名称
        genTable.setFunctionName(genTable.getTableComment());
        // 设置作者
        genTable.setFunctionAuthor(genConfigProperties.getAuthor());
    }


    /**
     * 初始化列属性字段
     *
     * @param column 列信息实体
     * @param table  表信息实体
     */
    public static void initColumnField(GenTableColumn column, GenTable table) {

        // 获取列名和列类型
        String columnName = column.getColumnName();
        String columnType = column.getColumnType();

        // 1. 设置基础信息
        column.setTableId(table.getTableId());
        column.setColumnName(columnName);
        column.setColumnComment(column.getColumnComment());
        column.setColumnType(columnType);
        column.setJavaType(GenConstants.TYPE_STRING); // 默认字符串类型
        column.setJavaField(StrUtil.toCamelCase(columnName.toLowerCase()));

        // 2. 设置默认值
        column.setJavaType(GenConstants.TYPE_STRING);
        column.setQueryType(GenConstants.QUERY_EQ);
        column.setHtmlType(GenConstants.HTML_INPUT);

        // 忽略主键字段
        if (!Arrays.asList(GenConstants.COLUMN_NAME_NOT_ADD).contains(columnName) && !column.getIsPk().equals(GenConstants.IS_PK)){
            column.setIsInsert(GenConstants.REQUIRE);
        }
        
        if (!Arrays.asList(GenConstants.COLUMN_NAME_NOT_EDIT).contains(columnName)){
            column.setIsEdit(GenConstants.REQUIRE);
        }
        if (!Arrays.asList(GenConstants.COLUMN_NAME_NOT_LIST).contains(columnName)){
            column.setIsList(GenConstants.REQUIRE);
        }
        if (!Arrays.asList(GenConstants.COLUMN_NAME_NOT_QUERY).contains(columnName)){
            column.setIsQuery(GenConstants.REQUIRE);
        }
   

        // 3. 解析数据库类型，设置Java类型和HTML类型
        String dbType = columnType;
        if (dbType.contains("(")) {
            dbType = StringUtils.substringBefore(dbType, "(");
        }
        String lowerDbType = dbType.toLowerCase();

        // 字符串类型
        if (Arrays.asList(GenConstants.COLUMN_TYPE_ALL_STR).contains(lowerDbType)) {
            column.setJavaType(GenConstants.TYPE_STRING);
            // 长文本使用文本域
            if (Arrays.asList(GenConstants.COLUMN_TYPE_ALL_LONG_TEXT).contains(lowerDbType)) {
                column.setHtmlType(GenConstants.HTML_TEXTAREA);
            }
        }
        // 整数类型
        else if (Arrays.asList(GenConstants.COLUMN_TYPE_ALL_INTEGER).contains(lowerDbType)) {
            column.setJavaType(GenConstants.TYPE_INTEGER);
        }
        // 长整数类型
        else if (Arrays.asList(GenConstants.COLUMN_TYPE_ALL_LONG).contains(lowerDbType)) {
            column.setJavaType(GenConstants.TYPE_LONG);
        }
        // 浮点类型
        else if (Arrays.asList(GenConstants.COLUMN_TYPE_ALL_DECIMAL).contains(lowerDbType)) {
            column.setJavaType(GenConstants.TYPE_BIG_DECIMAL);
        }
        // 日期时间类型
        else if (Arrays.asList(GenConstants.COLUMN_TYPE_ALL_DATETIME).contains(lowerDbType)) {
            column.setJavaType(GenConstants.TYPE_DATE);
            column.setHtmlType(GenConstants.HTML_DATETIME);
        }
        // 布尔类型
        else if (Arrays.asList(GenConstants.COLUMN_TYPE_ALL_BOOLEAN).contains(lowerDbType)) {
            column.setJavaType(GenConstants.TYPE_BOOLEAN);
            column.setHtmlType(GenConstants.HTML_RADIO);
        }
    }



}
