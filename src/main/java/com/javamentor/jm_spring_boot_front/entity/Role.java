package com.javamentor.jm_spring_boot_front.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Comparator;
import java.util.Objects;

@JsonIgnoreProperties(value = {"authority"})
public class Role implements GrantedAuthority, Comparable {

    private Long id;
    private String name;

    public Role() {
    }

    public Role(String name) {
        Assert.hasText(name, "A granted authority textual representation is required");
        this.name = name;
    }

    public Role(Long id, String name) {
        this(name);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Role) {
            return name.equals(((Role) obj).name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Object obj) {
        int result;
        if (obj instanceof Role) {
            result = Objects.compare(name, ((Role) obj).getName(), Comparator.comparing(String::toLowerCase));
        } else {
            result = Objects.compare(this, obj, Comparator.comparing(Objects::toString));
        }
        return result;
    }

}
