package com.zs.sys.dept.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.core.HttpEnum;
import com.zs.common.core.events.DataPermissionChangedEvent;
import com.zs.common.core.exception.ZsException;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.common.core.utils.MyTreeNode;
import com.zs.common.core.utils.TreeUtil;
import com.zs.sys.dept.domain.entity.SysDeptEntity;
import com.zs.sys.dept.domain.params.SysDeptAddParams;
import com.zs.sys.dept.domain.params.SysDeptPageQueryParams;
import com.zs.sys.dept.domain.params.SysDeptQueryParams;
import com.zs.sys.dept.domain.params.SysDeptUpdateParams;
import com.zs.sys.dept.domain.vo.SysDeptTreeVO;
import com.zs.sys.dept.domain.vo.SysDeptVO;
import com.zs.sys.dept.mapper.SysDeptMapper;
import com.zs.sys.dept.service.ISysDeptService;
import com.zs.sys.post.domain.params.SysPostQueryParams;
import com.zs.sys.post.domain.vo.SysPostVO;
import com.zs.sys.post.service.ISysPostService;
import com.zs.sys.user.service.ISysUserService;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zsadmin
 */

@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDeptEntity> implements ISysDeptService{

    private static final Logger log = LoggerFactory.getLogger(SysDeptServiceImpl.class);

    @Resource
    @Lazy
    private ISysPostService sysPostService;
    @Resource
    @Lazy
    private ISysUserService userService;
    @Resource
    private ApplicationEventPublisher eventPublisher;

    @Override
    public PageResult<SysDeptVO> page(SysDeptPageQueryParams sysDeptPageQueryParams) {

        // 创建分页对象
        Page<SysDeptEntity> page = new PageInfo<>(sysDeptPageQueryParams);


        List<SysDeptEntity> deptList = this.baseMapper.selectList(new LambdaQueryWrapper<SysDeptEntity>().eq(SysDeptEntity::getPid, sysDeptPageQueryParams.getSysDeptId()));

         List<Long> subIds =  deptList.stream().map(SysDeptEntity::getSysDeptId).toList();

        // 构建查询条件
        LambdaQueryWrapper<SysDeptEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(sysDeptPageQueryParams.getDeptName()), SysDeptEntity::getDeptName, sysDeptPageQueryParams.getDeptName());
        if(!subIds.isEmpty()){
            queryWrapper.eq(SysDeptEntity::getPid, sysDeptPageQueryParams.getSysDeptId());
        }else if (Objects.nonNull(sysDeptPageQueryParams.getSysDeptId())){
            queryWrapper.eq(SysDeptEntity::getSysDeptId, sysDeptPageQueryParams.getSysDeptId());
        }


        // 执行分页查询
        IPage<SysDeptEntity> resultPage = baseMapper.page(page, queryWrapper);

        // 转换 VO 并返回结果
        return new PageResult<>(
                BeanUtil.copyToList(resultPage.getRecords(), SysDeptVO.class),
                resultPage.getTotal(),
                SysDeptVO.class
        );
    }


    @NotNull
    @Override
    public List<SysDeptTreeVO> getTree(SysDeptQueryParams sysDeptQueryParams) {

        List<SysDeptEntity> entityList = baseMapper.getList(sysDeptQueryParams);
        List<SysDeptTreeVO> list = BeanUtil.copyToList(entityList, SysDeptTreeVO.class);
        return TreeUtil.build(list);


    }

    @Nullable
    @Override
    public List<SysDeptVO> getList(SysDeptQueryParams sysDeptQueryParams) {
        List<SysDeptEntity> entityList = baseMapper.getList(sysDeptQueryParams);
        return BeanUtil.copyToList(entityList, SysDeptVO.class);
    }


    @Override
    public void save(@NotNull SysDeptAddParams sysOrgAddParams) {
        SysDeptEntity sysDeptEntity = BeanUtil.copyProperties(sysOrgAddParams, SysDeptEntity.class);
        sysDeptEntity.setPids(StrUtil.join(",", getTree(sysOrgAddParams)));
        baseMapper.insert(sysDeptEntity);

        // 发布部门变更事件：影响父部门及上级部门下的用户
        publishDeptChangedEvent(sysDeptEntity.getSysDeptId());
    }

    @NotNull
    public List<Long> getTree(@NotNull SysDeptAddParams sysOrgAddParams) {
        List<SysDeptEntity> deptList = baseMapper.selectList(new QueryWrapper<>());
        Map<Long, SysDeptEntity> map = new HashMap<>(deptList.size());
        for (SysDeptEntity entity : deptList) {
            map.put(entity.getSysDeptId(), entity);
        }
        List<Long> pidList = new ArrayList<>();
        getPid(sysOrgAddParams.getPid(), map, pidList);
        return pidList;
    }

    public void getPid(Long pid, @NotNull Map<Long, SysDeptEntity> map, @NotNull List<Long> pidList) {
        SysDeptEntity parent = map.get(pid);
        if (Objects.nonNull(parent)) {
            pidList.add(parent.getSysDeptId());
            getPid(parent.getPid(), map, pidList);
        }
    }


    @Override
    public void update(SysDeptUpdateParams sysDeptUpdateParams) {
        baseMapper.updateById(BeanUtil.copyProperties(sysDeptUpdateParams, SysDeptEntity.class));
        // 发布部门变更事件
        publishDeptChangedEvent(sysDeptUpdateParams.getSysDeptId());
    }

    @Override
    public SysDeptVO getById(Long id) {
        return BeanUtil.copyProperties(baseMapper.selectById(id), SysDeptVO.class);
    }

    @Override
    public void removeById(Long sysDeptId) {
        // 查询是否存在子级
        Long count = baseMapper.selectCount(new LambdaQueryWrapper<SysDeptEntity>().eq(SysDeptEntity::getPid, sysDeptId));
        if (count > 0) {
            throw new ZsException(HttpEnum.SUB_DEPT_ERROR);
        }
        // 删除前发布事件
        publishDeptChangedEvent(sysDeptId);
        baseMapper.deleteById(sysDeptId);
    }

    /**
     * 发布部门变更事件，通知该部门及子部门下所有用户刷新权限缓存
     */
    private void publishDeptChangedEvent(Long sysDeptId) {
        List<Long> deptIds = getSubDeptIdList(sysDeptId);
        if (deptIds.isEmpty()) {
            return;
        }
        try {
            List<com.zs.sys.user.domain.vo.SysUserVO> userList = userService.getUserListByDeptId(deptIds);
            if (userList != null && !userList.isEmpty()) {
                Set<Long> affectedUserIds = userList.stream()
                        .map(com.zs.sys.user.domain.vo.SysUserVO::getSysUserId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());
                if (!affectedUserIds.isEmpty()) {
                    eventPublisher.publishEvent(new DataPermissionChangedEvent(
                            this, DataPermissionChangedEvent.ChangeType.DEPT_CHANGED, affectedUserIds));
                }
            }
        } catch (Exception e) {
            log.warn("发布部门变更事件失败: deptId={}", sysDeptId, e);
        }
    }

    @NotNull
    @Override
    public List<Long> getSubDeptIdList(Long sysDeptId) {

        if (Objects.isNull(sysDeptId)) {
            return new ArrayList<>();
        }
        List<Long> deptIdList = baseMapper.getSubDeptIdList(sysDeptId);
        deptIdList.add(sysDeptId);

        return deptIdList;
    }

    @Override
    public String getBySysDeptId(Long sysDeptId) {
        SysDeptEntity sysDeptEntity = baseMapper.selectById(sysDeptId);
        if (Objects.isNull(sysDeptEntity)) {
            return "";
        }
        // 将pids转换成list数组
        String[] split = sysDeptEntity.getPids().split(",");

        List<Long> pids = Arrays.stream(split)
                .filter(s -> s != null && !s.trim().isEmpty()) // 过滤空值
                .map(Long::valueOf).toList();

        if (pids.isEmpty()) {
            return sysDeptEntity.getDeptName();
        }
        // 查询父级部门列表
        List<SysDeptEntity> list = baseMapper.selectBatchIds(pids);

        // 构造结果字符串
        String result = list.stream().map(SysDeptEntity::getDeptName).collect(Collectors.joining("/"));
        return result + ("/" + sysDeptEntity.getDeptName());
    }

    @NotNull
    @Override
    public Set<Long> getDeptAndChildrenDeptIds(Long deptId) {
        return new HashSet<>(this.getSubDeptIdList(deptId));
    }

    @Override
    public List<MyTreeNode> getDeptPostTree() {

        List<SysDeptEntity> entityList = baseMapper.getList(new SysDeptQueryParams());
        List<SysDeptTreeVO> list = BeanUtil.copyToList(entityList, SysDeptTreeVO.class);
        List<SysPostVO>  postList = sysPostService.getList(new SysPostQueryParams());

        return buildDeptPostTree(list, Objects.requireNonNull(postList));
    }



    public List<MyTreeNode> buildDeptPostTree(List<SysDeptTreeVO> deptList, List<SysPostVO> postList) {
        // 使用TreeMap来自动排序部门ID
        Map<Long, MyTreeNode> nodeMap = new TreeMap<>();
        List<MyTreeNode> rootList = new ArrayList<>();

        // 创建部门节点，并构建部门树结构
        deptList.stream()
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

        // 将岗位添加到对应的部门节点下
        postList.stream()
                .filter(post -> nodeMap.containsKey(post.getSysDeptId()))
                .map(post -> new MyTreeNode(post.getSysPostId(), post.getSysDeptId(), post.getPostName()))
                .forEach(postNode -> nodeMap.get(postNode.getPid()).getChildren().add(postNode));

        return rootList;
    }


}
