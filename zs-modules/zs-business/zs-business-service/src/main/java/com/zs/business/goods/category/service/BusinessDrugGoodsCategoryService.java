package com.zs.business.goods.category.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.business.goods.category.domain.entity.BusinessDrugGoodsCategoryEntity;
import com.zs.business.goods.category.domain.vo.BusinessDrugGoodsCategoryVO;
import com.zs.business.goods.category.domain.params.BusinessDrugGoodsCategoryPageQueryParams;
import com.zs.business.goods.category.domain.params.BusinessDrugGoodsCategorySelectQueryParams;
import com.zs.business.goods.category.domain.params.BusinessDrugGoodsCategoryAddParams;
import com.zs.business.goods.category.domain.params.BusinessDrugGoodsCategoryUpdateParams;

import java.util.List;

/**
 * <p>
 * 商品档案 服务类
 * </p>
 *
 * @author zs
 * {@code @date} 2026-07-31 19:22:03
 */
public interface BusinessDrugGoodsCategoryService extends IService<BusinessDrugGoodsCategoryEntity> {

    /**
     * 分页查询
     * @param businessDrugGoodsCategoryPageQueryParams 查询参数
     * @return PageResult<BusinessDrugGoodsCategoryVO>
     */
    PageResult<BusinessDrugGoodsCategoryVO> page(BusinessDrugGoodsCategoryPageQueryParams businessDrugGoodsCategoryPageQueryParams);

    /**
     * 查询列表
     * @param businessDrugGoodsCategorySelectQueryParams 查询参数
     * @return List<BusinessDrugGoodsCategoryVO>
     */
    List<BusinessDrugGoodsCategoryVO> getList(BusinessDrugGoodsCategorySelectQueryParams businessDrugGoodsCategorySelectQueryParams);

    /**
     * 新增
     * @param businessDrugGoodsCategoryAddParams 新增参数
     */
    void save(BusinessDrugGoodsCategoryAddParams businessDrugGoodsCategoryAddParams);

    /**
     * 更新
     * @param businessDrugGoodsCategoryUpdateParams 更新参数
     */
    void update(BusinessDrugGoodsCategoryUpdateParams businessDrugGoodsCategoryUpdateParams);

    /**
     * 根据id查询
     * @param id 主键
     * @return BusinessDrugGoodsCategoryVO
     */
    BusinessDrugGoodsCategoryVO getById(Long id);

    /**
     * 单个删除
     * @param businessDrugGoodsCategoryId 主键
     */
    void deleteById(Long businessDrugGoodsCategoryId);

    /**
     * 批量删除
     * @param businessDrugGoodsCategoryIds 主键数组
     */
    void batchDelById(Long[] businessDrugGoodsCategoryIds);
}
