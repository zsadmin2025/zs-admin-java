package com.zs.bpm.model.domain.dto;

import lombok.Data;
/**
 * 按钮配置
 */
@Data
public class ButtonSettingDTO {

    private Integer id;
    private String displayName;
    private Boolean enable;
}
