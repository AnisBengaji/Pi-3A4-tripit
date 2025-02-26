package org.projeti.Service;

import org.projeti.entites.Destination;
import org.projeti.entites.Activity;
import org.projeti.utils.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ActivityService implements CRUD<Activity> {
    private Connection cnx;
    public ActivityService() {
        cnx= Database.getInstance().getCnx();
    }
    @Override
    public void insert(Activity activity) throws SQLException {
        String sql = "insert into Activity (idDestination, nom_activity, image_activity, type, description, activity_price) values (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, activity.getIdDestination());
            ps.setString(2, activity.getNom_activity());
            ps.setString(3, activity.getImage_activity());
            ps.setString(4, activity.getType());
            ps.setString(5, activity.getDescription());
            ps.setFloat(6, activity.getActivity_price());
            ps.executeUpdate();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Activity activity) throws SQLException {
        String sql = "update Activity set idDestination = ?, nom_activity = ?, image_activity = ?, type = ?, description = ?, activity_price = ? where id_activity = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1, activity.getIdDestination());
        ps.setString(2, activity.getNom_activity());
        ps.setString(3, activity.getImage_activity());
        ps.setString(4, activity.getType());
        ps.setString(5, activity.getDescription());
        ps.setFloat(6, activity.getActivity_price());
        ps.setInt(7, activity.getId_activity());
        ps.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "delete from Activity where id_activity = ?";
        PreparedStatement ps =cnx.prepareStatement(sql);
        ps.setInt(1,id);
        ps.executeUpdate();
    }

    @Override
    public List<Activity> showAll() throws SQLException {
        String sql = "select * from Activity";
        Statement statement = cnx.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List <Activity> activities = new ArrayList<>();
        while (rs.next()){
            Activity a = new Activity();
            a.setId_activity(rs.getInt("id_activity"));
            a.setNom_activity(rs.getString("nom_activity"));
            a.setImage_activity(rs.getString("image_activity"));
            a.setType(rs.getString("type"));
            a.setDescription(rs.getString("description"));
            a.setIdDestination(rs.getInt("idDestination"));
            a.setActivity_price(rs.getFloat("activity_price"));
            activities.add(a);
        }
        return activities;
    }
    public List<Activity> triParCritere(String critere) throws SQLException {
        if(critere.equals("Nom")){
            return showAll().stream().sorted((d1,d2)->d1.getNom_activity().toLowerCase().compareTo(d2.getNom_activity().toLowerCase())).collect(Collectors.toList());
        }else if(critere.equals("Type")){
            return showAll().stream().sorted((d1,d2)->d1.getType().toLowerCase().compareTo(d2.getType().toLowerCase())).collect(Collectors.toList());

        }else{
            return showAll().stream().sorted((d1,d2)->Float.compare(d1.getActivity_price(),d2.getActivity_price())).collect(Collectors.toList());

        }
    }
}
