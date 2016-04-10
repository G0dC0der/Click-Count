package com.pmoradi.essentials;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExpiringSet<T> {

    static class ExpiringObject {

        private final long expireTime;

        public ExpiringObject(final long millis) {
            this.expireTime = millis + System.currentTimeMillis();
        }

        public boolean hasExpired() {
            return System.currentTimeMillis() >= expireTime;
        }
    }

    private long cleanDelay;
    private Map<T, ExpiringObject> map;
    private Thread cleaner;

    public ExpiringSet() {
        this(60000);
    }

    public ExpiringSet(long cleanDelay) {
        this.map = new ConcurrentHashMap<>();
        this.cleanDelay = cleanDelay;

        this.cleaner = createCleanerThread(this);
        this.cleaner.setDaemon(true);
        this.cleaner.start();
    }


    public boolean put(T obj, long timeout) {
        ExpiringObject expiringObject = map.get(obj);
        if(expiringObject == null || expiringObject.hasExpired()) {
            map.put(obj, new ExpiringObject(timeout));
            return true;
        } else {
            return false;
        }
    }

    public boolean hasExpired(T obj) {
        ExpiringObject expiringObject = map.get(obj);
        return expiringObject == null || expiringObject.hasExpired();
    }

    public void setCleanDelay(long cleanDelay){
        this.cleanDelay = cleanDelay;
    }

    public int size() {
        return map.size();
    }

    public Thread getCleanerThread() {
        return cleaner;
    }

    private static <T> Thread createCleanerThread(ExpiringSet<T> expiringSet) {
        WeakReference<ExpiringSet<T>> ref = new WeakReference<>(expiringSet);
        expiringSet = null;

        Thread cleaner = new Thread(()->{
            try {
                while(!Thread.interrupted()) {
                    ref.get().map.entrySet()
                            .stream()
                            .filter(entry -> entry.getValue().hasExpired())
                            .forEach(entry -> ref.get().map.remove(entry.getKey()));

                    sleep(ref.get().cleanDelay);
                }
            } catch (NullPointerException e) {
                System.out.println("Cleaner thread terminated.");
            }
        });

        return cleaner;
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {}
    }
}
