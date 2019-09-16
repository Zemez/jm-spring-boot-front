package com.javamentor.jm_spring_boot_front.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@JsonIgnoreProperties(value = {"authorities", "admin", "user", "getNames"})
public class User implements UserDetails, CredentialsContainer {

//    private static final Logger logger = LoggerFactory.getLogger(User.class);

    private Long id;
    private String username;
    private String password;
    private boolean enabled = true;
    private boolean accountNonExpired = true;
    private boolean credentialsNonExpired = true;
    private boolean accountNonLocked = true;
    private Set<Role> roles;
    private String firstName;
    private String lastName;
    private String email;

    /************************
     ***   Constructors   ***
     ***********************/
    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public User(String username, String password, Set<Role> roles) {
        this(username, password, true, true, true, true, roles);
    }

    public User(String username, String password, boolean enabled, boolean accountNonExpired,
                boolean credentialsNonExpired, boolean accountNonLocked, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.roles = roles;
    }

    public User(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired,
                boolean accountNonLocked, Set<Role> roles, String firstName, String lastName, String email) {
        this(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, roles);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    /*******************
     ***   Methods   ***
     ******************/
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<String> getRoleNames() {
        if (roles == null) {
            return Collections.emptySet();
        }
        return roles.stream().map(Role::getName).collect(Collectors.toSet());
    }

    public boolean isAdmin() {
        return getRoleNames().contains("ADMIN");
    }

    public boolean isUser() {
        return getRoleNames().contains("USER");
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        if (roles == null) {
            return Collections.emptySortedSet();
        }
        return roles.stream().map(role -> (GrantedAuthority) () -> "ROLE_" + role).collect(Collectors.toSet());
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void eraseCredentials() {
        password = null;
    }

    @Override
    public String toString() {
        return "User: {" +
                " id: " + id +
                ", username: '" + username + '\'' +
                ", password: '" + password + '\'' +
                ", enabled: " + enabled +
                ", accountNonExpired: " + accountNonExpired +
                ", credentialsNonExpired: " + credentialsNonExpired +
                ", accountNonLocked: " + accountNonLocked +
                ", roles: " + roles +
                ", firstName: '" + firstName + '\'' +
                ", lastName: '" + lastName + '\'' +
                ", email: '" + email + '\'' +
                " }";
    }

}
