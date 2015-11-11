package com.pmoradi.system;

import com.pmoradi.dao.ClickDao;
import com.pmoradi.dao.GroupDao;
import com.pmoradi.dao.URLDao;

class Singeltons {

    static final ClickDao clickDao = new ClickDao();
    static final GroupDao groupDao = new GroupDao();
    static final URLDao urlDao = new URLDao();
    static final Engineering ngineering = new Engineering();
}
