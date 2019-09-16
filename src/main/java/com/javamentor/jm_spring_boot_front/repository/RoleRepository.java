package com.javamentor.jm_spring_boot_front.repository;

import com.javamentor.jm_spring_boot_front.entity.Role;

public interface RoleRepository extends GenericRepository<Role> {

    Role findByName(String name);

}
