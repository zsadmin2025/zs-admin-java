package com.zs.sys.notice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.sys.notice.domain.entity.SysNoticeFilesEntity;
import com.zs.sys.notice.domain.params.SysNoticeFilesParams;
import com.zs.sys.notice.domain.vo.SysNoticeFilesVO;

import java.util.List;

/**
 * <p>
 * 通知公告附件表 服务类
 * </p>
 *
 * @author zs
 * @since 2025-10-13 10:54:32
 */
public interface SysNoticeFilesService extends IService<SysNoticeFilesEntity> {

    /**
     * 保存
     * @param sysNoticeId 通知公告ID
     * @param sysNoticeFilesList 通知公告附件列表
     */
    void save(Long sysNoticeId, List<SysNoticeFilesParams> sysNoticeFilesList);


    /**
     * 修改
     * @param sysNoticeId 通知公告ID
     * @param sysNoticeFilesList 通知公告附件列表
     */
    void update(Long sysNoticeId, List<SysNoticeFilesParams> sysNoticeFilesList);

    /**
     * 获取列表
     * @param sysNoticeId 通知公告ID
     */
    List<SysNoticeFilesVO> list(Long sysNoticeId);
}
