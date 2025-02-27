package org.projeti.Service;

import org.projeti.entites.Categorie;
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
        String req = "INSERT INTO `publication`(`title`, `contenu`, `date_publication`, `author`, `visibility`, `image`, `idCategorie`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        ps = cnx.prepareStatement(req);
        ps.setString(1, publication.getTitle());
        ps.setString(2, publication.getContenu());
        ps.setDate(3, publication.getDate_publication());
        ps.setString(4, publication.getAuthor());
        ps.setString(5, publication.getVisibility());
        ps.setString(6, publication.getImage());
        ps.setInt(7, publication.getCategorie().getIdCategorie());

        return ps.executeUpdate();
    }
    @Override
    public int update(Publication publication) throws SQLException {
        String req = "UPDATE `publication` SET `title` = ?, `contenu` = ?, `date_publication` = ?, `author` = ?, `visibility` = ?, `image` = ?, `idCategorie` = ? WHERE `id_publication` = ?";

        ps = cnx.prepareStatement(req);
        ps.setString(1, publication.getTitle());
        ps.setString(2, publication.getContenu());
        ps.setDate(3, publication.getDate_publication());
        ps.setString(4, publication.getAuthor());
        ps.setString(5, publication.getVisibility());
        ps.setString(6, publication.getImage());

        // Handle the category ID (use a default value if null)
        int categorieId = (publication.getCategorie() != null) ? publication.getCategorie().getIdCategorie() : 0;
        ps.setInt(7, categorieId);

        // Set the ID for the WHERE clause
        ps.setInt(8, publication.getId_publication());

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
    public List<Publication> showAll() throws SQLException {
        List<Publication> temp = new ArrayList<>();

        // Join publication with categorie to fetch category name
        String req = "SELECT p.*, c.nomCategorie FROM publication p " +
                "LEFT JOIN categorie c ON p.idCategorie = c.idCategorie";

        st = cnx.createStatement();
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            Publication p = new Publication();
            p.setId_publication(rs.getInt("id_publication"));
            p.setTitle(rs.getString("title"));
            p.setContenu(rs.getString("contenu"));
            p.setDate_publication(rs.getDate("date_publication"));
            p.setAuthor(rs.getString("author"));
            p.setVisibility(rs.getString("visibility"));
            p.setImage(rs.getString("image"));

            int idCategorie = rs.getInt("idCategorie");
            String nomCategorie = rs.getString("nomCategorie"); // Fetch category name

            // Ensure Categorie class has a constructor accepting id and name
            Categorie categorie = new Categorie(idCategorie, nomCategorie);
            p.setCategorie(categorie);

            temp.add(p);
        }

        return temp;
    }

    public void deleteByCategory(int categoryId) throws SQLException {
        String query = "DELETE FROM publication WHERE idCategorie = ?";
        {
            ps.setInt(1, categoryId);
            ps.executeUpdate();
        }
    }

    public List<Publication> searchPublications(String searchTerm) throws SQLException {
        List<Publication> result = new ArrayList<>();
        String searchTermLike = "%" + searchTerm + "%";

        String req = "SELECT p.*, c.nomCategorie FROM publication p " +
                "LEFT JOIN categorie c ON p.idCategorie = c.idCategorie " +
                "WHERE p.title LIKE ? OR p.contenu LIKE ? OR p.author LIKE ?";

        ps = cnx.prepareStatement(req);
        ps.setString(1, searchTermLike);
        ps.setString(2, searchTermLike);
        ps.setString(3, searchTermLike);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Publication p = new Publication();
            p.setId_publication(rs.getInt("id_publication"));
            p.setTitle(rs.getString("title"));
            p.setContenu(rs.getString("contenu"));
            p.setDate_publication(rs.getDate("date_publication"));
            p.setAuthor(rs.getString("author"));
            p.setVisibility(rs.getString("visibility"));
            p.setImage(rs.getString("image"));

            int idCategorie = rs.getInt("idCategorie");
            String nomCategorie = rs.getString("nomCategorie");

            Categorie categorie = new Categorie(idCategorie, nomCategorie);
            p.setCategorie(categorie);

            result.add(p);
        }

        return result;
    }

    public List<Publication> getPublicationsByCategory(String categoryName) throws SQLException {
        List<Publication> result = new ArrayList<>();

        String req = "SELECT p.*, c.nomCategorie FROM publication p " +
                "JOIN categorie c ON p.idCategorie = c.idCategorie " +
                "WHERE c.nomCategorie = ?";

        ps = cnx.prepareStatement(req);
        ps.setString(1, categoryName);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Publication p = new Publication();
            p.setId_publication(rs.getInt("id_publication"));
            p.setTitle(rs.getString("title"));
            p.setContenu(rs.getString("contenu"));
            p.setDate_publication(rs.getDate("date_publication"));
            p.setAuthor(rs.getString("author"));
            p.setVisibility(rs.getString("visibility"));
            p.setImage(rs.getString("image"));

            int idCategorie = rs.getInt("idCategorie");
            String nomCategorie = rs.getString("nomCategorie");

            Categorie categorie = new Categorie(idCategorie, nomCategorie);
            p.setCategorie(categorie);

            result.add(p);
        }

        return result;
    }

    public List<Publication> getPublicationsByAuthor(String authorName) throws SQLException {
        List<Publication> result = new ArrayList<>();

        String req = "SELECT p.*, c.nomCategorie FROM publication p " +
                "LEFT JOIN categorie c ON p.idCategorie = c.idCategorie " +
                "WHERE p.author = ?";

        ps = cnx.prepareStatement(req);
        ps.setString(1, authorName);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Publication p = new Publication();
            p.setId_publication(rs.getInt("id_publication"));
            p.setTitle(rs.getString("title"));
            p.setContenu(rs.getString("contenu"));
            p.setDate_publication(rs.getDate("date_publication"));
            p.setAuthor(rs.getString("author"));
            p.setVisibility(rs.getString("visibility"));
            p.setImage(rs.getString("image"));

            int idCategorie = rs.getInt("idCategorie");
            String nomCategorie = rs.getString("nomCategorie");

            Categorie categorie = new Categorie(idCategorie, nomCategorie);
            p.setCategorie(categorie);

            result.add(p);
        }

        return result;
    }

    public List<Publication> getPublicationsByVisibility(String visibility) throws SQLException {
        List<Publication> result = new ArrayList<>();

        String req = "SELECT p.*, c.nomCategorie FROM publication p " +
                "LEFT JOIN categorie c ON p.idCategorie = c.idCategorie " +
                "WHERE p.visibility = ?";

        ps = cnx.prepareStatement(req);
        ps.setString(1, visibility);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Publication p = new Publication();
            p.setId_publication(rs.getInt("id_publication"));
            p.setTitle(rs.getString("title"));
            p.setContenu(rs.getString("contenu"));
            p.setDate_publication(rs.getDate("date_publication"));
            p.setAuthor(rs.getString("author"));
            p.setVisibility(rs.getString("visibility"));
            p.setImage(rs.getString("image"));

            int idCategorie = rs.getInt("idCategorie");
            String nomCategorie = rs.getString("nomCategorie");

            Categorie categorie = new Categorie(idCategorie, nomCategorie);
            p.setCategorie(categorie);

            result.add(p);
        }

        return result;
    }

    public void deletePublication(int publicationId) throws SQLException {
        Publication publication = new Publication();
        publication.setId_publication(publicationId);
        delete(publication);
    }
}















