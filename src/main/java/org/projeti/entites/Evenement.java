package org.projeti.entites;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Evenement {
    private int id_Evenement;
    private String Type;

    private String Date_EvenementDepart;

    private String Date_EvenementArriver;
    private String lieu;
    private String Description;
    private float price;
    private List<Reservation> reservations; // Liste des réservations associées à cet événement


    public Evenement(int id_Evenement, String type, String date_EvenementDepart, String date_EvenementArriver, String lieu, String Description, float price) {
        this.id_Evenement = id_Evenement;
        Type = type;
        this.Date_EvenementDepart = date_EvenementDepart;
        this.Date_EvenementArriver = date_EvenementArriver;
        this.lieu = lieu;
        this.Description = Description;
        this.price = price;
    }

    public Evenement() {
    }

    public int getId_Evenement() {
        return id_Evenement;
    }

    public void setId_Evenement(int id_Evenement) {
        this.id_Evenement = id_Evenement;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }


    public String getLieu() {
        return lieu;
    }

    public String getDate_EvenementDepart() {
        return Date_EvenementDepart;
    }

    public void setDate_EvenementDepart(String date_EvenementDepart) {
        Date_EvenementDepart = date_EvenementDepart;
    }

    public String getDate_EvenementArriver() {
        return Date_EvenementArriver;
    }

    public void setDate_EvenementArriver(String date_EvenementArriver) {
        Date_EvenementArriver = date_EvenementArriver;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String DEscription) {
        this.Description = DEscription;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Evenement{" +
                "id_Evenement=" + id_Evenement +
                ", Type='" + Type + '\'' +
                ", Date_EvenementDepart='" + Date_EvenementDepart + '\'' +
                ", Date_EvenementArriver='" + Date_EvenementArriver + '\'' +
                ", lieu='" + lieu + '\'' +
                ", Description='" + Description + '\'' +
                ", price=" + price +
                ", reservations=" + reservations +
                '}';
    }
}


