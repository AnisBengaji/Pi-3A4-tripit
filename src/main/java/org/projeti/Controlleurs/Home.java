package org.projeti.Controlleurs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class Home extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Charger le fichier evenement.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/Evenment.fxml"));  // Make sure the FXML file path is correct

            // Définir la scène
            primaryStage.setTitle("Gestion des Événements");
            primaryStage.setScene(new Scene(root, 800, 600));

            // Afficher la fenêtre principale
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de l'interface FXML.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

