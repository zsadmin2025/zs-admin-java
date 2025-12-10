package com.zs.file.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;


@Schema(description = "附件信息")
@Data
public class SysFileVO implements Serializable {

    @Schema(description = "附件id")
    private Long sysFileId;

    @Schema(description = "文件名")
    private String fileName;

    @Schema(description = "文件原始名")
    private String fileOriginalName;

    @Schema(description = "附件类型：1-图片, 2-视频, 3-音频, 4-文档, 5-其他")
    private String fileType;

    @Schema(description = "文件大小")
    private double fileSize;

    @Schema(description = "文件访问url")
    private String fileUrl;

    @Schema(description = "文件保存路径")
    @JsonIgnore
    private String filePath;
}
