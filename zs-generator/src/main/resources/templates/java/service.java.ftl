package ${packageName}.${moduleName}.${businessName}.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import ${packageName}.${moduleName}.${businessName}.domain.entity.${ClassName}Entity;
import ${packageName}.${moduleName}.${businessName}.domain.vo.${ClassName}VO;
import ${packageName}.${moduleName}.${businessName}.domain.params.${ClassName}PageQueryParams;
import ${packageName}.${moduleName}.${businessName}.domain.params.${ClassName}SelectQueryParams;
import ${packageName}.${moduleName}.${businessName}.domain.params.${ClassName}AddParams;
import ${packageName}.${moduleName}.${businessName}.domain.params.${ClassName}UpdateParams;

import java.util.List;

/**
 * <p>
 * ${table.comment!} 服务类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
public interface ${ClassName}Service extends IService<${ClassName}Entity> {

    /** 分页 **/
    PageResult<${ClassName}VO> page(${ClassName}PageQueryParams ${className}PageQueryParams);

    /** 列表 **/
    List<${ClassName}VO> getList(${ClassName}SelectQueryParams ${className}SelectQueryParams);

    /** 新增 **/
    void save(${ClassName}AddParams ${className}AddParams);

    /** 更新 **/
    void update(${ClassName}UpdateParams ${className}UpdateParams);

    /** 根据id查询 **/
    ${ClassName}VO getById(Long id);

    /** 单个删除 **/
    void deleteById(Long ${className}Id);

    /** 批量删除 **/
    void batchDelById(Long[] ${className}Ids);
}