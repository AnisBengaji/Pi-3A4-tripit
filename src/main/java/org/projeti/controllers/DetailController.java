package org.projeti.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.poi.ss.usermodel.Cell;
import org.projeti.Service.PublicationService;
import org.projeti.entites.Publication;
import org.projeti.entites.Categorie;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import java.io.File;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class DetailController {

    @FXML private TableView<Publication> publicationsTableView;
    @FXML private TableColumn<Publication, Integer> idColumn;
    @FXML private TableColumn<Publication, String> titleColumn;
    @FXML private TableColumn<Publication, String> contentColumn;
    @FXML private TableColumn<Publication, String> authorColumn;
    @FXML private TableColumn<Publication, String> dateColumn;
    @FXML private TableColumn<Publication, String> visibilityColumn;
    @FXML private TableColumn<Publication, String> imageColumn;
    @FXML private TableColumn<Publication, String> idCategorie;
    @FXML private TableColumn<Publication, Void> actionColumn;


    @FXML private Button buttonreturn;

    private PublicationService publicationService = new PublicationService();
    private ObservableList<Publication> publications = FXCollections.observableArrayList();

    public void initialize() {
        setupTable();
        try {
            loadPublications();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id_publication"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date_publication"));
        visibilityColumn.setCellValueFactory(new PropertyValueFactory<>("visibility"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));

        idCategorie.setCellValueFactory(cellData -> {
            Categorie categorie = cellData.getValue().getCategorie();
            if (categorie != null) {
                return new SimpleStringProperty(categorie.getIdCategorie() + " - " + categorie.getNomCategorie());
            } else {
                return new SimpleStringProperty("No Category");
            }
        });

        // Add action column with delete button
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button();
            private final Button deleteButton = new Button();

            {

                ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/newedit.png")));
                editIcon.setFitHeight(20);
                editIcon.setFitWidth(20);
                editButton.setGraphic(editIcon);
                editButton.getStyleClass().add("edit-button");


                ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/newdel.png")));
                deleteIcon.setFitHeight(20);
                deleteIcon.setFitWidth(20);
                deleteButton.setGraphic(deleteIcon);
                deleteButton.getStyleClass().add("delete-button");


                editButton.setOnAction(event -> {
                    Publication publication = getTableView().getItems().get(getIndex());
                    showEditDialog(publication);
                });


                deleteButton.setOnAction(event -> {
                    Publication publication = getTableView().getItems().get(getIndex());
                    deletePublication(publication);
                });


                HBox buttonsBox = new HBox(10, editButton, deleteButton);
                setGraphic(buttonsBox);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(getGraphic());
                }
            }
        });
    }


    private void deletePublication(Publication publication) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete this publication?");
        alert.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                publicationService.delete(publication);
                publications.remove(publication); // Remove from table
                publicationsTableView.refresh();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to delete publication: " + e.getMessage());
            }
        }
    }


    private void loadPublications() throws SQLException {
        List<Publication> publicationList = publicationService.showAll();
        publications.setAll(publicationList);
        publicationsTableView.setItems(publications);
    }

    @FXML
    private void GoBackHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/homePub.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) buttonreturn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Home");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load homePub.fxml: " + e.getMessage());
        }
    }

    private void showEditDialog(Publication publication) {
        Dialog<Pair<Boolean, Publication>> dialog = new Dialog<>();
        dialog.getDialogPane().getStyleClass().add("dialog-pane");
        dialog.setTitle("Modifier Publication");
        dialog.setHeaderText("Modifier Publication");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titleField = new TextField(publication.getTitle());
        TextField contentField = new TextField(publication.getContenu());
        DatePicker datePicker = new DatePicker(publication.getDate_publication().toLocalDate());
        TextField authorField = new TextField(publication.getAuthor());
        TextField visibilityField = new TextField(publication.getVisibility());
        TextField imageField = new TextField(publication.getImage());
        TextField categoryField = new TextField(String.valueOf(publication.getCategorie().getIdCategorie()));

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Content:"), 0, 1);
        grid.add(contentField, 1, 1);
        grid.add(new Label("Date:"), 0, 2);
        grid.add(datePicker, 1, 2);
        grid.add(new Label("Author:"), 0, 3);
        grid.add(authorField, 1, 3);
        grid.add(new Label("Visibility:"), 0, 4);
        grid.add(visibilityField, 1, 4);
        grid.add(new Label("Image URL:"), 0, 5);
        grid.add(imageField, 1, 5);
        grid.add(new Label("Category ID:"), 0, 6);
        grid.add(categoryField, 1, 6);

        dialog.getDialogPane().setContent(grid);

        Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);
        saveButton.setDisable(true);

        // Enable save button only when all fields are valid
        titleField.textProperty().addListener((observable, oldValue, newValue) -> saveButton.setDisable(!isInputValid(titleField, contentField, datePicker, authorField, visibilityField, imageField, categoryField)));
        contentField.textProperty().addListener((observable, oldValue, newValue) -> saveButton.setDisable(!isInputValid(titleField, contentField, datePicker, authorField, visibilityField, imageField, categoryField)));
        authorField.textProperty().addListener((observable, oldValue, newValue) -> saveButton.setDisable(!isInputValid(titleField, contentField, datePicker, authorField, visibilityField, imageField, categoryField)));
        visibilityField.textProperty().addListener((observable, oldValue, newValue) -> saveButton.setDisable(!isInputValid(titleField, contentField, datePicker, authorField, visibilityField, imageField, categoryField)));
        imageField.textProperty().addListener((observable, oldValue, newValue) -> saveButton.setDisable(!isInputValid(titleField, contentField, datePicker, authorField, visibilityField, imageField, categoryField)));
        categoryField.textProperty().addListener((observable, oldValue, newValue) -> saveButton.setDisable(!isInputValid(titleField, contentField, datePicker, authorField, visibilityField, imageField, categoryField)));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                if (!isInputValid(titleField, contentField, datePicker, authorField, visibilityField, imageField, categoryField)) {
                    showAlert("Invalid Input", "All fields must be filled.");
                    return null;
                }

                publication.setTitle(titleField.getText());
                publication.setContenu(contentField.getText());
                publication.setDate_publication(Date.valueOf(datePicker.getValue()));
                publication.setAuthor(authorField.getText());
                publication.setVisibility(visibilityField.getText());
                publication.setImage(imageField.getText());
                publication.getCategorie().setIdCategorie(Integer.parseInt(categoryField.getText()));
                return new Pair<>(true, publication);
            }
            return null;
        });

        Optional<Pair<Boolean, Publication>> result = dialog.showAndWait();

        result.ifPresent(pair -> {
            if (pair.getKey()) {
                Publication updatedPublication = pair.getValue();
                try {
                    update(updatedPublication);
                    publicationsTableView.refresh();
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert("Error", "Failed to update publication: " + e.getMessage());
                }
            }
        });
    }


    private boolean isInputValid(TextField title, TextField content, DatePicker date, TextField author, TextField visibility, TextField image, TextField category) {
        return !(title.getText().trim().isEmpty() || content.getText().trim().isEmpty() ||
                date.getValue() == null || author.getText().trim().isEmpty() ||
                visibility.getText().trim().isEmpty() || image.getText().trim().isEmpty() ||
                category.getText().trim().isEmpty());
    }

    private void update(Publication publication) throws SQLException {
        publicationService.update(publication);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void downloadPdf() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage();
                document.addPage(page);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    // Load the logo/image
                    PDImageXObject logo = PDImageXObject.createFromFile("src/main/resources/icons/iconTripit.png", document);

                    // Draw the logo and app name
                    contentStream.drawImage(logo, 50, 700, 100, 50); // Adjust position and size
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 24);
                    contentStream.newLineAtOffset(160, 730); // Adjust position
                    contentStream.showText("Trip It - Publications Report");
                    contentStream.endText();

                    // Add a separator line
                    contentStream.setLineWidth(1);
                    contentStream.moveTo(50, 690);
                    contentStream.lineTo(550, 690);
                    contentStream.stroke();

                    // Table headers
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                    contentStream.newLineAtOffset(50, 670);
                    contentStream.showText("ID");
                    contentStream.newLineAtOffset(50, 0);
                    contentStream.showText("Title");
                    contentStream.newLineAtOffset(150, 0);
                    contentStream.showText("Author");
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.showText("Date");
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.showText("Visibility");
                    contentStream.endText();

                    // Table content
                    int y = 650;
                    for (Publication publication : publicationsTableView.getItems()) {
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.HELVETICA, 10);
                        contentStream.newLineAtOffset(50, y);
                        contentStream.showText(String.valueOf(publication.getId_publication()));
                        contentStream.newLineAtOffset(50, 0);
                        contentStream.showText(publication.getTitle());
                        contentStream.newLineAtOffset(150, 0);
                        contentStream.showText(publication.getAuthor());
                        contentStream.newLineAtOffset(100, 0);
                        contentStream.showText(publication.getDate_publication().toString());
                        contentStream.newLineAtOffset(100, 0);
                        contentStream.showText(publication.getVisibility());
                        contentStream.endText();

                        // Draw a line under each row
                        contentStream.setLineWidth(0.5f);
                        contentStream.moveTo(50, y - 5);
                        contentStream.lineTo(550, y - 5);
                        contentStream.stroke();

                        y -= 20;
                    }

                    // Footer
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_OBLIQUE, 10);
                    contentStream.newLineAtOffset(50, 30);
                    contentStream.showText("Thank you for using Trip It!");
                    contentStream.endText();
                }

                document.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void downloadExcel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Publications");

                // Create header row
                Row headerRow = sheet.createRow(0);
                String[] columns = {"ID", "Title", "Content", "Author", "Date", "Visibility", "Image URL", "Category"};
                for (int i = 0; i < columns.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columns[i]);
                }


                int rowNum = 1;
                for (Publication publication : publicationsTableView.getItems()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(publication.getId_publication());
                    row.createCell(1).setCellValue(publication.getTitle());
                    row.createCell(2).setCellValue(publication.getContenu());
                    row.createCell(3).setCellValue(publication.getAuthor());
                    row.createCell(4).setCellValue(publication.getDate_publication().toString());
                    row.createCell(5).setCellValue(publication.getVisibility());
                    row.createCell(6).setCellValue(publication.getImage());
                    row.createCell(7).setCellValue(publication.getCategorie().getIdCategorie());
                }


                for (int i = 0; i < columns.length; i++) {
                    sheet.autoSizeColumn(i);
                }


                try (FileOutputStream fileOut = new FileOutputStream(file)) {
                    workbook.write(fileOut);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}