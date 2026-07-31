package com.zs.lawyer.cases.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.cases.contract.domain.entity.CaseContractNodeEntity;
import com.zs.lawyer.cases.contract.domain.params.CaseContractNodeAddParams;
import com.zs.lawyer.cases.contract.domain.params.CaseContractNodePageQueryParams;
import com.zs.lawyer.cases.contract.domain.params.CaseContractNodeSelectQueryParams;
import com.zs.lawyer.cases.contract.domain.params.CaseContractNodeUpdateParams;
import com.zs.lawyer.cases.contract.domain.vo.CaseContractNodeVO;

import java.util.List;

/**
 * <p>
 * 案件合同节点 服务类
 * </p>
 *
 * @author zs
 * @since 2025-06-08 18:03:56
 */
public interface CaseContractNodeService extends IService<CaseContractNodeEntity> {

    /** 分页 **/
    PageResult<CaseContractNodeVO> page(CaseContractNodePageQueryParams caseContractNodePageQueryParams);

    /** 列表 **/
    List<CaseContractNodeVO> getList(CaseContractNodeSelectQueryParams caseContractNodeSelectQueryParams);

    /** 新增 **/
    void save(CaseContractNodeAddParams caseContractNodeAddParams);

    /** 批量新增 **/
    void save(List<CaseContractNodeAddParams> caseContractNodeAddParams);

    /** 更新 **/
    void update(CaseContractNodeUpdateParams caseContractNodeUpdateParams);

    /** 根据id查询 **/
    CaseContractNodeVO getById(Long id);

    /** 单个删除 **/
    void deleteById(Long caseContractNodeId);

    /** 批量删除 **/
    void batchDelById(Long[] caseContractNodeIds);
}