package org.example;

import javafx.collections.ObservableList;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    List<T> getAll();

    public ObservableList<T> getObservableList();

    boolean save(T t);

    boolean delete(T t);
//
//    void update(T t);
}
