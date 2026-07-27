package com.zs.bpm.model.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.bpm.definition.domain.entity.BpmProcessDefinitionInfoEntity;
import com.zs.bpm.definition.domain.params.BpmProcessDefinitionInfoSaveParams;
import com.zs.bpm.definition.domain.vo.BpmProcessDefinitionInfoVO;
import com.zs.bpm.model.domain.params.BpmProcessDefinitionInfoPageQueryParams;
import com.zs.bpm.model.domain.params.BpmProcessDefinitionInfoQueryParams;
import com.zs.bpm.model.domain.vo.ModelValidateVO;
import com.zs.common.core.page.PageResult;

import java.util.List;

public interface IBpmProcessModelService extends IService<BpmProcessDefinitionInfoEntity> {

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
     * 获取可发起的流程列表
     *
     * @return 流程列表
     */
    List<BpmProcessDefinitionInfoVO> getCanStartProcessList(BpmProcessDefinitionInfoQueryParams params);

    /**
     * 创建模型
     *
     * @param params 模型信息
     * @return 模型ID
     */
    Long createModel(BpmProcessDefinitionInfoSaveParams params);

    /**
     * 更新模型
     *
     * @param params 模型信息
     * @return 模型ID
     */
    Long updateModel(BpmProcessDefinitionInfoSaveParams params);


    /**
     * 部署模型
     *
     * @param id 模型ID
     * @return 部署信息
     */
    Boolean deployModel(Long id);




    ModelValidateVO validateModel(Long id);
    Long publishModel(Long id);
    String activateProcess(String processDefId);
    String deactivateProcess(String processDefId);


}
