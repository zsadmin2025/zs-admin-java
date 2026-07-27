package com.zs.sys.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.sys.user.domain.entity.SysUserEntity;
import com.zs.sys.user.domain.params.SysUserAddParams;
import com.zs.sys.user.domain.params.SysUserPasswordParams;
import com.zs.sys.user.domain.params.SysUserQueryParams;
import com.zs.sys.user.domain.params.SysUserUpdateParams;
import com.zs.sys.user.domain.vo.SysUserInfoVO;
import com.zs.sys.user.domain.vo.SysUserVO;

import java.util.List;

/**
 * @author zsadmin
 */
public interface ISysUserService extends IService<SysUserEntity> {

    /** 分页 **/
    PageResult<SysUserVO> page(SysUserQueryParams sysUserQueryParams);

    /** 新增 **/
    void save(SysUserAddParams sysUserAddParams);

    /** 修改 **/
    void update(SysUserUpdateParams sysUserUpdateParams);

    /** 批量删除 **/
    void batchDelById(Long[] ids);

    /** 删除 **/
    void delById(Long id);

    /** 重置密码 **/
    void resetPassword(SysUserPasswordParams sysUserPasswordParams);

    /** 获取详情 **/
    SysUserInfoVO getById(Long id);

    /**
     * 列表(用户状态正常 status = 1)
     * @param sysUserQueryParams 查询参数
     * @return  List<SysUserVO> 列表
     */
    List<SysUserVO> list(SysUserQueryParams sysUserQueryParams);

    /**
     * 获取用户列表
     * @param sysUserIds 用户id集合
     * @return  List<SysUserVO> 列表
     */
    List<SysUserVO> getUserList(Long[] sysUserIds);


    /**
     * 根据部门id获取用户列表
     * @param sysDeptIds 部门id集合
     * @return  List<SysUserVO> 列表
     */
    List<SysUserVO> getUserListByDeptId(List<Long> sysDeptIds);

    /**
     * 根据岗位id获取用户列表
     * @param sysPostIds 岗位id集合
     * @return  List<SysUserVO> 列表
     */
    List<SysUserVO> getUserListByPostId(List<Long> sysPostIds);

    /**
     * 根据id获取用户详情
     * @param id 用户id
     * @return  SysUserVO 用户详情
     */
    SysUserVO getUserById(Long id);
}
