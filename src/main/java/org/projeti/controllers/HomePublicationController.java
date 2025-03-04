package org.projeti.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.projeti.Service.PublicationService;
import org.projeti.entites.Publication;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class HomePublicationController {

    @FXML
    private Button btnDetail;
    @FXML
    private Button btnAjouter;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button switchToCatButton;
    @FXML
    private ListView<Publication> publicationsListView; // ListView to display publications
    @FXML
    private BorderPane root; // Add @FXML annotation

    private PublicationService publicationService = new PublicationService();
    private ObservableList<Publication> publications = FXCollections.observableArrayList();
    private Publication selectedPublication; // To keep track of the selected publication

    // Cache the loaded views
    private Parent homeCatView; // Cached view for homeCat.fxml
    private Parent homePubView; // Cached view for homePub.fxml


    public void initialize() {
        try {
            loadPublications();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception properly
        }

        // Set up event handlers
        if (switchToCatButton != null) {
            switchToCatButton.setOnAction(event -> switchToHomeCat());
        }
    }

    private void loadPublications() throws SQLException {
        List<Publication> publicationList = publicationService.showAll();
        publications.setAll(publicationList);
        publicationsListView.setItems(publications);

        // Set a custom cell factory to display publications in the ListView
        publicationsListView.setCellFactory(param -> new PublicationListCell());
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
    private void goToDeletePub() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/deletePub.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnDelete.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Delete Publication");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load deletePub.fxml: " + e.getMessage());
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
            // Load the homeCat.fxml to switch the entire page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/homeCat.fxml"));
            Parent homeCatView = loader.load();

            // Replace the entire content of the current window with homeCat.fxml
            Stage stage = (Stage) switchToCatButton.getScene().getWindow();
            Scene scene = new Scene(homeCatView); // New scene with homeCat.fxml
            stage.setScene(scene);
            stage.setTitle("Les categories"); // Set the title for the new page
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading homeCat.fxml: " + e.getMessage());
        }
    }

}