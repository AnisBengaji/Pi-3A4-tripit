package org.projeti.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.projeti.Service.CategorieService;
import org.projeti.entites.Categorie;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class HomeCategoryController {
    @FXML
    private Button categorySearchTextField;
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
    @FXML
    private TextField searchTextField;
    @FXML
    private ComboBox<String> sortComboBox;
    @FXML
    private ComboBox<String> languageComboBox;
    @FXML
    private AnchorPane root;

    private CategorieService categorieService = new CategorieService();
    private ObservableList<Categorie> categories = FXCollections.observableArrayList();
    private FilteredList<Categorie> filteredCategories;

    public void initialize() {
        try {
            loadCategories();
            setupSearchFilter(); // Initialize search functionality
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadCategories() throws SQLException {
        List<Categorie> categorieList = categorieService.showAll();
        categories.setAll(categorieList);

        // Wrap categories in a FilteredList
        filteredCategories = new FilteredList<>(categories, p -> true);

        // Bind the filtered list to the ListView
        SortedList<Categorie> sortedCategories = new SortedList<>(filteredCategories);
        categoriesListView.setItems(sortedCategories);

        categoriesListView.setCellFactory(param -> new CategorieListCell());
    }

    private void setupSearchFilter() {
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredCategories.setPredicate(category -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return category.getNomCategorie().toLowerCase().contains(lowerCaseFilter);
            });
        });
    }

    @FXML
    private void goToDetailCategory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailCat.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnDetail.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Category Details");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToHomePub() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/homePub.fxml"));
            Parent homePubView = loader.load();
            Stage stage = (Stage) switchToPubButton.getScene().getWindow();
            stage.setScene(new Scene(homePubView));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToAjouterCat() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterCategorie.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnAjouter.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter Categorie");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
