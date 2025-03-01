package org.projeti.controllers;

import javafx.scene.control.ListCell;
import org.projeti.entites.Categorie;

public class CategorieListCell extends ListCell<Categorie> {
    @Override
    protected void updateItem(Categorie categorie, boolean empty) {
        super.updateItem(categorie, empty);

        if (empty || categorie == null) {
            setText(null);
            setGraphic(null);
        } else {

            setText(categorie.getNomCategorie() + " - " + categorie.getDescription());
        }
    }
}
