package com.zs.bpm.expression.mapper;

import com.zs.bpm.expression.domain.entity.BpmProcessExpressionEntity;
import com.zs.common.mp.base.DataPermissionMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 流程表达式 Mapper
 *
 * @author zsadmin
 */
@Mapper
public interface BpmProcessExpressionMapper extends DataPermissionMapper<BpmProcessExpressionEntity> {
}
