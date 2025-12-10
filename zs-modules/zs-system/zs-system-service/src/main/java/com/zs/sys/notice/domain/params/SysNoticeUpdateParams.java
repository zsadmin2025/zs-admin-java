package com.zs.sys.notice.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author zsadmin
 */
@Data
public class SysNoticeUpdateParams {

    @Schema(description = "通知公告id")
    private Long sysNoticeId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "通知公告类型: 通知、公告、其他")
    private String type;

    @Schema(description = "通知公告等级: 普通、一般、紧急")
    private String level;

    @Schema(description = "状态:0撤销，1草稿，2已发布")
    private Integer status;

    @Schema(description = "接收方式:1全部用户，2指定用户，3指定角色，4指定部门，5指定岗位")
    private Integer receivingType;

    @Schema(description = "接收人ids")
    private List<Long> receiverIds;

    @Schema(description = "发布时间")
    private String releaseTime;

    @Schema(description = "附件")
    private List<SysNoticeFilesParams> files;


}
