package com.pmoradi.system;

import java.util.HashSet;
import java.util.Set;

public class LockManager {

    public interface Key {
        String getName();
    }

    static class EntityKey implements Key {

        final String name;

        EntityKey(String name){
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == null || !(obj instanceof Key))
                return false;

            Key lock = (Key) obj;
            return getName().equals(lock.getName());
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

    private Set<Key> keys = new HashSet<>();

    public synchronized Key lock(String keyName) {
        final Key key = new EntityKey(keyName);

        while (keys.contains(key)) {
            try {
                wait();
            } catch (InterruptedException ignored) {}
        }
        keys.add(key);
        return key;
    }

    public synchronized void unlock(Key key) {
        keys.remove(key);
        notifyAll();
    }
}