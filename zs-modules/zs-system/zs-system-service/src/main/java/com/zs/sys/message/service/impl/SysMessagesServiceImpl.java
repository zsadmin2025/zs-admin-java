package com.zs.sys.message.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.common.core.utils.SecurityUtil;
import com.zs.sys.message.domain.entity.SysMessagesEntity;
import com.zs.sys.message.domain.params.SysMessagesAddParams;
import com.zs.sys.message.domain.params.SysMessagesPageQueryParams;
import com.zs.sys.message.domain.params.SysMessagesSelectQueryParams;
import com.zs.sys.message.domain.params.SysMessagesUpdateParams;
import com.zs.sys.message.domain.vo.SysMessagesVO;
import com.zs.sys.message.mapper.SysMessagesMapper;
import com.zs.sys.message.service.SysMessagesService;
import com.zs.sys.user.domain.vo.SysUserVO;
import com.zs.sys.user.service.ISysUserService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import websocket.WebsocketApi;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 消息表 服务实现类
 * </p>
 *
 * @author zs
 * @since 2025-11-17 09:01:44
 */
@Service
public class SysMessagesServiceImpl extends ServiceImpl<SysMessagesMapper, SysMessagesEntity> implements SysMessagesService {

    @Resource
    private ISysUserService sysUserService;
    @Resource
    private WebsocketApi websocketApi;

    @Override
    public PageResult<SysMessagesVO> page(@NotNull SysMessagesPageQueryParams sysMessagesPageQueryParams) {

        Page<SysMessagesEntity> page = new PageInfo<>(sysMessagesPageQueryParams);
        LambdaQueryWrapper<SysMessagesEntity> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(SysMessagesEntity::getReceiverId, SecurityUtil.getUserId());
        wrapper.orderByDesc(SysMessagesEntity::getCreateTime);

        IPage<SysMessagesEntity> iPage = baseMapper.selectPage(page, wrapper);
        List<SysMessagesVO> list = BeanUtil.copyToList(iPage.getRecords(), SysMessagesVO.class);

        List<Long> senderIds = list.stream().map(SysMessagesVO::getSenderId).toList();
        // 将List<Long>改变成Long[]
        Long[] senderIdsArray = senderIds.toArray(new Long[0]);

        List<SysUserVO> senderUsers = sysUserService.getUserList(senderIdsArray);


        list.forEach(message -> {
            for (SysUserVO senderUser : senderUsers) {
                if (message.getSenderId().equals(senderUser.getSysUserId())) {
                    message.setSenderUser(senderUser);
                }
            }
        });

        return new PageResult<>(list, page.getTotal(), SysMessagesVO.class);
    }

    @Override
    public List<SysMessagesVO> getList(@NotNull SysMessagesSelectQueryParams sysMessagesSelectQueryParams) {
        LambdaQueryWrapper<SysMessagesEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMessagesEntity::getReceiverId, SecurityUtil.getUserId());
        wrapper.orderByDesc(SysMessagesEntity::getCreateTime);
        List<SysMessagesVO> list = BeanUtil.copyToList(baseMapper.selectList(wrapper), SysMessagesVO.class);

        List<Long> senderIds = list.stream().map(SysMessagesVO::getSenderId).toList();

        // 将List<Long>改变成Long[]
        Long[] senderIdsArray = senderIds.toArray(new Long[0]);

        List<SysUserVO> senderUsers = sysUserService.getUserList(senderIdsArray);


        list.forEach(message -> {
            for (SysUserVO senderUser : senderUsers) {
                if (message.getSenderId().equals(senderUser.getSysUserId())) {
                    message.setSenderUser(senderUser);
                }
            }
        });

        return list;

    }

    @Override
    public void save(@NotNull SysMessagesAddParams sysMessagesAddParams) {
        SysMessagesEntity sysMessagesEntity = BeanUtil.copyProperties(sysMessagesAddParams, SysMessagesEntity.class);
        sysMessagesEntity.setSenderId(SecurityUtil.getUserId());
        sysMessagesEntity.setSenderName(SecurityUtil.getRealName());
        baseMapper.insert(sysMessagesEntity);

        websocketApi.sendMessage(sysMessagesAddParams.getContent());
    }

    @Override
    public void update(@NotNull SysMessagesUpdateParams sysMessagesUpdateParams) {
        SysMessagesEntity sysMessagesEntity = BeanUtil.copyProperties(sysMessagesUpdateParams, SysMessagesEntity.class);
        baseMapper.updateById(sysMessagesEntity);

        websocketApi.sendMessage(sysMessagesUpdateParams.getContent());
    }

    @Override
    public SysMessagesVO getById(Long id) {
        return BeanUtil.copyProperties(baseMapper.selectById(id), SysMessagesVO.class);
    }

    @Override
    public void deleteById(Long id) {
        baseMapper.deleteById(id);
    }

    @Override
    public void batchDelById(@NotNull Long[] ids) {
        baseMapper.deleteByIds(Arrays.asList(ids));
    }

    @Override
    public void batchUpdateRead(Long[] ids) {
        LambdaUpdateWrapper<SysMessagesEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(SysMessagesEntity::getIsRead, 1);
        updateWrapper.set(SysMessagesEntity::getReadTime, new Date());
        updateWrapper.in(SysMessagesEntity::getSysMessageId, Arrays.asList(ids));

        this.baseMapper.update(updateWrapper);
    }
}
