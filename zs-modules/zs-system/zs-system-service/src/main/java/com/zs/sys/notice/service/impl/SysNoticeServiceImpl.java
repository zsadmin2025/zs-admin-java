package com.zs.sys.notice.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.exception.ZsException;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.sys.notice.domain.entity.SysNoticeEntity;
import com.zs.sys.notice.domain.params.SysNoticeAddParams;
import com.zs.sys.notice.domain.params.SysNoticeQueryParams;
import com.zs.sys.notice.domain.params.SysNoticeUpdateParams;
import com.zs.sys.notice.domain.vo.SysNoticeDetailsVO;
import com.zs.sys.notice.domain.vo.SysNoticeFilesVO;
import com.zs.sys.notice.domain.vo.SysNoticeVO;
import com.zs.sys.notice.mapper.SysNoticeMapper;
import com.zs.sys.notice.service.SysNoticeDetailsService;
import com.zs.sys.notice.service.SysNoticeFilesService;
import com.zs.sys.notice.service.SysNoticeService;
import com.zs.sys.user.domain.params.SysUserQueryParams;
import com.zs.sys.user.domain.vo.SysUserVO;
import com.zs.sys.user.service.ISysUserRoleService;
import com.zs.sys.user.service.ISysUserService;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author zsadmin
 */
