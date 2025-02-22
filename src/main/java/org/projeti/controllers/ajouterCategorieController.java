package org.projeti.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.projeti.entites.Categorie;
import org.projeti.Service.CategorieService;

import java.io.IOException;
import java.sql.SQLException;

public class ajouterCategorieController {

    @FXML
    private TextField nomCategorieTextField;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private Button returnButton;

    @FXML
    void ajouterCategorie(ActionEvent event) {
        String nomCategorie = nomCategorieTextField.getText();
        String description = descriptionTextField.getText();

        // Debugging output: print the values of inputs
        System.out.println("Nom Categorie: " + nomCategorie);
        System.out.println("Description: " + description);

        // Simple validation: check if fields are empty
        if (nomCategorie.isEmpty() || description.isEmpty()) {
            showAlert("Error", "All fields are required!", Alert.AlertType.ERROR);
            return;
        }

        // Check if the category name is already taken
        if (isCategoryNameTaken(nomCategorie)) {
            showAlert("Error", "Category name already exists!", Alert.AlertType.ERROR);
            return;
        }

        // Check if the name and description are valid (only letters and spaces)
        if (!nomCategorie.matches("[a-zA-Z\\s]+") || !description.matches("[a-zA-Z\\s]+")) {
            showAlert("Error", "Category name and description should only contain letters and spaces.", Alert.AlertType.ERROR);
            return;
        }

        // Create the category and save it to the database
        Categorie categorie = new Categorie();
        categorie.setNomCategorie(nomCategorie);
        categorie.setDescription(description);

        CategorieService categorieService = new CategorieService();

        try {
            int rowsAffected = categorieService.insert(categorie);
            if (rowsAffected > 0) {
                showAlert("Success", "Category added successfully!", Alert.AlertType.INFORMATION);
                nomCategorieTextField.clear();
                descriptionTextField.clear();
            } else {
                showAlert("Error", "Failed to add category!", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Error", "An error occurred: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Method to check if the category name already exists in the database
    private boolean isCategoryNameTaken(String nomCategorie) {
        CategorieService categorieService = new CategorieService();
        try {
            return categorieService.exists(nomCategorie);
        } catch (SQLException e) {
            showAlert("Error", "Database error occurred: " + e.getMessage(), Alert.AlertType.ERROR);
            return false;
        }
    }

    // Simplified alert method with alert type flexibility
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    @FXML
    private void returnToDetailCategory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailCat.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) returnButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Detail");
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to load DetailCat.fxml: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
}
