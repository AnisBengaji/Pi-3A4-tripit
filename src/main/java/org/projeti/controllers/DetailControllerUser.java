package org.projeti.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.projeti.Service.UserService;
import org.projeti.entites.User;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DetailControllerUser {
    @FXML
    private TableView<User> tableView;
    @FXML
    private TableColumn<User, String> colNom;
    @FXML
    private TableColumn<User, String> colPrenom;
    @FXML
    private TableColumn<User, Integer> colTel;
    @FXML
    private TableColumn<User, String> colEmail;
    @FXML
    private TableColumn<User, String> colRole;
    @FXML
    private TableColumn<User, Void> colActions;
    @FXML
    private TableColumn<User, Void> colUpdate;

    private final UserService userService = new UserService();
    private final ObservableList<User> userList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("num_tel"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        addDeleteButtonToTable();
        addUpdateButtonToTable();
        loadUsers();
    }
    @FXML
    private void handleBackToMain(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
            Parent mainPage = loader.load();
            Scene mainScene = new Scene(mainPage);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(mainScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadUsers() {
        try {
            List<User> users = userService.showAll();
            userList.setAll(users);
            tableView.setItems(userList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addDeleteButtonToTable() {
        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = param -> new TableCell<>() {
            private final Button btnDelete = new Button("Supprimer");

            {
                btnDelete.setStyle("-fx-background-color: linear-gradient(to bottom, #FF742C, #FF9E6B); " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 12px; " +  // Smaller font size
                        "-fx-font-weight: normal; " + // Normal font weight
                        "-fx-padding: 8px 16px; " + // Reduced padding
                        "-fx-background-radius: 5px;");

                btnDelete.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    deleteUser(user);
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
        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = param -> new TableCell<>() {
            private final Button btnUpdate = new Button("Modifier");

            {
                btnUpdate.setStyle("-fx-background-color: linear-gradient(to bottom, #007A8C, #66b2b2); " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 12px; " +  // Same smaller font size as btnDelete
                        "-fx-font-weight: normal; " + // Normal font weight like btnDelete
                        "-fx-padding: 8px 16px; " + // Same reduced padding as btnDelete
                        "-fx-background-radius: 5px;"); // Same background radius


                btnUpdate.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    openUpdateWindow(user);
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

    private void openUpdateWindow(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateUser.fxml"));
            Parent root = loader.load();

            org.projeti.controllers.UpdateUser controller = loader.getController();
            controller.setUserData(user, this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier l'utilisateur");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger la page de modification.");
        }
    }



    private void deleteUser(User user) {
        try {
            userService.delete(user);
            userList.remove(user);
            showAlert("Succès", "Utilisateur supprimé avec succès.");
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la suppression.");
        }
    }

    public void refreshTable() {
        loadUsers();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
