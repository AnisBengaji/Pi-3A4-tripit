
package org.projeti.Controlleurs;

import com.sun.javafx.logging.PlatformLogger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.projeti.Service.EvenementService;
import org.projeti.entites.Evenement;
import org.projeti.utils.Database;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EvenementController {
    @FXML private ListView<String> evenementList;
    @FXML private TextField typeField;
    @FXML private DatePicker dateDepartField;  // Modifié en DatePicker
    @FXML private DatePicker dateArriverField; // Modifié en DatePicker
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

        // Ajouter un listener pour remplir automatiquement les champs lors de la sélection
        evenementList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fillFieldsWithSelectedEvent(newValue);
            }
        });
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

    private void fillFieldsWithSelectedEvent(String selectedEventText) {
        Evenement selectedEvent = getEventByText(selectedEventText);
        if (selectedEvent != null) {
            typeField.setText(selectedEvent.getType());
            dateDepartField.setValue(selectedEvent.getDate_EvenementDepart());  // Modifié pour utiliser setValue()
            dateArriverField.setValue(selectedEvent.getDate_EvenementArriver()); // Modifié pour utiliser setValue()
            lieuField.setText(selectedEvent.getLieu());
            descriptionField.setText(selectedEvent.getDescription());
            priceField.setText(String.valueOf(selectedEvent.getPrice()));
        }
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

                // Récupération des valeurs des DatePicker
                LocalDate dateDepart = dateDepartField.getValue();
                LocalDate dateArriver = dateArriverField.getValue();

                Evenement evenement = new Evenement(
                        0, typeField.getText(), dateDepart, dateArriver,
                        lieuField.getText(), descriptionField.getText(), price
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

                // Récupération des valeurs des DatePicker
                LocalDate dateDepart = dateDepartField.getValue();
                LocalDate dateArriver = dateArriverField.getValue();

                selectedEvent.setType(typeField.getText());
                selectedEvent.setDate_EvenementDepart(dateDepart);
                selectedEvent.setDate_EvenementArriver(dateArriver);
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
        if (typeField.getText().isEmpty() || lieuField.getText().isEmpty() || descriptionField.getText().isEmpty()) {
            showAlert("Erreur", "Le type, le lieu et la description doivent être remplis.", Alert.AlertType.ERROR);
            return false;
        }

        // Validation des dates
        LocalDate dateDepart = dateDepartField.getValue();
        LocalDate dateArriver = dateArriverField.getValue();
        LocalDate minDateDepart = LocalDate.of(2025, 2, 20);

        if (dateDepart == null || dateArriver == null) {
            showAlert("Erreur", "Les dates doivent être sélectionnées.", Alert.AlertType.ERROR);
            return false;
        }

        if (dateDepart.isBefore(minDateDepart)) {
            showAlert("Erreur", "La date de départ doit être au minimum le 20 février 2025.", Alert.AlertType.ERROR);
            return false;
        }

        if (!dateArriver.isAfter(dateDepart)) {
            showAlert("Erreur", "La date d'arrivée doit être postérieure à la date de départ.", Alert.AlertType.ERROR);
            return false;
        }

        if (priceField.getText().isEmpty() || !isNumeric(priceField.getText()) || Float.parseFloat(priceField.getText()) < 0) {
            showAlert("Erreur", "Veuillez entrer un prix valide.", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private boolean isNumeric(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    @FXML
    private void handleReservation(ActionEvent event) {
        try {
            // Charger le fichier FXML de réservation
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Reservation.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Créer une nouvelle fenêtre (stage)
            Stage reservationStage = new Stage();
            reservationStage.setTitle("Gestion des Réservations");
            reservationStage.setScene(scene);

            // Optionnel : configurer le stage parent
            reservationStage.initOwner(((Node) event.getSource()).getScene().getWindow());

            // Afficher la fenêtre
            reservationStage.show();

        } catch (IOException e) {
            Logger.getLogger(EvenementController.class.getName()).log(Level.SEVERE, "Erreur de chargement de Reservation.fxml", e);
            showAlert( // Appel avec 3 paramètres
                    "Erreur Critique",
                    "Erreur de chargement de l'interface :\n"
                            + e.getMessage()
                            + "\nVérifiez que le fichier Reservation.fxml existe dans le dossier views",
                    Alert.AlertType.ERROR // Troisième paramètre obligatoire
            );
        }
    }


    private void clearFields() {
        typeField.clear();
        dateDepartField.setValue(null); // Réinitialiser les DatePicker
        dateArriverField.setValue(null); // Réinitialiser les DatePicker
        lieuField.clear();
        descriptionField.clear();
        priceField.clear();
    }
}
