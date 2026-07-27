package com.zs.bpm.form.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.bpm.form.domain.entity.BpmFormDefinitionEntity;
import com.zs.bpm.form.domain.params.FormDefinitionAddParams;
import com.zs.bpm.form.domain.params.FormDefinitionQueryParams;
import com.zs.bpm.form.domain.vo.FormDefinitionVO;
import com.zs.common.core.page.PageResult;

/**
 * 动态表单定义 Service 接口
 *
 * @author zsadmin
 */
public interface IBpmFormDefinitionService extends IService<BpmFormDefinitionEntity> {

    /**
     * 分页查询表单定义
     *
     * @param params 查询参数
     * @return 分页结果
     */
    PageResult<FormDefinitionVO> page(FormDefinitionQueryParams params);

    /**
     * 根据ID查询表单定义
     *
     * @param id 表单ID
     * @return 表单定义视图对象
     */
    FormDefinitionVO getById(Long id);

    /**
     * 保存表单定义
     *
     * @param params 新增参数
     */
    void save(FormDefinitionAddParams params);

    /**
     * 更新表单定义
     *
     * @param params 更新参数
     */
    void update(FormDefinitionAddParams params);

    /**
     * 删除表单定义
     *
     * @param id 表单ID
     */
    void delete(Long id);

}
