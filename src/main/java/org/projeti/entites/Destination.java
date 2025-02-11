package org.projeti.entites;

import java.util.List;
import java.util.Objects;

public class Destination {
    private int id_Destination;
    private String pays;
    private String ville;
    private int code_postal;
    private float latitude;
    private float longitude;

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
        this.pays = pays;
        this.ville = ville;
        this.code_postal = code_postal;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public Destination() {}

    public Destination(String pays, String ville, int code_postal, float latitude, float longitude) {
        this.pays = pays;
        this.ville = ville;
        this.code_postal = code_postal;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId_Destination() {
        return id_Destination;
    }

    public void setId_Destination(int id_Destination) {
        this.id_Destination = id_Destination;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Destination that = (Destination) o;
        return id_Destination == that.id_Destination && Objects.equals(ville, that.ville);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_Destination, ville);
    }

    @Override
    public String toString() {
        return "Destination{" +
                "id_Destination=" + id_Destination +
                ", pays='" + pays + '\'' +
                ", ville='" + ville + '\'' +
                ", code_postal=" + code_postal +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", activites=" + activites +
                ", offres=" + offres +
                '}';
    }
}
