package ${packageName}.${moduleName}.${businessName}.service.impl;

import ${packageName}.${moduleName}.${businessName}.domain.entity.${ClassName}Entity;
import ${packageName}.${moduleName}.${businessName}.mapper.${ClassName}Mapper;
import ${packageName}.${moduleName}.${businessName}.service.${ClassName}Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import ${packageName}.${moduleName}.${businessName}.domain.vo.${ClassName}VO;
import ${packageName}.${moduleName}.${businessName}.domain.params.${ClassName}PageQueryParams;
import ${packageName}.${moduleName}.${businessName}.domain.params.${ClassName}SelectQueryParams;
import ${packageName}.${moduleName}.${businessName}.domain.params.${ClassName}AddParams;
import ${packageName}.${moduleName}.${businessName}.domain.params.${ClassName}UpdateParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import cn.hutool.core.util.ObjectUtil;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * ${table.comment!} 服务实现类
 * </p>
 *
 * @author ${author}
 * {@code @date} ${date}
 */
@Service
public class ${ClassName}ServiceImpl extends ServiceImpl<${ClassName}Mapper, ${ClassName}Entity> implements ${ClassName}Service {

        @Override
        public PageResult<${ClassName}VO> page(@NotNull ${ClassName}PageQueryParams ${className}PageQueryParams) {
            Page<${ClassName}Entity> page = new PageInfo<>(${className}PageQueryParams);
            LambdaQueryWrapper<${ClassName}Entity> wrapper = new LambdaQueryWrapper<>();
<#list columnList as column>
<#if column.isQuery == '1'>
    <#if column.queryType == 'EQ'>
            wrapper.eq(ObjectUtil.isNotEmpty(${className}PageQueryParams.get${column.javaField?cap_first}()), ${ClassName}Entity::get${column.javaField?cap_first}, ${className}PageQueryParams.get${column.javaField?cap_first}());
    <#elseif column.queryType == 'NE'>
            wrapper.ne(ObjectUtil.isNotEmpty(${className}PageQueryParams.get${column.javaField?cap_first}()), ${ClassName}Entity::get${column.javaField?cap_first}, ${className}PageQueryParams.get${column.javaField?cap_first}());
    <#elseif column.queryType == 'GT'>
            wrapper.gt(ObjectUtil.isNotEmpty(${className}PageQueryParams.get${column.javaField?cap_first}()), ${ClassName}Entity::get${column.javaField?cap_first}, ${className}PageQueryParams.get${column.javaField?cap_first}());
    <#elseif column.queryType == 'LT'>
            wrapper.lt(ObjectUtil.isNotEmpty(${className}PageQueryParams.get${column.javaField?cap_first}()), ${ClassName}Entity::get${column.javaField?cap_first}, ${className}PageQueryParams.get${column.javaField?cap_first}());
    <#elseif column.queryType == 'GTE'>
            wrapper.ge(ObjectUtil.isNotEmpty(${className}PageQueryParams.get${column.javaField?cap_first}()), ${ClassName}Entity::get${column.javaField?cap_first}, ${className}PageQueryParams.get${column.javaField?cap_first}());
    <#elseif column.queryType == 'LTE'>
            wrapper.le(ObjectUtil.isNotEmpty(${className}PageQueryParams.get${column.javaField?cap_first}()), ${ClassName}Entity::get${column.javaField?cap_first}, ${className}PageQueryParams.get${column.javaField?cap_first}());
    <#elseif column.queryType == 'LIKE'>
            wrapper.like(ObjectUtil.isNotEmpty(${className}PageQueryParams.get${column.javaField?cap_first}()), ${ClassName}Entity::get${column.javaField?cap_first}, ${className}PageQueryParams.get${column.javaField?cap_first}());
    <#elseif column.queryType == 'BETWEEN'>
            wrapper.between(ObjectUtil.isNotEmpty(${className}PageQueryParams.get${column.javaField?cap_first}Start()) && ObjectUtil.isNotEmpty(${className}PageQueryParams.get${column.javaField?cap_first}End()), ${ClassName}Entity::get${column.javaField?cap_first}, ${className}PageQueryParams.get${column.javaField?cap_first}Start(), ${className}PageQueryParams.get${column.javaField?cap_first}End());
    </#if>
</#if>
</#list>
            IPage<${ClassName}Entity> iPage = baseMapper.selectPage(page, wrapper);
            List<${ClassName}VO> list = BeanUtil.copyToList(iPage.getRecords(), ${ClassName}VO.class);

            return new PageResult<>(list, page.getTotal(), ${ClassName}VO.class);
        }

