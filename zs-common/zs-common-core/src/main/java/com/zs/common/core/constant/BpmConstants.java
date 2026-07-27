package com.zs.common.core.constant;

/**
 * BPM 模块常量
 */
public interface BpmConstants {

    /** 模型状态 */
    int MODEL_STATUS_DRAFT = 0;
    int MODEL_STATUS_PUBLISHED = 1;
    int MODEL_STATUS_DEPLOYED = 2;
    int MODEL_STATUS_ACTIVE = 3;
    int MODEL_STATUS_DISABLED = -1;

    /** 缓存前缀 */
    String CACHE_PREFIX = "bpm:";

    /** 默认版本号 */
    int DEFAULT_VERSION = 1;

    /** BPMN 文件后缀 */
    String BPMN_FILE_SUFFIX = ".bpmn20.xml";

    /** 流程变量名 */
    String VAR_INITIATOR = "initiator";
    String VAR_APPLY_USER_ID = "applyUserId";
    String VAR_BUSINESS_KEY = "businessKey";

    /** 审批节点扩展属性 - 审批人类型 */
    String EXT_APPROVE_SET_TYPE = "approveSetType";
    /** 审批节点扩展属性 - 审批人ID列表（逗号分隔） */
    String EXT_APPROVE_IDS = "approveIds";
    /** 审批节点扩展属性 - 审批人名称列表（逗号分隔） */
    String EXT_APPROVE_NAMES = "approveNames";
    /** 审批节点扩展属性 - 审批模式（1=会签, 2=或签, 3=依次审批） */
    String EXT_EXAMINE_MODE = "examineMode";
    /** 审批节点扩展属性 - 会签完成百分比 */
    String EXT_SIGN_PCT = "signPct";
    /** 审批节点扩展属性 - 无审批人时处理方式（0=自动通过, 1=自动拒绝, 2=转交管理员） */
    String EXT_NO_HANDLER_ACTION = "noHandlerAction";
    /** 审批节点扩展属性 - 表单字段ID（表单内的人类型时使用） */
    String EXT_FORM_FIELD_ID = "formFieldId";
    /** 审批节点扩展属性 - 是否允许发起人自选 */
    String EXT_SELF_SELECT_FLAG = "selfSelectFlag";

    /** 抄送节点字段 - 抄送人ID列表 */
    String FIELD_CC_USERS = "ccUsers";
    /** 抄送节点字段 - 部门负责人ID列表 */
    String FIELD_CC_DEPT_HEADS = "ccDeptHeads";
    /** 抄送节点字段 - 节点类型 */
    String FIELD_NODE_TYPE = "nodeType";

    /** 审批意见类型 */
    String COMMENT_TYPE_APPROVE = "approve";
    String COMMENT_TYPE_REJECT = "reject";
    String COMMENT_TYPE_TRANSFER = "transfer";
    String COMMENT_TYPE_DELEGATE = "delegate";
    String COMMENT_TYPE_CANCEL = "cancel";

    /** 挂起/终止原因 */
    String SUSPEND_REASON_ADMIN = "ADMIN_SUSPEND";
    String ACTIVATE_REASON_ADMIN = "ADMIN_ACTIVATE";
    String TERMINATE_REASON_ADMIN = "ADMIN_TERMINATE";
    String WITHDRAW_REASON_INITIATOR = "INITIATOR_WITHDRAW";
}
