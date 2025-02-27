package org.projeti.Controlleurs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.projeti.Service.EvenementService;
import org.projeti.entites.Evenement;
import org.projeti.utils.Database;


import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.Comparator;
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
    @FXML private TextField latitudeField;
    @FXML private TextField longitudeField;
    @FXML private TextField searchField; //
    @FXML private ComboBox<String> priceFilterCombo;


    private Evenement selectedEvent;
    private EvenementService evenementService;
    private ObservableList<Evenement> evenementData = FXCollections.observableArrayList();

    public void initialize() {
        Connection connection = Database.getInstance().getCnx();
        evenementService = new EvenementService(connection);
        configureListSelection();
        loadEvenements();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());

        // Ajout de l'écouteur pour le filtre par prix
        priceFilterCombo.setItems(FXCollections.observableArrayList("Tous", "Moins de 50€", "50€ - 100€", "Plus de 100€"));
        priceFilterCombo.getSelectionModel().selectFirst(); // Sélectionner "Tous" par défaut


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
    private String formatEvent(Evenement evenement) {
        return evenement.getNom() + " - " + evenement.getDate_EvenementDepart()
                + " - " + evenement.getDate_EvenementArriver() + " - " + evenement.getLieu()
                + " - " + String.format("%.2f", evenement.getPrice()) + " €";  // Ajout du prix formaté
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



    private void fillFieldsWithSelectedEvent(Evenement event) {
        nomField.setText(event.getNom());
        dateDepartField.setValue(event.getDate_EvenementDepart());
        dateArriverField.setValue(event.getDate_EvenementArriver());
        lieuField.setText(event.getLieu());
        descriptionField.setText(event.getDescription());
        priceField.setText(String.valueOf(event.getPrice()));
        latitudeField.setText(String.valueOf(event.getLatitude()));
        longitudeField.setText(String.valueOf(event.getLongitude()));
    }


    @FXML
    private void handleAddEvent() {
        if (fieldsAreValid()) {
            try {
                double latitude = Double.parseDouble(latitudeField.getText());
                double longitude = Double.parseDouble(longitudeField.getText());

                Evenement evenement = new Evenement(
                        0,
                        nomField.getText(),
                        dateDepartField.getValue(),
                        dateArriverField.getValue(),
                        lieuField.getText(),
                        descriptionField.getText(),
                        Float.parseFloat(priceField.getText()),
                        latitude,
                        longitude
                );

                evenementService.add(evenement);
                loadEvenements();
                clearFields();
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Veuillez entrer des coordonnées valides", Alert.AlertType.ERROR);
            }
        }
    }

    // Méthode pour mettre à jour un événement avec latitude et longitude
    @FXML
    private void handleUpdateEvent() {
        if (selectedEvent == null) {
            showAlert("Erreur", "Veuillez sélectionner un événement", Alert.AlertType.ERROR);
            return;
        }

        if (fieldsAreValid()) {
            try {
                double latitude = Double.parseDouble(latitudeField.getText());
                double longitude = Double.parseDouble(longitudeField.getText());

                selectedEvent.setNom(nomField.getText());
                selectedEvent.setDate_EvenementDepart(dateDepartField.getValue());
                selectedEvent.setDate_EvenementArriver(dateArriverField.getValue());
                selectedEvent.setLieu(lieuField.getText());
                selectedEvent.setDescription(descriptionField.getText());
                selectedEvent.setPrice(Float.parseFloat(priceField.getText()));
                selectedEvent.setLatitude(latitude);
                selectedEvent.setLongitude(longitude);

                evenementService.update(selectedEvent);
                loadEvenements();
                clearFields();
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Veuillez entrer des coordonnées valides", Alert.AlertType.ERROR);
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
        // Vérification des champs obligatoires : nom, lieu et description
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

        // Validation du prix
        if (priceField.getText().isEmpty() || !isNumeric(priceField.getText()) || Float.parseFloat(priceField.getText()) < 0) {
            showAlert("Erreur", "Veuillez entrer un prix valide.", Alert.AlertType.ERROR);
            return false;
        }

        // Validation des coordonnées (latitude et longitude)
        try {
            double latitude = Double.parseDouble(latitudeField.getText());
            double longitude = Double.parseDouble(longitudeField.getText());

            // Vérification des valeurs de latitude et longitude dans une plage acceptable
            if (latitude < -90 || latitude > 90) {
                showAlert("Erreur", "La latitude doit être entre -90 et 90.", Alert.AlertType.ERROR);
                return false;
            }

            if (longitude < -180 || longitude > 180) {
                showAlert("Erreur", "La longitude doit être entre -180 et 180.", Alert.AlertType.ERROR);
                return false;
            }

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des coordonnées valides pour la latitude et la longitude.", Alert.AlertType.ERROR);
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



  // ComboBox pour filtrer par prix


    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase();
        ObservableList<Evenement> filteredList = FXCollections.observableArrayList();

        for (Evenement event : evenementData) {
            // Recherche par nom ou lieu
            if (event.getNom().toLowerCase().contains(searchText) || event.getLieu().toLowerCase().contains(searchText)) {
                filteredList.add(event);
            }
        }

        evenementList.setItems(filteredList);
    }

    @FXML// Méthode pour filtrer les événements par prix
    private void handleFilter() {
        String filterValue = priceFilterCombo.getValue();
        ObservableList<Evenement> filteredList = FXCollections.observableArrayList();

        for (Evenement event : evenementData) {
            if (filterValue.equals("Tous")) {
                filteredList.add(event);
            } else if (filterValue.equals("Moins de 50€") && event.getPrice() < 50) {
                filteredList.add(event);
            } else if (filterValue.equals("50€ - 100€") && event.getPrice() >= 50 && event.getPrice() <= 100) {
                filteredList.add(event);
            } else if (filterValue.equals("Plus de 100€") && event.getPrice() > 100) {
                filteredList.add(event);
            }
        }

        evenementList.setItems(filteredList);
    }


}