package com.zs.common.core.excel;


import cn.hutool.json.JSONUtil;
import cn.idev.excel.EasyExcel;
import cn.idev.excel.converters.longconverter.LongStringConverter;
import cn.idev.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.zs.common.core.core.Result;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

/**
 * @author zsadmin
 */
public class ExcelUtils {


    /**
     * TODO: 导出Excel
     * 待完成：在controller调用此方法实现excel导出时，方法添加了AOP日志记录会报错Caused by: java.lang.IllegalStateException: getOutputStream() has already been called for this response。但不影响使用。
     */
    public static void exportExcel(@NotNull HttpServletResponse response, @NotNull String fileName, Class<?> clazz, Collection<?> list) throws IOException {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8) + ".xlsx");

            OutputStream outputStream = response.getOutputStream();
            EasyExcel.write(outputStream, clazz)
                    .autoCloseStream(true)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .registerConverter(new LongStringConverter())
                    .sheet("")
                    .doWrite(list);
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Result<?> result = new Result<>().error("下载文件失败" + e.getMessage());
            response.getWriter().println(JSONUtil.toJsonStr(result));
        }

    }

}
