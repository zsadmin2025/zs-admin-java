package com.zs.bpm.expression.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.bpm.expression.domain.entity.BpmProcessExpressionEntity;
import com.zs.bpm.expression.domain.params.BpmProcessExpressionQueryParams;
import com.zs.bpm.expression.domain.vo.BpmProcessExpressionVO;
import com.zs.common.core.page.PageResult;

/**
 * 流程表达式 Service 接口
 *
 * @author zsadmin
 */
public interface IBpmProcessExpressionService extends IService<BpmProcessExpressionEntity> {

    /**
     * 分页查询表达式
     *
     * @param queryParams 查询参数
     * @return 分页结果
     */
    PageResult<BpmProcessExpressionVO> pageQuery(BpmProcessExpressionQueryParams queryParams);

    /**
     * 删除表达式
     *
     * @param id 表达式ID
     */
    void deleteById(Long id);

    /**
     * 根据ID获取表达式
     *
     * @param id 表达式ID
     * @return 表达式
     */
    BpmProcessExpressionVO getById(Long id);

}
