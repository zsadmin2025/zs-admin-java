package com.zs.sms.mapper;

import com.zs.common.mp.base.DataPermissionMapper;
import com.zs.sms.domain.entity.SysSmsTemplateEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 短信模板表 Mapper 接口
 * </p>
 *
 * @author zs
 * @since 2025-11-26 09:40:35
 */
@Mapper
public interface SysSmsTemplateMapper extends DataPermissionMapper<SysSmsTemplateEntity> {

}
