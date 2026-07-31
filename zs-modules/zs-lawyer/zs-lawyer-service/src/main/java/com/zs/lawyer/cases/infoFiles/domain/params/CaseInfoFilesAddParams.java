package com.zs.lawyer.cases.infoFiles.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 案件相关附件表
 * </p>
 *
 * @author zs
 * @since 2025-06-21 09:34:39
 */
@Getter
@Setter
@Schema(description = "案件附件AddParams对象")
public class CaseInfoFilesAddParams implements Serializable {


    @Schema(description = "案件附件表ID")
    private Long caseInfoFilesId;

    @Schema(description = "案件ID")
    private Long caseInfoId;

    @Schema(description = "案件相关其他表ID")
    private Long caseOtherId;

    @Schema(description = "附件来源")
    private Integer fileSource;

    @Schema(description = "文件名称")
    private String fileName;

    @Schema(description = "文件类型")
    private String fileType;

    @Schema(description = "文件大小")
    private Long fileSize;

    @Schema(description = "文件访问url")
    private String fileUrl;





}
