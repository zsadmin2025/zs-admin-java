package com.zs.sys.notice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.sys.notice.domain.entity.SysNoticeDetailsEntity;
import com.zs.sys.notice.domain.vo.SysNoticeDetailsVO;
import com.zs.sys.notice.mapper.SysNoticeDetailsMapper;
import com.zs.sys.notice.service.SysNoticeDetailsService;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zsadmin
 */
@Service
public class SysNoticeDetailsServiceImpl extends ServiceImpl<SysNoticeDetailsMapper, SysNoticeDetailsEntity> implements SysNoticeDetailsService {
    @Override
    public void save(@NotNull List<Long> receiverIds, Long sysNoticeId) {
        receiverIds.forEach(receiverId -> {
            SysNoticeDetailsEntity sysNoticeDetailsEntity = new SysNoticeDetailsEntity();
            sysNoticeDetailsEntity.setSysNoticeId(sysNoticeId);
            sysNoticeDetailsEntity.setReceiverId(receiverId);
            this.baseMapper.insert(sysNoticeDetailsEntity);
        });
    }

    @Override
    public void update(@NotNull List<Long> receiverIds, Long sysNoticeId) {
        this.baseMapper.update(new LambdaQueryWrapper<SysNoticeDetailsEntity>().eq(SysNoticeDetailsEntity::getSysNoticeId, sysNoticeId));
    }

    @Nullable
    @Override
    public List<SysNoticeDetailsVO> list(Long sysNoticeId) {
        return BeanUtil.copyToList(this.baseMapper.list(sysNoticeId), SysNoticeDetailsVO.class);
    }

    @Override
    public void removeByNoticeId(Long noticeId) {
        this.baseMapper.delete(new LambdaQueryWrapper<SysNoticeDetailsEntity>().eq(SysNoticeDetailsEntity::getSysNoticeId, noticeId));
    }
}
