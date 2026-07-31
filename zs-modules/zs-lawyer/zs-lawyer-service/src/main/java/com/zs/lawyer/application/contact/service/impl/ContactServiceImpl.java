package com.zs.lawyer.application.contact.service.impl;

import com.zs.lawyer.application.contact.domain.entity.ContactEntity;
import com.zs.lawyer.application.contact.mapper.ContactMapper;
import com.zs.lawyer.application.contact.service.ContactService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.application.contact.domain.vo.ContactVO;
import com.zs.lawyer.application.contact.domain.params.ContactPageQueryParams;
import com.zs.lawyer.application.contact.domain.params.ContactSelectQueryParams;
import com.zs.lawyer.application.contact.domain.params.ContactAddParams;
import com.zs.lawyer.application.contact.domain.params.ContactUpdateParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.constraints.NotNull;
import cn.hutool.core.util.ObjectUtil;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 通讯录联系人表 服务实现类
 * </p>
 *
 * @author zs
 * {@code @date} 2026-07-30 18:37:41
 */
@Service
public class ContactServiceImpl extends ServiceImpl<ContactMapper, ContactEntity> implements ContactService {

        @Override
        public PageResult<ContactVO> page(@NotNull ContactPageQueryParams contactPageQueryParams) {
            Page<ContactEntity> page = new PageInfo<>(contactPageQueryParams);
            LambdaQueryWrapper<ContactEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ObjectUtil.isNotNull(contactPageQueryParams.getContactId()), ContactEntity::getContactId, contactPageQueryParams.getContactId());
            wrapper.eq(ObjectUtil.isNotNull(contactPageQueryParams.getName()), ContactEntity::getName, contactPageQueryParams.getName());
            wrapper.eq(ObjectUtil.isNotNull(contactPageQueryParams.getPhone()), ContactEntity::getPhone, contactPageQueryParams.getPhone());
            wrapper.eq(ObjectUtil.isNotNull(contactPageQueryParams.getGender()), ContactEntity::getGender, contactPageQueryParams.getGender());
            wrapper.eq(ObjectUtil.isNotNull(contactPageQueryParams.getGroupType()), ContactEntity::getGroupType, contactPageQueryParams.getGroupType());
            wrapper.eq(ObjectUtil.isNotNull(contactPageQueryParams.getScopeType()), ContactEntity::getScopeType, contactPageQueryParams.getScopeType());
            wrapper.eq(ObjectUtil.isNotNull(contactPageQueryParams.getRemark()), ContactEntity::getRemark, contactPageQueryParams.getRemark());
            IPage<ContactEntity> iPage = baseMapper.selectPage(page, wrapper);
            List<ContactVO> list = BeanUtil.copyToList(iPage.getRecords(), ContactVO.class);

            return new PageResult<>(list, page.getTotal(), ContactVO.class);
        }

        @Override
        public List<ContactVO> getList(@NotNull ContactSelectQueryParams contactSelectQueryParams) {
            LambdaQueryWrapper<ContactEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ObjectUtil.isNotNull(contactSelectQueryParams.getContactId()), ContactEntity::getContactId, contactSelectQueryParams.getContactId());
            wrapper.eq(ObjectUtil.isNotNull(contactSelectQueryParams.getName()), ContactEntity::getName, contactSelectQueryParams.getName());
            wrapper.eq(ObjectUtil.isNotNull(contactSelectQueryParams.getPhone()), ContactEntity::getPhone, contactSelectQueryParams.getPhone());
            wrapper.eq(ObjectUtil.isNotNull(contactSelectQueryParams.getGender()), ContactEntity::getGender, contactSelectQueryParams.getGender());
            wrapper.eq(ObjectUtil.isNotNull(contactSelectQueryParams.getGroupType()), ContactEntity::getGroupType, contactSelectQueryParams.getGroupType());
            wrapper.eq(ObjectUtil.isNotNull(contactSelectQueryParams.getScopeType()), ContactEntity::getScopeType, contactSelectQueryParams.getScopeType());
            wrapper.eq(ObjectUtil.isNotNull(contactSelectQueryParams.getRemark()), ContactEntity::getRemark, contactSelectQueryParams.getRemark());
            return BeanUtil.copyToList(baseMapper.selectList(wrapper), ContactVO.class);
        }

        @Override
        public void save(@NotNull ContactAddParams contactAddParams) {
            ContactEntity contactEntity = BeanUtil.copyProperties(contactAddParams, ContactEntity.class);
            baseMapper.insert(contactEntity);
        }

        @Override
        public void update(@NotNull ContactUpdateParams contactUpdateParams) {
            ContactEntity contactEntity = BeanUtil.copyProperties(contactUpdateParams, ContactEntity.class);
            baseMapper.updateById(contactEntity);
        }

        @Override
        public ContactVO getById(Long id) {
            return BeanUtil.copyProperties(baseMapper.selectById(id), ContactVO.class);
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
