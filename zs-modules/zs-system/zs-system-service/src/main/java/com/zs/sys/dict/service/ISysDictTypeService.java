package com.zs.sys.dict.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.sys.dict.domain.entity.SysDictTypeEntity;
import com.zs.sys.dict.domain.params.SysDictTypeAddParams;
import com.zs.sys.dict.domain.params.SysDictTypeQueryParams;
import com.zs.sys.dict.domain.vo.SysDictTypeVO;
import jakarta.annotation.Nullable;

import java.util.List;


/**
 * @author zsadmin
 */
public interface ISysDictTypeService extends IService<SysDictTypeEntity> {

    PageResult<SysDictTypeVO> page(SysDictTypeQueryParams sysDictTypeQueryParams);

    @Nullable
    List<SysDictTypeVO> list(SysDictTypeQueryParams sysDictTypeQueryParams);

    SysDictTypeVO getById(Long id);

    void save(SysDictTypeAddParams sysDictTypeAddParams);

    void update(SysDictTypeAddParams sysDictTypeAddParams);

    void deleteById(Long id);

}
