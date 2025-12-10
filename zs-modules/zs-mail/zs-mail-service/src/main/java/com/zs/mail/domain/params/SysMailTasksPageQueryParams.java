package com.zs.mail.domain.params;

import com.zs.common.core.page.BasePageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author zs
 * @since 2025-10-30 10:44:53
 */
@Getter
@Setter
@Schema(description = "邮件任务ageQueryParams对象")
public class SysMailTasksPageQueryParams  extends BasePageParams implements Serializable {

    @Schema(description = "邮件主题")
    private String subject;

    @Schema(description = "发件人邮箱地址")
    private String sender;

    @Schema(description = "发件人姓名")
    private String senderName;

    @Schema(description = "状态，1-草稿 2-已发送")
    private Integer status;
}

