package org.projeti.Service;

import java.sql.SQLException;
import java.util.List;

public interface  CRUD<T> {
    void add(T t) throws SQLException;
    void update(T t) throws SQLException;

    void delete(int id) throws SQLException;

    T getById(int id) throws SQLException;
    List<T> getAll() throws SQLException;
}


