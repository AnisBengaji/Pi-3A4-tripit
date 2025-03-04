package org.projeti.entites;

import java.sql.Date;
import java.util.Objects;

public class Activity {
    private int id_activity;
    private int idDestination;
    private String nom_activity;
    private String image_activity;
    private String image_activity2;  // New attribute
    private String image_activity3;  // New attribute
    private String type;
    private String description;
    private Float activity_price;
    private Date dateActivite;       // New attribute

    public Activity() {
    }

    // Constructor without id_activity (for new records)
    public Activity(String nom_activity, String image_activity, String image_activity2, String image_activity3, String type, String description, Float activity_price, Date dateActivite, int idDestination) {
        this.nom_activity = nom_activity;
        this.image_activity = image_activity;
        this.image_activity2 = image_activity2;
        this.image_activity3 = image_activity3;
        this.type = type;
        this.description = description;
        this.activity_price = activity_price;
        this.dateActivite = dateActivite;
        this.idDestination = idDestination;
    }

    // Constructor with id_activity (for existing records)
    public Activity(int id_activity, String nom_activity, String image_activity, String image_activity2, String image_activity3, String type, String description, Float activity_price, Date dateActivite, int idDestination) {
        this.id_activity = id_activity;
        this.nom_activity = nom_activity;
        this.image_activity = image_activity;
        this.image_activity2 = image_activity2;
        this.image_activity3 = image_activity3;
        this.type = type;
        this.description = description;
        this.activity_price = activity_price;
        this.dateActivite = dateActivite;
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

    public String getImage_activity2() {
        return image_activity2;
    }

    public void setImage_activity2(String image_activity2) {
        this.image_activity2 = image_activity2;
    }

    public String getImage_activity3() {
        return image_activity3;
    }

    public void setImage_activity3(String image_activity3) {
        this.image_activity3 = image_activity3;
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

    public Date getDateActivite() {
        return dateActivite;
    }

    public void setDateActivite(Date dateActivite) {
        this.dateActivite = dateActivite;
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
        if (!(o instanceof Activity)) return false;
        Activity activity = (Activity) o;
        return idDestination == activity.idDestination &&
                Objects.equals(nom_activity, activity.nom_activity) &&
                Objects.equals(image_activity, activity.image_activity) &&
                Objects.equals(image_activity2, activity.image_activity2) &&
                Objects.equals(image_activity3, activity.image_activity3) &&
                Objects.equals(type, activity.type) &&
                Objects.equals(description, activity.description) &&
                Objects.equals(activity_price, activity.activity_price) &&
                Objects.equals(dateActivite, activity.dateActivite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom_activity, image_activity, image_activity2, image_activity3, type, description, activity_price, dateActivite, idDestination);
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id_activity=" + id_activity +
                ", nom_activity='" + nom_activity + '\'' +
                ", image_activity='" + image_activity + '\'' +
                ", image_activity2='" + image_activity2 + '\'' +
                ", image_activity3='" + image_activity3 + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", activity_price=" + activity_price +
                ", dateActivite=" + dateActivite +
                ", idDestination=" + idDestination +
                '}';
    }
}
