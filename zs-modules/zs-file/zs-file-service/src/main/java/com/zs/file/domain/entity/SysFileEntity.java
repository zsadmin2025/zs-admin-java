package com.zs.file.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zs.common.core.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName("sys_file")
@Data
public class SysFileEntity extends BaseEntity {

    @TableId
    private Long sysFileId;
    // 文件名
    private String fileName;
    // 文件原始名
    private String fileOriginalName;
    // 文件类型 1-图片 2-视频 3-音频 4-文档 5-其他
    private String fileType;
    // 文件大小
    private double fileSize;
    // 文件访问url
    private String fileUrl;
    // 文件保存路径
    @JsonIgnore
    private String filePath;
}
