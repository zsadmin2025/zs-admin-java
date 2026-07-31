package com.zs.lawyer.cases.info.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.enums.CaseStatusEnum;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.common.core.utils.SecurityUtil;
import com.zs.lawyer.cases.contract.domain.entity.CaseContractEntity;
import com.zs.lawyer.cases.contract.domain.entity.CaseContractNodeEntity;
import com.zs.lawyer.cases.contract.domain.params.CaseContractAddParams;
import com.zs.lawyer.cases.contract.domain.params.CaseContractUpdateParams;
import com.zs.lawyer.cases.contract.service.CaseContractNodeService;
import com.zs.lawyer.cases.contract.service.CaseContractService;
import com.zs.lawyer.cases.customer.domain.entity.CaseCustomerEntity;
import com.zs.lawyer.cases.customer.domain.params.CaseCustomerAddParams;
import com.zs.lawyer.cases.customer.domain.params.CaseCustomerUpdateParams;
import com.zs.lawyer.cases.customer.service.CaseCustomerService;
import com.zs.lawyer.cases.hearing.domain.entity.CaseHearingEntity;
import com.zs.lawyer.cases.hearing.domain.params.CaseHearingAddParams;
import com.zs.lawyer.cases.hearing.domain.params.CaseHearingSelectQueryParams;
import com.zs.lawyer.cases.hearing.domain.params.CaseHearingUpdateParams;
import com.zs.lawyer.cases.hearing.domain.vo.CaseHearingVO;
import com.zs.lawyer.cases.hearing.service.CaseHearingService;
import com.zs.lawyer.cases.info.domain.entity.CaseInfoEntity;
import com.zs.lawyer.cases.info.domain.entity.CaseRelatedPartiesEntity;
import com.zs.lawyer.cases.info.domain.params.*;
import com.zs.lawyer.cases.info.domain.vo.CaseHomeHearingVO;
import com.zs.lawyer.cases.info.domain.vo.CaseHomeVO;
import com.zs.lawyer.cases.info.domain.vo.CaseInfoVO;
import com.zs.lawyer.cases.info.domain.vo.CaseVO;
import com.zs.lawyer.cases.info.mapper.CaseInfoMapper;
import com.zs.lawyer.cases.info.service.CaseInfoService;
import com.zs.lawyer.cases.info.service.CaseRelatedPartiesService;
import com.zs.lawyer.cases.infoApprovalForm.service.CaseInfoApprovalFormService;
import com.zs.lawyer.cases.infoApprove.domain.params.CaseInfoApprovePageQueryParams;
import com.zs.lawyer.cases.infoApprove.domain.params.CaseInfoApproveSelectQueryParams;
import com.zs.lawyer.cases.infoApprove.domain.vo.CaseInfoApproveVO;
import com.zs.lawyer.cases.infoApprove.service.CaseInfoApproveService;
import com.zs.lawyer.cases.infoFiles.domain.entity.CaseInfoFilesEntity;
import com.zs.lawyer.cases.infoFiles.domain.params.CaseInfoFilesSelectQueryParams;
import com.zs.lawyer.cases.infoFiles.service.CaseInfoFilesService;
import com.zs.lawyer.cases.infoList.domain.entity.CaseInfoListEntity;
import com.zs.lawyer.cases.infoList.service.CaseInfoListService;
import com.zs.lawyer.cases.team.domain.entity.CaseTeamEntity;
import com.zs.lawyer.cases.team.domain.params.CaseTeamAddParams;
import com.zs.lawyer.cases.team.domain.params.CaseTeamUpdateParams;
import com.zs.lawyer.cases.team.service.CaseTeamService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 案件信息表 服务实现类
 * </p>
 *
 * @author zs
 * @since 2025-06-08 16:43:20
 */
@Service
public class CaseInfoServiceImpl extends ServiceImpl<CaseInfoMapper, CaseInfoEntity> implements CaseInfoService {

