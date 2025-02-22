package org.projeti.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

public class travelapp implements Initializable {

    // FXML Components
    @FXML
    private VBox menu; // Left-side menu
    @FXML
    private Label aboutLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label subtitleLabel;
    @FXML
    private Button homeButton; // Home button
    @FXML
    private Button destinationsButton; // Destinations button
    @FXML
    private Button bookingsButton;
    @FXML
    private Button exploreButton; // Explore button
    @FXML
    private Button galleryButton; // Gallery button
    @FXML
    private Button tipsButton; // Travel Tips button
    @FXML
    private Button contactButton; // Contact button
    @FXML
    private Label welcomeLabel; // Welcome message label
    @FXML
    private Button eventbutton; // Event button
    @FXML
    private ImageView posterImage; // Poster image
    @FXML
    private Button returnButton; // Return button
    @FXML
    private AnchorPane root; // Root AnchorPane for parallax effect
    @FXML
    private ImageView backgroundImage; // Background image for parallax effect


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initializing controller...");
        if (exploreButton == null) {
            System.err.println("exploreButton is null!");
        } else {
            System.out.println("exploreButton is initialized.");
        }

        // Menu Slide-In Animation
        TranslateTransition menuSlideIn = new TranslateTransition(Duration.seconds(1), menu);
        menuSlideIn.setFromX(-250); // Start off-screen
        menuSlideIn.setToX(0); // Slide to original position
        menuSlideIn.play();

        // Welcome Text Fade-In Animation
        FadeTransition welcomeFadeIn = new FadeTransition(Duration.seconds(1.5), welcomeLabel);
        welcomeFadeIn.setFromValue(0); // Start fully transparent
        welcomeFadeIn.setToValue(1); // End fully opaque
        welcomeFadeIn.play();

        animateLabel(welcomeLabel, 0, 500); // Delay: 0ms, Duration: 500ms
        animateLabel(subtitleLabel, 200, 500); // Delay: 200ms, Duration: 500ms
        animateLabel(aboutLabel, 400, 500); // Delay: 400ms, Duration: 500ms
        animateLabel(descriptionLabel, 600, 500); // Delay: 600ms, Duration: 500ms
        animateLabel(exploreButton, 800, 500); // Delay: 800ms, Duration: 500ms

        // Explore Button Hover Animation
        if (exploreButton != null) {
            exploreButton.setOnMouseEntered(event -> {
                ScaleTransition scaleUp = new ScaleTransition(Duration.seconds(0.2), exploreButton);
                scaleUp.setToX(1.1); // Scale up by 10%
                scaleUp.setToY(1.1);
                scaleUp.play();
            });

            exploreButton.setOnMouseExited(event -> {
                ScaleTransition scaleDown = new ScaleTransition(Duration.seconds(0.2), exploreButton);
                scaleDown.setToX(1.0); // Return to original size
                scaleDown.setToY(1.0);
                scaleDown.play();
            });
        }

        // Background Image Parallax Effect
       /* root.setOnMouseMoved(event -> {
            double mouseX = event.getX();
            double mouseY = event.getY();
            double offsetX = (mouseX / 1200) * 20; // Adjust parallax intensity
            double offsetY = (mouseY / 800) * 20;
            backgroundImage.setTranslateX(-offsetX);
            backgroundImage.setTranslateY(-offsetY);
        }); */

