package org.projeti.Service;

import org.projeti.entites.Offre;
import org.projeti.utils.Database;
import java.util.ArrayList;
import java.util.List;

import java.sql.*;
public  class OffreService implements CRUD<Offre>{
    private Connection cnx = Database.getInstance().getCnx();
    private Statement st ;
    private PreparedStatement ps ;

    @Override
    public int insert(Offre offre) throws SQLException {
        String req = " INSERT INTO offre(titre, description, prix, tutorial, destination) " +
                " VALUES ('"+offre.getTitre()+"','"+offre.getDescription()+"','"+offre.getPrix()+"','"+offre.getTutorial()+"','"+offre.getDestination()+"')";
        st = cnx.createStatement();
        return st.executeUpdate(req);
    }
    public int insert1(Offre offre) throws SQLException {
        String req = " INSERT INTO offre(titre, description, prix, tutorial, destination) VALUES (?,?,?,?,?)";
        ps = cnx.prepareStatement(req);
        ps.setString(1, offre.getTitre());
        ps.setString(2, offre.getDescription());
        ps.setFloat(3, offre.getPrix());
        ps.setString(4, offre.getTutorial());
        ps.setString(5, offre.getDestination());
        return ps.executeUpdate();
    }


    @Override
    public int update(Offre offre) throws SQLException{
        String req="UPDATE offre SET titre= ?,description= ?,prix= ?,tutorial= ?,destination= ? WHERE IdOffre= ?";

        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, offre.getTitre());
        ps.setString(2, offre.getDescription());
        ps.setDouble(3, offre.getPrix());
        ps.setString(4, offre.getTutorial());
        ps.setString(5, offre.getDestination());
        ps.setInt(6, offre.getIdOffre());

        return ps.executeUpdate();

    }

    @Override
    public int delete(Offre offre) throws SQLException{
        String req = "DELETE FROM offre WHERE IdOffre = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, offre.getIdOffre());
        return ps.executeUpdate();
    }

    @Override
    public List<Offre> showAll() throws SQLException{
        List <Offre> temp = new ArrayList<Offre>();
        String req = "SELECT * FROM Offre";
        st = cnx.createStatement();
        ResultSet rs = st.executeQuery(req);
        while(rs.next()){
            Offre u = new Offre();
            u.setIdOffre(rs.getInt(1));
            u.setTitre(rs.getString("titre"));
            u.setDescription(rs.getString("description"));
            u.setPrix(rs.getFloat("prix"));
            u.setTutorial(rs.getString("tutorial"));
            u.setDestination(rs.getString("destination"));

            temp.add(u);


        }

        return temp;
    }
}
