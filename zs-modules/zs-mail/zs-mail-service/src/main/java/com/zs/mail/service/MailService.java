package com.zs.mail.service;

public interface MailService {


    /**
     * 发送简单邮件
     *
     * @param from 发送者
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    void sendSimpleEmail(String from, String to, String subject, String content);


    /**
     * 发送HTML邮件
     *
     * @param from     发送者
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    void sendHtmlEmail(String from, String to, String subject, String content);

    /**
     * 批量发送HTML邮件
     *
     * @param from 发送者
     * @param to 收件人列表
     * @param cc 抄送人列表
     * @param bcc 密送人列表
     * @param subject 主题
     * @param content  内容
     */
    void sendHtmlEmail(String from, String[] to,String[] cc, String[] bcc, String subject, String content);


    /**
     * 带多个附件的邮件
     *
     * @param from     发送者
     * @param to       收件人
     * @param subject  主题
     * @param content  内容
     * @param filePaths 附件路径
     */
    void sendHtmlEmailOrFiles(String from, String to, String subject, String content, String[] filePaths);

    /**
     * 发送带附件的邮件
     *
     * @param from     发送者
     * @param to       收件人
     * @param subject  主题
     * @param content  内容
     * @param filePath 附件
     */
    void sendAttachmentsMail(String from, String to, String subject, String content, String filePath);

    /**
     * 带多个附件的邮件
     *
     * @param from     发送者
     * @param to       收件人
     * @param subject  主题
     * @param content  内容
     * @param filePath 附件
     */
    void sendAttachmentsMail(String from, String to, String subject, String content, String[] filePath);

    /**
     * 带静态资源的邮件
     *
     * @param from     发送者
     * @param to       收件人
     * @param subject  主题
     * @param content  内容
     * @param rscPath  静态资源路径
     * @param rscId    静态资源id
     */
    void sendResourcesMail(String from, String to, String subject, String content, String rscPath, String rscId);

}
