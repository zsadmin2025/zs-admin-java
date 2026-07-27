package com.zs.sys.demo.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 代码生成测试表
 * </p>
 *
 * @author zs
 * @date 2026-01-07 11:01:19
 */
@Getter
@Setter
@TableName("sys_demo")
@Schema(description = "代码生成测试表Entity对象")
public class SysDemoEntity extends BaseEntity {

    /**  主键ID */
    @TableId
    private Long sysDemoId;

    /**  文本框测试字段 */
    private String inputField;

    /**  文本域测试字段 */
    private String textareaField;

    /**  数字框测试字段 */
    private BigDecimal numberField;

    /**  下拉框测试字段 */
    private String selectField;

    /**  单选框测试字段 */
    private String radioField;

    /**  复选框测试字段 */
    private String checkboxField;

    /**  日期控件测试字段 */
    private Date dateField;

    /**  日期时间控件测试字段 */
    private Date datetimeField;

    /**  时间控件测试字段 */
    private Date timeField;

    /**  图片上传测试字段 */
    private String imageField;

    /**  文件上传测试字段 */
    private String uploadField;

    /**  富文本测试字段 */
    private String editorField;

    /**  是否删除 */
    private Integer isDelete;

    /**  状态 */
    private Integer status;

}
