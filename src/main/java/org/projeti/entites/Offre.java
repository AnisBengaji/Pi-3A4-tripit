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

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public String getTutorial() {
        return tutorial;
    }

    public void setTutorial(String tutorial) {
        this.tutorial = tutorial;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "Offre{" +
                "idOffre=" + idOffre +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                ", tutorial='" + tutorial + '\'' +
                ", destination='" + destination + '\'' +
                '}';
    }
}
