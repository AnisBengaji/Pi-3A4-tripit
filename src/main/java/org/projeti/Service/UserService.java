package org.projeti.Service;

import org.projeti.entites.User;
import org.projeti.utils.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService  implements CRUD<User>{
    private Connection cnx = Database.getInstance().getCnx();
    private Statement st ;
    private PreparedStatement ps ;

    @Override
    public int insert(User user) throws SQLException {
        String req = " INSERT INTO `user`(`Nom`, `Prenom`, `Num_tel`, `Email`, `MDP`, `Role`) " +
                " VALUES ('"+user.getNom()+"','"+user.getPrenom()+"','"+user.getNum_tel()+"','"+user.getEmail()+"','"+user.getMDP()+"','"+ user.getRole() + "')";
        st = cnx.createStatement();
        return st.executeUpdate(req);
    }
    public int insert1(User user) throws SQLException {
        String req = " INSERT INTO `user`(`Nom`, `Prenom`, `Num_tel`, `Email`, `MDP`, `Role`) VALUES (?,?,?,?,?,?)";
        ps = cnx.prepareStatement(req);
        ps.setString(1, user.getNom());
        ps.setString(2, user.getPrenom());
        ps.setInt( 3, user.getNum_tel());
        ps.setString(4, user.getEmail());
        ps.setString(5, user.getMDP());
        ps.setString(6, user.getRole());
        return ps.executeUpdate();
    }


    @Override
    public int update(User user) throws SQLException{
        String req="UPDATE `user` SET `Nom`= ?,`Prenom`= ?,`Num_tel`= ?,`Email`= ?,`MDP`= ?,`Role`= ? WHERE `Id`= ?";

        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, user.getNom());
        ps.setString(2, user.getPrenom());
        ps.setInt(3, user.getNum_tel());
        ps.setString(4, user.getEmail());
        ps.setString(5, user.getMDP());
        ps.setString(6, user.getRole());
        ps.setInt(7, user.getId());

        return ps.executeUpdate();

    }

    @Override
    public int delete(User user) throws SQLException{
        String req = "DELETE FROM `user` WHERE `Id` = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, user.getId());
        return ps.executeUpdate();
    }

    @Override
    public List<User> showAll() throws SQLException{
        List <User> temp = new ArrayList<User>();
        String req = "SELECT * FROM `user`";
        st = cnx.createStatement();
        ResultSet rs = st.executeQuery(req);
        while(rs.next()){
            User u = new User();
            u.setId(rs.getInt(1));
            u.setNom(rs.getString("nom"));
            u.setPrenom(rs.getString("prenom"));
            u.setNum_tel(rs.getInt("num_tel"));
            u.setEmail(rs.getString("email"));
            u.setMDP(rs.getString("mdp"));
            u.setRole(rs.getString("role"));
            temp.add(u);


        }

        return temp;
    }

}