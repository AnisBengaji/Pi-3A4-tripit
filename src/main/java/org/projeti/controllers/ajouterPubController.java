package org.projeti.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.projeti.entites.Categorie;
import org.projeti.entites.Publication;
import org.projeti.Service.CategorieService;
import org.projeti.Service.PublicationService;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class ajouterPubController {

    @FXML private TextField titleTextField;
    @FXML private TextField contenuTextField;
    @FXML private DatePicker date_publicationDatePicker;
    @FXML private TextField authorTextField;
    @FXML private TextField visibilityTextField;
    @FXML private TextField imageTextField;
    @FXML private ComboBox<Categorie> categorieComboBox;

    private ObservableList<Categorie> categories;
    @FXML
    private Button returnButton;

    @FXML
    public void initialize() {
        // Load categories into the ComboBox
        CategorieService categorieService = new CategorieService();
        try {
            categories = FXCollections.observableArrayList(categorieService.showAll());
            categorieComboBox.setItems(categories);
        } catch (SQLException e) {
            System.err.println("Error loading categories: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Failed to load categories from the database!");
        }
    }

    @FXML
    void ajouterPub(ActionEvent event) {
        try {
            // Get data from the form fields
            String title = titleTextField.getText();
            String contenu = contenuTextField.getText();
            LocalDate localDate = date_publicationDatePicker.getValue();
            Date sqlDate = (localDate != null) ? Date.valueOf(localDate) : null;
            String author = authorTextField.getText();
            String visibility = visibilityTextField.getText();
            String image = imageTextField.getText();
            Categorie selectedCategorie = categorieComboBox.getSelectionModel().getSelectedItem();

            // Validate input
            if (title.isEmpty() || contenu.isEmpty() || author.isEmpty() || visibility.isEmpty() || selectedCategorie == null) {
                showAlert("Error", "All fields are required!");
                return;
            }

            // Create a Publication instance
            Publication publication = new Publication();
            publication.setTitle(title);
            publication.setContenu(contenu);
            publication.setDate_publication(sqlDate);
            publication.setAuthor(author);
            publication.setVisibility(visibility);
            publication.setImage(image);
            publication.setCategorie(selectedCategorie);

            // Save to database
            PublicationService publicationService = new PublicationService();
            int rowsAffected = publicationService.insert(publication);

            if (rowsAffected > 0) {
                showAlert("Success", "Publication added successfully!");
                clearForm();
            } else {
                showAlert("Error", "Failed to add publication!");
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Database error occurred!");
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "An unexpected error occurred!");
        }
    }

    @FXML
    void returnToDetail(ActionEvent event) {
        try {
            // Load the Detail.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/projeti/views/Detail.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) titleTextField.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Detail Page");
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading Detail.fxml: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Failed to navigate back to the detail page!");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    private void clearForm() {
        titleTextField.clear();
        contenuTextField.clear();
        date_publicationDatePicker.setValue(null);
        authorTextField.clear();
        visibilityTextField.clear();
        imageTextField.clear();
        categorieComboBox.getSelectionModel().clearSelection();
    }
    @FXML
    private void returnHome() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/social.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) returnButton.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Home");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load homePub.fxml: " + e.getMessage());
        }
    }

}