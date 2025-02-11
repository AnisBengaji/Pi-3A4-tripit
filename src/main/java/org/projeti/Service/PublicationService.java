package org.projeti.Service;

import org.projeti.entites.Publication;
import org.projeti.utils.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PublicationService implements CRUD<Publication> {
    private Connection cnx = Database.getInstance().getCnx();
    private Statement st ;
    private PreparedStatement ps ;

    @Override
    public int insert(Publication publication) throws SQLException {
        String req = "INSERT INTO `publication`(`title`, `contenu`, `date_publication`, `author`, `visibility`, `image`) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        ps = cnx.prepareStatement(req);
        ps.setString(1, publication.getTitle());
        ps.setString(2, publication.getContenu());
        ps.setDate(3, publication.getDate_publication());
        ps.setString(4, publication.getAuthor());
        ps.setString(5, publication.getVisiblity());
        ps.setString(6, publication.getImage());

        return ps.executeUpdate();
    }
    @Override
    public int update(Publication publication) throws SQLException {
        String req = "UPDATE `publication` SET `title` = ?, `contenu` = ?, `date_publication` = ?, `author` = ?, `visibility` = ?, `image` = ? WHERE `id_publication` = ?";

        ps = cnx.prepareStatement(req);
        ps.setString(1, publication.getTitle());
        ps.setString(2, publication.getContenu());
        ps.setDate(3, publication.getDate_publication());
        ps.setString(4, publication.getAuthor());
        ps.setString(5, publication.getVisiblity());
        ps.setString(6, publication.getImage());
        ps.setInt(7, publication.getId_publication());

        return ps.executeUpdate();
    }


    @Override
    public int delete(Publication publication) throws SQLException {
        String req = "DELETE FROM `publication` WHERE id_publication = ?";

        ps = cnx.prepareStatement(req);
        ps.setInt(1, publication.getId_publication());

        return ps.executeUpdate();
    }

    @Override
    public List<Publication> showAll() throws SQLException{
        List<Publication> temp = new ArrayList<>();

        String req = "SELECT * FROM `publication`";

        st = cnx.createStatement();

        ResultSet rs = st.executeQuery(req);

        while (rs.next()){
            Publication p = new Publication();
            p.setId_publication(rs.getInt("id_publication"));
            p.setTitle(rs.getString("title"));
            p.setContenu(rs.getString("contenu"));
            p.setDate_publication(rs.getDate("date_publication"));
            p.setAuthor(rs.getString("author"));
            p.setVisiblity(rs.getString("visibility"));
            p.setImage(rs.getString("image"));


            temp.add(p);
        }

        return temp;
    }







}







