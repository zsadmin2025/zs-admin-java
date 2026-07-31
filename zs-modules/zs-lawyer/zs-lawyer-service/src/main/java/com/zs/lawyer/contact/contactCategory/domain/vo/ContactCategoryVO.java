package com.zs.lawyer.contact.contactCategory.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 通讯录分类
 * </p>
 *
 * @author zs
 * @since 2025-08-26 10:34:29
 */
@Getter
@Setter
@Schema(description = "通讯录分类VO对象")
public class ContactCategoryVO implements Serializable {

    @Schema(description = "")
    private Long contactCategoryId;

    @Schema(description = "父ID")
    private Long pid;

    @Schema(description = "所有上级ID，用逗号分开")
    private String pids;

    @Schema(description = "部门名称")
    private String categoryName;

    @Schema(description = "是否公共分类 0-否 1-是")
    private Long isPublic;

    @Schema(description = "部门描述")
    private String remark;

    @Schema(description = "创建者")
    private Long creator;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新者")
    private Long updater;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "创建部门")
    private String creatorDept;

    @Schema(description = "租户id")
    private String tenantId;

}
