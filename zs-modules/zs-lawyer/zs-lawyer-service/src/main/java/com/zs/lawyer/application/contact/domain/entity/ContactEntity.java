package com.zs.lawyer.application.contact.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;


/**
 * <p>
 * 通讯录联系人表
 * </p>
 *
 * @author zs
 * {@code @date} 2026-07-30 18:37:41
 */
@Getter
@Setter
@TableName("biz_contact")
@Schema(description = "通讯录联系人表Entity对象")
public class ContactEntity extends BaseEntity {

    /**  主键ID */
    @TableId
    private Long contactId;

    /**  姓名 */
    private String name;

    /**  联系电话（支持固话、手机号） */
    private String phone;

    /**  性别 0-未知 1-男 2-女 */
    private Integer gender;

    /**  分组：内部、客户 */
    private String groupType;

    /**  归属范围：公共、我的 */
    private String scopeType;

    /**  备注信息 */
    private String remark;


}
