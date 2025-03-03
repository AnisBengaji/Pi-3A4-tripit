package org.projeti.Controlleurs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.projeti.Service.TutorialService;
import org.projeti.entites.Tutorial;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

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

    private final TutorialService tutorialService = new TutorialService();
    private int idTutorial = -1; // Stocke l'ID pour la modification

    @FXML
    private void sauvegarderTutorial() {
        System.out.println("Le bouton 'Sauvegarder' a été cliqué !");

        // Récupérer les valeurs des champs
        String nom = nomTutorialField.getText().trim();
        LocalDate dateDebut = dateDebutField.getValue();
        LocalDate dateFin = dateFinField.getValue();
        String prixText = prixField.getText().trim();
        String offre = offreField.getText().trim();

        // Vérification que les champs ne sont pas vides
        if (nom.isEmpty() || dateDebut == null || dateFin == null || prixText.isEmpty() || offre.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            float prix = Float.parseFloat(prixText);

            // Vérifier que la date de fin est après la date de début
            if (dateFin.isBefore(dateDebut)) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "La date de fin doit être après la date de début.");
                return;
            }

            // Créer l'objet Tutorial
            Tutorial tutorial = new Tutorial(idTutorial, nom, dateDebut, dateFin, prix, offre);
            System.out.println("Objet Tutorial créé : " + tutorial);

            // Ajouter ou modifier le tutoriel dans la base de données
            boolean isSuccessful;
            if (idTutorial == -1) {
                isSuccessful = tutorialService.insert(tutorial) > 0;
            } else {
                isSuccessful = tutorialService.update(tutorial) > 0;
                idTutorial = -1; // Réinitialiser après modification
            }

            if (isSuccessful) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Tutoriel enregistré avec succès !");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l'enregistrement du tutoriel.");
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez entrer un prix valide.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l'enregistrement du tutoriel.");
        }
    }

    @FXML
    private void clearFields() {
        nomTutorialField.clear();
        dateDebutField.setValue(null);
        dateFinField.setValue(null);
        prixField.clear();
        offreField.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void afficherTutorials(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/TutorialDetails.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Tutoriels");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
