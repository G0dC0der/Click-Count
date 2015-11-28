package com.pmoradi.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "urls", indexes = {@Index(columnList = "url", name = "url_index", unique = true)})
public class URL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String link;

    @ManyToOne
    @JoinColumn(name = "group_fk")
    private Group group;

    @OneToMany(mappedBy = "url")
    private List<Click> clicks;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<Click> getClicks() {
        return clicks;
    }

    public void setClicks(List<Click> clicks) {
        this.clicks = clicks;
    }
}