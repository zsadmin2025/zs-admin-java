package com.zs.bpm.org;

import java.util.List;

/**
 * 业务组织顶层抽象接口
 * <p>
 * 定义所有组织查询方法，仅接口无需实现。
 * 业务系统需要实现此接口，提供用户、部门、角色、岗位等组织数据的查询能力。
 * </p>
 * 
 * <p>实现说明：</p>
 * <ul>
 *   <li>所有方法返回的用户ID都是业务系统用户ID，非Flowable内置用户ID</li>
 *   <li>实现类需要注入到Spring容器中，由BusinessAssigneeLoader自动调用</li>
 *   <li>实现类需要处理空值和异常情况，返回空集合而非null</li>
 * </ul>
 *
 * @author zsadmin
 * @since 1.0.0
 */
public interface BusinessOrgService {

    /**
     * 根据角色ID批量查询业务用户ID
     * <p>
     * 查询指定角色下的所有用户ID，用于"指定角色"类型的审批人解析。
     * </p>
     *
     * @param roleId 角色ID
     * @return 用户ID集合，已去重且不含空值
     */
    List<String> getUserIdsByRoleId(String roleId);

    /**
     * 根据岗位ID批量查询业务用户ID
     * <p>
     * 查询指定岗位下的所有用户ID，用于"指定岗位"类型的审批人解析。
     * </p>
     *
     * @param postId 岗位ID
     * @return 用户ID集合，已去重且不含空值
     */
    List<String> getUserIdsByPostId(String postId);

    /**
     * 根据部门ID查询部门负责人userId
     * <p>
     * 查询指定部门的负责人用户ID，用于"部门负责人"类型的审批人解析。
     * </p>
     *
     * @param deptId 部门ID
     * @return 部门负责人用户ID，如果未设置返回null
     */
    String getDeptHeadUserId(String deptId);

    /**
     * 根据userId查询直属上级userId
     * <p>
     * 查询指定用户的直属上级用户ID，用于"直属上级"类型的审批人解析。
     * </p>
     *
     * @param userId 用户ID
     * @return 直属上级用户ID，如果未设置返回null
     */
    String getLeaderUserId(String userId);

    /**
     * 查询用户所有角色ID集合
     * <p>
     * 查询指定用户拥有的所有角色ID，用于权限校验和流程条件判断。
     * </p>
     *
     * @param userId 用户ID
     * @return 角色ID集合
     */
    List<String> getRoleIdsByUserId(String userId);

    /**
     * 获取系统兜底审核管理员userId列表
     * <p>
     * 当审批人为空且配置为"转交系统业务审核管理员"时，
     * 返回系统管理员的用户ID列表。
     * </p>
     *
     * @return 管理员用户ID列表
     */
    List<String> getSystemAdminUserIds();
}
