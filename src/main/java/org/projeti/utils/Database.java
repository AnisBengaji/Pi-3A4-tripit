package org.projeti.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
<<<<<<< HEAD
        private final String USER = "root";
        private final String PWD = "";
        private final String URL = "jdbc:mysql://localhost:3306/pi";

        //1st STEP
        public static Database instance;

        private Connection cnx;

        //2ND STEP
        private Database() {
            try {
                // Charger le driver explicitement
                Class.forName("com.mysql.cj.jdbc.Driver");
                cnx = DriverManager.getConnection(URL, USER, PWD);
                System.out.println("Connection établie !");
            } catch (SQLException | ClassNotFoundException e) {
                System.err.println("Échec de la connexion à la base de données !");
                e.printStackTrace();
            }
        }

        //3RD STEP
        public static Database getInstance(){
            if (instance == null) instance = new Database();
            return instance;
        }

        public Connection getCnx(){
            return cnx;
        }
    }

=======
    private final String USER = "root";
    private final String PWD = "";
    private final String URL = "jdbc:mysql://localhost:3306/projet";

    //1st STEP
    public static Database instance;

    private Connection cnx;

    //singeleton
    //limiter les instance en 1 seule
    private Database(){
        try {

            cnx = DriverManager.getConnection(URL, USER, PWD);
            System.out.println("Connection Etablie !");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    //3RD STEP
    public static Database getInstance(){
        if (instance == null) instance = new Database();
        return instance;
    }

    public Connection getCnx(){
        return cnx;
    }
}
>>>>>>> b4a66e2 (intergration interface+crud+controlS reservation+publication+evenement)
