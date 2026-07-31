package com.zs.lawyer.contact.contactCategory.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.contact.contactCategory.domain.entity.ContactCategoryEntity;
import com.zs.lawyer.contact.contactCategory.domain.params.ContactCategoryAddParams;
import com.zs.lawyer.contact.contactCategory.domain.params.ContactCategoryPageQueryParams;
import com.zs.lawyer.contact.contactCategory.domain.params.ContactCategorySelectQueryParams;
import com.zs.lawyer.contact.contactCategory.domain.params.ContactCategoryUpdateParams;
import com.zs.lawyer.contact.contactCategory.domain.vo.ContactCategoryVO;
import com.zs.lawyer.contact.contactCategory.mapper.ContactCategoryMapper;
import com.zs.lawyer.contact.contactCategory.service.ContactCategoryService;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 通讯录分类 服务实现类
 * </p>
 *
 * @author zs
 * @since 2025-08-26 10:34:29
 */
@Service
public class ContactCategoryServiceImpl extends ServiceImpl<ContactCategoryMapper, ContactCategoryEntity> implements ContactCategoryService {

        @Override
        public PageResult<ContactCategoryVO> page(@NotNull ContactCategoryPageQueryParams contactCategoryPageQueryParams) {

            Page<ContactCategoryEntity> page = new PageInfo<>(contactCategoryPageQueryParams);
            QueryWrapper<ContactCategoryEntity> wrapper = new QueryWrapper<>();

            IPage<ContactCategoryEntity> iPage = baseMapper.selectPage(page, wrapper);
            List<ContactCategoryVO> list = BeanUtil.copyToList(iPage.getRecords(), ContactCategoryVO.class);

            return new PageResult<>(list, page.getTotal(), ContactCategoryVO.class);
        }

        @Override
        public List<ContactCategoryVO> getList(@NotNull ContactCategorySelectQueryParams contactCategorySelectQueryParams) {
            QueryWrapper<ContactCategoryEntity> wrapper = new QueryWrapper<>();
            return BeanUtil.copyToList(baseMapper.selectList(wrapper), ContactCategoryVO.class);
        }

        @Override
        public void save(@NotNull ContactCategoryAddParams contactCategoryAddParams) {
            ContactCategoryEntity contactCategoryEntity = BeanUtil.copyProperties(contactCategoryAddParams, ContactCategoryEntity.class);
            baseMapper.insert(contactCategoryEntity);
        }

        @Override
        public void update(@NotNull ContactCategoryUpdateParams contactCategoryUpdateParams) {
            ContactCategoryEntity contactCategoryEntity = BeanUtil.copyProperties(contactCategoryUpdateParams, ContactCategoryEntity.class);
            baseMapper.updateById(contactCategoryEntity);
        }

        @Override
        public ContactCategoryVO getById(Long id) {
            ContactCategoryVO contactCategoryVO = BeanUtil.copyProperties(baseMapper.selectById(id), ContactCategoryVO.class);
            return contactCategoryVO;
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