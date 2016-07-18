package com.pmoradi.entities;

import com.pmoradi.entities.URL.URLIdentifier;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@IdClass(URLIdentifier.class)
public class URL {

    static class URLIdentifier implements java.io.Serializable{
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
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "namespace")
    private Namespace namespace;

    @Id
    private String alias;

    private String source;

    private Long clicks = 0L;

    private Long added;

    public URL() {
    }

    public URL(String alias, String source, Long clicks, Long added) {
        this.alias = alias;
        this.source = source;
        this.clicks = clicks;
        this.added = added;
    }

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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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