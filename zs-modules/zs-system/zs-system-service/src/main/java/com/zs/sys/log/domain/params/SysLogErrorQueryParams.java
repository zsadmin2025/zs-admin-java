package com.zs.sys.log.domain.params;

import com.zs.common.core.page.BasePageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author zsadmin
 */
@Schema(description = "系统异常日志查询参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class SysLogErrorQueryParams extends BasePageParams {

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "IP地址")
    private String ipAddress;

    @Schema(description = "模块")
    private String module;

    @Schema(description = "异常类型")
    private String exceptionType;

    @Schema(description = "请求路径")
    private String requestPath;

    @Schema(description = "请求方法")
    private String requestMethod;
}
