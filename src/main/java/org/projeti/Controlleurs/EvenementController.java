package org.projeti.Controlleurs;

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
    @FXML private ListView<Evenement> evenementList; // Changé en ListView<Evenement>
    @FXML private TextField nomField;
    @FXML private DatePicker dateDepartField;
    @FXML private DatePicker dateArriverField;
    @FXML private TextField lieuField;
    @FXML private TextField descriptionField;
    @FXML private TextField priceField;

    private Evenement selectedEvent;
    private EvenementService evenementService;
    private ObservableList<Evenement> evenementData = FXCollections.observableArrayList();

    public void initialize() {
        Connection connection = Database.getInstance().getCnx();
        evenementService = new EvenementService(connection);
        configureListSelection();
        loadEvenements();
    }

    private void configureListSelection() {
        evenementList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedEvent = newValue;
                System.out.println("Événement sélectionné - ID: " + selectedEvent.getId_Evenement()
                        + ", Nom: " + selectedEvent.getNom());
                fillFieldsWithSelectedEvent(newValue);
            }
        });

        evenementList.setCellFactory(param -> new ListCell<Evenement>() {
            @Override
            protected void updateItem(Evenement item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatEvent(item));
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
        evenementData.setAll(evenementService.getAll());
        evenementList.setItems(evenementData);
    }

    private String formatEvent(Evenement evenement) {
        return evenement.getNom() + " - " + evenement.getDate_EvenementDepart()
                + " - " + evenement.getDate_EvenementArriver() + " - " + evenement.getLieu();
    }

    private void fillFieldsWithSelectedEvent(Evenement event) {
        nomField.setText(event.getNom());
        dateDepartField.setValue(event.getDate_EvenementDepart());
        dateArriverField.setValue(event.getDate_EvenementArriver());
        lieuField.setText(event.getLieu());
        descriptionField.setText(event.getDescription());
        priceField.setText(String.valueOf(event.getPrice()));
    }

    @FXML
    private void handleAddEvent() {
        if (fieldsAreValid()) {
            try {
                Evenement evenement = new Evenement(
                        0,
                        nomField.getText(),
                        dateDepartField.getValue(),
                        dateArriverField.getValue(),
                        lieuField.getText(),
                        descriptionField.getText(),
                        Float.parseFloat(priceField.getText())
                );

                evenementService.add(evenement);
                loadEvenements();
                clearFields();
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Veuillez entrer un prix valide", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleUpdateEvent() {
        if (selectedEvent == null) {
            showAlert("Erreur", "Veuillez sélectionner un événement", Alert.AlertType.ERROR);
            return;
        }

        if (fieldsAreValid()) {
            try {
                selectedEvent.setNom(nomField.getText());
                selectedEvent.setDate_EvenementDepart(dateDepartField.getValue());
                selectedEvent.setDate_EvenementArriver(dateArriverField.getValue());
                selectedEvent.setLieu(lieuField.getText());
                selectedEvent.setDescription(descriptionField.getText());
                selectedEvent.setPrice(Float.parseFloat(priceField.getText()));

                evenementService.update(selectedEvent);
                loadEvenements();
                clearFields();
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Veuillez entrer un prix valide", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleDeleteEvent() {
        if (selectedEvent == null) {
            showAlert("Erreur", "Veuillez sélectionner un événement", Alert.AlertType.ERROR);
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cet événement ?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                evenementService.delete(selectedEvent.getId_Evenement());
                loadEvenements();
                clearFields();
            }
        });
    }



    @FXML
    private void handleReservation(ActionEvent event) {
        if (selectedEvent == null) {
            showAlert("Erreur", "Aucun événement sélectionné !", Alert.AlertType.WARNING);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Reservation.fxml"));
            Parent root = loader.load();

            ReservationController reservationController = loader.getController();
            reservationController.setEventId(selectedEvent.getId_Evenement());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir les réservations", Alert.AlertType.ERROR);
        }
    }

    private void clearFields() {
        nomField.clear();
        dateDepartField.setValue(null);
        dateArriverField.setValue(null);
        lieuField.clear();
        descriptionField.clear();
        priceField.clear();
    }
    private boolean fieldsAreValid() {
        if (nomField.getText().isEmpty() || lieuField.getText().isEmpty() || descriptionField.getText().isEmpty()) {
            showAlert("Erreur", "Le nom, le lieu et la description doivent être remplis.", Alert.AlertType.ERROR);
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
}