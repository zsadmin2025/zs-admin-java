package com.zs.bpm.definition.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.bpm.definition.domain.entity.BpmProcessDefinitionInfoEntity;
import com.zs.bpm.definition.domain.params.BpmProcessDefinitionInfoSaveParams;
import com.zs.bpm.definition.domain.vo.BpmProcessDefinitionInfoVO;
import com.zs.bpm.model.domain.params.BpmProcessDefinitionInfoPageQueryParams;
import com.zs.common.core.page.PageResult;
import org.flowable.engine.repository.ProcessDefinition;

import java.util.List;

/**
 * 流程定义信息 Service 接口
 *
 * @author zsadmin
 */
public interface IBpmProcessDefinitionInfoService extends IService<BpmProcessDefinitionInfoEntity> {


    /**
     * 查询流程定义
     * @return 流程定义列表
     */
    List<ProcessDefinition> queryProcessDefinition();

    /**
     * 删除流程定义
     * @param processDefinitionId 流程定义ID
     * @return 是否成功
     */
    Boolean deleteProcessDefinition(String processDefinitionId);



    /**
     * 分页查询流程定义
     *
     * @param params 查询参数
     * @return 分页结果
     */
    PageResult<BpmProcessDefinitionInfoVO> page(BpmProcessDefinitionInfoPageQueryParams params);

    /**
     * 获取流程定义详情
     *
     * @param id 主键ID
     * @return 流程定义VO
     */
    BpmProcessDefinitionInfoVO getDetail(Long id);

    /**
     * 保存流程定义
     *
     * @param params 保存参数
     * @return 主键ID
     */
    Long saveDefinition(BpmProcessDefinitionInfoSaveParams params);
}
