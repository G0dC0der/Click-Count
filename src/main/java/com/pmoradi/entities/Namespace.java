package com.pmoradi.entities;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Namespace {

    @Id
    private String name;

    private String password;

    @OneToMany(mappedBy = "namespace", fetch = FetchType.LAZY)
    @Cascade(CascadeType.DELETE)
    private Set<URL> urls;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<URL> getUrls() {
        return urls;
    }

    public void setUrls(Set<URL> urls) {
        this.urls = urls;
    }
}
