package org.projeti.controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.projeti.Service.UserService;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;
import java.io.File;
public class ForginController {

    @FXML
    private TextField emailField;

    private String generatedCode;

    @FXML
    private void handleResetPassword(ActionEvent event) {
        String email = emailField.getText();

        if (email.isEmpty()) {
            showAlert("Erreur", "Veuillez entrer votre adresse email.");
            return;
        }

        UserService userService = new UserService();
        if (!userService.emailExists(email)) {
            showAlert("Erreur", "Cet email n'est pas enregistré.");
            return;
        }

        generatedCode = generateVerificationCode();
        boolean emailSent = sendVerificationEmail(email, generatedCode);

        if (emailSent) {
            showAlert("Succès", "Un code de vérification a été envoyé à : " + email);
            navigateToResetPassword(email);
        } else {
            showAlert("Erreur", "Échec de l'envoi du code. Vérifiez votre connexion.");
        }
    }

    @FXML
    private void handleBackToSignIn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Signin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page de connexion.");
        }
    }

    private boolean sendVerificationEmail(String recipientEmail, String code) {
        final String senderEmail = "linatekaya00@gmail.com";
        final String senderPassword = "gmre fcoi mlktzbvb";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Reset Your Password");

            // **Styled Email Template (Fix Applied)**
            String emailContent = "<html>" +
                    "<head><style>" +
                    "body { margin: 0; padding: 0; font-family: Arial, sans-serif; text-align: center; }" +
                    ".email-container { background: #007A8C; padding: 40px 0;border-radius: 10px; width: 100%; }" +  // Background color applied here
                    ".content { background: #ffffff; padding: 20px; border-radius: 10px; width: 80%; max-width: 400px; margin: auto; }" +
                    ".logo img { max-width: 150px; }" +
                    "h2 { color: #FF742C; }" +
                    ".verification-code { font-size: 24px; font-weight: bold; background: #ffffff; padding: 15px 20px; border-radius: 8px; display: inline-block; border: 2px solid #FF742C; color: #333; }" +
                    ".footer { font-size: 12px; color: #fff; margin-top: 20px; }" + // Footer text is white
                    "</style></head>" +
                    "<body>" +
                    "<div class='email-container'>" + // This wraps everything in the blue background
                    "<div class='content'>" + // White box for content
                    "<div class='logo'><img src='cid:appLogo' alt='App Logo'></div>" +
                    "<h2>Password Reset Request</h2>" +
                    "<p>Hello,</p>" +
                    "<p>You have requested to reset your password. Use the verification code below to proceed:</p>" +
                    "<p class='verification-code'>" + code + "</p>" +
                    "<p>This code is valid for 5 minutes.</p>" +
                    "<p>If you did not request a password reset, please ignore this email.</p>" +
                    "</div>" + // Close .content (white container)
                    "<div class='footer'>© 2025 Your App Name. All rights reserved.</div>" + // Footer outside the white box
                    "</div>" + // Close .email-container
                    "</body></html>";

            MimeMultipart multipart = new MimeMultipart("related");

            // **HTML Part**
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(emailContent, "text/html; charset=utf-8");
            multipart.addBodyPart(htmlPart);

            // **Attach Logo**
            String logoPath = "src/main/resources/images/logo.png"; // Adjust the path if needed
            File logoFile = new File(logoPath);

            if (logoFile.exists()) {
                MimeBodyPart imagePart = new MimeBodyPart();
                DataSource fds = new FileDataSource(logoFile);
                imagePart.setDataHandler(new DataHandler(fds));
                imagePart.setHeader("Content-ID", "<appLogo>"); // Matches the `cid:appLogo` in HTML
                multipart.addBodyPart(imagePart);
            } else {
                System.err.println("Warning: Logo file not found. Email sent without an image.");
            }

            message.setContent(multipart);
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }




    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    private void navigateToResetPassword(String email) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ResetPassword.fxml"));
            Parent root = loader.load();

            ResetPasswordController controller = loader.getController();
            controller.initData(email, generatedCode);

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page de réinitialisation.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
