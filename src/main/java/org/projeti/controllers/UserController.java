package org.projeti.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.projeti.Service.UserService;
import org.projeti.entites.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class UserController {
    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField numTelField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField mdpField;
    @FXML
    private TextField roleField;

    // Email validation regex pattern
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    @FXML
    void handleSaveUser(ActionEvent event) {
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String email = emailField.getText().trim();
        String mdp = mdpField.getText().trim();
        String role = roleField.getText().trim().toLowerCase();  // Normalize role input

        // Validate inputs
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || mdp.isEmpty() || role.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        // Validate email format
        if (!Pattern.matches(EMAIL_REGEX, email)) {
            showAlert("Erreur", "L'email n'est pas valide. Veuillez saisir un email correct (ex: exemple@email.com).");
            return;
        }

        // Validate role (must be "admin", "client", or "fournisseur")
        if (!role.equals("admin") && !role.equals("client") && !role.equals("fournisseur")) {
            showAlert("Erreur", "Le rôle doit être 'admin', 'client' ou 'fournisseur'.");
            return;
        }

        int numTel;
        try {
            numTel = Integer.parseInt(numTelField.getText().trim());
            if (String.valueOf(numTel).length() < 8) {  // Check if phone number has at least 8 digits
                showAlert("Erreur", "Le numéro de téléphone doit contenir au moins 8 chiffres.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le numéro de téléphone doit être un nombre valide.");
            return;
        }

        // Create and insert user
        User user = new User(nom, prenom, numTel, email, mdp, role);
        UserService us = new UserService();

        try {
            us.insert(user);
            showAlert("Succès", "Utilisateur ajouté avec succès.");

            // Load Detail.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailUser.fxml"));
            Parent root = loader.load();

            // Get the DetailController and refresh the TableView
            DetailControllerUser detailControlleruser = loader.getController();
            detailControlleruser.loadUsers();  // Ensure table is updated

            // Change scene
            Stage stage = (Stage) nomField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();  // Display the new scene

        } catch (SQLException e) {
            showAlert("Erreur SQL", "Erreur lors de l'ajout de l'utilisateur: " + e.getMessage());
        } catch (IOException e) {
            showAlert("Erreur FXML", "Impossible de charger Detail.fxml: " + e.getMessage());
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
