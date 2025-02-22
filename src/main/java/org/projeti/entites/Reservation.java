package org.projeti.entites;

public class Reservation {
    private int id_reservation;
    private Status status;
    private float price_total;
    private ModePaiement Mode_paiment;
    private User user;
    private Evenement evenement; // Référence à l'événement auquel cette réservation appartient

// Référence à l'utilisateur auquel la réservation appartient


    public Reservation(int id_reservation, Status status, float price_total, ModePaiement mode_paiment) {
        this.id_reservation = id_reservation;
        this.status = status;  // Status directement
        this.price_total = price_total;
        this.Mode_paiment = mode_paiment;
    }
    public Reservation(int idReservation, String status, float price, String modePaiment){};

    public int getId_reservation() {
        return id_reservation;
    }

    public void setId_reservation(int id_reservation) {
        this.id_reservation = id_reservation;
    }



    public Status getStatus() {
        return status;
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

    public void setStatus(Status status) {
        this.status = status;
    }

    public float getPrice() {
        return price_total;
    }

    public void setPrice(float price) {
        this.price_total = price;
    }

    public ModePaiement getMode_paiment() {
        return Mode_paiment;
    }

    public void setMode_paiment(ModePaiement mode_paiment) {
        Mode_paiment = mode_paiment;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id_reservation=" + id_reservation +
                ", status='" + status + '\'' +
                ", price=" + price_total +
                ", Mode_paiment='" + Mode_paiment + '\'' +
                '}';
    }
}

