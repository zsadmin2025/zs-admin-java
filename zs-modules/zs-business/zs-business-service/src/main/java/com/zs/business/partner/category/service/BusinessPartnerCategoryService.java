package com.zs.business.partner.category.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.business.partner.category.domain.entity.BusinessPartnerCategoryEntity;
import com.zs.business.partner.category.domain.vo.BusinessPartnerCategoryVO;
import com.zs.business.partner.category.domain.params.BusinessPartnerCategoryPageQueryParams;
import com.zs.business.partner.category.domain.params.BusinessPartnerCategorySelectQueryParams;
import com.zs.business.partner.category.domain.params.BusinessPartnerCategoryAddParams;
import com.zs.business.partner.category.domain.params.BusinessPartnerCategoryUpdateParams;

import java.util.List;

/**
 * <p>
 * 单位分类 服务类
 * </p>
 *
 * @author zs
 * {@code @date} 2026-08-02 09:15:47
 */
public interface BusinessPartnerCategoryService extends IService<BusinessPartnerCategoryEntity> {

    /**
     * 分页查询
     * @param businessPartnerCategoryPageQueryParams 查询参数
     * @return PageResult<BusinessPartnerCategoryVO>
     */
    PageResult<BusinessPartnerCategoryVO> page(BusinessPartnerCategoryPageQueryParams businessPartnerCategoryPageQueryParams);

    /**
     * 查询列表
     * @param businessPartnerCategorySelectQueryParams 查询参数
     * @return List<BusinessPartnerCategoryVO>
     */
    List<BusinessPartnerCategoryVO> getList(BusinessPartnerCategorySelectQueryParams businessPartnerCategorySelectQueryParams);

    /**
     * 新增
     * @param businessPartnerCategoryAddParams 新增参数
     */
    void save(BusinessPartnerCategoryAddParams businessPartnerCategoryAddParams);

    /**
     * 更新
     * @param businessPartnerCategoryUpdateParams 更新参数
     */
    void update(BusinessPartnerCategoryUpdateParams businessPartnerCategoryUpdateParams);

    /**
     * 根据id查询
     * @param id 主键
     * @return BusinessPartnerCategoryVO
     */
    BusinessPartnerCategoryVO getById(Long id);

    /**
     * 单个删除
     * @param businessPartnerCategoryId 主键
     */
    void deleteById(Long businessPartnerCategoryId);

    /**
     * 批量删除
     * @param businessPartnerCategoryIds 主键数组
     */
    void batchDelById(Long[] businessPartnerCategoryIds);
}
