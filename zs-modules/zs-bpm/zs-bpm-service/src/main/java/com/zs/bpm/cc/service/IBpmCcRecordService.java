package com.zs.bpm.cc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.bpm.cc.domain.entity.BpmCcRecordEntity;
import com.zs.common.core.page.PageResult;

import java.util.Set;

/**
 * 抄送记录 Service 接口
 *
 * @author zsadmin
 */
public interface IBpmCcRecordService extends IService<BpmCcRecordEntity> {

    /**
     * 分页查询抄送列表
     *
     * @param userId   用户ID
     * @param current  当前页
     * @param pageSize 每页条数
     * @return 分页结果
     */
    PageResult<BpmCcRecordEntity> pageCcList(Long userId, int current, int pageSize);

    /**
     * 分页查询抄送列表（支持流程实例ID过滤）
     *
     * @param userId             用户ID
     * @param processInstanceIds 流程实例ID集合（为空则不过滤）
     * @param current            当前页
     * @param pageSize           每页条数
     * @return 分页结果
     */
    PageResult<BpmCcRecordEntity> pageCcList(Long userId, Set<String> processInstanceIds, int current, int pageSize);

    /**
     * 获取未读抄送数量
     *
     * @param userId 用户ID
     * @return 未读数
     */
    long getUnreadCount(Long userId);

    /**
     * 标记为已读
     *
     * @param id 抄送记录ID
     */
    void markAsRead(Long id);
}
