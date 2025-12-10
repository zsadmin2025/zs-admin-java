package com.zs.mail.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.common.core.utils.SecurityUtil;
import com.zs.mail.domain.entity.SysMailTasksEntity;
import com.zs.mail.domain.params.SysMailTasksAddParams;
import com.zs.mail.domain.params.SysMailTasksPageQueryParams;
import com.zs.mail.domain.params.SysMailTasksSelectQueryParams;
import com.zs.mail.domain.params.SysMailTasksUpdateParams;
import com.zs.mail.domain.vo.SysMailTasksVO;
import com.zs.mail.mapper.SysMailTasksMapper;
import com.zs.mail.service.MailService;
import com.zs.mail.service.SysMailTasksService;
import config.SysConfigApi;
import config.dto.SysConfigEmailDTO;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zs
 * @since 2025-10-30 11:13:31
 */
@Service
public class SysMailTasksServiceImpl extends ServiceImpl<SysMailTasksMapper, SysMailTasksEntity> implements SysMailTasksService {

    @Resource
    private MailService mailService;
    @Resource
    private SysConfigApi sysConfigApi;

    @Override
    public PageResult<SysMailTasksVO> page(@NotNull SysMailTasksPageQueryParams sysMailTasksPageQueryParams) {

        Page<SysMailTasksEntity> page = new PageInfo<>(sysMailTasksPageQueryParams);
        QueryWrapper<SysMailTasksEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .like(StringUtils.isNotBlank(sysMailTasksPageQueryParams.getSubject()), SysMailTasksEntity::getSubject, sysMailTasksPageQueryParams.getSubject());

        IPage<SysMailTasksEntity> iPage = baseMapper.selectPage(page, wrapper);

        // 转换为 VO 列表，同时处理特殊字段
        List<SysMailTasksVO> voList = iPage.getRecords().stream()
                .map(this::convertToVo)
                .collect(Collectors.toList());


        return new PageResult<>(voList, iPage.getTotal(), SysMailTasksVO.class);
    }

    // 将 Entity 转换为 VO 的私有方法
    private SysMailTasksVO convertToVo(SysMailTasksEntity entity) {
        SysMailTasksVO vo = BeanUtil.toBean(entity, SysMailTasksVO.class);

        vo.setRecipients(splitString(entity.getRecipients()));
        vo.setCc(splitString(entity.getCc()));
        vo.setBcc(splitString(entity.getBcc()));

        return vo;
    }


    @Override
    public List<SysMailTasksVO> getList(@NotNull SysMailTasksSelectQueryParams sysMailTasksSelectQueryParams) {
        QueryWrapper<SysMailTasksEntity> wrapper = new QueryWrapper<>();
        List<SysMailTasksEntity> list = baseMapper.selectList(wrapper);
        List<SysMailTasksVO> listVO = BeanUtil.copyToList(list, SysMailTasksVO.class);
        // 构建 ID -> Entity 的映射，避免 filter 遍历
        Map<Long, SysMailTasksEntity> entityMap = list.stream()
                .collect(Collectors.toMap(SysMailTasksEntity::getSysMailTasksId, Function.identity()));

        listVO.forEach(vo -> {
            SysMailTasksEntity entity = entityMap.get(vo.getSysMailTasksId());
            if (entity != null) {
                vo.setRecipients(splitString(entity.getRecipients()));
                vo.setCc(splitString(entity.getCc()));
                vo.setBcc(splitString(entity.getBcc()));
            }
        });

        return listVO;
    }

