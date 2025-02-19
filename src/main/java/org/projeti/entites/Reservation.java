package org.projeti.entites;

public class Reservation {
    private int id_reservation;
    private Status status;
    private float price_total;
<<<<<<< HEAD
    private String Mode_paiment;
=======
    private ModePaiement Mode_paiment;
>>>>>>> b4a66e2 (intergration interface+crud+controlS reservation+publication+evenement)
    private User user;
    private Evenement evenement; // Référence à l'événement auquel cette réservation appartient

// Référence à l'utilisateur auquel la réservation appartient


<<<<<<< HEAD
    public Reservation(int id_reservation, Status status, float price_total, String mode_paiment, Object o) {
        this.id_reservation = id_reservation;
        this.status = status;
        this.price_total = price_total;
        Mode_paiment = mode_paiment;
    }
    public Reservation(){};
=======
    public Reservation(int id_reservation, Status status, float price_total, ModePaiement mode_paiment) {
        this.id_reservation = id_reservation;
        this.status = status;  // Status directement
        this.price_total = price_total;
        this.Mode_paiment = mode_paiment;
    }
    public Reservation(int idReservation, String status, float price, String modePaiment){};
>>>>>>> b4a66e2 (intergration interface+crud+controlS reservation+publication+evenement)

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

<<<<<<< HEAD
    public String getMode_paiment() {
        return Mode_paiment;
    }

    public void setMode_paiment(String mode_paiment) {
=======
    public ModePaiement getMode_paiment() {
        return Mode_paiment;
    }

    public void setMode_paiment(ModePaiement mode_paiment) {
>>>>>>> b4a66e2 (intergration interface+crud+controlS reservation+publication+evenement)
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

