package com.zs.common.core.model;


import lombok.Data;

/**
 * @author zsadmin
 */
@Data
public class BaseVO {
    private Long creator;
    private String createTime;
    private Long updater;
    private String updateTime;
}
