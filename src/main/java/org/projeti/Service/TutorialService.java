package org.projeti.Service;

import org.projeti.entites.Tutorial;
import org.projeti.utils.Database;
import java.util.ArrayList;
import java.util.List;

import java.sql.*;
public  class TutorialService implements CRUD<Tutorial>{
    private Connection cnx = Database.getInstance().getCnx();
    private Statement st ;
    private PreparedStatement ps ;

    @Override
    public int insert(Tutorial tutorial) throws SQLException {
        String req = " INSERT INTO tutorial(nom_tutorial, Date_debutTutorial, Date_finTutorial, prix_tutorial, offre) " +
                " VALUES ('"+tutorial.getNom_tutorial()+"','"+tutorial.getDate_debutTutorial()+"','"+tutorial.getDate_finTutorial()+"','"+tutorial.getPrix_tutorial()+"','"+tutorial.getOffre()+ "')";
        st = cnx.createStatement();
        return st.executeUpdate(req);
    }
    public int insert2(Tutorial tutorial) throws SQLException {
        String req = " INSERT INTO tutorial(nom_tutorial, Date_debutTutorial, Date_finTutorial, prix_tutorial, offre) VALUES (?,?,?,?,?)";
        ps = cnx.prepareStatement(req);
        ps.setString(1, tutorial.getNom_tutorial());
        ps.setString(2, tutorial.getDate_debutTutorial());
        ps.setString(3, tutorial.getDate_finTutorial());
        ps.setFloat(4, tutorial.getPrix_tutorial());
        ps.setString(5, tutorial.getOffre());
        return ps.executeUpdate();
    }


    @Override
    public int update(Tutorial tutorial) throws SQLException{
        String req="UPDATE tutorial SET nom_tutorial= ?,Date_debutTutorial= ?,Date_finTutorial= ?,prix_tutorial= ?,offre= ? WHERE Nom_tutorial= ?";

        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, tutorial.getNom_tutorial());
        ps.setString(2, tutorial.getDate_debutTutorial());
        ps.setString(3, tutorial.getDate_finTutorial());
        ps.setFloat(4, tutorial.getPrix_tutorial());
        ps.setString(5, tutorial.getOffre());


        return ps.executeUpdate();

    }

    @Override
    public int delete(Tutorial tutorial) throws SQLException{
        String req = "DELETE FROM tutorial WHERE nom_tutorial = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, tutorial.getNom_tutorial());
        return ps.executeUpdate();
    }

    @Override
    public List<Tutorial> showAll() throws SQLException {
        List<Tutorial> temp = new ArrayList<>();
        String req = "SELECT * FROM tutorial";
        st = cnx.createStatement();
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            Tutorial tutorial = new Tutorial();
            tutorial.setNom_tutorial(rs.getString("nom_tutorial"));
            tutorial.setDate_debutTutorial(rs.getString("Date_debutTutorial"));
            tutorial.setDate_finTutorial(rs.getString("Date_finTutorial"));
            tutorial.setPrix_tutorial(rs.getFloat("prix_tutorial"));
            tutorial.setOffre(rs.getString("offre"));

            temp.add(tutorial);
        }

        return temp;
    }


}
