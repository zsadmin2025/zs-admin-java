package com.zs.sys.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.sys.message.domain.entity.SysMessagesEntity;
import com.zs.sys.message.domain.params.SysMessagesAddParams;
import com.zs.sys.message.domain.params.SysMessagesPageQueryParams;
import com.zs.sys.message.domain.params.SysMessagesSelectQueryParams;
import com.zs.sys.message.domain.params.SysMessagesUpdateParams;
import com.zs.sys.message.domain.vo.SysMessagesVO;

import java.util.List;

/**
 * <p>
 * 消息表 服务类
 * </p>
 *
 * @author zs
 * @since 2025-11-17 09:01:44
 */
public interface SysMessagesService extends IService<SysMessagesEntity> {

    /** 分页 **/
    PageResult<SysMessagesVO> page(SysMessagesPageQueryParams sysMessagesPageQueryParams);

    /** 列表 **/
    List<SysMessagesVO> getList(SysMessagesSelectQueryParams sysMessagesSelectQueryParams);

    /** 新增 **/
    void save(SysMessagesAddParams sysMessagesAddParams);

    /** 更新 **/
    void update(SysMessagesUpdateParams sysMessagesUpdateParams);

    /** 根据id查询 **/
    SysMessagesVO getById(Long id);

    /** 单个删除 **/
    void deleteById(Long sysMessagesId);

    /** 批量删除 **/
    void batchDelById(Long[] sysMessagesIds);

    /** 批量更新已读 **/
    void batchUpdateRead(Long[] ids);
}
