package mail;

public interface SysMailApi {

    void sendSimpleEmail(String from, String to, String subject, String content);

    void sendHtmlEmail(String from, String to, String subject, String content);

    void sendAttachmentsMail(String from, String to, String subject, String content, String filePath);

    void sendResourcesMail(String from, String to, String subject, String content, String rscPath, String rscId);
}
