package com.zs.sys.config.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 */
@Data
@TableName("sys_config")
@EqualsAndHashCode(callSuper = true)
public class SysConfigEntity extends BaseEntity {

    @TableId
    private Long sysConfigId;
    private String configKey;
    private String configName;
    private String configValue;
}
