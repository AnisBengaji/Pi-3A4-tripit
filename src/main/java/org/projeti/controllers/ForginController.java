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
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

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
        final String senderPassword = "gmre fcoi mlkt zbvb";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com"); // Ajoute cette ligne pour éviter les erreurs SSL

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Code de validation");
            message.setText("Votre code de validation est : " + code + "\nIl expire dans 5 minutes.");

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
