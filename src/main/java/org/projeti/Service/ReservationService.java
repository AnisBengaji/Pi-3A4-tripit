package org.projeti.Service;

import org.projeti.entites.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ReservationService implements CRUD<Reservation> {
    private Connection connection;

    public ReservationService(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void add(Reservation reservation) throws SQLException {
        String query = "INSERT INTO reservation (status, price_total, mode_paiment, user_id, id_evenement) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, reservation.getStatus().toString());
            ps.setFloat(2, reservation.getPrice_total());
            ps.setString(3, reservation.getMode_paiment().toString());
            ps.setInt(4, reservation.getUser().getId()); // Assurez-vous que getId() existe dans User
            ps.setInt(5, reservation.getEvenement().getId_Evenement()); // Assurez-vous que getId_Evenement() existe dans Evenement

            // Exécuter l'insertion
            ps.executeUpdate();

            // Récupérer l'ID généré automatiquement
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                reservation.setId_reservation(generatedKeys.getInt(1)); // Assurez-vous que Reservation a un setter pour setId_reservation(int)
            }
        }
    }


    @Override
    public void update(Reservation reservation) throws SQLException {
        String query = "UPDATE reservation SET status = ?, price_total = ?, mode_paiment = ?, user_id = ?, id_evenement = ? WHERE id_reservation = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, reservation.getStatus().toString());
            ps.setFloat(2, reservation.getPrice_total());
            ps.setString(3, reservation.getMode_paiment().toString());
            ps.setInt(4, reservation.getUser().getId());
            ps.setInt(5, reservation.getEvenement().getId_Evenement());
            ps.setInt(6, reservation.getId_reservation());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM reservation WHERE id_reservation = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Reservation getById(int id) throws SQLException {
        String query = "SELECT * FROM reservation WHERE id_reservation = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToReservation(rs);
            }
        }
        return null;
    }

    @Override
    public List<Reservation> getAll() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservation";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        }
        return reservations;
    }

    private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        // Récupérer les informations de la réservation à partir du ResultSet
        int idReservation = rs.getInt("id_reservation");
        Status status = Status.valueOf(rs.getString("status").toUpperCase()); // Assurer que l'énumération est en majuscules
        float price = rs.getFloat("price_total");

        // Récupérer et convertir mode_paiment en ModePaiement
        String modePaiementString = rs.getString("mode_paiment").toUpperCase(); // Forcer en majuscules
        ModePaiement modePaiement;

        try {
            modePaiement = ModePaiement.valueOf(modePaiementString); // Conversion en enum
        } catch (IllegalArgumentException e) {
            // Gérer le cas où la valeur n'existe pas dans l'énumération
            System.out.println("Mode de paiement invalide : " + modePaiementString);
            modePaiement = ModePaiement.CARTE; // Valeur par défaut ou gérer autrement
        }

        // Créer une nouvelle réservation avec ces valeurs
        Reservation reservation = new Reservation(idReservation, status, price, modePaiement);

        // Récupérer l'utilisateur associé (peut nécessiter une récupération d'utilisateur réel depuis la base)
        int userId = rs.getInt("user_id");
        User user = new User(); // Si l'utilisateur est déjà dans la base de données, récupère-le ici
        user.setId(userId);
        reservation.setUser(user);

        // Récupérer l'événement associé (peut nécessiter une récupération d'événement réel depuis la base)
        int evenementId = rs.getInt("id_evenement");
        Evenement evenement = new Evenement(); // Idem pour l'événement
        evenement.setId_Evenement(evenementId);
        reservation.setEvenement(evenement);

        return reservation;
    }

}

