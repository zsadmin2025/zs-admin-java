package config.dto;

import config.dto.sms.Aliyun;
import config.dto.sms.Tencent;
import lombok.Data;

@Data
public class SysConfigSmsDTO {

    private Integer type;

    private Aliyun aliyun;

    private Tencent tencent;
}
