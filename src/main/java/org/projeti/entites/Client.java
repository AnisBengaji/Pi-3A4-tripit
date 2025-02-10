package org.projeti.entites;

public class Client extends User{
    public Client() {
    }

    public Client(String name, int id, String prenom, String email, String password) {
        super(name, id, prenom, email, password);
    }
}
