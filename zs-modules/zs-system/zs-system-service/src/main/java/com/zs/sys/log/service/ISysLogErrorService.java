package com.zs.sys.log.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.log.params.SysLogErrorAddParams;
import com.zs.common.core.page.PageResult;
import com.zs.sys.log.domain.entity.SysLogErrorEntity;
import com.zs.sys.log.domain.params.SysLogErrorQueryParams;
import com.zs.sys.log.domain.vo.SysLogErrorVO;
import jakarta.annotation.Nullable;

import java.util.List;


/**
 * @author zsadmin
 */
public interface ISysLogErrorService extends IService<SysLogErrorEntity> {

    void save(SysLogErrorAddParams sysLogErrorAddParams);

    PageResult<SysLogErrorVO> page(SysLogErrorQueryParams sysLogErrorQueryParams);

    /**
     * 获取异常日志列表
     */
    @Nullable
    List<SysLogErrorVO> list(SysLogErrorQueryParams sysLogErrorQueryParams);
}
