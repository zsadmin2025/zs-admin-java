package com.zs.lawyer.application.contact.mapper;

import com.zs.common.mp.base.DataPermissionMapper;
import com.zs.lawyer.application.contact.domain.entity.ContactEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 通讯录联系人表 Mapper 接口
 * </p>
 *
 * @author zs
 * {@code @date} 2026-07-30 18:37:41
 */
@Mapper
public interface ContactMapper extends DataPermissionMapper<ContactEntity> {

}
