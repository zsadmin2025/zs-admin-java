package com.zs.lawyer.cases.hearing.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.enums.FileSourceEnum;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.cases.hearing.domain.entity.CaseHearingEntity;
import com.zs.lawyer.cases.hearing.domain.params.CaseHearingAddParams;
import com.zs.lawyer.cases.hearing.domain.params.CaseHearingPageQueryParams;
import com.zs.lawyer.cases.hearing.domain.params.CaseHearingSelectQueryParams;
import com.zs.lawyer.cases.hearing.domain.params.CaseHearingUpdateParams;
import com.zs.lawyer.cases.hearing.domain.vo.CaseHearingVO;
import com.zs.lawyer.cases.hearing.mapper.CaseHearingMapper;
import com.zs.lawyer.cases.hearing.service.CaseHearingService;
import com.zs.lawyer.cases.infoFiles.domain.entity.CaseInfoFilesEntity;
import com.zs.lawyer.cases.infoFiles.domain.params.CaseInfoFilesAddParams;
import com.zs.lawyer.cases.infoFiles.domain.params.CaseInfoFilesSelectQueryParams;
import com.zs.lawyer.cases.infoFiles.domain.params.CaseInfoFilesUpdateParams;
import com.zs.lawyer.cases.infoFiles.service.CaseInfoFilesService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 案件开庭表 服务实现类
 * </p>
 *
 * @author zs
 * @since 2025-06-08 17:58:57
 */
@Service
public class CaseHearingServiceImpl extends ServiceImpl<CaseHearingMapper, CaseHearingEntity> implements CaseHearingService {

    @Resource
    private CaseInfoFilesService caseInfoFilesService; // 案件文件信息

    @Override
    public PageResult<CaseHearingVO> page(@NotNull CaseHearingPageQueryParams caseHearingPageQueryParams) {

        Page<CaseHearingEntity> page = new PageInfo<>(caseHearingPageQueryParams);
        QueryWrapper<CaseHearingEntity> wrapper = new QueryWrapper<>();

        IPage<CaseHearingEntity> iPage = baseMapper.selectPage(page, wrapper);
        List<CaseHearingVO> list = BeanUtil.copyToList(iPage.getRecords(), CaseHearingVO.class);

        return new PageResult<>(list, page.getTotal(), CaseHearingVO.class);
    }

    @Override
    public List<CaseHearingVO> getList(@NotNull CaseHearingSelectQueryParams caseHearingSelectQueryParams) {
        QueryWrapper<CaseHearingEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(CaseHearingEntity::getCaseInfoId, caseHearingSelectQueryParams.getCaseInfoId())
                .orderByAsc(CaseHearingEntity::getCourtTime);
        List<CaseHearingEntity> entityList = baseMapper.selectList(wrapper);

        List<CaseHearingVO> list = BeanUtil.copyToList(entityList, CaseHearingVO.class);

        list.forEach(item -> {
            // 获取案件文件信息
            CaseInfoFilesSelectQueryParams caseInfoFilesSelectQueryParams = new CaseInfoFilesSelectQueryParams();
            caseInfoFilesSelectQueryParams.setCaseInfoId(item.getCaseInfoId());
            caseInfoFilesSelectQueryParams.setCaseOtherId(item.getCaseHearingId());
            item.setCaseHearingFilesList(caseInfoFilesService.getList(caseInfoFilesSelectQueryParams));

        });

        return list;
    }

    @Override
    public void save(CaseHearingAddParams caseHearingAddParams) {
        CaseHearingEntity caseHearingEntity = BeanUtil.copyProperties(caseHearingAddParams, CaseHearingEntity.class);
        baseMapper.insert(caseHearingEntity);

        List<CaseInfoFilesAddParams> caseHearingFilesList = caseHearingAddParams.getCaseHearingFilesList();
        if (caseHearingFilesList == null || caseHearingFilesList.isEmpty()) {
            return;
        }

        caseHearingFilesList.forEach(it -> {
            it.setCaseInfoId(caseHearingAddParams.getCaseInfoId());
            it.setCaseOtherId(caseHearingEntity.getCaseHearingId());
            it.setFileSource(FileSourceEnum.CASE_HEARING.getValue());
        });

        caseInfoFilesService.save(caseHearingFilesList);
    }

