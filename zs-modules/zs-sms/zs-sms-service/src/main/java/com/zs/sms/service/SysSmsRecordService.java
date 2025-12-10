package com.zs.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.sms.domain.entity.SysSmsRecordEntity;
import com.zs.sms.domain.params.SysSmsRecordPageQueryParams;
import com.zs.sms.domain.params.SysSmsRecordSelectQueryParams;
import com.zs.sms.domain.params.SysSmsRecordUpdateParams;
import com.zs.sms.domain.vo.SysSmsRecordVO;

import java.util.List;

/**
 * <p>
 * 短信记录表 服务类
 * </p>
 *
 * @author zs
 * @since 2025-11-25 22:20:34
 */
public interface SysSmsRecordService extends IService<SysSmsRecordEntity> {

    /**
     * 分页
     * @param sysSmsRecordPageQueryParams 查询参数
     * @return  分页结果
     */
    PageResult<SysSmsRecordVO> page(SysSmsRecordPageQueryParams sysSmsRecordPageQueryParams);

    /**
     * 列表
     * @param sysSmsRecordSelectQueryParams 查询参数
     * @return 列表
     */
    List<SysSmsRecordVO> getList(SysSmsRecordSelectQueryParams sysSmsRecordSelectQueryParams);

    /**
     * 更新
     * @param sysSmsRecordUpdateParams 更新参数
     */
    void update(SysSmsRecordUpdateParams sysSmsRecordUpdateParams);

    /**
     * 根据id查询
     * @param id  id
     * @return SysSmsRecordVO
     */
    SysSmsRecordVO getById(Long id);

    /**
     * 单个删除
     * @param sysSmsId  主键ID
     */
    void deleteById(Long sysSmsId);

    /**
     * 批量删除
     * @param sysSmsIds 主键ID数组
     */
    void batchDelById(Long[] sysSmsIds);

    /**
     * 根据模板编号删除
     * @param templateNumber 模板编号
     */
    void deleteByTemplateNumber(String templateNumber);

    /**
     * 根据模板编号查询是否存在
     * @param templateNumber 模板编号
     * @return true 存在 false 不存在
     */
    boolean getByTemplateNumber(String templateNumber);
}
