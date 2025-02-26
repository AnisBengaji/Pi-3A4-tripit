package org.projeti.controller;

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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.projeti.Service.DestinationService;
import org.projeti.entites.Activity;
import org.projeti.entites.Destination;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DestinationBack {
@FXML
    public Button btngoto;
    @FXML
    private TableColumn<Destination, Integer> colcodepostal;

    @FXML
    private TableColumn<Destination, Float> collat;

    @FXML
    private TableColumn<Destination, Float> collong;

    @FXML
    private TableColumn<Destination, String> colpays;

    @FXML
    private TableColumn<Destination, String> colville;

    @FXML
    private TableView<Destination> tabDestination;

    @FXML
    private TextField tfcode_postal;

    @FXML
    private TextField tflat;

    @FXML
    private TextField tflong;

    @FXML
    private TextField tfpays;
    @FXML
    private ComboBox<String> cbtri;
    @FXML
    private TextField tfrechercher;
    @FXML
    private TextField tfville;
    ObservableList<Destination> obslist;
    DestinationService ds=new DestinationService();
    public void initialize() {
        try {
            refresh(ds.showAll());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        cbtri.setItems(FXCollections.observableArrayList("Ville","Pays","Code postal"));
        recherche_avance();
    }
    void refresh(List<Destination> destinationList){

            colcodepostal.setCellValueFactory(new PropertyValueFactory<>("code_postal"));
            collat.setCellValueFactory(new PropertyValueFactory<>("latitude"));
            collong.setCellValueFactory(new PropertyValueFactory<>("longitude"));
            colpays.setCellValueFactory(new PropertyValueFactory<>("pays"));
            colville.setCellValueFactory(new PropertyValueFactory<>("ville"));
            obslist= FXCollections.observableArrayList(destinationList);
            tabDestination.setItems(obslist);

    }

    @FXML
    void ajouter(ActionEvent event) {
        if(!controle_saisie()){
            return;
        }
        Destination dest=new Destination();
        dest.setPays(tfpays.getText());
        dest.setVille(tfville.getText());
        dest.setCode_postal(Integer.parseInt(tfcode_postal.getText()));
        dest.setLatitude(Float.parseFloat(tflat.getText()));
        dest.setLongitude(Float.parseFloat(tflong.getText()));
        try {
            ds.insert(dest);
            refresh(ds.showAll());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @FXML
    void modifier(ActionEvent event) {
        if(!controle_saisie()){
            return;
        }
        Destination dest=tabDestination.getSelectionModel().getSelectedItem();
        if(dest!=null){
            try {
                dest.setPays(tfpays.getText());
                dest.setVille(tfville.getText());
                dest.setCode_postal(Integer.parseInt(tfcode_postal.getText()));
                dest.setLatitude(Float.parseFloat(tflat.getText()));
                dest.setLongitude(Float.parseFloat(tflong.getText()));
                ds.update(dest);
                refresh(ds.showAll());
                tabDestination.refresh();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


    }

    @FXML
    void supprimer(ActionEvent event) {
        Destination dest=tabDestination.getSelectionModel().getSelectedItem();
        if(dest!=null){
            try {
                ds.delete(dest.getId_Destination());
                refresh(ds.showAll());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }
    boolean controle_saisie(){
        String erreur="";
        if(tfpays.getText().trim().isEmpty()){
            erreur+="Pays vide\n";
        }
        if(tflat.getText().trim().isEmpty()){
            erreur+="Latitude vide\n";
        }else{
            try{
                Float.parseFloat(tflat.getText());
            }catch(NumberFormatException e){
                erreur+="Code postal doit etre un nombre entier\n";
            }
        }
        if(tfcode_postal.getText().trim().isEmpty()){
            erreur+="Code postal vide\n";
        }else{
            try{
                Integer.parseInt(tfcode_postal.getText());
            }catch(NumberFormatException e){
                erreur+="Code postal doit etre un nombre entier\n";
            }
        }
        if(tflong.getText().trim().isEmpty()){
            erreur+="Longitude vide\n";
        }else{
            try{
                Float.parseFloat(tflong.getText());
            }catch(NumberFormatException e){
                erreur+="Code postal doit etre un nombre entier\n";
            }
        }
        if(tfville.getText().trim().isEmpty()){
            erreur+="Ville vide\n";
        }
        if(tfpays.getText().length()<3){
            erreur+="Pays min 3 charcater\n";
        }
        if(!erreur.isEmpty()){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText("Veuillez corriger les erreurs suivantes :");
            alert.setContentText(erreur);
            alert.showAndWait();
            return false;
        }
        return true;

    }
    @FXML
    private void GoToActivity() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/activity-back.fxml"));

            Parent root = loader.load();
            Stage stage = (Stage) btngoto.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("activity");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load ActivityBack.fxml: " + e.getMessage());
        }
    }
    @FXML
    void fillForum(MouseEvent event) {
        Destination dest=tabDestination.getSelectionModel().getSelectedItem();
        tflat.setText(dest.getLatitude()+"");
        tflong.setText(dest.getLongitude()+"");
        tfcode_postal.setText(dest.getCode_postal()+"");
        tfville.setText(dest.getVille());
        tfpays.setText(dest.getPays());

    }
    @FXML
    void tri(ActionEvent event) {
        try {
            refresh(ds.triParCritere(cbtri.getValue()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void recherche_avance(){
        ObservableList<Destination> data= null;
        try {
            data = FXCollections.observableArrayList(ds.showAll());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        FilteredList<Destination> filteredList=new FilteredList<>(data);
        tfrechercher.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(
                    d->{
                        if(newValue==null || newValue.isEmpty()){
                            return true;
                        } else if (d.getPays().toLowerCase().contains(newValue.toLowerCase())) {
                            return true;
                        } else if (d.getVille().toLowerCase().contains(newValue.toLowerCase())) {
                        return true;
                        } else if (String.valueOf(d.getCode_postal()).contains(newValue.toLowerCase())) {
                            return true;
                        } else{
                            return false;
                        }
                    }
            );
            refresh(filteredList);

        });
    }

}
