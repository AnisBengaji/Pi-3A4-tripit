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
    private  String image;
    private Categorie categorie;  // Une seule catégorie pour chaque publication


    public Publication(int id_publication, String title, String contenu, Date date_publication, String author, String visibility, String image) {
        this.id_publication = id_publication;
        this.title = title;
        this.contenu = contenu;
        this.date_publication = (Date) date_publication;
        this.author = author;
        this.visibility = visibility;
        this.image = image;
    }
    public Publication() {}

    public int getId_publication() {
        return id_publication;
    }

    public void setId_publication(int id_publication) {
        this.id_publication = id_publication;
    }

    public String getTitle() {
        return title;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public String getVisiblity() {
        return visibility;
    }

    public void setVisiblity(String visiblity) {
        this.visibility = visiblity;
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
    @java.lang.Override

    public String toString() {
        return "Publication{" +
                "id_publication=" + id_publication +
                ", title='" + title + '\'' +
                ", contenue='" + contenu + '\'' +
                ", date_publication='" + date_publication + '\'' +
                ", author='" + author + '\'' +
                ", visiblity='" + visibility + '\'' +
                ", image='" + image + '\'' +
                ", categorie=" + categorie +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Publication that = (Publication) object;
        return getId_publication() == that.getId_publication() && Objects.equals(getTitle(), that.getTitle()) && Objects.equals(getContenu(), that.getContenu()) && Objects.equals(getDate_publication(), that.getDate_publication()) && Objects.equals(getAuthor(), that.getAuthor()) && Objects.equals(getVisibility(), that.getVisibility()) && Objects.equals(getImage(), that.getImage()) && Objects.equals(getCategorie(), that.getCategorie());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id_publication;
        return result;
    }

}
