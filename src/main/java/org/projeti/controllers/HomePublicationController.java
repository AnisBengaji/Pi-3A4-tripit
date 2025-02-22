package org.projeti.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.projeti.Service.PublicationService;
import org.projeti.entites.Publication;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class HomePublicationController {

    @FXML
    private Button btnDetail;
    @FXML
    private Button btnAjouter;
    @FXML
    private Button switchToCatButton;
    @FXML
    private ListView<Publication> publicationsListView;
    @FXML
    private TextField searchTextField;
    @FXML
    private ComboBox<String> sortComboBox;

    private PublicationService publicationService = new PublicationService();
    private ObservableList<Publication> publications = FXCollections.observableArrayList();
    private FilteredList<Publication> filteredPublications;

    public void initialize() {
        try {
            loadPublications();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Initialize incremental search
        setupIncrementalSearch();

        // Initialize date sorting
        setupDateSorting();

        if (switchToCatButton != null) {
            switchToCatButton.setOnAction(event -> switchToHomeCat());
        }
    }

    private void loadPublications() throws SQLException {
        List<Publication> publicationList = publicationService.showAll();
        publications.setAll(publicationList);
        publicationsListView.setItems(publications);
        publicationsListView.setCellFactory(param -> new PublicationListCell());
    }

    private void setupIncrementalSearch() {
        filteredPublications = new FilteredList<>(publications, p -> true);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredPublications.setPredicate(publication -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true; // Show all publications if search field is empty
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Check if title, author, or content contains the search term
                return publication.getTitle().toLowerCase().contains(lowerCaseFilter) ||
                        publication.getAuthor().toLowerCase().contains(lowerCaseFilter) ||
                        publication.getContenu().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<Publication> sortedPublications = new SortedList<>(filteredPublications);
        publicationsListView.setItems(sortedPublications);
    }

    private void setupDateSorting() {
        sortComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("Newest to Oldest")) {
                publications.sort(Comparator.comparing(Publication::getDate_publication).reversed());
            } else if (newValue.equals("Oldest to Newest")) {
                publications.sort(Comparator.comparing(Publication::getDate_publication));
            }
        });
    }

    @FXML
    private void goToDetail() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Detail.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnDetail.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Detail");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load Detail.fxml: " + e.getMessage());
        }
    }

    @FXML
    private void goToAjouterPub() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterPub.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnAjouter.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Add Publication");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load ajouterPub.fxml: " + e.getMessage());
        }
    }

    @FXML
    private void switchToHomeCat() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/homeCat.fxml"));
            Parent homeCatView = loader.load();

            Stage stage = (Stage) switchToCatButton.getScene().getWindow();
            Scene scene = new Scene(homeCatView);
            stage.setScene(scene);
            stage.setTitle("Les categories");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading homeCat.fxml: " + e.getMessage());
        }
    }
}