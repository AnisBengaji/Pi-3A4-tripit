package org.projeti.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Screen;

public class Home extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/signIn.fxml"));
            // Get the screen dimensions
            double screenWidth = Screen.getPrimary().getBounds().getWidth();
            double screenHeight = Screen.getPrimary().getBounds().getHeight();
            // Print the screen dimensions to the console
            System.out.println("Screen Width: " + screenWidth);
            System.out.println("Screen Height: " + screenHeight);

            primaryStage.setTitle("Accueil");
            primaryStage.setScene(new Scene(root, 1200, 750));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

