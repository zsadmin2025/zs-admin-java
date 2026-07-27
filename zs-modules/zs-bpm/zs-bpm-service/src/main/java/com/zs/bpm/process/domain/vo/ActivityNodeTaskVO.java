package com.zs.bpm.process.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 活动节点中的任务 VO
 *
 * @author zsadmin
 */
@Data
@Schema(description = "活动节点中的任务")
public class ActivityNodeTaskVO implements Serializable {

    @Schema(description = "任务ID")
    private String id;

    @Schema(description = "任务所属人")
    private AssigneeUserVO ownerUser;

    @Schema(description = "任务处理人")
    private AssigneeUserVO assigneeUser;

    @Schema(description = "任务状态 1=待处理 2=已完成")
    private Integer status;

    @Schema(description = "审批意见/原因")
    private String reason;

    @Schema(description = "签名图片URL")
    private String signPicUrl;
}
