package org.projeti.Controlleurs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.projeti.Service.OffreService;
import org.projeti.entites.Offre;

import java.io.IOException;
import java.sql.SQLException;

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

    private final OffreService offreService = new OffreService();
    private int idOffre = -1; // Stocke l'ID pour la modification

    @FXML
    private void sauvegarderOffre() {
        System.out.println("Le bouton 'Sauvegarder' a été cliqué !");

        // Récupérer les valeurs des champs
        String titre = titreField.getText().trim();
        String description = descriptionField.getText().trim();
        String prixText = prixField.getText().trim();
        String tutorial = tutorialField.getText().trim();
        String destination = destinationField.getText().trim();

        // Vérification que les champs ne sont pas vides
        if (titre.isEmpty() || description.isEmpty() || prixText.isEmpty() || tutorial.isEmpty() || destination.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs.");
            return;  // Empêcher l'ajout si des champs sont vides
        }

        try {
            // Vérifier si les valeurs numériques sont valides
            float prix = Float.parseFloat(prixText);

            // Créer l'objet Offre
            Offre offre = new Offre(idOffre, titre, description, prix, tutorial, destination);
            System.out.println("Objet Offre créé : " + offre);

            // Ajouter ou modifier l'offre dans la base de données
            if (idOffre == -1) {
                boolean isAdded = offreService.insert(offre) > 0;  // On suppose que la méthode 'insert' renvoie un boolean
                if (isAdded) {
                    System.out.println("Ajout en base effectué !");
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Offre ajoutée avec succès !");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l'ajout de l'offre.");
                }
            } else {
                boolean isUpdated = offreService.update(offre) > 0;  // On suppose que la méthode 'update' renvoie un boolean
                if (isUpdated) {
                    System.out.println("Modification en base effectuée !");
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Offre modifiée avec succès !");
                    idOffre = -1; // Réinitialiser après modification
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la modification de l'offre.");
                }
            }

            clearFields();

        } catch (NumberFormatException e) {
            System.out.println("Erreur de conversion : " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez entrer un prix valide.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout/modification : " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l'ajout/modification de l'offre.");
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

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void afficherOffres(ActionEvent event) {
        try {
            // Charger le fichier FXML de l'interface afficherOffre
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/OffreDetails.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle et changer vers la nouvelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Offres");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}