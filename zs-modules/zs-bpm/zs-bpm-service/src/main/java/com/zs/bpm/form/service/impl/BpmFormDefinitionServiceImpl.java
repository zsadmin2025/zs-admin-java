package com.zs.bpm.form.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.bpm.form.domain.entity.BpmFormDefinitionEntity;
import com.zs.bpm.form.domain.params.FormDefinitionAddParams;
import com.zs.bpm.form.domain.params.FormDefinitionQueryParams;
import com.zs.bpm.form.domain.vo.FormDefinitionVO;
import com.zs.bpm.form.mapper.BpmFormDefinitionMapper;
import com.zs.bpm.form.service.IBpmFormDefinitionService;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 动态表单定义 Service 实现
 *
 * @author zsadmin
 */
@Service
public class BpmFormDefinitionServiceImpl extends ServiceImpl<BpmFormDefinitionMapper, BpmFormDefinitionEntity> implements IBpmFormDefinitionService {

    @Resource
    private BpmFormDefinitionMapper baseMapper;

    @Override
    public PageResult<FormDefinitionVO> page(FormDefinitionQueryParams params) {
        Page<BpmFormDefinitionEntity> page = new PageInfo<>(params);
        LambdaQueryWrapper<BpmFormDefinitionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(params.getFormName() != null, BpmFormDefinitionEntity::getFormName, params.getFormName())
                .eq(params.getStatus() != null, BpmFormDefinitionEntity::getStatus, params.getStatus())
                .orderByDesc(BpmFormDefinitionEntity::getCreateTime);
        Page<BpmFormDefinitionEntity> result = baseMapper.selectPage(page, wrapper);
        List<FormDefinitionVO> list = BeanUtil.copyToList(result.getRecords(), FormDefinitionVO.class);
        return new PageResult<>(list, result.getTotal());
    }

    @Override
    public FormDefinitionVO getById(Long id) {
        BpmFormDefinitionEntity entity = baseMapper.selectById(id);
        return BeanUtil.copyProperties(entity, FormDefinitionVO.class);
    }

    @Override
    public void save(FormDefinitionAddParams params) {
        BpmFormDefinitionEntity entity = BeanUtil.copyProperties(params, BpmFormDefinitionEntity.class);
        baseMapper.insert(entity);
    }

    @Override
    public void update(FormDefinitionAddParams params) {
        BpmFormDefinitionEntity entity = BeanUtil.copyProperties(params, BpmFormDefinitionEntity.class);
        baseMapper.updateById(entity);
    }

    @Override
    public void delete(Long id) {
        baseMapper.deleteById(id);
    }

}
