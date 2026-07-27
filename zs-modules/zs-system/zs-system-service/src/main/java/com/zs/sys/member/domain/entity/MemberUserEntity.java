package com.zs.sys.member.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("member_user")
public class MemberUserEntity extends BaseEntity {

    @TableId(value = "member_user_id", type = IdType.ASSIGN_ID)
    private Long memberUserId;

    private String phone;

    private String password;

    private String nickname;

    private String avatar;

    private Integer sex;

    private String openid;

    private Integer status;

}
