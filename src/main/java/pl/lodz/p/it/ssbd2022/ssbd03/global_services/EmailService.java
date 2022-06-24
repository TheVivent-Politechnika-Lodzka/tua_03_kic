package pl.lodz.p.it.ssbd2022.ssbd03.global_services;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Config;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.EmailNotSendException;

import java.util.Properties;

@Stateless
public class EmailService {

    private static final long serialVersionUID = 1L;

    Properties properties = System.getProperties();
    Session session;


    @PostConstruct
    public void init() {
        properties.put("mail.smtp.host", Config.MAIL_SMTP_HOST);
        properties.put("mail.smtp.port", Config.MAIL_SMTP_PORT);
        properties.put("mail.smtp.ssl.enable", Config.MAIL_SMTP_SSL_ENABLE);
        properties.put("mail.smtp.auth", Config.MAIL_SMTP_AUTH);

        session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Config.MAIL_LOGIN, Config.MAIL_PASSWORD);
            }
        });
        session.setDebug(true);
    }

    /**
     * Wysyła wiadomość email
     * @param to - adres odbiorcy
     * @param subject - temat wiadomości
     * @param content - treść wiadomości
     */
    public void sendEmail(String to, String subject, String content) {

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Config.MAIL_LOGIN));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            message.setSubject(subject);
            message.setText(content);
            Transport.send(message);
        } catch (MessagingException mex) {
            throw new EmailNotSendException();
        }
    }

}
