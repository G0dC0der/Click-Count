package com.pmoradi.test.concurrent;

import com.pmoradi.system.LockManager;
import com.pmoradi.system.Repository;
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
        String lockName = "group:mygroup";

        for(int i = 0; i < cases; i++){
            final int threadNumber = i;
            executor.submit(()->{
                try {
                    LockManager.Lock lock = manager.lock(lockName);
                    System.out.println("Lock grabbed by: " + threadNumber);

                    Thread.sleep(10);

                    System.out.println("Lock released by: " + threadNumber);
                    manager.unlock(lock);
                    completedThreads.incrementAndGet();
                } catch (InterruptedException e) {}
            });
        }

        while(completedThreads.get() < cases){
            Thread.sleep(2);
        }
    }
}
