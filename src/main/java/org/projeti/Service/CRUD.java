package org.projeti.Service;

import java.sql.SQLException;
import java.util.List;

public interface  CRUD<T> {
    int insert(T t) throws SQLException;
    int update(int id,T t) throws SQLException;
    int delete(int id) throws SQLException;
    List<T> showAll() throws SQLException;
}


