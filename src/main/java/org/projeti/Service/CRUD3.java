package org.projeti.Service;

import java.sql.SQLException;
import java.util.List;

public interface CRUD3 <T> {

        void insert(T t) throws SQLException;
        void update(T t) throws SQLException;
        void delete(int id) throws SQLException;
        List<T> showAll() throws SQLException;
    }