        @Override
        public List<${ClassName}VO> getList(@NotNull ${ClassName}SelectQueryParams ${className}SelectQueryParams) {
            LambdaQueryWrapper<${ClassName}Entity> wrapper = new LambdaQueryWrapper<>();
<#list columnList as column>
<#if column.isQuery == '1'>
    <#if column.queryType == 'EQ'>
            wrapper.eq(ObjectUtil.isNotEmpty(${className}SelectQueryParams.get${column.javaField?cap_first}()), ${ClassName}Entity::get${column.javaField?cap_first}, ${className}SelectQueryParams.get${column.javaField?cap_first}());
    <#elseif column.queryType == 'NE'>
            wrapper.ne(ObjectUtil.isNotEmpty(${className}SelectQueryParams.get${column.javaField?cap_first}()), ${ClassName}Entity::get${column.javaField?cap_first}, ${className}SelectQueryParams.get${column.javaField?cap_first}());
    <#elseif column.queryType == 'GT'>
            wrapper.gt(ObjectUtil.isNotEmpty(${className}SelectQueryParams.get${column.javaField?cap_first}()), ${ClassName}Entity::get${column.javaField?cap_first}, ${className}SelectQueryParams.get${column.javaField?cap_first}());
    <#elseif column.queryType == 'LT'>
            wrapper.lt(ObjectUtil.isNotEmpty(${className}SelectQueryParams.get${column.javaField?cap_first}()), ${ClassName}Entity::get${column.javaField?cap_first}, ${className}SelectQueryParams.get${column.javaField?cap_first}());
    <#elseif column.queryType == 'GTE'>
            wrapper.ge(ObjectUtil.isNotEmpty(${className}SelectQueryParams.get${column.javaField?cap_first}()), ${ClassName}Entity::get${column.javaField?cap_first}, ${className}SelectQueryParams.get${column.javaField?cap_first}());
    <#elseif column.queryType == 'LTE'>
            wrapper.le(ObjectUtil.isNotEmpty(${className}SelectQueryParams.get${column.javaField?cap_first}()), ${ClassName}Entity::get${column.javaField?cap_first}, ${className}SelectQueryParams.get${column.javaField?cap_first}());
    <#elseif column.queryType == 'LIKE'>
            wrapper.like(ObjectUtil.isNotEmpty(${className}SelectQueryParams.get${column.javaField?cap_first}()), ${ClassName}Entity::get${column.javaField?cap_first}, ${className}SelectQueryParams.get${column.javaField?cap_first}());
    <#elseif column.queryType == 'BETWEEN'>
            wrapper.between(ObjectUtil.isNotEmpty(${className}SelectQueryParams.get${column.javaField?cap_first}Start()) && ObjectUtil.isNotEmpty(${className}SelectQueryParams.get${column.javaField?cap_first}End()), ${ClassName}Entity::get${column.javaField?cap_first}, ${className}PageQueryParams.get${column.javaField?cap_first}Start(), ${className}PageQueryParams.get${column.javaField?cap_first}End());
    </#if>
</#if>
</#list>
            return BeanUtil.copyToList(baseMapper.selectList(wrapper), ${ClassName}VO.class);
        }

        @Override
        public void save(@NotNull ${ClassName}AddParams ${className}AddParams) {
            ${ClassName}Entity ${className}Entity = BeanUtil.copyProperties(${className}AddParams, ${ClassName}Entity.class);
            baseMapper.insert(${className}Entity);
        }

        @Override
        public void update(@NotNull ${ClassName}UpdateParams ${className}UpdateParams) {
            ${ClassName}Entity ${className}Entity = BeanUtil.copyProperties(${className}UpdateParams, ${ClassName}Entity.class);
            baseMapper.updateById(${className}Entity);
        }

        @Override
        public ${ClassName}VO getById(Long id) {
            ${ClassName}VO ${className}VO = BeanUtil.copyProperties(baseMapper.selectById(id), ${ClassName}VO.class);
            return ${className}VO;
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