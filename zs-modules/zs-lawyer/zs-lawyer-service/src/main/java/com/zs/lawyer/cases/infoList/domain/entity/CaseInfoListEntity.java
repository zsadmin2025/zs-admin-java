package com.zs.lawyer.cases.infoList.domain.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

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
@TableName("case_info_list")
@Schema(description = "案件结案目录Entity对象")
public class CaseInfoListEntity extends BaseEntity {

    /**  案件结案目录表ID */
    @TableId
    private Long caseInfoListId;

    /**  案件信息表ID */
    private Long caseInfoId;

    /**  结案目录基础表ID */
    private Long caseListId;

    /**  结案目录文件名称 */
    private String caseListFileName;

    /**  结案目录文件url */
    private String caseListFileUrl;

    /**  文件名称 */
    private String fileName;

    /**  文件url */
    private String fileUrl;

    @TableField(exist = false)
    private Long basicCaseListId;

    @TableField(exist = false)
    private String basicFileName;

    @TableField(exist = false)
    private String basicFileOriginalName;

    @TableField(exist = false)
    private String fileType;

    @TableField(exist = false)
    private String basicFileUrl;

    @TableField(exist = false)
    private Integer isRequired;


}
