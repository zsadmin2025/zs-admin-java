package com.zs.gen.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.zs.common.core.constant.Constants;
import com.zs.common.core.exception.ZsException;
import com.zs.common.core.page.PageResult;
import com.zs.gen.config.DataBaseProperties;
import com.zs.gen.config.GenConfig;
import com.zs.gen.domain.entity.GenTable;
import com.zs.gen.domain.entity.GenTableColumn;
import com.zs.gen.domain.model.TreeNode;
import com.zs.gen.domain.params.GenTablePageQueryParams;
import com.zs.gen.domain.params.GenTableParams;
import com.zs.gen.domain.vo.GenTableVO;
import com.zs.gen.mapper.GenTableMapper;
import com.zs.gen.service.IGenTableColumnService;
import com.zs.gen.service.IGenTableService;
import com.zs.gen.util.GenUtils;
import com.zs.gen.util.TreeNodeUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * 业务 服务层实现
 *
 * @author ruoyi
 */
@Service
@Slf4j
public class GenTableServiceImpl extends ServiceImpl<GenTableMapper, GenTable> implements IGenTableService {

    @Resource
    private DataBaseProperties dataBaseProperties;
    @Resource
    private IGenTableColumnService iGenTableColumnService;
    @Resource
    private GenConfig genConfig;

    String outputDir = "D:\\gen"; // 建议改为配置项

    @Override
    public PageResult<GenTableVO> page(GenTablePageQueryParams genTablePageQueryParams) {

        IPage<GenTable> page = new Page<>(genTablePageQueryParams.getCurrent(), genTablePageQueryParams.getPageSize());

        LambdaQueryWrapper<GenTable> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StrUtil.isNotBlank(genTablePageQueryParams.getTableName()), GenTable::getTableName, genTablePageQueryParams.getTableName());

        String orderField = genTablePageQueryParams.getOrderField();
        boolean isAsc = Constants.ASC.equalsIgnoreCase(genTablePageQueryParams.getOrder());
        lambdaQueryWrapper.orderBy(StringUtils.isNotBlank(orderField), isAsc, GenTable::getCreateTime);


        IPage<GenTable> pages = this.baseMapper.selectPage(page, lambdaQueryWrapper);


        List<GenTableVO> genTableVOS = BeanUtil.copyToList(pages.getRecords(), GenTableVO.class);