@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNoticeEntity> implements SysNoticeService {

    @Resource
    private SysNoticeDetailsService sysNoticeDetailsService;
    @Resource
    private SysNoticeFilesService sysNoticeFilesService;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysUserRoleService iSysUserRoleService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(@NotNull SysNoticeAddParams sysNoticeAddParams) {
        SysNoticeEntity sysNoticeEntity = BeanUtil.copyProperties(sysNoticeAddParams, SysNoticeEntity.class);


        this.baseMapper.insert(sysNoticeEntity);

        // 保存通知公告附件
        if (!sysNoticeAddParams.getFiles().isEmpty()) {
            sysNoticeFilesService.save(sysNoticeEntity.getSysNoticeId(), sysNoticeAddParams.getFiles());
        }

        if (!sysNoticeAddParams.getReceiverIds().isEmpty()) {
            //保存通知公告详情
            List<Long> userIds = resolveReceiverUserIds(sysNoticeAddParams.getReceivingType(), sysNoticeAddParams.getReceiverIds());
            sysNoticeDetailsService.save(userIds, sysNoticeEntity.getSysNoticeId());

        }
    }



    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(@NotNull SysNoticeUpdateParams sysNoticeUpdateParams) {
        SysNoticeEntity sysNoticeEntity = BeanUtil.copyProperties(sysNoticeUpdateParams, SysNoticeEntity.class);
        this.baseMapper.updateById(sysNoticeEntity);

        // 修改通知公告附件
        if (!sysNoticeUpdateParams.getFiles().isEmpty()) {
            sysNoticeFilesService.update(sysNoticeEntity.getSysNoticeId(), sysNoticeUpdateParams.getFiles());
        }
        // 修改接收人：先删除旧的，再保存新的
        if (!sysNoticeUpdateParams.getReceiverIds().isEmpty()) {
            Long noticeId = sysNoticeEntity.getSysNoticeId();
            // ⚠️ 关键：先删除旧的接收人记录
            sysNoticeDetailsService.removeByNoticeId(noticeId);
            //保存通知公告详情
            List<Long> userIds = resolveReceiverUserIds(sysNoticeUpdateParams.getReceivingType(), sysNoticeUpdateParams.getReceiverIds());
            sysNoticeDetailsService.save(userIds, noticeId);
        }
    }


    /**
     * 根据接收类型和ID列表，解析出对应的用户ID列表
     * @param receivingType 接收类型 1:全部用户 2:指定用户 3:角色 4:部门 5:岗位
     */
    private List<Long> resolveReceiverUserIds(Integer receivingType, List<Long> receiverIds) {
        return switch (receivingType) {
            case 1 -> sysUserService.list(new SysUserQueryParams())
                    .stream()
                    .map(SysUserVO::getSysUserId)
                    .toList();

            case 2 -> new ArrayList<>(receiverIds); // 指定用户，直接返回

            case 3 -> iSysUserRoleService.queryUserIdList(receiverIds);

            case 4 -> sysUserService.getUserListByDeptId(receiverIds)
                    .stream()
                    .map(SysUserVO::getSysUserId)
                    .toList();

            case 5 -> sysUserService.getUserListByPostId(receiverIds)
                    .stream()
                    .map(SysUserVO::getSysUserId)
                    .toList();

            default -> Collections.emptyList();
        };
    }
    @Override
    public void delete(Long sysNoticeId) {
        SysNoticeEntity sysNoticeEntity = this.baseMapper.selectById(sysNoticeId);
        if (sysNoticeEntity == null) {
            throw new ZsException("通知公告不存在");
        }
        if (sysNoticeEntity.getStatus() == 2) {
            throw new ZsException("已发布的通知公告不能删除");
        }
        this.baseMapper.deleteById(sysNoticeId);
    }

    @NotNull
    @Override
    public SysNoticeVO get(Long sysNoticeId) {
        SysNoticeEntity sysNoticeEntity = this.baseMapper.get(sysNoticeId);
        SysNoticeVO sysNoticeVO = BeanUtil.copyProperties(sysNoticeEntity, SysNoticeVO.class);

        List<SysNoticeFilesVO> sysNoticeFilesVOs = sysNoticeFilesService.list(sysNoticeId);
        if (!sysNoticeFilesVOs.isEmpty()) {
            sysNoticeVO.setFiles(sysNoticeFilesVOs);
        }

        List<SysNoticeDetailsVO> sysNoticeDetailsVOs = sysNoticeDetailsService.list(sysNoticeId);
        if (!sysNoticeDetailsVOs.isEmpty()) {
            sysNoticeVO.setSysNoticeDetailsVOs(sysNoticeDetailsVOs);
        }

        return sysNoticeVO;
    }

    @NotNull
    @Override
    public PageResult<SysNoticeVO> page(@NotNull SysNoticeQueryParams sysNoticeQueryParams) {
        Page<SysNoticeEntity> pageResult = new PageInfo<>(sysNoticeQueryParams);
        Map<String, Object> params = BeanUtil.beanToMap(sysNoticeQueryParams);
        IPage<SysNoticeEntity> page = this.baseMapper.page(pageResult, params);
        List<SysNoticeVO> list = BeanUtil.copyToList(page.getRecords(), SysNoticeVO.class);
        return new PageResult<>(list, page.getTotal());
    }

    @Nullable
    @Override
    public List<SysNoticeVO> getLimit(Integer num) {
        List<SysNoticeEntity> list =  this.baseMapper.selectList(new LambdaQueryWrapper<SysNoticeEntity>()
                                    .eq(SysNoticeEntity::getStatus, 2)
                                    .orderByDesc(SysNoticeEntity::getReleaseTime)
                                    .last("limit " + num));
        return BeanUtil.copyToList(list, SysNoticeVO.class);
    }

    @Override
    public void release(SysNoticeUpdateParams sysNoticeUpdateParams) {
        SysNoticeEntity sysNoticeEntity = BeanUtil.copyProperties(sysNoticeUpdateParams, SysNoticeEntity.class);
        sysNoticeEntity.setStatus(2);
        sysNoticeEntity.setReleaseTime(DateUtil.now());

        this.baseMapper.updateById(sysNoticeEntity);
    }

    @Override
    public void releaseImmediately(SysNoticeAddParams sysNoticeAddParams) {
        SysNoticeEntity sysNoticeEntity = BeanUtil.copyProperties(sysNoticeAddParams, SysNoticeEntity.class);
        sysNoticeEntity.setStatus(2);
        sysNoticeEntity.setReleaseTime(DateUtil.now());

        // 保存通知公告附件
        if (!sysNoticeAddParams.getFiles().isEmpty()) {
            sysNoticeFilesService.save(sysNoticeEntity.getSysNoticeId(), sysNoticeAddParams.getFiles());
        }
        this.baseMapper.insert(sysNoticeEntity);
        if (!sysNoticeAddParams.getReceiverIds().isEmpty()) {
            //保存通知公告详情
            sysNoticeDetailsService.save(sysNoticeAddParams.getReceiverIds(), sysNoticeEntity.getSysNoticeId());
        }
    }

    @Override
    public void revoke(SysNoticeUpdateParams sysNoticeUpdateParams) {
        SysNoticeEntity sysNoticeEntity = BeanUtil.copyProperties(sysNoticeUpdateParams, SysNoticeEntity.class);
        sysNoticeEntity.setReleaseTime(null);
        sysNoticeEntity.setStatus(0);

        this.baseMapper.updateById(sysNoticeEntity);
    }

    @Override
    public void batchDelById(Long[] ids) {

        this.baseMapper.deleteByIds(Arrays.asList(ids));
    }

}
