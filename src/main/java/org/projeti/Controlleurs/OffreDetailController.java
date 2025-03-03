package org.projeti.Controlleurs;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.projeti.Service.OffreService;
import org.projeti.entites.Offre;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OffreDetailController {
    @FXML
    private TableView<Offre> tableView;
    @FXML
    private TableColumn<Offre, String> colTitre;
    @FXML
    private TableColumn<Offre, String> colDescription;
    @FXML
    private TableColumn<Offre, Float> colPrix;
    @FXML
    private TableColumn<Offre, String> colTutorial;
    @FXML
    private TableColumn<Offre, String> colDestination;
    @FXML
    private TableColumn<Offre, Void> colActions;
    @FXML
    private TableColumn<Offre, Void> colUpdate;

    private final OffreService offreService = new OffreService();
    private final ObservableList<Offre> offreList = FXCollections.observableArrayList();
    private String destinationPrioritaire = null; // Stocke la dernière destination sélectionnée

        @FXML
        public void initialize() {
            colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
            colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
            colTutorial.setCellValueFactory(new PropertyValueFactory<>("tutorial"));
            colDestination.setCellValueFactory(new PropertyValueFactory<>("destination"));

            addDeleteButtonToTable();
            addUpdateButtonToTable();
            loadOffres(null); // Chargement initial sans priorité

            // Événement pour trier par destination lorsque l'utilisateur clique sur une offre
            tableView.setOnMouseClicked(event -> {
                Offre selectedOffre = tableView.getSelectionModel().getSelectedItem();
                if (selectedOffre != null) {
                    destinationPrioritaire = selectedOffre.getDestination();
                    loadOffres(destinationPrioritaire);
                }
            });
        }

        public void loadOffres(String destinationPrioritaire) {
            try {
                List<Offre> offres = offreService.getAll();

                // Séparer les offres selon la destination prioritaire
                List<Offre> offresPrioritaires = new ArrayList<>();
                List<Offre> autresOffres = new ArrayList<>();

                for (Offre offre : offres) {
                    if (destinationPrioritaire != null && offre.getDestination().equalsIgnoreCase(destinationPrioritaire)) {
                        offresPrioritaires.add(offre);
                    } else {
                        autresOffres.add(offre);
                    }
                }

                // Fusionner les listes : les offres prioritaires en premier
                offresPrioritaires.addAll(autresOffres);
                offreList.setAll(offresPrioritaires);
                tableView.setItems(offreList);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private void addDeleteButtonToTable() {
            Callback<TableColumn<Offre, Void>, TableCell<Offre, Void>> cellFactory = param -> new TableCell<>() {
                private final Button btnDelete = new Button("Supprimer");

                {
                    btnDelete.setStyle("-fx-background-color: #FF742C; -fx-text-fill: white;");
                    btnDelete.setOnAction(event -> {
                        Offre offre = getTableView().getItems().get(getIndex());
                        deleteOffre(offre);
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
            Callback<TableColumn<Offre, Void>, TableCell<Offre, Void>> cellFactory = param -> new TableCell<>() {
                private final Button btnUpdate = new Button("Modifier");

                {
                    btnUpdate.setStyle("-fx-background-color: #007A8C; -fx-text-fill: white;");
                    btnUpdate.setOnAction(event -> {
                        Offre offre = getTableView().getItems().get(getIndex());
                        openUpdateWindow(offre);
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

        private void openUpdateWindow(Offre offre) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierOffre.fxml"));
                Parent root = loader.load();
                ModifierOffreController controller = loader.getController();
                controller.setOffreData(offre, this);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier l'offre");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            } catch (IOException e) {
                showAlert("Erreur", "Impossible de charger la page de modification.");
            }
        }

        private void deleteOffre(Offre offre) {
            try {
                offreService.delete(offre);
                offreList.remove(offre);
                showAlert("Succès", "Offre supprimée avec succès.");
            } catch (SQLException e) {
                showAlert("Erreur", "Erreur lors de la suppression.");
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
            loadOffres(destinationPrioritaire);
        }
    }