package com.zs.sys.log.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.log.params.SysLogErrorAddParams;
import com.zs.common.core.log.service.ILogErrorAspectService;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.sys.log.domain.entity.SysLogErrorEntity;
import com.zs.sys.log.domain.params.SysLogErrorQueryParams;
import com.zs.sys.log.domain.vo.SysLogErrorVO;
import com.zs.sys.log.mapper.SysLogErrorMapper;
import com.zs.sys.log.service.ISysLogErrorService;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author zsadmin
 */
@Service
public class SysLogErrorServiceImpl extends ServiceImpl<SysLogErrorMapper, SysLogErrorEntity> implements ISysLogErrorService, ILogErrorAspectService {
    @Override
    public void save(SysLogErrorAddParams sysLogErrorAddParams) {
        SysLogErrorEntity sysLogErrorEntity = BeanUtil.copyProperties(sysLogErrorAddParams, SysLogErrorEntity.class);
        baseMapper.insert(sysLogErrorEntity);
    }


    @NotNull
    @Override
    public PageResult<SysLogErrorVO> page(@NotNull SysLogErrorQueryParams sysLogErrorQueryParams) {

        Page<SysLogErrorEntity> page = new PageInfo<>(sysLogErrorQueryParams);
        IPage<SysLogErrorEntity> iPage = baseMapper.selectPage(page, getWrapper(sysLogErrorQueryParams));

        return new PageResult<>(BeanUtil.copyToList(iPage.getRecords(), SysLogErrorVO.class), page.getTotal(), SysLogErrorVO.class);
    }

    @Nullable
    @Override
    public List<SysLogErrorVO> list(@NotNull SysLogErrorQueryParams sysLogErrorQueryParams) {
        return BeanUtil.copyToList(baseMapper.selectList(getWrapper(sysLogErrorQueryParams)), SysLogErrorVO.class);
    }

    @NotNull
    public LambdaQueryWrapper<SysLogErrorEntity> getWrapper(@NotNull SysLogErrorQueryParams sysLogErrorQueryParams) {
        LambdaQueryWrapper<SysLogErrorEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Strings.isNotEmpty(sysLogErrorQueryParams.getUsername()), SysLogErrorEntity::getUsername, sysLogErrorQueryParams.getUsername())
                .like(Strings.isNotEmpty(sysLogErrorQueryParams.getIpAddress()), SysLogErrorEntity::getIpAddress, sysLogErrorQueryParams.getIpAddress())
                .like(Strings.isNotEmpty(sysLogErrorQueryParams.getModule()), SysLogErrorEntity::getModule, sysLogErrorQueryParams.getModule())
                .like(Strings.isNotEmpty(sysLogErrorQueryParams.getExceptionType()), SysLogErrorEntity::getExceptionType, sysLogErrorQueryParams.getExceptionType())
                .like(Strings.isNotEmpty(sysLogErrorQueryParams.getRequestPath()), SysLogErrorEntity::getRequestPath, sysLogErrorQueryParams.getRequestPath())
                .eq(Strings.isNotEmpty(sysLogErrorQueryParams.getRequestMethod()), SysLogErrorEntity::getRequestMethod, sysLogErrorQueryParams.getRequestMethod())
                .orderByDesc(SysLogErrorEntity::getCreateTime)
        ;

        return wrapper;
    }
}
