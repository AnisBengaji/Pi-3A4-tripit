package org.projeti.Service;

import org.projeti.entites.Evenement;
import org.projeti.entites.Reservation;
import org.projeti.entites.Status;
import org.projeti.entites.User;

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
        String query = "INSERT INTO reservation (id_reservation, status, price_total, mode_paiment, user_id, id_evenement) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, reservation.getId_reservation());
            ps.setString(2, reservation.getStatus().toString());
            ps.setFloat(3, reservation.getPrice_total());
            ps.setString(4, reservation.getMode_paiment());
            ps.setInt(5, reservation.getUser().getId()); // Assurez-vous que getId() existe dans User
            ps.setInt(6, reservation.getEvenement().getId_Evenement()); // Assurez-vous que getId_Evenement() existe dans Evenement
            ps.executeUpdate();
        }
    }

    @Override
    public void update(Reservation reservation) throws SQLException {
        String query = "UPDATE reservation SET status = ?, price_total = ?, mode_paiment = ?, user_id = ?, id_evenement = ? WHERE id_reservation = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, reservation.getStatus().toString());
            ps.setFloat(2, reservation.getPrice_total());
            ps.setString(3, reservation.getMode_paiment());
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
        Reservation reservation = new Reservation();
        reservation.setId_reservation(rs.getInt("id_reservation"));
        reservation.setStatus(Status.valueOf(rs.getString("status")));
        reservation.setPrice_total(rs.getFloat("price_total"));
        reservation.setMode_paiment(rs.getString("mode_paiment"));

        // Récupérer l'utilisateur associé
        User user = new User();
        user.setId(rs.getInt("user_id")); // Assurez-vous que User possède le setter setId(int)
        reservation.setUser(user);

        // Récupérer l'événement associé
        Evenement evenement = new Evenement();
        evenement.setId_Evenement(rs.getInt("id_evenement")); // Vérifiez que Evenement possède setId_Evenement(int)
        reservation.setEvenement(evenement);

        return reservation;
    }
}


