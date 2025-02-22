package org.projeti.controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.projeti.Service.TutorialService;
import org.projeti.entites.Tutorial;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherTutorial {

    private final TutorialService tutorialService = new TutorialService();

    @FXML
    private TableView<Tutorial> tutorialTable;
    @FXML
    private TableColumn<Tutorial, String> colNom;
    @FXML
    private TableColumn<Tutorial, String> colDebut;
    @FXML
    private TableColumn<Tutorial, String> colFin;
    @FXML
    private TableColumn<Tutorial, Float> colPrix;
    @FXML
    private TableColumn<Tutorial, String> colOffre;

    @FXML
    public void initialize() {
        System.out.println("initialize() appelé pour AfficherTutorial");

        try {
            List<Tutorial> tutorials = tutorialService.showAll();
            System.out.println("Tutoriels récupérés: " + tutorials);

            ObservableList<Tutorial> observableList = FXCollections.observableList(tutorials);
            tutorialTable.setItems(observableList);

            colNom.setCellValueFactory(new PropertyValueFactory<>("nom_tutorial"));
            colDebut.setCellValueFactory(new PropertyValueFactory<>("date_debutTutorial"));
            colFin.setCellValueFactory(new PropertyValueFactory<>("date_finTutorial"));
            colPrix.setCellValueFactory(new PropertyValueFactory<>("prix_tutorial"));
            colOffre.setCellValueFactory(new PropertyValueFactory<>("offre"));

            System.out.println("TableView configurée avec succès.");

        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les tutoriels: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void afficherAjouterTutorial(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterTutorial.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un Tutoriel");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre d'ajout de tutoriel.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
