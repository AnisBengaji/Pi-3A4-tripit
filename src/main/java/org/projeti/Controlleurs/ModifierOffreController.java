package org.projeti.Controlleurs;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.projeti.Service.OffreService;

import org.projeti.entites.Offre;

import java.sql.SQLException;

public class ModifierOffreController {
    @FXML
    private TextField txtTitre, txtDescription, txtPrix, txtTutorial, txtDestination;

    private Offre offreSelectionnee;
    private OffreDetailController offreDetailController;
    private final OffreService offreService = new OffreService();

    public void setOffreData(Offre offre ,OffreDetailController offreDetailController) {
        this.offreSelectionnee = offre;
        this.offreDetailController = offreDetailController;
        txtTitre.setText(offre.getTitre());
        txtDescription.setText(offre.getDescription());
        txtPrix.setText(String.valueOf(offre.getPrix()));
        txtTutorial.setText(offre.getTutorial());
        txtDestination.setText(offre.getDestination());
    }

    @FXML
    private void handleModifierOffre() {

        String titre = txtTitre.getText().trim();
        String description = txtDescription.getText().trim();
        String tutorial = txtTutorial.getText().trim();
        String destination = txtDestination.getText().trim();

        float prix;

        if (titre.isEmpty() || description.isEmpty() || tutorial.isEmpty() || destination.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.");
            return;
        }


        prix = Float.parseFloat(txtPrix.getText().trim());

        offreSelectionnee.setTitre(titre);
        offreSelectionnee.setDescription(description);
        offreSelectionnee.setPrix(prix);
        offreSelectionnee.setTutorial(tutorial);
        offreSelectionnee.setDestination(destination);

        try {
            offreService.update(offreSelectionnee);
            offreDetailController.refreshTable();
            showAlert("Succès", "Offre modifiée avec succès.");
            ((Stage) txtTitre.getScene().getWindow()).close();
        } catch (SQLException e) {
            showAlert("Erreur SQL", "Erreur lors de la mise à jour de l'offre: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

/*import javafx.collections.FXCollections;
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
import org.projeti.Service.OffreService;
import org.projeti.entites.Offre;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
public class ModifierOffreController {
    private final OffreService offreService = new OffreService();
    private Offre offreSelectionnee;

    @FXML
    private TextField txtTitre;
    @FXML
    private TextField txtDescription;
    @FXML
    private TextField txtPrix;
    @FXML
    private TextField txtTutorial;
    @FXML
    private TextField txtDestination;
    @FXML
    private TableView<Offre> tableView;
    @FXML
    private TableColumn<Offre, String> colTitre;
    @FXML
    private TableColumn<Offre, String> colDescription;
    @FXML
    private TableColumn<Offre, Float> colPrix;
    @FXML
    private TableColumn<Offre, String> colTutorial;
    @FXML
    private TableColumn<Offre, String> colDestination;

    @FXML
    public void initialize() {
        loadOffres();
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                remplirFormulaire(newSelection);
            }
        });
    }

    private void loadOffres() {
        try {
            List<Offre> offres = offreService.showAll();
            ObservableList<Offre> observableList = FXCollections.observableList(offres);
            tableView.setItems(observableList);

            colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
            colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
            colTutorial.setCellValueFactory(new PropertyValueFactory<>("tutorial"));
            colDestination.setCellValueFactory(new PropertyValueFactory<>("destination"));

        } catch (SQLException e) {
            afficherAlerte("Erreur", "Impossible de charger les offres: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void remplirFormulaire(Offre offre) {
        offreSelectionnee = offre;
        txtTitre.setText(offre.getTitre());
        txtDescription.setText(offre.getDescription());
        txtPrix.setText(String.valueOf(offre.getPrix()));
        txtTutorial.setText(offre.getTutorial());
        txtDestination.setText(offre.getDestination());
    }

    @FXML
    private void handleModifierOffre(ActionEvent event) {
        if (offreSelectionnee == null) {
            afficherAlerte("Erreur", "Veuillez sélectionner une offre à modifier.", Alert.AlertType.WARNING);
            return;
        }

        offreSelectionnee.setTitre(txtTitre.getText());
        offreSelectionnee.setDescription(txtDescription.getText());
        offreSelectionnee.setPrix(Float.parseFloat(txtPrix.getText()));
        offreSelectionnee.setTutorial(txtTutorial.getText());
        offreSelectionnee.setDestination(txtDestination.getText());

        try {
            offreService.update(offreSelectionnee);
            loadOffres();
            afficherAlerte("Succès", "Offre modifiée avec succès !", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            afficherAlerte("Erreur", "Échec de la modification: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleSupprimerOffre(ActionEvent event) {
        Offre selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            afficherAlerte("Erreur", "Veuillez sélectionner une offre à supprimer.", Alert.AlertType.WARNING);
            return;
        }

        try {
            offreService.delete(selected.getIdOffre());
            loadOffres();
            afficherAlerte("Succès", "Offre supprimée avec succès !", Alert.AlertType.INFORMATION);
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
            // Charger le fichier FXML de l'interface AjouterOffre
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterOffre.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle et changer vers la nouvelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une Offre");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}*/