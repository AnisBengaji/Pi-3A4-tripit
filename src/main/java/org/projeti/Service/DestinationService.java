package org.projeti.Service;

import org.projeti.entites.Destination;
import org.projeti.utils.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DestinationService implements CRUD<Destination> {
    private Connection cnx;
    public DestinationService() {
        cnx= Database.getInstance().getCnx();
    }
    @Override
    public int insert(Destination destination) throws SQLException {
        String query="INSERT INTO `destination`(`pays`, `ville`," +
                " `code_postal`, `latitude`," +
                " `longitude`) VALUES" +
                " ('"+destination.getPays()+"','"+destination.getVille()+"'," +
                "'"+destination.getCode_postal()+"','"+destination.getLatitude()+"','"+destination.getLongitude()+"')";
        Statement stmt=cnx.createStatement();
        stmt.executeUpdate(query);
        return 1;
    }

    @Override
    public int update(int id, Destination destination) throws SQLException {
        String query="UPDATE `destination` SET " +
                "`pays`='"+destination.getPays()+"',`ville`='"+destination.getVille()+"'," +
                "`code_postal`='"+destination.getCode_postal()+"',`latitude`='"+destination.getLatitude()+"'," +
                "`longitude`='"+destination.getLongitude()+"' WHERE idDestination="+id;
        Statement stmt=cnx.createStatement();
        stmt.executeUpdate(query);
        return 1;
    }

    @Override
    public int delete(int id) throws SQLException {
        String query="DELETE FROM `destination` WHERE idDestination="+id;
        Statement stmt=cnx.createStatement();
        stmt.executeUpdate(query);
        return 1;
    }

    @Override
    public List<Destination> showAll() throws SQLException {
        List<Destination> destinations=new ArrayList<Destination>();
        String query="SELECT * FROM `destination`";
        Statement stmt=cnx.createStatement();
        ResultSet rs=stmt.executeQuery(query);
        while(rs.next()){
            Destination destination=new Destination();
            destination.setId_Destination(rs.getInt("idDestination"));
            destination.setPays(rs.getString("pays"));
            destination.setVille(rs.getString("ville"));
            destination.setCode_postal(rs.getInt("code_postal"));
            destination.setLatitude(rs.getFloat("latitude"));
            destination.setLongitude(rs.getFloat("longitude"));
            destinations.add(destination);
        }
        return destinations;
    }
}
