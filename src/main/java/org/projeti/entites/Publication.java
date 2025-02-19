package org.projeti.entites;

import java.sql.Date;
import java.util.Objects;

public class Publication {
    private int id_publication;
    private String title;
    private String contenu;
    private Date date_publication;
    private String author;
    private String visibility;
    private String image;
    private Categorie categorie;  // Une seule catégorie pour chaque publication

    // Constructor with all fields
    public Publication(String title, String contenu, Date date_publication, String author, String visibility, String image, Categorie categorie) {
        this.title = title;
        this.contenu = contenu;
        this.date_publication = date_publication;
        this.author = author;
        this.visibility = visibility;
        this.image = image;
        this.categorie = categorie;
    }

    // Default constructor
    public Publication() {}

    // Getters and Setters
    public int getId_publication() {
        return id_publication;
    }

    public void setId_publication(int id_publication) {
        this.id_publication = id_publication;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Date getDate_publication() {
        return date_publication;
    }

    public void setDate_publication(Date date_publication) {
        this.date_publication = date_publication;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    @Override
    public String toString() {
        return "Publication{" +
                "id_publication=" + id_publication +
                ", title='" + title + '\'' +
                ", contenu='" + contenu + '\'' +
                ", date_publication=" + date_publication +
                ", author='" + author + '\'' +
                ", visibility='" + visibility + '\'' +
                ", image='" + image + '\'' +
                ", categorie=" + categorie +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Publication that = (Publication) object;
        return id_publication == that.id_publication &&
                Objects.equals(title, that.title) &&
                Objects.equals(contenu, that.contenu) &&
                Objects.equals(date_publication, that.date_publication) &&
                Objects.equals(author, that.author) &&
                Objects.equals(visibility, that.visibility) &&
                Objects.equals(image, that.image) &&
                Objects.equals(categorie, that.categorie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_publication, title, contenu, date_publication, author, visibility, image, categorie);
    }
}