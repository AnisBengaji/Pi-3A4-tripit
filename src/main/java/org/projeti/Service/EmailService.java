package org.projeti.Service;

import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.File;
import java.util.Properties;

public class EmailService {
    private final String username; // Email de l'expéditeur
    private final String password; // Mot de passe ou "App Password" (Gmail)

    public EmailService(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void sendEmail(String to, String subject, String body) throws MessagingException {
        // ⚙️ Configuration SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // 📧 Création de la session
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // 📌 Création du message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);

            // 🖼️ Contenu HTML avec image (cid:logo)
            String emailContent = "<html><body style='font-family: Arial, sans-serif; color: #333;'>"
                    + "<div style='max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px; background-color: #f9f9f9;'>"
                    + "<div style='text-align: center;'>"
                    + "<img src='cid:logo' alt='Trippin Logo' style='width: 150px; margin-bottom: 20px;'/>"
                    + "</div>"
                    + "<h2 style='color: #0078D7; text-align: center;'>Merci de voyager avec Trippin !</h2>"
                    + "<p style='font-size: 16px; text-align: justify;'>"
                    + "Cher(e) client(e),<br><br>"
                    + "Nous vous remercions pour votre confiance en notre agence de voyage <strong>Trippin</strong>. "
                    + "Nous nous engageons à vous offrir une expérience inoubliable et un service de qualité.<br><br>"
                    + body
                    + "<br><br>"
                    + "N'hésitez pas à nous contacter pour toute question ou assistance.<br><br>"
                    + "Cordialement,<br>"
                    + "<strong>L'équipe Trippin</strong><br>"
                    + "<span style='font-size: 14px; color: #666;'>Service client : contact@trippin.com | Téléphone : +33 1 23 45 67 89</span>"
                    + "</p>"
                    + "<hr style='border: 0; height: 1px; background-color: #ddd; margin: 20px 0;'/>"
                    + "<p style='font-size: 12px; text-align: center; color: gray;'>"
                    + "© 2025 Trippin. Tous droits réservés. | <a href='https://www.trippin.com' style='color: #0078D7; text-decoration: none;'>www.trippin.com</a>"
                    + "</p>"
                    + "</div>"
                    + "</body></html>";

            // 📜 Partie HTML
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(emailContent, "text/html; charset=utf-8");

            // 📷 Partie image (Logo)
            MimeBodyPart imagePart = new MimeBodyPart();
            String imagePath = "C:\\Users\\MSI\\Desktop\\PI\\PI-20250210T192842Z-001\\PI\\src\\main\\resources\\Views\\icons\\Logo.png";
            FileDataSource fds = new FileDataSource(imagePath);
            imagePart.setDataHandler(new DataHandler(fds));
            imagePart.setHeader("Content-ID", "<logo>");  // Correspond au `cid:logo`
            imagePart.setDisposition(MimeBodyPart.INLINE);

            // 📎 Création du message multipart
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart); // HTML
            multipart.addBodyPart(imagePart); // Logo

            // Attacher multipart au message
            message.setContent(multipart);

            // Envoi du message
            Transport.send(message);

        } catch (MessagingException e) {
            System.err.println("❌ Erreur lors de l'envoi de l'email : " + e.getMessage());
            throw new MessagingException("Erreur d'envoi: " + e.getMessage());
        }
    }





}
