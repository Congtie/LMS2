package dao;

import java.util.List;
import java.util.Optional;

public interface GenericDAO<T> {
    T save(T entity);
    Optional<T> findById(int id);
    List<T> findAll();
    T update(T entity);
    boolean delete(int id);
}