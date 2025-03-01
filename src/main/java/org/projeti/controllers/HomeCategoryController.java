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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.projeti.Service.CategorieService;
import org.projeti.entites.Categorie;
import org.projeti.entites.Publication;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
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
    /*@FXML
    private BorderPane root; */
    @FXML
    private TextField searchTextField;
    @FXML
    private ComboBox<String> sortComboBox;
    @FXML
    private ComboBox<String> languageComboBox; // Add this line
    private FilteredList<Categorie> filteredCategories;
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
    private void setupIncrementalSearch() {
        // Create a filtered list that will initially display all categories
        filteredCategories = new FilteredList<>(categories, c -> true);

        // Add a listener to the search text field
        categorySearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredCategories.setPredicate(category -> {
                // If search field is empty, show all categories
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Check if category name or description contains the search term
                if (category.getNomCategorie().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (category.getDescription() != null && category.getDescription().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                // Check if any of the publications in this category match the filter
                if (category.getPublications() != null) {
                    for (Publication publication : category.getPublications()) {
                        if (publication.getTitle().toLowerCase().contains(lowerCaseFilter) ||
                                (publication.getAuthor() != null && publication.getAuthor().toLowerCase().contains(lowerCaseFilter)) ||
                                (publication.getContenu() != null && publication.getContenu().toLowerCase().contains(lowerCaseFilter))) {
                            return true;
                        }
                    }
                }
                return false;
            });
        });

        // Wrap filtered list in a sorted list
        SortedList<Categorie> sortedCategories = new SortedList<>(filteredCategories,
                Comparator.comparing(Categorie::getNomCategorie) // Sort by category name
        );

        // Set the sorted list to the ListView
        categoriesListView.setItems(sortedCategories);
    }

}
