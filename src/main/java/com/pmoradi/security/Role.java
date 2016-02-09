package com.pmoradi.security;

public enum Role {

    ADMINISTRATOR(2),
    MAINTAINER(1),
    TRUSTED(0);

    private final int level;

    private Role(int level){
        this.level = level;
    }

    public boolean isAbove(Role role) {
        return this.level > role.level;
    }
}
