    package com.zs.gen.engine;


    import com.baomidou.mybatisplus.generator.config.ConstVal;
    import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
    import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
    import freemarker.template.Configuration;
    import freemarker.template.Template;
    import jakarta.validation.constraints.NotNull;

    import java.io.ByteArrayOutputStream;
    import java.io.File;
    import java.io.IOException;
    import java.io.StringWriter;
    import java.nio.charset.StandardCharsets;
    import java.util.HashMap;
    import java.util.Map;
    import java.util.zip.ZipEntry;
    import java.util.zip.ZipOutputStream;

    public class ZipFreemarkerTemplateEngine extends FreemarkerTemplateEngine {

        // 子类自己维护的Configuration实例
        private Configuration config;

        // 存储生成的文件内容
        private final Map<String, String> generatedFiles = new HashMap<>();

        // ZIP输出流
        private ByteArrayOutputStream zipOutputStream;

        @Override
        public @NotNull ZipFreemarkerTemplateEngine init(@NotNull ConfigBuilder configBuilder) {
            super.init(configBuilder);
            // 初始化自己的Configuration实例，复制父类的配置逻辑
            this.config = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
            this.config.setDefaultEncoding(ConstVal.UTF8);
            this.config.setClassForTemplateLoading(FreemarkerTemplateEngine.class, "/");

            this.zipOutputStream = new ByteArrayOutputStream();
            return this;
        }

        /**
         * 重写writer方法，将生成的内容存储到内存而不是写入文件
         */
        @Override
        public void writer(@NotNull Map<String, Object> objectMap, @NotNull String templatePath, @NotNull File outputFile) throws Exception {
            // 使用子类自己的config实例获取模板，避免访问父类私有字段
            Template template = this.config.getTemplate(templatePath);
            StringWriter writer = new StringWriter();
            template.process(objectMap, writer);

            // 获取相对路径作为ZIP中的条目名称
            String zipEntryName = getZipEntryName(outputFile);
            generatedFiles.put(zipEntryName, writer.toString());

            this.LOGGER.debug("模板:{};  内存文件:{}", templatePath, zipEntryName);
        }


        /**
         * 生成ZIP文件字节数组
         */
        public byte[] generateZip() throws IOException {
            zipOutputStream = new ByteArrayOutputStream();
            try (ZipOutputStream zos = new ZipOutputStream(zipOutputStream, StandardCharsets.UTF_8)) {
                // 遍历所有生成的文件，添加到ZIP中
                for (Map.Entry<String, String> entry : generatedFiles.entrySet()) {
                    // 确保条目名称不以/开头
                    String entryName = entry.getKey();
                    if (entryName.startsWith("/") || entryName.startsWith("\\")) {
                        entryName = entryName.substring(1);
                    }
                    // 确保条目名称不为空
                    if (!entryName.isEmpty()) {
                        ZipEntry zipEntry = new ZipEntry(entryName.replace("\\", "/"));
                        zos.putNextEntry(zipEntry);
                        zos.write(entry.getValue().getBytes(StandardCharsets.UTF_8));
                        zos.closeEntry();
                    }
                }
            }
            return zipOutputStream.toByteArray();
        }

        /**
         * 获取文件在ZIP中的相对路径
         */
        private String getZipEntryName(File outputFile) {
            // 获取配置的输出目录
            String outputDir = System.getProperty("user.dir");
            String filePath = outputFile.getAbsolutePath();

            // 统一使用正斜杠作为路径分隔符
            String normalizedOutputDir = outputDir.replace("\\", "/").replace("//", "/");
            String normalizedFilePath = filePath.replace("\\", "/").replace("//", "/");

            // 如果文件路径包含输出目录，则移除输出目录部分
            if (normalizedFilePath.startsWith(normalizedOutputDir)) {
                String entryName = normalizedFilePath.substring(normalizedOutputDir.length());
                // 移除开头的斜杠（如果有的话）
                if (entryName.startsWith("/") || entryName.startsWith("\\")) {
                    entryName = entryName.substring(1);
                }
                return entryName;
            }

            // 否则直接返回文件名
            return outputFile.getName();
        }

        /**
         * 清除已生成的文件内容，以便重用引擎
         */
        public void clearGeneratedFiles() {
            generatedFiles.clear();
            if (zipOutputStream != null) {
                zipOutputStream.reset();
            }
        }

    }

