package com.zs.lawyer.cases.team.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.cases.team.domain.entity.CaseTeamEntity;
import com.zs.lawyer.cases.team.domain.params.CaseTeamAddParams;
import com.zs.lawyer.cases.team.domain.params.CaseTeamPageQueryParams;
import com.zs.lawyer.cases.team.domain.params.CaseTeamSelectQueryParams;
import com.zs.lawyer.cases.team.domain.params.CaseTeamUpdateParams;
import com.zs.lawyer.cases.team.domain.vo.CaseTeamVO;
import com.zs.lawyer.cases.team.mapper.CaseTeamMapper;
import com.zs.lawyer.cases.team.service.CaseTeamService;
import com.zs.sys.user.domain.params.SysUserQueryParams;
import com.zs.sys.user.domain.vo.SysUserVO;
import com.zs.sys.user.service.ISysUserService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 案件团队 服务实现类
 * </p>
 *
 * @author zs
 * @since 2025-06-08 18:01:20
 */
@Service
public class CaseTeamServiceImpl extends ServiceImpl<CaseTeamMapper, CaseTeamEntity> implements CaseTeamService {

    @Resource
    private ISysUserService sysUserService;
        @Override
        public PageResult<CaseTeamVO> page(@NotNull CaseTeamPageQueryParams caseTeamPageQueryParams) {

            Page<CaseTeamEntity> page = new PageInfo<>(caseTeamPageQueryParams);
            QueryWrapper<CaseTeamEntity> wrapper = new QueryWrapper<>();

            IPage<CaseTeamEntity> iPage = baseMapper.selectPage(page, wrapper);
            List<CaseTeamVO> list = BeanUtil.copyToList(iPage.getRecords(), CaseTeamVO.class);

            return new PageResult<>(list, page.getTotal(), CaseTeamVO.class);
        }

        @Override
        public List<CaseTeamVO> getList(@NotNull CaseTeamSelectQueryParams caseTeamSelectQueryParams) {
            QueryWrapper<CaseTeamEntity> wrapper = new QueryWrapper<>();
            return BeanUtil.copyToList(baseMapper.selectList(wrapper), CaseTeamVO.class);
        }

        @Override
        public void save(@NotNull CaseTeamAddParams caseTeamAddParams) {
            CaseTeamEntity caseTeamEntity = BeanUtil.copyProperties(caseTeamAddParams, CaseTeamEntity.class);
            caseTeamEntity.setAssistant(caseTeamAddParams.getAssistant().stream().map(Object::toString).collect(Collectors.joining(",")));
            caseTeamEntity.setCoOrganizer(caseTeamAddParams.getCoOrganizer().stream().map(Object::toString).collect(Collectors.joining(",")));
            caseTeamEntity.setLeadLawyer(caseTeamAddParams.getLeadLawyer().stream().map(Object::toString).collect(Collectors.joining(",")));
            caseTeamEntity.setSecretary(caseTeamAddParams.getSecretary().stream().map(Object::toString).collect(Collectors.joining(",")));
            caseTeamEntity.setCoordinatingLawyer(caseTeamAddParams.getCoordinatingLawyer().stream().map(Object::toString).collect(Collectors.joining(",")));
            baseMapper.insert(caseTeamEntity);
        }

        @Override
        public void update(@NotNull CaseTeamUpdateParams caseTeamUpdateParams) {
            CaseTeamEntity caseTeamEntity = BeanUtil.copyProperties(caseTeamUpdateParams, CaseTeamEntity.class);
            caseTeamEntity.setAssistant(caseTeamUpdateParams.getAssistant().stream().map(Object::toString).collect(Collectors.joining(",")));
            caseTeamEntity.setCoOrganizer(caseTeamUpdateParams.getCoOrganizer().stream().map(Object::toString).collect(Collectors.joining(",")));
            caseTeamEntity.setLeadLawyer(caseTeamUpdateParams.getLeadLawyer().stream().map(Object::toString).collect(Collectors.joining(",")));
            caseTeamEntity.setSecretary(caseTeamUpdateParams.getSecretary().stream().map(Object::toString).collect(Collectors.joining(",")));
            caseTeamEntity.setCoordinatingLawyer(caseTeamUpdateParams.getCoordinatingLawyer().stream().map(Object::toString).collect(Collectors.joining(",")));
            baseMapper.updateById(caseTeamEntity);
        }