    @Override
    public void save(List<CaseHearingAddParams> caseHearingAddParamsList) {
        if (caseHearingAddParamsList == null || caseHearingAddParamsList.isEmpty()) {
            return;
        }

        // 转换并批量插入主表数据
        List<CaseHearingEntity> caseHearingEntityList = BeanUtil.copyToList(caseHearingAddParamsList, CaseHearingEntity.class);
        baseMapper.insert(caseHearingEntityList);

        // 逐条处理文件信息
        for (int i = 0; i < caseHearingEntityList.size(); i++) {
            CaseHearingEntity entity = caseHearingEntityList.get(i);
            CaseHearingAddParams params = caseHearingAddParamsList.get(i);

            List<CaseInfoFilesAddParams> caseHearingFilesList = params.getCaseHearingFilesList();
            if (caseHearingFilesList == null || caseHearingFilesList.isEmpty()) {
                continue;
            }

            caseHearingFilesList.forEach(it -> {
                it.setCaseInfoId(params.getCaseInfoId());
                it.setCaseOtherId(entity.getCaseHearingId());
                it.setFileSource(FileSourceEnum.CASE_HEARING.getValue());
            });

            caseInfoFilesService.save(caseHearingFilesList);
        }
    }



    @Override
    public void update(@NotNull CaseHearingUpdateParams caseHearingUpdateParams) {
        CaseHearingEntity caseHearingEntity = BeanUtil.copyProperties(caseHearingUpdateParams, CaseHearingEntity.class);
        baseMapper.updateById(caseHearingEntity);

        List<CaseInfoFilesUpdateParams> filesList = caseHearingUpdateParams.getCaseHearingFilesList();
        if (filesList == null) {
            filesList = Collections.emptyList(); // 避免 null 引发异常
        }
        caseInfoFilesService.saveOrUpdateBatch(
                filesList,
                caseHearingUpdateParams.getCaseInfoId(),
                caseHearingEntity.getCaseHearingId(),
                FileSourceEnum.CASE_HEARING.getValue()
        );

    }

    @Override
    public CaseHearingVO getById(Long id) {
        return BeanUtil.copyProperties(baseMapper.selectById(id), CaseHearingVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        baseMapper.deleteById(id);
        // 删除开庭附件信息
        caseInfoFilesService.remove(new LambdaQueryWrapper<CaseInfoFilesEntity>().eq(CaseInfoFilesEntity::getCaseOtherId, id));

    }

    @Override
    public void batchDelById(@NotNull Long[] ids) {
        baseMapper.deleteByIds(Arrays.asList(ids));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(List<CaseHearingUpdateParams> hearingList) {
        if (hearingList == null || hearingList.isEmpty()) {
            return;
        }

        // 批量保存附件数据
        for (CaseHearingUpdateParams params : hearingList){
           CaseHearingEntity caseHearingEntity = BeanUtil.copyProperties(params, CaseHearingEntity.class);
           baseMapper.insertOrUpdate(caseHearingEntity);

            List<CaseInfoFilesUpdateParams> filesList = params.getCaseHearingFilesList();
            if (filesList == null) {
                filesList = Collections.emptyList(); // 避免 null 引发异常
            }
            caseInfoFilesService.saveOrUpdateBatch(
                    filesList,
                    params.getCaseInfoId(),
                    caseHearingEntity.getCaseHearingId(),
                    FileSourceEnum.CASE_HEARING.getValue()
            );
        }

    }



    @Override
    public List<CaseHearingVO> getList(List<Long> caseInfoIds) {

        List<CaseHearingEntity> entityList = baseMapper.selectList(new LambdaQueryWrapper<CaseHearingEntity>().in(CaseHearingEntity::getCaseInfoId, caseInfoIds));

        List<CaseHearingVO> list = BeanUtil.copyToList(entityList, CaseHearingVO.class);

        list.forEach(item -> {
            // 获取案件文件信息
            CaseInfoFilesSelectQueryParams caseInfoFilesSelectQueryParams = new CaseInfoFilesSelectQueryParams();
            caseInfoFilesSelectQueryParams.setCaseInfoId(item.getCaseInfoId());
            caseInfoFilesSelectQueryParams.setCaseOtherId(item.getCaseHearingId());
            item.setCaseHearingFilesList(caseInfoFilesService.getList(caseInfoFilesSelectQueryParams));

        });

        return list;
    }
}