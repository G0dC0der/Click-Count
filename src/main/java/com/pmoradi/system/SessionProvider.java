package com.pmoradi.system;

import org.hibernate.Session;

public interface SessionProvider {

    Session provide();
}
