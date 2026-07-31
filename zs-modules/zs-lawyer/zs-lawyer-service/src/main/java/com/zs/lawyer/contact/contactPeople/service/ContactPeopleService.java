package com.zs.lawyer.contact.contactPeople.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.contact.contactPeople.domain.entity.ContactPeopleEntity;
import com.zs.lawyer.contact.contactPeople.domain.params.ContactPeopleAddParams;
import com.zs.lawyer.contact.contactPeople.domain.params.ContactPeoplePageQueryParams;
import com.zs.lawyer.contact.contactPeople.domain.params.ContactPeopleSelectQueryParams;
import com.zs.lawyer.contact.contactPeople.domain.params.ContactPeopleUpdateParams;
import com.zs.lawyer.contact.contactPeople.domain.vo.ContactPeopleVO;

import java.util.List;

/**
 * <p>
 * 通讯录联系人 服务类
 * </p>
 *
 * @author zs
 * @since 2025-08-26 10:42:27
 */
public interface ContactPeopleService extends IService<ContactPeopleEntity> {

    /** 分页 **/
    PageResult<ContactPeopleVO> page(ContactPeoplePageQueryParams contactPeoplePageQueryParams);

    /** 列表 **/
    List<ContactPeopleVO> getList(ContactPeopleSelectQueryParams contactPeopleSelectQueryParams);

    /** 新增 **/
    void save(ContactPeopleAddParams contactPeopleAddParams);

    /** 更新 **/
    void update(ContactPeopleUpdateParams contactPeopleUpdateParams);

    /** 根据id查询 **/
    ContactPeopleVO getById(Long id);

    /** 单个删除 **/
    void deleteById(Long contactPeopleId);

    /** 批量删除 **/
    void batchDelById(Long[] contactPeopleIds);
}