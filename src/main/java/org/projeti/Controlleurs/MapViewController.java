package org.projeti.Controlleurs;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.net.URL;

public class MapViewController {

    @FXML
    private WebView webView;

    @FXML
    public void initialize() {
        WebEngine webEngine = webView.getEngine();
        URL url = getClass().getResource("/org/projeti/views/map.html");
        if (url != null) {
            webEngine.load(url.toExternalForm());
        } else {
            System.out.println("Erreur : fichier map.html introuvable !");
        }
    }
}
