package com.zs.bpm.cc.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 抄送记录实体
 *
 * @author zsadmin
 */
@Data
@TableName("bpm_cc_record")
@EqualsAndHashCode(callSuper = false)
public class BpmCcRecordEntity extends BaseEntity {

    @TableId
    private Long id;
    private String processInstanceId;
    private String taskId;
    private Long userId;
    private String title;
    private Integer isRead;
    private String readTime;

    /** 抄送发起人ID（谁抄送的：手动=操作人，自动=流程发起人） */
    private Long ccSenderId;

    /** 抄送类型：1=流程自动(BPMN ServiceTask)，2=手动(API) */
    private Integer ccType;
}
