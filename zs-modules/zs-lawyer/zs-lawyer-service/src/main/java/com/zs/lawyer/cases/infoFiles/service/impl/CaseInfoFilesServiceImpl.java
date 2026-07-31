package com.zs.lawyer.cases.infoFiles.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.cases.infoFiles.domain.entity.CaseInfoFilesEntity;
import com.zs.lawyer.cases.infoFiles.domain.params.CaseInfoFilesAddParams;
import com.zs.lawyer.cases.infoFiles.domain.params.CaseInfoFilesPageQueryParams;
import com.zs.lawyer.cases.infoFiles.domain.params.CaseInfoFilesSelectQueryParams;
import com.zs.lawyer.cases.infoFiles.domain.params.CaseInfoFilesUpdateParams;
import com.zs.lawyer.cases.infoFiles.domain.vo.CaseInfoFilesVO;
import com.zs.lawyer.cases.infoFiles.mapper.CaseInfoFilesMapper;
import com.zs.lawyer.cases.infoFiles.service.CaseInfoFilesService;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 案件相关附件表 服务实现类
 * </p>
 *
 * @author zs
 * @since 2025-06-21 09:34:39
 */
@Service
public class CaseInfoFilesServiceImpl extends ServiceImpl<CaseInfoFilesMapper, CaseInfoFilesEntity> implements CaseInfoFilesService {

    @Override
    public PageResult<CaseInfoFilesVO> page(@NotNull CaseInfoFilesPageQueryParams caseInfoFilesPageQueryParams) {

        Page<CaseInfoFilesEntity> page = new PageInfo<>(caseInfoFilesPageQueryParams);
        QueryWrapper<CaseInfoFilesEntity> wrapper = new QueryWrapper<>();

        IPage<CaseInfoFilesEntity> iPage = baseMapper.selectPage(page, wrapper);
        List<CaseInfoFilesVO> list = BeanUtil.copyToList(iPage.getRecords(), CaseInfoFilesVO.class);

        return new PageResult<>(list, page.getTotal(), CaseInfoFilesVO.class);
    }

    @Override
    public List<CaseInfoFilesVO> getList(@NotNull CaseInfoFilesSelectQueryParams caseInfoFilesSelectQueryParams) {
        QueryWrapper<CaseInfoFilesEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(CaseInfoFilesEntity::getCaseInfoId, caseInfoFilesSelectQueryParams.getCaseInfoId())
                .eq(Objects.nonNull(caseInfoFilesSelectQueryParams.getCaseOtherId()) ,CaseInfoFilesEntity::getCaseOtherId, caseInfoFilesSelectQueryParams.getCaseOtherId());

        return BeanUtil.copyToList(baseMapper.selectList(wrapper), CaseInfoFilesVO.class);
    }

    @Override
    public void save(@NotNull CaseInfoFilesAddParams caseInfoFilesAddParams) {
        CaseInfoFilesEntity caseInfoFilesEntity = BeanUtil.copyProperties(caseInfoFilesAddParams, CaseInfoFilesEntity.class);
        baseMapper.insert(caseInfoFilesEntity);
    }

    @Override
    public void save(List<CaseInfoFilesAddParams> caseInfoFilesAddParams) {
        baseMapper.insertOrUpdate(BeanUtil.copyToList(caseInfoFilesAddParams, CaseInfoFilesEntity.class));
    }

    @Override
    public void saveOrUpdateBatch(List<CaseInfoFilesUpdateParams> caseInfoFiles, Long caseInfoId, Long caseOtherId, Integer fileSource) {
        // 1.提交的附件id
        List<Long> caseInfoFilesId = caseInfoFiles.stream().map(CaseInfoFilesUpdateParams::getCaseInfoFilesId).toList();

        // 2.数据库中已存在的附件id
        List<CaseInfoFilesEntity> caseInfoFilesEntities = baseMapper.selectList(new LambdaQueryWrapper<CaseInfoFilesEntity>()
                .eq(CaseInfoFilesEntity::getCaseInfoId, caseInfoId)
                .eq(CaseInfoFilesEntity::getCaseOtherId, caseOtherId));

        // 3. 删除不在列表中的附件
        List<Long> dbCaseInfoFilesId = caseInfoFilesEntities.stream().map(CaseInfoFilesEntity::getCaseInfoFilesId).toList();
        List<Long> deleteCaseInfoFilesId = dbCaseInfoFilesId.stream().filter(id -> !caseInfoFilesId.contains(id)).toList();

        if (!deleteCaseInfoFilesId.isEmpty()) {
            baseMapper.deleteByIds(deleteCaseInfoFilesId);
        }


        caseInfoFiles.forEach(caseInfoFilesVO -> {
            caseInfoFilesVO.setCaseInfoId(caseInfoId);
            caseInfoFilesVO.setCaseOtherId(caseOtherId);
            caseInfoFilesVO.setFileSource(fileSource);
        });

        // 4. 更新现有的
        baseMapper.insertOrUpdate(BeanUtil.copyToList(caseInfoFiles, CaseInfoFilesEntity.class));


    }

    @Override
    public void update(@NotNull CaseInfoFilesUpdateParams caseInfoFilesUpdateParams) {
        CaseInfoFilesEntity caseInfoFilesEntity = BeanUtil.copyProperties(caseInfoFilesUpdateParams, CaseInfoFilesEntity.class);
        baseMapper.updateById(caseInfoFilesEntity);
    }

    @Override
    public CaseInfoFilesVO getById(Long id) {
        return BeanUtil.copyProperties(baseMapper.selectById(id), CaseInfoFilesVO.class);
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