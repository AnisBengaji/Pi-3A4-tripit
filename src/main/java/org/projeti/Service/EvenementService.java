package org.projeti.Service;

import org.projeti.entites.Evenement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvenementService implements CRUD<Evenement> {
    private Connection conn;

    public EvenementService(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void add(Evenement evenement) {
        String query = "INSERT INTO evenement (type, date_evenement_depart, date_evenement_arriver, lieu, description, price) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, evenement.getType());
            stmt.setString(2, evenement.getDate_EvenementDepart());
            stmt.setString(3, evenement.getDate_EvenementDepart());
            stmt.setString(4, evenement.getLieu());
            stmt.setString(5, evenement.getDescription());
            stmt.setFloat(6, evenement.getPrice());

            stmt.executeUpdate();
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                evenement.setId_Evenement(generatedKeys.getInt(1));
            }
            System.out.println("Événement ajouté : " + evenement);
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    @Override
    public void update(Evenement evenement) {
        String query = "UPDATE evenement SET type = ?, date_evenement_depart = ?, date_evenement_arriver = ?, lieu = ?, description = ?, price = ? WHERE id_evenement = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, evenement.getType());
            stmt.setString(2, evenement.getDate_EvenementDepart());
            stmt.setString(3, evenement.getDate_EvenementArriver());
            stmt.setString(4, evenement.getLieu());
            stmt.setString(5, evenement.getDescription());
            stmt.setFloat(6, evenement.getPrice());
            stmt.setInt(7, evenement.getId_Evenement());

            int rowsUpdated = stmt.executeUpdate();
            System.out.println(rowsUpdated > 0 ? "Événement mis à jour !" : "Aucun événement trouvé.");
        } catch (SQLException e) {
            System.err.println("Erreur de mise à jour : " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM evenement WHERE id_evenement = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            System.out.println(rowsDeleted > 0 ? "Événement supprimé." : "Aucun événement trouvé.");
        } catch (SQLException e) {
            System.err.println("Erreur de suppression : " + e.getMessage());
        }
    }

    @Override
    public Evenement getById(int id) {
        String query = "SELECT * FROM evenement WHERE id_evenement = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Evenement(
                        rs.getInt("id_evenement"),
                        rs.getString("type"),
                        rs.getString("date_evenement_depart"),
                        rs.getString("date_evenement_arriver"),
                        rs.getString("lieu"),
                        rs.getString("description"),
                        rs.getFloat("price")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur de récupération : " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Evenement> getAll() {
        List<Evenement> evenements = new ArrayList<>();
        String query = "SELECT * FROM evenement";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                evenements.add(new Evenement(
                        rs.getInt("id_evenement"),
                        rs.getString("type"),
                        rs.getString("date_evenement_depart"),
                        rs.getString("date_evenement_arriver"),
                        rs.getString("lieu"),
                        rs.getString("description"),
                        rs.getFloat("price")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erreur de récupération : " + e.getMessage());
        }
        return evenements;
    }
}
