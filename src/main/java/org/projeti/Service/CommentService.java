package org.projeti.Service;

import org.projeti.Service.CRUD;
import org.projeti.entites.Comment;
import org.projeti.utils.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentService implements CRUD<Comment> {
    private Connection cnx = Database.getInstance().getCnx();
    private Statement st;
    private PreparedStatement ps;

    @Override
    public int insert(Comment comment) throws SQLException {
        String req = "INSERT INTO `comment`(`content`, `author`, `date_comment`, `publication_id`) VALUES (?, ?, ?, ?)";
        ps = cnx.prepareStatement(req);
        ps.setString(1, comment.getContent());
        ps.setString(2, comment.getAuthor());
        ps.setDate(3, comment.getDate_comment());
        ps.setInt(4, comment.getPublication_id());
        return ps.executeUpdate();
    }

    @Override
    public int update(Comment comment) throws SQLException {
        String req = "UPDATE `comment` SET `content` = ?, `author` = ?, `date_comment` = ? WHERE `id_comment` = ?";
        ps = cnx.prepareStatement(req);
        ps.setString(1, comment.getContent());
        ps.setString(2, comment.getAuthor());
        ps.setDate(3, comment.getDate_comment());
        ps.setInt(4, comment.getId_comment());
        return ps.executeUpdate();
    }

    @Override
    public int delete(Comment comment) throws SQLException {
        String req = "DELETE FROM `comment` WHERE id_comment = ?";
        ps = cnx.prepareStatement(req);
        ps.setInt(1, comment.getId_comment());
        return ps.executeUpdate();
    }

    @Override
    public List<Comment> showAll() throws SQLException {
        List<Comment> comments = new ArrayList<>();
        String req = "SELECT * FROM comment";
        st = cnx.createStatement();
        ResultSet rs = st.executeQuery(req);
        while (rs.next()) {
            Comment c = new Comment();
            c.setId_comment(rs.getInt("id_comment"));
            c.setContent(rs.getString("content"));
            c.setAuthor(rs.getString("author"));
            c.setDate_comment(rs.getDate("date_comment"));
            c.setPublication_id(rs.getInt("publication_id"));
            comments.add(c);
        }
        return comments;
    }

    public List<Comment> getCommentsByPublication(int publicationId) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        String req = "SELECT * FROM comment WHERE publication_id = ?";
        ps = cnx.prepareStatement(req);
        ps.setInt(1, publicationId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Comment c = new Comment();
            c.setId_comment(rs.getInt("id_comment"));
            c.setContent(rs.getString("content"));
            c.setAuthor(rs.getString("author"));
            c.setDate_comment(rs.getDate("date_comment"));
            c.setPublication_id(rs.getInt("publication_id"));
            comments.add(c);
        }
        return comments;
    }
}
