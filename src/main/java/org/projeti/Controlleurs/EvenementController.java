package org.projeti.Controlleurs;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.projeti.entites.Evenement;
import org.projeti.Service.EvenementService;
import org.projeti.utils.Database;

public class EvenementController {
    @FXML private ListView<String> evenementList;  // Liste des événements sous forme de texte
    @FXML private TextField typeField;
    @FXML private TextField dateField;
    @FXML private TextField lieuField;
    @FXML private TextField descriptionField;
    @FXML private TextField priceField;

    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;

    private EvenementService evenementService;
    private ObservableList<String> evenementData;  // Liste observable des événements sous forme de texte

    // Initialisation du contrôleur
    public void initialize() {
        // Connexion à la base de données
        Connection connection = Database.getInstance().getCnx();
        evenementService = new EvenementService(connection);
        loadEvenements();
    }
    private void showAlert(String title, String message, Alert.AlertType type) {
        // Afficher une alerte pour l'utilisateur
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadEvenements() {
        List<Evenement> evenements = evenementService.getAll();
        evenementData = FXCollections.observableArrayList();

        for (Evenement evenement : evenements) {
            evenementData.add(formatEvent(evenement));  // Formater chaque événement pour l'affichage
        }

        evenementList.setItems(evenementData);  // Assigner la liste des événements formatés
    }

    private String formatEvent(Evenement evenement) {
        return evenement.getType() + " - " + evenement.getDate_Evenement() + " - " + evenement.getLieu();
    }

    // Ajouter un événement
    @FXML
    private void handleAddEvent() {
        Evenement evenement = new Evenement(
                0, // ID généré automatiquement
                typeField.getText(),
                dateField.getText(),
                lieuField.getText(),
                descriptionField.getText(),
                Float.parseFloat(priceField.getText())
        );
        evenementService.add(evenement);
        loadEvenements(); // Recharger la liste des événements
    }

    // Mettre à jour un événement
    @FXML
    private void handleUpdateEvent() {
        String selectedEventText = evenementList.getSelectionModel().getSelectedItem();
        if (selectedEventText != null) {
            // Trouver l'événement correspondant à l'élément sélectionné
            Evenement selectedEvent = getEventByText(selectedEventText);
            if (selectedEvent != null) {
                selectedEvent.setType(typeField.getText());
                selectedEvent.setDate_Evenement(dateField.getText());
                selectedEvent.setLieu(lieuField.getText());
                selectedEvent.setDescription(descriptionField.getText());
                selectedEvent.setPrice(Float.parseFloat(priceField.getText()));

                evenementService.update(selectedEvent);
                loadEvenements(); // Recharger la liste des événements
            }
        }
    }

    // Supprimer un événement
    @FXML
    private void handleDeleteEvent() {
        String selectedEventText = evenementList.getSelectionModel().getSelectedItem();
        if (selectedEventText != null) {
            Evenement selectedEvent = getEventByText(selectedEventText);
            if (selectedEvent != null) {
                evenementService.delete(selectedEvent.getId_Evenement());
                loadEvenements(); // Recharger la liste des événements
            }
        }
    }

    // Trouver un événement basé sur son texte formaté
    private Evenement getEventByText(String eventText) {
        List<Evenement> evenements = evenementService.getAll();
        for (Evenement evenement : evenements) {
            if (formatEvent(evenement).equals(eventText)) {
                return evenement;
            }
        }
        return null;
    }
}