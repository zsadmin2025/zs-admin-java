package com.zs.sys.notice.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.sys.notice.domain.entity.SysNoticeFilesEntity;
import com.zs.sys.notice.domain.params.SysNoticeFilesParams;
import com.zs.sys.notice.domain.vo.SysNoticeFilesVO;
import com.zs.sys.notice.mapper.SysNoticeFilesMapper;
import com.zs.sys.notice.service.SysNoticeFilesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 通知公告附件表 服务实现类
 * </p>
 *
 * @author zs
 * @since 2025-10-13 10:54:32
 */
@Service
public class SysNoticeFilesServiceImpl extends ServiceImpl<SysNoticeFilesMapper, SysNoticeFilesEntity> implements SysNoticeFilesService {


    @Override
    public void save(Long sysNoticeId, List<SysNoticeFilesParams> sysNoticeFilesList) {
        // 空集合检查
        if (sysNoticeFilesList.isEmpty()) {
            return;
        }

        // 批量处理避免循环单条插入
        List<SysNoticeFilesEntity> entityList = new ArrayList<>();
        for (SysNoticeFilesParams sysNoticeFilesParams : sysNoticeFilesList) {
            SysNoticeFilesEntity sysNoticeFilesEntity = new SysNoticeFilesEntity();
            sysNoticeFilesEntity.setSysNoticeId(sysNoticeId);
            sysNoticeFilesEntity.setFileName(sysNoticeFilesParams.getFileName());
            sysNoticeFilesEntity.setFileOriginalName(sysNoticeFilesParams.getFileOriginalName());
            sysNoticeFilesEntity.setFileType(sysNoticeFilesParams.getFileType());
            sysNoticeFilesEntity.setFileSize(sysNoticeFilesParams.getFileSize());
            sysNoticeFilesEntity.setFileUrl(sysNoticeFilesParams.getFileUrl());
            sysNoticeFilesEntity.setFilePath(sysNoticeFilesParams.getFilePath());

            entityList.add(sysNoticeFilesEntity);
        }

        this.baseMapper.insert(entityList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Long sysNoticeId, List<SysNoticeFilesParams> sysNoticeFilesList) {
        // 空集合检查
        if (sysNoticeFilesList.isEmpty()) {
            return;
        }

        // 计算哪些需要更新，哪些需要删除
        // 1. 获取旧的ID
        List<SysNoticeFilesEntity> oldMenuList = this.baseMapper.selectList(new LambdaQueryWrapper<SysNoticeFilesEntity>().eq(SysNoticeFilesEntity::getSysNoticeId, sysNoticeId));
        List<Long> oldMenuIdList = oldMenuList.stream().map(SysNoticeFilesEntity::getSysNoticeFilesId).toList();


        // 2. 获取新的ID
        List<Long> newMenuIdList = sysNoticeFilesList.stream().map(SysNoticeFilesParams::getSysNoticeFilesId).toList();

        // 3. 删除旧的ID中，在新的ID中没有的
        List<Long> toDelete = oldMenuIdList.stream().filter(menuId -> !newMenuIdList.contains(menuId)).toList();
        if (!toDelete.isEmpty()) {
            this.baseMapper.deleteByIds(toDelete);
        }

        // 4. 新增新的ID中，在旧的ID中没有的
        List<Long> toAdd = newMenuIdList.stream().filter(menuId -> !oldMenuIdList.contains(menuId)).toList();

        List<SysNoticeFilesParams> toAddFiles = sysNoticeFilesList.stream().filter(menu -> toAdd.contains(menu.getSysNoticeFilesId())).toList();

        if (!toAddFiles.isEmpty()) {
            this.save(sysNoticeId, toAddFiles);
        }

    }

    @Override
    public List<SysNoticeFilesVO> list(Long sysNoticeId) {
        List<SysNoticeFilesEntity> sysNoticeFilesEntityList = this.baseMapper.selectList(new LambdaQueryWrapper<SysNoticeFilesEntity>().eq(SysNoticeFilesEntity::getSysNoticeId, sysNoticeId));
        return BeanUtil.copyToList(sysNoticeFilesEntityList, SysNoticeFilesVO.class);
    }
}
