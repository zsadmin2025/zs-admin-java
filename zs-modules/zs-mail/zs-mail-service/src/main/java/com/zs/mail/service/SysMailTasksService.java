package com.zs.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.mail.domain.entity.SysMailTasksEntity;
import com.zs.mail.domain.params.SysMailTasksAddParams;
import com.zs.mail.domain.params.SysMailTasksPageQueryParams;
import com.zs.mail.domain.params.SysMailTasksSelectQueryParams;
import com.zs.mail.domain.params.SysMailTasksUpdateParams;
import com.zs.mail.domain.vo.SysMailTasksVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zs
 * @since 2025-10-30 11:13:31
 */
public interface SysMailTasksService extends IService<SysMailTasksEntity> {

    /**
     * 分页
     * @param sysMailTasksPageQueryParams 查询参数
     * @return 分页结果
     */
    PageResult<SysMailTasksVO> page(SysMailTasksPageQueryParams sysMailTasksPageQueryParams);

    /**
     * 列表
     * @param sysMailTasksSelectQueryParams 查询参数
     * @return 列表结果
     */
    List<SysMailTasksVO> getList(SysMailTasksSelectQueryParams sysMailTasksSelectQueryParams);

    /**
     * 新增
     * @param sysMailTasksAddParams 新增参数
     */
    void save(SysMailTasksAddParams sysMailTasksAddParams);

    /**
     * 修改
     * @param sysMailTasksUpdateParams 修改参数
     */
    void update(SysMailTasksUpdateParams sysMailTasksUpdateParams);

    /**
     * 根据ID查询
     * @param id  ID
     * @return SysMailTasksVO
     */
    SysMailTasksVO getById(Long id);

    /**
     * 删除
     * @param sysMailTasksId 邮件任务id
     */
    void deleteById(Long sysMailTasksId);

    /**
     * 批量删除
     * @param sysMailTasksIds 邮件任务id集合
     */
    void batchDelById(Long[] sysMailTasksIds);

    /**
     * 发送邮件
     * @param sysMailTasksId 邮件任务id
     */
    void send(Long sysMailTasksId);

    /**
     * 立即发送
     * @param sysMailTasksAddParams 邮件参数
     */
    void sendNow(SysMailTasksAddParams sysMailTasksAddParams);
}

