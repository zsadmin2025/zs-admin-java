package com.zs.bpm.cc.mapper;

import com.zs.bpm.cc.domain.entity.BpmCcRecordEntity;
import com.zs.common.mp.base.DataPermissionMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 抄送记录 Mapper
 *
 * @author zsadmin
 */
@Mapper
public interface BpmCcRecordMapper extends DataPermissionMapper<BpmCcRecordEntity> {
}
