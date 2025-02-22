package org.projeti.controllers;

import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.projeti.entites.Publication;

public class PublicationListCell extends ListCell<Publication> {

    @Override
    protected void updateItem(Publication publication, boolean empty) {
        super.updateItem(publication, empty);

        if (empty || publication == null) {
            setText(null);
            setGraphic(null);
        } else {
            // Create a VBox to hold the post content
            VBox postBox = new VBox();
            postBox.setSpacing(10); // Space between elements
            postBox.setStyle("-fx-padding: 15px; -fx-background-color: #FFFFFF; -fx-border-color: #E0E0E0; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-background-radius: 5px;");

            // Title
            Text titleText = new Text(publication.getTitle());
            titleText.setFont(Font.font("System", FontWeight.BOLD, 16));
            titleText.setStyle("-fx-fill: #2A7F77;");

            // Author
            Text authorText = new Text("By: " + publication.getAuthor());
            authorText.setFont(Font.font("System", FontWeight.NORMAL, 12));
            authorText.setStyle("-fx-fill: #1E8E6F;");

            // Date (handle null case)
            String dateString = (publication.getDate_publication() != null)
                    ? "Published on: " + publication.getDate_publication().toString()
                    : "Date not available";
            Text dateText = new Text(dateString);
            dateText.setFont(Font.font("System", FontWeight.NORMAL, 12));
            dateText.setStyle("-fx-fill: #666666;");

            // Content
            Text contentText = new Text(publication.getContenu());
            contentText.setFont(Font.font("System", FontWeight.NORMAL, 14));
            contentText.setStyle("-fx-fill: #333333;");
            contentText.setWrappingWidth(700); // Wrap text to fit the width

            // Add all elements to the VBox
            postBox.getChildren().addAll(titleText, authorText, dateText, contentText);

            // Set the VBox as the graphic for the cell
            setGraphic(postBox);
        }
    }
}