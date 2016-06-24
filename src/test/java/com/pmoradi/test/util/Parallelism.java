package com.pmoradi.test.util;

import com.pmoradi.essentials.Loop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Parallelism {

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void concurrentTasks(int runs, Runnable task) throws Throwable {
        AtomicReference<Throwable> ref = new AtomicReference<>();
        List<Thread> threads = new ArrayList<>(runs);

        Loop.limit(runs).each(()->{
            Thread thread = new Thread(()->{
                try {
                    task.run();
                } catch (Throwable t) {
                    ref.compareAndSet(null, t);
                }
            });
            threads.add(thread);
        });

        threads.forEach(Thread::start);

        for(Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {}
        }

        if(ref.get() != null) {
            throw ref.get();
        }
    }
}
