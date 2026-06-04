package controller;

import jakarta.annotation.Resource;
import org.flowable.engine.*;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.api.Task;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bpm")
public class BpmController {

    // 流程仓库服务：部署、查询流程
    @Resource
    private RepositoryService repositoryService;

    // 运行时服务：启动流程、任务推进
    @Resource
    private RuntimeService runtimeService;

    // 任务服务：待办、完成任务
    @Resource
    private TaskService taskService;

    // 历史服务：管理历史数据，如已完成的流程实例、审批轨迹
    @Resource
    private HistoryService historyService;

    // 身份服务：用户、组、权限
    @Resource
    private IdentityService identityService;

    // 1. 部署流程（从 classpath:/bpmn/）
    @PostMapping("/deploy")
    public String deploy() {
        return repositoryService.createDeployment()
                .addClasspathResource("bpmn/leave_approval.bpmn20.xml")
                .name("请假审批")
                .deploy().getId();
    }

    // 2. 启动流程（用 Key）
    @PostMapping("/start/{key}")
    public String start(@PathVariable String key, @RequestBody Map<String, Object> vars) {
        return runtimeService.startProcessInstanceByKey(key, vars).getId();
    }

    // 3. 查询待办任务
    @GetMapping("/tasks/{assignee}")
    public List<Task> getTasks(@PathVariable String assignee) {
        return taskService.createTaskQuery().taskAssignee(assignee).list();
    }

    // 4. 完成任务
    @PostMapping("/complete/{taskId}")
    public void complete(@PathVariable String taskId, @RequestBody Map<String, Object> vars) {
        taskService.complete(taskId, vars);
    }

    /***
     * 查询所有的流程实例
     **/
    @GetMapping("/queryAllDeployedProcesses")
    public void queryAllDeployedProcesses() {

        // 查询所有流程定义
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .orderByProcessDefinitionKey().asc() // 按流程定义的 Key 排序
                .latestVersion() // 只查询每个流程定义的最新版本
                .list();

        // 打印所有已部署的流程的 key 和 name
        for (ProcessDefinition processDefinition : processDefinitions) {
            System.out.println("Process ID: " + processDefinition.getId());
            System.out.println("Process Key: " + processDefinition.getKey());
            System.out.println("Process Name: " + processDefinition.getName());
            System.out.println("Process Version: " + processDefinition.getVersion());


        }

    }


}
