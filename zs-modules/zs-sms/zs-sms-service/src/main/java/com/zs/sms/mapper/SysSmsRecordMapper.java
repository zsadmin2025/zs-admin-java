package com.zs.sms.mapper;

import com.zs.common.mp.base.DataPermissionMapper;
import com.zs.sms.domain.entity.SysSmsRecordEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 短信记录表 Mapper 接口
 * </p>
 *
 * @author zs
 * @since 2025-11-25 22:20:34
 */
@Mapper
public interface SysSmsRecordMapper extends DataPermissionMapper<SysSmsRecordEntity> {

}
