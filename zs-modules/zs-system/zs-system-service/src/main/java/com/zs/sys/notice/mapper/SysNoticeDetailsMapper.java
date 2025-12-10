package com.zs.sys.notice.mapper;

import com.zs.common.mp.base.DataPermissionMapper;
import com.zs.sys.notice.domain.entity.SysNoticeDetailsEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zsadmin
 */
@Mapper
public interface SysNoticeDetailsMapper extends DataPermissionMapper<SysNoticeDetailsEntity> {

    List<SysNoticeDetailsEntity> list(Long sysNoticeId);
}
