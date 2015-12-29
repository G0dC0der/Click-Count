package com.pmoradi.system;

import javax.persistence.EntityManager;

public interface SessionFactory {

    EntityManager newSession();
}
