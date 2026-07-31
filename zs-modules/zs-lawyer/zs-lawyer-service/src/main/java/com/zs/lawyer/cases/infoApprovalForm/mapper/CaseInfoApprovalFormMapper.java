package com.zs.lawyer.cases.infoApprovalForm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.lawyer.cases.infoApprovalForm.domain.entity.CaseInfoApprovalFormEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 案件审批表 Mapper 接口
 * </p>
 *
 * @author zs
 * @since 2025-07-10 07:07:27
 */
@Mapper
public interface CaseInfoApprovalFormMapper extends BaseMapper<CaseInfoApprovalFormEntity> {

    IPage<CaseInfoApprovalFormEntity> page(Page<CaseInfoApprovalFormEntity> page, @Param("params") Map<String, Object> params);
}
