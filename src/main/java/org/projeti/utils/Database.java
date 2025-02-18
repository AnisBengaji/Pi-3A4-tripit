package org.projeti.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
        private final String USER = "root";
        private final String PWD = "";
        private final String URL = "jdbc:mysql://localhost:3306/biblio_esprit";

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

