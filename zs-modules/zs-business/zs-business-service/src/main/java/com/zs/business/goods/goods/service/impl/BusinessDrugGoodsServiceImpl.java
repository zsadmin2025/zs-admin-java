package com.zs.business.goods.goods.service.impl;

import com.zs.business.goods.cert.domain.entity.BusinessDrugGoodsCertEntity;
import com.zs.business.goods.cert.domain.params.BusinessDrugGoodsCertSelectQueryParams;
import com.zs.business.goods.cert.domain.params.BusinessDrugGoodsCertUpdateParams;
import com.zs.business.goods.category.service.BusinessDrugGoodsCategoryService;
import com.zs.business.partner.partner.service.BusinessPartnerService;
import com.zs.business.goods.cert.service.BusinessDrugGoodsCertService;
import com.zs.business.goods.goods.domain.entity.BusinessDrugGoodsEntity;
import com.zs.business.goods.goods.mapper.BusinessDrugGoodsMapper;
import com.zs.business.goods.goods.service.BusinessDrugGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.business.goods.goods.domain.vo.BusinessDrugGoodsVO;
import com.zs.business.goods.goods.domain.params.BusinessDrugGoodsPageQueryParams;
import com.zs.business.goods.goods.domain.params.BusinessDrugGoodsSelectQueryParams;
import com.zs.business.goods.goods.domain.params.BusinessDrugGoodsAddParams;
import com.zs.business.goods.goods.domain.params.BusinessDrugGoodsUpdateParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 药品商品主信息表 服务实现类
 * </p>
 *
 * @author zs
 * {@code @date} 2026-07-31 19:22:08
 */
@Service
public class BusinessDrugGoodsServiceImpl extends ServiceImpl<BusinessDrugGoodsMapper, BusinessDrugGoodsEntity> implements BusinessDrugGoodsService {

        @Resource
        private BusinessDrugGoodsCertService businessDrugGoodsCertService;

        @Resource
        private BusinessDrugGoodsCategoryService businessDrugGoodsCategoryService;

        @Resource
        private BusinessPartnerService businessPartnerService;

