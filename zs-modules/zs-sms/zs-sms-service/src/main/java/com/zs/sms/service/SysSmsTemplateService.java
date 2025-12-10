package com.zs.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.sms.domain.entity.SysSmsTemplateEntity;
import com.zs.sms.domain.params.SysSmsTemplateAddParams;
import com.zs.sms.domain.params.SysSmsTemplatePageQueryParams;
import com.zs.sms.domain.params.SysSmsTemplateSelectQueryParams;
import com.zs.sms.domain.params.SysSmsTemplateUpdateParams;
import com.zs.sms.domain.vo.SysSmsTemplateVO;

import java.util.List;

/**
 * <p>
 * 短信模板表 服务类
 * </p>
 *
 * @author zs
 * @since 2025-11-26 09:40:35
 */
public interface SysSmsTemplateService extends IService<SysSmsTemplateEntity> {

    /**
     * 分页
     * @param sysSmsTemplatePageQueryParams 查询参数
     * @return 分页结果
     */
    PageResult<SysSmsTemplateVO> page(SysSmsTemplatePageQueryParams sysSmsTemplatePageQueryParams);

    /**
     * 列表
     * @param sysSmsTemplateSelectQueryParams 查询参数
     * @return 列表
     */
    List<SysSmsTemplateVO> getList(SysSmsTemplateSelectQueryParams sysSmsTemplateSelectQueryParams);

    /**
     * 新增
     * @param sysSmsTemplateAddParams 新增参数
     */
    void save(SysSmsTemplateAddParams sysSmsTemplateAddParams);

    /**
     * 更新
     * @param sysSmsTemplateUpdateParams 更新参数
     */
    void update(SysSmsTemplateUpdateParams sysSmsTemplateUpdateParams);

    /**
     * 根据id查询
     * @param id 主键ID
     * @return 查询结果
     */
    SysSmsTemplateVO getById(Long id);

    /**
     * 单个删除
     * @param sysSmsTemplateId 主键ID
     */
    void deleteById(Long sysSmsTemplateId);

    /**
     * 批量删除
     * @param sysSmsTemplateIds 主键ID列表
     */
    void batchDelById(Long[] sysSmsTemplateIds);

    /**
     * 根据模板编号查询
     * @param templateNumber 模板编号
     * @return SysSmsTemplateVO
     */
    SysSmsTemplateVO getByTemplateNumber(String templateNumber);
}
