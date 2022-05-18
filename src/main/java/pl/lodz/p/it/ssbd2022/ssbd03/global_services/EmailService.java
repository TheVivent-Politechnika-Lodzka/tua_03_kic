package pl.lodz.p.it.ssbd2022.ssbd03.global_services;

import jakarta.ejb.Stateless;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Config;

import java.util.Properties;

@Stateless
public class EmailService {
    Properties properties = System.getProperties();

    // TODO: Dodanie Javadoc
    // TODO: o ile to możliwe, wydzielenie z metody konfiguracji i połączenia do skrzynki do metody postConstruct
    public void sendEmail(String to, String subject, String content) {
        properties.put("mail.smtp.host", Config.MAIL_SMTP_HOST);
        properties.put("mail.smtp.port", Config.MAIL_SMTP_PORT);
        properties.put("mail.smtp.ssl.enable", Config.MAIL_SMTP_SSL_ENABLE);
        properties.put("mail.smtp.auth", Config.MAIL_SMTP_AUTH);

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Config.MAIL_LOGIN, Config.MAIL_PASSWORD);
            }
        });

        session.setDebug(true);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Config.MAIL_LOGIN));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            message.setSubject(subject);
            message.setText(content);
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

}