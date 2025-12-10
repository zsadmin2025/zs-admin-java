package com.zs.sms.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.sms.domain.entity.SysSmsTemplateEntity;
import com.zs.sms.domain.params.SysSmsTemplateAddParams;
import com.zs.sms.domain.params.SysSmsTemplatePageQueryParams;
import com.zs.sms.domain.params.SysSmsTemplateSelectQueryParams;
import com.zs.sms.domain.params.SysSmsTemplateUpdateParams;
import com.zs.sms.domain.vo.SysSmsTemplateVO;
import com.zs.sms.mapper.SysSmsTemplateMapper;
import com.zs.sms.service.SysSmsRecordService;
import com.zs.sms.service.SysSmsTemplateService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 短信模板表 服务实现类
 * </p>
 *
 * @author zs
 * @since 2025-11-26 09:40:35
 */
@Service
public class SysSmsTemplateServiceImpl extends ServiceImpl<SysSmsTemplateMapper, SysSmsTemplateEntity> implements SysSmsTemplateService {

    @Resource
    private SysSmsRecordService sysSmsRecordService;

    @Override
    public PageResult<SysSmsTemplateVO> page(@NotNull SysSmsTemplatePageQueryParams sysSmsTemplatePageQueryParams) {

        Page<SysSmsTemplateEntity> page = new PageInfo<>(sysSmsTemplatePageQueryParams);
        LambdaQueryWrapper<SysSmsTemplateEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Strings.isNotEmpty(sysSmsTemplatePageQueryParams.getTemplateNumber()), SysSmsTemplateEntity::getTemplateNumber, sysSmsTemplatePageQueryParams.getTemplateNumber())
                .eq(Objects.nonNull(sysSmsTemplatePageQueryParams.getChannel()), SysSmsTemplateEntity::getChannel, sysSmsTemplatePageQueryParams.getChannel())
                .eq(Strings.isNotEmpty(sysSmsTemplatePageQueryParams.getSignName()), SysSmsTemplateEntity::getSignName, sysSmsTemplatePageQueryParams.getSignName())
                .like(Strings.isNotEmpty(sysSmsTemplatePageQueryParams.getTemplateCode()), SysSmsTemplateEntity::getTemplateCode, sysSmsTemplatePageQueryParams.getTemplateCode());

        IPage<SysSmsTemplateEntity> iPage = baseMapper.selectPage(page, wrapper);
        List<SysSmsTemplateVO> list = BeanUtil.copyToList(iPage.getRecords(), SysSmsTemplateVO.class);

        return new PageResult<>(list, page.getTotal(), SysSmsTemplateVO.class);
    }

    @Override
    public List<SysSmsTemplateVO> getList(@NotNull SysSmsTemplateSelectQueryParams sysSmsTemplateSelectQueryParams) {
        QueryWrapper<SysSmsTemplateEntity> wrapper = new QueryWrapper<>();
        return BeanUtil.copyToList(baseMapper.selectList(wrapper), SysSmsTemplateVO.class);
    }

    @Override
    public void save(@NotNull SysSmsTemplateAddParams sysSmsTemplateAddParams) {
        SysSmsTemplateEntity sysSmsTemplateEntity = BeanUtil.copyProperties(sysSmsTemplateAddParams, SysSmsTemplateEntity.class);
        baseMapper.insert(sysSmsTemplateEntity);
    }

    @Override
    public void update(@NotNull SysSmsTemplateUpdateParams sysSmsTemplateUpdateParams) {
        SysSmsTemplateEntity sysSmsTemplateEntity = BeanUtil.copyProperties(sysSmsTemplateUpdateParams, SysSmsTemplateEntity.class);
        baseMapper.updateById(sysSmsTemplateEntity);
    }

    @Override
    public SysSmsTemplateVO getById(Long id) {
        return BeanUtil.copyProperties(baseMapper.selectById(id), SysSmsTemplateVO.class);
    }

    @Override
    public void deleteById(Long id) {
        // 是否存在发送记录
        SysSmsTemplateEntity sysSmsTemplateEntity = this.baseMapper.selectById(id);
        if (sysSmsRecordService.getByTemplateNumber(sysSmsTemplateEntity.getTemplateNumber())) {
            throw new RuntimeException("该模板下存在发送记录，请先删除发送记录");
        }

        baseMapper.deleteById(id);

    }

    @Override
    public void batchDelById(@NotNull Long[] ids) {
        // 是否存在发送记录
        for (Long id : ids) {
            SysSmsTemplateEntity sysSmsTemplateEntity = this.baseMapper.selectById(id);
            if (sysSmsRecordService.getByTemplateNumber(sysSmsTemplateEntity.getTemplateNumber())) {
                throw new RuntimeException("该模板下存在发送记录，请先删除发送记录");
            }
        }
        baseMapper.deleteByIds(Arrays.asList(ids));
    }

    @Override
    public SysSmsTemplateVO getByTemplateNumber(String templateNumber) {
        SysSmsTemplateEntity sysSmsTemplateEntity = this.baseMapper.selectOne(new LambdaQueryWrapper<SysSmsTemplateEntity>().eq(SysSmsTemplateEntity::getTemplateNumber, templateNumber));
        return BeanUtil.copyProperties(sysSmsTemplateEntity, SysSmsTemplateVO.class);
    }
}
