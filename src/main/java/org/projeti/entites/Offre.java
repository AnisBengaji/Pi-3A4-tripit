package org.projeti.entites;

public class Offre {
    private int idOffre;
    private String titre;
    private String description;
    private float prix;
    private Tutorial tutorial;
<<<<<<< HEAD
    private Destination destination;  // Référence à la destination
=======
    private Destination destination;
>>>>>>> b4a66e2 (intergration interface+crud+controlS reservation+publication+evenement)

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public Offre(int idOffre, String description, String titre, float prix) {
        this.idOffre = idOffre;
        this.description = description;
        this.titre = titre;
        this.prix = prix;
    }
    public Offre() {}

    public Tutorial getTutorial() {
        return tutorial;
    }

    public void setTutorial(Tutorial tutorial) {
        this.tutorial = tutorial;
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
}
