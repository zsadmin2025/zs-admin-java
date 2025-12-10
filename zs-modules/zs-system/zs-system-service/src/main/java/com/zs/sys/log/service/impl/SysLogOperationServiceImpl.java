package com.zs.sys.log.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.log.params.SysLogOperationAddParams;
import com.zs.common.core.log.service.ILogOperationAspectService;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.sys.log.domain.entity.SysLogOperationEntity;
import com.zs.sys.log.domain.params.SysLogOperationQueryParams;
import com.zs.sys.log.domain.vo.SysLogOperationVO;
import com.zs.sys.log.mapper.SysLogOperationMapper;
import com.zs.sys.log.service.ISysLogOperationService;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author zsadmin
 */
@Service
public class SysLogOperationServiceImpl extends ServiceImpl<SysLogOperationMapper, SysLogOperationEntity>
        implements ISysLogOperationService, ILogOperationAspectService {
    @Override
    public void save(SysLogOperationAddParams sysLogOperationAddParams) {
        SysLogOperationEntity sysLogOperationEntity = BeanUtil.copyProperties(sysLogOperationAddParams, SysLogOperationEntity.class);
        baseMapper.insert(sysLogOperationEntity);
    }

    @NotNull
    @Override
    public PageResult<SysLogOperationVO> page(@NotNull SysLogOperationQueryParams sysLogOperationQueryParams) {
        Page<SysLogOperationEntity> page = new PageInfo<>(sysLogOperationQueryParams);
        IPage<SysLogOperationEntity> iPage = baseMapper.selectPage(page, getWrapper(sysLogOperationQueryParams));

        return new PageResult<>(BeanUtil.copyToList(iPage.getRecords(), SysLogOperationVO.class), page.getTotal(), SysLogOperationVO.class);
    }

    @Nullable
    @Override
    public List<SysLogOperationVO> list(@NotNull SysLogOperationQueryParams sysLogOperationQueryParams) {
        return BeanUtil.copyToList(baseMapper.selectList(getWrapper(sysLogOperationQueryParams)), SysLogOperationVO.class);
    }

    @NotNull
    public LambdaQueryWrapper<SysLogOperationEntity> getWrapper(@NotNull SysLogOperationQueryParams sysLogOperationQueryParams) {
        LambdaQueryWrapper<SysLogOperationEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Strings.isNotEmpty(sysLogOperationQueryParams.getUsername()), SysLogOperationEntity::getUsername, sysLogOperationQueryParams.getUsername())
                .like(Strings.isNotEmpty(sysLogOperationQueryParams.getIpAddress()), SysLogOperationEntity::getIpAddress, sysLogOperationQueryParams.getIpAddress())
                .like(Strings.isNotEmpty(sysLogOperationQueryParams.getModule()), SysLogOperationEntity::getModule, sysLogOperationQueryParams.getModule())
                .like(Strings.isNotEmpty(sysLogOperationQueryParams.getOperationDescription()), SysLogOperationEntity::getOperationDescription, sysLogOperationQueryParams.getOperationDescription())
                .like(Strings.isNotEmpty(sysLogOperationQueryParams.getOperationType()), SysLogOperationEntity::getOperationType, sysLogOperationQueryParams.getOperationType())
                .eq(Strings.isNotEmpty(sysLogOperationQueryParams.getRequestMethod()), SysLogOperationEntity::getRequestMethod, sysLogOperationQueryParams.getRequestMethod())
                .like(Strings.isNotEmpty(sysLogOperationQueryParams.getRequestPath()), SysLogOperationEntity::getRequestPath, sysLogOperationQueryParams.getRequestPath())
                .eq(Objects.nonNull(sysLogOperationQueryParams.getResponseStatusCode()), SysLogOperationEntity::getResponseStatusCode, sysLogOperationQueryParams.getResponseStatusCode())
                .orderByDesc(SysLogOperationEntity::getCreateTime);

        return wrapper;
    }
}
