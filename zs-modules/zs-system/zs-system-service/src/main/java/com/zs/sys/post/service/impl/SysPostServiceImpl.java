package com.zs.sys.post.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.exception.ZsException;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.common.core.utils.MyTreeNode;
import com.zs.sys.dept.domain.entity.SysDeptEntity;
import com.zs.sys.dept.service.ISysDeptService;
import com.zs.sys.post.domain.entity.SysPostEntity;
import com.zs.sys.post.domain.params.SysPostAddParams;
import com.zs.sys.post.domain.params.SysPostQueryParams;
import com.zs.sys.post.domain.vo.SysPostVO;
import com.zs.sys.post.mapper.SysPostMapper;
import com.zs.sys.post.service.ISysPostService;
import com.zs.sys.user.service.ISysUserDeptPostService;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author zsadmin
 */
@Service
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPostEntity> implements ISysPostService {
    @Resource
    private ISysDeptService iSysDeptService;
    @Resource
    private ISysUserDeptPostService iSysUserDeptPostService;

    @NotNull
    @Override
    public PageResult<SysPostVO> page(@NotNull SysPostQueryParams sysPostQueryParams) {
        Page<SysPostEntity> page = new PageInfo<>(sysPostQueryParams);

        List<Long> deptList = iSysDeptService.getSubDeptIdList(sysPostQueryParams.getSysDeptId());

        QueryWrapper<SysPostEntity> queryWrapper = getSysPostEntityQueryWrapper(sysPostQueryParams, deptList);
        IPage<SysPostEntity> iPage = baseMapper.selectPage(page, queryWrapper);

        page.getRecords().forEach(item -> {
            item.setDeptName(iSysDeptService.getBySysDeptId(item.getSysDeptId()));
        });
        List<SysPostVO> list = BeanUtil.copyToList(iPage.getRecords(), SysPostVO.class);

        return new PageResult<>(list, page.getTotal(), SysPostVO.class);
    }

    private QueryWrapper<SysPostEntity> getSysPostEntityQueryWrapper(SysPostQueryParams sysPostQueryParams, List<Long> deptList) {
        QueryWrapper<SysPostEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(Strings.isNotEmpty(sysPostQueryParams.getPostName()), "post_name", sysPostQueryParams.getPostName());
        queryWrapper.eq(Objects.nonNull(sysPostQueryParams.getStatus()), "status", sysPostQueryParams.getStatus());
        queryWrapper.in(!deptList.isEmpty(),"sys_dept_id", deptList);
        return queryWrapper;
    }


    @Nullable
    @Override
    public List<SysPostVO> getList(@NotNull SysPostQueryParams sysPostQueryParams) {

        List<Long> deptList = iSysDeptService.getSubDeptIdList(sysPostQueryParams.getSysDeptId());
        Map<String, Object> params = BeanUtil.beanToMap(sysPostQueryParams);
        params.put("status", 1);
        params.put("deptList", deptList);

        return BeanUtil.copyToList(baseMapper.getList(params), SysPostVO.class);
    }

    @Override
    public void save(SysPostAddParams sysPostAddParams) {
        baseMapper.insert(BeanUtil.copyProperties(sysPostAddParams, SysPostEntity.class));
    }

    @Override
    public void update(SysPostAddParams sysPostAddParams) {
        baseMapper.updateById(BeanUtil.copyProperties(sysPostAddParams, SysPostEntity.class));
    }

    @Override
    public SysPostVO getById(Long id) {
        return BeanUtil.copyProperties(baseMapper.selectById(id), SysPostVO.class);
    }

    @Override
    public void batchDelById(@NotNull Long[] sysPostIds) {
        for (Long sysPostId : sysPostIds) {
            Long num = iSysUserDeptPostService.getByPostId(sysPostId);
            if (num > 0) {
                throw new ZsException("该岗位已有用户使用，不得删除。");
            }
        }
        this.baseMapper.deleteByIds(Arrays.asList(sysPostIds));
    }

    @Override
    public void delById(Long sysPostId) {
        Long num = iSysUserDeptPostService.getByPostId(sysPostId);
        if (num > 0) {
             throw new ZsException("该岗位已有用户使用，不得删除。");
        }
        this.baseMapper.deleteById(sysPostId);
    }

    @Override
    public List<MyTreeNode> getDeptPostTree() {
        // 获取部门列表（扁平结构）
        List<SysDeptEntity> deptList = iSysDeptService.list();
        List<SysPostVO> postList = getList(new SysPostQueryParams());

        return buildDeptPostTree(deptList, Objects.requireNonNull(postList));
    }

    private List<MyTreeNode> buildDeptPostTree(List<SysDeptEntity> deptList, List<SysPostVO> postList) {
        Map<Long, MyTreeNode> nodeMap = new TreeMap<>();
        List<MyTreeNode> rootList = new ArrayList<>();

        // 第一步：构建部门树（仅添加部门节点）
        deptList.stream()
                .filter(dept -> dept.getStatus() == 1)
                .map(dept -> new MyTreeNode(dept.getSysDeptId(), dept.getPid(), dept.getDeptName()))
                .forEach(node -> {
                    nodeMap.put(node.getId(), node);
                    if (node.getPid() == null || node.getPid() == 0) {
                        rootList.add(node);
                    } else {
                        MyTreeNode parent = nodeMap.get(node.getPid());
                        if (parent != null) {
                            parent.getChildren().add(node);
                        }
                    }
                });

        // 第二步：将岗位作为叶子节点添加到对应部门下
        // 说明：
        // 1. 岗位的 sys_dept_id 应该指向其所属的部门ID
        // 2. 如果某个部门既有子部门又有岗位，那么子部门和岗位会在同一层级的 children 中
        // 3. 前端可以通过 type 字段（dept/post）来区分部门和岗位，使用不同的图标或样式展示
        postList.forEach(post -> {
            if (post.getSysDeptId() == null) {
                // 如果岗位没有关联部门，跳过
                return;
            }
            
            MyTreeNode deptNode = nodeMap.get(post.getSysDeptId());
            if (deptNode != null) {
                // 创建岗位节点，type="post" 表示这是岗位节点
                MyTreeNode postNode = new MyTreeNode(post.getSysPostId(), post.getSysDeptId(), post.getPostName(), "post");
                // 将岗位添加为部门的子节点
                deptNode.getChildren().add(postNode);
            }
            // 如果找不到对应的部门节点，说明该岗位的sys_dept_id指向的部门不存在或已禁用，跳过该岗位
        });

        return rootList;
    }

}
