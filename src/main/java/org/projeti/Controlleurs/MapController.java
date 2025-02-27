package org.projeti.Controlleurs;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.concurrent.Worker;
import netscape.javascript.JSObject;

public class MapController {
    @FXML
    private WebView webView;

    private WebEngine webEngine;

    public void initialize() {
        webEngine = webView.getEngine();
        // Chargez le fichier HTML de la carte
        webEngine.load(getClass().getResource("/map.html").toExternalForm());

        // Communication Java-JavaScript
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaConnector", new JavaConnector());
                addSampleEvents(); // Ajouter des marqueurs après chargement
            }
        });
    }

    // Classe pour communiquer avec JavaScript
    public class JavaConnector {
        public void showEventDetails(String title, String description) {
            // Affichez les détails dans JavaFX (ex: nouvelle fenêtre)
            Platform.runLater(() -> {
                System.out.println("Événement cliqué: " + title);
                // Mettez à jour l'UI ici (ex: Label, TableView, etc.)
            });
        }
    }

    // Ajouter des marqueurs depuis la base de données
    private void addSampleEvents() {
        // Exemple de données (remplacez par des données réelles)
        webEngine.executeScript("addMarker(48.8566, 2.3522, 'Tour Eiffel', 'Réservation disponible')");
        webEngine.executeScript("addMarker(48.8588, 2.2944, 'Champ de Mars', 'Événement annuel')");
    }
}