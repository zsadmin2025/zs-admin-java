package com.zs.lawyer.cases.infoApprovalForm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.cases.infoApprovalForm.domain.entity.CaseInfoApprovalFormEntity;
import com.zs.lawyer.cases.infoApprovalForm.domain.params.*;
import com.zs.lawyer.cases.infoApprovalForm.domain.vo.CaseInfoApprovalFormVO;

import java.util.List;

/**
 * <p>
 * 案件审批表 服务类
 * </p>
 *
 * @author zs
 * @since 2025-07-10 07:07:27
 */
public interface CaseInfoApprovalFormService extends IService<CaseInfoApprovalFormEntity> {

    /** 分页 **/
    PageResult<CaseInfoApprovalFormVO> page(CaseInfoApprovalFormPageQueryParams caseInfoApprovalFormPageQueryParams);

    /** 列表 **/
    List<CaseInfoApprovalFormVO> getList(CaseInfoApprovalFormSelectQueryParams caseInfoApprovalFormSelectQueryParams);

    /** 新增 **/
    void save(CaseInfoApprovalFormAddParams caseInfoApprovalFormAddParams);

    /** 更新 **/
    void update(CaseInfoApprovalFormUpdateParams caseInfoApprovalFormUpdateParams);

    /** 根据id查询 **/
    CaseInfoApprovalFormVO getById(Long id);

    /** 单个删除 **/
    void deleteById(Long caseInfoApprovalFormId);

    /** 批量删除 **/
    void batchDelById(Long[] caseInfoApprovalFormIds);

    /** 通过审批 **/
    void passApprove(CaseInfoApprovalFormParams caseInfoApprovalFormParams);

    CaseInfoApprovalFormVO getByCaseInfoId(Long caseInfoId);

    void submitApprovalForm(CaseInfoApprovalFormUpdateParams caseInfoApprovalFormUpdateParams);
}