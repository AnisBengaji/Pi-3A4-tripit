/*package org.projeti.Controlleurs;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.projeti.Service.EvenementService;
import org.projeti.entites.Evenement;


import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
/*public class EvenementController {

    @FXML
    private TextField txtType;
    @FXML
    private DatePicker txtDateEvenement;
    @FXML
    private TextField txtLieu;
    @FXML
    private TextArea txtDescription;
    @FXML
    private TextField txtPrice;

    @FXML
    private TableView<Evenement> tableEvenement;
    @FXML
    private TableColumn<Evenement, Integer> colId;
    @FXML
    private TableColumn<Evenement, String> colType;
    @FXML
    private TableColumn<Evenement, String> colDate;
    @FXML
    private TableColumn<Evenement, String> colLieu;
    @FXML
    private TableColumn<Evenement, String> colDescription;
    @FXML
    private TableColumn<Evenement, Float> colPrice;

    private ObservableList<Evenement> evenementList = FXCollections.observableArrayList();
    private EvenementService evenementService;


    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId_Evenement()).asObject());
        colType.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getType()));
        colDate.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDate_Evenement()));
        colLieu.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getLieu()));
        colDescription.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription()));
        colPrice.setCellValueFactory(cellData -> new javafx.beans.property.SimpleFloatProperty(cellData.getValue().getPrice()).asObject());

        loadEvenements();
    }

    private void loadEvenements() {
        evenementList.clear();
        List<Evenement> evenements = evenementService.getAll();
        evenementList.addAll(evenements);
        tableEvenement.setItems(evenementList);
    }

    @FXML
    private void addEvenement(ActionEvent event) {
        String type = txtType.getText();
        LocalDate date = txtDateEvenement.getValue();
        String lieu = txtLieu.getText();
        String description = txtDescription.getText();
        float price;

        try {
            price = Float.parseFloat(txtPrice.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le prix doit être un nombre valide.");
            return;
        }

        if (type.isEmpty() || date == null || lieu.isEmpty() || description.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        Evenement evenement = new Evenement(0, type, date.toString(), lieu, description, price);
        evenementService.add(evenement);
        loadEvenements();
        clearFields();
    }

    @FXML
    private void updateEvenement(ActionEvent event) {
        Evenement selected = tableEvenement.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner un événement à modifier.");
            return;
        }

        String type = txtType.getText();
        LocalDate date = txtDateEvenement.getValue();
        String lieu = txtLieu.getText();
        String description = txtDescription.getText();
        float price;

        try {
            price = Float.parseFloat(txtPrice.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le prix doit être un nombre valide.");
            return;
        }

        if (type.isEmpty() || date == null || lieu.isEmpty() || description.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        selected.setType(type);
        selected.setDate_Evenement(date.toString());
        selected.setLieu(lieu);
        selected.setDescription(description);
        selected.setPrice(price);

        evenementService.update(selected);
        loadEvenements();
        clearFields();
    }

    @FXML
    private void deleteEvenement(ActionEvent event) {
        Evenement selected = tableEvenement.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner un événement à supprimer.");
            return;
        }

        evenementService.delete(selected.getId_Evenement());
        loadEvenements();
        clearFields();
    }

    @FXML
    private void selectEvenement(MouseEvent event) {
        Evenement selected = tableEvenement.getSelectionModel().getSelectedItem();
        if (selected != null) {
            txtType.setText(selected.getType());
            txtDateEvenement.setValue(LocalDate.parse(selected.getDate_Evenement()));
            txtLieu.setText(selected.getLieu());
            txtDescription.setText(selected.getDescription());
            txtPrice.setText(String.valueOf(selected.getPrice()));
        }
    }

    @FXML
    private void clearFields() {
        txtType.clear();
        txtDateEvenement.setValue(null);
        txtLieu.clear();
        txtDescription.clear();
        txtPrice.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}*/