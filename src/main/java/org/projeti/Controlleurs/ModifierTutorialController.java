package org.projeti.Controlleurs;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.projeti.Service.TutorialService;
import org.projeti.entites.Tutorial;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ModifierTutorialController {
    @FXML
    private TextField txtNomTutorial, txtDateDebut, txtDateFin, txtPrixTutorial, txtOffre;

    private Tutorial tutorialSelectionne;
    private TutorialDetailController tutorialDetailController;
    private final TutorialService tutorialService = new TutorialService();

    public void setTutorialData(Tutorial tutorial, TutorialDetailController tutorialDetailController) {
        this.tutorialSelectionne = tutorial;
        this.tutorialDetailController = tutorialDetailController;

        // Remplir les champs avec les données du tutoriel sélectionné
        txtNomTutorial.setText(tutorial.getNom_tutorial());
        txtDateDebut.setText(tutorial.getDateDebut().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        txtDateFin.setText(tutorial.getDateFin().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        txtPrixTutorial.setText(String.valueOf(tutorial.getPrix_tutorial()));
        txtOffre.setText(tutorial.getOffre());
    }

    @FXML
    private void handleModifierTutorial() {
        String nomTutorial = txtNomTutorial.getText().trim();
        String dateDebutStr = txtDateDebut.getText().trim();
        String dateFinStr = txtDateFin.getText().trim();
        String prixTutorialStr = txtPrixTutorial.getText().trim();
        String offre = txtOffre.getText().trim();

        // Validation des champs
        if (nomTutorial.isEmpty() || dateDebutStr.isEmpty() || dateFinStr.isEmpty() || prixTutorialStr.isEmpty() || offre.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        // Conversion des données
        LocalDate dateDebut = LocalDate.parse(dateDebutStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate dateFin = LocalDate.parse(dateFinStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        float prixTutorial = Float.parseFloat(prixTutorialStr);

        // Mettre à jour l'objet Tutorial
        tutorialSelectionne.setNom_tutorial(nomTutorial);
        tutorialSelectionne.setDateDebut(dateDebut);
        tutorialSelectionne.setDateFin(dateFin);
        tutorialSelectionne.setPrix_tutorial(prixTutorial);
        tutorialSelectionne.setOffre(offre);

        try {
            // Mettre à jour le tutoriel dans la base de données
            tutorialService.update(tutorialSelectionne);
            // Rafraîchir la table dans le contrôleur parent
            tutorialDetailController.refreshTable();
            // Afficher un message de succès
            showAlert("Succès", "Tutorial modifié avec succès.");
            // Fermer la fenêtre de modification
            ((Stage) txtNomTutorial.getScene().getWindow()).close();
        } catch (SQLException e) {
            showAlert("Erreur SQL", "Erreur lors de la mise à jour du tutorial: " + e.getMessage());
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