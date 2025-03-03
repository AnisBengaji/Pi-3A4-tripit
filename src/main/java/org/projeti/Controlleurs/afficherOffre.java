package org.projeti.Controlleurs;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.SortEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.projeti.Service.OffreService;
import org.projeti.entites.Offre;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class afficherOffre {


    private final OffreService ps = new OffreService();
    @FXML
    private TableColumn<Offre, String> titre;
    @FXML
    private TableColumn<Offre, String> description;
    @FXML
    private TableColumn<Offre, Float> prix;
    @FXML
    private TableColumn<Offre, String> tutorial;
    @FXML
    private TableColumn<Offre, String> destination;
    @FXML
    private TableColumn<Offre, Void> modifier;
    @FXML
    private TableColumn<Offre, Void> supprimer;
    @FXML
    private TableView<Offre> tableView;
    @FXML
    public void initialize() {
        System.out.println("initialize() appelé");

        try {
            List<Offre> offres = ps.showAll();
            System.out.println("Offres récupérées: " + offres);

            ObservableList<Offre> observableList = FXCollections.observableList(offres);
            tableView.setItems(observableList);

            titre.setCellValueFactory(new PropertyValueFactory<>("titre"));
            description.setCellValueFactory(new PropertyValueFactory<>("description"));
            prix.setCellValueFactory(new PropertyValueFactory<>("prix"));
            tutorial.setCellValueFactory(new PropertyValueFactory<>("tutorial"));
            destination.setCellValueFactory(new PropertyValueFactory<>("destination"));

            System.out.println("TableView configurée avec succès.");

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de chargement");
            alert.setContentText("Impossible de charger les offres: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void afficherAjouterOffre(ActionEvent event) {
        try {
            // Charger le fichier FXML de l'interface AjouterOffre
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterOffre.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle et changer vers la nouvelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une Offre");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}





