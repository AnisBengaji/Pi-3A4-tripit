package org.projeti.Service;

import org.projeti.entites.Evenement;
<<<<<<< HEAD
import org.projeti.utils.Database;
=======
>>>>>>> b4a66e2 (intergration interface+crud+controlS reservation+publication+evenement)

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
<<<<<<< HEAD

public  class EvenementService implements CRUD<Evenement> {
=======
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvenementService implements CRUD2<Evenement> {
>>>>>>> b4a66e2 (intergration interface+crud+controlS reservation+publication+evenement)
    private Connection conn;

    public EvenementService(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void add(Evenement evenement) {
<<<<<<< HEAD
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
=======
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
>>>>>>> b4a66e2 (intergration interface+crud+controlS reservation+publication+evenement)
        }
    }

    @Override
    public void update(Evenement evenement) {
<<<<<<< HEAD
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
=======
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
>>>>>>> b4a66e2 (intergration interface+crud+controlS reservation+publication+evenement)
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM evenement WHERE id_evenement = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
<<<<<<< HEAD
            if (rowsDeleted > 0) {
                System.out.println("Événement supprimé avec ID : " + id);
            } else {
                System.out.println("Aucun événement trouvé avec cet ID !");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'événement : " + e.getMessage());
=======
            System.out.println(rowsDeleted > 0 ? "Événement supprimé." : "Aucun événement trouvé.");
        } catch (SQLException e) {
            System.err.println("Erreur de suppression : " + e.getMessage());
>>>>>>> b4a66e2 (intergration interface+crud+controlS reservation+publication+evenement)
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
<<<<<<< HEAD
                        rs.getString("date_evenement"),
=======
                        rs.getString("date_evenement_depart"),
                        rs.getString("date_evenement_arriver"),
>>>>>>> b4a66e2 (intergration interface+crud+controlS reservation+publication+evenement)
                        rs.getString("lieu"),
                        rs.getString("description"),
                        rs.getFloat("price")
                );
<<<<<<< HEAD
            } else {
                System.out.println("Événement non trouvé !");
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'événement : " + e.getMessage());
            return null;
        }
=======
            }
        } catch (SQLException e) {
            System.err.println("Erreur de récupération : " + e.getMessage());
        }
        return null;
>>>>>>> b4a66e2 (intergration interface+crud+controlS reservation+publication+evenement)
    }

    @Override
    public List<Evenement> getAll() {
        List<Evenement> evenements = new ArrayList<>();
        String query = "SELECT * FROM evenement";
<<<<<<< HEAD
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
=======
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
>>>>>>> b4a66e2 (intergration interface+crud+controlS reservation+publication+evenement)
        }
        return evenements;
    }
}
<<<<<<< HEAD

=======
>>>>>>> b4a66e2 (intergration interface+crud+controlS reservation+publication+evenement)
