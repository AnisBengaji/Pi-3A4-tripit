package org.projeti.entites;

public class Publication {
    private int id_publication;
    private String title;
    private String contenue;
    private String date_publication;
    private String author;
    private String visiblity;
    private  String image;
    private Categorie categorie;  // Une seule catégorie pour chaque publication


    public Publication(int id_publication, String title, String contenue, String date_publication, String author, String visiblity, String image) {
        this.id_publication = id_publication;
        this.title = title;
        this.contenue = contenue;
        this.date_publication = date_publication;
        this.author = author;
        this.visiblity = visiblity;
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
        return visiblity;
    }

    public void setVisiblity(String visiblity) {
        this.visiblity = visiblity;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContenue() {
        return contenue;
    }

    public void setContenue(String contenue) {
        this.contenue = contenue;
    }

    public String getDate_publication() {
        return date_publication;
    }

    public void setDate_publication(String date_publication) {
        this.date_publication = date_publication;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getvisiblity() {
        return visiblity;
    }

    public void setvisiblity(String visiblity) {
        visiblity = visiblity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
