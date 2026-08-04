package com.zs.business.partner.partner.service.impl;

import com.zs.business.partner.partner.domain.entity.BusinessPartnerEntity;
import com.zs.business.partner.partner.mapper.BusinessPartnerMapper;
import com.zs.business.partner.partner.service.BusinessPartnerService;
import com.zs.business.partner.category.domain.entity.BusinessPartnerCategoryEntity;
import com.zs.business.partner.category.mapper.BusinessPartnerCategoryMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.business.partner.partner.domain.vo.BusinessPartnerVO;
import com.zs.business.partner.partner.domain.params.BusinessPartnerPageQueryParams;
import com.zs.business.partner.partner.domain.params.BusinessPartnerSelectQueryParams;
import com.zs.business.partner.partner.domain.params.BusinessPartnerAddParams;
import com.zs.business.partner.partner.domain.params.BusinessPartnerUpdateParams;
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
import java.util.stream.Collectors;

/**
 * <p>
 * 往来单位 服务实现类
 * </p>
 *
 * @author zs
 * {@code @date} 2026-08-02 11:23:09
 */
@Service
public class BusinessPartnerServiceImpl extends ServiceImpl<BusinessPartnerMapper, BusinessPartnerEntity> implements BusinessPartnerService {

        /** 单位分类 Mapper，用于查询分类名称 */
        @Resource
        private BusinessPartnerCategoryMapper businessPartnerCategoryMapper;

        @Override
        public PageResult<BusinessPartnerVO> page(@NotNull BusinessPartnerPageQueryParams businessPartnerPageQueryParams) {
            Page<BusinessPartnerEntity> page = new PageInfo<>(businessPartnerPageQueryParams);
            LambdaQueryWrapper<BusinessPartnerEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.like(ObjectUtil.isNotEmpty(businessPartnerPageQueryParams.getCompanyName()), BusinessPartnerEntity::getCompanyName, businessPartnerPageQueryParams.getCompanyName());
            wrapper.eq(ObjectUtil.isNotEmpty(businessPartnerPageQueryParams.getPartnerType()), BusinessPartnerEntity::getPartnerType, businessPartnerPageQueryParams.getPartnerType());
            wrapper.eq(ObjectUtil.isNotEmpty(businessPartnerPageQueryParams.getStatus()), BusinessPartnerEntity::getStatus, businessPartnerPageQueryParams.getStatus());
            IPage<BusinessPartnerEntity> iPage = baseMapper.selectPage(page, wrapper);
            List<BusinessPartnerVO> list = BeanUtil.copyToList(iPage.getRecords(), BusinessPartnerVO.class);

            // 批量查询分类名称并回填到 VO
            fillPartnerCategoryName(list, iPage.getRecords());

            return new PageResult<>(list, page.getTotal(), BusinessPartnerVO.class);
        }

        /**
         * 批量查询合作方分类名称并回填到 VO 列表
         *
         * @param voList   VO 列表
         * @param entityList 对应的实体列表（用于获取 partnerCategoryId）
         */
        private void fillPartnerCategoryName(List<BusinessPartnerVO> voList, List<BusinessPartnerEntity> entityList) {
            // 收集所有非空的分类ID，去重
            List<Long> categoryIds = entityList.stream()
                    .map(BusinessPartnerEntity::getPartnerCategoryId)
                    .filter(ObjectUtil::isNotNull)
                    .distinct()
                    .toList();
            if (categoryIds.isEmpty()) {
                return;
            }
            // 批量查询分类实体，构建 ID -> 分类名称 的映射
            List<BusinessPartnerCategoryEntity> categoryEntities = businessPartnerCategoryMapper.selectBatchIds(categoryIds);
            Map<Long, String> categoryNameMap = categoryEntities.stream()
                    .collect(Collectors.toMap(
                            BusinessPartnerCategoryEntity::getBusinessPartnerCategoryId,
                            BusinessPartnerCategoryEntity::getPartnerCategoryName));
            // 回填分类名称
            for (int i = 0; i < voList.size(); i++) {
                BusinessPartnerEntity entity = entityList.get(i);
                if (entity.getPartnerCategoryId() != null) {
                    voList.get(i).setPartnerCategoryName(categoryNameMap.get(entity.getPartnerCategoryId()));
                }
            }
        }

        @Override
        public List<BusinessPartnerVO> getList(@NotNull BusinessPartnerSelectQueryParams businessPartnerSelectQueryParams) {
            LambdaQueryWrapper<BusinessPartnerEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.like(ObjectUtil.isNotEmpty(businessPartnerSelectQueryParams.getCompanyName()), BusinessPartnerEntity::getCompanyName, businessPartnerSelectQueryParams.getCompanyName());
            wrapper.eq(ObjectUtil.isNotEmpty(businessPartnerSelectQueryParams.getPartnerType()), BusinessPartnerEntity::getPartnerType, businessPartnerSelectQueryParams.getPartnerType());
            wrapper.eq(ObjectUtil.isNotEmpty(businessPartnerSelectQueryParams.getStatus()), BusinessPartnerEntity::getStatus, businessPartnerSelectQueryParams.getStatus());
            return BeanUtil.copyToList(baseMapper.selectList(wrapper), BusinessPartnerVO.class);
        }

        @Override
        public void save(@NotNull BusinessPartnerAddParams businessPartnerAddParams) {
            BusinessPartnerEntity businessPartnerEntity = BeanUtil.copyProperties(businessPartnerAddParams, BusinessPartnerEntity.class);
            baseMapper.insert(businessPartnerEntity);
        }

        @Override
        public void update(@NotNull BusinessPartnerUpdateParams businessPartnerUpdateParams) {
            BusinessPartnerEntity businessPartnerEntity = BeanUtil.copyProperties(businessPartnerUpdateParams, BusinessPartnerEntity.class);
            baseMapper.updateById(businessPartnerEntity);
        }

        @Override
        public BusinessPartnerVO getById(Long id) {
            BusinessPartnerVO businessPartnerVO = BeanUtil.copyProperties(baseMapper.selectById(id), BusinessPartnerVO.class);
            return businessPartnerVO;
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
