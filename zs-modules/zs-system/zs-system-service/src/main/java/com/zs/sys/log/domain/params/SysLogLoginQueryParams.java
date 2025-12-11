package com.zs.sys.log.domain.params;

import com.zs.common.core.page.BasePageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author zsadmin
 */
@Schema(description = "系统登录日志查询参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class SysLogLoginQueryParams extends BasePageParams {

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "IP地址")
    private String ipAddress;

    @Schema(description = "城市")
    private String city;

    @Schema(description = "登陆状态， 1-成功 0-失败")
    private Integer loginStatus;

    @Schema(description = "浏览器")
    private String browser;

    @Schema(description = "操作系统")
    private String os;
}
