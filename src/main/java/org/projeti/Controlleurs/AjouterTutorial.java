package org.projeti.Controlleurs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.projeti.Service.TutorialService;
import org.projeti.entites.Tutorial;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AjouterTutorial {

    @FXML
    private TextField nomTutorialField;
    @FXML
    private DatePicker dateDebutField;
    @FXML
    private DatePicker dateFinField;
    @FXML
    private TextField prixField;
    @FXML
    private TextField offreField;
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

    private TutorialService tutorialService = new TutorialService();
    private ObservableList<Tutorial> tutorialList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Lier les colonnes aux attributs de la classe Tutorial
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom_tutorial"));
        colDebut.setCellValueFactory(new PropertyValueFactory<>("date_debutTutorial"));
        colFin.setCellValueFactory(new PropertyValueFactory<>("date_finTutorial"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix_tutorial"));
        colOffre.setCellValueFactory(new PropertyValueFactory<>("offre"));

        // Charger les tutoriels existants
        chargerTutoriels();
    }

    @FXML
    private void sauvegarderTutorial() {
        try {
            if (nomTutorialField.getText().isEmpty() || dateDebutField.getValue() == null ||
                    dateFinField.getValue() == null || prixField.getText().isEmpty() || offreField.getText().isEmpty()) {
                showAlert("Erreur", "Tous les champs doivent être remplis !");
                return;
            }

            LocalDate dateDebut = dateDebutField.getValue();
            LocalDate dateFin = dateFinField.getValue();
            float prix = Float.parseFloat(prixField.getText());

            Tutorial newTutorial = new Tutorial(nomTutorialField.getText(),
                    dateDebut.toString(),
                    dateFin.toString(),
                    prix,
                    offreField.getText());

            tutorialService.insert2(newTutorial);
            showAlert("Succès", "Le tutoriel a été ajouté avec succès !");
            clearFields();

            // Mettre à jour immédiatement la liste
            chargerTutoriels();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le prix doit être un nombre valide !");
        } catch (SQLException e) {
            showAlert("Erreur", "Une erreur est survenue lors de l'ajout du tutoriel !");
            e.printStackTrace();
        }
    }

    private void chargerTutoriels() {
        try {
            List<Tutorial> tutoriels = tutorialService.showAll();
            tutorialList.clear();
            tutorialList.addAll(tutoriels);
            tutorialTable.setItems(tutorialList);
            tutorialTable.refresh(); // Forcer la mise à jour
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les tutoriels !");
            e.printStackTrace();
        }
    }

    private void clearFields() {
        nomTutorialField.clear();
        dateDebutField.setValue(null);
        dateFinField.setValue(null);
        prixField.clear();
        offreField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}