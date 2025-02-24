package org.projeti.controllers;



import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.projeti.Service.UserService;
import javafx.event.ActionEvent;
import java.io.IOException;

public class ResetPasswordController {

    @FXML
    private TextField codeField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private Button changePasswordButton;

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
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Code vérifié. Vous pouvez maintenant entrer un nouveau mot de passe.");
            newPasswordField.setDisable(false);
            changePasswordButton.setDisable(false);
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Code invalide.");
        }
    }

    @FXML
    private void handleChangePassword(ActionEvent event) {
        String newPassword = newPasswordField.getText();

        if (newPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un nouveau mot de passe.");
            return;
        }

        UserService userService = new UserService();
        boolean updated = userService.updatePassword(userEmail, newPassword);

        if (updated) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Mot de passe mis à jour avec succès!");

            // 🔹 Redirect to the sign-in page
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/signIn.fxml"));
                Parent signInPage = loader.load();
                Scene signInScene = new Scene(signInPage);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(signInScene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la mise à jour du mot de passe.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

/*
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
}*/
