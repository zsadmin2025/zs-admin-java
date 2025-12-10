package com.zs.mail.config;



import config.SysConfigApi;
import config.dto.SysConfigEmailDTO;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class MailConfig {

    @Resource
    private SysConfigApi sysConfigApi;

    @Bean
    @Primary
    public JavaMailSender javaMailSender() {
        SysConfigEmailDTO config = sysConfigApi.emailInfo();
        if (config == null) {
            throw new RuntimeException("邮件配置不能为空");
        }

        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(config.getHost());
        sender.setPort(config.getPort());
        sender.setUsername(config.getUsername());
        sender.setPassword(config.getPassword());

        Properties props = sender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true"); // 163 等常用邮箱需要 SSL
        props.put("mail.smtp.starttls.enable", "false");
        sender.setDefaultEncoding("UTF-8");

        return sender;
    }
}
