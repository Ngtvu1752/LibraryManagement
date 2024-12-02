package org.example.Database.DAO;

import javafx.collections.ObservableList;

import java.util.List;

public interface DAO<T> {
    List<T> getAll();

    public ObservableList<T> getObservableList();

    boolean save(T t);

    boolean delete(T t);

    boolean update(T t);
}
