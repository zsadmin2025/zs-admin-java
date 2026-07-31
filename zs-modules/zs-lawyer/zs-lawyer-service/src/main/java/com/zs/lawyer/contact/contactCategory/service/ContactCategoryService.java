package com.zs.lawyer.contact.contactCategory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.contact.contactCategory.domain.entity.ContactCategoryEntity;
import com.zs.lawyer.contact.contactCategory.domain.params.ContactCategoryAddParams;
import com.zs.lawyer.contact.contactCategory.domain.params.ContactCategoryPageQueryParams;
import com.zs.lawyer.contact.contactCategory.domain.params.ContactCategorySelectQueryParams;
import com.zs.lawyer.contact.contactCategory.domain.params.ContactCategoryUpdateParams;
import com.zs.lawyer.contact.contactCategory.domain.vo.ContactCategoryVO;

import java.util.List;

/**
 * <p>
 * 通讯录分类 服务类
 * </p>
 *
 * @author zs
 * @since 2025-08-26 10:34:29
 */
public interface ContactCategoryService extends IService<ContactCategoryEntity> {

    /** 分页 **/
    PageResult<ContactCategoryVO> page(ContactCategoryPageQueryParams contactCategoryPageQueryParams);

    /** 列表 **/
    List<ContactCategoryVO> getList(ContactCategorySelectQueryParams contactCategorySelectQueryParams);

    /** 新增 **/
    void save(ContactCategoryAddParams contactCategoryAddParams);

    /** 更新 **/
    void update(ContactCategoryUpdateParams contactCategoryUpdateParams);

    /** 根据id查询 **/
    ContactCategoryVO getById(Long id);

    /** 单个删除 **/
    void deleteById(Long contactCategoryId);

    /** 批量删除 **/
    void batchDelById(Long[] contactCategoryIds);
}