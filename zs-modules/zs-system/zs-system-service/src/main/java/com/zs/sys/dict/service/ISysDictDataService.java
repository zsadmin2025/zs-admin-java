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

    /**
     * 分页查询字典数据
     * @param sysDictDataPageQueryParams 字典数据分页查询参数
     * @return 字典数据VO分页结果
     */
    PageResult<SysDictDataVO> page(SysDictDataPageQueryParams sysDictDataPageQueryParams);

    /**
     * 获取字典数据列表
     * @return 字典数据VO列表
     */
    Map<String, List<SysDictDataVO>> getList();

    /**
     * 根据字典类型查询字典数据列表
     * @param sysDictDataSelectQueryParams 字典数据查询参数
     * @return 字典数据VO列表
     */
    List<SysDictDataVO> dictTypeList(SysDictDataSelectQueryParams sysDictDataSelectQueryParams);

    /**
     * 根据id查询
     * @param id 字典数据id
     * @return 字典数据VO
     */
    SysDictDataVO getById(Long id);

    /**
     * 新增
     * @param sysDictDataAddParams 字典数据新增参数
     */
    void save(SysDictDataAddParams sysDictDataAddParams);

    /**
     * 修改
     * @param sysDictDataUpdateParams 字典数据修改参数
     */
    void update(SysDictDataUpdateParams sysDictDataUpdateParams);

    /**
     * 删除
     * @param id 字典数据id
     */
    void deleteById(Long id);

    /**
     * 批量删除
     * @param sysDictDataIds 字典数据id
     */
    void batchDelById(Long[] sysDictDataIds);

    /**
     * 根据字典类型检查是否存在字典子项
     * @param sysDictTypeId 字典类型id
     * @return 存在返回true
     */
    boolean countByDictId(Long sysDictTypeId);

}
