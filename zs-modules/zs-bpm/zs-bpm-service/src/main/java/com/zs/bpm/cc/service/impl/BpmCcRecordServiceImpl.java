package com.zs.bpm.cc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.bpm.cc.domain.entity.BpmCcRecordEntity;
import com.zs.bpm.cc.mapper.BpmCcRecordMapper;
import com.zs.bpm.cc.service.IBpmCcRecordService;
import com.zs.common.core.page.PageResult;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 抄送记录 Service 实现
 *
 * @author zsadmin
 */
@Service
public class BpmCcRecordServiceImpl extends ServiceImpl<BpmCcRecordMapper, BpmCcRecordEntity> implements IBpmCcRecordService {

    @Resource
    private BpmCcRecordMapper baseMapper;

    @Override
    public PageResult<BpmCcRecordEntity> pageCcList(Long userId, int current, int pageSize) {
        return pageCcList(userId, null, current, pageSize);
    }

    @Override
    public PageResult<BpmCcRecordEntity> pageCcList(Long userId, Set<String> processInstanceIds, int current, int pageSize) {
        Page<BpmCcRecordEntity> page = new Page<>(current, pageSize);
        LambdaQueryWrapper<BpmCcRecordEntity> wrapper = new LambdaQueryWrapper<BpmCcRecordEntity>()
                .eq(BpmCcRecordEntity::getUserId, userId);
        if (processInstanceIds != null && !processInstanceIds.isEmpty()) {
            wrapper.in(BpmCcRecordEntity::getProcessInstanceId, processInstanceIds);
        }
        wrapper.orderByDesc(BpmCcRecordEntity::getCreateTime);
        Page<BpmCcRecordEntity> result = baseMapper.selectPage(page, wrapper);
        return new PageResult<>(result);
    }

    @Override
    public long getUnreadCount(Long userId) {
        return baseMapper.selectCount(new LambdaQueryWrapper<BpmCcRecordEntity>()
                .eq(BpmCcRecordEntity::getUserId, userId)
                .eq(BpmCcRecordEntity::getIsRead, 0));
    }

    @Override
    public void markAsRead(Long id) {
        BpmCcRecordEntity entity = baseMapper.selectById(id);
        if (entity != null) {
            entity.setIsRead(1);
            entity.setReadTime(java.time.LocalDateTime.now().toString());
            baseMapper.updateById(entity);
        }
    }
}
