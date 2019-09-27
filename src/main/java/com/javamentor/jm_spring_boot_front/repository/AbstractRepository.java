package com.javamentor.jm_spring_boot_front.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@PropertySource("classpath:backend.properties")
public abstract class AbstractRepository<T> implements GenericRepository<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractRepository.class);

    private static final String INVALID_NULL_ID = "Invalid null id.";
    private final String INVALID_NULL_ENTITY;

    private String apiUrl;

    @Value("${backend.connection.proto}")
    private String proto;
    @Value("${backend.connection.host}")
    private String host;
    @Value("${backend.connection.port}")
    private int port;

    private final Class<T> entityClass;
    private final Class<T[]> entityArrayClass;
    private final RestTemplate restTemplate;

    public AbstractRepository(Class<T> entityClass, Class<T[]> entityArrayClass, RestTemplate restTemplate) {
        this.entityClass = entityClass;
        this.entityArrayClass = entityArrayClass;
        this.restTemplate = restTemplate;
        INVALID_NULL_ENTITY = String.format("Invalid null %s.", entityClass.getSimpleName());
    }

    @Override
    public T create(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException(INVALID_NULL_ENTITY);
        }
        T entityNew = restTemplate.postForObject(getApiUrl(), entity, entityClass);
        logger.debug("Create entity: {}", entityNew);
        return entityNew;
    }

    @Override
    public T findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(INVALID_NULL_ID);
        }
        T entity = restTemplate.getForObject(getApiUrl() + "/" + id, entityClass);
        logger.debug("Read entity: {}", entity);
        return entity;
    }

    @Override
    public List<T> findAll() {
        T[] result = restTemplate.getForObject(getApiUrl() + "/all", entityArrayClass);
        List<T> entities = result != null ? Arrays.asList(result) : Collections.emptyList();
        logger.debug("Read all: {}", entities);
        return entities;
    }

    @Override
    public T update(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException(INVALID_NULL_ENTITY);
        }
        T entityUpd = restTemplate.exchange(getApiUrl(), HttpMethod.PUT, new HttpEntity<>(entity), entityClass).getBody();
        logger.debug("Update entity: {}", entityUpd);
        return entityUpd;
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(INVALID_NULL_ID);
        }
        restTemplate.delete(getApiUrl() + "/" + id);
    }

    protected String getApiUrl() {
        if (apiUrl == null) {
            apiUrl = proto + "://" + host + ":" + port + "/api/" + entityClass.getSimpleName().toLowerCase();
        }
        return apiUrl;
    }

}
