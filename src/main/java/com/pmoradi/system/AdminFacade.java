package com.pmoradi.system;

import com.pmoradi.entities.dao.ClickDao;
import com.pmoradi.entities.dao.GroupDao;
import com.pmoradi.entities.dao.URLDao;

import javax.inject.Inject;

public class AdminFacade {

    @Inject
    private ClickDao clickDAO;
    @Inject
    private GroupDao groupDAO;
    @Inject
    private URLDao urlDAO;
    @Inject
    private ApplicationSettings settings;

    public void suspend(){

    }

    public void deleteGroup(String groupName) {

    }

    public void deleteURL(String groupName, String urlName) {

    }

    public void renameGroup(String groupName) {

    }

    public void renameURL(String groupName, String urlName) {

    }

    public void invalidateGroup(String group) {
        //Set all this groups url to error page
    }

    public void cheat(String groupName, String urlName, int countManipulation) {

    }

    public void verifyAdmin(String username, String password) {

    }
}
