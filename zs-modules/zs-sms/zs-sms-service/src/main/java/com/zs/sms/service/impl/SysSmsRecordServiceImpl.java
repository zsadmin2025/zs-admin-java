package com.zs.sms.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.sms.domain.entity.SysSmsRecordEntity;
import com.zs.sms.domain.params.SysSmsRecordPageQueryParams;
import com.zs.sms.domain.params.SysSmsRecordSelectQueryParams;
import com.zs.sms.domain.params.SysSmsRecordUpdateParams;
import com.zs.sms.domain.vo.SysSmsRecordVO;
import com.zs.sms.mapper.SysSmsRecordMapper;
import com.zs.sms.service.SysSmsRecordService;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 短信记录表 服务实现类
 * </p>
 *
 * @author zs
 * @since 2025-11-25 22:20:34
 */
@Service
public class SysSmsRecordServiceImpl extends ServiceImpl<SysSmsRecordMapper, SysSmsRecordEntity> implements SysSmsRecordService {

    @Override
    public PageResult<SysSmsRecordVO> page(@NotNull SysSmsRecordPageQueryParams sysSmsPageQueryParams) {

        Page<SysSmsRecordEntity> page = new PageInfo<>(sysSmsPageQueryParams);
        LambdaQueryWrapper<SysSmsRecordEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysSmsRecordEntity::getTemplateNumber, sysSmsPageQueryParams.getTemplateNumber())
                .eq(Strings.isNotEmpty(sysSmsPageQueryParams.getRequestId()), SysSmsRecordEntity::getRequestId, sysSmsPageQueryParams.getRequestId())
                .eq(Strings.isNotEmpty(sysSmsPageQueryParams.getBizId()), SysSmsRecordEntity::getBizId, sysSmsPageQueryParams.getBizId())
                .like(Strings.isNotEmpty(sysSmsPageQueryParams.getPhoneNumbers()), SysSmsRecordEntity::getPhoneNumbers, sysSmsPageQueryParams.getPhoneNumbers())
                .orderByDesc(SysSmsRecordEntity::getSendTime);

        IPage<SysSmsRecordEntity> iPage = baseMapper.selectPage(page, wrapper);
        List<SysSmsRecordVO> list = BeanUtil.copyToList(iPage.getRecords(), SysSmsRecordVO.class);

        return new PageResult<>(list, page.getTotal(), SysSmsRecordVO.class);
    }

    @Override
    public List<SysSmsRecordVO> getList(@NotNull SysSmsRecordSelectQueryParams sysSmsSelectQueryParams) {
        QueryWrapper<SysSmsRecordEntity> wrapper = new QueryWrapper<>();
        return BeanUtil.copyToList(baseMapper.selectList(wrapper), SysSmsRecordVO.class);
    }


    @Override
    public void update(@NotNull SysSmsRecordUpdateParams sysSmsUpdateParams) {
        SysSmsRecordEntity SysSmsRecordEntity = BeanUtil.copyProperties(sysSmsUpdateParams, SysSmsRecordEntity.class);
        baseMapper.updateById(SysSmsRecordEntity);
    }

    @Override
    public SysSmsRecordVO getById(Long id) {
        return BeanUtil.copyProperties(baseMapper.selectById(id), SysSmsRecordVO.class);
    }

    @Override
    public void deleteById(Long id) {
        baseMapper.deleteById(id);
    }

    @Override
    public void batchDelById(@NotNull Long[] ids) {
        baseMapper.deleteByIds(Arrays.asList(ids));
    }

    @Override
    public void deleteByTemplateNumber(String templateNumber) {
        this.baseMapper.delete(new LambdaQueryWrapper<SysSmsRecordEntity>().eq(SysSmsRecordEntity::getTemplateNumber, templateNumber));
    }

    @Override
    public boolean getByTemplateNumber(String templateNumber) {
        return this.baseMapper.exists(new LambdaQueryWrapper<SysSmsRecordEntity>().eq(SysSmsRecordEntity::getTemplateNumber, templateNumber));
    }
}
