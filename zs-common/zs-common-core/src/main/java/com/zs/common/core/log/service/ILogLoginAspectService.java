package com.zs.common.core.log.service;


import com.zs.common.core.log.params.SysLogLoginAddParams;

/**
 * @author zsadmin
 */
public interface ILogLoginAspectService {
    void save(SysLogLoginAddParams sysLogLoginAddParams);

}
