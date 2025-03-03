package org.projeti.Controlleurs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import java.io.File;
import java.net.URL;

/*public class Home extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterOffre.fxml"));

            primaryStage.setTitle("Gestion des Offres");
            primaryStage.setScene(new Scene(root, 800, 600));

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de l'interface FXML.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}*/

public class Home extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    public void start (Stage stage){

        try {


            Parent root = FXMLLoader.load(getClass().getResource("/TutorialDetails.fxml"));

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();


        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }
}
