package org.projeti.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.projeti.Service.CategorieService;
import org.projeti.entites.Categorie;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DetailCategoryController implements Initializable {

    @FXML
    private TableView<Categorie> categoriesTableView;

    @FXML
    private TableColumn<Categorie, Integer> categoryIdColumn;

    @FXML
    private TableColumn<Categorie, String> categoryNameColumn;

    @FXML
    private TableColumn<Categorie, String> descriptionColumn;

    @FXML
    private TableColumn<Categorie, String> publicationsIdColumn;

    @FXML
    private TableColumn<Categorie, Void> actionsColumn;

    @FXML
    private Button buttonreturn;

    private final CategorieService categorieService = new CategorieService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        loadCategories();
    }

    private void setupTableColumns() {
        categoryIdColumn.setPrefWidth(50);
        categoryNameColumn.setPrefWidth(150);
        descriptionColumn.setPrefWidth(200);
        publicationsIdColumn.setPrefWidth(150);
        actionsColumn.setPrefWidth(159);

        categoryIdColumn.setCellValueFactory(new PropertyValueFactory<>("idCategorie"));
        categoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("nomCategorie"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Fix for publicationsIdColumn
        publicationsIdColumn.setCellValueFactory(cellData -> {
            Categorie categorie = cellData.getValue();
            String publicationIds = "No Publications";  // Default message

            // Check if the publications are correctly retrieved
            if (categorie.getPublications() != null && !categorie.getPublications().isEmpty()) {
                publicationIds = categorie.getPublications().stream()
                        .map(publication -> String.valueOf(publication.getId_publication()))
                        .collect(Collectors.joining(", "));
            }

            return new javafx.beans.property.SimpleStringProperty(publicationIds);
        });

        actionsColumn.setCellFactory(createActionsCellFactory());
    }

    private Callback<TableColumn<Categorie, Void>, TableCell<Categorie, Void>> createActionsCellFactory() {
        return param -> new TableCell<>() {
            private final Button updateButton = new Button();
            private final Button deleteButton = new Button();

            {
                // Load and resize icons
                ImageView updateIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/upp.png")));
                updateIcon.setFitWidth(20);
                updateIcon.setFitHeight(20);

                ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/dl.png")));
                deleteIcon.setFitWidth(20);
                deleteIcon.setFitHeight(20);

                // Assign icons to buttons
                updateButton.setGraphic(updateIcon);
                deleteButton.setGraphic(deleteIcon);

                // Style buttons for a cleaner look
                updateButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
                deleteButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

                // Set button actions
                updateButton.setOnAction(event -> {
                    Categorie categorie = getTableView().getItems().get(getIndex());
                    updateCategory(categorie);
                });

                deleteButton.setOnAction(event -> {
                    Categorie categorie = getTableView().getItems().get(getIndex());
                    deleteCategory(categorie);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttonContainer = new HBox(15, updateButton, deleteButton); // Add spacing between icons
                    buttonContainer.setStyle("-fx-alignment: center;"); // Center align icons
                    setGraphic(buttonContainer);
                }
            }
        };
    }

    private void updateCategory(Categorie categorie) {
        // Open a dialog to update the category name
        TextInputDialog dialog = new TextInputDialog(categorie.getNomCategorie());
        dialog.setTitle("Update Category");
        dialog.setHeaderText("Modify the category name and description");
        dialog.setContentText("Enter new name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newName -> {
            // Open a dialog to update the description
            TextInputDialog descriptionDialog = new TextInputDialog(categorie.getDescription());
            descriptionDialog.setTitle("Update Category Description");
            descriptionDialog.setHeaderText("Modify the category description");
            descriptionDialog.setContentText("Enter new description:");

            Optional<String> descriptionResult = descriptionDialog.showAndWait();
            descriptionResult.ifPresent(newDescription -> {
                // Perform validation before updating
                if (controlSaisie(newName, newDescription)) {
                    categorie.setNomCategorie(newName);
                    categorie.setDescription(newDescription);
                    try {
                        categorieService.update(categorie);
                        loadCategories();
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Category updated successfully!");
                    } catch (SQLException e) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Failed to update category.");
                        e.printStackTrace();
                    }
                }
            });
        });
    }

    // Method to check if the name and description are valid and if the name already exists
    private boolean controlSaisie(String nomCategorie, String description) {
        // Check if the name and description are only letters and spaces
        if (!nomCategorie.matches("[a-zA-Z\\s]+") || !description.matches("[a-zA-Z\\s]+")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Category name and description should only contain letters and spaces.");
            return false;
        }

        // Check if the category name already exists
        try {
            if (categorieService.exists(nomCategorie)) {
                showAlert(Alert.AlertType.ERROR, "Error", "Category name already exists!");
                return false;
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error checking category name uniqueness.");
            return false;
        }

        return true;
    }

    private void deleteCategory(Categorie categorie) {
        try {
            if (categorie.getPublications() == null || categorie.getPublications().isEmpty()) {
                categorieService.delete(categorie);
                loadCategories();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Category deleted successfully!");
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "Cannot delete category with publications.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete category.");
            e.printStackTrace();
        }
    }

    private void loadCategories() {
        try {
            List<Categorie> categories = categorieService.showAll();
            ObservableList<Categorie> observableCategories = FXCollections.observableArrayList(categories);
            categoriesTableView.setItems(observableCategories);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load categories.");
            throw new RuntimeException(e);
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void returnHomeCat() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomeCat.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) buttonreturn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Categories!");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load HomeCat.fxml: " + e.getMessage());
        }
    }
}
