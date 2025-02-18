package org.projeti.entites;

import java.sql.Date;

public class Tutorial {
    private String nom_tutorial;
    private String Date_debutTutorial;
    private String Date_finTutorial;
    private float prix_tutorial;
    private String offre;

    public Tutorial(String nom_tutorial, String date_debutTutorial, String date_finTutorial, float prix_tutorial) {
        this.nom_tutorial = nom_tutorial;
        Date_debutTutorial = date_debutTutorial;
        Date_finTutorial = date_finTutorial;
        this.prix_tutorial = prix_tutorial;


    }
    public Tutorial() {}
    public Tutorial(String tekaya, String date, String date1, float i, String admin) {
    }

    public Tutorial(String tekaya, String date, float i, String date1, String admin) {
    }

    public String getNom_tutorial() {
        return nom_tutorial;
    }

    public void setNom_tutorial(String nom_tutorial) {
        this.nom_tutorial = nom_tutorial;
    }

    public Date getDate_debutTutorial() {
        return Date.valueOf(Date_debutTutorial);
    }

    public void setDate_debutTutorial(String date_debutTutorial) {
        Date_debutTutorial = date_debutTutorial;
    }

    public Date getDate_finTutorial() {
        return Date.valueOf(Date_finTutorial);
    }

    public void setDate_finTutorial(String date_finTutorial) {
        Date_finTutorial = date_finTutorial;
    }

    public float getPrix_tutorial() {
        return prix_tutorial;
    }

    public void setPrix_tutorial(float prix_tutorial) {
        this.prix_tutorial = prix_tutorial;
    }

    public String getOffre() {
        return offre;
    }

    public void setOffre(String offre) {
        this.offre = offre;
    }


    @Override
    public String toString() {
        return "Tutorial{" +
                "nom_tutorial='" + nom_tutorial + '\'' +
                ", Date_debutTutorial='" + Date_debutTutorial + '\'' +
                ", Date_finTutorial='" + Date_finTutorial + '\'' +
                ", prix_tutorial=" + prix_tutorial +
                '}';
    }
}
