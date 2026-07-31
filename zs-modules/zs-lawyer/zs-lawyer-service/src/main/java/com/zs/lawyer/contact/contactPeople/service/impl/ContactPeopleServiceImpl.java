package com.zs.lawyer.contact.contactPeople.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.contact.contactPeople.domain.entity.ContactPeopleEntity;
import com.zs.lawyer.contact.contactPeople.domain.params.ContactPeopleAddParams;
import com.zs.lawyer.contact.contactPeople.domain.params.ContactPeoplePageQueryParams;
import com.zs.lawyer.contact.contactPeople.domain.params.ContactPeopleSelectQueryParams;
import com.zs.lawyer.contact.contactPeople.domain.params.ContactPeopleUpdateParams;
import com.zs.lawyer.contact.contactPeople.domain.vo.ContactPeopleVO;
import com.zs.lawyer.contact.contactPeople.mapper.ContactPeopleMapper;
import com.zs.lawyer.contact.contactPeople.service.ContactPeopleService;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 通讯录联系人 服务实现类
 * </p>
 *
 * @author zs
 * @since 2025-08-26 10:42:27
 */
@Service
public class ContactPeopleServiceImpl extends ServiceImpl<ContactPeopleMapper, ContactPeopleEntity> implements ContactPeopleService {

        @Override
        public PageResult<ContactPeopleVO> page(@NotNull ContactPeoplePageQueryParams contactPeoplePageQueryParams) {

            Page<ContactPeopleEntity> page = new PageInfo<>(contactPeoplePageQueryParams);
            QueryWrapper<ContactPeopleEntity> wrapper = new QueryWrapper<>();

            IPage<ContactPeopleEntity> iPage = baseMapper.selectPage(page, wrapper);
            List<ContactPeopleVO> list = BeanUtil.copyToList(iPage.getRecords(), ContactPeopleVO.class);

            return new PageResult<>(list, page.getTotal(), ContactPeopleVO.class);
        }

        @Override
        public List<ContactPeopleVO> getList(@NotNull ContactPeopleSelectQueryParams contactPeopleSelectQueryParams) {
            QueryWrapper<ContactPeopleEntity> wrapper = new QueryWrapper<>();
            return BeanUtil.copyToList(baseMapper.selectList(wrapper), ContactPeopleVO.class);
        }

        @Override
        public void save(@NotNull ContactPeopleAddParams contactPeopleAddParams) {
            ContactPeopleEntity contactPeopleEntity = BeanUtil.copyProperties(contactPeopleAddParams, ContactPeopleEntity.class);
            baseMapper.insert(contactPeopleEntity);
        }

        @Override
        public void update(@NotNull ContactPeopleUpdateParams contactPeopleUpdateParams) {
            ContactPeopleEntity contactPeopleEntity = BeanUtil.copyProperties(contactPeopleUpdateParams, ContactPeopleEntity.class);
            baseMapper.updateById(contactPeopleEntity);
        }

        @Override
        public ContactPeopleVO getById(Long id) {
            ContactPeopleVO contactPeopleVO = BeanUtil.copyProperties(baseMapper.selectById(id), ContactPeopleVO.class);
            return contactPeopleVO;
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