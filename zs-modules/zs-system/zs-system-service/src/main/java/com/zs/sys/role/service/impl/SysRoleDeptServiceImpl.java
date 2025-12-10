package com.zs.sys.role.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.sys.role.domain.entity.SysRoleDeptEntity;
import com.zs.sys.role.mapper.SysRoleDeptMapper;
import com.zs.sys.role.service.ISysRoleDeptService;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysRoleDeptServiceImpl extends ServiceImpl<SysRoleDeptMapper, SysRoleDeptEntity> implements ISysRoleDeptService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Long sysRoleId, @NotNull List<Long> deptIds) {
        // 先删除角色对应的部门关系

        this.baseMapper.delete(new LambdaQueryWrapper<SysRoleDeptEntity>().eq(SysRoleDeptEntity::getSysRoleId, sysRoleId));

        // 再保存新的关系
        deptIds.forEach(deptId -> {
            SysRoleDeptEntity sysRoleDeptEntity = new SysRoleDeptEntity();
            sysRoleDeptEntity.setSysRoleId(sysRoleId);
            sysRoleDeptEntity.setSysDeptId(deptId);
            this.baseMapper.insert(sysRoleDeptEntity);
        });
    }

    @NotNull
    @Override
    public List<Long> getDeptIds(Long sysRoleId) {
        List<SysRoleDeptEntity> sysRoleDeptEntityList = this.baseMapper.selectList(new LambdaQueryWrapper<SysRoleDeptEntity>().eq(SysRoleDeptEntity::getSysRoleId, sysRoleId));
        return sysRoleDeptEntityList.stream().map(SysRoleDeptEntity::getSysDeptId).collect(Collectors.toList());
    }
}
