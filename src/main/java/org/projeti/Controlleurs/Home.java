package org.projeti.Controlleurs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.projeti.Service.EvenementService;
import org.projeti.entites.Evenement;
import org.projeti.utils.Database;

import java.sql.Connection;

public class Home extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Vérification de l'accès au fichier FXML
            System.out.println("Chargement du fichier FXML...");
            if (getClass().getResource("/Evenement.fxml") == null) {  // Vérifie si le fichier Evenement.fxml est présent
                System.err.println("Erreur : Le fichier FXML 'Evenement.fxml' est introuvable !");
                return;
            } else {
                System.out.println("Fichier FXML trouvé !");
            }

            // Charger l'interface
            Parent root = FXMLLoader.load(getClass().getResource("/Evenement.fxml"));
            primaryStage.setTitle("Gestion des Événements");
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.show();

            System.out.println("Interface chargée avec succès !");

            // Vérifier si les événements sont bien récupérés depuis la base de données
             Connection conn = Database.getInstance().getCnx(); /* Code pour obtenir la connexion à la base de données */;
            EvenementService evenementService = new EvenementService(conn);

            // Vérification de l'existence des événements dans la base de données
            if (evenementService.getAll().isEmpty()) {
                System.out.println("Aucun événement trouvé dans la base de données !");
            } else {
                System.out.println("Des événements ont été récupérés avec succès !");
            }

            // Exemple d'ajout d'un événement pour tester
            Evenement newEvent = new Evenement(0, "Concert", "2025-05-20", "Paris", "Concert live", 50.0f);
            evenementService.add(newEvent);
            System.out.println("Événement ajouté avec succès : " + newEvent);

            // Vérifier si l'événement a bien été ajouté
            if (evenementService.getAll().contains(newEvent)) {
                System.out.println("L'événement a été ajouté et récupéré avec succès !");
            } else {
                System.err.println("Erreur : L'événement n'a pas été ajouté correctement.");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'interface FXML.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}


