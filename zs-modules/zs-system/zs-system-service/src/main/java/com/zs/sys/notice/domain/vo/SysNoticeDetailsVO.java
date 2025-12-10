package com.zs.sys.notice.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author zsadmin
 */
@Schema(description = "通知公告详情VO")
@Data
public class SysNoticeDetailsVO {


    @Schema(description = "通知公告详情id")
    private Long sysNoticeDetailsId;

    @Schema(description = "通知公告id")
    private Long sysNoticeId;

    @Schema(description = "用户id")
    private Long receiverId;

    @Schema(description = "用户名")
    private String realName;

    @Schema(description = "状态:1已读，2未读")
    private Integer status;

    @Schema(description = "阅读时间")
    private String readTime;
}

