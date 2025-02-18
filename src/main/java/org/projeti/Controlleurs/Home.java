package org.projeti.Controlleurs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.projeti.Service.EvenementService;
import org.projeti.Service.ReservationService;
import org.projeti.entites.Evenement;
import org.projeti.utils.Database;

import java.sql.Connection;
import java.util.List;

public class Home extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("Démarrage de l'application...");

            // Charger la vue d'événements
            loadView(primaryStage, "/Evenement.fxml", "Gestion des Événements");

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'application !");
            e.printStackTrace();
        }
    }

    /**
     * Charge une vue FXML et l'affiche dans une fenêtre
     */
    private void loadView(Stage stage, String fxmlPath, String title) throws Exception {
        System.out.println("Chargement du fichier FXML : " + fxmlPath);

        if (getClass().getResource(fxmlPath) == null) {
            System.err.println("Erreur : Le fichier '" + fxmlPath + "' est introuvable !");
            return;
        }

        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        stage.setTitle(title);
        stage.setScene(new Scene(root, 800, 600));
        stage.show();

        System.out.println("Vue chargée avec succès !");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
