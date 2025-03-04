package org.projeti.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.projeti.Service.DestinationService;
import org.projeti.entites.Activity;
import org.projeti.utils.WeatherApiUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActivityDetailsController {

    @FXML
    private Button btnNext;

    @FXML
    private Button btnPrev;

    @FXML
    private ImageView imageViewSlider;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblDescription;

    @FXML
    private Label lblDestination;

    @FXML
    private Label lblName;

    @FXML
    private Label lblPrice;
    @FXML private Label lblWeather;
    @FXML private Label lblMinTemp;
    @FXML private Label lblMaxTemp;
    @FXML private Label lblCondition;
    private DestinationService ds=new DestinationService();

    @FXML
    private Label lblType;
    private final String IMAGE_FOLDER = "C:\\Users\\21658\\Desktop\\ines_pidev\\Pi-3A4-tripit\\src\\main\\resources\\img";
    private Activity activity;
    private List<String> imageFilenames = new ArrayList<>();
    private int currentImageIndex = 0;
    public void setActivity(Activity activity) {
        this.activity = activity;
        lblName.setText(activity.getNom_activity());
        lblType.setText("Type: " + activity.getType());
        lblDescription.setText("Description: " + activity.getDescription());
        lblPrice.setText("Price: " + activity.getActivity_price() + " DT");
        lblDate.setText("Date: " + activity.getDateActivite().toString());
        try {
            String paysVille = ds.getPaysVilleById(activity.getIdDestination());
            String formattedLocation = paysVille.replace("-", ",");
            lblDestination.setText("Destination: " + formattedLocation);
            // Retrieve weather info using the location and activity date (yyyy-MM-dd)
            WeatherApiUtil.WeatherInfo weather = WeatherApiUtil.getWeather(formattedLocation, activity.getDateActivite().toString());
            if (weather != null) {
                lblWeather.setText("Weather:");
                lblMinTemp.setText("Min Temp: " + weather.getMinTemp() + "°C");
                lblMaxTemp.setText("Max Temp: " + weather.getMaxTemp() + "°C");
                lblCondition.setText("Condition: " + weather.getCondition());
            } else {
                lblWeather.setText("Weather data not available");
                lblMinTemp.setText("");
                lblMaxTemp.setText("");
                lblCondition.setText("");
            }
        } catch(Exception e) {
            e.printStackTrace();
            lblDestination.setText("Destination ID: " + activity.getIdDestination());
            lblWeather.setText("Weather data not available");
        }

        if (activity.getImage_activity() != null && !activity.getImage_activity().isEmpty()) {
            imageFilenames.add(activity.getImage_activity());
        }
        if (activity.getImage_activity2() != null && !activity.getImage_activity2().isEmpty()) {
            imageFilenames.add(activity.getImage_activity2());
        }
        if (activity.getImage_activity3() != null && !activity.getImage_activity3().isEmpty()) {
            imageFilenames.add(activity.getImage_activity3());
        }
        if (!imageFilenames.isEmpty()) {
            currentImageIndex = 0;
            displayCurrentImage();
        }
    }
    private void displayCurrentImage() {
        if (imageFilenames.isEmpty()) {
            imageViewSlider.setImage(null);
            return;
        }
        String filename = imageFilenames.get(currentImageIndex);
        File file = new File(IMAGE_FOLDER, filename);
        if (file.exists()) {
            Image image = new Image(file.toURI().toString());
            imageViewSlider.setImage(image);
        } else {
            imageViewSlider.setImage(null);
        }
    }
    @FXML
    void nextImage(ActionEvent event) {
        if (imageFilenames.isEmpty()) {
            return;
        }
        currentImageIndex++;
        if (currentImageIndex >= imageFilenames.size()) {
            currentImageIndex = 0;
        }
        displayCurrentImage();
    }

    @FXML
    void prevImage(ActionEvent event) {
        if (imageFilenames.isEmpty()) {
            return;
        }
        currentImageIndex--;
        if (currentImageIndex < 0) {
            currentImageIndex = imageFilenames.size() - 1;
        }
        displayCurrentImage();
    }
    @FXML
    void retour(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/activity-back.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) lblDescription.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Destination");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load Destination.fxml: " + e.getMessage());
        }
    }
}

