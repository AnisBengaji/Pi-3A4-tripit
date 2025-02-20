package org.projeti.entites;

public class User {
    private int Id;
    private String Nom;
    private String Prenom;
    private  int Num_tel;
    private String Email;
    private String MDP;
    private String Role;
    //private List<Reclamtion> reclamations;
    // private List<Reservation> reservations;

    public User (){}

    public User(String Nom, String Prenom, int num_tel, String Email, String MDP, String Role) {
        this.Nom = Nom;
        this.Prenom = Prenom;
        Num_tel = num_tel;
        this.Email = Email;
        this.MDP = MDP;
        this.Role = Role;
    }

    public User(String nom, String prenom, String email, String MDP, String role) {
        Nom = nom;
        Prenom = prenom;
        Email = email;
        this.MDP = MDP;
        Role = role;
    }

    public User(int id, String nom, String prenom, int num_tel, String email, String MDP, String role) {
        Id = id;
        Nom = nom;
        Prenom = prenom;
        Num_tel = num_tel;
        Email = email;
        this.MDP = MDP;
        Role = role;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getPrenom() {
        return Prenom;
    }

    public void setPrenom(String prenom) {
        Prenom = prenom;
    }

    public int getNum_tel() {
        return Num_tel;
    }

    public void setNum_tel(int num_tel) {
        Num_tel = num_tel;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMDP() {
        return MDP;
    }

    public void setMDP(String MDP) {
        this.MDP = MDP;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "Id=" + Id +
                ", Nom='" + Nom + '\'' +
                ", Prenom='" + Prenom + '\'' +
                ", Num_tel=" + Num_tel +
                ", Email='" + Email + '\'' +
                ", MDP='" + MDP + '\'' +
                ", Role='" + Role + '\'' +
                '}';
    }
}
