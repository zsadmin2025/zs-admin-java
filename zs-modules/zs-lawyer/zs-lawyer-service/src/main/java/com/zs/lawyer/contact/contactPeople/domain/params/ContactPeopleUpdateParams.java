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
@Schema(description = "通讯录联系人updateParams对象")
public class ContactPeopleUpdateParams implements Serializable {


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

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "工作单位")
    private String placeWork;





    @Schema(description = "创建部门")
    private String creatorDept;

    @Schema(description = "租户id")
    private String tenantId;

}
