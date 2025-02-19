package org.projeti.Service;

<<<<<<< HEAD
import org.projeti.entites.Evenement;
import org.projeti.entites.Reservation;
import org.projeti.entites.Status;
import org.projeti.entites.User;
=======
import org.projeti.entites.*;
>>>>>>> b4a66e2 (intergration interface+crud+controlS reservation+publication+evenement)

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
<<<<<<< HEAD

public class ReservationService implements CRUD<Reservation> {
=======
public class ReservationService implements CRUD2<Reservation> {
>>>>>>> b4a66e2 (intergration interface+crud+controlS reservation+publication+evenement)
    private Connection connection;

    public ReservationService(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void add(Reservation reservation) throws SQLException {
<<<<<<< HEAD
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

=======
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


>>>>>>> b4a66e2 (intergration interface+crud+controlS reservation+publication+evenement)
    @Override
    public void update(Reservation reservation) throws SQLException {
        String query = "UPDATE reservation SET status = ?, price_total = ?, mode_paiment = ?, user_id = ?, id_evenement = ? WHERE id_reservation = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, reservation.getStatus().toString());
            ps.setFloat(2, reservation.getPrice_total());
<<<<<<< HEAD
            ps.setString(3, reservation.getMode_paiment());
=======
            ps.setString(3, reservation.getMode_paiment().toString());
>>>>>>> b4a66e2 (intergration interface+crud+controlS reservation+publication+evenement)
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
<<<<<<< HEAD
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
=======
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
>>>>>>> b4a66e2 (intergration interface+crud+controlS reservation+publication+evenement)
        reservation.setEvenement(evenement);

        return reservation;
    }
<<<<<<< HEAD
}


=======

}

>>>>>>> b4a66e2 (intergration interface+crud+controlS reservation+publication+evenement)
