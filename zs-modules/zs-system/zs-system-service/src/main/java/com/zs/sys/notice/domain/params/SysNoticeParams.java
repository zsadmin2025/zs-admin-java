package com.zs.sys.notice.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author zsadmin
 */
@Data
public class SysNoticeParams {


    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "通知公告类型: 1通知、2公告、3其他")
    private Integer type;

    @Schema(description = "通知公告等级: 1普通、2一般、3紧急")
    private Integer level;

    @Schema(description = "状态:0撤销，1草稿，2已发布")
    private Integer status;

    @Schema(description = "接收方式:1全部用户，2指定用户，3指定角色，4指定部门，5指定岗位")
    private Integer receivingType;

    @Schema(description = "接收的id集合(根据接收方式不同，接收的id也不同)")
    private List<Long> receiverIds;

    @Schema(description = "附件")
    private List<SysNoticeFilesParams> sysNoticeFilesList;
}
