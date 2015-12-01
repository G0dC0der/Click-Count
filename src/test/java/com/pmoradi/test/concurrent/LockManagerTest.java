package com.pmoradi.test.concurrent;

import com.pmoradi.system.LockManager;
import com.pmoradi.system.Repository;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class LockManagerTest {

    @Test
    public void testLockManager() throws InterruptedException {
        final int cases = 100;
        LockManager manager = Repository.getLockManager();
        ExecutorService executor = Executors.newFixedThreadPool(cases);
        AtomicInteger completedThreads = new AtomicInteger();
        AtomicInteger lockOwner = new AtomicInteger();
        AtomicBoolean fail = new AtomicBoolean(false);
        String lockName = "group:mygroup";

        for(int i = 0; i < cases; i++){
            final int threadNumber = i;
            executor.submit(()->{
                try {
                    LockManager.Key lock = manager.lock(lockName);
                    lockOwner.set(threadNumber);

                    Thread.sleep(1);

                    if(threadNumber != lockOwner.get())
                        fail.set(true);
                    manager.unlock(lock);
                    completedThreads.incrementAndGet();
                } catch (InterruptedException e) {}
            });
        }

        while(completedThreads.get() < cases){
            Thread.sleep(1);
        }

        Assert.assertTrue(!fail.get());
    }
}
