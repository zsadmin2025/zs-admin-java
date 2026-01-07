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
 * {@code @date} ${date}
 */
public interface ${ClassName}Service extends IService<${ClassName}Entity> {

    /**
     * 分页查询
     * @param ${className}PageQueryParams 查询参数
     * @return PageResult<${ClassName}VO>
     */
    PageResult<${ClassName}VO> page(${ClassName}PageQueryParams ${className}PageQueryParams);

    /**
     * 查询列表
     * @param ${className}SelectQueryParams 查询参数
     * @return List<${ClassName}VO>
     */
    List<${ClassName}VO> getList(${ClassName}SelectQueryParams ${className}SelectQueryParams);

    /**
     * 新增
     * @param ${className}AddParams 新增参数
     */
    void save(${ClassName}AddParams ${className}AddParams);

    /**
     * 更新
     * @param ${className}UpdateParams 更新参数
     */
    void update(${ClassName}UpdateParams ${className}UpdateParams);

    /**
     * 根据id查询
     * @param id 主键
     * @return ${ClassName}VO
     */
    ${ClassName}VO getById(Long id);

    /**
     * 单个删除
     * @param ${className}Id 主键
     */
    void deleteById(Long ${className}Id);

    /**
     * 批量删除
     * @param ${className}Ids 主键数组
     */
    void batchDelById(Long[] ${className}Ids);
}
