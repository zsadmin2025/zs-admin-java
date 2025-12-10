package com.zs.sys.notice.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zsadmin
 */
@Data
public class SysNoticeVO implements Serializable {

    @Schema(description = "通知公告id")
    private Long sysNoticeId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "通知公告类型: 1通知、2公告、3其他")
    private Integer type;

    @Schema(description = "通知公告等级: 1普通、2一般、3紧急")
    private Integer level;

    @Schema(description = "状态:0撤销，1草稿，2已发布")
    private Integer status;

    @Schema(description = "发布时间")
    private String releaseTime;

    @Schema(description = "创建人名称")
    private String realName;

    @Schema(description = "创建人ID")
    private Long creator;

    @Schema(description = "创建时间")
    private String createTime;

    @Schema(description = "修改人ID")
    private Long updater;

    @Schema(description = "修改时间")
    private String updateTime;

    @Schema(description = "附件")
    private List<SysNoticeFilesVO> files = new ArrayList<>();

    @Schema(description = "接收人")
    private List<SysNoticeDetailsVO> sysNoticeDetailsVOs = new ArrayList<>();
}
