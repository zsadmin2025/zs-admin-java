package com.zs.sys.log.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.log.params.SysLogLoginAddParams;
import com.zs.common.core.page.PageResult;
import com.zs.sys.log.domain.entity.SysLogLoginEntity;
import com.zs.sys.log.domain.params.SysLogLoginQueryParams;
import com.zs.sys.log.domain.vo.SysLogLoginVO;
import jakarta.annotation.Nullable;

import java.util.List;

/**
 * @author zsadmin
 */
public interface ISysLogLoginService extends IService<SysLogLoginEntity> {

    void save(SysLogLoginAddParams sysLogLoginAddParams);

    PageResult<SysLogLoginVO> page(SysLogLoginQueryParams sysLogLoginQueryParams);

    /**
     * 获取登录日志列表
     */
    @Nullable
    List<SysLogLoginVO> list(SysLogLoginQueryParams sysLogLoginQueryParams);

    /**
     * 获取今日登录日志
     */
    @Nullable
    List<SysLogLoginVO> todayList();
}
