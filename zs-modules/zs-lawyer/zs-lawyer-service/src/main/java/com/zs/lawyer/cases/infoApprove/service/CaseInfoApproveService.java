package com.zs.lawyer.cases.infoApprove.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.cases.infoApprove.domain.entity.CaseInfoApproveEntity;
import com.zs.lawyer.cases.infoApprove.domain.params.CaseInfoApproveAddParams;
import com.zs.lawyer.cases.infoApprove.domain.params.CaseInfoApprovePageQueryParams;
import com.zs.lawyer.cases.infoApprove.domain.params.CaseInfoApproveSelectQueryParams;
import com.zs.lawyer.cases.infoApprove.domain.params.CaseInfoApproveUpdateParams;
import com.zs.lawyer.cases.infoApprove.domain.vo.CaseInfoApproveVO;

import java.util.List;

/**
 * <p>
 * 案件审批表 服务类
 * </p>
 *
 * @author zs
 * @since 2025-06-30 09:04:42
 */
public interface CaseInfoApproveService extends IService<CaseInfoApproveEntity> {

    /** 分页 **/
    PageResult<CaseInfoApproveVO> page(CaseInfoApprovePageQueryParams caseInfoApprovePageQueryParams);

    /** 列表 **/
    List<CaseInfoApproveVO> getList(CaseInfoApproveSelectQueryParams caseInfoApproveSelectQueryParams);


    /** 新增 **/
    void save(CaseInfoApproveAddParams caseInfoApproveAddParams);

    /** 更新 **/
    void update(CaseInfoApproveUpdateParams caseInfoApproveUpdateParams);

    /** 根据id查询 **/
    CaseInfoApproveVO getById(Long id);

    /** 单个删除 **/
    void deleteById(Long caseInfoApproveId);

    /** 批量删除 **/
    void batchDelById(Long[] caseInfoApproveIds);

     /** 保存案件审批人 **/
    void save(List<Long> approvalLawyerList, Long caseInfoId);

    /** 修改案件审批人 **/
    void update(List<Long> approvalLawyerList, Long caseInfoId);



}