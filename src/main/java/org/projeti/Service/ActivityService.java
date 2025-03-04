package org.projeti.Service;

import org.projeti.entites.Activity;
import org.projeti.utils.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ActivityService implements CRUD<Activity> {
    private Connection cnx;

    public ActivityService() {
        cnx = Database.getInstance().getCnx();
    }

    @Override
    public void insert(Activity activity) throws SQLException {
        String sql = "INSERT INTO Activity (idDestination, nom_activity, image_activity, image_activity2, image_activity3, type, description, activity_price, date_activite) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, activity.getIdDestination());
            ps.setString(2, activity.getNom_activity());
            ps.setString(3, activity.getImage_activity());
            ps.setString(4, activity.getImage_activity2());
            ps.setString(5, activity.getImage_activity3());
            ps.setString(6, activity.getType());
            ps.setString(7, activity.getDescription());
            ps.setFloat(8, activity.getActivity_price());
            ps.setDate(9, activity.getDateActivite());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Activity activity) throws SQLException {
        String sql = "UPDATE Activity SET idDestination = ?, nom_activity = ?, image_activity = ?, image_activity2 = ?, image_activity3 = ?, type = ?, description = ?, activity_price = ?, date_activite = ? WHERE id_activity = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1, activity.getIdDestination());
        ps.setString(2, activity.getNom_activity());
        ps.setString(3, activity.getImage_activity());
        ps.setString(4, activity.getImage_activity2());
        ps.setString(5, activity.getImage_activity3());
        ps.setString(6, activity.getType());
        ps.setString(7, activity.getDescription());
        ps.setFloat(8, activity.getActivity_price());
        ps.setDate(9, activity.getDateActivite());
        ps.setInt(10, activity.getId_activity());
        ps.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Activity WHERE id_activity = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    @Override
    public List<Activity> showAll() throws SQLException {
        String sql = "SELECT * FROM Activity";
        Statement statement = cnx.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Activity> activities = new ArrayList<>();
        while (rs.next()) {
            Activity a = new Activity();
            a.setId_activity(rs.getInt("id_activity"));
            a.setNom_activity(rs.getString("nom_activity"));
            a.setImage_activity(rs.getString("image_activity"));
            a.setImage_activity2(rs.getString("image_activity2"));
            a.setImage_activity3(rs.getString("image_activity3"));
            a.setType(rs.getString("type"));
            a.setDescription(rs.getString("description"));
            a.setActivity_price(rs.getFloat("activity_price"));
            a.setDateActivite(rs.getDate("date_activite"));
            a.setIdDestination(rs.getInt("idDestination"));
            activities.add(a);
        }
        return activities;
    }

    public List<Activity> triParCritere(String critere) throws SQLException {
        if (critere.equals("Nom")) {
            return showAll().stream()
                    .sorted((d1, d2) -> d1.getNom_activity().toLowerCase().compareTo(d2.getNom_activity().toLowerCase()))
                    .collect(Collectors.toList());
        } else if (critere.equals("Type")) {
            return showAll().stream()
                    .sorted((d1, d2) -> d1.getType().toLowerCase().compareTo(d2.getType().toLowerCase()))
                    .collect(Collectors.toList());
        } else {
            return showAll().stream()
                    .sorted((d1, d2) -> Float.compare(d1.getActivity_price(), d2.getActivity_price()))
                    .collect(Collectors.toList());
        }
    }
}
