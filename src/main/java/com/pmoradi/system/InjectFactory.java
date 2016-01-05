package com.pmoradi.system;

import com.pmoradi.entities.dao.ClickDao;
import com.pmoradi.entities.dao.GroupDao;
import com.pmoradi.entities.dao.URLDao;
import org.glassfish.hk2.api.Factory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
            @Override
            public Facade provide() {
                return new Facade(manager);
            }

            @Override
            public void dispose(Facade facade) {}
        };
    }

    static Factory<ApplicationSettings> getApplicationSettingsFactory(String domain, String path) {
        Properties props;
        try {
            InputStream stream = new BufferedInputStream(new FileInputStream(path));
            props = new Properties();
            props.load(stream);
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException("credentials.properties was not found.");
        }

        return new Factory<ApplicationSettings>() {
            @Override
            public ApplicationSettings provide() {
                return new ApplicationSettings() {

                    String publicIP;
                    {
                        try {
                            publicIP = InetAddress.getByName(getServerDomain()).getHostAddress();
                        } catch (UnknownHostException e) {}
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
                    public String getAdminUsername() {
                        return props.getProperty("admin.username");
                    }

                    @Override
                    public String getAdminPassword() {
                        return props.getProperty("admin.password");
                    }
                };
            }

            @Override
            public void dispose(ApplicationSettings instance) {}
        };
    }
}
