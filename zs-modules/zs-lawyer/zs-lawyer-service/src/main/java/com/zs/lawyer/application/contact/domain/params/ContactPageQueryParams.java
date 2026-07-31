package com.zs.lawyer.application.contact.domain.params;

import com.zs.common.core.page.BasePageParams;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

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
@Schema(description = "通讯录联系人表ageQueryParams对象")
public class ContactPageQueryParams  extends BasePageParams implements Serializable {

    @Schema(description = "主键ID")
    private Long contactId;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "联系电话（支持固话、手机号）")
    private String phone;

    @Schema(description = "性别 0-未知 1-男 2-女")
    private Integer gender;

    @Schema(description = "分组：内部、客户")
    private String groupType;

    @Schema(description = "归属范围：公共、我的")
    private String scopeType;

    @Schema(description = "备注信息")
    private String remark;

}
