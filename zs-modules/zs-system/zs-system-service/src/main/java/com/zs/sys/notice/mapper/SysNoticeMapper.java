package com.zs.sys.notice.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.common.mp.base.DataPermissionMapper;
import com.zs.sys.notice.domain.entity.SysNoticeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author zsadmin
 */
@Mapper
public interface SysNoticeMapper extends DataPermissionMapper<SysNoticeEntity> {

    IPage<SysNoticeEntity> page(Page<SysNoticeEntity> page, @Param("params") Map<String, Object> params);

    SysNoticeEntity get(Long sysNoticeId);
}
