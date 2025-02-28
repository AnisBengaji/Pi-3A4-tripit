package org.projeti.Controlleurs;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.InputStream;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.Desktop;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.projeti.Service.EvenementService;
import org.projeti.entites.Evenement;
import org.projeti.utils.Database;


import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EvenementController {
    @FXML
    private ListView<Evenement> evenementList; // Changé en ListView<Evenement>
    @FXML
    private TextField nomField;
    @FXML
    private DatePicker dateDepartField;
    @FXML
    private DatePicker dateArriverField;
    @FXML
    private TextField lieuField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField latitudeField;
    @FXML
    private TextField longitudeField;
    @FXML
    private TextField searchField; //

    @FXML
    private ComboBox<String> priceFilterCombo;


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
    @FXML
    private void handlePdf(ActionEvent event) throws FileNotFoundException {
        if (selectedEvent == null) {
            showAlert("Erreur", "Veuillez sélectionner un événement pour générer un PDF.", Alert.AlertType.ERROR);
            return;
        }

        // Création du fichier PDF
        String path = "C:\\Users\\MSI\\Desktop\\PI\\PI-20250210T192842Z-001\\PI\\src\\main\\resources\\Views\\pdf_" + selectedEvent.getId_Evenement() + ".pdf";
        PdfWriter writer = new PdfWriter(path);
        PdfDocument pdf = new PdfDocument(writer);
        com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdf);
        String logoPath = getClass().getResource("/views/icons/Logo.png").toExternalForm();  // Accès à une ressource dans le répertoire /resources

        // Ajouter le logo
        try {
            ImageData imageData = ImageDataFactory.create(logoPath);
            com.itextpdf.layout.element.Image image = new com.itextpdf.layout.element.Image(imageData);

            // Ajuster la taille de l'image et l'ajouter au document
            image.setHeight(100).setWidth(100);  // Vous pouvez ajuster la taille selon vos besoins
            document.add(image);
        } catch (IOException e) {
            showAlert("Erreur", "Le logo n'a pas pu être ajouté.", Alert.AlertType.ERROR);
        }

        // Titre du PDF
        document.add(new com.itextpdf.layout.element.Paragraph("Détails de l'événement : " + selectedEvent.getNom())
                .setFontColor(ColorConstants.BLACK).setBold().setFontSize(16).setTextAlignment(TextAlignment.CENTER));

        // Informations de l'événement
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 1})).useAllAvailableWidth();
        table.addCell("Nom :");
        table.addCell(selectedEvent.getNom());
        table.addCell("Date de départ :");
        table.addCell(selectedEvent.getDate_EvenementDepart().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        table.addCell("Date d'arrivée :");
        table.addCell(selectedEvent.getDate_EvenementArriver().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        table.addCell("Lieu :");
        table.addCell(selectedEvent.getLieu());
        table.addCell("Description :");
        table.addCell(selectedEvent.getDescription());
        table.addCell("Prix :");
        table.addCell(String.valueOf(selectedEvent.getPrice()) + " €");
        document.add(table);

        // Ajouter le message de bienvenue
        document.add(new com.itextpdf.layout.element.Paragraph("\nBienvenue chez Tripin!\n")
                .setFontColor(ColorConstants.BLACK).setFontSize(14).setTextAlignment(TextAlignment.LEFT));
        document.add(new com.itextpdf.layout.element.Paragraph("Nous sommes ravis que vous ayez choisi notre agence pour votre événement. Voici les détails de votre réservation.")
                .setFontColor(ColorConstants.BLACK).setFontSize(12).setTextAlignment(TextAlignment.LEFT));
        document.add(new com.itextpdf.layout.element.Paragraph("\nCordialement,\n")
                .setFontColor(ColorConstants.BLACK).setFontSize(12).setTextAlignment(TextAlignment.LEFT));
        document.add(new com.itextpdf.layout.element.Paragraph("Le Directeur Tripin")
                .setFontColor(ColorConstants.BLACK).setFontSize(12).setTextAlignment(TextAlignment.LEFT));

        // Ajouter la signature (image)
        try {
            String signaturePath = getClass().getResource("/views/icons/signature.png").toExternalForm(); // Chemin de l'image de la signature
            ImageData signatureImageData = ImageDataFactory.create(signaturePath);
            com.itextpdf.layout.element.Image signatureImage = new com.itextpdf.layout.element.Image(signatureImageData);
            signatureImage.setHeight(50).setWidth(150);  // Vous pouvez ajuster la taille de la signature
            document.add(signatureImage);
        } catch (IOException e) {
            showAlert("Erreur", "La signature n'a pas pu être ajoutée.", Alert.AlertType.ERROR);
        }

        document.close();

        // Ouvrir le PDF
        try {
            Desktop.getDesktop().open(new File(path));
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir le fichier PDF.", Alert.AlertType.ERROR);
        }
    }



}




