package com.zs.gen.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 代码生成业务字段表
 * 用于存储代码生成过程中的字段配置信息
 *
 * @author zs
 */
@TableName("gen_table_column")
@EqualsAndHashCode(callSuper = true)
@Data
public class GenTableColumn extends BaseEntity {

    /**
     * 字段主键
     */
    @TableId
    private Long columnId;

    /**
     * 归属表主键
     */
    private Long tableId;

    /**
     * 列名称
     */
    @NotBlank(message = "列名称不能为空")
    @Size(max = 50, message = "列名称长度不能超过50个字符")
    private String columnName;

    /**
     * 列描述
     */
    @Size(max = 200, message = "列描述长度不能超过200个字符")
    private String columnComment;

    /**
     * 列类型（数据库类型）
     */
    @NotBlank(message = "列类型不能为空")
    @Size(max = 50, message = "列类型长度不能超过50个字符")
    private String columnType;

    /**
     * Java类型
     */
    @NotBlank(message = "Java类型不能为空")
    @Size(max = 50, message = "Java类型长度不能超过50个字符")
    private String javaType;

    /**
     * Java字段名
     */
    @NotBlank(message = "Java属性不能为空")
    @Size(max = 50, message = "Java属性长度不能超过50个字符")
    private String javaField;

    /**
     * 是否主键（Y是 N否）
     */
    private String isPk;

    /**
     * 是否自增（Y是 N否）
     */
    private String isIncrement;

    /**
     * 是否必填（Y是 N否）
     */
    private String isRequired;

    /**
     * 是否为插入字段（Y是 N否）
     */
    private String isInsert;

    /**
     * 是否编辑字段（Y是 N否）
     */
    private String isEdit;

    /**
     * 是否列表字段（Y是 N否）
     */
    private String isList;

    /**
     * 是否查询字段（Y是 N否）
     */
    private String isQuery;

    /**
     * 查询方式（EQ等于、NE不等于、GT大于、LT小于、LIKE模糊、BETWEEN范围）
     */
    private String queryType;

    /**
     * 显示类型（input文本框、textarea文本域、select下拉框、checkbox复选框、radio单选框、datetime日期控件、image图片上传控件、upload文件上传控件、editor富文本控件）
     */
    private String htmlType;

    /**
     * 字典类型
     */
    @Size(max = 50, message = "字典类型长度不能超过50个字符")
    private String dictType;

    /**
     * 排序
     */
    private Integer sort;

}