    @Resource
    private CaseCustomerService caseCustomerService; // 客户信息
    @Resource
    private CaseHearingService caseHearingService; // 案件开庭信息
    @Resource
    private CaseTeamService caseTeamService; // 案件团队信息
    @Resource
    private CaseContractService caseContractService; // 案件合同信息
    @Resource
    private CaseContractNodeService caseContractNodeService; // 案件合同节点信息
    @Resource
    private CaseRelatedPartiesService caseRelatedPartiesService; // 案件关联方信息
    @Resource
    private CaseInfoFilesService caseInfoFilesService; // 案件文件信息
    @Resource
    private CaseInfoListService caseInfoListService; // 案件界结案目录信息
    @Resource
    private CaseInfoApproveService caseInfoApproveService; // 案件审批信息
    @Resource
    private CaseInfoApprovalFormService caseInfoApprovalFormService; // 案件审批表

    @Override
    public PageResult<CaseVO> page(@NotNull CaseInfoPageQueryParams caseInfoPageQueryParams) {

        Page<CaseInfoEntity> page = new PageInfo<>(caseInfoPageQueryParams);
        QueryWrapper<CaseInfoEntity> wrapper = getQueryWrapper(caseInfoPageQueryParams);

        IPage<CaseInfoEntity> iPage = baseMapper.selectPage(page, wrapper);
        List<CaseInfoVO> list = BeanUtil.copyToList(iPage.getRecords(), CaseInfoVO.class);

        List<CaseVO> caseVOList = new ArrayList<>();

        // 提取所有案件ID用于批量查询
        List<Long> caseInfoIds = list.stream().map(CaseInfoVO::getCaseInfoId).filter(Objects::nonNull).toList();

        // 批量获取开庭信息
        List<CaseHearingVO> allHearings = new ArrayList<>();
        if (!caseInfoIds.isEmpty()) {
            allHearings = caseHearingService.getList(caseInfoIds);

        }

        // 按案件ID分组开庭信息（O(n)）
        Map<Long, List<CaseHearingVO>> hearingsByCase = new HashMap<>();
        for (CaseHearingVO hearing : allHearings) {
            Long caseId = hearing.getCaseInfoId();
            if (caseId != null) {
                hearingsByCase.computeIfAbsent(caseId, k -> new ArrayList<>()).add(hearing);
            }
        }

        list.forEach(caseInfoVO -> {
            Long caseInfoId = caseInfoVO.getCaseInfoId();
            if (caseInfoId == null) {
                return;
            }

            CaseVO caseVO = new CaseVO();
            caseVO.setCaseCustomer(caseCustomerService.getByCaseInfoId(caseInfoId));
            caseInfoVO.setOurSide(caseRelatedPartiesService.getOurSideListByCaseInfoId(caseInfoId));
            caseInfoVO.setOtherSide(caseRelatedPartiesService.getOtherSideListByCaseInfoId(caseInfoId));
            caseVO.setCaseInfo(caseInfoVO);

            // 获取已预加载的开庭信息
            caseVO.setCaseHearingList(hearingsByCase.getOrDefault(caseInfoId, new ArrayList<>()));

            // 案件团队信息

            // 案件团队信息
            caseVO.setCaseTeam(caseTeamService.getByCaseInfoId(caseInfoVO.getCaseInfoId()));

            // 案件合同信息
            caseVO.setCaseContract(caseContractService.getByCaseInfoId(caseInfoVO.getCaseInfoId()));

            caseVOList.add(caseVO);

        });

        return new PageResult<>(caseVOList, page.getTotal(), CaseVO.class);
    }

