package com.zs.common.core.log.service;


import com.zs.common.core.log.params.SysLogOperationAddParams;

/**
 * @author zsadmin
 */
public interface ILogOperationAspectService {
    void save(SysLogOperationAddParams sysLogOperationAddParams);

}
