package com.zs.sys.user.domain.params;


import com.zs.common.core.page.BasePageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zsadmin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserQueryParams extends BasePageParams {


    @Schema(description = "用户id")
    private Long sysUserId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "年龄")
    private Integer age;

    @Schema(description = "性别")
    private Integer sex;

    @Schema(description = "工号")
    private String employeeNumber;

    @Schema(description = "是否是管理员")
    private Integer isAdmin;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "部门id")
    private Long sysDeptId;

    @Schema(description = "岗位id")
    private Long sysPostId;

}
