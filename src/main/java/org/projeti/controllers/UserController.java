package org.projeti.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
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
    private CheckBox clientCheckBox;  // Checkbox for Client
    @FXML
    private CheckBox fournisseurCheckBox;  // Checkbox for Fournisseur
    @FXML
    private TextField captchaField;  // CAPTCHA input field
    @FXML
    private Label captchaLabel;  // Label for CAPTCHA

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private String generatedCaptcha;

    @FXML
    public void initialize() {
        // Ensure only one checkbox is selected at a time
        clientCheckBox.setOnAction(event -> {
            if (clientCheckBox.isSelected()) {
                fournisseurCheckBox.setSelected(false);
            }
        });

        fournisseurCheckBox.setOnAction(event -> {
            if (fournisseurCheckBox.isSelected()) {
                clientCheckBox.setSelected(false);
            }
        });

        // Generate a simple CAPTCHA when the page loads
        generateCaptcha();
    }

    // Method to generate a simple CAPTCHA
    private void generateCaptcha() {
        int number = (int) (Math.random() * 10000);  // Generates a random 4-digit number
        generatedCaptcha = String.valueOf(number);
        captchaLabel.setText("Enter this CAPTCHA: " + generatedCaptcha);
    }

    @FXML
    private void handleBackToLogIn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Signin.fxml"));  // Retour à la connexion
            Parent root = loader.load();
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the sign-in page.");
        }
    }

    @FXML
    void handleSaveUser(ActionEvent event) {
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String email = emailField.getText().trim();
        String mdp = mdpField.getText().trim();
        String captchaInput = captchaField.getText().trim();

        // Validate CAPTCHA
        if (!captchaInput.equals(generatedCaptcha)) {
            showAlert("Erreur", "Le CAPTCHA est incorrect. Veuillez essayer à nouveau.");
            generateCaptcha();  // Regenerate CAPTCHA on failure
            return;
        }

        // Validate inputs
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || mdp.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        // Validate email format
        if (!Pattern.matches(EMAIL_REGEX, email)) {
            showAlert("Erreur", "L'email n'est pas valide. Veuillez saisir un email correct (ex: exemple@email.com).");
            return;
        }

        // Validate role (exactly one role should be selected)
        String role = "";
        if (clientCheckBox.isSelected()) {
            role = "client";
        } else if (fournisseurCheckBox.isSelected()) {
            role = "fournisseur";
        }

        if (role.isEmpty()) {
            showAlert("Erreur", "Le rôle doit être 'client' ou 'fournisseur'.");
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

            // Load signIn.fxml after saving user
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/signIn.fxml"));
            Parent root = loader.load();

            // Change scene
            Stage stage = (Stage) nomField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();  // Display the new scene

        } catch (SQLException e) {
            showAlert("Erreur SQL", "Erreur lors de l'ajout de l'utilisateur: " + e.getMessage());
        } catch (IOException e) {
            showAlert("Erreur FXML", "Impossible de charger SignIn.fxml: " + e.getMessage());
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

/*
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
    private CheckBox clientCheckBox;  // Checkbox for Client
    @FXML
    private CheckBox fournisseurCheckBox;  // Checkbox for Fournisseur

    // Email validation regex pattern
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    @FXML
    public void initialize() {
        // Ensure only one checkbox is selected at a time
        clientCheckBox.setOnAction(event -> {
            if (clientCheckBox.isSelected()) {
                fournisseurCheckBox.setSelected(false);
            }
        });

        fournisseurCheckBox.setOnAction(event -> {
            if (fournisseurCheckBox.isSelected()) {
                clientCheckBox.setSelected(false);
            }
        });
    }

    @FXML
    private void handleBackToLogIn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Signin.fxml"));  // Retour à la connexion
            Parent root = loader.load();
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the sign-in page.");
        }
    }

    @FXML
    void handleSaveUser(ActionEvent event) {
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String email = emailField.getText().trim();
        String mdp = mdpField.getText().trim();

        // Validate inputs
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || mdp.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        // Validate email format
        if (!Pattern.matches(EMAIL_REGEX, email)) {
            showAlert("Erreur", "L'email n'est pas valide. Veuillez saisir un email correct (ex: exemple@email.com).");
            return;
        }

        // Validate role (exactly one role should be selected)
        String role = "";
        if (clientCheckBox.isSelected()) {
            role = "client";
        } else if (fournisseurCheckBox.isSelected()) {
            role = "fournisseur";
        }

        if (role.isEmpty()) {
            showAlert("Erreur", "Le rôle doit être 'client' ou 'fournisseur'.");
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/signIn.fxml"));
            Parent root = loader.load();

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
*/





/*
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
    private CheckBox clientCheckBox;  // Checkbox for Client
    @FXML
    private CheckBox fournisseurCheckBox;  // Checkbox for Fournisseur

    // Email validation regex pattern
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    @FXML
    private void handleBackToLogIn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Signin.fxml"));  // Retour à la connexion
            Parent root = loader.load();
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the sign-in page.");
        }
    }

    @FXML
    void handleSaveUser(ActionEvent event) {
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String email = emailField.getText().trim();
        String mdp = mdpField.getText().trim();

        // Validate inputs
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || mdp.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        // Validate email format
        if (!Pattern.matches(EMAIL_REGEX, email)) {
            showAlert("Erreur", "L'email n'est pas valide. Veuillez saisir un email correct (ex: exemple@email.com).");
            return;
        }

        // Validate role (at least one role should be selected)
        String role = "";
        if (clientCheckBox.isSelected()) {
            role = "client";
        }
        if (fournisseurCheckBox.isSelected()) {
            role = role.isEmpty() ? "fournisseur" : role + ", fournisseur";  // Add Fournisseur to the role if already Client
        }

        if (role.isEmpty()) {
            showAlert("Erreur", "Le rôle doit être 'client' ou 'fournisseur'.");
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/signIn.fxml"));
            Parent root = loader.load();

            // Get the DetailController and refresh the TableView
            SigninController detailControlleruser = loader.getController();


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
*/