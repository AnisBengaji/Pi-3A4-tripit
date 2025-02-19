package org.projeti.Service;

import java.sql.SQLException;
import java.util.List;

public interface  CRUD<T> {
<<<<<<< HEAD
    void add(T t) throws SQLException;
    void update(T t) throws SQLException;

    void delete(int id) throws SQLException;

    T getById(int id) throws SQLException;
    List<T> getAll() throws SQLException;
=======
    int insert(T t) throws SQLException;
    int update(T t) throws SQLException;
    int delete(T t) throws SQLException;

    List<T> showAll() throws SQLException;
>>>>>>> b4a66e2 (intergration interface+crud+controlS reservation+publication+evenement)
}


