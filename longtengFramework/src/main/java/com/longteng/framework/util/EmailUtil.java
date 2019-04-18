package com.longteng.framework.util;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.longteng.framework.config.Config;

public class EmailUtil {

    private static String host = "";
    private static String username = "";
    private static String password = "";
    private static String nickname = "";

    private static Properties properties = new Properties();

    static {
        host = Config.get("mail.host");
        username = Config.get("mail.username");
        password = Config.get("mail.password");
        nickname = Config.get("mail.nickname");
        properties.setProperty("mail.smtp.auth", "true");
    }

    private static Session getSession() {

        Session session = Session.getInstance(properties);
        session.setDebug(false);

        return session;
    }

    /**
     * 发送邮件
     * 
     * @param subject
     *            邮件主题
     * @param body
     *            邮件内容
     * @param sendTo
     *            收件人地址
     */
    public static int doSendHtmlEmail(String subject, String body, String sendTo) {

        Session session = getSession();

        Transport transport = null;

        try {

            transport = session.getTransport("smtp");

            MimeMessage message = new MimeMessage(session);

            // 发件人
            InternetAddress from = new InternetAddress(MimeUtility.encodeWord(nickname) + " <" + username + ">");
            message.setFrom(from);

            // 收件人
            InternetAddress to = new InternetAddress(sendTo);
            message.setRecipient(Message.RecipientType.TO, to);

            // 邮件主题
            message.setSubject(subject);

            String content = body.toString();
            // 邮件内容,也可以使纯文本"text/plain"
            message.setContent(content, "text/html;charset=UTF-8");

            // 保存邮件
            message.saveChanges();

            // smtp验证
            transport.connect(host, username, password);

            // 发送
            transport.sendMessage(message, message.getAllRecipients());
        }
        catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        finally {

            if (transport != null) {
                try {
                    transport.close();
                }
                catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }

        return 0;
    }

    /**
     * 发送带附件邮件
     * 
     * @param subject
     *            邮件主题
     * @param body
     *            邮件内容
     * @param sendTo
     *            收件人地址
     * @param affix
     *            附件
     */
    public static int doSendAttachmentEmail(String subject, String body, String sendTo, String affix) {

        Session session = getSession();

        Transport transport = null;

        try {

            transport = session.getTransport("smtp");

            MimeMessage message = new MimeMessage(session);
            // 加载发件人地址
            InternetAddress from = new InternetAddress(MimeUtility.encodeWord("系统邮件") + " <" + username + ">");
            message.setFrom(from);

            // 加载收件人地址
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(sendTo));
            // 加载标题
            message.setSubject(subject);

            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();

            // 设置邮件的文本内容
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setText(body.toString());
            multipart.addBodyPart(contentPart);

            // 添加附件
            BodyPart messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(affix);

            // 添加附件的内容
            messageBodyPart.setDataHandler(new DataHandler(source));
            // 添加附件的标题
            messageBodyPart.setFileName(MimeUtility.encodeWord(source.getName()));
            multipart.addBodyPart(messageBodyPart);

            // 将multipart对象放到message中
            message.setContent(multipart);
            // 保存邮件
            message.saveChanges();

            // smtp验证
            transport.connect(host, username, password);

            // 发送
            transport.sendMessage(message, message.getAllRecipients());
        }
        catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        finally {
            if (transport != null) {
                try {
                    transport.close();
                }
                catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }

        return 0;
    }
}
