package com.zs.lawyer.contact.contactPeople.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

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
@TableName("contact_people")
@Schema(description = "通讯录联系人Entity对象")
public class ContactPeopleEntity extends BaseEntity {

    /**   */
    @TableId
    private Long contactPeopleId;

    /**  姓名 */
    private String name;

    /**  联系电话 */
    private String phone;

    /**  性别 */
    private String sex;

    /**  关联分类表ID */
    private Long contactCategoryId;

    /**  工作单位 */
    private String placeWork;

    /**  备注 */
    private String remark;







}
