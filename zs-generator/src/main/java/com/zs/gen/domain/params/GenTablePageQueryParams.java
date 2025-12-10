package com.zs.gen.domain.params;

import com.zs.common.core.page.BasePageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "代码生成分页参数")
@Getter
@Setter
public class GenTablePageQueryParams extends BasePageParams {

    @Schema(description = "表名称")
    private String tableName;
}
