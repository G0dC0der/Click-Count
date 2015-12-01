package com.pmoradi.system;

public interface LockManager {

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
            if(obj == null || obj instanceof Key == false)
                return false;

            Key lock = (Key) obj;
            return getName().equals(lock.getName());
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

    Key lock(String keyName);

    void unlock(Key key);
}