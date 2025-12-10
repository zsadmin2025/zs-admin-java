package config.dto.file;

import lombok.Data;

/**
 * 本地上传
 */
@Data
public class Local {
    private String domain;
    private String prefix;
    private String path;
}
