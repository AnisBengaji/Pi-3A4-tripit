package org.projeti.Service;

import org.projeti.entites.Offre;
import org.projeti.entites.Tutorial;
import org.projeti.utils.Database;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TutorialService implements CRUD<Tutorial> {
    private Connection cnx = Database.getInstance().getCnx();

    @Override
    public int insert(Tutorial tutorial) throws SQLException {
        String req = "INSERT INTO tutorial(nom_tutorial, Date_debutTutorial, Date_finTutorial, prix_tutorial, offre) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, tutorial.getNom_tutorial());
            ps.setDate(2, Date.valueOf(tutorial.getDateDebut()));
            ps.setDate(3, Date.valueOf(tutorial.getDateFin()));
            ps.setFloat(4, tutorial.getPrix_tutorial());
            ps.setString(5, tutorial.getOffre());
            return ps.executeUpdate();
        }
    }

    @Override
    public int update(Tutorial tutorial) throws SQLException {
        String req = "UPDATE tutorial SET nom_tutorial=?, Date_debutTutorial=?, Date_finTutorial=?, prix_tutorial=?, offre=? WHERE id_tutorial=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, tutorial.getNom_tutorial());
            ps.setDate(2, Date.valueOf(tutorial.getDateDebut()));
            ps.setDate(3, Date.valueOf(tutorial.getDateFin()));
            ps.setFloat(4, tutorial.getPrix_tutorial());
            ps.setString(5, tutorial.getOffre());
            ps.setInt(6, tutorial.getId_tutorial());
            return ps.executeUpdate();
        }
    }

    @Override
    public int delete(Tutorial tutorial) throws SQLException {
        String req = "DELETE FROM tutorial WHERE id_tutorial=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, tutorial.getId_tutorial());
            return ps.executeUpdate();
        }
    }

    @Override
    public List<Tutorial> showAll() throws SQLException {
        List<Tutorial> tutorials = new ArrayList<>();
        String req = "SELECT * FROM tutorial";
        try (Statement st = cnx.createStatement(); ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                Tutorial tutorial = new Tutorial();
                tutorial.setId_tutorial(rs.getInt("id_tutorial"));
                tutorial.setNom_tutorial(rs.getString("nom_tutorial"));
                tutorial.setDateDebut(rs.getDate("Date_debutTutorial").toLocalDate());
                tutorial.setDateFin(rs.getDate("Date_finTutorial").toLocalDate());
                tutorial.setPrix_tutorial(rs.getFloat("prix_tutorial"));
                tutorial.setOffre(rs.getString("offre"));
                tutorials.add(tutorial);
            }
        }
        return tutorials;
    }

    public List<Tutorial> getAll() throws SQLException {
        List<Tutorial> tutorials = new ArrayList<>();
        String req = "SELECT * FROM tutorial ORDER BY id_tutorial DESC";
        try (PreparedStatement ps = cnx.prepareStatement(req); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Tutorial tutorial = new Tutorial();
                tutorial.setId_tutorial(rs.getInt("id_tutorial"));
                tutorial.setNom_tutorial(rs.getString("nom_tutorial"));
                tutorial.setDateDebut(rs.getDate("Date_debutTutorial").toLocalDate());
                tutorial.setDateFin(rs.getDate("Date_finTutorial").toLocalDate());
                tutorial.setPrix_tutorial(rs.getFloat("prix_tutorial"));
                tutorial.setOffre(rs.getString("offre"));
                tutorials.add(tutorial);
            }
        }
        return tutorials;
    }

    public Tutorial getById(int id_tutorial) throws SQLException {
        String req = "SELECT * FROM tutorial WHERE id_tutorial = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, id_tutorial);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Tutorial tutorial = new Tutorial();
                tutorial.setId_tutorial(rs.getInt("id_tutorial"));
                tutorial.setNom_tutorial(rs.getString("nom_tutorial"));
                tutorial.setDateDebut(rs.getDate("Date_debutTutorial").toLocalDate());
                tutorial.setDateFin(rs.getDate("Date_finTutorial").toLocalDate());
                tutorial.setPrix_tutorial(rs.getFloat("prix_tutorial"));
                tutorial.setOffre(rs.getString("offre"));
                return tutorial;
            }
        }
        return null;
    }
}
