package org.projeti.entites;

import java.util.Objects;

public class Activity {
    private int id_activity;
    private int idDestination;
    private String nom_activity;
    private String image_activity;
    private String type;
    private String description;
    private Float activity_price;

    public Activity() {
    }

    public Activity(String nom_activity, String image_activity, String type, String description, Float activity_price, int idDestination) {
        this.nom_activity = nom_activity;
        this.image_activity = image_activity;
        this.type = type;
        this.description = description;
        this.activity_price = activity_price;
        this.idDestination = idDestination;
    }

    public Activity(int id_activity, String nom_activity, String image_activity, String type, String description, Float activity_price, int idDestination) {
        this.id_activity = id_activity;
        this.nom_activity = nom_activity;
        this.image_activity = image_activity;
        this.type = type;
        this.description = description;
        this.activity_price = activity_price;
        this.idDestination = idDestination;
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

    public int getIdDestination() {
        return idDestination;
    }

    public void setIdDestination(int idDestination) {
        this.idDestination = idDestination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return idDestination == activity.idDestination && Objects.equals(nom_activity, activity.nom_activity) && Objects.equals(image_activity, activity.image_activity) && Objects.equals(type, activity.type) && Objects.equals(description, activity.description) && Objects.equals(activity_price, activity.activity_price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom_activity, image_activity, type, description, activity_price, idDestination);
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id_activity=" + id_activity +
                ", nom_activity='" + nom_activity + '\'' +
                ", image_activity='" + image_activity + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", activity_price=" + activity_price +
                ", idDestination=" + idDestination +
                '}';
    }
}