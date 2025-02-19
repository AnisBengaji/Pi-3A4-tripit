package org.projeti.Test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/destination-back.fxml"));
        Scene scene=new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Destination Back");

        stage.show();

    }
    public static void main(String[] args) {
        launch();
    }
}

