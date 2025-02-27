package org.projeti.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.projeti.Service.CategorieService;
import org.projeti.entites.Categorie;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class HomeCategoryController {

    @FXML
    private Button btnDetail;
    @FXML
    private Button btnAjouter;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button switchToPubButton;
    @FXML
    private ListView<Categorie> categoriesListView;
    /*@FXML
    private BorderPane root; */

    @FXML
    private AnchorPane root;


    private CategorieService categorieService = new CategorieService();
    private ObservableList<Categorie> categories = FXCollections.observableArrayList();
    private Categorie selectedCategory;

    public void initialize() {
        try {
            loadCategories();
            Image image = new Image(getClass().getResourceAsStream("/icons/post1.png"));
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(30);
            imageView.setFitWidth(30);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void loadCategories() throws SQLException {
        List<Categorie> categorieList = categorieService.showAll();
        categories.setAll(categorieList);
        categoriesListView.setItems(categories);


        categoriesListView.setCellFactory(param -> new CategorieListCell());
    }

    @FXML
    private void goToDetailCategorie() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailCat.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnDetail.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Category Details");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load Detail.fxml: " + e.getMessage());
        }
    }

    @FXML
    private void goToDetailCategory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailCat.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnDetail.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("crud Categories");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load DetailCat.fxml: " + e.getMessage());
        }
    }

    @FXML
    private void switchToHomePub() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/homePub.fxml"));
            Parent homePubView = loader.load();


            Stage stage = (Stage) switchToPubButton.getScene().getWindow();
            Scene scene = new Scene(homePubView); // New scene with homeCat.fxml
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading homeCat.fxml: " + e.getMessage());
        }
    }
    @FXML
    private void goToAjouterCat() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterCategorie.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnAjouter.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("ajouter");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load ajouterCategorie.fxml: " + e.getMessage());
        }
    }
}
