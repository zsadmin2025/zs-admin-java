package com.zs.business.goods.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.business.goods.goods.domain.entity.BusinessDrugGoodsEntity;
import com.zs.business.goods.goods.domain.vo.BusinessDrugGoodsVO;
import com.zs.business.goods.goods.domain.params.BusinessDrugGoodsPageQueryParams;
import com.zs.business.goods.goods.domain.params.BusinessDrugGoodsSelectQueryParams;
import com.zs.business.goods.goods.domain.params.BusinessDrugGoodsAddParams;
import com.zs.business.goods.goods.domain.params.BusinessDrugGoodsUpdateParams;

import java.util.List;

/**
 * <p>
 * 药品商品主信息表 服务类
 * </p>
 *
 * @author zs
 * {@code @date} 2026-07-31 19:22:08
 */
public interface BusinessDrugGoodsService extends IService<BusinessDrugGoodsEntity> {

    /**
     * 分页查询
     * @param businessDrugGoodsPageQueryParams 查询参数
     * @return PageResult<BusinessDrugGoodsVO>
     */
    PageResult<BusinessDrugGoodsVO> page(BusinessDrugGoodsPageQueryParams businessDrugGoodsPageQueryParams);

    /**
     * 查询列表
     * @param businessDrugGoodsSelectQueryParams 查询参数
     * @return List<BusinessDrugGoodsVO>
     */
    List<BusinessDrugGoodsVO> getList(BusinessDrugGoodsSelectQueryParams businessDrugGoodsSelectQueryParams);

    /**
     * 新增
     * @param businessDrugGoodsAddParams 新增参数
     */
    void save(BusinessDrugGoodsAddParams businessDrugGoodsAddParams);

    /**
     * 更新
     * @param businessDrugGoodsUpdateParams 更新参数
     */
    void update(BusinessDrugGoodsUpdateParams businessDrugGoodsUpdateParams);

    /**
     * 根据id查询
     * @param id 主键
     * @return BusinessDrugGoodsVO
     */
    BusinessDrugGoodsVO getById(Long id);

    /**
     * 单个删除
     * @param businessDrugGoodsId 主键
     */
    void deleteById(Long businessDrugGoodsId);

    /**
     * 批量删除
     * @param businessDrugGoodsIds 主键数组
     */
    void batchDelById(Long[] businessDrugGoodsIds);
}
