package org.projeti.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
   private final static String URL="jdbc:mysql://localhost:3306/projet";
   private final static String USER="root";
   private final static String PASSWORD="";
   private Connection cnx;
   private static Database instance;
   private Database() {
       try {
           cnx=DriverManager.getConnection(URL,USER,PASSWORD);
           System.out.println("Connected to database");
       } catch (SQLException e) {
           System.out.println(e.getMessage());
           throw new RuntimeException(e);
       }
   }
   public static Database getInstance() {
       if (instance == null) {
           instance = new Database();
       }else{
           System.out.println("Instance already exists");
       }
       return instance;
   }

    public Connection getCnx() {
        return cnx;
    }
}
