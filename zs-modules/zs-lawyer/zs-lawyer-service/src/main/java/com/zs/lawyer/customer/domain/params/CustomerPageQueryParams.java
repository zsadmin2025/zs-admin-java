package com.zs.lawyer.customer.domain.params;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zs.common.core.page.BasePageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 客户表
 * </p>
 *
 * @author zs
 * @since 2025-05-29 21:55:59
 */
@Getter
@Setter
@Schema(description = "客户管理ageQueryParams对象")
public class CustomerPageQueryParams  extends BasePageParams implements Serializable {

    @Schema(description = "客户类别")
    private String customerCategory;
    @Schema(description = "客户性质")
    private String customerNature;
    @Schema(description = "客户名称")
    private String customerName;
    @Schema(description = "维系人")
    private List<String> maintainingPeople;
    @Schema(description = "共享人")
    private List<String> sharer;
    @Schema(description = "客户等级")
    private String customerGrade;
    @Schema(description = "是否顾问，0-否，1-是")
    private Integer isConsultant;
    @Schema(description = "录入人")
    private Long inputPerson;
    @Schema(description = "录入时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inputTime;
    @Schema(description = "行业类别")
    private String industryCategory;
    @Schema(description = "客户状态")
    private Integer status;
}
