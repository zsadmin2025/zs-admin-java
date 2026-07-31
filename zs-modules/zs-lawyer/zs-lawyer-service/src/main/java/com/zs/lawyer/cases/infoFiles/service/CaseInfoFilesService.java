package com.zs.lawyer.cases.infoFiles.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.cases.infoFiles.domain.entity.CaseInfoFilesEntity;
import com.zs.lawyer.cases.infoFiles.domain.params.CaseInfoFilesAddParams;
import com.zs.lawyer.cases.infoFiles.domain.params.CaseInfoFilesPageQueryParams;
import com.zs.lawyer.cases.infoFiles.domain.params.CaseInfoFilesSelectQueryParams;
import com.zs.lawyer.cases.infoFiles.domain.params.CaseInfoFilesUpdateParams;
import com.zs.lawyer.cases.infoFiles.domain.vo.CaseInfoFilesVO;

import java.util.List;

/**
 * <p>
 * 案件相关附件表 服务类
 * </p>
 *
 * @author zs
 * @since 2025-06-21 09:34:39
 */
public interface CaseInfoFilesService extends IService<CaseInfoFilesEntity> {

    /** 分页 **/
    PageResult<CaseInfoFilesVO> page(CaseInfoFilesPageQueryParams caseInfoFilesPageQueryParams);

    /** 列表 **/
    List<CaseInfoFilesVO> getList(CaseInfoFilesSelectQueryParams caseInfoFilesSelectQueryParams);

    /** 新增 **/
    void save(CaseInfoFilesAddParams caseInfoFilesAddParams);

    /** 批量新增 **/
    void save(List<CaseInfoFilesAddParams> caseInfoFilesAddParams);

    /**
     * 修改
     * @param caseInfoFiles 修改参数
     * @param caseInfoId 案件id
     * @param caseOtherId 其他案件id
     */
    void saveOrUpdateBatch(List<CaseInfoFilesUpdateParams> caseInfoFiles, Long caseInfoId, Long caseOtherId, Integer fileSource);

    /** 更新 **/
    void update(CaseInfoFilesUpdateParams caseInfoFilesUpdateParams);

    /** 根据id查询 **/
    CaseInfoFilesVO getById(Long id);

    /** 单个删除 **/
    void deleteById(Long caseInfoFilesId);

    /** 批量删除 **/
    void batchDelById(Long[] caseInfoFilesIds);

}