        // Set up event handlers for buttons
        if (homeButton != null) {
            homeButton.setOnAction(event -> handleHomeButton());
        }
        if (destinationsButton != null) {
            destinationsButton.setOnAction(event -> handleDestinationsButton());
        }
        if (bookingsButton != null) {
            bookingsButton.setOnAction(event -> handleBookingsButton());
        }
        if (galleryButton != null) {
            galleryButton.setOnAction(event -> handleGalleryButton());
        }
        if (tipsButton != null) {
            tipsButton.setOnAction(event -> handleTipsButton());
        }
        if (contactButton != null) {
            contactButton.setOnAction(event -> handleContactButton());
        }
    }

    // Event Handlers for Menu Buttons
    private void handleHomeButton() {
        welcomeLabel.setText("Welcome to TravelEase - Home");
        System.out.println("Home button clicked!");
    }

    private void handleDestinationsButton() {
        welcomeLabel.setText("Explore Destinations");
        System.out.println("Destinations button clicked!");
    }

    private void handleBookingsButton() {
        welcomeLabel.setText("Manage Your Bookings");
        System.out.println("Bookings button clicked!");
    }

    private void handleGalleryButton() {
        welcomeLabel.setText("Travel Gallery");
        System.out.println("Gallery button clicked!");
    }

    private void handleTipsButton() {
        welcomeLabel.setText("Travel Tips and Guides");
        System.out.println("Travel Tips button clicked!");
    }

    private void handleContactButton() {
        welcomeLabel.setText("Contact Us");
        System.out.println("Contact button clicked!");
    }

    @FXML
    public void handleExploreButton() {
        try {
            // Load the explore.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/explore.fxml"));
            Parent root = loader.load();

            // Create a new scene and stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Explore TripIt");
            stage.show();

            // Close the current window (optional)
            if (exploreButton != null && exploreButton.getScene() != null) {
                ((Stage) exploreButton.getScene().getWindow()).close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handlePosterHover() {
        // Scale up the poster image on hover
        posterImage.setScaleX(1.05);
        posterImage.setScaleY(1.05);
    }

    @FXML
    public void handlePosterExit() {
        // Reset the poster image scale on exit
        posterImage.setScaleX(1.0);
        posterImage.setScaleY(1.0);
    }

    @FXML
    public void openFacebook() {
        openUrl("https://www.facebook.com");
    }

    @FXML
    public void openTwitter() {
        openUrl("https://www.twitter.com");
    }

    @FXML
    public void openInstagram() {
        openUrl("https://www.instagram.com");
    }

    @FXML
    public void openLinkedIn() {
        openUrl("https://www.linkedin.com");
    }

    private void openUrl(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goBackToTestHome(ActionEvent event) {
        System.out.println("Return button clicked!"); // Debugging statement
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/testhome.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showReservation() {
        loadView("Reservation.fxml");
    }

    @FXML
    private void showEvenement() {
        loadView("Evenement.fxml");
    }

    @FXML
    private void showhomePub() {
        loadView("homePub.fxml");
    }

    @FXML
    private void showdesination() {
        loadView("destination-back.fxml");
    }

    @FXML
    private void goToProfile() {
        loadView("AjouterUser.fxml");
    }

    @FXML
    private void goToAjouterOffre() {
        loadView("AjouterOffre.fxml");
    }

    private void loadView(String fxmlFile) {
        try {
            // Check if the FXML file exists
            URL fxmlLocation = getClass().getResource("/" + fxmlFile);
            if (fxmlLocation == null) {
                throw new IOException("FXML file not found: " + fxmlFile);
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            // Get the current scene and check if it exists
            Stage stage = (Stage) (root.getScene() != null ? root.getScene().getWindow() : null);
            if (stage == null) {
                stage = new Stage();
            }

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading " + fxmlFile + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void animateLabel(Node node, int delay, int duration) {
        if (node == null) {
            System.err.println("Node is null. Cannot animate.");
            return;
        }

        // Fade in animation
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(duration), node);
        fadeTransition.setFromValue(0); // Start fully transparent
        fadeTransition.setToValue(1); // End fully opaque
        fadeTransition.setDelay(Duration.millis(delay)); // Delay before starting
        fadeTransition.play();

        // Slide up animation
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(duration), node);
        translateTransition.setFromY(20); // Start 20px below
        translateTransition.setToY(0); // Slide to original position
        translateTransition.setDelay(Duration.millis(delay)); // Delay before starting
        translateTransition.play();

        // Scale up animation
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(duration), node);
        scaleTransition.setFromX(0.8); // Start slightly smaller
        scaleTransition.setFromY(0.8);
        scaleTransition.setToX(1.0); // End at original size
        scaleTransition.setToY(1.0);
        scaleTransition.setDelay(Duration.millis(delay)); // Delay before starting
        scaleTransition.play();
    }
}