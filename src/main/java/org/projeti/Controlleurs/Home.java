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
            Parent root = FXMLLoader.load(getClass().getResource("/views/home.fxml"));
            primaryStage.setTitle("Accueil");
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}