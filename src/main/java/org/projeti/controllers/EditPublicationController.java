package org.projeti.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

import org.projeti.Service.CategorieService;
import org.projeti.Service.PublicationService;
import org.projeti.entites.Categorie;
import org.projeti.entites.Publication;

public class EditPublicationController {
    @FXML private TextField titleField;
    @FXML private TextArea contentArea;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private ComboBox<String> visibilityComboBox;
    @FXML private TextField imagePathField;
    @FXML private Button browseButton;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private PublicationService publicationService;
    private CategorieService categorieService;
    private List<Categorie> categories;
    private Publication publication;
    private File selectedImage;

    public void initialize() {
        // Initialize visibility options
        visibilityComboBox.getItems().addAll("Public", "Private", "Friends");

        // Setup browse button action
        browseButton.setOnAction(e -> browseForImage());

        // Setup cancel button action
        cancelButton.setOnAction(e -> closeWindow());

        // Setup save button action
        saveButton.setOnAction(e -> savePublication());
    }

    public void setPublicationService(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    public void setCategorieService(CategorieService categorieService) {
        this.categorieService = categorieService;
        loadCategories();
    }

    public void initData(Publication publication) {
        this.publication = publication;

        // Populate form fields with publication data
        titleField.setText(publication.getTitle());
        contentArea.setText(publication.getContenu());
        imagePathField.setText(publication.getImage());

        // Set visibility
        visibilityComboBox.getSelectionModel().select(publication.getVisibility());

        // Set category
        if (publication.getCategorie() != null) {
            for (int i = 0; i < categoryComboBox.getItems().size(); i++) {
                String categoryName = categoryComboBox.getItems().get(i);
                if (categoryName.equals(publication.getCategorie().getNomCategorie())) {
                    categoryComboBox.getSelectionModel().select(i);
                    break;
                }
            }
        }
    }

    private void loadCategories() {
        try {
            // Clear previous items
            categoryComboBox.getItems().clear();

            // Get all categories
            categories = categorieService.showAll();

            // Add categories to combo box
            for (Categorie category : categories) {
                categoryComboBox.getItems().add(category.getNomCategorie());
            }
        } catch (Exception e) {
            showAlert("Error loading categories: " + e.getMessage());
        }
    }

    private void browseForImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Show file chooser dialog
        selectedImage = fileChooser.showOpenDialog(browseButton.getScene().getWindow());

        if (selectedImage != null) {
            imagePathField.setText(selectedImage.getAbsolutePath());
        }
    }

    private void savePublication() {
        try {
            // Validate inputs
            if (titleField.getText().trim().isEmpty()) {
                showAlert("Title cannot be empty");
                return;
            }

            if (contentArea.getText().trim().isEmpty()) {
                showAlert("Content cannot be empty");
                return;
            }

            if (categoryComboBox.getSelectionModel().getSelectedItem() == null) {
                showAlert("Please select a category");
                return;
            }

            // Update publication object
            publication.setTitle(titleField.getText().trim());
            publication.setContenu(contentArea.getText().trim());
            publication.setVisibility(visibilityComboBox.getSelectionModel().getSelectedItem());

            if (selectedImage != null) {
                publication.setImage(selectedImage.getAbsolutePath());
            } else if (!imagePathField.getText().isEmpty()) {
                publication.setImage(imagePathField.getText());
            } else {
                publication.setImage("");
            }

            // Find selected category
            String selectedCategoryName = categoryComboBox.getSelectionModel().getSelectedItem();
            Categorie selectedCategory = null;

            for (Categorie category : categories) {
                if (category.getNomCategorie().equals(selectedCategoryName)) {
                    selectedCategory = category;
                    break;
                }
            }

            if (selectedCategory == null) {
                showAlert("Selected category not found");
                return;
            }

            publication.setCategorie(selectedCategory);

            // Save publication
            int result = publicationService.update(publication);

            if (result > 0) {
                // Success, close the window
                closeWindow();
            } else {
                showAlert("Failed to update publication");
            }
        } catch (Exception e) {
            showAlert("Error updating publication: " + e.getMessage());
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    public Publication getPublication() {
        return publication;
    }
}