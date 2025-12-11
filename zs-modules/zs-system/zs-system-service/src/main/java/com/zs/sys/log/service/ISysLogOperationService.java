package com.zs.sys.log.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.log.params.SysLogOperationAddParams;
import com.zs.common.core.page.PageResult;
import com.zs.sys.log.domain.entity.SysLogOperationEntity;
import com.zs.sys.log.domain.params.SysLogOperationQueryParams;
import com.zs.sys.log.domain.vo.SysLogOperationVO;
import jakarta.annotation.Nullable;

import java.util.List;

/**
 * @author zsadmin
 */
public interface ISysLogOperationService extends IService<SysLogOperationEntity> {

    void save(SysLogOperationAddParams sysLogOperationAddParams);

    PageResult<SysLogOperationVO> page(SysLogOperationQueryParams sysLogOperationQueryParams);

    /**
     * 获取操作日志集合
     */
    @Nullable
    List<SysLogOperationVO> list(SysLogOperationQueryParams sysLogOperationQueryParams);
}