        @Override
        public CaseTeamVO getById(Long id) {
            return BeanUtil.copyProperties(baseMapper.selectById(id), CaseTeamVO.class);
        }

    @Override
    public CaseTeamVO getByCaseInfoId(Long caseInfoId) {
        CaseTeamEntity caseTeamEntity = baseMapper.selectOne(new LambdaQueryWrapper<CaseTeamEntity>().eq(CaseTeamEntity::getCaseInfoId, caseInfoId));
        if (caseTeamEntity == null) {
            // 可返回 null 或抛出自定义异常，根据业务需求决定
            return null;
        }
        CaseTeamVO caseTeamVO = BeanUtil.copyProperties(caseTeamEntity, CaseTeamVO.class);
        // 协接律师
        if (StringUtils.isNotBlank(caseTeamEntity.getCoordinatingLawyer())) {
            caseTeamVO.setCoordinatingLawyer(Arrays.asList(caseTeamEntity.getCoordinatingLawyer().split(",")));
        }
        // 主办律师
        if (StringUtils.isNotBlank(caseTeamEntity.getLeadLawyer())) {
            caseTeamVO.setLeadLawyer(Arrays.asList(caseTeamEntity.getLeadLawyer().split(",")));
        }
        // 协办人员
        if (StringUtils.isNotBlank(caseTeamEntity.getCoOrganizer())) {
            caseTeamVO.setCoOrganizer(Arrays.asList(caseTeamEntity.getCoOrganizer().split(",")));
        }
        // 助理
        if (StringUtils.isNotBlank(caseTeamEntity.getAssistant())) {
            caseTeamVO.setAssistant(Arrays.asList(caseTeamEntity.getAssistant().split(",")));
        }
        // 秘书
        if (StringUtils.isNotBlank(caseTeamEntity.getSecretary())) {
            caseTeamVO.setSecretary(Arrays.asList(caseTeamEntity.getSecretary().split(",")));
        }

        // 获取所有用户列表信息
        SysUserQueryParams sysUserQueryParams = new SysUserQueryParams();
        List<SysUserVO> sysUserList = sysUserService.list(sysUserQueryParams);

        // 获取承办律师名称
        caseTeamVO.setUndertakeLawyerVo(
                sysUserList.stream().filter(sysUserVO -> sysUserVO.getSysUserId().equals(caseTeamVO.getUndertakeLawyer())).findFirst().orElse(null)
        );

        // 获取协接律师名称
        caseTeamVO.setCoordinatingLawyerVos(
                sysUserList.stream()
                        .filter(sysUserVO -> caseTeamVO.getCoordinatingLawyer().contains(sysUserVO.getSysUserId().toString())).toList()
        );

        // 获取主办律师名称
        caseTeamVO.setLeadLawyerVos(
                sysUserList.stream()
                        .filter(sysUserVO -> caseTeamVO.getLeadLawyer().contains(sysUserVO.getSysUserId().toString())).toList()
        );

        // 获取协办人员名称
        caseTeamVO.setCoOrganizerVos(
                sysUserList.stream()
                        .filter(sysUserVO -> caseTeamVO.getCoOrganizer().contains(sysUserVO.getSysUserId().toString())).toList()
        );

        // 获取助理名称
        caseTeamVO.setAssistantVos(
                sysUserList.stream()
                        .filter(sysUserVO -> caseTeamVO.getAssistant().contains(sysUserVO.getSysUserId().toString())).toList()
        );

        // 获取秘书名称
        caseTeamVO.setSecretaryVos(
                sysUserList.stream()
                        .filter(sysUserVO -> caseTeamVO.getSecretary().contains(sysUserVO.getSysUserId().toString())).toList()
        );


        return caseTeamVO;
    }

    @Override
        public void deleteById(Long id) {
            baseMapper.deleteById(id);
        }

        @Override
        public void batchDelById(@NotNull Long[] ids) {
            baseMapper.deleteByIds(Arrays.asList(ids));
        }
}