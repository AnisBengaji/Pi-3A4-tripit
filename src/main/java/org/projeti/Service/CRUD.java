package org.projeti.Service;

import java.sql.SQLException;
import java.util.List;

public interface CRUD<T> {
    int insert(T entity) throws SQLException;
    int update(T entity) throws SQLException;
    int delete(T entity) throws SQLException;
    List<T> showAll() throws SQLException;
}