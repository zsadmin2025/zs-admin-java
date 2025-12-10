package com.zs.sys.notice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.sys.notice.domain.entity.SysNoticeDetailsEntity;
import com.zs.sys.notice.domain.vo.SysNoticeDetailsVO;

import java.util.List;

/**
 * @author zsadmin
 */
public interface SysNoticeDetailsService extends IService<SysNoticeDetailsEntity> {

    /**
     * 保存
     *
     * @param receiverIds 接收人ID
     * @param sysNoticeId 通知公告ID
     */
    void save(List<Long> receiverIds, Long sysNoticeId);

    /**
     * 修改
     *
     * @param receiverIds 接收人ID
     * @param sysNoticeId 通知公告ID
     */
    void update(List<Long> receiverIds, Long sysNoticeId);

    /**
     * 获取列表
     *
     * @param sysNoticeId 通知公告ID
     * @return List<SysNoticeDetailsVO> 列表
     */
    List<SysNoticeDetailsVO> list(Long sysNoticeId);

    /**
     * 删除
     *
     * @param noticeId 通知公告ID
     */
    void removeByNoticeId(Long noticeId);
}
