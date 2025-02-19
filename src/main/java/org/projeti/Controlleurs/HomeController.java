package org.projeti.Controlleurs;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {

    @FXML
    private void showReservation() {
        loadView("Reservation.fxml");
    }

    @FXML
    private void showEvenement() {
        loadView("Evenement.fxml");
    }

    private void loadView(String fxmlFile) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/" + fxmlFile));
            Parent root = loader.load();

            // Créer une nouvelle scène à partir du contenu FXML
            Scene scene = new Scene(root);

            // Si c'est la première scène ou un stage déjà initialisé, obtenir le stage actuel
            Stage stage = (Stage) root.getScene().getWindow();

            if (stage == null) {
                // Si le stage est nul, obtenir la fenêtre principale
                stage = new Stage();
            }

            // Mettre à jour la scène de la fenêtre
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
