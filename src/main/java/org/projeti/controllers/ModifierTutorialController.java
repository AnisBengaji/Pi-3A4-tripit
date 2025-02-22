package org.projeti.controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.projeti.Service.TutorialService;
import org.projeti.entites.Tutorial;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ModifierTutorialController {

    private final TutorialService tutorialService = new TutorialService();
    private Tutorial tutorialSelectionne;

    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtDebut;
    @FXML
    private TextField txtFin;
    @FXML
    private TextField txtPrix;
    @FXML
    private TextField txtOffre;
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
        loadTutorials();
        tutorialTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                remplirFormulaire(newSelection);
            }
        });
    }

    private void loadTutorials() {
        try {
            List<Tutorial> tutorials = tutorialService.showAll();
            ObservableList<Tutorial> observableList = FXCollections.observableList(tutorials);
            tutorialTable.setItems(observableList);

            colNom.setCellValueFactory(new PropertyValueFactory<>("nom_tutorial"));
            colDebut.setCellValueFactory(new PropertyValueFactory<>("date_debutTutorial"));
            colFin.setCellValueFactory(new PropertyValueFactory<>("date_finTutorial"));
            colPrix.setCellValueFactory(new PropertyValueFactory<>("prix_tutorial"));
            colOffre.setCellValueFactory(new PropertyValueFactory<>("offre"));

        } catch (SQLException e) {
            afficherAlerte("Erreur", "Impossible de charger les tutoriels: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void remplirFormulaire(Tutorial tutorial) {
        tutorialSelectionne = tutorial;
        txtNom.setText(String.valueOf(tutorial.getNom_tutorial()));
        txtDebut.setText(tutorial.getDate_debutTutorial());
        txtFin.setText(tutorial.getDate_finTutorial());
        txtPrix.setText(String.valueOf(tutorial.getPrix_tutorial()));
        txtOffre.setText(tutorial.getOffre());
    }

    @FXML
    private void handleModifierTutorial(ActionEvent event) {
        if (tutorialSelectionne == null) {
            afficherAlerte("Erreur", "Veuillez sélectionner un tutoriel à modifier.", Alert.AlertType.WARNING);
            return;
        }

        tutorialSelectionne.setNom_tutorial(txtNom.getText());
        tutorialSelectionne.setDate_debutTutorial(txtDebut.getText());
        tutorialSelectionne.setDate_finTutorial(txtFin.getText());
        tutorialSelectionne.setPrix_tutorial(Float.parseFloat(txtPrix.getText()));
        tutorialSelectionne.setOffre(txtOffre.getText());

        try {
            tutorialService.update(tutorialSelectionne);
            loadTutorials();
            afficherAlerte("Succès", "Tutoriel modifié avec succès !", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            afficherAlerte("Erreur", "Échec de la modification: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleSupprimerTutorial(ActionEvent event) {
        Tutorial selected = tutorialTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            afficherAlerte("Erreur", "Veuillez sélectionner un tutoriel à supprimer.", Alert.AlertType.WARNING);
            return;
        }

        try {
            tutorialService.delete(selected);
            loadTutorials();
            afficherAlerte("Succès", "Tutoriel supprimé avec succès !", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            afficherAlerte("Erreur", "Échec de la suppression: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void afficherAlerte(String titre, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void afficherAjouterOffre(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterTutorial.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un Tutoriel");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}