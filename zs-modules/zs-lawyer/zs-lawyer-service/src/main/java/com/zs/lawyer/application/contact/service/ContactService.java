package com.zs.lawyer.application.contact.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.application.contact.domain.entity.ContactEntity;
import com.zs.lawyer.application.contact.domain.vo.ContactVO;
import com.zs.lawyer.application.contact.domain.params.ContactPageQueryParams;
import com.zs.lawyer.application.contact.domain.params.ContactSelectQueryParams;
import com.zs.lawyer.application.contact.domain.params.ContactAddParams;
import com.zs.lawyer.application.contact.domain.params.ContactUpdateParams;

import java.util.List;

/**
 * <p>
 * 通讯录联系人表 服务类
 * </p>
 *
 * @author zs
 * {@code @date} 2026-07-30 18:37:41
 */
public interface ContactService extends IService<ContactEntity> {

    /**
     * 分页查询
     * @param contactPageQueryParams 查询参数
     * @return PageResult<ContactVO>
     */
    PageResult<ContactVO> page(ContactPageQueryParams contactPageQueryParams);

    /**
     * 查询列表
     * @param contactSelectQueryParams 查询参数
     * @return List<ContactVO>
     */
    List<ContactVO> getList(ContactSelectQueryParams contactSelectQueryParams);

    /**
     * 新增
     * @param contactAddParams 新增参数
     */
    void save(ContactAddParams contactAddParams);

    /**
     * 更新
     * @param contactUpdateParams 更新参数
     */
    void update(ContactUpdateParams contactUpdateParams);

    /**
     * 根据id查询
     * @param id 主键
     * @return ContactVO
     */
    ContactVO getById(Long id);

    /**
     * 单个删除
     * @param contactId 主键
     */
    void deleteById(Long contactId);

    /**
     * 批量删除
     * @param contactIds 主键数组
     */
    void batchDelById(Long[] contactIds);
}
