package com.zs.sys.dict.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.sys.dict.domain.entity.SysDictDataEntity;
import com.zs.sys.dict.domain.params.SysDictDataAddParams;
import com.zs.sys.dict.domain.params.SysDictDataPageQueryParams;
import com.zs.sys.dict.domain.params.SysDictDataSelectQueryParams;
import com.zs.sys.dict.domain.params.SysDictDataUpdateParams;
import com.zs.sys.dict.domain.vo.SysDictDataVO;

import java.util.List;
import java.util.Map;


/**
 * @author zsadmin
 */
public interface ISysDictDataService extends IService<SysDictDataEntity> {

    /** 分页查询 **/
    PageResult<SysDictDataVO> page(SysDictDataPageQueryParams sysDictDataPageQueryParams);

    /** 查询列表 **/
    Map<String, List<SysDictDataVO>> getList();

    /** 根据字典类型查询列表 **/
    List<SysDictDataVO> dictTypeList(SysDictDataSelectQueryParams sysDictDataSelectQueryParams);

    /** 根据字典类型查询列表 **/
    SysDictDataVO getById(Long id);

    /** 新增 **/
    void save(SysDictDataAddParams sysDictDataAddParams);

    /** 修改 **/
    void update(SysDictDataUpdateParams sysDictDataUpdateParams);

    /** 删除 **/
    void deleteById(Long id);

    /** 批量删除 **/
    void batchDelById(Long[] sysDictDataIds);


}
