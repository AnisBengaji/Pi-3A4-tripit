package org.projeti.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.projeti.Service.UserService;

public class ResetPasswordController {

    @FXML
    private TextField codeField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private Button changePasswordButton; // Assurez-vous que ce bouton est bien lié dans le FXML

    private String userEmail;
    private String verificationCode;

    public void initData(String email, String code) {
        this.userEmail = email;
        this.verificationCode = code;
    }

    @FXML
    private void handleVerifyCode() {
        String enteredCode = codeField.getText();

        if (enteredCode.equals(verificationCode)) {
            showAlert("Succès", "Code vérifié. Vous pouvez maintenant entrer un nouveau mot de passe.");
            newPasswordField.setDisable(false);
            changePasswordButton.setDisable(false); // 🔹 Active le bouton après la vérification
        } else {
            showAlert("Erreur", "Code invalide.");
        }
    }

    @FXML
    private void handleChangePassword() {
        String newPassword = newPasswordField.getText();

        if (newPassword.isEmpty()) {
            showAlert("Erreur", "Veuillez entrer un nouveau mot de passe.");
            return;
        }

        UserService userService = new UserService();
        boolean updated = userService.updatePassword(userEmail, newPassword);

        if (updated) {
            showAlert("Succès", "Mot de passe mis à jour.");
        } else {
            showAlert("Erreur", "Échec de la mise à jour.");
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
