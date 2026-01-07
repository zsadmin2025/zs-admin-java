package ${packageName}.${moduleName}.${businessName}.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.hutool.core.bean.BeanUtil;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.page.PageResult;
import ${packageName}.${moduleName}.${businessName}.domain.vo.${ClassName}VO;
import ${packageName}.${moduleName}.${businessName}.domain.params.${ClassName}PageQueryParams;
import ${packageName}.${moduleName}.${businessName}.domain.params.${ClassName}SelectQueryParams;
import ${packageName}.${moduleName}.${businessName}.domain.params.${ClassName}AddParams;
import ${packageName}.${moduleName}.${businessName}.domain.params.${ClassName}UpdateParams;
import ${packageName}.${moduleName}.${businessName}.domain.excel.${ClassName}Excel;
import ${packageName}.${moduleName}.${businessName}.service.${ClassName}Service;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
/**
 * <p>
 * ${functionName} 前端控制器
 * </p>
 *
 * @author ${author}
 * {@code @date} ${date}
 */
@RestController
@RequestMapping("/${moduleName}/${businessName}")
public class ${ClassName}Controller {

    @Resource
    private ${ClassName}Service ${className}Service;

    @GetMapping("page")
    @PreAuthorize("hasAuthority('${moduleName}:${businessName}:page')")
    public Result<PageResult<${ClassName}VO>> page(${ClassName}PageQueryParams ${className}PageQueryParams) {
        PageResult<${ClassName}VO> iPage = ${className}Service.page(${className}PageQueryParams);
        return new Result<PageResult<${ClassName}VO>>().ok(iPage);
    }

    @GetMapping("list")
    @PreAuthorize("hasAuthority('${moduleName}:${businessName}:list')")
    public Result<List<${ClassName}VO>> list(${ClassName}SelectQueryParams ${className}SelectQueryParams) {
        List<${ClassName}VO> list = ${className}Service.getList(${className}SelectQueryParams);
        return new Result<List<${ClassName}VO>>().ok(list);
    }

    @Log(module = "${functionName}-新增", type = OperationTypeEnum.ADD, description = "新增${functionName}信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('${moduleName}:${businessName}:save')")
    public Result<?> save(@RequestBody ${ClassName}AddParams ${className}AddParams) {
        ${className}Service.save(${className}AddParams);
        return new Result<>().ok();
    }

    @Log(module = "${functionName}-修改", type = OperationTypeEnum.UPDATE, description = "修改${functionName}信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('${moduleName}:${businessName}:update')")
    public Result<?> update(@RequestBody ${ClassName}UpdateParams ${className}UpdateParams) {
        ${className}Service.update(${className}UpdateParams);
        return new Result<>().ok();
    }


    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('${moduleName}:${businessName}:info')")
    public Result<${ClassName}VO> get(@PathVariable("id") Long id) {
        ${ClassName}VO ${className}VO = ${className}Service.getById(id);
        return new Result<${ClassName}VO>().ok(${className}VO);
    }


    @Log(module = "${functionName}-删除", type = OperationTypeEnum.DELETE, description = "删除${functionName}信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('${moduleName}:${businessName}:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
        ${className}Service.deleteById(id);
        return new Result<>().ok();
    }

    @Log(module = "${functionName}-批量删除", type = OperationTypeEnum.DELETE_BATCH, description = "批量删除${functionName}信息")
    @DeleteMapping
    @PreAuthorize("hasAuthority('${moduleName}:${businessName}:batchDelete')")
    public Result<?> batchDelete(@RequestBody Long[] ids) {
        ${className}Service.batchDelById(ids);
        return new Result<>().ok();
    }
    @Log(module = "${functionName}-导出", type = OperationTypeEnum.EXPORT, description = "导出${functionName}信息")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('${moduleName}:${businessName}:export')")
    public void export(HttpServletResponse response, ${ClassName}SelectQueryParams ${className}SelectQueryParams) throws IOException {
        List<${ClassName}VO> list = ${className}Service.getList(${className}SelectQueryParams);
        List<${ClassName}Excel> excelList = BeanUtil.copyToList(list, ${ClassName}Excel.class);
        ExcelUtils.exportExcel(response, "${functionName}.xlsx", ${ClassName}Excel.class, excelList);

    }
}
