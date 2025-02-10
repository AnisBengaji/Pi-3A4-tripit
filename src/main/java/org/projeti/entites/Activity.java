package org.projeti.entites;

public class Activity {
    private int id_activity;
    private String nom_activity;
    private String image_activity;
    private String type;
    private String description;
    private Float activity_price;
    private Destination destination;  // Référence vers la destination associée à l'activité


    public Activity(int id_activity, String nom_activity, String image_activity, String type, String description, Float activity_price) {
        this.id_activity = id_activity;
        this.nom_activity = nom_activity;
        this.image_activity = image_activity;
        this.type = type;
        this.description = description;
        this.activity_price = activity_price;
    }

    public int getId_activity() {
        return id_activity;
    }

    public void setId_activity(int id_activity) {
        this.id_activity = id_activity;
    }

    public String getNom_activity() {
        return nom_activity;
    }

    public void setNom_activity(String nom_activity) {
        this.nom_activity = nom_activity;
    }

    public String getImage_activity() {
        return image_activity;
    }

    public void setImage_activity(String image_activity) {
        this.image_activity = image_activity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getActivity_price() {
        return activity_price;
    }

    public void setActivity_price(Float activity_price) {
        this.activity_price = activity_price;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }
}
