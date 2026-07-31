package com.zs.lawyer.contact.contactPeople.controller;

import cn.hutool.core.bean.BeanUtil;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.contact.contactPeople.domain.excel.ContactPeopleExcel;
import com.zs.lawyer.contact.contactPeople.domain.params.ContactPeopleAddParams;
import com.zs.lawyer.contact.contactPeople.domain.params.ContactPeoplePageQueryParams;
import com.zs.lawyer.contact.contactPeople.domain.params.ContactPeopleSelectQueryParams;
import com.zs.lawyer.contact.contactPeople.domain.params.ContactPeopleUpdateParams;
import com.zs.lawyer.contact.contactPeople.domain.vo.ContactPeopleVO;
import com.zs.lawyer.contact.contactPeople.service.ContactPeopleService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 通讯录联系人 前端控制器
 * </p>
 *
 * @author zs
 * @since 2025-08-26 10:42:27
 */
@RestController
@RequestMapping("/lawyer/contact/contactPeople")
public class ContactPeopleController {

    @Resource
    private ContactPeopleService contactPeopleService;

    @GetMapping("page")
    @PreAuthorize("hasAuthority('contact:contactPeople:page')")
    public Result<PageResult<ContactPeopleVO>> page(ContactPeoplePageQueryParams contactPeoplePageQueryParams) {
        PageResult<ContactPeopleVO> iPage = contactPeopleService.page(contactPeoplePageQueryParams);
        return new Result<PageResult<ContactPeopleVO>>().ok(iPage);
    }

    @GetMapping("list")
    @PreAuthorize("hasAuthority('contact:contactPeople:list')")
    public Result<List<ContactPeopleVO>> list(ContactPeopleSelectQueryParams contactPeopleSelectQueryParams) {
        List<ContactPeopleVO> list = contactPeopleService.getList(contactPeopleSelectQueryParams);
        return new Result<List<ContactPeopleVO>>().ok(list);
    }

    @Log(module = "通讯录联系人-新增", type = OperationTypeEnum.ADD, description = "新增通讯录联系人信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('contact:contactPeople:save')")
    public Result<?> save(@RequestBody ContactPeopleAddParams contactPeopleAddParams) {
				contactPeopleService.save(contactPeopleAddParams);
        return new Result<>().ok();
    }

    @Log(module = "通讯录联系人-修改", type = OperationTypeEnum.UPDATE, description = "修改通讯录联系人信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('contact:contactPeople:update')")
    public Result<?> update(@RequestBody ContactPeopleUpdateParams contactPeopleUpdateParams) {
				contactPeopleService.update(contactPeopleUpdateParams);
        return new Result<>().ok();
    }


    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('contact:contactPeople:info')")
    public Result<ContactPeopleVO> get(@PathVariable("id") Long id) {
				ContactPeopleVO contactPeopleVO = contactPeopleService.getById(id);
        return new Result<ContactPeopleVO>().ok(contactPeopleVO);
    }


    @Log(module = "通讯录联系人-删除", type = OperationTypeEnum.DELETE, description = "删除通讯录联系人信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('contact:contactPeople:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
				contactPeopleService.deleteById(id);
        return new Result<>().ok();
    }

    @Log(module = "通讯录联系人-批量删除", type = OperationTypeEnum.DELETE_BATCH, description = "批量删除通讯录联系人信息")
    @DeleteMapping
    @PreAuthorize("hasAuthority('contact:contactPeople:batchDelete')")
    public Result<?> batchDelete(@RequestBody Long[] ids) {
				contactPeopleService.batchDelById(ids);
        return new Result<>().ok();
    }
    @Log(module = "通讯录联系人-导出", type = OperationTypeEnum.EXPORT, description = "导出通讯录联系人信息")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('contact:contactPeople:export')")
    public void export(HttpServletResponse response, ContactPeopleSelectQueryParams contactPeopleSelectQueryParams) throws IOException {
        List<ContactPeopleVO> list = contactPeopleService.getList(contactPeopleSelectQueryParams);
        List<ContactPeopleExcel> excelList = BeanUtil.copyToList(list, ContactPeopleExcel.class);
        ExcelUtils.exportExcel(response, "通讯录联系人.xlsx", ContactPeopleExcel.class, excelList);

    }
}
