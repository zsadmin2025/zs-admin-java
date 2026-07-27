package com.zs.bpm.category.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.bpm.category.domain.entity.BpmProcessCategoryEntity;
import com.zs.bpm.category.domain.params.BpmProcessCategoryAddParams;
import com.zs.bpm.category.domain.params.BpmProcessCategoryQueryParams;
import com.zs.bpm.category.domain.params.BpmProcessCategoryUpdateParams;
import com.zs.bpm.category.domain.vo.BpmProcessCategoryVO;
import com.zs.bpm.category.mapper.BpmProcessCategoryMapper;
import com.zs.bpm.category.service.IBpmProcessCategoryService;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 流程分类 Service 实现
 *
 * @author zsadmin
 */
@Service
public class BpmProcessCategoryServiceImpl extends ServiceImpl<BpmProcessCategoryMapper, BpmProcessCategoryEntity> implements IBpmProcessCategoryService {

    @Override
    public PageResult<BpmProcessCategoryVO> page(BpmProcessCategoryQueryParams params) {
        Page<BpmProcessCategoryEntity> page = new PageInfo<>(params);
        LambdaQueryWrapper<BpmProcessCategoryEntity> wrapper = new LambdaQueryWrapper<BpmProcessCategoryEntity>()
                .like(params.getName() != null, BpmProcessCategoryEntity::getName, params.getName())
                .eq(params.getCode() != null, BpmProcessCategoryEntity::getCode, params.getCode())
                .eq(params.getStatus() != null, BpmProcessCategoryEntity::getStatus, params.getStatus())
                .orderByAsc(BpmProcessCategoryEntity::getSort);
        Page<BpmProcessCategoryEntity> result = baseMapper.selectPage(page, wrapper);
        List<BpmProcessCategoryVO> list = BeanUtil.copyToList(result.getRecords(), BpmProcessCategoryVO.class);
        return new PageResult<>(list, result.getTotal());
    }

    @Override
    public List<BpmProcessCategoryVO> getList(BpmProcessCategoryQueryParams params) {
        LambdaQueryWrapper<BpmProcessCategoryEntity> wrapper = new LambdaQueryWrapper<BpmProcessCategoryEntity>()
                .like(params.getName() != null, BpmProcessCategoryEntity::getName, params.getName())
                .eq(params.getCode() != null, BpmProcessCategoryEntity::getCode, params.getCode())
                .eq(params.getStatus() != null, BpmProcessCategoryEntity::getStatus, params.getStatus())
                .orderByAsc(BpmProcessCategoryEntity::getSort);
        List<BpmProcessCategoryEntity> list = baseMapper.selectList(wrapper);
        return BeanUtil.copyToList(list, BpmProcessCategoryVO.class);
    }

    @Override
    public BpmProcessCategoryVO getById(Long id) {
        BpmProcessCategoryEntity entity = baseMapper.selectById(id);
        return BeanUtil.copyProperties(entity, BpmProcessCategoryVO.class);
    }

    @Override
    public void save(BpmProcessCategoryAddParams params) {
        BpmProcessCategoryEntity entity = BeanUtil.copyProperties(params, BpmProcessCategoryEntity.class);
        baseMapper.insert(entity);
    }

    @Override
    public void update(BpmProcessCategoryUpdateParams params) {
        BpmProcessCategoryEntity entity = BeanUtil.copyProperties(params, BpmProcessCategoryEntity.class);
        baseMapper.updateById(entity);
    }

    @Override
    public List<BpmProcessCategoryVO> getTreeList() {
        List<BpmProcessCategoryEntity> list = baseMapper.selectList(new LambdaQueryWrapper<BpmProcessCategoryEntity>()
                .orderByAsc(BpmProcessCategoryEntity::getSort));
        return BeanUtil.copyToList(list, BpmProcessCategoryVO.class);
    }
}
