package com.zs.business.goods.category.service.impl;

import com.zs.business.goods.category.domain.entity.BusinessDrugGoodsCategoryEntity;
import com.zs.business.goods.category.mapper.BusinessDrugGoodsCategoryMapper;
import com.zs.business.goods.category.service.BusinessDrugGoodsCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.business.goods.category.domain.vo.BusinessDrugGoodsCategoryVO;
import com.zs.business.goods.category.domain.params.BusinessDrugGoodsCategoryPageQueryParams;
import com.zs.business.goods.category.domain.params.BusinessDrugGoodsCategorySelectQueryParams;
import com.zs.business.goods.category.domain.params.BusinessDrugGoodsCategoryAddParams;
import com.zs.business.goods.category.domain.params.BusinessDrugGoodsCategoryUpdateParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import cn.hutool.core.util.ObjectUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.zs.sys.api.role.RemoteUserService;

/**
 * <p>
 * 商品档案 服务实现类
 * </p>
 *
 * @author zs
 * {@code @date} 2026-07-31 19:22:03
 */
@Service
public class BusinessDrugGoodsCategoryServiceImpl extends ServiceImpl<BusinessDrugGoodsCategoryMapper, BusinessDrugGoodsCategoryEntity> implements BusinessDrugGoodsCategoryService {

        @Resource
        private RemoteUserService remoteUserService;

        @Override
        public PageResult<BusinessDrugGoodsCategoryVO> page(@NotNull BusinessDrugGoodsCategoryPageQueryParams businessDrugGoodsCategoryPageQueryParams) {
            Page<BusinessDrugGoodsCategoryEntity> page = new PageInfo<>(businessDrugGoodsCategoryPageQueryParams);
            LambdaQueryWrapper<BusinessDrugGoodsCategoryEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsCategoryPageQueryParams.getCategoryId()), BusinessDrugGoodsCategoryEntity::getCategoryId, businessDrugGoodsCategoryPageQueryParams.getCategoryId());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsCategoryPageQueryParams.getCategoryName()), BusinessDrugGoodsCategoryEntity::getCategoryName, businessDrugGoodsCategoryPageQueryParams.getCategoryName());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsCategoryPageQueryParams.getStatus()), BusinessDrugGoodsCategoryEntity::getStatus, businessDrugGoodsCategoryPageQueryParams.getStatus());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsCategoryPageQueryParams.getRemark()), BusinessDrugGoodsCategoryEntity::getRemark, businessDrugGoodsCategoryPageQueryParams.getRemark());
            IPage<BusinessDrugGoodsCategoryEntity> iPage = baseMapper.selectPage(page, wrapper);
            List<BusinessDrugGoodsCategoryVO> list = BeanUtil.copyToList(iPage.getRecords(), BusinessDrugGoodsCategoryVO.class);

            // 批量查询创建人姓名
            List<Long> creatorIds = iPage.getRecords().stream()
                    .map(BusinessDrugGoodsCategoryEntity::getCreator)
                    .filter(ObjectUtil::isNotNull)
                    .distinct()
                    .toList();
            if (!creatorIds.isEmpty()) {
                Map<Long, String> userNameMap = remoteUserService.getUserNameMap(creatorIds);
                for (int i = 0; i < list.size(); i++) {
                    BusinessDrugGoodsCategoryEntity entity = iPage.getRecords().get(i);
                    if (entity.getCreator() != null) {
                        list.get(i).setCreatorName(userNameMap.get(entity.getCreator()));
                    }
                }
            }

            return new PageResult<>(list, page.getTotal(), BusinessDrugGoodsCategoryVO.class);
        }

        @Override
        public List<BusinessDrugGoodsCategoryVO> getList(@NotNull BusinessDrugGoodsCategorySelectQueryParams businessDrugGoodsCategorySelectQueryParams) {
            LambdaQueryWrapper<BusinessDrugGoodsCategoryEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsCategorySelectQueryParams.getCategoryId()), BusinessDrugGoodsCategoryEntity::getCategoryId, businessDrugGoodsCategorySelectQueryParams.getCategoryId());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsCategorySelectQueryParams.getCategoryName()), BusinessDrugGoodsCategoryEntity::getCategoryName, businessDrugGoodsCategorySelectQueryParams.getCategoryName());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsCategorySelectQueryParams.getStatus()), BusinessDrugGoodsCategoryEntity::getStatus, businessDrugGoodsCategorySelectQueryParams.getStatus());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsCategorySelectQueryParams.getRemark()), BusinessDrugGoodsCategoryEntity::getRemark, businessDrugGoodsCategorySelectQueryParams.getRemark());
            return BeanUtil.copyToList(baseMapper.selectList(wrapper), BusinessDrugGoodsCategoryVO.class);
        }

        @Override
        public void save(@NotNull BusinessDrugGoodsCategoryAddParams businessDrugGoodsCategoryAddParams) {
            BusinessDrugGoodsCategoryEntity businessDrugGoodsCategoryEntity = BeanUtil.copyProperties(businessDrugGoodsCategoryAddParams, BusinessDrugGoodsCategoryEntity.class);
            baseMapper.insert(businessDrugGoodsCategoryEntity);
        }

        @Override
        public void update(@NotNull BusinessDrugGoodsCategoryUpdateParams businessDrugGoodsCategoryUpdateParams) {
            BusinessDrugGoodsCategoryEntity businessDrugGoodsCategoryEntity = BeanUtil.copyProperties(businessDrugGoodsCategoryUpdateParams, BusinessDrugGoodsCategoryEntity.class);
            baseMapper.updateById(businessDrugGoodsCategoryEntity);
        }

        @Override
        public BusinessDrugGoodsCategoryVO getById(Long id) {
            return BeanUtil.copyProperties(baseMapper.selectById(id), BusinessDrugGoodsCategoryVO.class);
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