        @Override
        public PageResult<BusinessDrugGoodsVO> page(BusinessDrugGoodsPageQueryParams businessDrugGoodsPageQueryParams) {
            Page<BusinessDrugGoodsEntity> page = new PageInfo<>(businessDrugGoodsPageQueryParams);
            LambdaQueryWrapper<BusinessDrugGoodsEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsPageQueryParams.getGoodsCategoryId()), BusinessDrugGoodsEntity::getGoodsCategoryId, businessDrugGoodsPageQueryParams.getGoodsCategoryId());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsPageQueryParams.getGoodsSn()), BusinessDrugGoodsEntity::getGoodsSn, businessDrugGoodsPageQueryParams.getGoodsSn());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsPageQueryParams.getCommonName()), BusinessDrugGoodsEntity::getCommonName, businessDrugGoodsPageQueryParams.getCommonName());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsPageQueryParams.getGoodsName()), BusinessDrugGoodsEntity::getGoodsName, businessDrugGoodsPageQueryParams.getGoodsName());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsPageQueryParams.getCommonNamePinyin()), BusinessDrugGoodsEntity::getCommonNamePinyin, businessDrugGoodsPageQueryParams.getCommonNamePinyin());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsPageQueryParams.getFileNo()), BusinessDrugGoodsEntity::getFileNo, businessDrugGoodsPageQueryParams.getFileNo());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsPageQueryParams.getNationalCode()), BusinessDrugGoodsEntity::getNationalCode, businessDrugGoodsPageQueryParams.getNationalCode());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsPageQueryParams.getManufacturerId()), BusinessDrugGoodsEntity::getManufacturerId, businessDrugGoodsPageQueryParams.getManufacturerId());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsPageQueryParams.getStatus()), BusinessDrugGoodsEntity::getStatus, businessDrugGoodsPageQueryParams.getStatus());
            IPage<BusinessDrugGoodsEntity> iPage = baseMapper.selectPage(page, wrapper);
            List<BusinessDrugGoodsVO> list = BeanUtil.copyToList(iPage.getRecords(), BusinessDrugGoodsVO.class);

            // 批量回填关联名称（商品类别、生产厂家、最近供应商）
            fillRelatedNames(list);

            return new PageResult<>(list, page.getTotal(), BusinessDrugGoodsVO.class);
        }

        @Override
        public List<BusinessDrugGoodsVO> getList(BusinessDrugGoodsSelectQueryParams businessDrugGoodsSelectQueryParams) {
            LambdaQueryWrapper<BusinessDrugGoodsEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsSelectQueryParams.getGoodsCategoryId()), BusinessDrugGoodsEntity::getGoodsCategoryId, businessDrugGoodsSelectQueryParams.getGoodsCategoryId());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsSelectQueryParams.getGoodsSn()), BusinessDrugGoodsEntity::getGoodsSn, businessDrugGoodsSelectQueryParams.getGoodsSn());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsSelectQueryParams.getCommonName()), BusinessDrugGoodsEntity::getCommonName, businessDrugGoodsSelectQueryParams.getCommonName());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsSelectQueryParams.getGoodsName()), BusinessDrugGoodsEntity::getGoodsName, businessDrugGoodsSelectQueryParams.getGoodsName());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsSelectQueryParams.getTraceCode()), BusinessDrugGoodsEntity::getTraceCode, businessDrugGoodsSelectQueryParams.getTraceCode());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsSelectQueryParams.getCommonNamePinyin()), BusinessDrugGoodsEntity::getCommonNamePinyin, businessDrugGoodsSelectQueryParams.getCommonNamePinyin());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsSelectQueryParams.getFileNo()), BusinessDrugGoodsEntity::getFileNo, businessDrugGoodsSelectQueryParams.getFileNo());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsSelectQueryParams.getNationalCode()), BusinessDrugGoodsEntity::getNationalCode, businessDrugGoodsSelectQueryParams.getNationalCode());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsSelectQueryParams.getManufacturerId()), BusinessDrugGoodsEntity::getManufacturerId, businessDrugGoodsSelectQueryParams.getManufacturerId());
            wrapper.eq(ObjectUtil.isNotEmpty(businessDrugGoodsSelectQueryParams.getStatus()), BusinessDrugGoodsEntity::getStatus, businessDrugGoodsSelectQueryParams.getStatus());
            List<BusinessDrugGoodsVO> list = BeanUtil.copyToList(baseMapper.selectList(wrapper), BusinessDrugGoodsVO.class);

            // 批量回填关联名称
            fillRelatedNames(list);

            return list;
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public void save(BusinessDrugGoodsAddParams businessDrugGoodsAddParams) {
            BusinessDrugGoodsEntity businessDrugGoodsEntity = BeanUtil.copyProperties(businessDrugGoodsAddParams, BusinessDrugGoodsEntity.class);
            baseMapper.insert(businessDrugGoodsEntity);

            // 批量保存商品证照附件
            batchSaveCerts(businessDrugGoodsEntity.getDrugGoodsId(), businessDrugGoodsAddParams.getCerts());
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public void update(BusinessDrugGoodsUpdateParams businessDrugGoodsUpdateParams) {
            BusinessDrugGoodsEntity businessDrugGoodsEntity = BeanUtil.copyProperties(businessDrugGoodsUpdateParams, BusinessDrugGoodsEntity.class);
            baseMapper.updateById(businessDrugGoodsEntity);

            // 处理证照附件（逻辑删除移除的 + 更新/新增保留的）
            updateCerts(businessDrugGoodsEntity.getDrugGoodsId(), businessDrugGoodsUpdateParams.getCerts());
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public BusinessDrugGoodsVO getById(Long id) {
            BusinessDrugGoodsVO businessDrugGoodsVO = BeanUtil.copyProperties(baseMapper.selectById(id), BusinessDrugGoodsVO.class);

            BusinessDrugGoodsCertSelectQueryParams businessDrugGoodsCertSelectQueryParams = new BusinessDrugGoodsCertSelectQueryParams();
            businessDrugGoodsCertSelectQueryParams.setDrugGoodsId(businessDrugGoodsVO.getDrugGoodsId());
            businessDrugGoodsVO.setCerts(businessDrugGoodsCertService.getList(businessDrugGoodsCertSelectQueryParams));
            return businessDrugGoodsVO;
        }

        @Override
        public void deleteById(Long id) {
            baseMapper.deleteById(id);
        }

        @Override
        public void batchDelById(Long[] ids) {
            baseMapper.deleteByIds(Arrays.asList(ids));
        }

    /**
     * 新增时批量保存证照附件（新附件无主键ID，直接批量插入）
     * @param drugGoodsId 商品ID
     * @param certParamsList 证照参数列表
     */
    private void batchSaveCerts(Long drugGoodsId, List<?> certParamsList) {
        if (CollUtil.isEmpty(certParamsList)) {
            return;
        }
        List<BusinessDrugGoodsCertEntity> certs = new ArrayList<>();
        certParamsList.forEach(cert -> {
            BusinessDrugGoodsCertEntity certEntity = BeanUtil.copyProperties(cert, BusinessDrugGoodsCertEntity.class);
            certEntity.setDrugGoodsId(drugGoodsId);
            certs.add(certEntity);
        });
        businessDrugGoodsCertService.saveBatch(certs);
    }

    /**
     * 更新时处理证照附件（兼容逻辑删除）
     * 策略：逻辑删除传入列表中不存在的旧附件，saveOrUpdate处理传入的附件
     * @param drugGoodsId 商品ID
     * @param certParamsList 证照更新参数列表
     */
    private void updateCerts(Long drugGoodsId, List<BusinessDrugGoodsCertUpdateParams> certParamsList) {
        if (CollUtil.isEmpty(certParamsList)) {
            // 传入空列表：逻辑删除该商品下所有旧附件
            businessDrugGoodsCertService.remove(
                new LambdaQueryWrapper<BusinessDrugGoodsCertEntity>()
                    .eq(BusinessDrugGoodsCertEntity::getDrugGoodsId, drugGoodsId)
            );
            return;
        }

        // 收集传入的已有附件ID（非null的为已存在附件）
        List<Long> incomingIds = certParamsList.stream()
            .map(BusinessDrugGoodsCertUpdateParams::getDrugGoodsCertId)
            .filter(ObjectUtil::isNotNull)
            .toList();

        // 查询当前商品下所有未被逻辑删除的附件
        List<BusinessDrugGoodsCertEntity> existingCerts = businessDrugGoodsCertService.list(
            new LambdaQueryWrapper<BusinessDrugGoodsCertEntity>()
                .eq(BusinessDrugGoodsCertEntity::getDrugGoodsId, drugGoodsId)
        );

        // 逻辑删除不在传入列表中的旧附件
        if (CollUtil.isNotEmpty(existingCerts)) {
            List<Long> toDeleteIds = existingCerts.stream()
                .map(BusinessDrugGoodsCertEntity::getDrugGoodsCertId)
                .filter(id -> !incomingIds.contains(id))
                .toList();
            if (CollUtil.isNotEmpty(toDeleteIds)) {
                businessDrugGoodsCertService.removeByIds(toDeleteIds);
            }
        }

        // saveOrUpdateBatch：已有ID的更新，无ID的新增
        List<BusinessDrugGoodsCertEntity> certs = new ArrayList<>();
        certParamsList.forEach(cert -> {
            BusinessDrugGoodsCertEntity certEntity = BeanUtil.copyProperties(cert, BusinessDrugGoodsCertEntity.class);
            certEntity.setDrugGoodsId(drugGoodsId);
            certs.add(certEntity);
        });
        businessDrugGoodsCertService.saveOrUpdateBatch(certs);
    }

    /**
     * 批量回填关联名称（商品类别名称、生产厂家名称、最近供应商名称）
     * 避免 N+1 查询，批量收集 ID 后一次性查询映射
     * @param voList 商品VO列表
     */
    private void fillRelatedNames(List<BusinessDrugGoodsVO> voList) {
        if (CollUtil.isEmpty(voList)) {
            return;
        }

        // 收集所有关联ID（去重）
        Set<Long> categoryIds = voList.stream()
            .map(BusinessDrugGoodsVO::getGoodsCategoryId)
            .filter(ObjectUtil::isNotNull)
            .collect(Collectors.toSet());
        Set<Long> partnerIds = new HashSet<>();
        voList.forEach(vo -> {
            if (vo.getManufacturerId() != null) partnerIds.add(vo.getManufacturerId());
            if (vo.getLatestSupplierId() != null) partnerIds.add(vo.getLatestSupplierId());
        });

        // 批量查询商品类别 → ID→名称映射
        Map<Long, String> categoryNameMap = new HashMap<>();
        if (CollUtil.isNotEmpty(categoryIds)) {
            businessDrugGoodsCategoryService.listByIds(categoryIds)
                .forEach(e -> categoryNameMap.put(e.getCategoryId(), e.getCategoryName()));
        }

        // 批量查询合作方（生产厂家 & 供应商）→ ID→公司名称映射
        Map<Long, String> partnerNameMap = new HashMap<>();
        if (CollUtil.isNotEmpty(partnerIds)) {
            businessPartnerService.listByIds(partnerIds)
                .forEach(e -> partnerNameMap.put(e.getPartnerId(), e.getCompanyName()));
        }

        // 回填名称到每个 VO
        voList.forEach(vo -> {
            vo.setGoodsCategoryIdName(categoryNameMap.get(vo.getGoodsCategoryId()));
            vo.setManufacturerIdName(partnerNameMap.get(vo.getManufacturerId()));
            vo.setLatestSupplierIdName(partnerNameMap.get(vo.getLatestSupplierId()));
        });
    }
}
