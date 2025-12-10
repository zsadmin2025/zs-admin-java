package com.zs.model.params;

import com.zs.common.core.page.BasePageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "在线用户查询参数")
@Getter
@Setter
public class OnlineUserQueryParams  extends BasePageParams {

    @Schema(description = "用户名")
    private String username;
}