        return new PageResult<>(genTableVOS, pages.getTotal(), GenTableVO.class);
    }

    @Override
    public PageResult<GenTableVO> dbPage(GenTablePageQueryParams genTablePageQueryParams) {

        Page<GenTable> page = new Page<>(genTablePageQueryParams.getCurrent(), genTablePageQueryParams.getPageSize());

        LambdaQueryWrapper<GenTable> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        IPage<GenTable> pages = this.baseMapper.dbPage(page, lambdaQueryWrapper);

        List<GenTableVO> genTableVOS = BeanUtil.copyToList(pages.getRecords(), GenTableVO.class);

        return new PageResult<>(genTableVOS, pages.getTotal(), GenTableVO.class);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(GenTable genTable) {
        this.baseMapper.updateById(genTable);

        iGenTableColumnService.updateGenTableColumn(genTable.getColumns());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importTableSave(List<String> tables) {

        List<GenTable> genTableList = this.baseMapper.selectList(new LambdaQueryWrapper<GenTable>().in(GenTable::getTableName, tables));
        if (!genTableList.isEmpty()) {
            throw new ZsException("导入失败，存在重复的表名");
        }

        // 查询表信息
        List<GenTable> tableList = this.baseMapper.selectTableListByNames(tables);
        for (GenTable table : tableList) {
            String tableName = table.getTableName();
            table.setClassName(StrUtil.upperFirst(StrUtil.toCamelCase(tableName)));


            GenUtils.initTable(table, genConfig);
            this.baseMapper.insert(table);

            // 保存列信息
            List<GenTableColumn> genTableColumns = iGenTableColumnService.selectTableColumnsByName(tableName);
            for (GenTableColumn column : genTableColumns) {
                GenUtils.initColumnField(column, table);
                column.setTableId(table.getTableId());
                iGenTableColumnService.save(column);
            }

        }
    }

    @Override
    public void generateCode(GenTableParams genTableParams) {
        GenTableVO genTable = this.getGenTableById(genTableParams.getTableId());

        FreemarkerTemplateEngine engine = new FreemarkerTemplateEngine();
        FastAutoGenerator.create(dataBaseProperties.getUrl(), dataBaseProperties.getUsername(), dataBaseProperties.getPassword())
                // 全局配置
                .globalConfig(builder -> generateCodeGlobalConfig(builder, genTable))
                // 包配置
                .packageConfig(builder -> generateCodePackageConfig(builder, genTable))
                // 策略配置
                .strategyConfig(builder -> generateCodeStrategyConfig(builder, genTable))
                // 注入配置
                .injectionConfig(builder -> generateCodeInjectionConfig(builder, genTable))
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(engine).execute();

    }


    @Override
    public byte[] generateCodeZip(Long tableId) throws IOException {

        List<TreeNode> nodes = this.previewCode(tableId);
        // 创建临时目录
        Path tempDir = Files.createTempDirectory("code_gen_");
        Path outputZip = Files.createTempFile("project_", ".zip");

        try {


            // 递归生成文件
            createFiles(nodes, tempDir);

            // 打包成 ZIP
            zipDirectory(tempDir, outputZip);

            // 返回 ZIP 资源
            byte[] zipBytes = Files.readAllBytes(outputZip);

            return zipBytes;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {

            deleteDirectory(tempDir); // 删除临时目录
            Files.deleteIfExists(outputZip); // 删除临时 ZIP
        }

    }


    /**
     * 递归创建文件和目录
     */
    private void createFiles(List<TreeNode> nodes, Path parentPath) throws IOException {
        for (TreeNode node : nodes) {
            Path currentPath = parentPath.resolve(node.getTitle());

            if (node.getChildren() != null && !node.getChildren().isEmpty()) {
                // 是目录
                Files.createDirectories(currentPath);
                createFiles(node.getChildren(), currentPath);
            } else {
                // 是文件
                String content = node.getValue() != null ? node.getValue() : "";
                Files.createDirectories(currentPath.getParent()); // 确保父目录存在
                Files.writeString(currentPath, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            }
        }
    }

    /**
     * 将目录压缩为 ZIP
     */
    private void zipDirectory(Path sourceDir, Path zipPath) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipPath))) {
            Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String relativePath = sourceDir.relativize(file).toString().replace("\\", "/");
                    zos.putNextEntry(new ZipEntry(relativePath));
                    Files.copy(file, zos); // 自动写入，zos 会处理
                    zos.closeEntry();
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    if (!dir.equals(sourceDir)) {
                        String relativePath = sourceDir.relativize(dir).toString().replace("\\", "/") + "/";
                        zos.putNextEntry(new ZipEntry(relativePath));
                        zos.closeEntry();
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } // ✅ 自动关闭 zos
    }

    /**
     * 删除临时目录
     */
    private void deleteDirectory(Path path) {
        try {
            if (Files.exists(path)) {
                Files.walk(path)
                        .sorted((a, b) -> b.compareTo(a))
                        .forEach(p -> {
                            try {
                                Files.delete(p);
                            } catch (IOException e) {
                                System.err.println("删除失败: " + p);
                            }
                        });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 全局配置
    private GlobalConfig generateCodeGlobalConfig(GlobalConfig.Builder builder, GenTableVO genTable) {
        return builder.author(genTable.getFunctionAuthor())
//                .outputDir(outputDir)
                .disableOpenDir()
                .enableSwagger() // 开启 swagger
                .dateType(DateType.ONLY_DATE)
                .commentDate("yyyy-MM-dd HH:mm:ss")
                .build();
    }

    // 包配置
    private PackageConfig generateCodePackageConfig(PackageConfig.Builder builder, GenTableVO genTable) {
        return builder
                .parent(genTable.getPackageName()) // 设置父包名
                .moduleName(genTable.getModuleName() + "." + genTable.getBusinessName())
                .entity("domain.entity") // 设置实体类包名
                .mapper("mapper") // 设置 Mapper 接口包名
                .service("service") // 设置 Service 接口包名
                .serviceImpl("service.impl") // 设置 Service 实现类包名
                .xml("mappers")
                .pathInfo(Collections.singletonMap(OutputFile.xml, "/src/main/resources/mapper")) // 设置 Mapper XML 文件生成路径
                .build();
    }

    // 策略配置
    private StrategyConfig generateCodeStrategyConfig(StrategyConfig.Builder builder, GenTableVO genTable) {
        return builder.addInclude(genTable.getTableName()) // 设置需要生成的表名
                .addTablePrefix("biz_")
                .entityBuilder()
                .addIgnoreColumns("creator", "create_time", "updater", "update_time")
                .javaTemplate("/templates/java/entity.java") // 使用自定义模板
                .formatFileName("%sEntity")
                .enableFileOverride() // 启用覆盖已存在的文件


                .mapperBuilder()
                .formatMapperFileName("%sMapper")
                .formatXmlFileName("%sMapper")
                .mapperTemplate("/templates/java/mapper.java")
                .mapperXmlTemplate("/templates/java/mapper.xml")
                .enableFileOverride() // 启用覆盖已存在的文件


                .serviceBuilder()
                .formatServiceFileName("%sService")
                .formatServiceImplFileName("%sServiceImpl")
                .serviceTemplate("/templates/java/service.java") // 设置 ServiceImpl 模板
                .serviceImplTemplate("/templates/java/serviceImpl.java") // 设置 ServiceImpl 模板
                .enableFileOverride() // 启用覆盖已存在的文件

                .controllerBuilder()
                .formatFileName("%sController")
                .template("/templates/java/controller.java")
                .enableFileOverride() // 启用覆盖已存在的文件
                .enableRestStyle()
                .build();
    }

    // 注入配置
    private InjectionConfig generateCodeInjectionConfig(InjectionConfig.Builder builder, GenTableVO genTable) {
        Map<String, Object> customMap = new HashMap<>();
        customMap.put("tableName", genTable.getTableName());
        customMap.put("packageName", genTable.getPackageName());
        customMap.put("moduleName", genTable.getModuleName());
        customMap.put("businessName", genTable.getBusinessName());
        customMap.put("BusinessName", StrUtil.upperFirst(genTable.getBusinessName()));
        customMap.put("ClassName", genTable.getClassName());
        customMap.put("className", StrUtil.lowerFirst(genTable.getClassName()));
        customMap.put("class-name", StrUtil.toUnderlineCase(genTable.getClassName()).replace("_", "-").toLowerCase());
        customMap.put("functionName", genTable.getFunctionName());
        customMap.put("functionAuthor", genTable.getFunctionAuthor());
        customMap.put("parentMenuId", genTable.getParentMenuId());
        customMap.put("parentMenuName", genTable.getParentMenuName());
        customMap.put("columnList", genTable.getColumns());

        return builder.customMap(customMap)
                // 添加java文件
                .customFile(buildCustomFile(genTable, "VO.java", "templates/java/vo.java.ftl", "domain.vo"))
                .customFile(buildCustomFile(genTable, "AddParams.java", "templates/java/addParams.java.ftl", "domain.params"))
                .customFile(buildCustomFile(genTable, "UpdateParams.java", "templates/java/updateParams.java.ftl", "domain.params"))
                .customFile(buildCustomFile(genTable, "PageQueryParams.java", "templates/java/pageQueryParams.java.ftl", "domain.params"))
                .customFile(buildCustomFile(genTable, "SelectQueryParams.java", "templates/java/selectQueryParams.java.ftl", "domain.params"))
                .customFile(buildCustomFile(genTable, "Excel.java", "templates/java/excel.java.ftl", "domain.excel"))

                // sql 文件
                .customFile(buildCustomFile(genTable, "sql.ftl", "templates/sql/sql.ftl", "sql"))

                // Vue 文件
                .customFile(buildVueFile(genTable, "index.vue", "templates/vue/index.vue.ftl", outputDir))
                .customFile(buildVueFile(genTable, genTable.getBusinessName() + "-add-or-edit.vue", "templates/vue/add-or-edit.vue.ftl", outputDir))

                // TS 文件
                .customFile(buildTsFile(genTable, genTable.getBusinessName() + "Store.ts", "templates/ts/store.ts.ftl", outputDir))
                .customFile(buildTsFile(genTable, genTable.getBusinessName() + "AddOrEditStore.ts", "templates/ts/AddOrEditStore.ts.ftl", outputDir))
                .customFile(buildTsFile(genTable, genTable.getBusinessName() + ".ts", "templates/ts/api.ts.ftl", outputDir))
                .customFile(buildTsFile(genTable, genTable.getBusinessName() + "Types.ts", "templates/ts/types.ts.ftl", outputDir))

                .build();


    }

    // 构建自定义文件
    private CustomFile buildCustomFile(GenTableVO genTable, String fileName, String templatePath, String packageName) {
        return new CustomFile.Builder()
                .fileName(genTable.getClassName() + fileName)
                .formatNameFunction(tableInfo -> tableInfo.getEntityName().replace(genTable.getClassName() + "Entity", ""))
                .templatePath(templatePath)
                .enableFileOverride()
                .packageName(packageName)
                .build();
    }

    // 构建 Vue 文件
    private CustomFile buildVueFile(GenTableVO genTable, String fileName, String templatePath, String outputDir) {
        return new CustomFile.Builder()
                .fileName(fileName)
                .formatNameFunction(tableInfo -> tableInfo.getEntityName().replace(genTable.getClassName() + "Entity", ""))
                .templatePath(templatePath)
                .enableFileOverride()
                .filePath(outputDir + "\\views\\" + genTable.getModuleName() + "\\" + genTable.getBusinessName() +
                        (fileName.contains("components") ? "\\components" : ""))
                .build();
    }

    // 构建 TS 文件
    private CustomFile buildTsFile(GenTableVO genTable, String fileName, String templatePath, String outputDir) {
        String filePath = outputDir;
        if (fileName.endsWith("Store.ts") || fileName.endsWith("AddOrEditStore.ts")) {
            filePath += "\\store\\" + genTable.getModuleName() + "\\" + genTable.getBusinessName();
        } else if (fileName.endsWith(".ts") && !fileName.contains("Types")) {
            filePath += "\\api\\" + genTable.getModuleName();
        } else if (fileName.endsWith("Types.ts")) {
            filePath += "\\types\\" + genTable.getModuleName() + "\\" + genTable.getBusinessName();
        }
        return new CustomFile.Builder()
                .fileName(fileName)
                .formatNameFunction(tableInfo -> tableInfo.getEntityName().replace(genTable.getClassName() + "Entity", ""))
                .templatePath(templatePath)
                .enableFileOverride()
                .filePath(filePath)
                .build();
    }


    private DataSourceConfig generateCodeDataSourceConfig() {
        return new DataSourceConfig.Builder(dataBaseProperties.getUrl(), dataBaseProperties.getUsername(), dataBaseProperties.getPassword())
                .build();
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteGenTableByIds(Long[] tableIds) {

        iGenTableColumnService.remove(new QueryWrapper<GenTableColumn>().lambda().in(GenTableColumn::getTableId, Arrays.asList(tableIds)));
        for (Long tableId : tableIds) {
            this.baseMapper.deleteById(tableId);
        }
    }


    @Override
    public List<TreeNode> previewCode(Long tableId) {
        Map<String, String> previewResults = new LinkedHashMap<>();
        // 查询表信息
        GenTableVO genTable = this.getGenTableById(tableId);
        if (genTable == null) {
            throw new ZsException("表信息不存在，ID：" + tableId);
        }

        // 1. 构建配置 创建 ConfigBuilder
        ConfigBuilder configBuilder = new ConfigBuilder(
                generateCodePackageConfig(new PackageConfig.Builder(), genTable),
                generateCodeDataSourceConfig(),
                generateCodeStrategyConfig(new StrategyConfig.Builder(), genTable),
                null,
                generateCodeGlobalConfig(new GlobalConfig.Builder(), genTable),
                generateCodeInjectionConfig(new InjectionConfig.Builder(), genTable));

        // 获取表信息
        List<TableInfo> tableInfoList = configBuilder.getTableInfoList();
        if (tableInfoList.isEmpty()) {
            throw new ZsException("无法获取表结构信息");
        }
        TableInfo tableInfo = tableInfoList.get(0);

        // 2. 初始化 Freemarker 模板引擎
        FreemarkerTemplateEngine engine = new FreemarkerTemplateEngine();
        engine.init(configBuilder);


        // 3. 获取所有需要预览的模板路径和对应的输出文件名。获取模板到文件名的映射（如：controller.java.ftl -> UserController.java）
        Map<String, String> templateToFileMap = getTemplateToFileMap(genTable, tableInfo);

        // 检查模板映射是否为空
        if (templateToFileMap.isEmpty()) {
            log.warn("未找到任何模板文件用于预览");
            previewResults.put("警告", "未找到任何模板文件用于预览");
            return new ArrayList<>();
        }

        // 4. 遍历每个模板，渲染内容
        for (Map.Entry<String, String> entry : templateToFileMap.entrySet()) {
            String templatePath = entry.getKey();
            if (templatePath.startsWith("/")) {
                templatePath = templatePath.substring(1);
            }

            String fileName = entry.getValue();

            if (fileName.startsWith("/")) {
                fileName = fileName.substring(1);
            }

            try {
                // 获取数据模型（模板变量）
                Map<String, Object> objectMap = engine.getObjectMap(configBuilder, tableInfo);
                objectMap.put("tableName", genTable.getTableName());
                objectMap.put("packageName", genTable.getPackageName());
                objectMap.put("moduleName", genTable.getModuleName());
                objectMap.put("businessName", genTable.getBusinessName());
                objectMap.put("BusinessName", StrUtil.upperFirst(genTable.getBusinessName()));
                objectMap.put("ClassName", genTable.getClassName());
                objectMap.put("className", StrUtil.lowerFirst(genTable.getClassName()));
                objectMap.put("class-name", StrUtil.toUnderlineCase(genTable.getClassName()).replace("_", "-").toLowerCase());
                objectMap.put("functionName", genTable.getFunctionName());
                objectMap.put("functionAuthor", genTable.getFunctionAuthor());
                objectMap.put("parentMenuId", genTable.getParentMenuId());
                objectMap.put("parentMenuName", genTable.getParentMenuName());
                objectMap.put("columnList", genTable.getColumns());

                String str = getTemplateString(templatePath);

                String renderedContent = engine.writer(objectMap, templatePath, str);


                // 检查渲染结果是否为空
                if (renderedContent == null || renderedContent.trim().isEmpty()) {
                    log.warn("模板[{}]渲染结果为空", templatePath);
                    previewResults.put(fileName, "// 警告: 渲染结果为空");
                } else {
                    previewResults.put(fileName, renderedContent);
                }
            } catch (Exception e) {
                log.error("模板渲染失败: {}", templatePath, e);
                previewResults.put(fileName, "// 渲染错误: " + e.getMessage());
            }
        }

        // 构建树形结构
        List<TreeNode> root = TreeNodeUtils.buildAntdTree(previewResults, genTable.getBusinessName());

        // 将previewResults转换成树形结构，包含：java、vue、sql等
        return root;
    }


    @Override
    public GenTableVO getGenTableById(Long tableId) {
        GenTableVO genTableVO = BeanUtil.copyProperties(this.baseMapper.selectById(tableId), GenTableVO.class);

        if (genTableVO != null) {
            List<GenTableColumn> genTableColumns = iGenTableColumnService.list(new QueryWrapper<GenTableColumn>().lambda().eq(GenTableColumn::getTableId, tableId));
            genTableVO.setColumns(genTableColumns);
        }

        return genTableVO;
    }


    public String getTemplateString(String templateFileName) throws Exception {
        // 例如：templateFileName = "templates/entity.java.ftl"
        // 去掉开头的 '/'，避免 classpath 查找错误
        if (templateFileName.startsWith("/")) {
            templateFileName = templateFileName.substring(1);
        }

        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(templateFileName);

        if (inputStream == null) {
            throw new IllegalArgumentException("模板文件未找到: " + templateFileName);
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
    }


    private Map<String, String> getTemplateToFileMap(GenTableVO genTable, TableInfo tableInfo) {
        Map<String, String> map = new LinkedHashMap<>();

        String className = genTable.getClassName();
        String businessName = genTable.getBusinessName();
        String moduleName = genTable.getModuleName();

        // 后端 Java 文件
        map.put("/templates/java/entity.java.ftl", className + "Entity.java");
        map.put("/templates/java/mapper.java.ftl", className + "Mapper.java");
        map.put("/templates/java/mapper.xml.ftl", className + "Mapper.xml");
        map.put("/templates/java/service.java.ftl", className + "Service.java");
        map.put("/templates/java/serviceImpl.java.ftl", className + "ServiceImpl.java");
        map.put("/templates/java/controller.java.ftl", className + "Controller.java");
        map.put("/templates/java/vo.java.ftl", className + "VO.java");
        map.put("/templates/java/addParams.java.ftl", className + "AddParams.java");
        map.put("/templates/java/updateParams.java.ftl", className + "UpdateParams.java");
        map.put("/templates/java/pageQueryParams.java.ftl", className + "PageQueryParams.java");
        map.put("/templates/java/selectQueryParams.java.ftl", className + "SelectQueryParams.java");
        map.put("/templates/java/excel.java.ftl", className + "Excel.java");


        // sql
        map.put("/templates/sql/sql.ftl", className + ".sql");

        // 前端 Vue/TS 文件
        map.put("/templates/vue/index.vue.ftl", "index.vue");
        map.put("/templates/vue/add-or-edit.vue.ftl", businessName + "-add-or-edit.vue");
        map.put("/templates/ts/store.ts.ftl", businessName + "Store.ts");
        map.put("/templates/ts/AddOrEditStore.ts.ftl", businessName + "AddOrEditStore.ts");
        map.put("/templates/ts/api.ts.ftl", businessName + ".ts");
        map.put("/templates/ts/types.ts.ftl", businessName + "Types.ts");

        return map;
    }

}