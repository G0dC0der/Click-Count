package com.pmoradi.entities;

import com.pmoradi.entities.URL.URLIdentifier;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@IdClass(URLIdentifier.class)
public class URL {

    public static class URLIdentifier implements java.io.Serializable{
        private Namespace namespace;
        private String alias;

        public Namespace getNamespace() {
            return namespace;
        }

        public void setNamespace(Namespace namespace) {
            this.namespace = namespace;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }
    }

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "namespace")
    @Cascade(CascadeType.ALL)
    private Namespace namespace;

    @Id
    private String alias;

    private String link;

    private Long clicks;

    private Long added;

    public Namespace getNamespace() {
        return namespace;
    }

    public void setNamespace(Namespace namespace) {
        this.namespace = namespace;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Long getClicks() {
        return clicks;
    }

    public void setClicks(Long clicks) {
        this.clicks = clicks;
    }

    public Long getAdded() {
        return added;
    }

    public void setAdded(Long added) {
        this.added = added;
    }
}