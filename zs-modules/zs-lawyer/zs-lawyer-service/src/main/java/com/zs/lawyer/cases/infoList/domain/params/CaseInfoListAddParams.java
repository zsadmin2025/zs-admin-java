package com.zs.lawyer.cases.infoList.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 案件结案目录表
 * </p>
 *
 * @author zs
 * @since 2025-06-21 12:20:27
 */
@Getter
@Setter
@Schema(description = "案件结案目录AddParams对象")
public class CaseInfoListAddParams implements Serializable {


    @Schema(description = "案件结案目录表ID")
    private Long caseInfoListId;

    @Schema(description = "案件信息表ID")
    private Long caseInfoId;

    @Schema(description = "结案目录基础表ID")
    private Long caseListId;

    @Schema(description = "结案目录文件名称")
    private String caseListFileName;

    @Schema(description = "结案目录文件url")
    private String caseListFileUrl;

    @Schema(description = "文件名称")
    private String fileName;

    @Schema(description = "文件url")
    private String fileUrl;





}
