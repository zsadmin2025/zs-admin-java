package com.zs.sms.strategy;

import cn.hutool.json.JSONUtil;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.*;
import com.aliyun.teaopenapi.models.Config;
import com.zs.sms.domain.entity.SysSmsRecordEntity;
import com.zs.sms.domain.params.SysSmsParams;
import com.zs.sms.domain.vo.SysSmsTemplateVO;
import com.zs.sms.service.SysSmsTemplateService;
import com.zs.sys.config.domain.vo.SysConfigSmsVO;
import config.dto.sms.Aliyun;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
public class AliyunSendSmsStrategy implements SendSmsStrategy {

    private final Client client;


    public AliyunSendSmsStrategy(SysConfigSmsVO sysConfigSmsVO) {
        if (sysConfigSmsVO == null || sysConfigSmsVO.getAliyun() == null) {
            throw new IllegalArgumentException("阿里云配置不能为空");
        }
        Aliyun aliyun = sysConfigSmsVO.getAliyun();
        if (aliyun.getAccessKeyId() == null || aliyun.getAccessKeySecret() == null || aliyun.getEndpoint() == null) {
            throw new IllegalArgumentException("阿里云配置信息不完整");
        }
        Config config = new Config();
        config.accessKeyId = aliyun.getAccessKeyId();
        config.accessKeySecret = aliyun.getAccessKeySecret();
        config.endpoint = aliyun.getEndpoint();

        try {
            this.client = new Client(config);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public SysSmsRecordEntity send(SysSmsTemplateService sysSmsTemplateService, SysSmsParams sysSmsParams) {
        try {
            // 通过模板编号获取模板相关内容

            SysSmsTemplateVO sysSmsTemplateVO = sysSmsTemplateService.getByTemplateNumber(sysSmsParams.getTemplateNumber());

            if (sysSmsParams.getPhoneNumbers().size() == 1) {
                // 单独发送短信
                // 构造API请求对象，请替换请求参数值
                SendSmsRequest sendSmsRequest = new SendSmsRequest()
                        .setPhoneNumbers(sysSmsParams.getPhoneNumbers().get(0))
                        .setSignName(sysSmsTemplateVO.getSignName())
                        .setTemplateCode(sysSmsTemplateVO.getTemplateCode())
                        .setTemplateParam(sysSmsParams.getTemplateParam()); // TemplateParam为序列化后的JSON字符串。
                // 获取响应对象
                SendSmsResponse sendSmsResponse = this.client.sendSms(sendSmsRequest);
                // 响应包含服务端响应的 body 和 headers
                log.info("发送短信成功{}", JSONUtil.toJsonStr(sendSmsResponse.getBody()));

                if (!sendSmsResponse.getBody().code.equals("OK")) {
                    log.error("发送短信失败{}", JSONUtil.toJsonStr(sendSmsResponse.getBody()));
                    throw new RuntimeException("发送短信失败");
                }

                return getSysSmsRecordEntity(sysSmsParams, sendSmsResponse.getBody().requestId, sendSmsResponse.getBody().bizId, sysSmsTemplateVO);

            } else if (sysSmsParams.getPhoneNumbers().size() > 1) {
                // 批量发送
                List<String> signNames = Collections.nCopies(sysSmsParams.getPhoneNumbers().size(), sysSmsTemplateVO.getSignName());
                List<String> templateParams = Collections.nCopies(sysSmsParams.getPhoneNumbers().size(), sysSmsParams.getTemplateParam());

                // 构造API请求对象，请替换请求参数值
                SendBatchSmsRequest sendSmsRequest = new SendBatchSmsRequest()
                        .setPhoneNumberJson(JSONUtil.toJsonStr(sysSmsParams.getPhoneNumbers()))
                        .setSignNameJson(JSONUtil.toJsonStr(signNames))
                        .setTemplateCode(sysSmsTemplateVO.getTemplateCode())
                        .setTemplateParamJson(JSONUtil.toJsonStr(templateParams)); // TemplateParam为序列化后的JSON字符串。
                // 获取响应对象
                SendBatchSmsResponse sendBatchSmsResponse = this.client.sendBatchSms(sendSmsRequest);
                // 响应包含服务端响应的 body 和 headers
                SendBatchSmsResponseBody body = sendBatchSmsResponse.getBody();
                log.info("批量发送短信成功{}", JSONUtil.toJsonStr(body));

                if (!body.code.equals("OK")) {
                    log.error("批量发送短信失败{}", JSONUtil.toJsonStr(body));
                    throw new RuntimeException("批量发送短信失败");
                }

                return getSysSmsRecordEntity(sysSmsParams, body.requestId, body.bizId, sysSmsTemplateVO);
            }

            return null;
        } catch (Exception e) {
            log.error("发送短信失败", e);
        }
        return new SysSmsRecordEntity();

    }

    private static SysSmsRecordEntity getSysSmsRecordEntity(SysSmsParams sysSmsParams, String requestId, String bizId, SysSmsTemplateVO sysSmsTemplateVO) {
        SysSmsRecordEntity sysSmsRecordEntity = new SysSmsRecordEntity();
        sysSmsRecordEntity.setRequestId(requestId);
        sysSmsRecordEntity.setBizId(bizId);
        sysSmsRecordEntity.setPhoneNumbers(String.join(",", sysSmsParams.getPhoneNumbers()));
        sysSmsRecordEntity.setContent(sysSmsTemplateVO.getTemplateContent());
        sysSmsRecordEntity.setTemplateCode(sysSmsTemplateVO.getTemplateCode());
        sysSmsRecordEntity.setTemplateParams(sysSmsParams.getTemplateParam());
        sysSmsRecordEntity.setChannel(1L);
        sysSmsRecordEntity.setStatus(1L);
        sysSmsRecordEntity.setSendTime(new Date());
        return sysSmsRecordEntity;
    }
}
