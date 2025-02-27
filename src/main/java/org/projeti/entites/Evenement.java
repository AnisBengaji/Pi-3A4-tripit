package org.projeti.entites;

import java.time.LocalDate;
import java.util.List;

public class Evenement {
    private int id_Evenement;
    private String nom;
    private LocalDate Date_EvenementDepart;
    private LocalDate Date_EvenementArriver;
    private String lieu;
    private String Description;
    private float price;
    private List<Reservation> reservations; // Liste des réservations associées à cet événement
    private double latitude;  // Nouveau champ pour la latitude
    private double longitude; // Nouveau champ pour la longitude

    // Constructeur avec latitude et longitude
    public Evenement(int id_Evenement, String nom, LocalDate date_EvenementDepart, LocalDate date_EvenementArriver, String lieu, String Description, float price, double latitude, double longitude) {
        this.id_Evenement = id_Evenement;
        this.nom = nom;
        this.Date_EvenementDepart = date_EvenementDepart;
        this.Date_EvenementArriver = date_EvenementArriver;
        this.lieu = lieu;
        this.Description = Description;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Constructeur par défaut
    public Evenement() {
    }

    // Getters et setters pour les nouveaux attributs
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    // Getters et setters existants
    public int getId_Evenement() {
        return id_Evenement;
    }

    public void setId_Evenement(int id_Evenement) {
        this.id_Evenement = id_Evenement;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public LocalDate getDate_EvenementDepart() {
        return Date_EvenementDepart;
    }

    public void setDate_EvenementDepart(LocalDate date_EvenementDepart) {
        Date_EvenementDepart = date_EvenementDepart;
    }

    public LocalDate getDate_EvenementArriver() {
        return Date_EvenementArriver;
    }

    public void setDate_EvenementArriver(LocalDate date_EvenementArriver) {
        Date_EvenementArriver = date_EvenementArriver;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Override
    public String toString() {
        return "Evenement{" +
                "id_Evenement=" + id_Evenement +
                ", nom='" + nom + '\'' +
                ", Date_EvenementDepart=" + Date_EvenementDepart +
                ", Date_EvenementArriver=" + Date_EvenementArriver +
                ", lieu='" + lieu + '\'' +
                ", Description='" + Description + '\'' +
                ", price=" + price +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", reservations=" + reservations +
                '}';
    }
}
