package org.projeti.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.projeti.Service.ActivityService;
import org.projeti.Service.DestinationService;
import org.projeti.entites.Activity;
import org.projeti.utils.CurrencyConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ActivityBack {

    // Existing FXML fields...
    @FXML
    private Button btnajout;
    @FXML
    private Button btnsup;
    @FXML
    private TextField tfrechercher;
    @FXML
    private ComboBox<String> cbtri;
    @FXML
    private ComboBox<String> cbdestination;
    @FXML
    private TableColumn<Activity, Float> colactivity_price;
    @FXML
    private TableColumn<Activity, ?> coldescription;
    @FXML
    private TableColumn<Activity, String> colidDestination;
    @FXML
    private TableColumn<Activity, ?> colimage_activity;
    @FXML
    private TableColumn<Activity, ?> colimage_activity2;
    @FXML
    private TableColumn<Activity, ?> colimage_activity3;
    @FXML
    private TableColumn<Activity, ?> colnom_activity;
    @FXML
    private TableColumn<Activity, ?> coltype;
    @FXML
    private TableColumn<Activity, ?> coldt_activite;
    @FXML
    private Button modifier;
    @FXML
    private TableView<Activity> tabActivity;
    @FXML
    private TextField tfactivity_price;
    @FXML
    private TextField tfdescription;
    @FXML
    private TextField tfimage_activity;
    @FXML
    private TextField tfimage_activity2;
    @FXML
    private TextField tfimage_activity3;
    @FXML
    private TextField tfnom_activity;
    @FXML
    private TextField tftype;
    @FXML
    private ComboBox<String> cbCurrency;

    // Replace the text field with a DatePicker for date selection.
    @FXML
    private DatePicker dpDateActivite;

    // New upload buttons (defined in FXML)
    @FXML
    private Button btnUploadImage1;
    @FXML
    private Button btnUploadImage2;
    @FXML
    private Button btnUploadImage3;

    ObservableList<Activity> obslist;
    private final ActivityService as = new ActivityService();
    private final DestinationService ds = new DestinationService();

    // Fixed folder where images will be stored.
    private final String IMAGE_FOLDER = "C:\\Users\\21658\\Desktop\\ines_pidev\\Pi-3A4-tripit\\src\\main\\resources\\img";

    @FXML
    public void initialize() throws SQLException {
        try {
            cbdestination.setItems(FXCollections.observableArrayList(ds.getPaysVille()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (tabActivity == null) {
            System.err.println("Erreur : tabActivity est null. Vérifiez le fichier FXML.");
        } else {
            tabActivity.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            refresh(as.showAll());
        }
        cbtri.setItems(FXCollections.observableArrayList("Nom", "Prix", "Type"));
        recherche_avance();
        cbCurrency.setItems(FXCollections.observableArrayList("TND", "USD", "EUR", "CAD"));
        cbCurrency.setValue("TND");
        cbCurrency.valueProperty().addListener((obs,oldValue,newValue) -> {
            tabActivity.refresh();
        });
    }

    void refresh(List<Activity> activities) {
        if (tabActivity == null) {
            System.err.println("Erreur : tabActivity est null. Vérifiez le fichier FXML.");
            return;
        }

        colnom_activity.setCellValueFactory(new PropertyValueFactory<>("nom_activity"));
        colimage_activity.setCellValueFactory(new PropertyValueFactory<>("image_activity"));
        colimage_activity2.setCellValueFactory(new PropertyValueFactory<>("image_activity2"));
        colimage_activity3.setCellValueFactory(new PropertyValueFactory<>("image_activity3"));
        coltype.setCellValueFactory(new PropertyValueFactory<>("type"));
        coldescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colactivity_price.setCellFactory(col -> new TableCell<Activity, Float>() {
            @Override
            protected void updateItem(Float item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null) {
                    setText(null);
                } else {
                    Activity activity = (Activity) getTableRow().getItem();
                    if (activity == null) {
                        setText(null);
                    } else {
                        String currency = cbCurrency.getValue();
                        double basePrice = activity.getActivity_price();
                        double convertedPrice = basePrice;
                        if (!currency.equals("TND")) {
                            convertedPrice = CurrencyConverter.convert(basePrice, "TND", currency);
                        }
                        setText(String.format("%.2f %s", convertedPrice, currency));
                    }
                }
            }
        });

        coldt_activite.setCellValueFactory(new PropertyValueFactory<>("dateActivite"));
        colidDestination.setCellValueFactory(cell -> {
            int id = cell.getValue().getIdDestination();
            try {
                String payville = ds.getPaysVilleById(id);
                return new SimpleStringProperty(payville);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        obslist = FXCollections.observableArrayList(activities);
        System.out.println("Données récupérées : " + obslist.size() + " activités trouvées");
        tabActivity.setItems(null);
        tabActivity.setItems(obslist);
        if (obslist.isEmpty()) {
            System.err.println("Aucune activité trouvée dans la base de données.");
        }
    }

    @FXML
    void ajouter(ActionEvent event) {
        if (!controle_saisie()) {
            return;
        }

        try {
            // Retrieve the date from DatePicker and convert it to java.sql.Date.
            LocalDate localDate = dpDateActivite.getValue();
            Date dateActivite = Date.valueOf(localDate);

            Activity a = new Activity(
                    tfnom_activity.getText(),
                    tfimage_activity.getText(),      // Contains filename only
                    tfimage_activity2.getText(),
                    tfimage_activity3.getText(),
                    tftype.getText(),
                    tfdescription.getText(),
                    Float.parseFloat(tfactivity_price.getText()),
                    dateActivite,
                    ds.getIdByPaysVille(cbdestination.getValue())
            );
            as.insert(a);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setContentText("Activité insérée avec succès!");
            alert.show();
            refresh(as.showAll());
            initialize();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setContentText(e.getMessage());
            alert.show();
            throw new RuntimeException(e);
        }
    }

    boolean controle_saisie() {
        String erreur = "";
        if (tfnom_activity.getText().trim().isEmpty()) {
            erreur += "Nom de l'activité vide\n";
        }
        if (tfimage_activity.getText().trim().isEmpty()) {
            erreur += "Image de l'activité vide\n";
        }
        if (tftype.getText().trim().isEmpty()) {
            erreur += "Type d'activité vide\n";
        }
        if (tfdescription.getText().trim().isEmpty()) {
            erreur += "Description vide\n";
        }
        if (tfactivity_price.getText().trim().isEmpty()) {
            erreur += "Prix de l'activité vide\n";
        } else {
            try {
                float price = Float.parseFloat(tfactivity_price.getText());
                if (price < 0) {
                    erreur += "Le prix de l'activité doit être un nombre positif\n";
                }
            } catch (NumberFormatException e) {
                erreur += "Le prix de l'activité doit être un nombre valide\n";
            }
        }
        if (dpDateActivite.getValue() == null) {
            erreur += "Date de l'activité non sélectionnée\n";
        }
        if (!erreur.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText("Veuillez corriger les erreurs suivantes :");
            alert.setContentText(erreur);
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private void viderChamps() {
        tfnom_activity.clear();
        tfimage_activity.clear();
        tfimage_activity2.clear();
        tfimage_activity3.clear();
        tftype.clear();
        tfdescription.clear();
        tfactivity_price.clear();
        dpDateActivite.setValue(null);
    }

    @FXML
    void supprimer(ActionEvent event) {
        Activity dest = tabActivity.getSelectionModel().getSelectedItem();
        if (dest != null) {
            try {
                as.delete(dest.getId_activity());
                refresh(as.showAll());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void modifier(ActionEvent event) {
        Activity selected = tabActivity.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setContentText("Veuillez sélectionner une activité à modifier.");
            alert.show();
            return;
        }
        if (!controle_saisie()) {
            return;
        }
        try {
            selected.setNom_activity(tfnom_activity.getText());
            selected.setImage_activity(tfimage_activity.getText());
            selected.setImage_activity2(tfimage_activity2.getText());
            selected.setImage_activity3(tfimage_activity3.getText());
            selected.setType(tftype.getText());
            selected.setDescription(tfdescription.getText());
            selected.setActivity_price(Float.parseFloat(tfactivity_price.getText()));
            // Convert selected DatePicker value
            LocalDate localDate = dpDateActivite.getValue();
            selected.setDateActivite(Date.valueOf(localDate));
            selected.setIdDestination(ds.getIdByPaysVille(cbdestination.getValue()));
            as.update(selected);
            refresh(as.showAll());
            tabActivity.refresh();
            viderChamps();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.show();
            e.printStackTrace();
        }
    }

    @FXML
    void gotoDestination(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/destination-back.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tfactivity_price.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Destination");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load Destination.fxml: " + e.getMessage());
        }
    }

    @FXML
    void tri(ActionEvent event) {
        try {
            refresh(as.triParCritere(cbtri.getValue()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void recherche_avance() {
        try {
            ObservableList<Activity> data = FXCollections.observableArrayList(as.showAll());
            FilteredList<Activity> filteredList = new FilteredList<>(data, b -> true);
            tfrechercher.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(activity -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (activity.getNom_activity().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (activity.getType().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (activity.getDescription().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (String.valueOf(activity.getActivity_price()).contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
                refresh(filteredList);
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void uploadImage(TextField targetField){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selectionner une image");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image File", "*.jpg", "*.png","*.jpeg", "*.gif")
        );
        Stage stage = (Stage) tfactivity_price.getScene().getWindow();
        File file=fileChooser.showOpenDialog(stage);
        if(file!=null){
            try {
                Path targetDir=Paths.get(IMAGE_FOLDER);
                if(!Files.exists(targetDir)){
                    Files.createDirectories(targetDir);
                }
                Path targetFile=targetDir.resolve(file.getName());
                Files.copy(file.toPath(),targetFile,StandardCopyOption.REPLACE_EXISTING);
                targetField.setText(file.getName());
            } catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur d'upload");
                alert.setContentText("Impossible d'uploader le fichier : " + ex.getMessage());
                alert.showAndWait();
            }
        }

    }
    @FXML
    void gotoDetails(ActionEvent event) {
        Activity selected = tabActivity.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setContentText("Veuillez sélectionner une activité à afficher en détails.");
            alert.show();
            return;
        }
        try {
            // Load the activity details FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/activity-details.fxml"));
            Parent root = loader.load();

            // Get the details controller and pass the selected activity
            ActivityDetailsController detailsController = loader.getController();
            detailsController.setActivity(selected);

            // Switch the scene to the details view
            Stage stage = (Stage) tfactivity_price.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Détails de l'activité");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Impossible de charger la vue des détails : " + e.getMessage());
            alert.show();
        }

    }

    @FXML
    void uploadImage1(ActionEvent event) {
        uploadImage(tfimage_activity);
    }

    @FXML
    void uploadImage2(ActionEvent event) {
        uploadImage(tfimage_activity2);
    }

    @FXML
    void uploadImage3(ActionEvent event) {
        uploadImage(tfimage_activity3);
    }
}
