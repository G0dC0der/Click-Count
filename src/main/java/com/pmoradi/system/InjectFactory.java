package com.pmoradi.system;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmoradi.entities.dao.ClickDao;
import com.pmoradi.entities.dao.GroupDao;
import com.pmoradi.entities.dao.URLDao;
import com.pmoradi.rest.entries.AdminEntry;
import org.apache.commons.io.IOUtils;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.ServiceLocator;
import org.hibernate.engine.jdbc.StreamUtils;

import javax.ws.rs.core.Context;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

class InjectFactory {

    static Factory<ClickDao> getClickDaoFactory(SessionFactory sessionFactory){
        return new Factory<ClickDao>() {
            @Override
            public ClickDao provide() {
                return new ClickDao(sessionFactory);
            }

            @Override public void dispose(ClickDao clickDao) {}
        };
    }

    static Factory<GroupDao> getGroupDaoFactory(SessionFactory sessionFactory){
        return new Factory<GroupDao>() {
            @Override
            public GroupDao provide() {
                return new GroupDao(sessionFactory);
            }

            @Override public void dispose(GroupDao groupDao) {}
        };
    }

    static Factory<URLDao> getURLDaoFactory(SessionFactory sessionFactory){
        return new Factory<URLDao>() {
            @Override
            public URLDao provide() {
                return new URLDao(sessionFactory);
            }

            @Override public void dispose(URLDao urlDao) {}
        };
    }

    static Factory<Facade> getFacadeFactory(LockManager manager) {
        return new Factory<Facade>() {

            @Context
            ServiceLocator locator;

            @Override
            public Facade provide() {
                Facade facade = new Facade(manager);
                locator.inject(facade);
                return facade;
            }

            @Override
            public void dispose(Facade facade) {}
        };
    }

    static Factory<AdminFacade> getAdminFacadeFactory() {
        return new Factory<AdminFacade>() {

            @Context
            ServiceLocator locator;

            @Override
            public AdminFacade provide() {
                AdminFacade facade = new AdminFacade();
                locator.inject(facade);
                return facade;
            }

            @Override
            public void dispose(AdminFacade facade) {}
        };
    }


    static Factory<ApplicationSettings> getApplicationSettingsFactory(String domain, String path) {
        List<AdminEntry> admins = new ArrayList<>();
        InputStream stream = null;
        try {
            stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            String jsonData = IOUtils.toString(stream);

            ObjectMapper mapper = new ObjectMapper();
            admins.addAll(mapper.readValue(jsonData, new TypeReference<List<AdminEntry>>(){}));
        } catch (IOException e) {
            throw new RuntimeException("Admin data file could not be loaded.", e);
        } finally {
            IOUtils.closeQuietly(stream);
        }

        return new Factory<ApplicationSettings>() {
            @Override
            public ApplicationSettings provide() {
                return new ApplicationSettings() {

                    String publicIP;
                    {
                        try {
                            publicIP = InetAddress.getByName(getServerDomain()).getHostAddress();
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public String getServerIP() {
                        return publicIP;
                    }

                    @Override
                    public String getServerDomain() {
                        return domain;
                    }

                    @Override
                    public boolean isAdmin(String username, String password){
                        return  username != null &&
                                password != null &&
                                admins.contains(new AdminEntry(username, password));
                    }
                };
            }

            @Override
            public void dispose(ApplicationSettings instance) {}
        };
    }
}
