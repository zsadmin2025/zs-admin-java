package com.zs.lawyer.contact.contactPeople.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 通讯录联系人
 * </p>
 *
 * @author zs
 * @since 2025-08-26 10:42:27
 */
@Getter
@Setter
@Schema(description = "通讯录联系人electQueryParams对象")
public class ContactPeopleSelectQueryParams implements Serializable {

    @Schema(description = "")
    private Long contactPeopleId;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "性别")
    private String sex;

    @Schema(description = "关联分类表ID")
    private Long contactCategoryId;

    @Schema(description = "工作单位")
    private String placeWork;

    @Schema(description = "创建者")
    private Long creator;

    @Schema(description = "更新者")
    private Long updater;

    @Schema(description = "创建部门")
    private String creatorDept;

    @Schema(description = "租户id")
    private String tenantId;

}