    private static QueryWrapper<CaseInfoEntity> getQueryWrapper(CaseInfoPageQueryParams caseInfoPageQueryParams) {
        QueryWrapper<CaseInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(Objects.nonNull(caseInfoPageQueryParams.getIsVoided()), CaseInfoEntity::getIsVoided, caseInfoPageQueryParams.getIsVoided())  //
                .eq(StringUtils.isNotBlank(caseInfoPageQueryParams.getCaseType()), CaseInfoEntity::getCaseType, caseInfoPageQueryParams.getCaseType()) // 案件类型
                .like(StringUtils.isNotBlank(caseInfoPageQueryParams.getCaseName()), CaseInfoEntity::getCaseName, caseInfoPageQueryParams.getCaseName())     // 案件名称
                .like(StringUtils.isNotBlank(caseInfoPageQueryParams.getCaseNo()), CaseInfoEntity::getCaseNo, caseInfoPageQueryParams.getCaseNo()) // 案件编号
                .eq(StringUtils.isNotBlank(caseInfoPageQueryParams.getCaseType()), CaseInfoEntity::getCaseType, caseInfoPageQueryParams.getCaseType()) // 案件类型
                .like(StringUtils.isNotBlank(caseInfoPageQueryParams.getProxyStage()), CaseInfoEntity::getProxyStage, caseInfoPageQueryParams.getProxyStage()) // 代理阶段
                .like(StringUtils.isNotBlank(caseInfoPageQueryParams.getApplicantName()), CaseInfoEntity::getApplicantName, caseInfoPageQueryParams.getApplicantName()) // 申请人名称
                .like(StringUtils.isNotBlank(caseInfoPageQueryParams.getLitigationStatus()), CaseInfoEntity::getLitigationStatus, caseInfoPageQueryParams.getLitigationStatus())
                .like(Objects.nonNull(caseInfoPageQueryParams.getCaseStatus()), CaseInfoEntity::getCaseStatus, caseInfoPageQueryParams.getCaseStatus())
                .in(caseInfoPageQueryParams.getCaseStatusList() != null && !caseInfoPageQueryParams.getCaseStatusList().isEmpty(), CaseInfoEntity::getCaseStatus, caseInfoPageQueryParams.getCaseStatusList())
                .like(StringUtils.isNotBlank(caseInfoPageQueryParams.getNowStage()), CaseInfoEntity::getNowStage, caseInfoPageQueryParams.getNowStage())
                .like(StringUtils.isNotBlank(caseInfoPageQueryParams.getProxyStage()), CaseInfoEntity::getProxyStage, caseInfoPageQueryParams.getProxyStage());
        return wrapper;
    }

    @Override
    public PageResult<CaseVO> approvePage(CaseInfoApprovePageQueryParams caseInfoApprovePageQueryParams) {
        Page<CaseInfoEntity> page = new PageInfo<>(caseInfoApprovePageQueryParams);
        Map<String, Object> params = BeanUtil.beanToMap(caseInfoApprovePageQueryParams);
        if(!SecurityUtil.isAdmin()){
            params.put("approvalLawyer", SecurityUtil.getUserId()); // 待审批律师
        }

        IPage<CaseInfoEntity> iPage = baseMapper.approvePage(page, params);

        List<CaseInfoVO> list = BeanUtil.copyToList(iPage.getRecords(), CaseInfoVO.class);

        List<CaseVO> caseVOList = new ArrayList<>();

        list.forEach(caseInfoVO -> {
            Long caseInfoId = caseInfoVO.getCaseInfoId();

            CaseVO caseVO = new CaseVO();
            caseVO.setCaseCustomer(caseCustomerService.getByCaseInfoId(caseInfoId));
            caseVO.setCaseInfo(caseInfoVO);

            caseVOList.add(caseVO);

        });

        return new PageResult<>(caseVOList, iPage.getTotal(), CaseVO.class);
    }

    @Override
    public List<CaseInfoVO> getList(@NotNull CaseInfoSelectQueryParams caseInfoSelectQueryParams) {
        QueryWrapper<CaseInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(CaseInfoEntity::getIsVoided, 1);
        return BeanUtil.copyToList(baseMapper.selectList(wrapper), CaseInfoVO.class);
    }

