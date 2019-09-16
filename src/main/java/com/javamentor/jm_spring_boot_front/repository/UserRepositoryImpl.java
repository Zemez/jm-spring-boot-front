package com.javamentor.jm_spring_boot_front.repository;

import com.javamentor.jm_spring_boot_front.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository("userRepository")
public class UserRepositoryImpl extends AbstractRepository<User> implements UserRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    private final RestTemplate restTemplate;

    public UserRepositoryImpl(RestTemplate restTemplate) {
        super(User.class, User[].class, restTemplate);
        this.restTemplate = restTemplate;
    }

    @Override
    public User findByUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Invalid null username.");
        }
        User user = restTemplate.getForObject(getApiUrl() + "/name/" + username, User.class);
        logger.debug("Read user: {}", user);
        return user;

    }

}
