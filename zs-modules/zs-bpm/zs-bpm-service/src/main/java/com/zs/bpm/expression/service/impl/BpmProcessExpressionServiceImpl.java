package com.zs.bpm.expression.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.bpm.expression.domain.entity.BpmProcessExpressionEntity;
import com.zs.bpm.expression.domain.params.BpmProcessExpressionQueryParams;
import com.zs.bpm.expression.domain.vo.BpmProcessExpressionVO;
import com.zs.bpm.expression.mapper.BpmProcessExpressionMapper;
import com.zs.bpm.expression.service.IBpmProcessExpressionService;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 流程表达式 Service 实现
 *
 * @author zsadmin
 */
@Service
public class BpmProcessExpressionServiceImpl extends ServiceImpl<BpmProcessExpressionMapper, BpmProcessExpressionEntity> implements IBpmProcessExpressionService {

    @Override
    public PageResult<BpmProcessExpressionVO> pageQuery(BpmProcessExpressionQueryParams queryParams) {
        Page<BpmProcessExpressionEntity> page = new PageInfo<>(queryParams);
        LambdaQueryWrapper<BpmProcessExpressionEntity> wrapper = new LambdaQueryWrapper<>();
        if(queryParams.getName() != null && !queryParams.getName().isEmpty()){
            wrapper.like(BpmProcessExpressionEntity::getName, queryParams.getName());
        }
        if(queryParams.getCode() != null && !queryParams.getCode().isEmpty()){
            wrapper.like(BpmProcessExpressionEntity::getCode, queryParams.getCode());
        }
        wrapper.orderByDesc(BpmProcessExpressionEntity::getCreateTime);
        Page<BpmProcessExpressionEntity> result = baseMapper.selectPage(page, wrapper);
        List<BpmProcessExpressionVO> list = BeanUtil.copyToList(result.getRecords(), BpmProcessExpressionVO.class);

        return new PageResult<>(list, result.getTotal());
    }

    @Override
    public void deleteById(Long id) {
        baseMapper.deleteById(id);
    }

    @Override
    public BpmProcessExpressionVO getById(Long id) {
        BpmProcessExpressionEntity entity = baseMapper.selectById(id);
        return BeanUtil.copyProperties(entity, BpmProcessExpressionVO.class);
    }

}
