package com.zs.business.partner.category.service.impl;

import com.zs.business.partner.category.domain.entity.BusinessPartnerCategoryEntity;
import com.zs.business.partner.category.mapper.BusinessPartnerCategoryMapper;
import com.zs.business.partner.category.service.BusinessPartnerCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.sys.api.role.RemoteUserService;
import org.springframework.stereotype.Service;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.business.partner.category.domain.vo.BusinessPartnerCategoryVO;
import com.zs.business.partner.category.domain.params.BusinessPartnerCategoryPageQueryParams;
import com.zs.business.partner.category.domain.params.BusinessPartnerCategorySelectQueryParams;
import com.zs.business.partner.category.domain.params.BusinessPartnerCategoryAddParams;
import com.zs.business.partner.category.domain.params.BusinessPartnerCategoryUpdateParams;
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

/**
 * <p>
 * 单位分类 服务实现类
 * </p>
 *
 * @author zs
 * {@code @date} 2026-08-02 09:15:47
 */
@Service
public class BusinessPartnerCategoryServiceImpl extends ServiceImpl<BusinessPartnerCategoryMapper, BusinessPartnerCategoryEntity> implements BusinessPartnerCategoryService {

    @Resource
    private RemoteUserService remoteUserService;

    @Override
    public PageResult<BusinessPartnerCategoryVO> page(@NotNull BusinessPartnerCategoryPageQueryParams businessPartnerCategoryPageQueryParams) {
        Page<BusinessPartnerCategoryEntity> page = new PageInfo<>(businessPartnerCategoryPageQueryParams);
        LambdaQueryWrapper<BusinessPartnerCategoryEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ObjectUtil.isNotEmpty(businessPartnerCategoryPageQueryParams.getBusinessPartnerCategoryId()), BusinessPartnerCategoryEntity::getBusinessPartnerCategoryId, businessPartnerCategoryPageQueryParams.getBusinessPartnerCategoryId());
        wrapper.like(ObjectUtil.isNotEmpty(businessPartnerCategoryPageQueryParams.getPartnerCategoryName()), BusinessPartnerCategoryEntity::getPartnerCategoryName, businessPartnerCategoryPageQueryParams.getPartnerCategoryName());
        wrapper.eq(ObjectUtil.isNotEmpty(businessPartnerCategoryPageQueryParams.getStatus()), BusinessPartnerCategoryEntity::getStatus, businessPartnerCategoryPageQueryParams.getStatus());

        IPage<BusinessPartnerCategoryEntity> iPage = baseMapper.selectPage(page, wrapper);
        List<BusinessPartnerCategoryVO> list = BeanUtil.copyToList(iPage.getRecords(), BusinessPartnerCategoryVO.class);

        // 批量查询创建人姓名
        List<Long> creatorIds = iPage.getRecords().stream().map(BusinessPartnerCategoryEntity::getCreator).filter(ObjectUtil::isNotNull).distinct().toList();
        if (!creatorIds.isEmpty()) {
            Map<Long, String> userNameMap = remoteUserService.getUserNameMap(creatorIds);
            for (int i = 0; i < list.size(); i++) {
                BusinessPartnerCategoryEntity entity = iPage.getRecords().get(i);
                if (entity.getCreator() != null) {
                    list.get(i).setCreatorName(userNameMap.get(entity.getCreator()));
                }
            }
        }

        return new PageResult<>(list, page.getTotal(), BusinessPartnerCategoryVO.class);
    }

    @Override
    public List<BusinessPartnerCategoryVO> getList(@NotNull BusinessPartnerCategorySelectQueryParams businessPartnerCategorySelectQueryParams) {
        LambdaQueryWrapper<BusinessPartnerCategoryEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ObjectUtil.isNotEmpty(businessPartnerCategorySelectQueryParams.getBusinessPartnerCategoryId()), BusinessPartnerCategoryEntity::getBusinessPartnerCategoryId, businessPartnerCategorySelectQueryParams.getBusinessPartnerCategoryId());
        wrapper.like(ObjectUtil.isNotEmpty(businessPartnerCategorySelectQueryParams.getPartnerCategoryName()), BusinessPartnerCategoryEntity::getPartnerCategoryName, businessPartnerCategorySelectQueryParams.getPartnerCategoryName());

        wrapper.eq(ObjectUtil.isNotEmpty(businessPartnerCategorySelectQueryParams.getStatus()), BusinessPartnerCategoryEntity::getStatus, businessPartnerCategorySelectQueryParams.getStatus());
        return BeanUtil.copyToList(baseMapper.selectList(wrapper), BusinessPartnerCategoryVO.class);
    }

    @Override
    public void save(@NotNull BusinessPartnerCategoryAddParams businessPartnerCategoryAddParams) {
        BusinessPartnerCategoryEntity businessPartnerCategoryEntity = BeanUtil.copyProperties(businessPartnerCategoryAddParams, BusinessPartnerCategoryEntity.class);
        baseMapper.insert(businessPartnerCategoryEntity);
    }

    @Override
    public void update(@NotNull BusinessPartnerCategoryUpdateParams businessPartnerCategoryUpdateParams) {
        BusinessPartnerCategoryEntity businessPartnerCategoryEntity = BeanUtil.copyProperties(businessPartnerCategoryUpdateParams, BusinessPartnerCategoryEntity.class);
        baseMapper.updateById(businessPartnerCategoryEntity);
    }

    @Override
    public BusinessPartnerCategoryVO getById(Long id) {
        return BeanUtil.copyProperties(baseMapper.selectById(id), BusinessPartnerCategoryVO.class);
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
