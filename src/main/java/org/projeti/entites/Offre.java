package org.projeti.entites;

public class Offre {
    private int idOffre;
    private String titre;
    private String description;
    private float prix;
    private String tutorial;
    private String destination;

    public Offre() {}

    public Offre(String titre, String description, float prix, String tutorial, String destination) {
        this.titre = titre;
        this.description = description;
        this.prix = prix;
        this.tutorial = tutorial;
        this.destination = destination;
    }

    public Offre(int idOffre, String titre, String description, float prix, String tutorial, String destination) {
        this.idOffre = idOffre;
        this.titre = titre;
        this.description = description;
        this.prix = prix;
        this.tutorial = tutorial;
        this.destination = destination;
    }

    public int getIdOffre() {
        return idOffre;
    }

    public void setIdOffre(int idOffre) {
        this.idOffre = idOffre;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrix() {
        return prix;
    }

    public String getTutorial() {
        return "";
    }

    public String getDestination() {
        return "";
    }

    public void setPrix(float v) {
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setTutorial(String tutorial) {
        this.tutorial = tutorial;
    }


}


