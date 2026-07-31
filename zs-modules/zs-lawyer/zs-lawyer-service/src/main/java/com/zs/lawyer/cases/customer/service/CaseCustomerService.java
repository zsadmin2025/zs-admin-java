package com.zs.lawyer.cases.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.cases.customer.domain.entity.CaseCustomerEntity;
import com.zs.lawyer.cases.customer.domain.params.CaseCustomerAddParams;
import com.zs.lawyer.cases.customer.domain.params.CaseCustomerPageQueryParams;
import com.zs.lawyer.cases.customer.domain.params.CaseCustomerSelectQueryParams;
import com.zs.lawyer.cases.customer.domain.params.CaseCustomerUpdateParams;
import com.zs.lawyer.cases.customer.domain.vo.CaseCustomerVO;

import java.util.List;

/**
 * <p>
 * 案件客户表 服务类
 * </p>
 *
 * @author zs
 * @since 2025-06-08 17:55:28
 */
public interface CaseCustomerService extends IService<CaseCustomerEntity> {

    /** 分页 **/
    PageResult<CaseCustomerVO> page(CaseCustomerPageQueryParams caseCustomerPageQueryParams);

    /** 列表 **/
    List<CaseCustomerVO> getList(CaseCustomerSelectQueryParams caseCustomerSelectQueryParams);

    /** 新增 **/
    void save(CaseCustomerAddParams caseCustomerAddParams);

    /** 更新 **/
    void update(CaseCustomerUpdateParams caseCustomerUpdateParams);


    void saveOrUpdate(CaseCustomerAddParams caseCustomerAddParams);

    /** 根据id查询 **/
    CaseCustomerVO getById(Long id);

    /** 根据案件id查询 **/
    CaseCustomerVO getByCaseInfoId(Long caseInfoId);

    /** 单个删除 **/
    void deleteById(Long caseCustomerId);

    /** 批量删除 **/
    void batchDelById(Long[] caseCustomerIds);
}