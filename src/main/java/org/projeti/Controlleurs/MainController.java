package org.projeti.Controlleurs;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MainController {

        @FXML
        private TextField searchField;

        @FXML
        private ListView<String> menuList;

        @FXML
        private ListView<String> timelineList;

        @FXML
        private DatePicker datePicker;

        @FXML
        public void initialize() {
            // Initialisation du menu
            menuList.getItems().addAll("Home", "All trips", "Travels", "Rooms", "Transport", "Events");

            // Initialisation du calendrier
            timelineList.getItems().addAll("Taxi transfer 18:30 - 19:00", "Check into a hotel 19:00 - 20:00");

            // Gestion de la recherche
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                System.out.println("Recherche : " + newValue);
            });
        }
    }
