package com.zs.gen.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "gen")
@Data
public class GenConfig {

    /** 作者 */
    public  String author;

    /** 生成包路径 */
    public  String packageName;

    /** 生成基础路径 */
    public  String basePath;

    /** 是否包含主键ID */
    public  boolean autoRemovePre;

    /** 表前缀（生成类名不会包含表前缀） */
    public  String tablePrefix;

}
