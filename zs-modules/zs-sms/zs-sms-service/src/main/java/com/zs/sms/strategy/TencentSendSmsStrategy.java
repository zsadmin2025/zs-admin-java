package com.zs.sms.strategy;

import com.zs.sms.domain.entity.SysSmsRecordEntity;
import com.zs.sms.domain.params.SysSmsParams;
import com.zs.sms.service.SysSmsTemplateService;
import com.zs.sys.config.domain.vo.SysConfigSmsVO;

public class TencentSendSmsStrategy implements  SendSmsStrategy{


    public TencentSendSmsStrategy(SysConfigSmsVO sysConfigSmsVO) {
    }


    @Override
    public SysSmsRecordEntity send(SysSmsTemplateService sysSmsTemplateService, SysSmsParams sysSmsParams) {

        return null;
    }
}
