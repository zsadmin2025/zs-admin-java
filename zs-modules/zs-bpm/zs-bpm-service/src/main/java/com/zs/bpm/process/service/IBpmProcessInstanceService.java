package com.zs.bpm.process.service;

import com.zs.bpm.process.domain.vo.ProcessInstanceVO;
import com.zs.bpm.task.domain.params.TaskPageQueryParams;
import com.zs.common.core.page.PageResult;
import org.flowable.engine.runtime.ProcessInstance;

import java.util.Map;

/**
 * 流程实例管理 Service 接口
 *
 * @author zsadmin
 */
public interface IBpmProcessInstanceService {


    /**
     * 获取所有流程全部实例（运行中+已结束），含完整审批轨迹
     * <p>
     * 每条记录包含：基本信息、已完成审批节点轨迹（审批人+时间+耗时）、
     * 当前活跃节点（谁还没审批）、审批意见/操作记录。
     * （通常是后台管理员用来查看全盘"进行中"和"已结束"的所有流程列表。）
     *
     * @return 所有流程实例（含审批轨迹）
     */
    PageResult<ProcessInstanceVO> getAllProcessInstance(TaskPageQueryParams params);



    /**
     * 分页查询我的流程
     * （当前登录用户作为“流程发起人”提交的所有流程单据（包括进行中和已结束的）。）
     *
     * @param params 查询参数
     * @return 我的流程分页结果
     */
    PageResult<ProcessInstanceVO> myProcesses(TaskPageQueryParams params);

    /**
     * 获取所有运行中的流程实例
     * （通常是后台管理员用来查看全盘“进行中”的所有流程列表。）
     *
     * @return 所有运行中的流程实例
     */
    PageResult<ProcessInstance> getRunningProcessInstance(TaskPageQueryParams params);



    /**
     * 挂起流程实例
     *
     * @param processInstanceId 流程实例ID
     */
    void suspend(String processInstanceId);

    /**
     * 激活流程实例
     *
     * @param processInstanceId 流程实例ID
     */
    void activate(String processInstanceId);

    /**
     * 终止流程实例
     *
     * @param processInstanceId 流程实例ID
     * @param reason            终止原因
     */
    void terminate(String processInstanceId, String reason);

    /**
     * 获取流程图追踪
     *
     * @param processInstanceId 流程实例ID
     * @return 追踪数据
     */
    Map<String, Object> getTrace(String processInstanceId);

    /**
     * 获取流程变量
     *
     * @param processInstanceId 流程实例ID
     * @return 变量Map
     */
    Map<String, Object> getVariables(String processInstanceId);
}
