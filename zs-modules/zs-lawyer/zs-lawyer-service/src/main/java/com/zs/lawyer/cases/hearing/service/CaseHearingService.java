package com.zs.lawyer.cases.hearing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.cases.hearing.domain.entity.CaseHearingEntity;
import com.zs.lawyer.cases.hearing.domain.params.CaseHearingAddParams;
import com.zs.lawyer.cases.hearing.domain.params.CaseHearingPageQueryParams;
import com.zs.lawyer.cases.hearing.domain.params.CaseHearingSelectQueryParams;
import com.zs.lawyer.cases.hearing.domain.params.CaseHearingUpdateParams;
import com.zs.lawyer.cases.hearing.domain.vo.CaseHearingVO;

import java.util.List;

/**
 * <p>
 * 案件开庭表 服务类
 * </p>
 *
 * @author zs
 * @since 2025-06-08 17:58:57
 */
public interface CaseHearingService extends IService<CaseHearingEntity> {

    /** 分页 **/
    PageResult<CaseHearingVO> page(CaseHearingPageQueryParams caseHearingPageQueryParams);

    /** 列表 **/
    List<CaseHearingVO> getList(CaseHearingSelectQueryParams caseHearingSelectQueryParams);

    /** 新增 **/
    void save(CaseHearingAddParams caseHearingAddParams);

    /** 批量新增 **/
    void save(List<CaseHearingAddParams> caseHearingAddParamsList);

    /** 更新 **/
    void update(CaseHearingUpdateParams caseHearingUpdateParams);

    /** 根据id查询 **/
    CaseHearingVO getById(Long id);

    /** 单个删除 **/
    void deleteById(Long caseHearingId);

    /** 批量删除 **/
    void batchDelById(Long[] caseHearingIds);

    /** 批量保存 **/
    void update(List<CaseHearingUpdateParams> hearingList);

    /** 根据案件IDs查询 **/
    List<CaseHearingVO> getList(List<Long> caseInfoIds);
}