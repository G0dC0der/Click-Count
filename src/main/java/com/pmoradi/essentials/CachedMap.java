package com.pmoradi.essentials;

import org.apache.commons.collections4.map.PassiveExpiringMap;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Collections;

public class CachedMap {

    public static <K,V> Map<K,V> getCachedMap(int timeout){
        Map<K,V> map =  Collections.synchronizedMap(new PassiveExpiringMap<K, V>(timeout));
        WeakReference<Map<K,V>> ref = new WeakReference<>(map);

        Thread t = new Thread(()->{
            while(ref.get() != null){
                map.isEmpty();
                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        t.setDaemon(true);
        t.start();

        return map;
    }
}
