package com.zs.common.core.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zsadmin
 */
@Data
public abstract class BasePageParams implements Serializable {

    @Schema(description = "当前页", requiredMode = Schema.RequiredMode.REQUIRED)
    private long current;

    @Schema(description = "每页显示条数", requiredMode = Schema.RequiredMode.REQUIRED)
    private long pageSize;

    /**
     * 排序方法
     **/
    @Schema(description = "排序方法")
    private String order;

    /**
     * 排序字段
     **/
    @Schema(description = "排序字段")
    private String orderField;

}
