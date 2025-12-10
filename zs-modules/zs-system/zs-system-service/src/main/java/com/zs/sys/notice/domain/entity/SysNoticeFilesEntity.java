package com.zs.sys.notice.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName("sys_notice_files")
@Data
@Schema(description = "通知公告附件")
public class SysNoticeFilesEntity extends BaseEntity {

    /**
     * 附件id
     */
    @TableId
    private Long sysNoticeFilesId;

    /**
     * 附件所属的id
     */
    private Long sysNoticeId;

    /**
     * 附件名称
     */
    private String fileName;

    /**
     * 附件原始名称
     */
    private String fileOriginalName;

    /**
     * 文件类型 1-图片 2-视频 3-音频 4-文档 5-其他
     */
    private String fileType;

    /**
     * 文件大小
     */
    private double fileSize;

    /**
     * 文件访问url
     */
    private String fileUrl;
    /**
     * 附件报错路径
     */
    private String filePath;
}
