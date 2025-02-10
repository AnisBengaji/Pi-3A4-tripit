package org.projeti.entites;

import java.util.List;

public class Reservation {
    private int id_reservation;
    private String Status;
    private float price_total;
    private String Mode_paiment;
    private User user;
    private Evenement evenement; // Référence à l'événement auquel cette réservation appartient

// Référence à l'utilisateur auquel la réservation appartient


    public Reservation(int id_reservation, String status, float price_total, String mode_paiment) {
        this.id_reservation = id_reservation;
        Status = status;
        this.price_total = price_total;
        Mode_paiment = mode_paiment;
    }
    public Reservation(){};

    public int getId_reservation() {
        return id_reservation;
    }

    public void setId_reservation(int id_reservation) {
        this.id_reservation = id_reservation;
    }



    public String getStatus() {
        return Status;
    }

    public float getPrice_total() {
        return price_total;
    }

    public void setPrice_total(float price_total) {
        this.price_total = price_total;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Evenement getEvenement() {
        return evenement;
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public float getPrice() {
        return price_total;
    }

    public void setPrice(float price) {
        this.price_total = price;
    }

    public String getMode_paiment() {
        return Mode_paiment;
    }

    public void setMode_paiment(String mode_paiment) {
        Mode_paiment = mode_paiment;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id_reservation=" + id_reservation +
                ", Status='" + Status + '\'' +
                ", price=" + price_total +
                ", Mode_paiment='" + Mode_paiment + '\'' +
                '}';
    }
}

