package org.projeti.controllers;

import org.projeti.Service.UserService;
import org.projeti.utils.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class SigninController {

    @FXML
    private TextField emailField;  // Champ de saisie pour l'email

    @FXML
    private PasswordField passwordField;  // Champ de saisie pour le mot de passe

    private UserService userService = new UserService();  // Service utilisateur pour les opérations CRUD

    private Connection cnx = Database.getInstance().getCnx(); // Connexion à la base de données

    @FXML
    private void handleSignUp(ActionEvent event) throws IOException {
        // Ouvre la fenêtre d'inscription
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterUser.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
    @FXML
    private void handleForgotPassword(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forgin.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the forgot password page.");
        }
    }

    @FXML
    private void handleSignIn(ActionEvent event) {
        String email = emailField.getText();  // Récupérer l'email saisi
        String password = passwordField.getText();  // Récupérer le mot de passe saisi

        // Vérification des identifiants
        if (isValidLogin(email, password)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main.fxml"));  // Charger la page principale
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();

                // Fermer la fenêtre de connexion
                Stage currentStage = (Stage) emailField.getScene().getWindow();
                currentStage.close();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load the main page.");
            }
        }
    }

    private boolean isValidLogin(String email, String password) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Vérifier si l'email existe dans la base de données
            String emailQuery = "SELECT `MDP` FROM `user` WHERE `Email` = ?";
            ps = cnx.prepareStatement(emailQuery);
            ps.setString(1, email);
            rs = ps.executeQuery();

            if (!rs.next()) {
                showAlert("Login Failed", "This email is not found.");
                return false; // L'email n'existe pas
            }

            // Vérifier si le mot de passe correspond
            String correctPassword = rs.getString("MDP");
            if (!correctPassword.equals(password)) {
                showAlert("Login Failed", "Incorrect password.");
                return false; // Mauvais mot de passe
            }

            return true; // Connexion réussie

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database error during login: " + e.getMessage());
            return false;
        } finally {
            // Fermeture des ressources
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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




/*package org.projeti.controllers;

import org.projeti.Service.UserService;
import org.projeti.utils.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class SigninController {

    @FXML
    private TextField emailField;  // Email input field

    @FXML
    private PasswordField passwordField;  // Password input field

    private UserService userService = new UserService();  // Instantiate UserService to access CRUD methods

    private Connection cnx = Database.getInstance().getCnx(); // Assuming this is your DB connection

    @FXML
    private void handleSignUp(ActionEvent event) throws IOException {
        // Open the sign-up form (your existing code for the sign-up window)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterUser.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    private void handleSignIn(ActionEvent event) {
        String email = emailField.getText();  // Get email from the input field
        String password = passwordField.getText();  // Get password from the input field

        // Check if the login credentials are valid
        if (isValidLogin(email, password)) {
            // If login is successful, load the main page (main.fxml)
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main.fxml"));  // Correct page to load
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();

                // Close the current stage (login screen)
                Stage currentStage = (Stage) emailField.getScene().getWindow();
                currentStage.close();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load the main page.");
            }
        } else {
            // If login fails, show an alert to the user
            showAlert("Login Failed", "Invalid email or password");
        }
    }
    private boolean isValidLogin(String email, String password) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // SQL query to check if the email and password match
            String query = "SELECT * FROM `user` WHERE `Email` = ? AND `MDP` = ?";
            ps = cnx.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);

            rs = ps.executeQuery();

            // Check if a matching row exists
            if (rs.next()) {
                // User found, valid login
                return true;
            } else {
                // No matching user, invalid login
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database error during login: " + e.getMessage());
            return false;  // Error connecting to the database
        } finally {
            // Close database resources
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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