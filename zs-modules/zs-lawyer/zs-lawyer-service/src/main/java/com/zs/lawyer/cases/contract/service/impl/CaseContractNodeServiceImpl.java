package com.zs.lawyer.cases.contract.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.cases.contract.domain.entity.CaseContractNodeEntity;
import com.zs.lawyer.cases.contract.domain.params.CaseContractNodeAddParams;
import com.zs.lawyer.cases.contract.domain.params.CaseContractNodePageQueryParams;
import com.zs.lawyer.cases.contract.domain.params.CaseContractNodeSelectQueryParams;
import com.zs.lawyer.cases.contract.domain.params.CaseContractNodeUpdateParams;
import com.zs.lawyer.cases.contract.domain.vo.CaseContractNodeVO;
import com.zs.lawyer.cases.contract.mapper.CaseContractNodeMapper;
import com.zs.lawyer.cases.contract.service.CaseContractNodeService;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 案件合同节点 服务实现类
 * </p>
 *
 * @author zs
 * @since 2025-06-08 18:03:56
 */
@Service
public class CaseContractNodeServiceImpl extends ServiceImpl<CaseContractNodeMapper, CaseContractNodeEntity> implements CaseContractNodeService {

    @Override
    public PageResult<CaseContractNodeVO> page(@NotNull CaseContractNodePageQueryParams caseContractNodePageQueryParams) {

        Page<CaseContractNodeEntity> page = new PageInfo<>(caseContractNodePageQueryParams);
        QueryWrapper<CaseContractNodeEntity> wrapper = new QueryWrapper<>();

        IPage<CaseContractNodeEntity> iPage = baseMapper.selectPage(page, wrapper);
        List<CaseContractNodeVO> list = BeanUtil.copyToList(iPage.getRecords(), CaseContractNodeVO.class);

        return new PageResult<>(list, page.getTotal(), CaseContractNodeVO.class);
    }

    @Override
    public List<CaseContractNodeVO> getList(@NotNull CaseContractNodeSelectQueryParams caseContractNodeSelectQueryParams) {
        QueryWrapper<CaseContractNodeEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(CaseContractNodeEntity::getCaseContractId, caseContractNodeSelectQueryParams.getCaseContractId());
        return BeanUtil.copyToList(baseMapper.selectList(wrapper), CaseContractNodeVO.class);
    }

    @Override
    public void save(@NotNull CaseContractNodeAddParams caseContractNodeAddParams) {
        CaseContractNodeEntity caseContractNodeEntity = BeanUtil.copyProperties(caseContractNodeAddParams, CaseContractNodeEntity.class);
        baseMapper.insert(caseContractNodeEntity);
    }

    @Override
    public void save(List<CaseContractNodeAddParams> caseContractNodeAddParams) {
        List<CaseContractNodeEntity> caseContractNodeEntityList = BeanUtil.copyToList(caseContractNodeAddParams, CaseContractNodeEntity.class);
        baseMapper.insert(caseContractNodeEntityList);
    }

    @Override
    public void update(@NotNull CaseContractNodeUpdateParams caseContractNodeUpdateParams) {
        CaseContractNodeEntity caseContractNodeEntity = BeanUtil.copyProperties(caseContractNodeUpdateParams, CaseContractNodeEntity.class);
        baseMapper.updateById(caseContractNodeEntity);
    }

    @Override
    public CaseContractNodeVO getById(Long id) {
        CaseContractNodeVO caseContractNodeVO = BeanUtil.copyProperties(baseMapper.selectById(id), CaseContractNodeVO.class);
        return caseContractNodeVO;
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