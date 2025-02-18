//package org.projeti.Service;

import org.projeti.entites.Evenement;
import org.projeti.utils.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*public  class EvenementService implements CRUD<Evenement> {
    private Connection conn;

    public EvenementService(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void add(Evenement evenement) {
        String query = "INSERT INTO evenement (id_evenement, type, date_evenement, lieu, description, price) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, evenement.getId_Evenement());
            stmt.setString(2, evenement.getType());
            stmt.setString(3, evenement.getDate_Evenement());
            stmt.setString(4, evenement.getLieu());
            stmt.setString(5, evenement.getDescription());
            stmt.setFloat(6, evenement.getPrice());
            stmt.executeUpdate();
            System.out.println("Événement ajouté à la base de données : " + evenement);
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'événement : " + e.getMessage());
        }
    }

    @Override
    public void update(Evenement evenement) {
        String query = "UPDATE evenement SET type = ?, date_evenement = ?, lieu = ?, description = ?, price = ? WHERE id_evenement = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, evenement.getType());
            stmt.setString(2, evenement.getDate_Evenement());
            stmt.setString(3, evenement.getLieu());
            stmt.setString(4, evenement.getDescription());
            stmt.setFloat(5, evenement.getPrice());
            stmt.setInt(6, evenement.getId_Evenement());
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Événement mis à jour : " + evenement);
            } else {
                System.out.println("Événement non trouvé pour la mise à jour !");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'événement : " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM evenement WHERE id_evenement = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Événement supprimé avec ID : " + id);
            } else {
                System.out.println("Aucun événement trouvé avec cet ID !");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'événement : " + e.getMessage());
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
                        rs.getString("date_evenement"),
                        rs.getString("lieu"),
                        rs.getString("description"),
                        rs.getFloat("price")
                );
            } else {
                System.out.println("Événement non trouvé !");
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'événement : " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Evenement> getAll() {
        List<Evenement> evenements = new ArrayList<>();
        String query = "SELECT * FROM evenement";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Evenement evenement = new Evenement(
                        rs.getInt("id_evenement"),
                        rs.getString("type"),
                        rs.getString("date_evenement"),
                        rs.getString("lieu"),
                        rs.getString("description"),
                        rs.getFloat("price")
                );
                evenements.add(evenement);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des événements : " + e.getMessage());
        }
        return evenements;
    }
}*/

