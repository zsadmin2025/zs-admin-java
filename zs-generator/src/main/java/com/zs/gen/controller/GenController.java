package com.zs.gen.controller;

import cn.hutool.json.JSONUtil;
import com.zs.common.core.core.Result;
import com.zs.common.core.page.PageResult;
import com.zs.gen.domain.entity.GenTable;
import com.zs.gen.domain.model.TreeNode;
import com.zs.gen.domain.params.GenTablePageQueryParams;
import com.zs.gen.domain.params.GenTableParams;
import com.zs.gen.domain.params.ImportTableRequest;
import com.zs.gen.domain.vo.GenTableVO;
import com.zs.gen.service.IGenTableColumnService;
import com.zs.gen.service.IGenTableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 代码生成 操作处理
 *
 * @author zs
 */
@RestController
@RequestMapping("/tool/gen")
@Slf4j
@Tag(name = "代码生成")
public class GenController {
    @Resource
    private IGenTableService genTableService;

    @Resource
    private IGenTableColumnService genTableColumnService;


    @Operation(summary = "查询代码生成列表")
    @GetMapping("/page")
    public Result<PageResult<GenTableVO>> page(GenTablePageQueryParams params) {
        PageResult<GenTableVO> iPage = genTableService.page(params);
        return new Result<PageResult<GenTableVO>>().ok(iPage);
    }

    @Operation(summary = "查询数据库表")
    @GetMapping("/db/page")
    public Result<PageResult<GenTableVO>> dbPage(GenTablePageQueryParams params) {
        PageResult<GenTableVO> iPage = genTableService.dbPage(params);
        return new Result<PageResult<GenTableVO>>().ok(iPage);
    }

    @Operation(summary = "导入表结构")
    @PostMapping("/importTable")
    public Result<?> importTableSave(@RequestBody ImportTableRequest request) {
        List<String> tables = request.getTables();
        genTableService.importTableSave(tables);
        return new Result<>().ok();
    }


    @Operation(summary = "修改保存代码生成业务")
    @PutMapping("/update")
    public Result<?> update(@RequestBody GenTable genTable) {

        genTableService.update(genTable);

        return new Result<>().ok();
    }

    @Operation(summary = "生成代码")
    @PostMapping("/genCode")
    public Result<?> generateCode(@RequestBody GenTableParams genTableParams) {

        genTableService.generateCode(genTableParams);

        return new Result<>().ok();
    }


    @Operation(summary = "下载代码")
    @GetMapping("/download/zip/{tableId}")
    public void downloadCodeZip(@PathVariable("tableId") Long tableId, HttpServletResponse response) throws IOException {



        try {
            byte[] zipData = genTableService.generateCodeZip(tableId);

            // 设置响应头
            response.setContentType("application/zip");
            response.setContentLength(zipData.length);

            // 处理中文文件名
            String fileName = URLEncoder.encode("zs-admin" + ".zip", StandardCharsets.UTF_8);

            response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");

            // 写入响应流
            try (OutputStream os = response.getOutputStream()) {
                os.write(zipData);
                os.flush();
            }
        } catch (Exception e) {
            // 异常时返回 JSON 错误信息（模仿 exportExcel 的风格）
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            try {
                Result<?> result = new Result<>().error("下载文件失败：" + e.getMessage());
                response.getWriter().println(JSONUtil.toJsonStr(result));
            } catch (IOException ex) {
                // 最终异常无法返回 JSON，只能打印
                log.error("无法写入错误响应", ex);
            }
        }

    }


    @Operation(summary = "查询表详细信息")
    @GetMapping("/info/{tableId}")
    public Result<GenTableVO> getInfo(@PathVariable Long tableId) {
        GenTableVO tableInfo = genTableService.getGenTableById(tableId);
        return new Result<GenTableVO>().ok(tableInfo);
    }

    @Operation(summary = "删除代码生成")
    @DeleteMapping
    public Result<?> batchDelete(@RequestBody Long[] ids) {
        genTableService.deleteGenTableByIds(ids);
        return new Result<>().ok();
    }

    @Operation(summary = "预览代码")
    @GetMapping("/preview/{tableId}")
    public Result<List<TreeNode>> preview(@PathVariable("tableId") Long tableId) throws IOException
    {
        List<TreeNode> dataMap = genTableService.previewCode(tableId);
        return new Result<List<TreeNode>>().ok(dataMap);
    }

}