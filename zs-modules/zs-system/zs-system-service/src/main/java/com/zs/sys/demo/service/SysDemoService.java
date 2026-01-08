package com.zs.sys.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.sys.demo.domain.entity.SysDemoEntity;
import com.zs.sys.demo.domain.vo.SysDemoVO;
import com.zs.sys.demo.domain.params.SysDemoPageQueryParams;
import com.zs.sys.demo.domain.params.SysDemoSelectQueryParams;
import com.zs.sys.demo.domain.params.SysDemoAddParams;
import com.zs.sys.demo.domain.params.SysDemoUpdateParams;

import java.util.List;

/**
 * <p>
 * 代码生成测试表 服务类
 * </p>
 *
 * @author zs
 * {@code @date} 2026-01-07 11:01:19
 */
public interface SysDemoService extends IService<SysDemoEntity> {

    /**
     * 分页查询
     * @param sysDemoPageQueryParams 查询参数
     * @return PageResult<SysDemoVO>
     */
    PageResult<SysDemoVO> page(SysDemoPageQueryParams sysDemoPageQueryParams);

    /**
     * 查询列表
     * @param sysDemoSelectQueryParams 查询参数
     * @return List<SysDemoVO>
     */
    List<SysDemoVO> getList(SysDemoSelectQueryParams sysDemoSelectQueryParams);

    /**
     * 新增
     * @param sysDemoAddParams 新增参数
     */
    void save(SysDemoAddParams sysDemoAddParams);

    /**
     * 更新
     * @param sysDemoUpdateParams 更新参数
     */
    void update(SysDemoUpdateParams sysDemoUpdateParams);

    /**
     * 根据id查询
     * @param id 主键
     * @return SysDemoVO
     */
    SysDemoVO getById(Long id);

    /**
     * 单个删除
     * @param sysDemoId 主键
     */
    void deleteById(Long sysDemoId);

    /**
     * 批量删除
     * @param sysDemoIds 主键数组
     */
    void batchDelById(Long[] sysDemoIds);
}
