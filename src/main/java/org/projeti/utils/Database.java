package org.projeti.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private final String USER = "root";
    private final String PWD = "";
    private final String URL = "jdbc:mysql://localhost:3306/projet";

    //1st STEP
    public static Database instance;

    private Connection cnx;

    //2ND STEP
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
