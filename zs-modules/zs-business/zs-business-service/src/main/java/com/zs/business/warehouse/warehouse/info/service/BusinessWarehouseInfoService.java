package com.zs.business.warehouse.warehouse.info.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.business.warehouse.warehouse.info.domain.entity.BusinessWarehouseInfoEntity;
import com.zs.business.warehouse.warehouse.info.domain.vo.BusinessWarehouseInfoVO;
import com.zs.business.warehouse.warehouse.info.domain.params.BusinessWarehouseInfoPageQueryParams;
import com.zs.business.warehouse.warehouse.info.domain.params.BusinessWarehouseInfoSelectQueryParams;
import com.zs.business.warehouse.warehouse.info.domain.params.BusinessWarehouseInfoAddParams;
import com.zs.business.warehouse.warehouse.info.domain.params.BusinessWarehouseInfoUpdateParams;

import java.util.List;

/**
 * <p>
 * 库房表 服务类
 * </p>
 *
 * @author zs
 * {@code @date} 2026-08-04 11:38:39
 */
public interface BusinessWarehouseInfoService extends IService<BusinessWarehouseInfoEntity> {

    /**
     * 分页查询
     * @param businessWarehouseInfoPageQueryParams 查询参数
     * @return PageResult<BusinessWarehouseInfoVO>
     */
    PageResult<BusinessWarehouseInfoVO> page(BusinessWarehouseInfoPageQueryParams businessWarehouseInfoPageQueryParams);

    /**
     * 查询列表
     * @param businessWarehouseInfoSelectQueryParams 查询参数
     * @return List<BusinessWarehouseInfoVO>
     */
    List<BusinessWarehouseInfoVO> getList(BusinessWarehouseInfoSelectQueryParams businessWarehouseInfoSelectQueryParams);

    /**
     * 新增
     * @param businessWarehouseInfoAddParams 新增参数
     */
    void save(BusinessWarehouseInfoAddParams businessWarehouseInfoAddParams);

    /**
     * 更新
     * @param businessWarehouseInfoUpdateParams 更新参数
     */
    void update(BusinessWarehouseInfoUpdateParams businessWarehouseInfoUpdateParams);

    /**
     * 根据id查询
     * @param id 主键
     * @return BusinessWarehouseInfoVO
     */
    BusinessWarehouseInfoVO getById(Long id);

    /**
     * 单个删除
     * @param businessWarehouseInfoId 主键
     */
    void deleteById(Long businessWarehouseInfoId);

    /**
     * 批量删除
     * @param businessWarehouseInfoIds 主键数组
     */
    void batchDelById(Long[] businessWarehouseInfoIds);
}
