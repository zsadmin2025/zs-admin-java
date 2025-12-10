package com.zs.sys.role.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.sys.role.domain.entity.SysRoleEntity;
import com.zs.sys.role.domain.params.SysRoleAddParams;
import com.zs.sys.role.domain.params.SysRoleQueryParams;
import com.zs.sys.role.domain.vo.SysRoleVO;

import java.util.List;
import java.util.Set;

/**
 * @author zsadmin
 */
public interface ISysRoleService extends IService<SysRoleEntity> {

    /**
     * 分页
     * @param sysRoleQueryParams 查询参数
     * @return 分页结果
     */
    PageResult<SysRoleVO> page(SysRoleQueryParams sysRoleQueryParams);

    /**
     * 列表
     * @param sysRoleQueryParams 查询参数
     * @return 列表结果
     */
    List<SysRoleVO> getList(SysRoleQueryParams sysRoleQueryParams);


    /**
     * 新增
     * @param sysRoleAddParams 新增参数
     */
    void save(SysRoleAddParams sysRoleAddParams);

    /**
     * 更新
     * @param sysRoleAddParams 更新参数
     */
    void update(SysRoleAddParams sysRoleAddParams);

    /**
     * 根据id查询
     * @param id 主键
     * @return 查询结果
     */
    SysRoleVO getById(Long id);

    /**
     * 单个删除
     * @param sysRoleId 主键
     */
    void deleteById(Long sysRoleId);

    /**
     * 批量删除
     * @param sysRoleIds 主键
     */
    void batchDelById(List<Long> sysRoleIds);

    /**
      * 根据用户userId获取用户角色
      * @param sysUserId 用户ID
      * @return 用户的角色列表
     */
    List<SysRoleEntity> findByUserId(Long sysUserId);


    /**
     * 根据角色ID列表获取角色列表
     * @param sysRoleIds 角色ID列表
     * @return 角色列表
     */
    List<SysRoleVO> getList(List<Long> sysRoleIds);


    /**
     * 根据用户ID查询角色数据权限类型集合
     * @param userId 用户ID
     * @return 角色数据权限类型集合
     */
    Set<Integer> getDataScope(Long userId);


    /**
     * 根据用户ID查询自定义部门权限ID集合
     * @param userId 用户ID
     * @return 自定义部门权限ID集合
     */
    Set<Long> getRoleDeptIds(Long userId);
}
