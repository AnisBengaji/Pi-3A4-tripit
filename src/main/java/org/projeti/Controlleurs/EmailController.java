
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

        // Vérification des champs obligatoires avant d'envoyer l'e-mail
        if (toField.getText().trim().isEmpty() || subjectField.getText().trim().isEmpty() || bodyField.getText().trim().isEmpty()) {
            statusLabel.setText("Erreur : Tous les champs doivent être remplis !");
            return;
        }

        statusLabel.setText("Envoi en cours...");

        Task<Void> sendTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    // Envoi de l'e-mail avec les données
                    emailService.sendEmail(
                            toField.getText().trim(),
                            subjectField.getText().trim(),
                            bodyField.getText().trim()
                    );
                } catch (Exception e) {
                    updateMessage("Erreur lors de l'envoi : " + e.getMessage());
                    throw e; // Relancer l'exception pour gérer l'échec
                }
                return null;
            }
        };

        sendTask.setOnSucceeded(e -> {
            statusLabel.setText("Succès ✅: E-mail envoyé !");
        });

        sendTask.setOnFailed(e -> {
            Throwable cause = sendTask.getException().getCause();
            statusLabel.setText("Échec ❌: " +
                    (cause != null ? cause.getMessage() : "Erreur inconnue"));
        });

        // Lancer la tâche dans un thread séparé
        new Thread(sendTask).start();
    }

}
