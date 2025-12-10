package com.zs.sys.notice.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SysNoticeFilesVO {


    @Schema(description = "通知公告附件表ID")
    private Long sysNoticeFilesId;

    @Schema(description = "通知公告表ID")
    private Long sysNoticeId;

    @Schema(description = "附件名称")
    private String fileName;

    @Schema(description = "附件原始名称")
    private String fileOriginalName;

    @Schema(description = "文件类型 1-图片 2-视频 3-音频 4-文档 5-其他")
    private String fileType;

    @Schema(description = "文件大小")
    private double fileSize;

    @Schema(description = "文件访问url")
    private String fileUrl;

    @Schema(description = "附件保存路径")
    private String filePath;
}
