package com.pmoradi.system;

public interface LockManager {

    public interface Lock{
        String getName();
    }

    static class EntityLock implements Lock{

        final String name;

        EntityLock(String name){
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == null || obj instanceof Lock == false)
                return false;

            Lock lock = (Lock) obj;
            return getName().equals(lock.getName());
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

    Lock lock(String name) throws InterruptedException;

    void unlock(Lock lock);
}