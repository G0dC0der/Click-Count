package com.pmoradi.entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "urls", indexes = {@Index(columnList = "url", name = "url_index", unique = false)})
public class URL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String link;

    @Column(name = "add_date")
    private Timestamp addDate;

    @ManyToOne
    @JoinColumn(name = "group_fk")
    private Group group;

    @Column(nullable = false)
    private Long clicks = 0L;

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

    public Timestamp getAddDate() {
        return addDate;
    }

    public void setAddDate(Timestamp addDate) {
        this.addDate = addDate;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Long getClicks() {
        return clicks;
    }

    public void setClicks(Long clicks) {
        this.clicks = clicks;
    }
}