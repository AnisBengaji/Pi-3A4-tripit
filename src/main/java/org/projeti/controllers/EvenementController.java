package org.projeti.controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.projeti.Service.EvenementService;
import org.projeti.entites.Evenement;
import org.projeti.utils.Database;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
public class EvenementController {
    @FXML private ListView<String> evenementList;
    @FXML private TextField typeField;
    @FXML private TextField dateDepartField;
    @FXML private TextField dateArriverField;
    @FXML private TextField lieuField;
    @FXML private TextField descriptionField;
    @FXML private TextField priceField;
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;

    private EvenementService evenementService;
    private ObservableList<String> evenementData = FXCollections.observableArrayList();

    public void initialize() {
        Connection connection = Database.getInstance().getCnx();
        evenementService = new EvenementService(connection);
        loadEvenements();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadEvenements() {
        List<Evenement> evenements = evenementService.getAll();
        evenementData.clear();

        for (Evenement evenement : evenements) {
            evenementData.add(formatEvent(evenement));
        }

        evenementList.setItems(evenementData);
    }

    private String formatEvent(Evenement evenement) {
        return evenement.getType() + " - " + evenement.getDate_EvenementDepart() + " - " + evenement.getDate_EvenementArriver() + " - "
                + evenement.getLieu() + " - " + evenement.getDescription() + " - "
                + evenement.getPrice();
    }

    @FXML
    private void handleAddEvent() {
        if (fieldsAreValid()) {
            try {
                float price = Float.parseFloat(priceField.getText());

                if (price < 0) {
                    showAlert("Erreur", "Le prix ne peut pas être négatif.", Alert.AlertType.ERROR);
                    return;
                }

                Evenement evenement = new Evenement(
                        0, typeField.getText(), dateDepartField.getText(), dateArriverField.getText(),lieuField.getText(),
                        descriptionField.getText(), price
                );
                evenementService.add(evenement);
                loadEvenements();
                clearFields();
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Veuillez entrer un prix valide.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleUpdateEvent() {
        String selectedEventText = evenementList.getSelectionModel().getSelectedItem();
        if (selectedEventText == null) {
            showAlert("Erreur", "Veuillez sélectionner un événement à modifier.", Alert.AlertType.ERROR);
            return;
        }

        Evenement selectedEvent = getEventByText(selectedEventText);
        if (selectedEvent == null) {
            showAlert("Erreur", "Événement introuvable.", Alert.AlertType.ERROR);
            return;
        }

        if (fieldsAreValid()) {
            try {
                float price = Float.parseFloat(priceField.getText());

                if (price < 0) {
                    showAlert("Erreur", "Le prix ne peut pas être négatif.", Alert.AlertType.ERROR);
                    return;
                }

                selectedEvent.setType(typeField.getText());
                selectedEvent.setDate_EvenementDepart(dateDepartField.getText());
                selectedEvent.setDate_EvenementArriver(dateArriverField.getText());
                selectedEvent.setLieu(lieuField.getText());
                selectedEvent.setDescription(descriptionField.getText());
                selectedEvent.setPrice(price);

                evenementService.update(selectedEvent);
                loadEvenements();
                clearFields();
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Veuillez entrer un prix valide.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleDeleteEvent() {
        String selectedEventText = evenementList.getSelectionModel().getSelectedItem();
        if (selectedEventText == null) {
            showAlert("Erreur", "Veuillez sélectionner un événement à supprimer.", Alert.AlertType.ERROR);
            return;
        }

        Evenement selectedEvent = getEventByText(selectedEventText);
        if (selectedEvent == null) {
            showAlert("Erreur", "Événement introuvable.", Alert.AlertType.ERROR);
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cet événement ?");
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                evenementService.delete(selectedEvent.getId_Evenement());
                loadEvenements();
            }
        });
    }

    private Evenement getEventByText(String eventText) {
        return evenementService.getAll().stream()
                .filter(e -> formatEvent(e).equals(eventText))
                .findFirst()
                .orElse(null);
    }


    private boolean fieldsAreValid() {
        // Vérification que 'type', 'lieu' et 'description' sont des chaînes non vides
        if (typeField.getText().isEmpty() || lieuField.getText().isEmpty() || descriptionField.getText().isEmpty()) {
            showAlert("Erreur", "Le type, le lieu et la description doivent être remplis.", Alert.AlertType.ERROR);
            return false;
        }

        // Vérification que 'type', 'lieu', et 'description' ne sont pas des nombres
        if (isNumeric(typeField.getText())) {
            showAlert("Erreur", "Le type ne peut pas être un nombre.", Alert.AlertType.ERROR);
            return false;
        }
        if (isNumeric(lieuField.getText())) {
            showAlert("Erreur", "Le lieu ne peut pas être un nombre.", Alert.AlertType.ERROR);
            return false;
        }
        if (isNumeric(descriptionField.getText())) {
            showAlert("Erreur", "La description ne peut pas être un nombre.", Alert.AlertType.ERROR);
            return false;
        }

        // Vérification et conversion des dates (format attendu: "AAAA-MM-JJ")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateDepart, dateArriver;
        LocalDate minDateDepart = LocalDate.of(2025, 2, 20); // Date minimale autorisée

        try {
            dateDepart = LocalDate.parse(dateDepartField.getText(), formatter);
            dateArriver = LocalDate.parse(dateArriverField.getText(), formatter);
        } catch (DateTimeParseException e) {
            showAlert("Erreur", "Les dates doivent être au format AAAA-MM-JJ.", Alert.AlertType.ERROR);
            return false;
        }

        // Vérification que la date de départ est au moins le 20 février 2025
        if (dateDepart.isBefore(minDateDepart)) {
            showAlert("Erreur", "La date de départ doit être au minimum le 20 février 2025.", Alert.AlertType.ERROR);
            return false;
        }

        // Vérification que la date d'arrivée est après la date de départ
        if (!dateArriver.isAfter(dateDepart)) {
            showAlert("Erreur", "La date d'arrivée doit être postérieure à la date de départ.", Alert.AlertType.ERROR);
            return false;
        }

        // Vérification de la validité du prix
        if (priceField.getText().isEmpty()) {
            showAlert("Erreur", "Le prix ne peut pas être vide.", Alert.AlertType.ERROR);
            return false;
        }

        try {
            float price = Float.parseFloat(priceField.getText());
            if (price < 0) {
                showAlert("Erreur", "Le prix ne peut pas être négatif.", Alert.AlertType.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un prix valide.", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    // Méthode utilitaire pour vérifier si une chaîne est un nombre
    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void clearFields() {
        typeField.clear();
        dateDepartField.clear();
        dateArriverField.clear();
        lieuField.clear();
        descriptionField.clear();
        priceField.clear();
    }
}
