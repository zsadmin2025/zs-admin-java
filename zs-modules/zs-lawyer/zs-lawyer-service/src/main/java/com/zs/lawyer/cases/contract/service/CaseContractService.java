package com.zs.lawyer.cases.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.cases.contract.domain.entity.CaseContractEntity;
import com.zs.lawyer.cases.contract.domain.params.CaseContractAddParams;
import com.zs.lawyer.cases.contract.domain.params.CaseContractPageQueryParams;
import com.zs.lawyer.cases.contract.domain.params.CaseContractSelectQueryParams;
import com.zs.lawyer.cases.contract.domain.params.CaseContractUpdateParams;
import com.zs.lawyer.cases.contract.domain.vo.CaseContractVO;

import java.util.List;

/**
 * <p>
 * 案件合同 服务类
 * </p>
 *
 * @author zs
 * @since 2025-06-08 18:02:46
 */
public interface CaseContractService extends IService<CaseContractEntity> {

    /** 分页 **/
    PageResult<CaseContractVO> page(CaseContractPageQueryParams caseContractPageQueryParams);

    /** 列表 **/
    List<CaseContractVO> getList(CaseContractSelectQueryParams caseContractSelectQueryParams);

    /** 新增 **/
    void save(CaseContractAddParams caseContractAddParams);

    /** 更新 **/
    void update(CaseContractUpdateParams caseContractUpdateParams);

    /** 根据id查询 **/
    CaseContractVO getById(Long id);

    CaseContractVO getByCaseInfoId(Long caseInfoId);

    /** 单个删除 **/
    void deleteById(Long caseContractId);

    /** 批量删除 **/
    void batchDelById(Long[] caseContractIds);
}