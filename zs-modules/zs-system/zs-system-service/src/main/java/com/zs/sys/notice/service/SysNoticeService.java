package com.zs.sys.notice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.sys.notice.domain.entity.SysNoticeEntity;
import com.zs.sys.notice.domain.params.SysNoticeAddParams;
import com.zs.sys.notice.domain.params.SysNoticeQueryParams;
import com.zs.sys.notice.domain.params.SysNoticeUpdateParams;
import com.zs.sys.notice.domain.vo.SysNoticeVO;

import java.util.List;

/**
 * @author zsadmin
 */
public interface SysNoticeService extends IService<SysNoticeEntity>{

    /**
     * 新增草稿
     * @param sysNoticeAddParams 新增参数
     */
    void save(SysNoticeAddParams sysNoticeAddParams);

    /**
     * 修改草稿
     * @param sysNoticeUpdateParams 修改参数
     */
    void update(SysNoticeUpdateParams sysNoticeUpdateParams);

    /**
     * 删除通知公告
     * @param sysNoticeId 通知公告id
     */
    void delete(Long sysNoticeId);

    /**
     * 获取通知公告
     * @param sysNoticeId 通知公告id
     * @return 通知公告
     */
    SysNoticeVO get(Long sysNoticeId);

    /**
     * 分页查询
     * @param sysNoticeQueryParams 查询参数
     * @return 分页结果
     */
    PageResult<SysNoticeVO> page(SysNoticeQueryParams sysNoticeQueryParams);


    /**
     * 获取最新通知公告
     * @param num 数量
     * @return 最新通知公告
     */
    List<SysNoticeVO>  getLimit(Integer num);

    /**
     * 发布通知公告
     * @param sysNoticeUpdateParams 发布参数
     */
    void release(SysNoticeUpdateParams sysNoticeUpdateParams);

    /**
     * 立即发布通知公告
     * @param sysNoticeAddParams 发布参数
     */
    void releaseImmediately(SysNoticeAddParams sysNoticeAddParams);


    /**
     * 撤销通知公告
     * @param sysNoticeUpdateParams 撤销参数
     */
    void revoke(SysNoticeUpdateParams sysNoticeUpdateParams);

    /**
     * 批量删除
     * @param ids 删除的id
     */
    void batchDelById(Long[] ids);

}
