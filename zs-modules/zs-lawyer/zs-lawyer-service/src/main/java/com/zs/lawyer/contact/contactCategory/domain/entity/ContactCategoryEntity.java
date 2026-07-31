package com.zs.lawyer.contact.contactCategory.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

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
@TableName("contact_category")
@Schema(description = "通讯录分类Entity对象")
public class ContactCategoryEntity extends BaseEntity {

    /**   */
    @TableId
    private Long contactCategoryId;

    /**  父ID */
    private Long pid;

    /**  所有上级ID，用逗号分开 */
    private String pids;

    /**  部门名称 */
    private String categoryName;

    /**  是否公共分类 0-否 1-是 */
    private Long isPublic;

    /**  部门描述 */
    private String remark;






}
