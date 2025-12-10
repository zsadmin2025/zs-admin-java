package com.zs.sms.strategy;

import com.zs.sms.domain.entity.SysSmsRecordEntity;
import com.zs.sms.domain.params.SysSmsParams;
import com.zs.sms.service.SysSmsTemplateService;

public interface SendSmsStrategy {

    SysSmsRecordEntity send(SysSmsTemplateService sysSmsTemplateService, SysSmsParams sysSmsParams);
}
