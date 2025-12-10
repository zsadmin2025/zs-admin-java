package com.zs.sys.config.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 网站配置VO
 */
@Schema(name = "网站配置VO")
@Data
public class SysConfigWebsiteVO {

    @Schema(name = "网站名称")
    private String websiteName;

    @Schema(name = "网站描述")
    private String description;

    @Schema(name = "网站logo")
    private String logo;

    @Schema(name = "版本")
    private String version;

    @Schema(name = "版权")
    private String copyright;

    @Schema(name = "备案号")
    private String icp;

    @Schema(name = "备案号链接")
    private String icpLink;

    @Schema(name = "隐私政策")
    private String privacyPolicy;

    @Schema(name = "服务条款")
    private String termsOfService;
}
