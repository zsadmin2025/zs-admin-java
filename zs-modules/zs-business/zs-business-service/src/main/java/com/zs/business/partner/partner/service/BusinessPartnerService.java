package com.zs.business.partner.partner.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.business.partner.partner.domain.entity.BusinessPartnerEntity;
import com.zs.business.partner.partner.domain.vo.BusinessPartnerVO;
import com.zs.business.partner.partner.domain.params.BusinessPartnerPageQueryParams;
import com.zs.business.partner.partner.domain.params.BusinessPartnerSelectQueryParams;
import com.zs.business.partner.partner.domain.params.BusinessPartnerAddParams;
import com.zs.business.partner.partner.domain.params.BusinessPartnerUpdateParams;

import java.util.List;

/**
 * <p>
 * 往来单位 服务类
 * </p>
 *
 * @author zs
 * {@code @date} 2026-08-02 11:23:09
 */
public interface BusinessPartnerService extends IService<BusinessPartnerEntity> {

    /**
     * 分页查询
     * @param businessPartnerPageQueryParams 查询参数
     * @return PageResult<BusinessPartnerVO>
     */
    PageResult<BusinessPartnerVO> page(BusinessPartnerPageQueryParams businessPartnerPageQueryParams);

    /**
     * 查询列表
     * @param businessPartnerSelectQueryParams 查询参数
     * @return List<BusinessPartnerVO>
     */
    List<BusinessPartnerVO> getList(BusinessPartnerSelectQueryParams businessPartnerSelectQueryParams);

    /**
     * 新增
     * @param businessPartnerAddParams 新增参数
     */
    void save(BusinessPartnerAddParams businessPartnerAddParams);

    /**
     * 更新
     * @param businessPartnerUpdateParams 更新参数
     */
    void update(BusinessPartnerUpdateParams businessPartnerUpdateParams);

    /**
     * 根据id查询
     * @param id 主键
     * @return BusinessPartnerVO
     */
    BusinessPartnerVO getById(Long id);

    /**
     * 单个删除
     * @param businessPartnerId 主键
     */
    void deleteById(Long businessPartnerId);

    /**
     * 批量删除
     * @param businessPartnerIds 主键数组
     */
    void batchDelById(Long[] businessPartnerIds);
}
