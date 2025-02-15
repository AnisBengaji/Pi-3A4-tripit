package org.projeti.Test;


import org.projeti.Service.EvenementService;
import org.projeti.Service.ReservationService;
import org.projeti.entites.Evenement;
import org.projeti.entites.Reservation;
import org.projeti.entites.Status;
import org.projeti.entites.User;
import org.projeti.utils.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        Database db = Database.getInstance();
        Connection conn = db.getCnx();

        // Vérification de la connexion
        if (conn == null) {
            System.err.println("Échec de la connexion à la base de données !");
            return;
        }



    }
}


