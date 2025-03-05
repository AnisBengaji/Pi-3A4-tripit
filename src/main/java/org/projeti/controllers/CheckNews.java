package org.projeti.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;
import java.awt.Desktop;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class CheckNews {

    private static final String API_KEY = "faa104d60e5a4431b10e2e2c70c8c83d"; // Replace with your NewsAPI key
    private static final String BASE_URL = "https://newsapi.org/v2/top-headlines?country=us&apiKey=" + API_KEY;

    @FXML
    private ListView<String> newsListView;

    @FXML
    private TextField searchField;

    private List<String> articleUrls = new ArrayList<>();

    @FXML
    public void initialize() {
        fetchNews(BASE_URL);

        // Add listener for selection
        newsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            openArticle();
        });
    }

    private void fetchNews(String url) {
        new Thread(() -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode != 200) {
                    showError("Failed to fetch news. Error code: " + responseCode);
                    return;
                }

                Scanner scanner = new Scanner(connection.getInputStream());
                StringBuilder response = new StringBuilder();
                while (scanner.hasNext()) {
                    response.append(scanner.nextLine());
                }
                scanner.close();

                parseNews(response.toString());
            } catch (Exception e) {
                showError("Error fetching news: " + e.getMessage());
            }
        }).start();
    }

    private void parseNews(String jsonResponse) {
        Platform.runLater(() -> {
            newsListView.getItems().clear();
            articleUrls.clear();

            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray articles = jsonObject.getJSONArray("articles");

            for (int i = 0; i < articles.length(); i++) {
                JSONObject article = articles.getJSONObject(i);
                String title = article.getString("title");
                String url = article.getString("url");

                newsListView.getItems().add(title);
                articleUrls.add(url);
            }
        });
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String query = searchField.getText().trim();
        if (!query.isEmpty()) {
            String searchUrl = "https://newsapi.org/v2/everything?q=" + query + "&apiKey=" + API_KEY;
            fetchNews(searchUrl);
        }
    }

    private void openArticle() {
        int selectedIndex = newsListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            String url = articleUrls.get(selectedIndex);
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception e) {
                showError("Failed to open article.");
            }
        }
    }

    private void showError(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    @FXML
    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleSearch(new ActionEvent());
        }
    }
}
