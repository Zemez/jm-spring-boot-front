package com.javamentor.jm_spring_boot_front.service;

import com.javamentor.jm_spring_boot_front.entity.Role;
import com.javamentor.jm_spring_boot_front.entity.User;
import com.javamentor.jm_spring_boot_front.repository.RoleRepository;
import com.javamentor.jm_spring_boot_front.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

@Service("userService")
@Transactional(readOnly = true)
public class UserServiceImpl extends AbstractService<User> implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final Map<String, Long> roleCache = new TreeMap<>();
    private final Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        super(userRepository);
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User create(User user) {
        if (user.getId() != null) {
            user.setId(null);
        }
        identifyAuthorities(user);
        injectBCryptPassword(user);
        return userRepository.create(user);
    }

    @Override
    @Transactional
    public User update(User user) {
        identifyAuthorities(user);
        injectBCryptPassword(user);
        if (userRepository.findById(user.getId()) == null) {
            throw new IllegalArgumentException("Invalid user.");
        }
        return userRepository.update(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found.");
        }
        org.springframework.security.core.userdetails.User.UserBuilder userBuilder =
                org.springframework.security.core.userdetails.User.withUserDetails(user);
        return userBuilder.build();
//        return user;
    }

    private void identifyAuthorities(User user) {
        for (Role role : user.getRoles()) {
            if (role.getId() == null) {
                String name = role.getName();
                if (roleCache.containsKey(name)) {
                    role.setId(roleCache.get(name));
                } else {
                    Role exists = roleRepository.findByName(name);
                    if (exists != null) {
                        Long id = exists.getId();
                        role.setId(id);
                        roleCache.put(name, id);
                    }
                }
                logger.debug("cache: {}", roleCache);
            }
        }
    }

    private void injectBCryptPassword(User user) {
        String password = user.getPassword();
        if (!BCRYPT_PATTERN.matcher(password).matches()) {
            user.setPassword(passwordEncoder.encode(password));
        }
    }

}
