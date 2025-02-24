package org.projeti.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainController {

    private Node sidebar;

    @FXML
    private void showReservation() {
        loadView("Reservation.fxml");
    }

    @FXML
    private void showEvenement() {
        loadView("Evenement.fxml");
    }
    @FXML
    private void showhomePub(){
        loadView("homePub.fxml");
    }
    @FXML
    private void showdesination(){
        loadView("destination-back.fxml");
    }
    @FXML
    private void goToProfile(){
        loadView("AjouterUser.fxml");
    }
    @FXML
    private void handleSignOut() {
        try {
            // Load the Sign-In Page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/signIn.fxml"));
            Parent root = loader.load();

            // Create a new stage for Sign-In
            Stage signInStage = new Stage();
            signInStage.setScene(new Scene(root));
            signInStage.show();

            // Close the current window (Main.fxml) using all windows
            Stage currentStage = (Stage) Stage.getWindows().stream()
                    .filter(window -> window.isShowing())
                    .findFirst()
                    .orElse(null);

            if (currentStage != null) {
                currentStage.close();  // Close the main stage
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadView(String fxmlFile) {
        try {
            // Vérifier si le fichier FXML existe
            URL fxmlLocation = getClass().getResource("/" + fxmlFile);
            if (fxmlLocation == null) {
                throw new IOException("Fichier FXML non trouvé : " + fxmlFile);
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            // Récupérer la scène actuelle et vérifier si elle existe
            Stage stage = (Stage) (root.getScene() != null ? root.getScene().getWindow() : null);
            if (stage == null) {
                stage = new Stage();
            }

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de " + fxmlFile + " : " + e.getMessage());
            e.printStackTrace();
        }

    }
}