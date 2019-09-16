package com.javamentor.jm_spring_boot_front.repository;

import java.io.Serializable;
import java.util.List;

public interface GenericRepository<T> extends Serializable {

    T create(T entity);

    T findById(Long id);

    List<T> findAll();

    T update(T entity);

    void deleteById(Long id);

}
