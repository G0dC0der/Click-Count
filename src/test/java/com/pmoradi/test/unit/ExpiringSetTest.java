package com.pmoradi.test.unit;

import com.pmoradi.essentials.ExpiringSet;
import com.pmoradi.test.util.Randomization;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.await;
import static com.pmoradi.test.util.Parallelism.concurrentTasks;
import static com.pmoradi.test.util.Parallelism.sleep;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ExpiringSetTest {

    @Test
    public void verifyObjectExpires(){
        ExpiringSet<Object> set = new ExpiringSet<>();
        Object obj = new Object();

        set.put(obj, 100);
        assertFalse(set.hasExpired(obj));

        sleep(100);
        assertTrue(set.hasExpired(obj));
    }

    @Test
    public void verifyParallelObjectExpires() throws Throwable {
        ExpiringSet<Object> set = new ExpiringSet<>();

        concurrentTasks(1000, ()->{
            final int TIME = Randomization.randomInt(100) + 100;
            Object obj = new Object();

            set.put(obj, TIME);
            assertFalse(set.hasExpired(obj));

            sleep(TIME);
            assertTrue(set.hasExpired(obj));
        });
    }

    @Test
    public void verifyCleanerWorks() throws Throwable {
        ExpiringSet<Object> set = new ExpiringSet<>(Randomization.randomInt(500) + 100);

        concurrentTasks(1000, ()->{
            final int TIME = Randomization.randomInt(100) + 100;
            Object obj = new Object();

            set.put(obj, TIME);
            assertFalse(set.hasExpired(obj));

            await().atMost(1, TimeUnit.MINUTES).until(()-> set.size() == 0);
        });
    }

    @Test
    public void verifyAggressiveCleanerWorks() throws Throwable {
        ExpiringSet<Object> set = new ExpiringSet<>(1);

        concurrentTasks(1000, ()->{
            final int TIME = Randomization.randomInt(100) + 100;
            Object obj = new Object();

            set.put(obj, TIME);
            assertFalse(set.hasExpired(obj));

            await().atMost(1, TimeUnit.MINUTES).until(()-> set.size() == 0);
        });
    }

    @Test
    public void verifyCleanerThreadDiesWithOwner() {
        ExpiringSet<Object> set = new ExpiringSet<>(1);

        Thread cleaner = set.getCleanerThread();
        assertTrue(cleaner.isAlive());

        set = null;
        System.gc();

        await().atMost(1, TimeUnit.MINUTES).until(()-> !cleaner.isAlive());
    }

    @Test
    public void verifyCleanerThreadDiesWithOwnerWhileWorking() {
        ExpiringSet<Object> set = new ExpiringSet<>(5000);

        sleep(100);

        Thread cleaner = set.getCleanerThread();
        assertTrue(cleaner.isAlive());

        for(int i = 0; i < 1000; i++) {
            Object obj = new Object();
            set.put(obj, 1000);
        }

        while(set.size() > 950) {
            sleep(1);
        }

        set = null;
        System.gc();

        await().atMost(1, TimeUnit.MINUTES).until(()-> !cleaner.isAlive());
    }


    @Test
    public void verifyChecksWorksWhileCleaning() throws Throwable {
        ExpiringSet<Object> set = new ExpiringSet<>(1000);
        final int TIME = Randomization.randomInt(100) + 100;

        Object obj = new Object();
        set.put(obj, Integer.MAX_VALUE);

        concurrentTasks(1000, ()-> set.put(new Object(), TIME));
        sleep(1010);

        assertFalse(set.hasExpired(obj));
    }
}
