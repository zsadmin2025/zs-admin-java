package com.zs.lawyer.cases.infoList.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.cases.infoList.domain.entity.CaseInfoListEntity;
import com.zs.lawyer.cases.infoList.domain.params.CaseInfoListAddParams;
import com.zs.lawyer.cases.infoList.domain.params.CaseInfoListPageQueryParams;
import com.zs.lawyer.cases.infoList.domain.params.CaseInfoListSelectQueryParams;
import com.zs.lawyer.cases.infoList.domain.params.CaseInfoListUpdateParams;
import com.zs.lawyer.cases.infoList.domain.vo.CaseInfoListVO;

import java.util.List;

/**
 * <p>
 * 案件结案目录表 服务类
 * </p>
 *
 * @author zs
 * @since 2025-06-21 12:20:27
 */
public interface CaseInfoListService extends IService<CaseInfoListEntity> {

    /** 分页 **/
    PageResult<CaseInfoListVO> page(CaseInfoListPageQueryParams caseInfoListPageQueryParams);

    /** 列表 **/
    List<CaseInfoListVO> getList(CaseInfoListSelectQueryParams caseInfoListSelectQueryParams);

    /** 新增 **/
    void save(CaseInfoListAddParams caseInfoListAddParams);

    /** 更新 **/
    void update(CaseInfoListUpdateParams caseInfoListUpdateParams);

    /** 根据id查询 **/
    CaseInfoListVO getById(Long id);

    /** 单个删除 **/
    void deleteById(Long caseInfoListId);

    /** 批量删除 **/
    void batchDelById(Long[] caseInfoListIds);

    /**
     * 获取案件结案目录列表
     * @param caseInfoId 案件信息表ID
     * @param caseType 案件类型
     * @return List<CaseInfoListVO>
     */
    List<CaseInfoListVO> getList(Long caseInfoId, String caseType);
}