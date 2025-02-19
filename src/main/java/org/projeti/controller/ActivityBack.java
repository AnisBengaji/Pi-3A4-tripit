package org.projeti.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.projeti.Service.ActivityService;
import org.projeti.Service.DestinationService;
import org.projeti.entites.Activity;
import org.projeti.entites.Destination;

import java.io.IOException;
import java.sql.SQLException;

public class ActivityBack {

    @FXML
    private Button btnajout;

    @FXML
    private Button btnsup;


    @FXML
    private ComboBox<String> cbdestination;



    @FXML
    private TableColumn<Activity, ?> colactivity_price;

    @FXML
    private TableColumn<Activity, ?> coldescription;

    @FXML
    private TableColumn<Activity, String> colidDestination;

    @FXML
    private TableColumn<Activity, ?> colimage_activity;

    @FXML
    private TableColumn<Activity, ?> colnom_activity;

    @FXML
    private TableColumn<Activity, ?> coltype;

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
    private TextField tfnom_activity;

    @FXML
    private TextField tftype;

    ObservableList<Activity> obslist;
    private final ActivityService as = new ActivityService();
    private final DestinationService ds=new DestinationService();
    @FXML
    public void initialize() {
        try {
            cbdestination.setItems(FXCollections.observableArrayList(ds.getPaysVille()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (tabActivity == null) {
            System.err.println("Erreur : tabActivity est null. Vérifiez le fichier FXML.");
        } else {
            tabActivity.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            refresh();
        }
    }

    void refresh() {
        if (tabActivity == null) {
            System.err.println("Erreur : tabActivity est null. Vérifiez le fichier FXML.");
            return;
        }
        try {
            colnom_activity.setCellValueFactory(new PropertyValueFactory<>("nom_activity"));
            colimage_activity.setCellValueFactory(new PropertyValueFactory<>("image_activity"));
            coltype.setCellValueFactory(new PropertyValueFactory<>("type"));
            coldescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            colactivity_price.setCellValueFactory(new PropertyValueFactory<>("activity_price"));
            colidDestination.setCellValueFactory(cell->{
                int id=cell.getValue().getIdDestination();
                try {
                    String payville=ds.getPaysVilleById(id);
                    return new SimpleStringProperty(payville);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            obslist = FXCollections.observableArrayList(as.showAll());
            System.out.println("Données récupérées : " + obslist.size() + " activités trouvées");

            tabActivity.setItems(null);
            tabActivity.setItems(obslist);

            if (obslist.isEmpty()) {
                System.err.println("Aucune activité trouvée dans la base de données.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ajouter(ActionEvent event) {
        if (!controle_saisie()) {
            return;
        }

        try {
            Activity a = new Activity(tfnom_activity.getText(),tfimage_activity.getText(),tftype.getText(),tfdescription.getText(),Float.parseFloat(tfactivity_price.getText()),ds.getIdByPaysVille(cbdestination.getValue()));

            as.insert(a);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setContentText("User insérée avec succés!");
            alert.show();

            refresh();

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
        tftype.clear();
        tfdescription.clear();
        tfactivity_price.clear();
    }
    @FXML
    void supprimer(ActionEvent event) {
        Activity dest=tabActivity.getSelectionModel().getSelectedItem();
        if(dest!=null){
            try {
                as.delete(dest.getId_activity());
                refresh();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @FXML
    void modifier(ActionEvent event) {

        Activity selected=tabActivity.getSelectionModel().getSelectedItem();
        if(selected==null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setContentText("Veuillez sélectionner une activité à modifier.");
            alert.show();
            return;
        }
        if (!controle_saisie()) {
            return;
        }
        try{
            selected.setNom_activity(tfnom_activity.getText());
            selected.setImage_activity(tfimage_activity.getText());
            selected.setType(tftype.getText());
            selected.setDescription(tfdescription.getText());
            selected.setActivity_price(Float.parseFloat(tfactivity_price.getText()));
            selected.setIdDestination(ds.getIdByPaysVille(cbdestination.getValue()));
            as.update(selected);
            refresh();
            tabActivity.refresh();
            viderChamps();
        }catch (SQLException e) {
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
}
