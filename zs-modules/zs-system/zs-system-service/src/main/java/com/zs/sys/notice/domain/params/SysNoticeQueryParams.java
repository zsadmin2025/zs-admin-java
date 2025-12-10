package com.zs.sys.notice.domain.params;

import com.zs.common.core.page.BasePageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zsadmin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysNoticeQueryParams extends BasePageParams {

    @Schema(description = "标题")
    public String title;

    @Schema(description = "内容")
    public String content;

    @Schema(description = "通知公告类型: 通知、公告、其他")
    public Integer type;

    @Schema(description = "通知公告等级: 普通、一般、紧急")
    public Integer level;

    @Schema(description = "状态:0撤销，1草稿，2已发布")
    public Integer status;

}
