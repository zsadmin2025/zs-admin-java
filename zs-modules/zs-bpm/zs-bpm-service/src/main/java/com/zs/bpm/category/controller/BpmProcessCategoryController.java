package com.zs.bpm.category.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zs.bpm.category.domain.params.BpmProcessCategoryAddParams;
import com.zs.bpm.category.domain.params.BpmProcessCategoryQueryParams;
import com.zs.bpm.category.domain.params.BpmProcessCategoryUpdateParams;
import com.zs.bpm.category.domain.vo.BpmProcessCategoryVO;
import com.zs.bpm.category.service.IBpmProcessCategoryService;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.page.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 流程分类 Controller
 *
 * @author zsadmin
 */
@RestController
@RequestMapping("bpm/setting/category")
@Tag(name = "流程分类")
public class BpmProcessCategoryController {

    @Resource
    private IBpmProcessCategoryService bpmProcessCategoryService;

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "分页查询")
//    @PreAuthorize("hasAuthority('bpm:category:page')")
    @GetMapping("page")
    public Result<PageResult<BpmProcessCategoryVO>> page(BpmProcessCategoryQueryParams params) {
        PageResult<BpmProcessCategoryVO> page = bpmProcessCategoryService.page(params);
        return new Result<PageResult<BpmProcessCategoryVO>>().ok(page);
    }

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "列表查询")
//    @PreAuthorize("hasAuthority('bpm:category:list')")
    @GetMapping("list")
    public Result<List<BpmProcessCategoryVO>> list(BpmProcessCategoryQueryParams params) {
        List<BpmProcessCategoryVO> list = bpmProcessCategoryService.getList(params);
        return new Result<List<BpmProcessCategoryVO>>().ok(list);
    }

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "获取分类树")
//    @PreAuthorize("hasAuthority('bpm:category:list')")
    @GetMapping("tree")
    public Result<List<BpmProcessCategoryVO>> tree() {
        List<BpmProcessCategoryVO> list = bpmProcessCategoryService.getTreeList();
        return new Result<List<BpmProcessCategoryVO>>().ok(list);
    }

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "获取分类详情")
//    @PreAuthorize("hasAuthority('bpm:category:page')")
    @GetMapping("{id}")
    public Result<BpmProcessCategoryVO> get(@PathVariable("id") Long id) {
        BpmProcessCategoryVO vo = bpmProcessCategoryService.getById(id);
        return new Result<BpmProcessCategoryVO>().ok(vo);
    }

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "新增流程分类")
    @Log(module = "流程分类-新增", type = OperationTypeEnum.ADD, description = "新增流程分类")
//    @PreAuthorize("hasAuthority('bpm:category:save')")
    @PostMapping("save")
    public Result<?> save(@RequestBody BpmProcessCategoryAddParams params) {
        bpmProcessCategoryService.save(params);
        return new Result<>().ok();
    }

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "修改流程分类")
    @Log(module = "流程分类-修改", type = OperationTypeEnum.UPDATE, description = "修改流程分类")
//    @PreAuthorize("hasAuthority('bpm:category:save')")
    @PutMapping("update")
    public Result<?> update(@RequestBody BpmProcessCategoryUpdateParams params) {

        bpmProcessCategoryService.update(params);
        return new Result<>().ok();
    }

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "删除流程分类")
    @Log(module = "流程分类-删除", type = OperationTypeEnum.DELETE, description = "删除流程分类")
//    @PreAuthorize("hasAuthority('bpm:category:delete')")
    @DeleteMapping("{id}")
    public Result<?> delete(@PathVariable("id") Long id) {
        bpmProcessCategoryService.removeById(id);
        return new Result<>().ok();
    }
}
