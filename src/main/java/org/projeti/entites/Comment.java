package org.projeti.entites;

import java.sql.Date;
import java.util.Objects;

public class Comment {
    private int id_comment;
    private String content;
    private String author;
    private Date date_comment;
    private int publication_id;  // Foreign key to link with Publication

    // Constructor
    public Comment(String content, String author, Date date_comment, int publication_id) {
        this.content = content;
        this.author = author;
        this.date_comment = date_comment;
        this.publication_id = publication_id;
    }

    public Comment() {}

    // Getters and Setters
    public int getId_comment() {
        return id_comment;
    }

    public void setId_comment(int id_comment) {
        this.id_comment = id_comment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDate_comment() {
        return date_comment;
    }

    public void setDate_comment(Date date_comment) {
        this.date_comment = date_comment;
    }

    public int getPublication_id() {
        return publication_id;
    }

    public void setPublication_id(int publication_id) {
        this.publication_id = publication_id;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id_comment=" + id_comment +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", date_comment=" + date_comment +
                ", publication_id=" + publication_id +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Comment comment = (Comment) object;
        return id_comment == comment.id_comment &&
                publication_id == comment.publication_id &&
                Objects.equals(content, comment.content) &&
                Objects.equals(author, comment.author) &&
                Objects.equals(date_comment, comment.date_comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_comment, content, author, date_comment, publication_id);
    }
}

