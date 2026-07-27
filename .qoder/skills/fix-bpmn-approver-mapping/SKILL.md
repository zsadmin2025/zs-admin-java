---
name: fix-bpmn-approver-mapping
description: 修复FlowToBpmnConverter中审批人类型的BPMN映射逻辑，确保生成的BPMN20.XML文件能够正确执行。适用于修复Flowable工作流引擎的审批人分配问题、多人审批配置、空审批人策略等场景。
---

# 修复BPMN审批人映射

## 问题背景

当前`FlowToBpmnConverter.java`中的`processUserTask`方法存在以下问题，导致生成的BPMN XML无法正确执行：

### 主要问题

1. **指定用户(settype=1)**: 直接使用`${assignee}`表达式，没有提取具体的用户ID列表
2. **部门负责人(settype=4)**: 使用`${deptManager}`但未处理多个部门的选择
3. **发起人自己(settype=6)**: 只设置isStarter扩展属性，未设置assignee
4. **流程表达式(settype=9)**: 直接使用`${assignee}`，未使用用户定义的表达式
5. **缺少多人审批**: 未实现会签、或签的多实例配置
6. **缺少空审批人策略**: 未处理审批人为空时的自动通过/拒绝逻辑

## 修复指南

### 1. 审批人类型映射修复

```java
case SETTYPE_SPECIFIED_USER: // 指定成员
    JSONArray userList = node.getJSONArray("nodeUserList");
    if (userList != null && !userList.isEmpty()) {
        List<String> userIds = extractIdList(userList);
        if (userIds.size() == 1) {
            userTask.setAssignee(userIds.get(0));
        } else {
            userTask.setCandidateUsers(userIds);
        }
    }
    break;

case SETTYPE_SPECIFIED_ROLE: // 指定角色
case SETTYPE_SPECIFIED_POST: // 指定岗位
    JSONArray groupList = node.getJSONArray(
        settype == SETTYPE_SPECIFIED_ROLE ? "nodeRoleList" : "nodePostList"
    );
    if (groupList != null && !groupList.isEmpty()) {
        userTask.setCandidateGroups(extractIdList(groupList));
    }
    break;

case SETTYPE_DEPT_MANAGER: // 部门负责人
    JSONArray deptList = node.getJSONArray("nodeDeptList");
    if (deptList != null && !deptList.isEmpty()) {
        userTask.setCandidateGroups(extractIdList(deptList));
        // 使用扩展属性存储部门负责人解析表达式
        ExtensionAttribute resolveAttr = new ExtensionAttribute();
        resolveAttr.setName("assigneeExpression");
        resolveAttr.setValue("${resolveDeptManager}");
        resolveAttr.setNamespace("http://flowable.org/bpmn");
        resolveAttr.setNamespacePrefix("flowable");
        userTask.addAttribute(resolveAttr);
    }
    break;

case SETTYPE_STARTER_SELF: // 发起人自己
    userTask.setAssignee("${startUserId}");
    break;

case SETTYPE_EXPRESSION: // 流程表达式
    String expression = node.getStr("expression", "");
    if (StrUtil.isNotBlank(expression)) {
        userTask.setAssignee(expression);
    }
    break;
```

### 2. 多人审批方式配置

```java
private void setupMultiInstance(UserTask userTask, JSONObject node) {
    int approvalType = node.getInt("multipleApprovalType", 1);
    int percent = node.getInt("multipleApprovalPercent", 100);
    
    if (approvalType == 1) {
        // 顺序审批（串行多实例）
        MultiInstanceLoopCharacteristics multiInstance = new MultiInstanceLoopCharacteristics();
        multiInstance.setSequential(true);
        userTask.setLoopCharacteristics(multiInstance);
    } else if (approvalType == 2) {
        // 会签（并行多实例 + 完成条件）
        MultiInstanceLoopCharacteristics multiInstance = new MultiInstanceLoopCharacteristics();
        multiInstance.setSequential(false);
        String completionCondition = 
            "${(nrOfCompletedInstances/nrOfInstances) >= " + (percent/100.0) + "}";
        multiInstance.setCompletionCondition(completionCondition);
        userTask.setLoopCharacteristics(multiInstance);
    } else if (approvalType == 3) {
        // 或签（并行多实例 + 一人通过即可）
        MultiInstanceLoopCharacteristics multiInstance = new MultiInstanceLoopCharacteristics();
        multiInstance.setSequential(false);
        multiInstance.setCompletionCondition("${nrOfCompletedInstances >= 1}");
        userTask.setLoopCharacteristics(multiInstance);
    }
}
```

