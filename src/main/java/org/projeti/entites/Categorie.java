package org.projeti.entites;

import java.util.List;
import java.util.Objects;

public class Categorie {

    private int idCategorie;
    private String nomCategorie;
    private String description;
    private List<Publication> publications;  // Liste des publications associées à cette catégorie

    public Categorie( String nomCategorie, String description, List<Publication> publications) {
        this.nomCategorie = nomCategorie;
        this.description = description;
        this.publications = publications;
    }
    public Categorie(){}
    public int getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie(int idCategorie) {
        this.idCategorie = idCategorie;
    }

    public String getNomCategorie() {
        return nomCategorie;
    }

    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Publication> getPublications() {
        return publications;
    }

    public void setPublications(List<Publication> publications) {
        this.publications = publications;
    }


    @Override
    public String toString() {
        return "Categorie{" +
                "idCategorie=" + idCategorie +
                ", nomCategorie='" + nomCategorie + '\'' +
                ", description='" + description + '\'' +
                ", publications=" + publications +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Categorie categorie = (Categorie) object;
        return getIdCategorie() == categorie.getIdCategorie() && Objects.equals(getNomCategorie(), categorie.getNomCategorie()) && Objects.equals(getDescription(), categorie.getDescription()) && Objects.equals(getPublications(), categorie.getPublications());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdCategorie(), getNomCategorie(), getDescription(), getPublications());
    }


}