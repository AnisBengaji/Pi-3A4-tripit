package org.projeti.Service;

import org.projeti.entites.Categorie;
<<<<<<< HEAD

/*public  class CategorieService implements CRUD<Categorie> {
}
*/
=======
import org.projeti.utils.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategorieService implements CRUD<Categorie> {
    private Connection cnx = Database.getInstance().getCnx();
    private PreparedStatement ps;
    private Statement st;

    @Override
    public int insert(Categorie categorie) throws SQLException {
        String req = "INSERT INTO categorie (nomCategorie, description) VALUES (?, ?)";
        ps = cnx.prepareStatement(req);

        ps.setString(1, categorie.getNomCategorie());
        ps.setString(2, categorie.getDescription());

        return ps.executeUpdate();
    }



    @Override
    public int update(Categorie categorie) throws SQLException {
        String req = "UPDATE `categorie` SET `nomCategorie` = ?, `description` = ? WHERE `idCategorie` = ?";
        ps = cnx.prepareStatement(req);
        ps.setString(1, categorie.getNomCategorie());
        ps.setString(2, categorie.getDescription());
        ps.setInt(3, categorie.getIdCategorie());

        return ps.executeUpdate();
    }


    @Override
    public int delete(Categorie categorie) throws SQLException {
        String req = "DELETE FROM `categorie` WHERE idCategorie = ?";

        ps = cnx.prepareStatement(req);
        ps.setInt(1, categorie.getIdCategorie());

        return ps.executeUpdate();
    }



    @Override
    public List<Categorie> showAll() throws SQLException {
        List<Categorie> categories = new ArrayList<>();
        String req = "SELECT * FROM `categorie`";
        st = cnx.createStatement();
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            Categorie categorie = new Categorie();
            categorie.setIdCategorie(rs.getInt("idCategorie")); // Set the idCategorie
            categorie.setNomCategorie(rs.getString("nomCategorie"));
            categorie.setDescription(rs.getString("description"));
            categories.add(categorie);
        }

        return categories;
    }

    public boolean exists(String nomCategorie) throws SQLException {
        String req = "SELECT 1 FROM `categorie` WHERE nomCategorie = ?";
        ps = cnx.prepareStatement(req);
        ps.setString(1, nomCategorie);
        ResultSet rs = ps.executeQuery();

        return rs.next();  // If a result exists, the category name already exists
    }
}
>>>>>>> b4a66e2 (intergration interface+crud+controlS reservation+publication+evenement)