### 3. 空审批人策略配置

```java
private void setupEmptyApproverStrategy(UserTask userTask, JSONObject node) {
    int strategy = node.getInt("emptyApproverAction", 1);
    
    ExtensionAttribute strategyAttr = new ExtensionAttribute();
    strategyAttr.setName("emptyApproverAction");
    strategyAttr.setValue(String.valueOf(strategy));
    strategyAttr.setNamespace("http://flowable.org/bpmn");
    strategyAttr.setNamespacePrefix("flowable");
    userTask.addAttribute(strategyAttr);
    
    if (strategy == 3 || strategy == 4) {
        String targetUser = node.getStr("emptyApproverUserId", "");
        ExtensionAttribute targetAttr = new ExtensionAttribute();
        targetAttr.setName("emptyApproverUserId");
        targetAttr.setValue(targetUser);
        targetAttr.setNamespace("http://flowable.org/bpmn");
        targetAttr.setNamespacePrefix("flowable");
        userTask.addAttribute(targetAttr);
    }
}
```

## 验证步骤

1. **编译检查**: 确保所有Flowable API调用正确
2. **XML验证**: 使用`BpmnXMLConverter`验证生成的XML格式
3. **流程部署**: 测试流程能否正确部署到Flowable引擎
4. **任务创建**: 验证审批人能否正确创建任务
5. **多实例测试**: 测试会签、或签功能是否正常

## 关键文件

- 主要修改文件: `zs-modules/zs-bpm/zs-bpm-service/src/main/java/com/zs/bpm/model/manager/FlowToBpmnConverter.java`
- 测试文件: `zs-modules/zs-bpm/zs-bpm-service/src/test/java/com/zs/bpm/FlowToBpmnConverterTest.java`
- 常量定义: `zs-modules/zs-bpm/zs-bpm-service/src/main/java/com/zs/bpm/common/constants/BpmConstants.java`

## Flowable API注意事项

1. 使用`setCandidateUsers`设置多个候选人
2. 使用`setCandidateGroups`设置多个候选组
3. 多实例配置使用`MultiInstanceLoopCharacteristics`
4. 完成条件使用JUEL表达式格式
5. 扩展属性必须设置正确的命名空间`http://flowable.org/bpmn`

## 运行时监听器（可选）

对于动态审批人解析，需要创建监听器：

### 部门负责人解析监听器

```java
@Component("resolveDeptManager")
public class DeptManagerResolveListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        // 从candidateGroups获取部门ID
        Set<String> deptIds = delegateTask.getCandidateGroups();
        // 查询部门负责人
        List<String> managers = deptService.getDeptManagers(deptIds);
        // 设置实际审批人
        delegateTask.setAssignee(managers.get(0));
    }
}
```

### 流程变量配置

启动流程时需要设置以下变量：

```java
Map<String, Object> variables = new HashMap<>();
variables.put("startUserId", currentUser.getId());
variables.put("_SKIP_INITIATOR", true); // 跳过发起人节点
processService.startProcess(processKey, variables);
```

## 常见问题

| 问题 | 原因 | 解决方案 |
|------|------|----------|
| 审批人为空 | 表达式未解析 | 检查监听器配置 |
| 多实例不生效 | 缺少candidateUsers | 确保设置了候选人列表 |
| 发起人节点不跳过 | 变量未设置 | 检查`_SKIP_INITIATOR`变量 |