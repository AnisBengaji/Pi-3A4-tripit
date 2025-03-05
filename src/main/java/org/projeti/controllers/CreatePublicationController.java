package org.projeti.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.projeti.Service.CategorieService;
import org.projeti.Service.EmailValidation;
import org.projeti.Service.PublicationService;
import org.projeti.entites.Categorie;
import org.projeti.entites.Publication;

import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class CreatePublicationController {
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
    private File selectedImage;

    // Add an EmailService class for sending emails
    private EmailValidation emailValidation;

    public void initialize() {
        // Initialize visibility options
        visibilityComboBox.getItems().addAll("Public", "Private", "Friends");
        visibilityComboBox.getSelectionModel().select("Public");

        // Initialize EmailService
        emailValidation = new EmailValidation();

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

            // Select first category if available
            if (!categoryComboBox.getItems().isEmpty()) {
                categoryComboBox.getSelectionModel().select(0);
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

            // Create publication object
            Publication publication = new Publication();
            publication.setTitle(titleField.getText().trim());
            publication.setContenu(contentArea.getText().trim());
            publication.setDate_publication(Date.valueOf(LocalDate.now()));
            publication.setAuthor("Current User"); // Replace with actual user
            publication.setVisibility(visibilityComboBox.getSelectionModel().getSelectedItem());

            if (selectedImage != null) {
                publication.setImage(selectedImage.getAbsolutePath());
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
            int result = publicationService.insert(publication);

            if (result > 0) {
                // Send email after publication is saved
                String recipientEmail = "yagamilight839@gmail.com";  // Replace with actual recipient email
                String subject = "New Publication Created";
                String message = "A new publication has been created:\n\nTitle: " + publication.getTitle() + "\n\nContent: " + publication.getContenu();

                emailValidation.sendEmail(recipientEmail, subject, message);

                // Success, close the window
                closeWindow();
            } else {
                showAlert("Failed to save publication");
            }
        } catch (Exception e) {
            showAlert("Error saving publication: " + e.getMessage());
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
}
