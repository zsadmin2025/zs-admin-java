package com.zs.lawyer.application.contact.domain.params;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
@Schema(description = "通讯录联系人表AddParams对象")
public class ContactAddParams implements Serializable {


    @Schema(description = "姓名")
    @NotBlank(message = "姓名不能为空")
    @Size(max = 50, message = "姓名长度不能超过50")
    private String name;

    @Schema(description = "联系电话（支持固话、手机号）")
    @Size(max = 20, message = "联系电话（支持固话、手机号）长度不能超过20")
    private String phone;

    @Schema(description = "性别 0-未知 1-男 2-女")
    private Integer gender;

    @Schema(description = "分组：内部、客户")
    @NotBlank(message = "分组：内部、客户不能为空")
    @Size(max = 30, message = "分组：内部、客户长度不能超过30")
    private String groupType;

    @Schema(description = "归属范围：公共、我的")
    @NotBlank(message = "归属范围：公共、我的不能为空")
    @Size(max = 30, message = "归属范围：公共、我的长度不能超过30")
    private String scopeType;

    @Schema(description = "备注信息")
    @Size(max = 500, message = "备注信息长度不能超过500")
    private String remark;








}
