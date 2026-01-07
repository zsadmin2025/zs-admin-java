package com.zs.sys.dict.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.exception.ZsException;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.sys.dict.domain.entity.SysDictTypeEntity;
import com.zs.sys.dict.domain.params.SysDictTypeAddParams;
import com.zs.sys.dict.domain.params.SysDictTypeQueryParams;
import com.zs.sys.dict.domain.vo.SysDictTypeVO;
import com.zs.sys.dict.mapper.SysDictTypeMapper;
import com.zs.sys.dict.service.ISysDictDataService;
import com.zs.sys.dict.service.ISysDictTypeService;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zsadmin
 */
@Service
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictTypeEntity> implements ISysDictTypeService {

    @Resource
    private ISysDictDataService iSysDictDataService;

    @NotNull
    @Override
    public PageResult<SysDictTypeVO> page(@NotNull SysDictTypeQueryParams sysDictTypeQueryParams) {
        Page<SysDictTypeEntity> page = new PageInfo<>(sysDictTypeQueryParams);
        QueryWrapper<SysDictTypeEntity> wrapper = new QueryWrapper<>();
        wrapper.like(Strings.isNotEmpty(sysDictTypeQueryParams.getDictType()), "dict_type", sysDictTypeQueryParams.getDictType());
        wrapper.like(Strings.isNotEmpty(sysDictTypeQueryParams.getDictName()), "dict_name", sysDictTypeQueryParams.getDictName());

        IPage<SysDictTypeEntity> iPage = baseMapper.selectPage(page, wrapper);
        List<SysDictTypeVO> list = BeanUtil.copyToList(iPage.getRecords(), SysDictTypeVO.class);

        return new PageResult<>(list, page.getTotal(), SysDictTypeVO.class);
    }

    @Nullable
    @Override
    public List<SysDictTypeVO> list(SysDictTypeQueryParams sysDictTypeQueryParams) {
        return BeanUtil.copyToList(baseMapper.selectList(new QueryWrapper<SysDictTypeEntity>().orderByAsc("sort")), SysDictTypeVO.class);
    }

    @Override
    public SysDictTypeVO getById(Long id) {
        return BeanUtil.copyProperties(baseMapper.selectById(id), SysDictTypeVO.class);
    }

    @Override
    public void save(SysDictTypeAddParams sysDictTypeAddParams) {
        SysDictTypeEntity sysDictTypeEntity = BeanUtil.copyProperties(sysDictTypeAddParams, SysDictTypeEntity.class);
        baseMapper.insert(sysDictTypeEntity);
    }

    @Override
    public void update(SysDictTypeAddParams sysDictTypeAddParams) {
        SysDictTypeEntity sysDictTypeEntity = BeanUtil.copyProperties(sysDictTypeAddParams, SysDictTypeEntity.class);
        baseMapper.updateById(sysDictTypeEntity);
    }

    @Override
    public void deleteById(Long sysDictTypeId) {

        // 检查是否存在字典子项
        if (iSysDictDataService.countByDictId(sysDictTypeId)) {
            throw new ZsException("存在字典子项，不允许删除，请先删除子项。");
        }
        baseMapper.deleteById(sysDictTypeId);
    }


}
