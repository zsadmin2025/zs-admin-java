package com.zs.gen.engine;

import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.validation.constraints.NotNull;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class PreviewFreemarkerTemplateEngine extends FreemarkerTemplateEngine {
    private Configuration configuration;
    private final Map<String, String> previewResults = new HashMap<>();

    @Override
    public void writer(@NotNull Map<String, Object> objectMap,
                       @NotNull String templatePath,
                       @NotNull File outputFile) throws Exception {

        // 1. 获取模板
        Template template = this.configuration.getTemplate(templatePath);

        // 2. 使用 StringWriter 捕获输出（而不是写入文件）
        StringWriter stringWriter = new StringWriter();
        template.process(objectMap, stringWriter);

        // 3. 保存预览内容
        String content = stringWriter.toString();
        String fileName = outputFile.getName(); // 或用路径
        previewResults.put(fileName, content);

        // 4. 打印预览
        System.out.println("📄 预览文件: " + fileName);
        System.out.println(content);
        System.out.println("──────────────────────────");
    }

    public Map<String, String> getPreviewResults() {
        return previewResults;
    }
}