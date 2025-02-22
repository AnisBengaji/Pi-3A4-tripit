package org.projeti.entites;

import java.util.List;

public class Destination {
    private int id_Destination;
    private String Pays;
    private String ville;
    private int code_postal;
    private float latitude;
    private float longitude;
    ;
    private List<Activity> activites;
    private List<Offre> offres;  // Liste des offres associées à cette destination

    public List<Offre> getOffres() {
        return offres;
    }

    public void setOffres(List<Offre> offres) {
        this.offres = offres;
    }

    public List<Activity> getActivites() {
        return activites;
    }

    public void setActivites(List<Activity> activites) {
        this.activites = activites;
    }

    public Destination(int id_Destination, String pays, String ville, int code_postal, float latitude, float longitude) {
        this.id_Destination = id_Destination;
        Pays = pays;
        this.ville = ville;
        this.code_postal = code_postal;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public Destination() {}

    public int getId_Destination() {
        return id_Destination;
    }

    public void setId_Destination(int id_Destination) {
        this.id_Destination = id_Destination;
    }

    public String getPays() {
        return Pays;
    }

    public void setPays(String pays) {
        Pays = pays;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public int getCode_postal() {
        return code_postal;
    }

    public void setCode_postal(int code_postal) {
        this.code_postal = code_postal;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
