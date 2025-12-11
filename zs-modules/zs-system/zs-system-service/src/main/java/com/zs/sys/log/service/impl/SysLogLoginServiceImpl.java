package com.zs.sys.log.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.log.params.SysLogLoginAddParams;
import com.zs.common.core.log.service.ILogLoginAspectService;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.sys.log.domain.entity.SysLogLoginEntity;
import com.zs.sys.log.domain.params.SysLogLoginQueryParams;
import com.zs.sys.log.domain.vo.SysLogLoginVO;
import com.zs.sys.log.mapper.SysLogLoginMapper;
import com.zs.sys.log.service.ISysLogLoginService;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author zsadmin
 */
@Service
public class SysLogLoginServiceImpl extends ServiceImpl<SysLogLoginMapper, SysLogLoginEntity> implements ISysLogLoginService, ILogLoginAspectService {



    @Override
    public void save(SysLogLoginAddParams sysLogLoginAddParams) {
        SysLogLoginEntity sysLogLoginEntity = BeanUtil.copyProperties(sysLogLoginAddParams, SysLogLoginEntity.class);
        baseMapper.insert(sysLogLoginEntity);



    }

    @NotNull
    @Override
    public PageResult<SysLogLoginVO> page(@NotNull SysLogLoginQueryParams sysLogLoginQueryParams) {
        Page<SysLogLoginEntity> page = new PageInfo<>(sysLogLoginQueryParams);
        IPage<SysLogLoginEntity> iPage = baseMapper.selectPage(page, getWrapper(sysLogLoginQueryParams));

        return new PageResult<>(BeanUtil.copyToList(iPage.getRecords(), SysLogLoginVO.class), page.getTotal(), SysLogLoginVO.class);
    }

    @Nullable
    @Override
    public List<SysLogLoginVO> list(@NotNull SysLogLoginQueryParams sysLogLoginQueryParams) {
        List<SysLogLoginEntity> entities = baseMapper.selectList(getWrapper(sysLogLoginQueryParams));
        return BeanUtil.copyToList(entities, SysLogLoginVO.class);
    }

    @Nullable
    @Override
    public List<SysLogLoginVO> todayList() {
        List<SysLogLoginEntity> entities = baseMapper.selectList(new LambdaQueryWrapper<SysLogLoginEntity>()
                .between(SysLogLoginEntity::getLoginTime, DateUtil.beginOfDay(new Date()), DateUtil.endOfDay(new Date()))
                .orderByDesc(SysLogLoginEntity::getLoginTime));
           return BeanUtil.copyToList(entities, SysLogLoginVO.class);
    }

    @NotNull
    public LambdaQueryWrapper<SysLogLoginEntity> getWrapper(@NotNull SysLogLoginQueryParams sysLogLoginQueryParams) {
        LambdaQueryWrapper<SysLogLoginEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Strings.isNotEmpty(sysLogLoginQueryParams.getUsername()), SysLogLoginEntity::getUsername, sysLogLoginQueryParams.getUsername())
                .eq(Strings.isNotEmpty(sysLogLoginQueryParams.getIpAddress()), SysLogLoginEntity::getIpAddress, sysLogLoginQueryParams.getIpAddress())
                .eq(Strings.isNotEmpty(sysLogLoginQueryParams.getCity()), SysLogLoginEntity::getCity, sysLogLoginQueryParams.getCity())
                .eq(Objects.nonNull(sysLogLoginQueryParams.getLoginStatus()), SysLogLoginEntity::getLoginStatus, sysLogLoginQueryParams.getLoginStatus())
                .eq(Strings.isNotEmpty(sysLogLoginQueryParams.getBrowser()), SysLogLoginEntity::getBrowser, sysLogLoginQueryParams.getBrowser())
                .eq(Strings.isNotEmpty(sysLogLoginQueryParams.getOs()), SysLogLoginEntity::getOs, sysLogLoginQueryParams.getOs());

        return wrapper;
    }

}
