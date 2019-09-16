package com.javamentor.jm_spring_boot_front.repository;

import com.javamentor.jm_spring_boot_front.entity.User;

public interface UserRepository extends GenericRepository<User> {

    User findByUsername(String username);

}
