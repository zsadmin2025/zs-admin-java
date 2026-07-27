package com.zs.sys.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zs.sys.member.domain.entity.MemberUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberUserMapper extends BaseMapper<MemberUserEntity> {

    MemberUserEntity selectByPhone(@Param("phone") String phone);

    MemberUserEntity selectByPhoneAndTenant(@Param("phone") String phone, @Param("tenantId") String tenantId);
}
