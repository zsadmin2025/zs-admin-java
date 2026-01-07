package com.zs.sys.demo.service.impl;

import com.zs.sys.demo.domain.entity.SysDemoEntity;
import com.zs.sys.demo.mapper.SysDemoMapper;
import com.zs.sys.demo.service.SysDemoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.sys.demo.domain.vo.SysDemoVO;
import com.zs.sys.demo.domain.params.SysDemoPageQueryParams;
import com.zs.sys.demo.domain.params.SysDemoSelectQueryParams;
import com.zs.sys.demo.domain.params.SysDemoAddParams;
import com.zs.sys.demo.domain.params.SysDemoUpdateParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import cn.hutool.core.util.ObjectUtil;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 代码生成测试表 服务实现类
 * </p>
 *
 * @author zs
 * @date 2026-01-07 11:01:19
 */
@Service
public class SysDemoServiceImpl extends ServiceImpl<SysDemoMapper, SysDemoEntity> implements SysDemoService {




        @Override
        public PageResult<SysDemoVO> page(@NotNull SysDemoPageQueryParams sysDemoPageQueryParams) {
            Page<SysDemoEntity> page = new PageInfo<>(sysDemoPageQueryParams);
            LambdaQueryWrapper<SysDemoEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ObjectUtil.isNotNull(sysDemoPageQueryParams.getSysDemoId()), SysDemoEntity::getSysDemoId, sysDemoPageQueryParams.getSysDemoId());
            wrapper.like(ObjectUtil.isNotNull(sysDemoPageQueryParams.getInputField()), SysDemoEntity::getInputField, sysDemoPageQueryParams.getInputField());
            wrapper.eq(ObjectUtil.isNotNull(sysDemoPageQueryParams.getNumberField()), SysDemoEntity::getNumberField, sysDemoPageQueryParams.getNumberField());
            wrapper.between(ObjectUtil.isNotNull(sysDemoPageQueryParams.getDatetimeFieldStart()) && ObjectUtil.isNotNull(sysDemoPageQueryParams.getDatetimeFieldEnd()),
                    SysDemoEntity::getDatetimeField, sysDemoPageQueryParams.getDatetimeFieldStart(), sysDemoPageQueryParams.getDatetimeFieldEnd());
            wrapper.eq(ObjectUtil.isNotNull(sysDemoPageQueryParams.getIsDelete()), SysDemoEntity::getIsDelete, sysDemoPageQueryParams.getIsDelete());
            wrapper.eq(ObjectUtil.isNotNull(sysDemoPageQueryParams.getStatus()), SysDemoEntity::getStatus, sysDemoPageQueryParams.getStatus());

            IPage<SysDemoEntity> iPage = baseMapper.selectPage(page, wrapper);
            List<SysDemoVO> list = BeanUtil.copyToList(iPage.getRecords(), SysDemoVO.class);

            return new PageResult<>(list, page.getTotal(), SysDemoVO.class);
        }

        @Override
        public List<SysDemoVO> getList(@NotNull SysDemoSelectQueryParams sysDemoSelectQueryParams) {
            LambdaQueryWrapper<SysDemoEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ObjectUtil.isNotNull(sysDemoSelectQueryParams.getSysDemoId()), SysDemoEntity::getSysDemoId, sysDemoSelectQueryParams.getSysDemoId());
            wrapper.like(ObjectUtil.isNotNull(sysDemoSelectQueryParams.getInputField()), SysDemoEntity::getInputField, sysDemoSelectQueryParams.getInputField());
            wrapper.eq(ObjectUtil.isNotNull(sysDemoSelectQueryParams.getNumberField()), SysDemoEntity::getNumberField, sysDemoSelectQueryParams.getNumberField());
            wrapper.between(ObjectUtil.isNotNull(sysDemoSelectQueryParams.getDatetimeFieldStart()) && ObjectUtil.isNotNull(sysDemoSelectQueryParams.getDatetimeFieldEnd()),
                    SysDemoEntity::getDatetimeField, sysDemoSelectQueryParams.getDatetimeFieldStart(), sysDemoSelectQueryParams.getDatetimeFieldEnd());
            wrapper.eq(ObjectUtil.isNotNull(sysDemoSelectQueryParams.getIsDelete()), SysDemoEntity::getIsDelete, sysDemoSelectQueryParams.getIsDelete());
            wrapper.eq(ObjectUtil.isNotNull(sysDemoSelectQueryParams.getStatus()), SysDemoEntity::getStatus, sysDemoSelectQueryParams.getStatus());
            return BeanUtil.copyToList(baseMapper.selectList(wrapper), SysDemoVO.class);
        }

        @Override
        public void save(@NotNull SysDemoAddParams sysDemoAddParams) {
            SysDemoEntity sysDemoEntity = BeanUtil.copyProperties(sysDemoAddParams, SysDemoEntity.class);
            baseMapper.insert(sysDemoEntity);
        }

        @Override
        public void update(@NotNull SysDemoUpdateParams sysDemoUpdateParams) {
            SysDemoEntity sysDemoEntity = BeanUtil.copyProperties(sysDemoUpdateParams, SysDemoEntity.class);
            baseMapper.updateById(sysDemoEntity);
        }

        @Override
        public SysDemoVO getById(Long id) {
            SysDemoVO sysDemoVO = BeanUtil.copyProperties(baseMapper.selectById(id), SysDemoVO.class);
            return sysDemoVO;
        }

        @Override
        public void deleteById(Long id) {
            baseMapper.deleteById(id);
        }

        @Override
        public void batchDelById(@NotNull Long[] ids) {
            baseMapper.deleteByIds(Arrays.asList(ids));
        }
}
