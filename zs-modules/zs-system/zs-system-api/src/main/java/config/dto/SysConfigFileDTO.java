package config.dto;

import config.dto.file.Aliyun;
import config.dto.file.Huawei;
import config.dto.file.Local;
import config.dto.file.Tencent;
import lombok.Data;

@Data
public class SysConfigFileDTO {

    private Integer type;
    private Local local;
    private Aliyun aliyun;
    private Tencent tencent;
    private Huawei huawei;
}
