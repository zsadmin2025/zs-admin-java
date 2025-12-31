package com.zs.gen.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "gen")
@Data
public class GenConfigProperties {

    /** 作者 */
    public  String author;

    /** 生成包路径 */
    public  String packageName;

    /** 默认模块名称 */
    public  String moduleName;

    /** 生成基础路径 */
    public  String basePath;

    /** 是否包含主键ID */
    public  boolean autoRemovePre;

    /** 是否移除表前缀（默认是false）**/
    public  String tablePrefix;

}
