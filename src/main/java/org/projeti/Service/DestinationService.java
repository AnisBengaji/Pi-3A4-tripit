package org.projeti.Service;

import org.projeti.entites.Destination;
import org.projeti.utils.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DestinationService implements CRUD3<Destination> {
    private Connection cnx;

    public DestinationService() {
        cnx = Database.getInstance().getCnx();
    }

    @Override
    public void insert(Destination destination) throws SQLException {
        String query = "INSERT INTO destination (pays, ville, code_postal, latitude, longitude) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = cnx.prepareStatement(query);
        pstmt.setString(1, destination.getPays());
        pstmt.setString(2, destination.getVille());
        pstmt.setInt(3, destination.getCode_postal());
        pstmt.setFloat(4, destination.getLatitude());
        pstmt.setFloat(5, destination.getLongitude());
        pstmt.executeUpdate();
    }

    @Override
    public void update(Destination destination) throws SQLException {
        String query = "UPDATE destination SET pays = ?, ville = ?, code_postal = ?, latitude = ?, longitude = ? WHERE idDestination = ?";
        PreparedStatement pstmt = cnx.prepareStatement(query);
        pstmt.setString(1, destination.getPays());
        pstmt.setString(2, destination.getVille());
        pstmt.setInt(3, destination.getCode_postal());
        pstmt.setFloat(4, destination.getLatitude());
        pstmt.setFloat(5, destination.getLongitude());
        pstmt.setInt(6, destination.getId_Destination());
        pstmt.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM destination WHERE idDestination = ?";
        PreparedStatement pstmt = cnx.prepareStatement(query);
        pstmt.setInt(1, id);
        pstmt.executeUpdate();
    }

    @Override
    public List<Destination> showAll() throws SQLException {
        List<Destination> destinations = new ArrayList<>();
        String query = "SELECT * FROM destination";
        PreparedStatement pstmt = cnx.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            Destination destination = new Destination();
            destination.setId_Destination(rs.getInt("idDestination"));
            destination.setPays(rs.getString("pays"));
            destination.setVille(rs.getString("ville"));
            destination.setCode_postal(rs.getInt("code_postal"));
            destination.setLatitude(rs.getFloat("latitude"));
            destination.setLongitude(rs.getFloat("longitude"));
            destinations.add(destination);
        }
        return destinations;
    }
    public List<String> getPaysVille() throws SQLException {
        return showAll().stream().map(d->d.getPays()+"-"+d.getVille()).collect(Collectors.toList());
    }
    public int getIdByPaysVille(String paysville) throws SQLException {
        String[] parts=paysville.split("-");
        String pays=parts[0];
        String ville=parts[1];
        return showAll().stream().filter(
                d->d.getVille().equals(ville) && d.getPays().equals(pays)
        ).findFirst().get().getId_Destination();
    }
    public String getPaysVilleById(int id) throws SQLException {
        Destination destination = showAll().stream().filter(d->d.getId_Destination()==id).findFirst().get();
        return destination.getPays()+"-"+destination.getVille();
    }
}
