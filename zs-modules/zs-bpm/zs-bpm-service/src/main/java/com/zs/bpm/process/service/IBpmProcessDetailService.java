package com.zs.bpm.process.service;

import com.zs.bpm.task.domain.params.TodoTaskParams;
import com.zs.bpm.task.domain.vo.ProcessDetailVO;

/**
 * BPM 流程详情查询 Service 接口
 * <p>
 * 封装流程详情全景视图（含流程实例基本信息、表单定义与数据、审批节点链、当前待办任务）的查询逻辑。
 * </p>
 *
 * @author zsadmin
 */
public interface IBpmProcessDetailService {

    /**
     * 获取流程详情
     *
     * @param params 查询参数（流程实例ID）
     * @return 流程详情全景视图
     */
    ProcessDetailVO getProcessDetail(TodoTaskParams params);
}
