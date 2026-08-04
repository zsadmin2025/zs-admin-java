package com.zs.business.goods.cert.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.business.goods.cert.domain.entity.BusinessDrugGoodsCertEntity;
import com.zs.business.goods.cert.domain.vo.BusinessDrugGoodsCertVO;
import com.zs.business.goods.cert.domain.params.BusinessDrugGoodsCertPageQueryParams;
import com.zs.business.goods.cert.domain.params.BusinessDrugGoodsCertSelectQueryParams;
import com.zs.business.goods.cert.domain.params.BusinessDrugGoodsCertAddParams;
import com.zs.business.goods.cert.domain.params.BusinessDrugGoodsCertUpdateParams;

import java.util.List;

/**
 * <p>
 * 商品证照附件 服务类
 * </p>
 *
 * @author zs
 * {@code @date} 2026-08-03 11:09:18
 */
public interface BusinessDrugGoodsCertService extends IService<BusinessDrugGoodsCertEntity> {

    /**
     * 分页查询
     * @param businessDrugGoodsCertPageQueryParams 查询参数
     * @return PageResult<BusinessDrugGoodsCertVO>
     */
    PageResult<BusinessDrugGoodsCertVO> page(BusinessDrugGoodsCertPageQueryParams businessDrugGoodsCertPageQueryParams);

    /**
     * 查询列表
     * @param businessDrugGoodsCertSelectQueryParams 查询参数
     * @return List<BusinessDrugGoodsCertVO>
     */
    List<BusinessDrugGoodsCertVO> getList(BusinessDrugGoodsCertSelectQueryParams businessDrugGoodsCertSelectQueryParams);

    /**
     * 新增
     * @param businessDrugGoodsCertAddParams 新增参数
     */
    void save(BusinessDrugGoodsCertAddParams businessDrugGoodsCertAddParams);

    /**
     * 更新
     * @param businessDrugGoodsCertUpdateParams 更新参数
     */
    void update(BusinessDrugGoodsCertUpdateParams businessDrugGoodsCertUpdateParams);

    /**
     * 根据id查询
     * @param id 主键
     * @return BusinessDrugGoodsCertVO
     */
    BusinessDrugGoodsCertVO getById(Long id);

    /**
     * 单个删除
     * @param businessDrugGoodsCertId 主键
     */
    void deleteById(Long businessDrugGoodsCertId);

    /**
     * 批量删除
     * @param businessDrugGoodsCertIds 主键数组
     */
    void batchDelById(Long[] businessDrugGoodsCertIds);
}
