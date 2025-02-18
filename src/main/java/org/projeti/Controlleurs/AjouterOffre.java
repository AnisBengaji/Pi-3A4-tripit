package org.projeti.Controlleurs;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.projeti.entites.Offre;

public class AjouterOffre {
    @FXML
    private TextField titreField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField prixField;
    @FXML
    private TextField tutorialField;
    @FXML
    private TextField destinationField;

    // Méthode appelée lorsqu'on clique sur le bouton "Sauvegarder l'offre"
    @FXML
    private void sauvegarderOffre() {
        try {
            // Vérifier que tous les champs sont remplis
            if (titreField.getText().isEmpty() || descriptionField.getText().isEmpty() ||
                    prixField.getText().isEmpty() || tutorialField.getText().isEmpty() || destinationField.getText().isEmpty()) {
                showAlert("Erreur", "Tous les champs doivent être remplis !");
                return;
            }

            float prix = Float.parseFloat(prixField.getText());

            // Création de l'offre
            Offre nouvelleOffre = new Offre(titreField.getText(), descriptionField.getText(), prix, tutorialField.getText(), destinationField.getText());
            System.out.println("Offre sauvegardée: " + nouvelleOffre);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le prix doit être un nombre valide !");
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
/*public class AjouterOffre {

    @FXML
    private TextField titreField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField prixField;
    @FXML
    private TextField tutorialField;
    @FXML
    private TextField destinationField;

    private OffreService offreService = new OffreService(); // Initialisation du service

    @FXML
    private void sauvegarderOffre() {
        try {
            // Vérifier que tous les champs sont remplis
            if (titreField.getText().isEmpty() || descriptionField.getText().isEmpty() ||
                    prixField.getText().isEmpty() || tutorialField.getText().isEmpty() || destinationField.getText().isEmpty()) {
                showAlert("Erreur", "Tous les champs doivent être remplis !");
                return;
            }

            float prix = Float.parseFloat(prixField.getText());

            // Création de l'offre
            Offre nouvelleOffre = new Offre(titreField.getText(), descriptionField.getText(), prix, tutorialField.getText(), destinationField.getText());

            // Ajout dans la base de données
            offreService.insert(nouvelleOffre);

            showAlert("Succès", "L'offre a été ajoutée avec succès !");
            clearFields(); // Vider les champs après l'ajout
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le prix doit être un nombre valide !");
        } catch (SQLException e) {
            showAlert("Erreur", "Une erreur est survenue lors de l'ajout de l'offre !");
            e.printStackTrace();
        }
    }

    @FXML
    private void clearFields() {
        titreField.clear();
        descriptionField.clear();
        prixField.clear();
        tutorialField.clear();
        destinationField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}*/