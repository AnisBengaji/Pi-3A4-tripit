package org.projeti.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import java.io.IOException;
import java.net.URL;

public class AdminInterfaceController {

    @FXML
    private VBox sidebar;

    @FXML
    private HBox navbar;

    @FXML
    private VBox content;

    @FXML
    private TextField searchBar;

    @FXML
    private ImageView userIcon;

    @FXML
    private Label dashboardLabel, userManagementLabel, reportsLabel, settingsLabel;

    @FXML
    public void initialize() {
        // Add event listeners for menu items
        if (dashboardLabel != null)
            dashboardLabel.setOnMouseClicked(event -> loadSection("Main.fxml"));
        if (userManagementLabel != null)
            userManagementLabel.setOnMouseClicked(event -> loadSection("UserManagement.fxml"));
        if (reportsLabel != null)
            reportsLabel.setOnMouseClicked(event -> loadSection("Reports.fxml"));
        if (settingsLabel != null)
            settingsLabel.setOnMouseClicked(event -> loadSection("Settings.fxml"));
    }
    private void loadSection(String fxmlFile) {
        try {
            String filePath = "/" + fxmlFile; // Only the filename
            System.out.println("Trying to load: " + filePath); // Debugging output

            URL resource = getClass().getResource(filePath);
            if (resource == null) {
                throw new IOException("FXML file not found: " + filePath);
            }

            FXMLLoader loader = new FXMLLoader(resource);
            AnchorPane section = loader.load();
            content.getChildren().setAll(section); // Replace current content

            System.out.println("Successfully loaded: " + fxmlFile);
        } catch (IOException e) {
            System.err.println("⚠ Error loading " + fxmlFile + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

}
