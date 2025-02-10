package org.projeti.entites;

import java.util.List;

public class Categorie {

    private String nom_Categorie;
    private String description;
    private List<Publication> publications;  // Liste des publications associées à cette catégorie


    public Categorie(String nom_Categorie, String description) {
        this.nom_Categorie = nom_Categorie;
        this.description = description;
    }
    public String getNom_categorie() {
        return nom_Categorie;
    }

    public List<Publication> getPublications() {
        return publications;
    }

    public void setPublications(List<Publication> publications) {
        this.publications = publications;
    }

    public String getNom_Categorie() {
        return nom_Categorie;
    }

    public void setNom_Categorie(String nom_Categorie) {
        this.nom_Categorie = nom_Categorie;
    }

    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }
}


