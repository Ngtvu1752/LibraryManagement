package org.example;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    List<T> getAll();

    Optional<T> findById(int id);

    boolean save(T t);

//    void delete(T t);
//
//    void update(T t);
}
