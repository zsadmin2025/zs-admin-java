package com.zs.gen.domain.vo;

import com.zs.gen.domain.entity.GenTableColumn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Schema(description = "代码生成业务表")
@Getter
@Setter
public class GenTableVO {

    @Schema(description = "表ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long tableId;

    @Schema(description = "表名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tableName;

    @Schema(description = "表描述", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tableComment;

    @Schema(description = "实体类名称(首字母大写)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String className;

    @Schema(description = "生成包名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String packageName;

    @Schema(description = "生成模块名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String moduleName;

    @Schema(description = "生成业务名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String businessName;

    @Schema(description = "生成功能名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String functionName;

    @Schema(description = "生成作者", requiredMode = Schema.RequiredMode.REQUIRED)
    private String functionAuthor;

    @Schema(description = "上级菜单ID字段")
    private Long parentMenuId;

    @Schema(description = "上级菜单名称字段")
    private String parentMenuName;

    @Schema(description = "创建时间")
    private String createTime;

    @Schema(description = "更新时间")
    private String updateTime;

    private List<GenTableColumn> columns;
}
