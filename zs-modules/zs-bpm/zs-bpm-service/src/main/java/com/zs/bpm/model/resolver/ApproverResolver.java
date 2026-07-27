package com.zs.bpm.model.resolver;

import java.util.List;

/**
 * 审批人解析器接口
 * <p>
 * 定义审批人解析的策略模式接口，每种审批人类型对应一个实现类。
 * 通过策略模式实现开闭原则，新增审批人类型只需添加新的实现类。
 * </p>
 * 
 * <p>使用说明：</p>
 * <ul>
 *   <li>实现类必须添加 @Component 注解，由Spring自动注入</li>
 *   <li>getResolverCode() 返回值对应 ApproveSetTypeEnum 枚举值</li>
 *   <li>resolve() 方法负责根据参数解析出审批人用户ID列表</li>
 *   <li>所有实现类必须处理空值和异常情况，返回空集合而非null</li>
 * </ul>
 *
 * @author zsadmin
 * @since 1.0.0
 */
public interface ApproverResolver {

    /**
     * 获取解析器编码，对应 ApproveSetTypeEnum 枚举值
     * 
     * @return 解析器编码
     */
    Integer getResolverCode();

    /**
     * 获取解析器名称
     * 
     * @return 解析器名称
     */
    String getResolverName();

    /**
     * 解析审批人列表
     * 
     * @param param 解析参数（如用户ID列表、角色ID、岗位ID等，逗号分隔）
     * @param initiator 流程发起人用户ID
     * @return 审批人用户ID列表，不会返回null
     */
    List<String> resolve(String param, String initiator);
}