package org.projeti.controllers;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.projeti.Service.UserService;

import org.projeti.entites.User;
import org.projeti.controllers.DetailControllerUser;
import java.sql.SQLException;

public class UpdateUser {
    @FXML
    private TextField nomField, prenomField, numTelField, emailField;
    @FXML
    private CheckBox adminCheckBox, clientCheckBox, fournisseurCheckBox;

    private User user;
    private DetailControllerUser detailControllerUser;
    private final UserService userService = new UserService();

    // Email validation regex pattern
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    public void setUserData(User user, DetailControllerUser detailControllerUser) {
        this.user = user;
        this.detailControllerUser = detailControllerUser;
        nomField.setText(user.getNom());
        prenomField.setText(user.getPrenom());
        numTelField.setText(String.valueOf(user.getNum_tel()));
        emailField.setText(user.getEmail());

        // Set role checkboxes based on the user role
        if (user.getRole().equals("admin")) {
            adminCheckBox.setSelected(true);
        } else if (user.getRole().equals("client")) {
            clientCheckBox.setSelected(true);
        } else if (user.getRole().equals("fournisseur")) {
            fournisseurCheckBox.setSelected(true);
        }
    }

    @FXML
    public void initialize() {
        // Ensure only one checkbox is selected at a time
        adminCheckBox.setOnAction(event -> {
            if (adminCheckBox.isSelected()) {
                fournisseurCheckBox.setSelected(false);
                clientCheckBox.setSelected(false);
            }
        });
        clientCheckBox.setOnAction(event -> {
            if (clientCheckBox.isSelected()) {
                fournisseurCheckBox.setSelected(false);
                adminCheckBox.setSelected(false);
            }
        });

        fournisseurCheckBox.setOnAction(event -> {
            if (fournisseurCheckBox.isSelected()) {
                clientCheckBox.setSelected(false);
                adminCheckBox.setSelected(false);
            }
        });
    }
    @FXML
    private void handleUpdateUser() {
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String email = emailField.getText().trim();

        // Validate inputs
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        // Validate email format
        if (!email.matches(EMAIL_REGEX)) {
            showAlert("Erreur", "L'email n'est pas valide. Veuillez saisir un email correct (ex: exemple@email.com).");
            return;
        }

        // Check role selection
        String role = "";
        if (adminCheckBox.isSelected()) {
            role = "admin";
        } else if (clientCheckBox.isSelected()) {
            role = "client";
        } else if (fournisseurCheckBox.isSelected()) {
            role = "fournisseur";
        }

        if (role.isEmpty()) {
            showAlert("Erreur", "Veuillez sélectionner un rôle.");
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

        // Update user data
        user.setNom(nom);
        user.setPrenom(prenom);
        user.setNum_tel(numTel);
        user.setEmail(email);
        user.setRole(role);

        try {
            userService.update(user);
            detailControllerUser.refreshTable();
            showAlert("Succès", "Utilisateur mis à jour avec succès.");
            ((Stage) nomField.getScene().getWindow()).close();
        } catch (SQLException e) {
            showAlert("Erreur SQL", "Erreur lors de la mise à jour de l'utilisateur: " + e.getMessage());
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

