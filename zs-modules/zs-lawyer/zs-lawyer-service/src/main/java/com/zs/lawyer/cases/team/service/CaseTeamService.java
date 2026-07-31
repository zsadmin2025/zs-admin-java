package com.zs.lawyer.cases.team.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.cases.team.domain.entity.CaseTeamEntity;
import com.zs.lawyer.cases.team.domain.params.CaseTeamAddParams;
import com.zs.lawyer.cases.team.domain.params.CaseTeamPageQueryParams;
import com.zs.lawyer.cases.team.domain.params.CaseTeamSelectQueryParams;
import com.zs.lawyer.cases.team.domain.params.CaseTeamUpdateParams;
import com.zs.lawyer.cases.team.domain.vo.CaseTeamVO;

import java.util.List;

/**
 * <p>
 * 案件团队 服务类
 * </p>
 *
 * @author zs
 * @since 2025-06-08 18:01:20
 */
public interface CaseTeamService extends IService<CaseTeamEntity> {

    /** 分页 **/
    PageResult<CaseTeamVO> page(CaseTeamPageQueryParams caseTeamPageQueryParams);

    /** 列表 **/
    List<CaseTeamVO> getList(CaseTeamSelectQueryParams caseTeamSelectQueryParams);

    /** 新增 **/
    void save(CaseTeamAddParams caseTeamAddParams);

    /** 更新 **/
    void update(CaseTeamUpdateParams caseTeamUpdateParams);

    /** 根据id查询 **/
    CaseTeamVO getById(Long id);

    CaseTeamVO getByCaseInfoId(Long caseInfoId);

    /** 单个删除 **/
    void deleteById(Long caseTeamId);

    /** 批量删除 **/
    void batchDelById(Long[] caseTeamIds);
}