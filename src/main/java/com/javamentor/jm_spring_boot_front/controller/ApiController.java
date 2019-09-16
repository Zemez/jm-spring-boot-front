package com.javamentor.jm_spring_boot_front.controller;

import com.javamentor.jm_spring_boot_front.entity.User;
import com.javamentor.jm_spring_boot_front.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private UserService userService;

    @GetMapping(path = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> user(@PathVariable Long id) {
        User user = userService.findById(id);
        logger.debug("User read: {}", user);
        if (user == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(user);
        }
    }

    @GetMapping(path = "/user/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> users() {
        List<User> users = userService.findAll();
//        logger.debug("Users read: {}", users);
        if (users == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(users);
        }
    }

    @PostMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        User userNew = userService.create(user);
        logger.debug("User create: {}", userNew);
        if (userNew == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(userNew);
        }
    }

    @PutMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        User userUpd = userService.update(user);
        logger.debug("User update: {}", userUpd);
        if (userUpd == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(userUpd);
        }
    }

    @DeleteMapping(path = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            userService.deleteById(id);
            logger.debug("User delete: {}", id);
            return ResponseEntity.ok(Collections.singletonMap("message", "User successful deleted."));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