    public String generateCustomerCode() {
        String year = DateUtil.thisYear() + ""; // 获取当前年份，如 20250101
        // 根据当前日期查询数据库最大客户编号
        String maxCustomerCode = baseMapper.findMaxCodeByPrefix("DA" + year);
        int nextNum = 1;
        if (maxCustomerCode != null && maxCustomerCode.length() >= 12) {
            String numStr = maxCustomerCode.substring(6, 12);
            try {
                nextNum = Integer.parseInt(numStr) + 1;
            } catch (NumberFormatException e) {
                // 记录日志：编号格式异常
            }
        }
        return "DA" + year + String.format("%06d", nextNum);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(CaseAddParams caseAddParams) {

        Boolean isApproval = true;

        // 1. 保存案件信息
        CaseInfoEntity caseInfoEntity = saveCaseInfo(caseAddParams, isApproval);

        // 2. 保存我方与对方当事人信息
        saveRelatedParties(caseAddParams.getCaseInfo().getOurSide(), caseInfoEntity.getCaseInfoId());
        saveRelatedParties(caseAddParams.getCaseInfo().getOtherSide(), caseInfoEntity.getCaseInfoId());

        // 3. 保存客户信息
        saveCustomer(caseAddParams.getCaseCustomer(), caseInfoEntity.getCaseInfoId());

        // 4. 保存开庭信息（如果存在）
        saveHearingList(caseAddParams.getCaseHearingList(), caseInfoEntity.getCaseInfoId());

        // 5. 保存团队信息
        saveTeam(caseAddParams.getCaseTeam(), caseInfoEntity.getCaseInfoId());

        // 6. 保存合同信息
        saveContract(caseAddParams.getCaseContract(), caseInfoEntity.getCaseInfoId());

        // 7. 保存审批信息
        saveApproval(caseAddParams.getApprovalLawyerList(), caseInfoEntity.getCaseInfoId());
    }

    @Override
    public void passApprove(CaseApproveParams caseApproveParams) {
        CaseInfoEntity caseInfoEntity = new CaseInfoEntity();
        caseInfoEntity.setCaseInfoId(caseApproveParams.getCaseInfoId());
        caseInfoEntity.setApproveStatus(2);
        caseInfoEntity.setApprovalLawyer(SecurityUtil.getUserId());
        caseInfoEntity.setApprovalLawyerName(SecurityUtil.getRealName());
        caseInfoEntity.setApprovalOpinion(caseApproveParams.getApprovalOpinion());
        caseInfoEntity.setApprovalTime(new Date());

        baseMapper.updateById(caseInfoEntity);
    }

    @Override
    public void rejectApprove(CaseApproveParams caseApproveParams) {

        CaseInfoEntity caseInfoEntity = new CaseInfoEntity();
        caseInfoEntity.setCaseInfoId(caseApproveParams.getCaseInfoId());
        caseInfoEntity.setApproveStatus(0);
        caseInfoEntity.setApprovalLawyer(SecurityUtil.getUserId());
        caseInfoEntity.setApprovalLawyerName(SecurityUtil.getRealName());
        caseInfoEntity.setApprovalOpinion(caseApproveParams.getApprovalOpinion());
        caseInfoEntity.setApprovalTime(new Date());

        baseMapper.updateById(caseInfoEntity);
    }


    private CaseInfoEntity saveCaseInfo(CaseAddParams caseAddParams, Boolean isApproval) {
        // 案件信息
        CaseInfoEntity caseInfoEntity = BeanUtil.copyProperties(caseAddParams.getCaseInfo(), CaseInfoEntity.class);
        // 将List<string>转换成string
        caseInfoEntity.setProxyStage(caseAddParams.getCaseInfo().getProxyStage().stream().map(Object::toString).collect(Collectors.joining(",")));
        // 案件编号
        caseInfoEntity.setCaseNo(generateCustomerCode());
        // 申请时间
        caseInfoEntity.setApplyDate(new Date());
        // 申请人，当前登录用户
        caseInfoEntity.setApplicant(SecurityUtil.getUserId());
        caseInfoEntity.setApplicantName(SecurityUtil.getRealName());

        if (isApproval){
            caseInfoEntity.setIsApprove(1);
            caseInfoEntity.setApproveStatus(1);
        }else {
            caseInfoEntity.setIsApprove(0);
        }

        baseMapper.insert(caseInfoEntity);
        return caseInfoEntity;
    }

    private void saveRelatedParties(List<CaseRelatedPartiesAddParams> parties, Long caseInfoId) {
        if (CollectionUtils.isEmpty(parties)) return;

        parties.forEach(p -> p.setCaseInfoId(caseInfoId));
        caseRelatedPartiesService.saveBatch(BeanUtil.copyToList(parties, CaseRelatedPartiesEntity.class));
    }

    private void saveCustomer(CaseCustomerAddParams customer, Long caseInfoId) {
        if (customer == null) return;

        customer.setCaseInfoId(caseInfoId);
        caseCustomerService.save(customer);
    }

    private void saveHearingList(List<CaseHearingAddParams> hearingList, Long caseInfoId) {
        if (CollectionUtils.isEmpty(hearingList)) return;

        hearingList.forEach(h -> h.setCaseInfoId(caseInfoId));
        caseHearingService.save(hearingList);
    }

    private void saveTeam(CaseTeamAddParams team, Long caseInfoId) {
        if (team == null) return;

        team.setCaseInfoId(caseInfoId);
        caseTeamService.save(team);
    }

    private void saveContract(CaseContractAddParams contract, Long caseInfoId) {
        if (contract == null) return;

        contract.setCaseInfoId(caseInfoId);
        caseContractService.save(contract);
    }
    private void saveApproval(List<Long> approvalLawyerList, Long caseInfoId) {
        caseInfoApproveService.save(approvalLawyerList, caseInfoId);
    }

    @Override
    public void update(CaseUpdateParams caseUpdateParams) {

        Boolean isApproval = true;

        // 1. 更新案件信息
        CaseInfoEntity caseInfoEntity = updateCaseInfo(caseUpdateParams, isApproval);

        // 2. 保存我方与对方当事人信息
        saveOrUpdateRelatedParties(caseUpdateParams.getCaseInfo().getOurSide());
        saveOrUpdateRelatedParties(caseUpdateParams.getCaseInfo().getOtherSide());

        // 3. 保存客户信息
        saveOrUpdateCustomer(caseUpdateParams.getCaseCustomer());

        // 4. 保存开庭信息（如果存在）
        saveOrUpdateHearingList(caseUpdateParams.getCaseHearingList());

        // 5. 保存团队信息
        saveOrUpdateTeam(caseUpdateParams.getCaseTeam());

        // 6. 保存合同信息
        saveOrUpdateContract(caseUpdateParams.getCaseContract());

        // 7. 保存审批信息
        saveOrUpdateApproval(caseUpdateParams.getApprovalLawyerList(), caseInfoEntity.getCaseInfoId());
    }

    @Override
    public CaseInfoVO getByCaseInfoId(Long caseInfoId) {
        CaseInfoEntity caseInfoEntity = baseMapper.selectById(caseInfoId);
        return BeanUtil.copyProperties(caseInfoEntity, CaseInfoVO.class);
    }

    private CaseInfoEntity updateCaseInfo(CaseUpdateParams caseUpdateParams, Boolean isApproval) {
        // 案件信息
        CaseInfoEntity caseInfoEntity = BeanUtil.copyProperties(caseUpdateParams.getCaseInfo(), CaseInfoEntity.class);
        // 将List<string>转换成string
        caseInfoEntity.setProxyStage(caseUpdateParams.getCaseInfo().getProxyStage().stream().map(Object::toString).collect(Collectors.joining(",")));

        if (isApproval){
            caseInfoEntity.setIsApprove(1);
            caseInfoEntity.setApproveStatus(1);
        }else {
            caseInfoEntity.setIsApprove(0);
        }

        baseMapper.updateById(caseInfoEntity);

        return caseInfoEntity;
    }


    private void saveOrUpdateRelatedParties(List<CaseRelatedPartiesUpdateParams> parties) {
        if (CollectionUtils.isEmpty(parties)) return;

        caseRelatedPartiesService.saveOrUpdateBatch(BeanUtil.copyToList(parties, CaseRelatedPartiesEntity.class));
    }

    private void saveOrUpdateCustomer(CaseCustomerUpdateParams customer) {
        if (customer == null) return;
        caseCustomerService.update(customer);
    }

    private void saveOrUpdateHearingList(List<CaseHearingUpdateParams> hearingList) {
        if (CollectionUtils.isEmpty(hearingList)) return;
        caseHearingService.update(hearingList);
    }

    private void saveOrUpdateTeam(CaseTeamUpdateParams team) {
        if (team == null) return;
        caseTeamService.update(team);
    }

    private void saveOrUpdateContract(CaseContractUpdateParams contract) {
        if (contract == null) return;
        caseContractService.update(contract);
    }

    private void saveOrUpdateApproval(List<Long> approvalLawyerList, Long caseInfoId) {
        caseInfoApproveService.update(approvalLawyerList, caseInfoId);
    }


    @Override
    public CaseVO getById(Long id) {
        CaseInfoEntity caseInfoEntity = baseMapper.selectById(id);
        if (caseInfoEntity == null) {
            return new CaseVO(); // 或抛出自定义异常，视业务需求而定
        }

        CaseInfoVO caseInfoVO = BeanUtil.copyProperties(caseInfoEntity, CaseInfoVO.class);

        // 安全处理 proxyStage 字段
        String proxyStageStr = caseInfoEntity.getProxyStage();
        if (proxyStageStr != null && !proxyStageStr.isEmpty()) {
            caseInfoVO.setProxyStage(Arrays.asList(proxyStageStr.split(",")));
        } else {
            caseInfoVO.setProxyStage(Collections.emptyList());
        }


        CaseVO caseVO = new CaseVO();
        caseVO.setCaseInfo(caseInfoVO);

        caseVO.setCaseCustomer(caseCustomerService.getByCaseInfoId(caseInfoVO.getCaseInfoId()));
        caseInfoVO.setOurSide(caseRelatedPartiesService.getOurSideListByCaseInfoId(caseInfoVO.getCaseInfoId()));
        caseInfoVO.setOtherSide(caseRelatedPartiesService.getOtherSideListByCaseInfoId(caseInfoVO.getCaseInfoId()));

        CaseHearingSelectQueryParams caseHearingSelectQueryParams = new CaseHearingSelectQueryParams();
        caseHearingSelectQueryParams.setCaseInfoId(caseInfoVO.getCaseInfoId());

        caseVO.setCaseHearingList(caseHearingService.getList(caseHearingSelectQueryParams));

        // 案件团队信息
        caseVO.setCaseTeam(caseTeamService.getByCaseInfoId(caseInfoVO.getCaseInfoId()));

        // 案件合同信息
        caseVO.setCaseContract(caseContractService.getByCaseInfoId(caseInfoVO.getCaseInfoId()));

        // 附件信息
        CaseInfoFilesSelectQueryParams caseInfoFilesSelectQueryParams = new CaseInfoFilesSelectQueryParams();
        caseInfoFilesSelectQueryParams.setCaseInfoId(caseInfoVO.getCaseInfoId());
        caseVO.setCaseInfoFilesList(caseInfoFilesService.getList(caseInfoFilesSelectQueryParams));

        // 案件结案目录信息
        caseInfoListService.getList(caseInfoVO.getCaseInfoId(), caseInfoVO.getCaseType());
        caseVO.setCaseInfoList(caseInfoListService.getList(caseInfoVO.getCaseInfoId(), caseInfoVO.getCaseType()));

        CaseInfoApproveSelectQueryParams caseInfoApproveSelectQueryParams = new CaseInfoApproveSelectQueryParams();
        caseInfoApproveSelectQueryParams.setCaseInfoId(caseInfoVO.getCaseInfoId());
        List<CaseInfoApproveVO> caseInfoApproveVOList  = caseInfoApproveService.getList(caseInfoApproveSelectQueryParams);
        caseVO.setApprovalLawyerList(caseInfoApproveVOList.stream().map(CaseInfoApproveVO::getApprovalLawyer).collect(Collectors.toList()));

        // 案件审批表
        caseVO.setCaseInfoApprovalForm(caseInfoApprovalFormService.getByCaseInfoId(caseInfoVO.getCaseInfoId()));
        return caseVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Long id) {
        baseMapper.deleteById(id);


        // 删除关联信息
        removeByCaseInfoId(caseRelatedPartiesService, CaseRelatedPartiesEntity::getCaseInfoId, id);
        removeByCaseInfoId(caseCustomerService, CaseCustomerEntity::getCaseInfoId, id);
        removeByCaseInfoId(caseHearingService, CaseHearingEntity::getCaseInfoId, id);
        removeByCaseInfoId(caseTeamService, CaseTeamEntity::getCaseInfoId, id);
        removeByCaseInfoId(caseContractService, CaseContractEntity::getCaseInfoId, id);
        removeByCaseInfoId(caseContractNodeService, CaseContractNodeEntity::getCaseInfoId, id);
        removeByCaseInfoId(caseInfoFilesService, CaseInfoFilesEntity::getCaseInfoId, id);
        removeByCaseInfoId(caseInfoListService, CaseInfoListEntity::getCaseInfoId, id);

    }

    /**
     * 通用删除方法，用于根据 caseInfoId 删除关联数据
     */
    private <T, E> void removeByCaseInfoId(IService<T> service, SFunction<T, ?> fieldFunc, Long caseInfoId) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(fieldFunc, caseInfoId);
        service.remove(wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchDelById(@NotNull Long[] ids) {
        baseMapper.deleteByIds(Arrays.asList(ids));

        for (Long id : ids) {
            // 删除关联信息
            removeByCaseInfoId(caseRelatedPartiesService, CaseRelatedPartiesEntity::getCaseInfoId, id);
            removeByCaseInfoId(caseCustomerService, CaseCustomerEntity::getCaseInfoId, id);
            removeByCaseInfoId(caseHearingService, CaseHearingEntity::getCaseInfoId, id);
            removeByCaseInfoId(caseTeamService, CaseTeamEntity::getCaseInfoId, id);
            removeByCaseInfoId(caseContractService, CaseContractEntity::getCaseInfoId, id);
            removeByCaseInfoId(caseContractNodeService, CaseContractNodeEntity::getCaseInfoId, id);
            removeByCaseInfoId(caseInfoFilesService, CaseInfoFilesEntity::getCaseInfoId, id);
            removeByCaseInfoId(caseInfoListService, CaseInfoListEntity::getCaseInfoId, id);
        }

    }

    @Override
    public void closed(CaseInfoStatusParams caseInfoStatusParams) {
        CaseInfoEntity caseInfoEntity = new CaseInfoEntity();
        caseInfoEntity.setCaseInfoId(caseInfoStatusParams.getCaseInfoId());
        caseInfoEntity.setCaseStatus(CaseStatusEnum.CLOSED.getCode());

        this.updateById(caseInfoEntity);
    }

    @Override
    public void filing(CaseInfoStatusParams caseInfoStatusParams) {
        CaseInfoEntity caseInfoEntity = new CaseInfoEntity();
        caseInfoEntity.setCaseInfoId(caseInfoStatusParams.getCaseInfoId());
        caseInfoEntity.setCaseStatus(CaseStatusEnum.FILING.getCode());

        this.updateById(caseInfoEntity);
    }

    @Override
    public void cancel(CaseInfoStatusParams caseInfoStatusParams) {
        CaseInfoEntity caseInfoEntity = new CaseInfoEntity();
        caseInfoEntity.setCaseInfoId(caseInfoStatusParams.getCaseInfoId());
        caseInfoEntity.setIsVoided(1);

        this.updateById(caseInfoEntity);
    }

    @Override
    public void restore(CaseInfoStatusParams caseInfoStatusParams) {
        CaseInfoEntity caseInfoEntity = new CaseInfoEntity();
        caseInfoEntity.setCaseInfoId(caseInfoStatusParams.getCaseInfoId());
        caseInfoEntity.setIsVoided(0);

        this.updateById(caseInfoEntity);
    }

    @Override
    public void savePowerAttorney(CaseInfoPowerAttorneyParams caseInfoPowerAttorneyParams) {
        CaseInfoEntity caseInfoEntity = new CaseInfoEntity();
        caseInfoEntity.setCaseInfoId(caseInfoPowerAttorneyParams.getCaseInfoId());
        caseInfoEntity.setPowerAttorney(caseInfoPowerAttorneyParams.getPowerAttorney());

        this.updateById(caseInfoEntity);
    }

    @Override
    public List<CaseHomeVO> getRecentThreeMonthRegisteredCase() {
        List<CaseInfoEntity> entityList = this.baseMapper.getRecentThreeMonthRegisteredCase();

        List<CaseInfoVO> list = BeanUtil.copyToList(entityList, CaseInfoVO.class);

        List<CaseHomeVO> caseVOList = new ArrayList<>();

        for (CaseInfoVO caseInfoVO : list) {
            Long caseInfoId = caseInfoVO.getCaseInfoId();
            if (caseInfoId == null) {
                continue;
            }

            CaseHomeVO caseVO = new CaseHomeVO();
            caseVO.setCaseCustomer(caseCustomerService.getByCaseInfoId(caseInfoId));
            caseVO.setCaseInfo(caseInfoVO);

            caseVOList.add(caseVO);
        }
        return caseVOList;
    }

    @Override
    public List<CaseHomeHearingVO> getRecentOneMonthHearingCase() {
        return this.baseMapper.getRecentOneMonthHearingCase();
    }


}