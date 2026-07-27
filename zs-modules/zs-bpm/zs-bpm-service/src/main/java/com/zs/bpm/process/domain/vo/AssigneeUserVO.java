package com.zs.bpm.process.domain.vo;

import com.zs.sys.user.domain.vo.SysUserVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 审批人简要信息 VO
 *
 * @author zsadmin
 */

@Data
@Schema(description = "审批人简要信息")
public class AssigneeUserVO implements Serializable {

    @Schema(description = "发起人用户ID")
    private String startUserId;

    @Schema(description = "发起人真实姓名")
    private String startUserName;

    @Schema(description = "头像URL")
    private String avatar;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "公司ID")
    private Long companyId;

    @Schema(description = "公司名称")
    private String companyName;

    /**
     * 从 SysUserVO 转换
     */
    public static AssigneeUserVO from(SysUserVO sysUser) {
        if (sysUser == null) {
            return null;
        }
        AssigneeUserVO vo = new AssigneeUserVO();
        vo.setStartUserId(String.valueOf(sysUser.getSysUserId()));
        vo.setStartUserName(sysUser.getRealName() );
        vo.setAvatar(sysUser.getAvatar());
        vo.setDeptId(sysUser.getSysDeptId());
        vo.setDeptName(sysUser.getDeptName());
        return vo;
    }

}