    // 通用方法：将字符串按逗号分割为 List<String>，空值返回空列表
    private List<String> splitString(String str) {
        if (str == null || str.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(str.trim().split("\\s*,\\s*")); // 支持空格兼容
    }

    private String convertListToString(List<String> list) {
        return list != null && !list.isEmpty() ? String.join(",", list) : null;
    }
    @Override
    public void save(@NotNull SysMailTasksAddParams sysMailTasksAddParams) {
        SysConfigEmailDTO sysConfigEmailVO = sysConfigApi.emailInfo();

        SysMailTasksEntity sysMailTasksEntity = BeanUtil.copyProperties(sysMailTasksAddParams, SysMailTasksEntity.class);
        sysMailTasksEntity.setRecipients(convertListToString(sysMailTasksAddParams.getRecipients()));
        sysMailTasksEntity.setCc(convertListToString(sysMailTasksAddParams.getCc()));
        sysMailTasksEntity.setBcc(convertListToString(sysMailTasksAddParams.getBcc()));
        sysMailTasksEntity.setSender(sysConfigEmailVO.getUsername());
        sysMailTasksEntity.setSenderName(SecurityUtil.getRealName());
        baseMapper.insert(sysMailTasksEntity);

    }

    @Override
    public void update(@NotNull SysMailTasksUpdateParams sysMailTasksUpdateParams) {
        SysConfigEmailDTO sysConfigEmailDTO = sysConfigApi.emailInfo();

        SysMailTasksEntity sysMailTasksEntity = BeanUtil.copyProperties(sysMailTasksUpdateParams, SysMailTasksEntity.class);
        sysMailTasksEntity.setRecipients(convertListToString(sysMailTasksUpdateParams.getRecipients()));
        sysMailTasksEntity.setCc(convertListToString(sysMailTasksUpdateParams.getCc()));
        sysMailTasksEntity.setBcc(convertListToString(sysMailTasksUpdateParams.getBcc()));
        sysMailTasksEntity.setSender(sysConfigEmailDTO.getUsername());
        sysMailTasksEntity.setSenderName(SecurityUtil.getRealName());
        baseMapper.updateById(sysMailTasksEntity);
    }

    @Override
    public SysMailTasksVO getById(Long id) {
        SysMailTasksEntity entity = baseMapper.selectById(id);
        SysMailTasksVO vo = BeanUtil.copyProperties(entity, SysMailTasksVO.class);
        if (entity.getRecipients() != null && ! entity.getRecipients().isEmpty()) {
            vo.setRecipients(splitString(entity.getRecipients()));
        }
        if (entity.getCc() != null && ! entity.getCc().isEmpty()) {
            vo.setCc(splitString(entity.getCc()));
        }
        if (entity.getBcc() != null && ! entity.getBcc().isEmpty()) {
            vo.setBcc(splitString(entity.getBcc()));
        }
        return vo;
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
    public void send(Long sysMailTasksId) {
        SysMailTasksEntity sysMailTasksEntity = baseMapper.selectById(sysMailTasksId);

        SysConfigEmailDTO sysConfigEmailDTO = sysConfigApi.emailInfo();

        String[] recipients = sysMailTasksEntity.getRecipients() != null ?
                sysMailTasksEntity.getRecipients().split(",") : new String[0];
        String[] cc = sysMailTasksEntity.getCc() != null ?
                sysMailTasksEntity.getCc().split(",") : new String[0];
        String[] bcc = sysMailTasksEntity.getBcc() != null ?
                sysMailTasksEntity.getBcc().split(",") : new String[0];

        mailService.sendHtmlEmail(sysConfigEmailDTO.getUsername(), recipients, cc, bcc, sysMailTasksEntity.getSubject(), sysMailTasksEntity.getContent());


        sysMailTasksEntity.setStatus(1);
        baseMapper.updateById(sysMailTasksEntity);
    }

    @Override
    public void sendNow(SysMailTasksAddParams sysMailTasksAddParams) {
        SysConfigEmailDTO sysConfigEmailDTO = sysConfigApi.emailInfo();

        SysMailTasksEntity sysMailTasksEntity = BeanUtil.copyProperties(sysMailTasksAddParams, SysMailTasksEntity.class);
        sysMailTasksEntity.setRecipients(convertListToString(sysMailTasksAddParams.getRecipients()));
        sysMailTasksEntity.setCc(convertListToString(sysMailTasksAddParams.getCc()));
        sysMailTasksEntity.setBcc(convertListToString(sysMailTasksAddParams.getBcc()));
        sysMailTasksEntity.setSender(sysConfigEmailDTO.getUsername());
        sysMailTasksEntity.setSenderName(SecurityUtil.getRealName());

        sysMailTasksEntity.setStatus(2);
        baseMapper.insert(sysMailTasksEntity);

        String[] recipients = sysMailTasksEntity.getRecipients() != null ?
                sysMailTasksEntity.getRecipients().split(",") : new String[0];
        String[] cc = sysMailTasksEntity.getCc() != null ?
                sysMailTasksEntity.getCc().split(",") : new String[0];
        String[] bcc = sysMailTasksEntity.getBcc() != null ?
                sysMailTasksEntity.getBcc().split(",") : new String[0];

        // 批量发送邮件
        mailService.sendHtmlEmail(sysConfigEmailDTO.getUsername(), recipients, cc, bcc, sysMailTasksAddParams.getSubject(), sysMailTasksAddParams.getContent());
    }


}

