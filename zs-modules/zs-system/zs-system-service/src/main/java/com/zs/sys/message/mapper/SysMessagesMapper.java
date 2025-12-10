package com.zs.sys.message.mapper;

import com.zs.common.mp.base.DataPermissionMapper;
import com.zs.sys.message.domain.entity.SysMessagesEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 消息表 Mapper 接口
 * </p>
 *
 * @author zs
 * @since 2025-11-17 09:01:44
 */
@Mapper
public interface SysMessagesMapper extends DataPermissionMapper<SysMessagesEntity> {

}
