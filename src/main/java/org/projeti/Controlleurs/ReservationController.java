
package org.projeti.Controlleurs;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.projeti.Service.ReservationService;
import org.projeti.Service.StripePaymentService;
import org.projeti.entites.*;
import org.projeti.utils.Database;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReservationController {

    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private TextField priceField;
    @FXML
    private ComboBox<ModePaiement> modePaiementComboBox;  // Change de TextField à ComboBox<ModePaiement>
    @FXML
    private ListView<Reservation> reservationListView;
    private Evenement selectedEvent;


    private HostServices hostServices;

    @FXML
    private Button payButton;

    private ReservationService reservationService;
    private int eventId;


    private ObservableList<Reservation> reservationList = FXCollections.observableArrayList();

    public void initialize() {
        // Initialisation de la ComboBox pour le statut avec des valeurs bien définies
        statusComboBox.setItems(FXCollections.observableArrayList("EN_ATTENTE", "CONFIRMEE", "ANNULEE"));
        reservationListView.setItems(reservationList);

        // Initialiser la ComboBox pour le mode de paiement avec les valeurs de l'énumération ModePaiement


        try {
            // Connexion à la base de données
            Connection connection = Database.getInstance().getCnx();
            reservationService = new ReservationService(connection);
            loadReservations();
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les réservations", Alert.AlertType.ERROR);
        }

        // Ajouter un listener pour la sélection dans la ListView
        reservationListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                populateFields(newValue); // Remplir les champs avec les données de la réservation sélectionnée
            }
        });
    }

    // Méthode pour remplir les champs avec les données de la réservation sélectionnée
    private void populateFields(Reservation reservation) {
        statusComboBox.setValue(reservation.getStatus().toString());
        priceField.setText(String.valueOf(reservation.getPrice_total()));
        modePaiementComboBox.setValue(reservation.getMode_paiment());
    }

    private boolean fieldsAreValid() {
        try {
            float price_total = Float.parseFloat(priceField.getText());
            if (price_total < 0) {
                showAlert("Erreur", "Le prix ne peut pas être négatif.", Alert.AlertType.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un prix valide.", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    @FXML
    private void handleAddReservation() {
        try {
            // Validation du statut sélectionné
            String statusString = statusComboBox.getValue();
            if (statusString == null || statusString.isEmpty()) {
                showAlert("Erreur", "Veuillez sélectionner un statut.", Alert.AlertType.ERROR);
                return;
            }
            Status status = Status.valueOf(statusString); // Convertir en enum

            // Validation du prix
            if (!fieldsAreValid()) return;
            float price_total = Float.parseFloat(priceField.getText());

            // Validation du mode de paiement
            ModePaiement modePaiement = modePaiementComboBox.getValue(); // Récupère la valeur de la ComboBox
            if (modePaiement == null) {
                showAlert("Erreur", "Veuillez sélectionner un mode de paiement.", Alert.AlertType.ERROR);
                return;
            }

            // Créer un utilisateur et un événement (remplacer par des valeurs réelles)
            User user = new User();
            user.setId(1); // Remplacez par l'ID réel de l'utilisateur

            Evenement evenement = new Evenement();
            evenement.setId_Evenement(this.eventId); // Utilisation de l'ID stocké

            // Créer la réservation avec un ID auto-incrémenté (0 pour indiquer qu'il sera généré)
            Reservation reservation = new Reservation(0, status, price_total, modePaiement);
            reservation.setUser(user);
            reservation.setEvenement(evenement);

            // Ajouter la réservation à la base de données et à la liste observable
            reservationService.add(reservation);
            reservationList.add(reservation);

            // Réinitialiser les champs du formulaire
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace(); // Ajoute cette ligne pour voir le message d'erreur précis
            showAlert("Erreur", "Impossible d'ajouter la réservation à la base de données.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleUpdateReservation() {
        Reservation selected = reservationListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                // Validation du statut
                String statusString = statusComboBox.getValue();
                if (statusString == null || statusString.isEmpty()) {
                    showAlert("Erreur", "Veuillez sélectionner un statut.", Alert.AlertType.ERROR);
                    return;
                }
                selected.setStatus(Status.valueOf(statusString)); // Mettre à jour le statut

                // Validation du prix
                if (!fieldsAreValid()) return;
                selected.setPrice_total(Float.parseFloat(priceField.getText()));

                // Mettre à jour le mode de paiement
                ModePaiement modePaiement = modePaiementComboBox.getValue(); // Récupérer le mode de paiement sélectionné
                if (modePaiement != null) {
                    selected.setMode_paiment(modePaiement);
                }

                // Mise à jour dans la base de données
                reservationService.update(selected);
                reservationListView.refresh(); // Actualisation de la vue de la liste
            } catch (SQLException e) {
                showAlert("Erreur", "Impossible de mettre à jour la réservation dans la base de données.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Aucune sélection", "Sélectionnez une réservation à modifier", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void handleDeleteReservation() {
        Reservation selected = reservationListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                // Supprimer la réservation de la base de données
                reservationService.delete(selected.getId_reservation());
                reservationList.remove(selected); // Supprimer de la liste observable
                reservationListView.getSelectionModel().clearSelection(); // Réinitialiser la sélection
            } catch (SQLException e) {
                showAlert("Erreur", "Impossible de supprimer la réservation.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Aucune sélection", "Sélectionnez une réservation à supprimer", Alert.AlertType.WARNING);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        // Afficher une alerte pour l'utilisateur
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        // Réinitialiser les champs du formulaire
        statusComboBox.getSelectionModel().clearSelection();
        priceField.clear();
        modePaiementComboBox.getSelectionModel().clearSelection();
    }

    private void loadReservations() throws SQLException {
        // Charger les réservations existantes depuis la base de données
        List<Reservation> reservations = reservationService.getAll();
        reservationList.setAll(reservations); // Mettre à jour la liste observable
    }
    public void setEventId(int eventId) {
        this.eventId = eventId;
        System.out.println("ID reçu : " + eventId); // Pour vérification
    }
    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @FXML
    private void handlePayButton() {
        try {
            // Récupérer le montant à payer
            String priceText = priceField.getText().trim();

            // Vérifiez que le champ n'est pas vide
            if (priceText.isEmpty()) {
                showError("Erreur de saisie", "Le champ 'Prix total' est vide.", "Veuillez entrer un montant valide.");
                return;
            }

            // Convertir la chaîne en double
            double amount = Double.parseDouble(priceText);

            // Créer une session de paiement Stripe
            String paymentUrl = StripePaymentService.createCheckoutSession(amount);

            // Ouvrir l'URL de paiement dans un WebView
            WebView webView = new WebView();
            webView.getEngine().load(paymentUrl);

            // Afficher le WebView dans une nouvelle fenêtre
            Stage stage = new Stage();
            stage.setScene(new Scene(webView, 800, 600));
            stage.setTitle("Paiement Stripe");
            stage.show();
        } catch (NumberFormatException e) {
            showError("Erreur de saisie", "Le montant saisi est invalide.", "Veuillez entrer un nombre valide.");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur de paiement", "Une erreur s'est produite lors du paiement.", e.getMessage());
        }
    }

    private void showError(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR); // Utilisez AlertType.ERROR
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
