package com.zs.lawyer.application.contact.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.hutool.core.bean.BeanUtil;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.application.contact.domain.vo.ContactVO;
import com.zs.lawyer.application.contact.domain.params.ContactPageQueryParams;
import com.zs.lawyer.application.contact.domain.params.ContactSelectQueryParams;
import com.zs.lawyer.application.contact.domain.params.ContactAddParams;
import com.zs.lawyer.application.contact.domain.params.ContactUpdateParams;
import com.zs.lawyer.application.contact.domain.excel.ContactExcel;
import com.zs.lawyer.application.contact.service.ContactService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
/**
 * <p>
 * 通讯录联系人表 前端控制器
 * </p>
 *
 * @author zs
 * {@code @date} 2026-07-30 18:37:41
 */
@RestController
@RequestMapping("/application/contact")
public class ContactController {

    @Resource
    private ContactService contactService;

    @GetMapping("page")
    @PreAuthorize("hasAuthority('application:contact:page')")
    public Result<PageResult<ContactVO>> page(ContactPageQueryParams contactPageQueryParams) {
        PageResult<ContactVO> iPage = contactService.page(contactPageQueryParams);
        return new Result<PageResult<ContactVO>>().ok(iPage);
    }

    @GetMapping("list")
    @PreAuthorize("hasAuthority('application:contact:list')")
    public Result<List<ContactVO>> list(ContactSelectQueryParams contactSelectQueryParams) {
        List<ContactVO> list = contactService.getList(contactSelectQueryParams);
        return new Result<List<ContactVO>>().ok(list);
    }

    @Log(module = "通讯录联系人表-新增", type = OperationTypeEnum.ADD, description = "新增通讯录联系人表信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('application:contact:save')")
    public Result<?> save(@RequestBody ContactAddParams contactAddParams) {
        contactService.save(contactAddParams);
        return new Result<>().ok();
    }

    @Log(module = "通讯录联系人表-修改", type = OperationTypeEnum.UPDATE, description = "修改通讯录联系人表信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('application:contact:update')")
    public Result<?> update(@RequestBody ContactUpdateParams contactUpdateParams) {
        contactService.update(contactUpdateParams);
        return new Result<>().ok();
    }


    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('application:contact:info')")
    public Result<ContactVO> get(@PathVariable("id") Long id) {
        ContactVO contactVO = contactService.getById(id);
        return new Result<ContactVO>().ok(contactVO);
    }


    @Log(module = "通讯录联系人表-删除", type = OperationTypeEnum.DELETE, description = "删除通讯录联系人表信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('application:contact:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
        contactService.deleteById(id);
        return new Result<>().ok();
    }

    @Log(module = "通讯录联系人表-批量删除", type = OperationTypeEnum.DELETE_BATCH, description = "批量删除通讯录联系人表信息")
    @DeleteMapping
    @PreAuthorize("hasAuthority('application:contact:batchDelete')")
    public Result<?> batchDelete(@RequestBody Long[] ids) {
        contactService.batchDelById(ids);
        return new Result<>().ok();
    }
    @Log(module = "通讯录联系人表-导出", type = OperationTypeEnum.EXPORT, description = "导出通讯录联系人表信息")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('application:contact:export')")
    public void export(HttpServletResponse response, ContactSelectQueryParams contactSelectQueryParams) throws IOException {
        List<ContactVO> list = contactService.getList(contactSelectQueryParams);
        List<ContactExcel> excelList = BeanUtil.copyToList(list, ContactExcel.class);
        ExcelUtils.exportExcel(response, "通讯录联系人表.xlsx", ContactExcel.class, excelList);

    }
}
