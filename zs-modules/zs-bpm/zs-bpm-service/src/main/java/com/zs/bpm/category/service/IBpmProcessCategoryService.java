package com.zs.bpm.category.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.bpm.category.domain.entity.BpmProcessCategoryEntity;
import com.zs.bpm.category.domain.params.BpmProcessCategoryAddParams;
import com.zs.bpm.category.domain.params.BpmProcessCategoryQueryParams;
import com.zs.bpm.category.domain.params.BpmProcessCategoryUpdateParams;
import com.zs.bpm.category.domain.vo.BpmProcessCategoryVO;
import com.zs.common.core.page.PageResult;

import java.util.List;

/**
 * 流程分类 Service 接口
 *
 * @author zsadmin
 */
public interface IBpmProcessCategoryService extends IService<BpmProcessCategoryEntity> {

    /**
     * 分页查询
     */
    PageResult<BpmProcessCategoryVO> page(BpmProcessCategoryQueryParams params);

    /**
     * 列表查询
     */
    List<BpmProcessCategoryVO> getList(BpmProcessCategoryQueryParams params);

    /**
     * 详情
     */
    BpmProcessCategoryVO getById(Long id);

    /**
     * 新增
     */
    void save(BpmProcessCategoryAddParams params);

    /**
     * 修改
     */
    void update(BpmProcessCategoryUpdateParams params);

    /**
     * 获取分类树列表
     */
    List<BpmProcessCategoryVO> getTreeList();
}
