package org.projeti.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.projeti.Service.PublicationService;
import org.projeti.entites.Publication;
import org.projeti.entites.Categorie;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class DetailController {

    @FXML private TableView<Publication> publicationsTableView;
    @FXML private TableColumn<Publication, Integer> idColumn;
    @FXML private TableColumn<Publication, String> titleColumn;
    @FXML private TableColumn<Publication, String> contentColumn;
    @FXML private TableColumn<Publication, String> authorColumn;
    @FXML private TableColumn<Publication, String> dateColumn;
    @FXML private TableColumn<Publication, String> visibilityColumn;
    @FXML private TableColumn<Publication, String> imageColumn;
    @FXML private TableColumn<Publication, String> idCategorie;
    @FXML private TableColumn<Publication, Void> actionColumn;

    @FXML private Button buttonreturn;

    private PublicationService publicationService = new PublicationService();
    private ObservableList<Publication> publications = FXCollections.observableArrayList();

    public void initialize() {
        setupTable();
        try {
            loadPublications();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id_publication"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date_publication"));
        visibilityColumn.setCellValueFactory(new PropertyValueFactory<>("visibility"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));

        idCategorie.setCellValueFactory(cellData -> {
            Categorie categorie = cellData.getValue().getCategorie();
            if (categorie != null) {
                return new SimpleStringProperty(categorie.getIdCategorie() + " - " + categorie.getNomCategorie());
            } else {
                return new SimpleStringProperty("No Category");
            }
        });

        // Add "Edit" button to each row
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("");

            {
                // Add an icon to the Edit button
                ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/pen.png")));
                editIcon.setFitHeight(20);
                editIcon.setFitWidth(20);
                editButton.setGraphic(editIcon);

                // Apply CSS class
                editButton.getStyleClass().add("edit-button");

                // Set action for the Edit button
                editButton.setOnAction(event -> {
                    Publication publication = getTableView().getItems().get(getIndex());
                    showEditDialog(publication);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        });
    }

    private void loadPublications() throws SQLException {
        List<Publication> publicationList = publicationService.showAll();
        publications.setAll(publicationList);
        publicationsTableView.setItems(publications);
    }

    @FXML
    private void GoBackHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/homePub.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) buttonreturn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Home");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load homePub.fxml: " + e.getMessage());
        }
    }

    private void showEditDialog(Publication publication) {
        // Create a dialog
        Dialog<Pair<Boolean, Publication>> dialog = new Dialog<>();
        dialog.getDialogPane().getStyleClass().add("dialog-pane"); // Apply the CSS class
        dialog.setTitle("Edit Publication");
        dialog.setHeaderText("Edit the publication details");

        // Set the button types (Save and Cancel)
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create the form fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titleField = new TextField(publication.getTitle());
        TextField contentField = new TextField(publication.getContenu());
        DatePicker datePicker = new DatePicker(publication.getDate_publication().toLocalDate());
        TextField authorField = new TextField(publication.getAuthor());
        TextField visibilityField = new TextField(publication.getVisibility());
        TextField imageField = new TextField(publication.getImage());
        TextField categoryField = new TextField(String.valueOf(publication.getCategorie().getIdCategorie()));

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Content:"), 0, 1);
        grid.add(contentField, 1, 1);
        grid.add(new Label("Date:"), 0, 2);
        grid.add(datePicker, 1, 2);
        grid.add(new Label("Author:"), 0, 3);
        grid.add(authorField, 1, 3);
        grid.add(new Label("Visibility:"), 0, 4);
        grid.add(visibilityField, 1, 4);
        grid.add(new Label("Image URL:"), 0, 5);
        grid.add(imageField, 1, 5);
        grid.add(new Label("Category ID:"), 0, 6);
        grid.add(categoryField, 1, 6);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a Pair<Boolean, Publication> when the save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                publication.setTitle(titleField.getText());
                publication.setContenu(contentField.getText());
                publication.setDate_publication(Date.valueOf(datePicker.getValue()));
                publication.setAuthor(authorField.getText());
                publication.setVisibility(visibilityField.getText());
                publication.setImage(imageField.getText());
                publication.getCategorie().setIdCategorie(Integer.parseInt(categoryField.getText()));
                return new Pair<>(true, publication);
            }
            return null;
        });

        Optional<Pair<Boolean, Publication>> result = dialog.showAndWait();

        result.ifPresent(pair -> {
            if (pair.getKey()) {
                Publication updatedPublication = pair.getValue();
                try {
                    update(updatedPublication);
                    publicationsTableView.refresh(); // Refresh the table to reflect changes
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert("Error", "Failed to update publication: " + e.getMessage());
                }
            }
        });
    }

    private void update(Publication publication) throws SQLException {
        publicationService.update(publication);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}