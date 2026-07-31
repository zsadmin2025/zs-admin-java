package com.zs.lawyer.contact.contactCategory.controller;

import cn.hutool.core.bean.BeanUtil;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.contact.contactCategory.domain.excel.ContactCategoryExcel;
import com.zs.lawyer.contact.contactCategory.domain.params.ContactCategoryAddParams;
import com.zs.lawyer.contact.contactCategory.domain.params.ContactCategoryPageQueryParams;
import com.zs.lawyer.contact.contactCategory.domain.params.ContactCategorySelectQueryParams;
import com.zs.lawyer.contact.contactCategory.domain.params.ContactCategoryUpdateParams;
import com.zs.lawyer.contact.contactCategory.domain.vo.ContactCategoryVO;
import com.zs.lawyer.contact.contactCategory.service.ContactCategoryService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 通讯录分类 前端控制器
 * </p>
 *
 * @author zs
 * @since 2025-08-26 10:34:29
 */
@RestController
@RequestMapping("/lawyer/contact/contactCategory")
public class ContactCategoryController {

    @Resource
    private ContactCategoryService contactCategoryService;

    @GetMapping("page")
    @PreAuthorize("hasAuthority('contact:contactCategory:page')")
    public Result<PageResult<ContactCategoryVO>> page(ContactCategoryPageQueryParams contactCategoryPageQueryParams) {
        PageResult<ContactCategoryVO> iPage = contactCategoryService.page(contactCategoryPageQueryParams);
        return new Result<PageResult<ContactCategoryVO>>().ok(iPage);
    }

    @GetMapping("list")
    @PreAuthorize("hasAuthority('contact:contactCategory:list')")
    public Result<List<ContactCategoryVO>> list(ContactCategorySelectQueryParams contactCategorySelectQueryParams) {
        List<ContactCategoryVO> list = contactCategoryService.getList(contactCategorySelectQueryParams);
        return new Result<List<ContactCategoryVO>>().ok(list);
    }

    @Log(module = "通讯录分类-新增", type = OperationTypeEnum.ADD, description = "新增通讯录分类信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('contact:contactCategory:save')")
    public Result<?> save(@RequestBody ContactCategoryAddParams contactCategoryAddParams) {
				contactCategoryService.save(contactCategoryAddParams);
        return new Result<>().ok();
    }

    @Log(module = "通讯录分类-修改", type = OperationTypeEnum.UPDATE, description = "修改通讯录分类信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('contact:contactCategory:update')")
    public Result<?> update(@RequestBody ContactCategoryUpdateParams contactCategoryUpdateParams) {
				contactCategoryService.update(contactCategoryUpdateParams);
        return new Result<>().ok();
    }


    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('contact:contactCategory:info')")
    public Result<ContactCategoryVO> get(@PathVariable("id") Long id) {
				ContactCategoryVO contactCategoryVO = contactCategoryService.getById(id);
        return new Result<ContactCategoryVO>().ok(contactCategoryVO);
    }


    @Log(module = "通讯录分类-删除", type = OperationTypeEnum.DELETE, description = "删除通讯录分类信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('contact:contactCategory:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
				contactCategoryService.deleteById(id);
        return new Result<>().ok();
    }

    @Log(module = "通讯录分类-批量删除", type = OperationTypeEnum.DELETE_BATCH, description = "批量删除通讯录分类信息")
    @DeleteMapping
    @PreAuthorize("hasAuthority('contact:contactCategory:batchDelete')")
    public Result<?> batchDelete(@RequestBody Long[] ids) {
				contactCategoryService.batchDelById(ids);
        return new Result<>().ok();
    }
    @Log(module = "通讯录分类-导出", type = OperationTypeEnum.EXPORT, description = "导出通讯录分类信息")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('contact:contactCategory:export')")
    public void export(HttpServletResponse response, ContactCategorySelectQueryParams contactCategorySelectQueryParams) throws IOException {
        List<ContactCategoryVO> list = contactCategoryService.getList(contactCategorySelectQueryParams);
        List<ContactCategoryExcel> excelList = BeanUtil.copyToList(list, ContactCategoryExcel.class);
        ExcelUtils.exportExcel(response, "通讯录分类.xlsx", ContactCategoryExcel.class, excelList);

    }
}
