package org.projeti.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.projeti.Service.CommentService;
import org.projeti.entites.Comment;
import org.projeti.entites.Publication;
import org.projeti.entites.Categorie;
import org.projeti.Service.PublicationService;
import org.projeti.Service.CategorieService;


public class SocialController implements Initializable {

    @FXML
    private TextField searchField;
    @FXML
    private ListView<String> categoriesListView;
    @FXML
    private VBox publicationsContainer;
    @FXML
    private VBox publicationTemplate;
    @FXML
    private Button buttonnavigateadd;
    @FXML
    private Button buttonAddCat;
    String currentUser = "Guest";
    private PublicationService publicationService;
    private CategorieService categorieService;
    private List<Publication> currentPublications;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // Initialize services
            publicationService = new PublicationService();
            categorieService = new CategorieService();
            currentPublications = new ArrayList<>();

            // Load categories
            loadCategories();

            // Load all publications initially
            loadAllPublications();

        } catch (Exception e) {
            showAlert("Error initializing application: " + e.getMessage());
        }
    }

    private void loadCategories() {
        try {
            List<Categorie> categories = categorieService.showAll();

            categoriesListView.getItems().add("All Categories");

            for (Categorie category : categories) {
                categoriesListView.getItems().add(category.getNomCategorie());
            }

            // Select "All Categories" by default
            categoriesListView.getSelectionModel().select(0);

        } catch (Exception e) {
            showAlert("Error loading categories: " + e.getMessage());
        }
    }

    private void loadAllPublications() {
        try {
            currentPublications = publicationService.showAll();
            displayPublications(currentPublications);
        } catch (Exception e) {
            showAlert("Error loading publications: " + e.getMessage());
        }
    }

    private void displayPublications(List<Publication> publications) {
        publicationsContainer.getChildren().clear();

        if (publications.isEmpty()) {
            Label noPublicationsLabel = new Label("No publications found");
            noPublicationsLabel.getStyleClass().add("no-publications-label");
            publicationsContainer.getChildren().add(noPublicationsLabel);
            return;
        }

        for (Publication publication : publications) {
            VBox publicationCard = createPublicationCard(publication);
            publicationsContainer.getChildren().add(publicationCard);
        }
    }

    private VBox createPublicationCard(Publication publication) {
        // Create the card container
        VBox card = new VBox();
        card.getStyleClass().add("publication-card");
        card.setSpacing(10);

        // Set user data for reference
        card.setUserData(publication.getId_publication());

        // Create header with author info
        ImageView authorAvatar = new ImageView();
        try {
            // Try to load user avatar, or use default
            String avatarPath = "/images/default-avatar.png"; // Default avatar path
            File avatarFile = new File(avatarPath);

            if (avatarFile.exists()) {
                authorAvatar.setImage(new Image(avatarFile.toURI().toString()));
            } else {
                // Fallback to class resource
                authorAvatar.setImage(new Image(getClass().getResourceAsStream(avatarPath)));
            }
        } catch (Exception e) {
            // If loading fails, use a placeholder
            authorAvatar.setImage(null);
        }

        authorAvatar.setFitHeight(40);
        authorAvatar.setFitWidth(40);
        authorAvatar.getStyleClass().add("author-avatar");

        Label authorLabel = new Label(publication.getAuthor());
        authorLabel.getStyleClass().add("author-name");

        // Format date for display
        String formattedDate = "Unknown Date";
        if (publication.getDate_publication() != null) {
            formattedDate = publication.getDate_publication().toLocalDate()
                    .format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
        }
        Label dateLabel = new Label(formattedDate);
        dateLabel.getStyleClass().add("publication-date");

        Label visibilityBadge = new Label(publication.getVisibility());
        visibilityBadge.getStyleClass().add("visibility-badge");

        // Create title
        Label titleLabel = new Label(publication.getTitle());
        titleLabel.getStyleClass().add("publication-title");
        titleLabel.setWrapText(true);

        // Create content
        TextFlow contentFlow = new TextFlow();
        contentFlow.getStyleClass().add("publication-content");
        Text contentText = new Text(publication.getContenu());
        contentFlow.getChildren().add(contentText);

        // Add image if available
        ImageView publicationImageView = null;
        if (publication.getImage() != null && !publication.getImage().isEmpty()) {
            try {
                File imageFile = new File(publication.getImage());
                if (imageFile.exists()) {
                    publicationImageView = new ImageView(new Image(imageFile.toURI().toString()));
                } else {
                    // Try as a resource or URL
                    publicationImageView = new ImageView(new Image(publication.getImage()));
                }

                publicationImageView.setFitWidth(400);
                publicationImageView.setPreserveRatio(true);
                publicationImageView.getStyleClass().add("publication-image");
            } catch (Exception e) {
                // Image couldn't be loaded, no need to add it
                publicationImageView = null;
            }
        }

        String categoryName = publication.getCategorie() != null ?
                publication.getCategorie().getNomCategorie() : "Uncategorized";
        Label categoryLabel = new Label(categoryName);
        categoryLabel.getStyleClass().add("category-tag");

        // Create action buttons
        Button likeButton = new Button("Like");
        likeButton.getStyleClass().add("action-button");
        int pubId = publication.getId_publication();
        likeButton.setOnAction(e -> handleLike(pubId));

        Button commentButton = new Button("Comment");
        commentButton.getStyleClass().add("action-button");
        commentButton.setOnAction(e -> handleComment(pubId));

        Button shareButton = new Button("Share");
        shareButton.getStyleClass().add("action-button");
        shareButton.setOnAction(e -> handleShare(pubId));

        // Create comments section (initially hidden)
        VBox commentsSection = new VBox();
        commentsSection.getStyleClass().add("comments-section");
        commentsSection.setManaged(false);
        commentsSection.setVisible(false);

        HBox commentInputBox = new HBox();
        commentInputBox.setSpacing(10);
        TextField commentField = new TextField();
        commentField.setPromptText("Write a comment...");
        commentField.getStyleClass().add("comment-field");
        HBox.setHgrow(commentField, Priority.ALWAYS);

        Button postButton = new Button("Post");
        postButton.getStyleClass().add("post-comment-button");
        postButton.setOnAction(e -> {
            try {
                postComment(pubId, commentField.getText());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        commentInputBox.getChildren().addAll(commentField, postButton);

        VBox commentsContainer = new VBox();
        commentsContainer.getStyleClass().add("comments-container");

        commentsSection.getChildren().addAll(commentInputBox, commentsContainer);

        // Assemble the card
        HBox header = createPublicationHeader(authorAvatar, authorLabel, dateLabel, visibilityBadge, pubId);
        HBox actionButtons = createActionButtons(likeButton, commentButton, shareButton);

        card.getChildren().addAll(header, titleLabel, contentFlow, categoryLabel, actionButtons, commentsSection);

        // Add image if available (after title but before content)
        if (publicationImageView != null) {
            card.getChildren().add(2, publicationImageView);
        }

        return card;
    }

    private HBox createPublicationHeader(ImageView avatar, Label author, Label date, Label visibility, int publicationId) {
        HBox header = new HBox();
        header.getStyleClass().add("publication-header");
        header.setSpacing(10);

        VBox authorInfo = new VBox();
        authorInfo.getChildren().addAll(author, date);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        MenuButton menuButton = new MenuButton();
        menuButton.getStyleClass().add("publication-menu");

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(e -> editPublication(publicationId));

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(e -> deletePublication(publicationId));

        MenuItem shareItem = new MenuItem("Share");
        shareItem.setOnAction(e -> sharePublication(publicationId));

        menuButton.getItems().addAll(editItem, deleteItem, shareItem);

        header.getChildren().addAll(avatar, authorInfo, spacer, visibility, menuButton);

        return header;
    }

    private HBox createActionButtons(Button like, Button comment, Button share) {
        HBox actions = new HBox();
        actions.getStyleClass().add("publication-actions");
        actions.setSpacing(10);
        actions.getChildren().addAll(like, comment, share);

        return actions;
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().trim();

        if (searchTerm.isEmpty()) {
            loadAllPublications();
        } else {
            try {
                // Since PublicationService doesn't have a search method, we'll implement a basic search here
                List<Publication> allPublications = publicationService.showAll();
                List<Publication> searchResults = new ArrayList<>();

                for (Publication pub : allPublications) {
                    if (pub.getTitle().toLowerCase().contains(searchTerm.toLowerCase()) ||
                            pub.getContenu().toLowerCase().contains(searchTerm.toLowerCase()) ||
                            pub.getAuthor().toLowerCase().contains(searchTerm.toLowerCase())) {
                        searchResults.add(pub);
                    }
                }

                currentPublications = searchResults;
                displayPublications(searchResults);
            } catch (Exception e) {
                showAlert("Error searching publications: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleCategorySelect() {
        String selected = categoriesListView.getSelectionModel().getSelectedItem();

        if (selected == null || selected.equals("All Categories")) {
            loadAllPublications();
        } else {
            try {
                List<Publication> filteredPublications = publicationService.getPublicationsByCategory(selected);
                currentPublications = filteredPublications;
                displayPublications(filteredPublications);
            } catch (Exception e) {
                showAlert("Error filtering publications: " + e.getMessage());
            }
        }
    }

    @FXML
    private void filterAll() {
        loadAllPublications();
    }

    @FXML
    private void filterMine() {
        // Assuming the current user's name is stored somewhere
        String currentUser = "Current User"; // Replace with actual user name from your authentication system

        try {
            List<Publication> userPublications = publicationService.getPublicationsByAuthor(currentUser);
            currentPublications = userPublications;
            displayPublications(userPublications);
        } catch (Exception e) {
            showAlert("Error filtering your publications: " + e.getMessage());
        }
    }

    @FXML
    private void filterPublic() {
        try {
            List<Publication> publicPublications = publicationService.getPublicationsByVisibility("Public");
            currentPublications = publicPublications;
            displayPublications(publicPublications);
        } catch (Exception e) {
            showAlert("Error filtering public publications: " + e.getMessage());
        }
    }

    @FXML
    private void showCreatePublicationDialog() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CreatePublication.fxml"));
            Parent root = loader.load();

            CreatePublicationController controller = loader.getController();

            if (publicationService == null || categorieService == null) {
                System.err.println("Error: publicationService or categorieService is null!");
                return;
            }

            controller.setPublicationService(publicationService);
            controller.setCategorieService(categorieService);

            // Create and show the dialog
            Stage stage = new Stage();
            stage.setTitle("Create New Publication");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);

            // Refresh publications when dialog is closed
            stage.setOnHidden(e -> loadAllPublications());

            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace(); // Print full error for debugging
            showAlert("Error opening publication form: " + e.getMessage());
        }
    }

    @FXML
    private void handleLike(int publicationId) {
        // Implementation for liking a publication
        // This would typically involve:
        // 1. Checking if user has already liked the publication
        // 2. Adding/removing like in the database
        // 3. Updating the UI to reflect the change

        System.out.println("Like publication: " + publicationId);
        // Replace with actual implementation
    }

    @FXML
    private void handleComment(int publicationId) {
        // Find the publication card by its ID
        for (javafx.scene.Node node : publicationsContainer.getChildren()) {
            if (node instanceof VBox && node.getUserData() != null &&
                    node.getUserData().equals(publicationId)) {

                VBox card = (VBox) node;

                // Find the comments section (it should be the last child)
                VBox commentsSection = (VBox) card.getChildren().get(card.getChildren().size() - 1);

                // Toggle visibility
                boolean isVisible = commentsSection.isVisible();
                commentsSection.setManaged(!isVisible);
                commentsSection.setVisible(!isVisible);

                // If showing comments, load them from database
                if (!isVisible) {
                    loadComments(publicationId, commentsSection);
                }

                break;
            }
        }
    }

    private void loadComments(int publicationId, VBox commentsSection) {
        // Get the comments container (should be second child in comments section)
        VBox commentsContainer = (VBox) commentsSection.getChildren().get(1);
        commentsContainer.getChildren().clear();

        // TODO: Replace with actual comments loading from database
        // For now, add some placeholder comments
        VBox comment1 = createCommentBubble("User 1", "This is a great post!");
        VBox comment2 = createCommentBubble("User 2", "Thanks for sharing this information.");

        commentsContainer.getChildren().addAll(comment1, comment2);
    }

    private VBox createCommentBubble(String author, String text) {
        VBox commentBubble = new VBox();
        commentBubble.getStyleClass().add("comment-bubble");

        Label authorLabel = new Label(author);
        authorLabel.getStyleClass().add("comment-author");

        Label commentText = new Label(text);
        commentText.getStyleClass().add("comment-text");
        commentText.setWrapText(true);

        commentBubble.getChildren().addAll(authorLabel, commentText);

        return commentBubble;
    }

    @FXML
    private void handleShare(int publicationId) {
        // Implementation for sharing a publication
        // This might open a dialog with sharing options

        System.out.println("Share publication: " + publicationId);
        // Replace with actual implementation
    }

    @FXML
    private void editPublication(int publicationId) {
        try {
            // Find the publication object
            Publication publicationToEdit = null;
            for (Publication pub : currentPublications) {
                if (pub.getId_publication() == publicationId) {
                    publicationToEdit = pub;
                    break;
                }
            }

            if (publicationToEdit == null) {
                showAlert("Publication not found");
                return;
            }

            // Load the edit form
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditPublication.fxml"));
            Parent root = loader.load();

            // Get the controller and pass data
            EditPublicationController controller = loader.getController();
            controller.setPublication(publicationToEdit);
            controller.setPublicationService(publicationService);
            controller.setCategorieService(categorieService);

            // Create and show the dialog
            Stage stage = new Stage();
            stage.setTitle("Edit Publication");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);

            // Refresh publications when dialog is closed
            stage.setOnHidden(e -> loadAllPublications());

            stage.showAndWait();

        } catch (Exception e) {
            showAlert("Error opening edit form: " + e.getMessage());
        }
    }

    @FXML
    private void deletePublication(int publicationId) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Publication");
        confirmAlert.setHeaderText("Are you sure?");
        confirmAlert.setContentText("This action cannot be undone.");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    publicationService.deletePublication(publicationId);
                    loadAllPublications(); // Refresh the view
                } catch (Exception e) {
                    showAlert("Error deleting publication: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void sharePublication(int publicationId) {
        // Similar to handleShare but called from menu
        // Could have more options than the quick share button
        handleShare(publicationId);
    }

    private void postComment(int publicationId, String commentText) throws SQLException {
        if (commentText == null || commentText.trim().isEmpty()) {
            return; // Don't post empty comments
        }

        // Retrieve the current user (Modify based on your actual user management)
       // String currentUser = UserSession.getInstance().getUsername(); // Ensure this method exists

        // Convert LocalDateTime to java.sql.Date
        java.sql.Date sqlDate = java.sql.Date.valueOf(LocalDate.now());

        // Save comment to database with corrected constructor parameters
        CommentService commentService = new CommentService();

        Comment newComment = new Comment(currentUser, commentText, sqlDate, publicationId);
        commentService.insert(newComment);


        Platform.runLater(() -> {

            for (javafx.scene.Node node : publicationsContainer.getChildren()) {
                if (node instanceof VBox && publicationId == (int) node.getUserData()) {
                    VBox card = (VBox) node;
                    VBox commentsSection = (VBox) card.getChildren().get(card.getChildren().size() - 1);
                    VBox commentsContainer = (VBox) commentsSection.getChildren().get(1);


                    VBox newCommentBubble = createCommentBubble(currentUser, commentText);
                    commentsContainer.getChildren().add(0, newCommentBubble);

                    // Clear the comment field
                    TextField commentField = (TextField) ((HBox) commentsSection.getChildren().get(0)).getChildren().get(0);
                    commentField.clear();

                    break;
                }
            }
        });
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void GoToAddpublication() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterPub.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) buttonnavigateadd.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Home");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load homePub.fxml: " + e.getMessage());
        }
    }

    @FXML
    private void gotoAddCategoryFront() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterCatFront.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) buttonAddCat.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("ajouter categorie!");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load AjouterCategorie.fxml: " + e.getMessage());
        }
    }
}