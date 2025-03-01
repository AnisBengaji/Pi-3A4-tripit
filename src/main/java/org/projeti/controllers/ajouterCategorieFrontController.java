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

public class ajouterCategorieFrontController {

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

        System.out.println("Nom Categorie: " + nomCategorie);
        System.out.println("Description: " + description);

        // Simple validation: check if fields are empty
        if (nomCategorie.isEmpty() || description.isEmpty()) {
            showAlert("Error", "tous les champs sont obligatoire", Alert.AlertType.ERROR);
            return;
        }


        if (isCategoryNameTaken(nomCategorie)) {
            showAlert("Error", "nom du categorie exist deja!", Alert.AlertType.ERROR);
            return;
        }


        if (!nomCategorie.matches("[a-zA-Z\\s]+") || !description.matches("[a-zA-Z\\s]+")) {
            showAlert("Error", "Category name and description should only contain letters and spaces.", Alert.AlertType.ERROR);
            return;
        }


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


    private boolean isCategoryNameTaken(String nomCategorie) {
        CategorieService categorieService = new CategorieService();
        try {
            return categorieService.exists(nomCategorie);
        } catch (SQLException e) {
            showAlert("Error", "Database error occurred: " + e.getMessage(), Alert.AlertType.ERROR);
            return false;
        }
    }


    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    @FXML
    private void returnToHomePublication() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/social.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) returnButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("publications!");
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to load social.fxml: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
}
