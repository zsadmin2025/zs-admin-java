package com.zs.bpm.model.domain.dto;

import lombok.Data;

import java.util.List;
/**
 * 节点配置信息(递归节点核心)
 */
@Data
public class NodeConfigDTO {

    private String nodeName;
    private Integer type;          // 0 发起人 1 审批人 2 抄送人 4 路由
    private NodeConfigDTO childNode;

    // 路由节点专属(type=4)
    private List<ConditionNodeDTO> conditionNodes;
    private Integer priorityLevel;

    // 审批/抄送节点专属(type=1/2)
    private Integer settype;       // 人员来源类型
    private Integer examineMode;   // 1依次审批 2会签 3或签
    private Integer signPct;       // 会签通过率 0-100
    private Integer noHanderAction;// 空兜底 1自动通过 2自动拒绝 3指定备用 4转交管理员
    private String backupUsers;    // 备用人员ID逗号分隔

    private List<String> candidateParam;
    private List<ButtonSettingDTO> buttonsSetting;
    private List<FieldPermissionDTO> fieldsPermission;
}
