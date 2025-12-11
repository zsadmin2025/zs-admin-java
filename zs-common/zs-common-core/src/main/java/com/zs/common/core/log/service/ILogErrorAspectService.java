package com.zs.common.core.log.service;


import com.zs.common.core.log.params.SysLogErrorAddParams;

/**
 * @author zsadmin
 */
public interface ILogErrorAspectService {
    void save(SysLogErrorAddParams sysLogErrorAddParams);

}
