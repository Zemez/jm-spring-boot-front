package com.javamentor.jm_spring_boot_front.service;

import com.javamentor.jm_spring_boot_front.entity.Role;

public interface RoleService extends GenericService<Role> {

    Role findByName(String name);

}
