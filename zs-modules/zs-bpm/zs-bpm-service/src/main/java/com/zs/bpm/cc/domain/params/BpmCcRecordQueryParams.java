package com.zs.bpm.cc.domain.params;

import com.zs.common.core.page.BasePageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "抄送记录查询参数")
public class BpmCcRecordQueryParams extends BasePageParams {
    @Schema(description = "抄送标题")
    private String title;
    @Schema(description = "是否已读(0=未读,1=已读)")
    private Integer isRead;
}
