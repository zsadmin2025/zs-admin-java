package com.zs.lawyer.cases.info.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.cases.info.domain.entity.CaseInfoEntity;
import com.zs.lawyer.cases.info.domain.params.*;
import com.zs.lawyer.cases.info.domain.vo.CaseHomeHearingVO;
import com.zs.lawyer.cases.info.domain.vo.CaseHomeVO;
import com.zs.lawyer.cases.info.domain.vo.CaseInfoVO;
import com.zs.lawyer.cases.info.domain.vo.CaseVO;
import com.zs.lawyer.cases.infoApprove.domain.params.CaseInfoApprovePageQueryParams;

import java.util.List;

/**
 * <p>
 * 案件信息表 服务类
 * </p>
 *
 * @author zs
 * @since 2025-06-08 16:43:20
 */
public interface CaseInfoService extends IService<CaseInfoEntity> {

    /** 分页 **/
    PageResult<CaseVO> page(CaseInfoPageQueryParams caseInfoPageQueryParams);

    /** 立项审批分页 **/
    PageResult<CaseVO> approvePage(CaseInfoApprovePageQueryParams caseInfoApprovePageQueryParams);

    /** 列表 **/
    List<CaseInfoVO> getList(CaseInfoSelectQueryParams caseInfoSelectQueryParams);

    /** 新增 **/
    void save(CaseAddParams caseAddParams);

    /** 通过审批 **/
    void passApprove(CaseApproveParams caseApproveParams);

    /** 否决审批 **/
    void rejectApprove(CaseApproveParams caseApproveParams);

    /** 更新 **/
    void update(CaseUpdateParams caseUpdateParams);

    CaseInfoVO getByCaseInfoId(Long caseInfoId);

    /** 根据id查询 **/
    CaseVO getById(Long id);

    /** 单个删除 **/
    void deleteById(Long caseInfoId);

    /** 批量删除 **/
    void batchDelById(Long[] caseInfoIds);

    /** 结案 **/
    void closed(CaseInfoStatusParams caseInfoStatusParams);

    /** 归档 **/
    void filing(CaseInfoStatusParams caseInfoStatusParams);

    /** 作废 **/
    void cancel(CaseInfoStatusParams caseInfoStatusParams);

    /** 恢复 **/
    void restore(CaseInfoStatusParams caseInfoStatusParams);

    /** 新增案件委托书 **/
    void savePowerAttorney(CaseInfoPowerAttorneyParams caseInfoPowerAttorneyParams);

    /** 最近三个月的案件 **/
    List<CaseHomeVO> getRecentThreeMonthRegisteredCase();

    /** 最近一个月的案件 **/
    List<CaseHomeHearingVO> getRecentOneMonthHearingCase();
}