package org.projeti.Controlleurs;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.concurrent.Task;
import org.projeti.Service.EmailService;
import java.util.Properties;
import java.io.InputStream;

public class EmailController {
    @FXML private TextField toField;
    @FXML private TextField subjectField;
    @FXML private TextField bodyField;
    @FXML private Button sendButton;
    @FXML private Label statusLabel;

    private EmailService emailService;

    @FXML
    public void initialize() {
        loadEmailConfig();
    }

    private void loadEmailConfig() {
        try (InputStream input = getClass().getResourceAsStream("/config.properties")) {
            Properties config = new Properties();
            config.load(input);
            emailService = new EmailService(
                    config.getProperty("email.user"),
                    config.getProperty("email.password")
            );
        } catch (Exception e) {
            statusLabel.setText("Configuration manquante !");
        }
    }
    // Dans EmailController.java
    public void setRecipient(String email) {
        // Validation basique + assignation
        if (email != null && !email.trim().isEmpty()) {
            toField.setText(email.trim());
        } else {
            toField.clear();
        }
    }
    @FXML
    private void handleSendEmail() {
        if (emailService == null) {
            statusLabel.setText("Service e-mail non configuré !");
            return;
        }

        statusLabel.setText("Envoi en cours...");

        Task<Void> sendTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                emailService.sendEmail(
                        toField.getText().trim(),
                        subjectField.getText().trim(),
                        bodyField.getText().trim()
                );
                return null;
            }
        };

        sendTask.setOnSucceeded(e ->
                statusLabel.setText("Succès ✅: E-mail envoyé !")
        );

        sendTask.setOnFailed(e -> {
            Throwable cause = sendTask.getException().getCause();
            statusLabel.setText("Échec ❌: " +
                    (cause != null ? cause.getMessage() : "Erreur inconnue"));
        });

        new Thread(sendTask).start();
    }
}