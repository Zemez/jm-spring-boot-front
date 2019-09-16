package com.javamentor.jm_spring_boot_front.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

//@PropertySource("classpath:backend.properties")
public abstract class AbstractRepository<T> implements GenericRepository<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractRepository.class);

    private static final String INVALID_NULL_ID = "Invalid null id.";
    private final String INVALID_NULL_ENTITY;
    private final String API_URL;

    private final Class<T> entityClass;
    private final RestTemplate restTemplate;

    public AbstractRepository(Class<T> entityClass, RestTemplate restTemplate) {
        this.entityClass = entityClass;
        this.restTemplate = restTemplate;
        API_URL = "http://localhost:8081/api/" + entityClass.getSimpleName().toLowerCase();
        INVALID_NULL_ENTITY = String.format("Invalid null %s.", entityClass.getSimpleName());
    }

    @Override
    public T create(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException(INVALID_NULL_ENTITY);
        }
        T entityNew = restTemplate.postForEntity(getApiUrl(), entity, entityClass).getBody();
        logger.debug("Create entity: {}", entityNew);
        return entityNew;
    }

    @Override
    public T findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(INVALID_NULL_ID);
        }
        T entity = restTemplate.getForEntity(getApiUrl() + "/" + id, entityClass).getBody();
        logger.debug("Read entity: {}", entity);
        return entity;
    }

    @Override
    public List<T> findAll() {
        ResponseEntity<T[]> response = restTemplate.exchange(getApiUrl() + "/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<T[]>() {
                });
        List<T> entities = Arrays.asList(Objects.requireNonNull(response.getBody()));
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

    String getApiUrl() {
        return API_URL;
    }

}
