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
     * 导出Excel
     *
     */
    public static void exportExcel(@NotNull HttpServletResponse response, @NotNull String fileName, Class<?> clazz, Collection<?> list) throws IOException {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8) + ".xlsx");

            OutputStream outputStream = response.getOutputStream();
            EasyExcel.write(outputStream, clazz)
                    // 关闭自动流关闭，解决AOP日志流冲突
                    .autoCloseStream(false)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .registerConverter(new LongStringConverter())
                    .sheet("")
                    .doWrite(list);
            // 强制刷新缓冲区，防止数据缺失
            outputStream.flush();
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
