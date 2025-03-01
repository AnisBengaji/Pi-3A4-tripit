package org.projeti.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.projeti.Service.CategorieService;
import org.projeti.entites.Categorie;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
// Existing imports you likely already have
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
// Apache POI imports
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

// Java IO imports (for file handling)
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


// New imports needed for PDF generation
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

// Utility imports
import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

public class DetailCategoryController implements Initializable {

    @FXML
    private TableView<Categorie> categoriesTableView;

    @FXML
    private TableColumn<Categorie, Integer> categoryIdColumn;

    @FXML
    private TableColumn<Categorie, String> categoryNameColumn;

    @FXML
    private TableColumn<Categorie, String> descriptionColumn;

    @FXML
    private TableColumn<Categorie, String> publicationsIdColumn;

    @FXML
    private TableColumn<Categorie, Void> actionsColumn;

    @FXML
    private Button buttonreturn;

    private final CategorieService categorieService = new CategorieService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        loadCategories();
    }

    private void setupTableColumns() {
        categoryIdColumn.setCellValueFactory(new PropertyValueFactory<>("idCategorie"));
        categoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("nomCategorie"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Configure publicationsIdColumn to display publication IDs
        publicationsIdColumn.setCellValueFactory(cellData -> {
            Categorie categorie = cellData.getValue();
            String publicationIds = "No Publications";  // Default message

            // Check if the publications are correctly retrieved
            if (categorie.getPublications() != null && !categorie.getPublications().isEmpty()) {
                publicationIds = categorie.getPublications().stream()
                        .map(publication -> String.valueOf(publication.getId_publication()))
                        .collect(Collectors.joining(", "));
            }

            return new javafx.beans.property.SimpleStringProperty(publicationIds);
        });

        actionsColumn.setCellFactory(createActionsCellFactory());
    }

    private Callback<TableColumn<Categorie, Void>, TableCell<Categorie, Void>> createActionsCellFactory() {
        return param -> new TableCell<>() {
            private final Button updateButton = new Button();
            private final Button deleteButton = new Button();

            {

                ImageView updateIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/newedit.png")));
                updateIcon.setFitWidth(20);
                updateIcon.setFitHeight(20);

                ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/newdel.png")));
                deleteIcon.setFitWidth(20);
                deleteIcon.setFitHeight(20);


                updateButton.setGraphic(updateIcon);
                deleteButton.setGraphic(deleteIcon);


                updateButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
                deleteButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");


                updateButton.setOnAction(event -> {
                    Categorie categorie = getTableView().getItems().get(getIndex());
                    updateCategory(categorie);
                });

                deleteButton.setOnAction(event -> {
                    Categorie categorie = getTableView().getItems().get(getIndex());
                    deleteCategory(categorie);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttonContainer = new HBox(15, updateButton, deleteButton);
                    buttonContainer.setStyle("-fx-alignment: center;");
                    setGraphic(buttonContainer);
                }
            }
        };
    }

    private void updateCategory(Categorie categorie) {
        // Open a dialog to update the category name
        TextInputDialog dialog = new TextInputDialog(categorie.getNomCategorie());
        dialog.setTitle("mise a jour cateogir");
        dialog.setHeaderText("Modifier le nom et la description de la catégorie\"");
        dialog.setContentText("entrez un nouveau nom ::");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newName -> {

            TextInputDialog descriptionDialog = new TextInputDialog(categorie.getDescription());
            descriptionDialog.setTitle("modifier la description");
            descriptionDialog.setHeaderText("Moodifier la description");
            descriptionDialog.setContentText("Entrer une description:");

            Optional<String> descriptionResult = descriptionDialog.showAndWait();
            descriptionResult.ifPresent(newDescription -> {

                if (controlSaisie(newName, newDescription)) {
                    categorie.setNomCategorie(newName);
                    categorie.setDescription(newDescription);
                    try {
                        categorieService.update(categorie);
                        loadCategories();
                        showAlert(Alert.AlertType.INFORMATION, "Success", "categorie mis a jour avec s!");
                    } catch (SQLException e) {
                        showAlert(Alert.AlertType.ERROR, "Error", "echeck du mise a jour.");
                        e.printStackTrace();
                    }
                }
            });
        });
    }


    private boolean controlSaisie(String nomCategorie, String description) {
        // Check if the name and description are only letters and spaces
        if (!nomCategorie.matches("[a-zA-Z\\s]+") || !description.matches("[a-zA-Z\\s]+")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Le nom et la description de la catégorie ne doivent contenir que des lettres et des espaces.");
            return false;
        }


        try {
            if (categorieService.exists(nomCategorie)) {
                showAlert(Alert.AlertType.ERROR, "Error", "le nom de la catégorie existe déjà!");
                return false;
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Erreur lors de la vérification de l'unicité du nom de catégorie.");
            return false;
        }

        return true;
    }

    private void deleteCategory(Categorie categorie) {
        try {
            if (categorie.getPublications() == null || categorie.getPublications().isEmpty()) {
                categorieService.delete(categorie);
                loadCategories();
                showAlert(Alert.AlertType.INFORMATION, "Success", "categorie supprimer avec succes! :D");
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "Impossible de supprimer une catégorie avec des publications.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Échec de la suppression de la catégorie. :(");
            e.printStackTrace();
        }
    }

    private void loadCategories() {
        try {
            List<Categorie> categories = categorieService.showAll();
            ObservableList<Categorie> observableCategories = FXCollections.observableArrayList(categories);
            categoriesTableView.setItems(observableCategories);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load categories.");
            throw new RuntimeException(e);
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void returnHomeCat() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomeCat.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) buttonreturn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Categories!");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load HomeCat.fxml: " + e.getMessage());
        }
    }
    @FXML
    private void downloadCategoryPdf() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (PDDocument document = new PDDocument()) {
                createCategoriesPdf(document, categoriesTableView.getItems());
                document.save(file);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Categories report saved successfully!");
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to generate PDF: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void createCategoriesPdf(PDDocument document, ObservableList<Categorie> categories) throws IOException {
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Initial y position for content
        int y = 750;
        boolean isFirstPage = true;

        // Process each category
        for (int i = 0; i < categories.size(); i++) {
            // If we need a new page (not enough vertical space or first item on next page)
            if (y < 100) {
                // Close current content stream
                contentStream.close();

                // Create a new page
                page = new PDPage();
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);

                // Reset position
                y = 750;
                isFirstPage = false;
            }

            // Draw header on first page or new pages
            if ((i == 0) || (y == 750 && !isFirstPage)) {
                y = drawHeader(document, contentStream, y);
            }

            // Draw category data
            y = drawCategoryRow(contentStream, categories.get(i), y);
        }

        // Draw footer
        drawFooter(contentStream);

        // Close the content stream
        contentStream.close();
    }

    private int drawHeader(PDDocument document, PDPageContentStream contentStream, int y) throws IOException {
        // Load the logo/image
        PDImageXObject logo = PDImageXObject.createFromFile("src/main/resources/icons/iconTripit.png", document);

        // Draw the logo and app name
        contentStream.drawImage(logo, 50, y - 50, 100, 50);
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 24);
        contentStream.newLineAtOffset(160, y - 20);
        contentStream.showText("Trip It - Categories Report");
        contentStream.endText();

        // Add a separator line
        contentStream.setLineWidth(1);
        contentStream.moveTo(50, y - 60);
        contentStream.lineTo(550, y - 60);
        contentStream.stroke();

        // Table headers
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.newLineAtOffset(50, y - 80);
        contentStream.showText("ID");
        contentStream.newLineAtOffset(50, 0);
        contentStream.showText("Category Name");
        contentStream.newLineAtOffset(150, 0);
        contentStream.showText("Description");
        contentStream.newLineAtOffset(150, 0);
        contentStream.showText("Publications");
        contentStream.endText();

        // Header separator line
        contentStream.setLineWidth(0.5f);
        contentStream.moveTo(50, y - 85);
        contentStream.lineTo(550, y - 85);
        contentStream.stroke();

        // Return new y position
        return y - 100;
    }

    private int drawCategoryRow(PDPageContentStream contentStream, Categorie categorie, int y) throws IOException {
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        contentStream.newLineAtOffset(50, y);
        contentStream.showText(String.valueOf(categorie.getIdCategorie()));
        contentStream.newLineAtOffset(50, 0);

        // Truncate category name if too long
        String categoryName = categorie.getNomCategorie();
        if (categoryName.length() > 20) {
            categoryName = categoryName.substring(0, 17) + "...";
        }
        contentStream.showText(categoryName);

        contentStream.newLineAtOffset(150, 0);

        // Truncate description if too long
        String description = categorie.getDescription();
        if (description.length() > 25) {
            description = description.substring(0, 22) + "...";
        }
        contentStream.showText(description);

        contentStream.newLineAtOffset(150, 0);

        // Get publication IDs
        String publicationIds = "None";
        if (categorie.getPublications() != null && !categorie.getPublications().isEmpty()) {
            publicationIds = categorie.getPublications().stream()
                    .map(publication -> String.valueOf(publication.getId_publication()))
                    .collect(Collectors.joining(", "));

            // Truncate if too long
            if (publicationIds.length() > 20) {
                publicationIds = publicationIds.substring(0, 17) + "...";
            }
        }
        contentStream.showText(publicationIds);
        contentStream.endText();

        // Draw a line under each row
        contentStream.setLineWidth(0.5f);
        contentStream.moveTo(50, y - 5);
        contentStream.lineTo(550, y - 5);
        contentStream.stroke();

        // Return new y position (20 points down)
        return y - 20;
    }

    private void drawFooter(PDPageContentStream contentStream) throws IOException {
        // Footer
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_OBLIQUE, 10);
        contentStream.newLineAtOffset(50, 30);
        contentStream.showText("Thank you for using Trip It!");
        contentStream.endText();

        // Add date of report
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        contentStream.newLineAtOffset(400, 30);
        contentStream.showText("Report generated: " + java.time.LocalDate.now().toString());
        contentStream.endText();
    }


    @FXML
    private void downloadCategoryExcel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Categories");

                // Create header row
                Row headerRow = sheet.createRow(0);
                String[] columns = {"ID", "Category Name", "Description", "Publications"};
                for (int i = 0; i < columns.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columns[i]);
                }

                int rowNum = 1;
                for (Categorie categorie : categoriesTableView.getItems()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(categorie.getIdCategorie());
                    row.createCell(1).setCellValue(categorie.getNomCategorie());
                    row.createCell(2).setCellValue(categorie.getDescription());

                    // Fetch publication IDs
                    String publicationIds = "None";
                    if (categorie.getPublications() != null && !categorie.getPublications().isEmpty()) {
                        publicationIds = categorie.getPublications().stream()
                                .map(publication -> String.valueOf(publication.getId_publication()))
                                .collect(Collectors.joining(", "));
                    }
                    row.createCell(3).setCellValue(publicationIds);
                }

                // Auto-size columns for better readability
                for (int i = 0; i < columns.length; i++) {
                    sheet.autoSizeColumn(i);
                }

                // Write to file
                try (FileOutputStream fileOut = new FileOutputStream(file)) {
                    workbook.write(fileOut);
                }

                showAlert(Alert.AlertType.INFORMATION, "Success", "Excel file saved successfully!");
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save Excel file.");
                e.printStackTrace();
            }
        }
    }

}
