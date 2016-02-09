package com.pmoradi.entities;

import com.pmoradi.security.Role;

import javax.persistence.*;

@Entity
@Table(name = "users", indexes = {@Index(columnList = "user_name", name = "username_index", unique = true)})
public class User {

    @Id
    @Column(name = "user_name", nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
