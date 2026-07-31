package com.zs.lawyer.cases.info.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.cases.info.domain.entity.CaseRelatedPartiesEntity;
import com.zs.lawyer.cases.info.domain.params.CaseRelatedPartiesAddParams;
import com.zs.lawyer.cases.info.domain.params.CaseRelatedPartiesPageQueryParams;
import com.zs.lawyer.cases.info.domain.params.CaseRelatedPartiesSelectQueryParams;
import com.zs.lawyer.cases.info.domain.params.CaseRelatedPartiesUpdateParams;
import com.zs.lawyer.cases.info.domain.vo.CaseRelatedPartiesVO;

import java.util.List;

/**
 * <p>
 * 案件相关方 服务类
 * </p>
 *
 * @author zs
 * @since 2025-06-08 18:08:42
 */
public interface CaseRelatedPartiesService extends IService<CaseRelatedPartiesEntity> {

    /** 分页 **/
    PageResult<CaseRelatedPartiesVO> page(CaseRelatedPartiesPageQueryParams caseRelatedPartiesPageQueryParams);

    /** 列表 **/
    List<CaseRelatedPartiesVO> getList(CaseRelatedPartiesSelectQueryParams caseRelatedPartiesSelectQueryParams);

    /** 新增 **/
    void save(CaseRelatedPartiesAddParams caseRelatedPartiesAddParams);

    /** 更新 **/
    void update(CaseRelatedPartiesUpdateParams caseRelatedPartiesUpdateParams);

    /** 根据id查询 **/
    CaseRelatedPartiesVO getById(Long id);

    /** 单个删除 **/
    void deleteById(Long caseRelatedPartiesId);

    /** 批量删除 **/
    void batchDelById(Long[] caseRelatedPartiesIds);

    /** 根据案件id查询我方信息 **/
    List<CaseRelatedPartiesVO> getOurSideListByCaseInfoId(Long caseInfoId);

    /** 根据案件id查询对方信息 **/
    List<CaseRelatedPartiesVO> getOtherSideListByCaseInfoId(Long caseInfoId);
}