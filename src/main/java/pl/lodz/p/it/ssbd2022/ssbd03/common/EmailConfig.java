package pl.lodz.p.it.ssbd2022.ssbd03.common;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailConfig {
    static String from = "szurySSBD@gmail.com";
    static String host = "smtp.gmail.com";
    static Properties properties = System.getProperties();

    public static void sendEmail(String to) {
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, "P@ssword1!");
            }
        });

        session.setDebug(true);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            message.setSubject("Tytul");
            message.setText("Wiadomo, albo niewiadomo");
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

}
