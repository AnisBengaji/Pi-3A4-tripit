package org.projeti.Controlleurs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.projeti.Service.TutorialService;
import org.projeti.entites.Tutorial;
/*import org.json.JSONArray;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;*/
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.projeti.Service.TutorialService;
import org.projeti.entites.Tutorial;
import java.awt.Desktop;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class TutorialDetailController {
    @FXML
    private TableView<Tutorial> tableView;
    @FXML
    private TableColumn<Tutorial, String> colNomTutorial;
    @FXML
    private TableColumn<Tutorial, String> colDateDebut;
    @FXML
    private TableColumn<Tutorial, String> colDateFin;
    @FXML
    private TableColumn<Tutorial, Float> colPrixTutorial;
    @FXML
    private TableColumn<Tutorial, String> colOffre;
    @FXML
    private TableColumn<Tutorial, Void> colActions;
    @FXML
    private TableColumn<Tutorial, Void> colUpdate;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;

    private final TutorialService tutorialService = new TutorialService();
    private final ObservableList<Tutorial> tutorialList = FXCollections.observableArrayList();
    private static final String API_KEY = "VOTRE_CLE_API";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    @FXML
    public void initialize() {
        colNomTutorial.setCellValueFactory(new PropertyValueFactory<>("nom_tutorial"));
        colDateDebut.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        colDateFin.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        colPrixTutorial.setCellValueFactory(new PropertyValueFactory<>("prix_tutorial"));
        colOffre.setCellValueFactory(new PropertyValueFactory<>("offre"));

        addDeleteButtonToTable();
        addUpdateButtonToTable();
        addYoutubeButtonToTable();
        loadTutorials();

        searchButton.setOnAction(event -> searchOnYoutube());
    }

    public void loadTutorials() {
        try {
            List<Tutorial> tutorials = tutorialService.showAll();
            tutorialList.setAll(tutorials);
            tableView.setItems(tutorialList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addDeleteButtonToTable() {
        Callback<TableColumn<Tutorial, Void>, TableCell<Tutorial, Void>> cellFactory = param -> new TableCell<>() {
            private final Button btnDelete = new Button("Supprimer");

            {
                btnDelete.setStyle("-fx-background-color: #FF742C; -fx-text-fill: white;");
                btnDelete.setOnAction(event -> {
                    Tutorial tutorial = getTableView().getItems().get(getIndex());
                    deleteTutorial(tutorial);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnDelete);
            }
        };
        colActions.setCellFactory(cellFactory);
    }

    private void addUpdateButtonToTable() {
        Callback<TableColumn<Tutorial, Void>, TableCell<Tutorial, Void>> cellFactory = param -> new TableCell<>() {
            private final Button btnUpdate = new Button("Modifier");

            {
                btnUpdate.setStyle("-fx-background-color: #007A8C; -fx-text-fill: white;");
                btnUpdate.setOnAction(event -> {
                    Tutorial tutorial = getTableView().getItems().get(getIndex());
                    openUpdateWindow(tutorial);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnUpdate);
            }
        };
        colUpdate.setCellFactory(cellFactory);
    }

    private void addYoutubeButtonToTable() {
        colOffre.setCellFactory(column -> new TableCell<>() {
            private final Button btnYoutube = new Button("Voir sur YouTube");

            @Override
            protected void updateItem(String url, boolean empty) {
                super.updateItem(url, empty);

                if (empty || url == null || !url.matches("^https://www\\.youtube\\.com/watch\\?v=.*$")) {
                    setGraphic(null);
                } else {
                    btnYoutube.setOnAction(event -> {
                        try {
                            Desktop.getDesktop().browse(new URI(url));
                        } catch (Exception e) {
                            showAlert("Erreur", "Impossible d'ouvrir YouTube.");
                        }
                    });
                    setGraphic(btnYoutube);
                }
            }
        });
    }

    private void openUpdateWindow(Tutorial tutorial) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierTutorial.fxml"));
            Parent root = loader.load();
            org.projeti.Controlleurs.ModifierTutorialController controller = loader.getController();
            controller.setTutorialData(tutorial, this);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier le tutoriel");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger la page de modification.");
        }
    }

    private void deleteTutorial(Tutorial tutorial) {
        try {
            tutorialService.delete(tutorial);
            tutorialList.remove(tutorial);
            showAlert("Succès", "Tutoriel supprimé avec succès.");
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la suppression.");
        }
    }
    private void searchOnYoutube() {
        String query = searchField.getText();
        if (query.isEmpty()) {
            showAlert("Erreur", "Veuillez entrer un mot-clé pour la recherche.");
            return;
        }

        try {
            YouTube youtubeService = new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, null)
                    .setApplicationName("TutorialFinder")
                    .build();

            YouTube.Search.List request = youtubeService.search().list("snippet");
            SearchListResponse response = request.setKey(API_KEY)
                    .setQ(query)
                    .setType("video")
                    .setMaxResults(5L)
                    .execute();

            List<Tutorial> results = response.getItems().stream().map(item -> {
                Tutorial tutorial = new Tutorial();
                tutorial.setNom_tutorial(item.getSnippet().getTitle());
                tutorial.setOffre("https://www.youtube.com/watch?v=" + item.getId().getVideoId());
                return tutorial;
            }).collect(Collectors.toList());

            tutorialList.setAll(results);
            tableView.setItems(tutorialList);
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de récupérer les résultats.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



public void refreshTable() {
        loadTutorials();
    }
}
