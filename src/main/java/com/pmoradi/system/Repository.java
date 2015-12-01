package com.pmoradi.system;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashSet;
import java.util.Set;

public class Repository {

    private static EntityManagerFactory entityManagerFactory;
    private static LockManager lockManager;

    public static EntityManagerFactory getDatabase(){
        if(entityManagerFactory == null)
            entityManagerFactory = Persistence.createEntityManagerFactory("hibernate-engine");

        return entityManagerFactory;
    }

    public static EntityManager newSession(){
        return newSession();
    }

    public static LockManager getLockManager(){
        if(lockManager == null){
            lockManager = new LockManager() {

                private Set<Key> keys = new HashSet<>();

                @Override
                public synchronized Key lock(String keyName) {
                    final Key key = new EntityKey(keyName);

                    while(keys.contains(key)){
                        try {
                            wait();
                        } catch (InterruptedException e) {}
                    }

                    keys.add(key);
                    return key;
                }

                @Override
                public synchronized void unlock(Key key) {
                    keys.remove(key);
                    notifyAll();
                }
            };
        }
        return lockManager;
    }
}
