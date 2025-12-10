package com.zs;

import jakarta.annotation.Resource;
import mail.SysMailApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MainTest {

    @Resource
    private SysMailApi mailService;

    @Test
    public void testSendSimpleEmail() {
        mailService.sendSimpleEmail("zsadmintop@163.com","867406308@qq.com", "测试", "测试");
    }

    @Test
    public void testSendHtmlEmail() {
        mailService.sendHtmlEmail("zsadmintop@163.com","867406308@qq.com", "测试", "<h1>测试</h1>");
    }

    @Test
    public void testSendAttachmentsMail() {
        mailService.sendAttachmentsMail("zsadmintop@163.com","867406308@qq.com", "测试", "<h1>测试</h1>", "D:\\test.txt");
    }

    @Test
    public void testSendResourcesMail() {
        String rscId = "123";
        String rscPath = "D:\\4.jpg";
        String content = "<html><body>这是有图片的邮件：<img src=\'cid:" + rscId + "\'></body></html>";
        mailService.sendResourcesMail("zsadmintop@163.com","867406308@qq.com", "测试", content, rscPath, rscId);
    }
}
