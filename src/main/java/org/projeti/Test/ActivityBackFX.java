package org.projeti.Test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ActivityBackFX extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/activity-back.fxml"));
        Scene scene = new Scene(loader.load());


        stage.setScene(scene);
        stage.setTitle("Activity Back");

        // Afficher la fenêtre
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
