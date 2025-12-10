package com.zs.mail.service.impl;

import com.zs.mail.service.MailService;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;

@Slf4j
@Service
public class MailServiceImpl implements MailService {


    @Resource
    JavaMailSender javaMailSender;

    @Override
    public void sendSimpleEmail(String from, String to, String subject, String content) {
        try {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);

            javaMailSender.send(message);

            log.info("发送邮件成功");

        }catch (Exception e) {
            log.error("发送邮件失败", e);
        }
    }

    @Override
    public void sendHtmlEmail(String from, String to, String subject, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);


            javaMailSender.send(message);
            log.info("html邮件发送成功");
        } catch (Exception e) {
            log.error("html邮件发送失败", e);
        }
    }

    @Override
    public void sendHtmlEmail(String from, String[] to, String[] cc, String[] bcc, String subject, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.setSentDate(new Date());

            // 设置抄送 (CC)
            if (cc != null && cc.length > 0) {
                helper.setCc(cc);
            }

            // 设置密送 (BCC)
            if (bcc != null && bcc.length > 0) {
                helper.setBcc(bcc);
            }

            javaMailSender.send(message);
            log.info("批量html邮件发送成功");
        } catch (Exception e) {
            log.error("批量html邮件发送失败", e);
        }
    }

    @Override
    public void sendHtmlEmailOrFiles(String from, String to, String subject, String content, String[] filePaths) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            for (String path : filePaths) {

                FileSystemResource file = new FileSystemResource(new File(path));
                helper.addAttachment(file.getFilename(), file);

            }

            javaMailSender.send(message);
            log.info("html和多文件邮件发送成功");
        } catch (Exception e) {
            log.error("html和多文件邮件发送失败", e);
        }
    }

    @Override
    public void sendAttachmentsMail(String from, String to, String subject, String content, String filePath) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = file.getFilename();
            helper.addAttachment(fileName, file);

            javaMailSender.send(message);
            log.info("带附件的邮件发送成功");
        } catch (Exception e) {
            log.error("带附件的邮件发送失败", e);
        }
    }

    @Override
    public void sendAttachmentsMail(String from, String to, String subject, String content, String[] filePath) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            for (String path : filePath) {

                FileSystemResource file = new FileSystemResource(new File(path));
                helper.addAttachment(file.getFilename(), file);

            }
            javaMailSender.send(message);

            log.info("发送多附件邮件成功");
        }catch (MessagingException e) {
            log.error("发送多附件邮件失败", e);
        }
    }

    @Override
    public void sendResourcesMail(String from, String to, String subject, String content, String rscPath, String rscId) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            FileSystemResource res = new FileSystemResource(new File(rscPath));
            helper.addInline(rscId, res);

            javaMailSender.send(message);
            log.info("发送静态资源邮件成功");
        } catch (Exception e) {
            log.error("发送静态资源邮件失败", e);
        }
    }
}
