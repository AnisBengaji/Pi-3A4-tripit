package org.projeti.Controlleurs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.projeti.Service.ReservationService;
import org.projeti.Service.StripePaymentService;
import org.projeti.Service.WalletService;
import org.projeti.entites.*;
import org.projeti.utils.Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ReservationController {
    @FXML private ComboBox<String> statusComboBox;
    @FXML private TextField priceField;
    @FXML private ComboBox<ModePaiement> modePaiementComboBox;
    @FXML private ListView<Reservation> reservationListView;
    @FXML private CheckBox useScoreCheckBox;
    @FXML private Label walletScoreLabel;
    @FXML private Button decreaseScoreButton;
    @FXML private Label scoreToUseLabel;
    @FXML private Button increaseScoreButton;

    private ReservationService reservationService;
    private WalletService walletService;
    private ObservableList<Reservation> reservationList = FXCollections.observableArrayList();
    private float scoreToUse = 0.0f;

    public void initialize() {
        statusComboBox.setItems(FXCollections.observableArrayList("EN_ATTENTE", "CONFIRMEE", "ANNULEE"));
        reservationListView.setItems(reservationList);
        modePaiementComboBox.setItems(FXCollections.observableArrayList(ModePaiement.values()));

        try {
            Connection connection = Database.getInstance().getCnx();
            reservationService = new ReservationService(connection);
            walletService = new WalletService(connection);
            loadReservations();
            updateWalletDisplay();
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les réservations", Alert.AlertType.ERROR);
        }

        reservationListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue != null) {
                    populateFields(newValue);
                }
                updateWalletDisplay();
            } catch (SQLException e) {
                showAlert("Erreur", "Erreur lors de la mise à jour du wallet", Alert.AlertType.ERROR);
            }
        });
    }

    private void populateFields(Reservation reservation) {
        statusComboBox.setValue(reservation.getStatus().toString());
        priceField.setText(String.format("%.2f", reservation.getPrice_total()));
        modePaiementComboBox.setValue(reservation.getMode_paiment());
        scoreToUse = 0.0f;
        scoreToUseLabel.setText(String.format("%.2f€", scoreToUse));
    }

    private boolean fieldsAreValid() {
        try {
            float price_total = Float.parseFloat(priceField.getText());
            if (price_total < 0) {
                showAlert("Erreur", "Le prix ne peut pas être négatif.", Alert.AlertType.ERROR);
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un prix valide.", Alert.AlertType.ERROR);
            return false;
        }
    }

    @FXML
    private void increaseScore() {
        try {
            float availableScore = walletService.getWallet(1).getScore();
            float currentPrice = Float.parseFloat(priceField.getText());
            if (scoreToUse + 10 <= availableScore && scoreToUse + 10 <= currentPrice) {
                scoreToUse += 10.0f;
                scoreToUseLabel.setText(String.format("%.2f€", scoreToUse));
            } else {
                showAlert("Limite atteinte", "Impossible d'augmenter davantage : score disponible ou prix dépassé.", Alert.AlertType.WARNING);
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la récupération du score.", Alert.AlertType.ERROR);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un prix valide avant d'ajuster le score.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void decreaseScore() {
        if (scoreToUse - 10 >= 0) {
            scoreToUse -= 10.0f;
            scoreToUseLabel.setText(String.format("%.2f€", scoreToUse));
        }
    }

    @FXML
    private void handleAddReservation() {
        try {
            String statusString = statusComboBox.getValue();
            if (statusString == null || statusString.isEmpty()) {
                showAlert("Erreur", "Veuillez sélectionner un statut.", Alert.AlertType.ERROR);
                return;
            }
            Status status = Status.valueOf(statusString);

            if (!fieldsAreValid()) return;
            float originalPrice = Float.parseFloat(priceField.getText());
            float finalPrice = originalPrice;

            ModePaiement modePaiement = modePaiementComboBox.getValue();
            if (modePaiement == null) {
                showAlert("Erreur", "Veuillez sélectionner un mode de paiement.", Alert.AlertType.ERROR);
                return;
            }

            User user = new User();
            user.setId(1);

            Evenement evenement = new Evenement();
            evenement.setId_Evenement(1);

            if (useScoreCheckBox.isSelected() && scoreToUse > 0) {
                Wallet wallet = walletService.getWallet(user.getId());
                float availableScore = wallet.getScore();
                if (availableScore >= scoreToUse) {
                    float discount = scoreToUse;
                    finalPrice = originalPrice - discount;
                    wallet.deductScore(discount);
                    walletService.updateWallet(wallet);
                    showAlert("Succès",
                            "Discount de " + String.format("%.2f", discount) + "€ appliqué lors de l'ajout. " +
                                    "Prix final: " + String.format("%.2f", finalPrice) + "€",
                            Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Attention", "Score insuffisant pour le discount sélectionné.", Alert.AlertType.WARNING);
                }
            }

            Reservation reservation = new Reservation(0, status, finalPrice, modePaiement);
            reservation.setUser(user);
            reservation.setEvenement(evenement);

            reservationService.add(reservation);
            reservationList.add(reservation);

            walletService.addScore(user.getId(), 10.0f);
            updateWalletDisplay();

            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ajouter la réservation à la base de données : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleUpdateReservation() {
        Reservation selected = reservationListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                String statusString = statusComboBox.getValue();
                if (statusString == null || statusString.isEmpty()) {
                    showAlert("Erreur", "Veuillez sélectionner un statut.", Alert.AlertType.ERROR);
                    return;
                }
                selected.setStatus(Status.valueOf(statusString));

                if (!fieldsAreValid()) return;
                float originalPrice = Float.parseFloat(priceField.getText());
                float finalPrice = originalPrice;

                if (useScoreCheckBox.isSelected() && scoreToUse > 0) {
                    Wallet wallet = walletService.getWallet(selected.getUser().getId());
                    float availableScore = wallet.getScore();
                    if (availableScore >= scoreToUse) {
                        float discount = scoreToUse;
                        finalPrice = originalPrice - discount;
                        wallet.deductScore(discount);
                        walletService.updateWallet(wallet);
                        priceField.setText(String.format("%.2f", finalPrice));
                        showAlert("Succès",
                                "Discount de " + String.format("%.2f", discount) + "€ appliqué. " +
                                        "Nouveau prix: " + String.format("%.2f", finalPrice) + "€",
                                Alert.AlertType.INFORMATION);
                    } else {
                        showAlert("Attention", "Score insuffisant pour le discount sélectionné.", Alert.AlertType.WARNING);
                    }
                }

                selected.setPrice_total(finalPrice);

                ModePaiement modePaiement = modePaiementComboBox.getValue();
                if (modePaiement != null) {
                    selected.setMode_paiment(modePaiement);
                }

                reservationService.update(selected);
                loadReservations();
                reservationListView.refresh();
                updateWalletDisplay();
                populateFields(selected);
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible de mettre à jour la réservation : " + e.getMessage(), Alert.AlertType.ERROR);
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
                reservationService.delete(selected.getId_reservation());
                reservationList.remove(selected);
                reservationListView.getSelectionModel().clearSelection();
                updateWalletDisplay();
            } catch (SQLException e) {
                showAlert("Erreur", "Impossible de supprimer la réservation.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Aucune sélection", "Sélectionnez une réservation à supprimer", Alert.AlertType.WARNING);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        statusComboBox.getSelectionModel().clearSelection();
        priceField.clear();
        modePaiementComboBox.getSelectionModel().clearSelection();
        useScoreCheckBox.setSelected(false);
        scoreToUse = 0.0f;
        scoreToUseLabel.setText("0.00€");
    }

    private void loadReservations() throws SQLException {
        List<Reservation> reservations = reservationService.getAll();
        reservationList.setAll(reservations);
    }

    private void updateWalletDisplay() throws SQLException {
        Reservation selected = reservationListView.getSelectionModel().getSelectedItem();
        int userId = 1;
        if (selected != null) {
            userId = selected.getUser().getId();
        }

        Wallet wallet = walletService.getWallet(userId);
        if (wallet != null) {
            walletScoreLabel.setText(String.format("%.2f€", wallet.getScore()));
        } else {
            walletScoreLabel.setText("0.00€");
        }
    }
    @FXML
    private void handlePayButton() {
        try {
            String priceText = priceField.getText().trim();
            System.out.println("Valeur dans priceField: '" + priceText + "'"); // Ajoutez ce log
            if (priceText.isEmpty()) {
                showError("Erreur de saisie", "Le champ 'Prix total' est vide.", "Veuillez entrer un montant valide.");
                return;
            }

            double amount = Double.parseDouble(priceText);
            System.out.println("Montant: " + amount); // Vérifiez le montant

            String paymentUrl = StripePaymentService.createCheckoutSession(amount);
            System.out.println("URL Stripe: " + paymentUrl); // Vérifiez l’URL

            if (paymentUrl == null || paymentUrl.isEmpty()) {
                showError("Erreur Stripe", "URL de paiement invalide", "La session Stripe n'a pas retourné d'URL valide");
                return;
            }

            WebView webView = new WebView();
            webView.getEngine().load(paymentUrl);
            webView.getEngine().setOnError(event -> {
                System.out.println("Erreur WebView: " + event.toString());
                showError("Erreur WebView", "Erreur de chargement", event.toString());
            });

            Stage stage = new Stage();
            stage.setScene(new Scene(webView, 800, 600));
            stage.setTitle("Paiement Stripe");
            stage.show();

        } catch (NumberFormatException e) {
            System.out.println("Erreur NumberFormatException: " + e.getMessage()); // Log supplémentaire
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
    @FXML
    private void handleEmailButton(ActionEvent event) {
        try {
            // Charger le FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Email.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur
            EmailController emailController = loader.getController();




            // Créer la fenêtre
            Stage stage = new Stage();
            stage.setTitle("Envoyer un e-mail");
            stage.setScene(new Scene(root, 400, 300));
            stage.show();

        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir l'interface e-mail", Alert.AlertType.ERROR);
        }
    }
}