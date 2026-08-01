package com.zs.sys.api.role;

import java.util.List;
import java.util.Map;

/**
 * 远程用户服务接口，供其他模块通过 API 层获取用户信息
 *
 * @author zs
 */
public interface RemoteUserService {

    /**
     * 根据用户ID列表批量查询用户ID到姓名的映射
     *
     * @param userIds 用户ID列表
     * @return 用户ID -> 用户姓名(realName) 的映射，若 realName 为空则回退到 username
     */
    Map<Long, String> getUserNameMap(List<Long> userIds);

}
