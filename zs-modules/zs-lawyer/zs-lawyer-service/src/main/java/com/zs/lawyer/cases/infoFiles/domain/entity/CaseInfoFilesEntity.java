package com.zs.lawyer.cases.infoFiles.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

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
@TableName("case_info_files")
@Schema(description = "案件附件Entity对象")
public class CaseInfoFilesEntity extends BaseEntity {

    /**  案件附件表ID */
    @TableId
    private Long caseInfoFilesId;

    /**  案件ID */
    private Long caseInfoId;

    /**  案件相关其他表ID */
    private Long caseOtherId;

    /**  附件来源 */
    private Integer fileSource;

    /**  文件名称 */
    private String fileName;

    /**  文件类型 */
    private String fileType;

    /**  文件大小 */
    private Long fileSize;

    /**  文件访问url */
    private String fileUrl;




}
