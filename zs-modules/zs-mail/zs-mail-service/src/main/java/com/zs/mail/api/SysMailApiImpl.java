package com.zs.mail.api;

import com.zs.mail.service.MailService;
import jakarta.annotation.Resource;
import mail.SysMailApi;
import org.springframework.stereotype.Service;

@Service
public class SysMailApiImpl implements SysMailApi {

    @Resource
    private MailService mailService;

    @Override
    public void sendSimpleEmail(String from, String to, String subject, String content) {
        mailService.sendSimpleEmail(from, to, subject, content);
    }

    @Override
    public void sendHtmlEmail(String from, String to, String subject, String content) {
        mailService.sendHtmlEmail(from, to, subject, content);
    }

    @Override
    public void sendAttachmentsMail(String from, String to, String subject, String content, String filePath) {
        mailService.sendAttachmentsMail(from, to, subject, content, filePath);
    }

    @Override
    public void sendResourcesMail(String from, String to, String subject, String content, String rscPath, String rscId) {
        mailService.sendResourcesMail(from, to, subject, content, rscPath, rscId);
    }
}
