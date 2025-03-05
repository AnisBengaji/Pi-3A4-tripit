package org.projeti.Service;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailPubService {

    private static final String SMTP_HOST = "smtp.gmail.com"; // E.g., Gmail, Yahoo, etc.
    private static final String SMTP_PORT = "587";
    private static final String SENDER_EMAIL = "bengaji.anis@gmail.com";
    private static final String APP_PASSWORD = "Bengajianis2004";  // Use an App password if 2FA is enabled

    public void sendEmail(String recipientEmail, String subject, String body) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, APP_PASSWORD);  // Use the App Password here
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Error sending email: " + e.getMessage());
        }
    }
}
