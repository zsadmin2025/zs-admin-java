package com.zs.sys.dept.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.common.core.utils.MyTreeNode;
import com.zs.sys.dept.domain.entity.SysDeptEntity;
import com.zs.sys.dept.domain.params.SysDeptAddParams;
import com.zs.sys.dept.domain.params.SysDeptPageQueryParams;
import com.zs.sys.dept.domain.params.SysDeptQueryParams;
import com.zs.sys.dept.domain.params.SysDeptUpdateParams;
import com.zs.sys.dept.domain.vo.SysDeptTreeVO;
import com.zs.sys.dept.domain.vo.SysDeptVO;

import java.util.List;
import java.util.Set;

/**
 * @author zsadmin
 */
public interface ISysDeptService extends IService<SysDeptEntity> {

    /**
     * 分页查询
     * @param sysDeptPageQueryParams 查询参数
     * @return 分页结果
     */
    PageResult<SysDeptVO> page(SysDeptPageQueryParams sysDeptPageQueryParams);

    /**
     * 获取部门树
     * @param sysDeptQueryParams 查询参数
     * @return 部门树
     */
    List<SysDeptTreeVO> getTree(SysDeptQueryParams sysDeptQueryParams);

    /**
     * 获取部门列表
     * @param sysDeptQueryParams 查询参数
     * @return 部门列表
     */
    List<SysDeptVO> getList(SysDeptQueryParams sysDeptQueryParams);

    /**
     * 保存部门
     * @param sysOrgAddParams 部门参数
     */
    void save(SysDeptAddParams sysOrgAddParams);

    /**
     * 更新部门
     * @param sysDeptUpdateParams 部门参数
     */
    void update(SysDeptUpdateParams sysDeptUpdateParams);

    /**
     * 根据ID查询部门
     * @param sysDeptId 部门ID
     * @return 部门
     */
    SysDeptVO getById(Long sysDeptId);

    /**
     * 删除部门
     * @param sysDeptId 部门ID
     */
    void removeById(Long sysDeptId);

    /**
     * 获取部门及子部门ID列表
     *
     * @param sysDeptId 部门ID
     * @return 部门及子部门ID列表
     */
    List<Long> getSubDeptIdList(Long sysDeptId);

    /**
     * 根据部门ID获取部门名称
     *
     * @param sysDeptId 部门ID
     * @return 部门名称
     */
    String getBySysDeptId(Long sysDeptId);

    /**
     * 获取部门岗位树
     *
     * @return 部门岗位树
     */
    List<MyTreeNode> getDeptPostTree();

    /**
     * 获取部门及子部门ID列表
     *
     * @param deptId 部门ID
     * @return 部门及子部门ID列表
     */
    Set<Long> getDeptAndChildrenDeptIds(Long deptId);
}
