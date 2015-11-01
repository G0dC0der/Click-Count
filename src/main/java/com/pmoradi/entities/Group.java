package com.pmoradi.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "groups", indexes = {@Index(columnList = "groupname", name = "group_index", unique = true)})
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "groupname",nullable = false)
    private String groupName;

    @Column(nullable = false)
    private String password = "";

    @OneToMany(mappedBy = "group")
    private List<URL> urls;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<URL> getUrls() {
        return urls;
    }

    public void setUrls(List<URL> urls) {
        this.urls = urls;
    }
}