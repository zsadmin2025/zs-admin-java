package com.zs.bpm.task.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class ProcessDefinitionInfo {

    @TableId
    private Long id;

    /** Flowable ACT_RE_PROCDEF.ID_（部署后生成） */
    private String processDefinitionId;

    /** Flowable ACT_DEPLOYMENT.ID_（部署ID） */
    private String deploymentId;

    /** Flowable ACT_RE_MODEL.ID_（模型ID） */
    private String modelId;

    /** 流程定义Key */
    private String processKey;

    /** 流程定义名称 */
    private String processName;

    /** 流程分类ID */
    private Long categoryId;

    /** 图标 */
    private String icon;

    /** 描述 */
    private String description;

    /** 版本号 */
    private Integer version;

    /** 表单ID */
    private Long formId;

    /** 表单类型 1-动态表单 2-业务表单 */
    private String formType;

    /** 表单规则 */
    private String formRule;

    /** 表单选项 */
    private String formOption;

    /** BPMN模型JSON */
    private String modelJson;

    /** BPMN 2.0 XML（部署到引擎用） */
    private String bpmnXml;

    /** 状态: 0=禁用,1=已启用 */
    private Integer status;

    /** 发布时间 */
    private Date publishTime;

    /** 非数据库字段：分类名称 */
    @TableField(exist = false)
    private String categoryName;
}
