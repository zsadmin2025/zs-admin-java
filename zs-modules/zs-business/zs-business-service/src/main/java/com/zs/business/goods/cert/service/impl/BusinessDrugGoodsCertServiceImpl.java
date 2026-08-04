package com.zs.business.goods.cert.service.impl;

import com.zs.business.goods.cert.domain.entity.BusinessDrugGoodsCertEntity;
import com.zs.business.goods.cert.mapper.BusinessDrugGoodsCertMapper;
import com.zs.business.goods.cert.service.BusinessDrugGoodsCertService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.business.goods.cert.domain.vo.BusinessDrugGoodsCertVO;
import com.zs.business.goods.cert.domain.params.BusinessDrugGoodsCertPageQueryParams;
import com.zs.business.goods.cert.domain.params.BusinessDrugGoodsCertSelectQueryParams;
import com.zs.business.goods.cert.domain.params.BusinessDrugGoodsCertAddParams;
import com.zs.business.goods.cert.domain.params.BusinessDrugGoodsCertUpdateParams;
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
 * 商品证照附件 服务实现类
 * </p>
 *
 * @author zs
 * {@code @date} 2026-08-03 11:09:18
 */
@Service
public class BusinessDrugGoodsCertServiceImpl extends ServiceImpl<BusinessDrugGoodsCertMapper, BusinessDrugGoodsCertEntity> implements BusinessDrugGoodsCertService {

        @Override
        public PageResult<BusinessDrugGoodsCertVO> page(@NotNull BusinessDrugGoodsCertPageQueryParams businessDrugGoodsCertPageQueryParams) {
            Page<BusinessDrugGoodsCertEntity> page = new PageInfo<>(businessDrugGoodsCertPageQueryParams);
            LambdaQueryWrapper<BusinessDrugGoodsCertEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsCertPageQueryParams.getDrugGoodsCertId()), BusinessDrugGoodsCertEntity::getDrugGoodsCertId, businessDrugGoodsCertPageQueryParams.getDrugGoodsCertId());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsCertPageQueryParams.getDrugGoodsId()), BusinessDrugGoodsCertEntity::getDrugGoodsId, businessDrugGoodsCertPageQueryParams.getDrugGoodsId());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsCertPageQueryParams.getCertName()), BusinessDrugGoodsCertEntity::getCertName, businessDrugGoodsCertPageQueryParams.getCertName());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsCertPageQueryParams.getCertNo()), BusinessDrugGoodsCertEntity::getCertNo, businessDrugGoodsCertPageQueryParams.getCertNo());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsCertPageQueryParams.getValidEndDate()), BusinessDrugGoodsCertEntity::getValidEndDate, businessDrugGoodsCertPageQueryParams.getValidEndDate());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsCertPageQueryParams.getFileUrl()), BusinessDrugGoodsCertEntity::getFileUrl, businessDrugGoodsCertPageQueryParams.getFileUrl());
            IPage<BusinessDrugGoodsCertEntity> iPage = baseMapper.selectPage(page, wrapper);
            List<BusinessDrugGoodsCertVO> list = BeanUtil.copyToList(iPage.getRecords(), BusinessDrugGoodsCertVO.class);

            return new PageResult<>(list, page.getTotal(), BusinessDrugGoodsCertVO.class);
        }

        @Override
        public List<BusinessDrugGoodsCertVO> getList(@NotNull BusinessDrugGoodsCertSelectQueryParams businessDrugGoodsCertSelectQueryParams) {
            LambdaQueryWrapper<BusinessDrugGoodsCertEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsCertSelectQueryParams.getDrugGoodsCertId()), BusinessDrugGoodsCertEntity::getDrugGoodsCertId, businessDrugGoodsCertSelectQueryParams.getDrugGoodsCertId());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsCertSelectQueryParams.getDrugGoodsId()), BusinessDrugGoodsCertEntity::getDrugGoodsId, businessDrugGoodsCertSelectQueryParams.getDrugGoodsId());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsCertSelectQueryParams.getCertName()), BusinessDrugGoodsCertEntity::getCertName, businessDrugGoodsCertSelectQueryParams.getCertName());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsCertSelectQueryParams.getCertNo()), BusinessDrugGoodsCertEntity::getCertNo, businessDrugGoodsCertSelectQueryParams.getCertNo());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsCertSelectQueryParams.getValidEndDate()), BusinessDrugGoodsCertEntity::getValidEndDate, businessDrugGoodsCertSelectQueryParams.getValidEndDate());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsCertSelectQueryParams.getFileUrl()), BusinessDrugGoodsCertEntity::getFileUrl, businessDrugGoodsCertSelectQueryParams.getFileUrl());
            return BeanUtil.copyToList(baseMapper.selectList(wrapper), BusinessDrugGoodsCertVO.class);
        }

        @Override
        public void save(@NotNull BusinessDrugGoodsCertAddParams businessDrugGoodsCertAddParams) {
            BusinessDrugGoodsCertEntity businessDrugGoodsCertEntity = BeanUtil.copyProperties(businessDrugGoodsCertAddParams, BusinessDrugGoodsCertEntity.class);
            baseMapper.insert(businessDrugGoodsCertEntity);
        }

        @Override
        public void update(@NotNull BusinessDrugGoodsCertUpdateParams businessDrugGoodsCertUpdateParams) {
            BusinessDrugGoodsCertEntity businessDrugGoodsCertEntity = BeanUtil.copyProperties(businessDrugGoodsCertUpdateParams, BusinessDrugGoodsCertEntity.class);
            baseMapper.updateById(businessDrugGoodsCertEntity);
        }

        @Override
        public BusinessDrugGoodsCertVO getById(Long id) {
            BusinessDrugGoodsCertVO businessDrugGoodsCertVO = BeanUtil.copyProperties(baseMapper.selectById(id), BusinessDrugGoodsCertVO.class);
            return businessDrugGoodsCertVO;
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
