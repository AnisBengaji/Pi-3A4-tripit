package org.projeti.entites;

import java.util.List;

public class User {
    private int id;
    private String name;
    private String prenom;
    private String email;
    private String password;
    private List<Reclamtion> reclamations;
    private List<Reservation> reservations;

    public User(String name, int id, String prenom, String email, String password) {
        this.name = name;
        this.id = id;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
    }
    public User()
    {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
    public List<Reclamtion> getReclamations() {
        return reclamations;
    }

    public void setReclamations(List<Reclamtion> reclamations) {
        this.reclamations = reclamations;
    }
}
