package org.projeti.Service;

import org.projeti.entites.Offre;
import org.projeti.utils.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OffreService {

    private Connection cnx = Database.getInstance().getCnx();

    public int insert(Offre offre) throws SQLException {
        String req = "INSERT INTO offre (titre, description, prix, tutorial, destination) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, offre.getTitre());
            ps.setString(2, offre.getDescription());
            ps.setFloat(3, offre.getPrix());
            ps.setString(4, offre.getTutorial());
            ps.setString(5, offre.getDestination());
            return ps.executeUpdate();
        }
    }

    public int update(Offre offre) throws SQLException {
        String req = "UPDATE offre SET titre=?, description=?, prix=?, tutorial=?, destination=? WHERE idOffre=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, offre.getTitre());
            ps.setString(2, offre.getDescription());
            ps.setFloat(3, offre.getPrix());
            ps.setString(4, offre.getTutorial());
            ps.setString(5, offre.getDestination());
            ps.setInt(6, offre.getIdOffre());
            return ps.executeUpdate();
        }
    }

    public int delete(int idOffre) throws SQLException {
        String req = "DELETE FROM offre WHERE idOffre=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, idOffre);
            return ps.executeUpdate();
        }
    }



    public List<Offre> readAll() throws SQLException {
        List<Offre> offres = new ArrayList<>();
        String query = "select * from Offre";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()){
            int idOffre = rs.getInt("idOffre");
            String titre = rs.getString("titre");
            String description = rs.getString("description");
            float prix = rs.getFloat("prix");
            String tutorial = rs.getString("tutorial");
            String destination = rs.getString("destination");
            Offre p=new Offre(idOffre, titre, description, prix, tutorial, destination);
            offres.add(p);
        }

        return offres;
    }

    public List<Offre> getAll() throws SQLException {
        List<Offre> offres = new ArrayList<>();
        String req = "SELECT * FROM offre ORDER BY idOffre DESC";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int idOffre = rs.getInt("idOffre");
                String titre = rs.getString("titre");
                String description = rs.getString("description");
                float prix = rs.getFloat("prix");
                String tutorial = rs.getString("tutorial");
                String destination = rs.getString("destination");

                offres.add(new Offre(idOffre, titre, description, prix, tutorial, destination));
            }
        }
        return offres;
    }

    public Offre getById(int idOffre) throws SQLException {
        String req = "SELECT * FROM offre WHERE idOffre = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, idOffre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String titre = rs.getString("titre");
                String description = rs.getString("description");
                float prix = rs.getFloat("prix");
                String tutorial = rs.getString("tutorial");
                String destination = rs.getString("destination");

                return new Offre(idOffre, titre, description, prix, tutorial, destination);
            }
        }
        return null;
    }
}
