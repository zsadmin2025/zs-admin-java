package com.zs.sys.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.sys.user.domain.dto.SysUserDeptPostDTO;
import com.zs.sys.user.domain.entity.SysUserDeptPostEntity;
import com.zs.sys.user.domain.params.SysUserDeptPostAddParams;
import com.zs.sys.user.mapper.SysUserDeptPostMapper;
import com.zs.sys.user.service.ISysUserDeptPostService;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zsadmin
 */
@Service
public class SysUserDeptPostServiceImpl extends ServiceImpl<SysUserDeptPostMapper, SysUserDeptPostEntity> implements ISysUserDeptPostService {
    @Override
    public void saveOrUpdate(Long sysUserId, @NotNull List<SysUserDeptPostAddParams> sysDeptIdList) {
        // 先删除用户与部门关系
        baseMapper.delete(new QueryWrapper<SysUserDeptPostEntity>().eq("sys_user_id", sysUserId));
        // 在添加用户与部门关系
        if (!sysDeptIdList.isEmpty()) {
            for (SysUserDeptPostAddParams sysDeptId : sysDeptIdList) {
                SysUserDeptPostEntity sysUserDeptEntity = new SysUserDeptPostEntity();
                sysUserDeptEntity.setSysUserId(sysUserId);
                sysUserDeptEntity.setSysDeptId(sysDeptId.getSysDeptId());
                sysUserDeptEntity.setSysPostId(sysDeptId.getSysPostId());
                baseMapper.insert(sysUserDeptEntity);
            }
        }

    }

    @Nullable
    @Override
    public List<SysUserDeptPostDTO> getByUserId(Long sysUserId) {
        List<SysUserDeptPostEntity> sysUserDeptPostEntityList = baseMapper.selectList(new QueryWrapper<SysUserDeptPostEntity>().eq("sys_user_id", sysUserId));
        return BeanUtil.copyToList(sysUserDeptPostEntityList, SysUserDeptPostDTO.class);
    }

    @Override
    public void delByUserId(Long sysUserId) {
        this.baseMapper.delete(new QueryWrapper<SysUserDeptPostEntity>().eq("sys_user_id", sysUserId));
    }

    @Override
    public Long getByPostId(Long sysPostId) {
        return this.baseMapper.selectCount(new QueryWrapper<SysUserDeptPostEntity>().eq("sys_post_id", sysPostId));
    }
}
