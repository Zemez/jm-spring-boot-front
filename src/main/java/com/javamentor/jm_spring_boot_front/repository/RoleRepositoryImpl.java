package com.javamentor.jm_spring_boot_front.repository;

import com.javamentor.jm_spring_boot_front.entity.Role;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository("roleRepository")
public class RoleRepositoryImpl extends AbstractRepository<Role> implements RoleRepository {

    private final RestTemplate restTemplate;

    public RoleRepositoryImpl(RestTemplate restTemplate) {
        super(Role.class, Role[].class, restTemplate);
        this.restTemplate = restTemplate;
    }

    @Override
    public Role findByName(String name) {
        return restTemplate.getForObject(getApiUrl() + "/name/" + name, Role.class);
    }

}
