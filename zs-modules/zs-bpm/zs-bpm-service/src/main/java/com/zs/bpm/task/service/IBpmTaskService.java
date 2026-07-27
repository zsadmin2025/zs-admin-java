package com.zs.bpm.task.service;

import com.zs.bpm.cc.domain.vo.BpmCcRecordVO;
import com.zs.bpm.task.domain.params.*;
import com.zs.bpm.task.domain.vo.AllTaskVO;
import com.zs.bpm.task.domain.vo.ProcessDetailVO;
import com.zs.bpm.task.domain.vo.ProcessInstanceInfo;
import com.zs.common.core.page.PageResult;
import jakarta.servlet.http.HttpServletResponse;
import org.flowable.task.api.Task;

import java.util.List;

/**
 * BPM 任务管理 Service 接口（新）
 * <p>
 * 使用参数对象封装请求参数，提供类型安全的 VO 返回结果。
 *
 * @author zsadmin
 */
public interface IBpmTaskService {


    /**
     * 分页查询待办流程实例
     * （当前登录用户有待办任务的流程实例，按流程实例去重分页，避免同一流程的任务重复展示）
     *
     * @param params 查询参数
     * @return 待办流程实例分页结果
     */
    PageResult<ProcessInstanceInfo> todoPage(TaskPageQueryParams params);

    /**
     * 分页查询已办任务
     * （当前登录用户曾经审批过、处理过的历史任务。）
     *
     * @param params 查询参数
     * @return 已办任务分页结果
     */
    PageResult<ProcessInstanceInfo> finishedPage(TaskPageQueryParams params);



    /**
     * 分页查询抄送记录
     *
     * @param params 查询参数
     * @return 抄送记录分页结果
     */
    PageResult<BpmCcRecordVO> ccPage(TaskPageQueryParams params);


    /**
     * 分页查询全部任务（待办 + 已办）
     * <p>
     * 用于前端「流程任务」菜单，查询系统内所有任务，不受当前用户限制。
     * 合并运行时待办任务和历史已办任务，按任务开始时间倒序排列。
     * </p>
     *
     * @param params 查询参数
     * @return 全部任务分页结果
     */
    PageResult<AllTaskVO> allTaskPage(AllTaskPageQueryParams params);






    /**
     * 获取所有运行中的任务
     * (管理员查看当前系统里所有正在运行中的审批节点任务，用于转办、催办或异常排查)
     *
     * @return 所有运行中的任务
     */
    PageResult<Task> getAllActiveTasks(TaskPageQueryParams params);


    /**
     * 启动流程
     */

    String startProcess(TaskProcessParams params);

    /**
     * 获取流程详情
     *
     * @param params 查询参数
     * @return 流程详情
     */
    ProcessDetailVO getProcessDetail(TodoTaskParams params);

    /**
     * 根据业务主键查询流程详情
     *
     * @param businessKey 业务主键
     * @return 流程详情
     */
    ProcessDetailVO getProcessDetailByBusinessKey(String businessKey);

    /**
     * 完成任务
      * @param params 完成参数
     */
    void complete(TaskCompleteParams params);



    /**
     * 转办任务
     *
     * @param taskId 任务ID
     * @param userId 目标用户ID
     */
    void transfer(String taskId, String userId);

    /**
     * 委派任务
     *
     * @param taskId 任务ID
     * @param userId 目标用户ID
     */
    void delegateTask(String taskId, String userId);

    /**
     * 委派归还
     *
     * @param taskId 任务ID
     */
    void resolve(String taskId);

    /**
     * 加签
     *
     * @param taskId     任务ID
     * @param userIds    用户ID列表
     * @param sequential true=前加签, false=后加签
     */
    void addSign(String taskId, List<String> userIds, boolean sequential);

    /**
     * 减签
     *
     * @param taskId      任务ID
     * @param executionId 执行实例ID
     */
    void removeSign(String taskId, String executionId);

    /**
     * 撤销流程
     *
     * @param processInstanceId  流程实例ID
     * @param reason 撤销原因
     */
    void cancel(String processInstanceId, String reason);

    /**
     * 抄送
     *
     * @param processInstanceId 流程实例ID
     * @param taskId            任务ID
     * @param ccUserIds         抄送用户ID列表
     * @param title             抄送标题
     */
    void sendCc(String processInstanceId, String taskId, List<Long> ccUserIds, String title);

    /**
     * 获取流程图
     *
     * @param processInstanceId 流程实例ID
     */
    void getProcessDiagram(String processInstanceId, HttpServletResponse response);
}
