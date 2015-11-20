package com.pmoradi.system;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashSet;
import java.util.Set;

public class Repository {

    private static EntityManagerFactory entityManagerFactory;
    private static LockManager lockManager;

    public static EntityManagerFactory getEntityManagerFactory(){
        if(entityManagerFactory == null)
            entityManagerFactory = Persistence.createEntityManagerFactory("hibernate-engine");

        return entityManagerFactory;
    }

    public static LockManager getLockManager(){
        if(lockManager == null){
            lockManager = new LockManager() {

                private Set<Lock> locks = new HashSet<>();

                @Override
                public synchronized Lock lock(String name) throws InterruptedException {
                    final Lock lock = new EntityLock(name);

                    while(locks.contains(lock))
                        wait();

                    locks.add(lock);
                    return lock;
                }

                @Override
                public synchronized void unlock(Lock lock) {
                    locks.remove(lock);
                    notifyAll();
                }
            };
        }
        return lockManager;
    }
}
