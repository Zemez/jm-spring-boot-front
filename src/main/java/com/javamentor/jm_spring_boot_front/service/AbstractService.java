package com.javamentor.jm_spring_boot_front.service;

import com.javamentor.jm_spring_boot_front.repository.GenericRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public class AbstractService<T> implements GenericService<T> {

    private final GenericRepository<T> repository;

    public AbstractService(GenericRepository<T> repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public T create(T entity) {
        return repository.create(entity);
    }

    @Override
    public T findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public T update(T entity) {
        return repository.update(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
